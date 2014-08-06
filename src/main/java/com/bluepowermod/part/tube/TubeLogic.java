package com.bluepowermod.part.tube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.tube.IPneumaticTube;
import com.bluepowermod.api.tube.ITubeConnection;
import com.bluepowermod.api.tube.IWeightedTubeInventory;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.TileEntityCache;
import com.bluepowermod.util.Dependencies;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten
 */

public class TubeLogic implements IPneumaticTube {

    private final PneumaticTube tube;
    private TubeNode connectionNode; // contains a cache of connected TileEntities (not necessarily directly adjacent, but nodes, intersections, or
                                     // inventories). Also contains a colormask and distance.
    public List<TubeStack> tubeStacks = new ArrayList<TubeStack>();
    private int roundRobinCounter;

    public TubeLogic(PneumaticTube tube) {

        this.tube = tube;
    }

    public void clearNodeCaches() {

        List<PneumaticTube> clearedTubes = new ArrayList<PneumaticTube>();
        Stack<PneumaticTube> todoTubes = new Stack<PneumaticTube>();

        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);

        clearNodeCache();
        boolean firstRun = true;
        todoTubes.push(tube);

        while (!todoTubes.isEmpty()) {

            for (TileEntityCache cache : todoTubes.pop().getTileCache()) {
                PneumaticTube neighbor = compat.getBPPart(cache.getTileEntity(), PneumaticTube.class);
                if (neighbor != null) {
                    if (!clearedTubes.contains(neighbor)) {
                        neighbor.getLogic().clearNodeCache();
                        clearedTubes.add(neighbor);
                        if (firstRun || !neighbor.isCrossOver)
                            todoTubes.push(neighbor);
                    }
                }
            }
            firstRun = false;
        }

    }

    private void clearNodeCache() {

        connectionNode = null;
    }

    TubeNode getNode() {

        if (connectionNode == null && tube.getWorld() != null) {
            connectionNode = new TubeNode(tube);
            connectionNode.init();
        }
        return connectionNode;
    }

    public void update() {

        clearNodeCache();// TODO remove this line, which will enable node caching. Disabled for now, because I want to test stability with the
                         // non-cached system first.
        Iterator<TubeStack> iterator = tubeStacks.iterator();
        while (iterator.hasNext()) {
            TubeStack tubeStack = iterator.next();
            if (tubeStack.update()) {
                if (!tube.isCrossOver) {
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if (tube.connections[dir.ordinal()] && dir != tubeStack.heading.getOpposite()) {
                            tubeStack.heading = dir;
                            break;
                        }
                    }
                } else {// when we are at an intersection
                    if (!tube.getWorld().isRemote) {
                        Pair<ForgeDirection, TileEntity> heading = getHeadingForItem(tubeStack, false);
                        if (heading == null) {// if no valid destination
                            for (int i = 0; i < 6; i++) {
                                TubeEdge edge = getNode().edges[i];
                                if (edge != null) {
                                    tubeStack.heading = ForgeDirection.getOrientation(i);// this will allow the item to ignore the color mask when
                                                                                         // there's really no option left.
                                    if (canPassThroughMask(tubeStack.color, edge.colorMask)) {
                                        tubeStack.heading = ForgeDirection.getOrientation(i);// just a specific direction for now.
                                        break;
                                    }
                                }
                            }
                        } else {
                            tubeStack.heading = heading.getKey();
                        }
                        tube.sendUpdatePacket();
                    } else {
                        tubeStack.enabled = false;
                    }
                }
            } else if (tubeStack.progress >= 1) {// when the item reached the end of the tube.
                TileEntity output = tube.getTileCache()[tubeStack.heading.ordinal()].getTileEntity();
                IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
                PneumaticTube tube = compat.getBPPart(output, PneumaticTube.class);
                if (tube != null) {// we don't need to check connections, that's catched earlier.
                    TubeLogic logic = tube.getLogic();
                    tubeStack.progress = 0;
                    tubeStack.oldProgress = -TubeStack.ITEM_SPEED;
                    logic.tubeStacks.add(tubeStack);// transfer to another tube.
                    iterator.remove();
                } else if (!this.tube.getWorld().isRemote) {
                    ItemStack remainder = tubeStack.stack;
                    if (output instanceof ITubeConnection && ((ITubeConnection) output).isConnectedTo(tubeStack.heading.getOpposite())) {
                        TubeStack rem = ((ITubeConnection) output).acceptItemFromTube(tubeStack, tubeStack.heading.getOpposite(), false);
                        if (rem != null)
                            remainder = rem.stack;
                        else
                            remainder = null;
                    }
                    if (remainder != null)
                        remainder = IOHelper.insert(output, remainder, tubeStack.heading.getOpposite(), tubeStack.color, false);
                    if (remainder != null) {
                        if (injectStack(remainder, tubeStack.heading.getOpposite(), tubeStack.color, true)) {
                            tubeStack.stack = remainder;
                            tubeStack.progress = 0;
                            tubeStack.oldProgress = 0;
                            tubeStack.heading = tubeStack.heading.getOpposite();
                            this.tube.sendUpdatePacket();
                        } else {
                            EntityItem entity = new EntityItem(this.tube.getWorld(), this.tube.getX() + 0.5 + tubeStack.heading.offsetX
                                    * tubeStack.progress * 0.5, this.tube.getY() + 0.5 + tubeStack.heading.offsetY * tubeStack.progress * 0.5,
                                    this.tube.getZ() + 0.5 + tubeStack.heading.offsetX * tubeStack.progress * 0.5, remainder);
                            this.tube.getWorld().spawnEntityInWorld(entity);
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }

            }
        }
    }

    public boolean retrieveStack(TileEntity target, ForgeDirection dirToRetrieveInto, ItemStack filter) {

        return retrieveStack(target, dirToRetrieveInto, filter, TubeColor.NONE);
    }

    public boolean retrieveStack(TileEntity target, ForgeDirection dirToRetrieveInto, ItemStack filter, TubeColor color) {

        if (tube.getWorld() == null)
            return false;
        TubeStack stack = new TubeStack(filter, null, color);
        stack.setTarget(target, dirToRetrieveInto);

        Pair<ForgeDirection, TileEntity> result = getHeadingForItem(stack, false);
        if (result == null)
            return false;

        ItemStack extractedItem = null;
        if (filter != null) {
            extractedItem = IOHelper.extract(result.getValue(), result.getKey().getOpposite(), filter, true, false);
        } else {
            extractedItem = IOHelper.extract(result.getValue(), result.getKey().getOpposite(), false);
        }
        if (extractedItem == null)
            throw new IllegalArgumentException("This isn't possible!");

        stack = new TubeStack(extractedItem, result.getKey().getOpposite(), color);
        stack.setTarget(target, dirToRetrieveInto);

        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
        TileEntity te = tube.getWorld().getTileEntity(result.getValue().xCoord - result.getKey().offsetX,
                result.getValue().yCoord - result.getKey().offsetY, result.getValue().zCoord - result.getKey().offsetZ);
        PneumaticTube tube = compat.getBPPart(te, PneumaticTube.class);
        if (tube == null)
            throw new IllegalArgumentException("wieeeeerd!");
        return tube.getLogic().injectStack(stack, result.getKey().getOpposite(), false);
    }

    /**
     * This method gets the end target and heading for a TubeStack. When the tubestack's target variable is null, this is an exporting item, meaning
     * the returned target will be the TileEntity the item is going to transport to. When the tubestack's target variable is not not, the item is
     * being retrieved to this inventory. The returned target is the inventory the item came/should come from.
     * 
     * @param simulate
     *            The only difference between simulate and not simulate is the fact that the round robin handling will be updated in non-simulate.
     * @param from
     *            The direction this item came from, this direction will never be a valid heading. Is null in normal item routing, as the from
     *            direction IS a valid output.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Pair<ForgeDirection, TileEntity> getHeadingForItem(TubeStack stack, boolean simulate) {

        Map<TubeNode, Integer> distances = new HashMap<TubeNode, Integer>();
        Queue<TubeNode> traversingNodes = new LinkedBlockingQueue<TubeNode>();
        Queue<ForgeDirection> trackingExportDirection = new LinkedBlockingQueue<ForgeDirection>();
        Map<TubeEdge, ForgeDirection> validDestinations = new LinkedHashMap<TubeEdge, ForgeDirection>();// using a LinkedHashMap so the order doesn't
                                                                                                        // change, used for round robin.

        distances.put(getNode(), 0);// make this the origin.
        traversingNodes.add(getNode());

        boolean firstRun = true;
        int closestDest = 0;
        while (!traversingNodes.isEmpty()) {
            TubeNode node = traversingNodes.poll();
            ForgeDirection heading = firstRun ? null : trackingExportDirection.poll();
            for (int i = 0; i < 6; i++) {
                if (firstRun)
                    heading = ForgeDirection.getOrientation(i);
                TubeEdge edge = node.edges[i];
                if (edge != null && canPassThroughMask(stack.color, edge.colorMask)) {// if this item can travel through this color mask proceed.
                    Integer distance = distances.get(edge.target);
                    if (distance == null || distances.get(node) + edge.distance < distance) {
                        distances.put(edge.target, distances.get(node) + edge.distance);
                        if (edge.target.target instanceof PneumaticTube) {
                            traversingNodes.add(edge.target);
                            trackingExportDirection.add(heading);
                        } else if (stack.getTarget(tube.getWorld()) == null && edge.isValidForExportItem(stack.stack) || stack.heading == null
                                && edge.isValidForImportItem(stack.stack) || stack.heading != null
                                && stack.getTarget(tube.getWorld()) == edge.target.target
                                && edge.targetConnectionSide.getOpposite() == stack.getTargetEntryDir()) {
                            validDestinations.put(edge, stack.heading == null ? edge.targetConnectionSide : heading);
                        }
                    }
                }
            }

            // Check the distances of the current breadth first search layer. if no points are closer than the currently valid destination(s), we're
            // done searching.
            boolean isDoneSearching = true;
            closestDest = getClosestDestination(validDestinations.keySet(), distances);
            for (TubeNode checkingNode : traversingNodes) {
                if (distances.get(checkingNode) <= closestDest) {
                    isDoneSearching = false;
                    break;
                }
            }
            if (isDoneSearching)
                break;
            firstRun = false;
        }

        if (validDestinations.size() == 0) {
            if (stack.getTarget(tube.getWorld()) != null && stack.heading != null && !simulate) {
                stack.setTarget(null, ForgeDirection.UNKNOWN);// if we can't reach the retrieving target anymore, reroute as normal.
                return getHeadingForItem(stack, simulate);
            } else {
                return null;
            }
        }

        List<Pair<ForgeDirection, TileEntity>> validDirections = new ArrayList<Pair<ForgeDirection, TileEntity>>();
        for (Map.Entry<TubeEdge, ForgeDirection> entry : validDestinations.entrySet()) {
            if (distances.get(entry.getKey().target) == closestDest) {
                validDirections.add(new ImmutablePair(entry.getValue(), entry.getKey().target.target));
            }
        }

        // handle round robin
        if (!simulate)
            roundRobinCounter++;
        if (roundRobinCounter >= validDirections.size())
            roundRobinCounter = 0;
        return validDirections.get(roundRobinCounter);
    }

    private boolean canPassThroughMask(TubeColor color, int colorMask) {

        return color == TubeColor.NONE || Integer.bitCount(colorMask) == 0 || Integer.bitCount(colorMask) == 1
                && (colorMask & 1 << color.ordinal()) != 0;
    }

    /**
     * Used to get an indication for when the search is done.
     * 
     * @param validDestinations
     * @param distances
     * @return
     */
    private int getClosestDestination(Set<TubeEdge> validDestinations, Map<TubeNode, Integer> distances) {

        int minDest = Integer.MAX_VALUE;
        for (TubeEdge edge : validDestinations) {
            if (distances.get(edge.target) < minDest) {
                minDest = distances.get(edge.target);
            }
        }
        return minDest;
    }

    @Override
    public boolean injectStack(ItemStack stack, ForgeDirection from, TubeColor itemColor, boolean simulate) {

        return injectStack(new TubeStack(stack.copy(), from, itemColor), from, simulate);
    }

    public boolean injectStack(TubeStack stack, ForgeDirection from, boolean simulate) {

        if (tube.getWorld().isRemote)
            throw new IllegalArgumentException("[Pneumatic Tube] You can't inject items from the client side!");
        Pair<ForgeDirection, TileEntity> heading = getHeadingForItem(stack, simulate);
        if (heading != null && heading.getKey() != from.getOpposite()) {
            if (!simulate) {
                tubeStacks.add(stack);
                tube.sendUpdatePacket();
            }
            return true;
        } else {
            return false;
        }
    }

    public void writeToNBT(NBTTagCompound tag) {

        NBTTagList tagList = new NBTTagList();
        for (TubeStack stack : tubeStacks) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            tagList.appendTag(stackTag);
        }
        tag.setTag("tubeStacks", tagList);

        tag.setInteger("roundRobinCounter", roundRobinCounter);
    }

    public void readFromNBT(NBTTagCompound tag) {

        tubeStacks = new ArrayList<TubeStack>();
        NBTTagList tagList = tag.getTagList("tubeStacks", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound stackTag = tagList.getCompoundTagAt(i);
            tubeStacks.add(TubeStack.loadFromNBT(stackTag));
        }

        roundRobinCounter = tag.getInteger("roundRobinCounter");
    }

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float partialTick) {

        GL11.glPushMatrix();
        GL11.glTranslated(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        for (TubeStack stack : tubeStacks) {
            stack.render(partialTick);
        }
        GL11.glPopMatrix();
    }

    /**
     * Contains the distance and a reference to a connected node, directional.
     */
    public class TubeNode {

        public TubeEdge[] edges;
        public Object target; // Either a TileEntity (inventory), or a PneumaticTube

        public TubeNode(TileEntity te) {

            target = te;
        }

        public TubeNode(PneumaticTube tube) {

            target = tube;
        }

        public void init() {

            PneumaticTube nodeTube = (PneumaticTube) target;
            edges = new TubeEdge[6];
            for (int i = 0; i < 6; i++) {
                if (tube.connections[i]) {
                    TileEntity neighbor = nodeTube.getTileCache()[i].getTileEntity();

                    IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
                    PneumaticTube tube = compat.getBPPart(neighbor, PneumaticTube.class);

                    int colorMask = nodeTube.getColor(ForgeDirection.getOrientation(i)) != TubeColor.NONE ? 1 << nodeTube.getColor(
                            ForgeDirection.getOrientation(i)).ordinal() : 0;
                    if (tube != null) {
                        int dist = tube.getWeigth();
                        if (tube.getColor(ForgeDirection.getOrientation(i).getOpposite()) != TubeColor.NONE)
                            colorMask = colorMask | 1 << tube.getColor(ForgeDirection.getOrientation(i).getOpposite()).ordinal();
                        ForgeDirection curDir = ForgeDirection.getOrientation(i);
                        while (!tube.isCrossOver && tube.initialized) {// traverse the tubes
                            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                                if (dir != curDir.getOpposite() && tube.connections[dir.ordinal()]) {
                                    curDir = dir;
                                    break;
                                }
                            }
                            neighbor = tube.getTileCache()[curDir.ordinal()].getTileEntity();
                            if (neighbor != null) {
                                if (tube.getColor(curDir) != TubeColor.NONE) {
                                    colorMask = colorMask | 1 << tube.getColor(curDir).ordinal();
                                }
                                tube = compat.getBPPart(neighbor, PneumaticTube.class);
                                if (tube == null) {
                                    edges[i] = new TubeEdge(new TubeNode(neighbor), curDir, colorMask,
                                            dist
                                                    + (neighbor instanceof IWeightedTubeInventory ? ((IWeightedTubeInventory) neighbor)
                                                            .getWeight(curDir) : 0));
                                    break;
                                } else {
                                    if (!tube.initialized)
                                        break;
                                    dist += tube.getWeigth();
                                    if (tube.getColor(curDir.getOpposite()) != TubeColor.NONE) {
                                        colorMask = colorMask | 1 << tube.getColor(curDir.getOpposite()).ordinal();
                                    }
                                }
                            }
                        }
                        if (tube != null && tube != nodeTube)
                            edges[i] = new TubeEdge(tube.getLogic().getNode(), curDir, colorMask, dist);// only add an edge that isn't just connected
                                                                                                        // to itself.

                    } else if (neighbor != null) {
                        edges[i] = new TubeEdge(new TubeNode(neighbor), ForgeDirection.getOrientation(i), colorMask,
                                neighbor instanceof IWeightedTubeInventory ? ((IWeightedTubeInventory) neighbor).getWeight(ForgeDirection
                                        .getOrientation(i)) : 0);
                    }
                }
            }
        }
    }

    public class TubeEdge {

        public TubeNode target;
        private final ForgeDirection targetConnectionSide;
        public final int distance;
        public int colorMask; // bitmask of disallowed colored items through the tube. Least significant bit is TubeColor.values()[0]. only least
                              // significant 16 bits are used

        public TubeEdge(TubeNode target, ForgeDirection targetConnectionSide, int colorMask, int distance) {

            this.target = target;
            this.targetConnectionSide = targetConnectionSide;
            this.distance = distance;
            this.colorMask = colorMask;
        }

        public boolean isValidForExportItem(ItemStack stack) {

            if (target.target instanceof PneumaticTube)
                return false;
            if (target.target instanceof IWeightedTubeInventory && ((IWeightedTubeInventory) target.target).getWeight(targetConnectionSide) > 10000)
                return true;
            ItemStack remainder = IOHelper.insert((TileEntity) target.target, stack.copy(), targetConnectionSide.getOpposite(), true);
            return remainder == null || remainder.stackSize < stack.stackSize;
        }

        public boolean isValidForImportItem(ItemStack stack) {

            if (target.target instanceof PneumaticTube)
                return false;

            if (stack != null) {
                return IOHelper.extract((TileEntity) target.target, targetConnectionSide.getOpposite(), stack, true, true) != null;
            } else {
                return IOHelper.extract((TileEntity) target.target, targetConnectionSide.getOpposite(), true) != null;
            }
        }
    }

}
