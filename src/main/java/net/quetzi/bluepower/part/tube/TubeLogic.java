package net.quetzi.bluepower.part.tube;

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
import net.quetzi.bluepower.api.tube.IPneumaticTube;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.helper.TileEntityCache;
import net.quetzi.bluepower.references.Dependencies;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten
 */

public class TubeLogic implements IPneumaticTube {
    
    private final PneumaticTube tube;
    private TubeNode            connectionNode;                         //contains a cache of connected TileEntities (not necessarily directly adjacent, but nodes, intersections, or inventories). Also contains a colormask and distance.
    public List<TubeStack>      tubeStacks = new ArrayList<TubeStack>();
    private static final double ITEM_SPEED = 0.04;
    private int                 roundRobinCounter;
    
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
                        if (firstRun || !neighbor.isCrossOver) todoTubes.push(neighbor);
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
    
        if (connectionNode == null && tube.world != null) {
            connectionNode = new TubeNode(tube);
            connectionNode.init();
        }
        return connectionNode;
    }
    
    public void update() {
    
        clearNodeCache();
        Iterator<TubeStack> iterator = tubeStacks.iterator();
        while (iterator.hasNext()) {
            TubeStack tubeStack = iterator.next();
            if (tubeStack.update(ITEM_SPEED)) {
                if (!tube.isCrossOver) {
                    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                        if (tube.connections[dir.ordinal()] && dir != tubeStack.heading.getOpposite()) {
                            tubeStack.heading = dir;
                            break;
                        }
                    }
                } else {//when we are at an intersection
                    if (!tube.world.isRemote) {
                        Pair<ForgeDirection, TileEntity> heading = getHeadingForItem(tubeStack, false);
                        if (heading == null) {//if no valid destination
                            for (int i = 0; i < 6; i++) {
                                if (tube.connections[i]) {
                                    tubeStack.heading = ForgeDirection.getOrientation(i);//just a specific direction for now.
                                    break;
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
            } else if (tubeStack.progress >= 1) {//when the item reached the end of the tube.
                TileEntity output = tube.getTileCache()[tubeStack.heading.ordinal()].getTileEntity();
                IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
                PneumaticTube tube = compat.getBPPart(output, PneumaticTube.class);
                if (tube != null) {//we don't need to check connections, that's catched earlier.
                    TubeLogic logic = tube.getLogic();
                    tubeStack.progress = 0;
                    tubeStack.oldProgress = -ITEM_SPEED;
                    logic.tubeStacks.add(tubeStack);//transfer to another tube.
                } else if (!this.tube.world.isRemote) {
                    ItemStack remainder = IOHelper.insert(output, tubeStack.stack, tubeStack.heading.getOpposite(), tubeStack.color, false);
                    if (remainder != null /*&& !injectStack(remainder, tubeStack.heading.getOpposite(), tubeStack.color, false)*/) {
                        EntityItem entity = new EntityItem(this.tube.world, this.tube.x + 0.5 + tubeStack.heading.offsetX * tubeStack.progress * 0.5, this.tube.y + 0.5 + tubeStack.heading.offsetY * tubeStack.progress * 0.5, this.tube.z + 0.5 + tubeStack.heading.offsetX * tubeStack.progress * 0.5,
                                remainder);
                        this.tube.world.spawnEntityInWorld(entity);
                    }
                }
                iterator.remove();
            }
        }
    }
    
    /**
    This method gets the end target and heading for a TubeStack.
    When the tubestack's target variable is null, this is an exporting item, meaning the returned target will be the TileEntity the item is going to transport to.
    When the tubestack's target variable is not not, the item is being retrieved to this inventory. The returned target is the inventory the item came/should come from.
    @param simulate The only difference between simulate and not simulate is the fact that the round robin handling will be updated in non-simulate.

    */
    private Pair<ForgeDirection, TileEntity> getHeadingForItem(TubeStack stack, boolean simulate) {
    
        Map<TubeNode, Integer> distances = new HashMap<TubeNode, Integer>();
        Queue<TubeNode> traversingNodes = new LinkedBlockingQueue<TubeNode>();
        Queue<ForgeDirection> trackingExportDirection = new LinkedBlockingQueue<ForgeDirection>();
        Map<TubeEdge, ForgeDirection> validDestinations = new LinkedHashMap<TubeEdge, ForgeDirection>();//using a LinkedHashMap so the order doesn't change, used for round robin.
        
        distances.put(getNode(), 0);//make this the origin.
        traversingNodes.add(getNode());
        
        boolean firstRun = true;
        int closestDest = 0;
        while (!traversingNodes.isEmpty()) {
            TubeNode node = traversingNodes.poll();
            ForgeDirection heading = firstRun ? null : trackingExportDirection.poll();
            for (int i = 0; i < 6; i++) {
                if (firstRun) heading = ForgeDirection.getOrientation(i);
                TubeEdge edge = node.edges[i];
                if (edge != null && (edge.colorMask & 1 << stack.color.ordinal()) == 0) {//if this item can travel through this color mask proceed. If the tubestack's color == NONE, the bitshift will go beyond the mask, and returns 0.
                    Integer distance = distances.get(edge.target);
                    if (distance == null || distances.get(node) + edge.distance < distance) {
                        distances.put(edge.target, distances.get(node) + edge.distance);
                        if (edge.target.target instanceof PneumaticTube) {
                            traversingNodes.add(edge.target);
                            trackingExportDirection.add(heading);
                        } else if (stack.getTarget(tube.world) == null && edge.isValidForExportItem(stack.stack) || stack.getTarget(tube.world) != null && edge.isValidForImportItem(stack.stack)) {
                            validDestinations.put(edge, heading);
                        }
                    }
                }
            }
            
            //Check the distances of the current breadth first search layer. if no points are closer than the currently valid destination(s), we're done searching.
            boolean isDoneSearching = true;
            closestDest = getClosestDestination(validDestinations.keySet(), distances);
            for (TubeNode checkingNode : traversingNodes) {
                if (distances.get(checkingNode) <= closestDest) {
                    isDoneSearching = false;
                    break;
                }
            }
            if (isDoneSearching) break;
            firstRun = false;
        }
        
        if (validDestinations.size() == 0) {
            if (stack.getTarget(tube.world) != null && !simulate) {
                stack.setTarget(null);//if we can't reach the retrieving target anymore, reroute as normal.
                return getHeadingForItem(stack, simulate);
            } else {
                return null;
            }
        }
        
        List<Pair<ForgeDirection, TileEntity>> validDirections = new ArrayList<Pair<ForgeDirection, TileEntity>>();
        for (Map.Entry<TubeEdge, ForgeDirection> entry : validDestinations.entrySet()) {
            if (distances.get(entry.getKey().target) == closestDest) {
                validDirections.add(new ImmutablePair(entry.getValue(), entry.getKey().target));
            }
        }
        
        //handle round robin
        if (!simulate) roundRobinCounter++;
        if (roundRobinCounter >= validDirections.size()) roundRobinCounter = 0;
        return validDirections.get(roundRobinCounter);
    }
    
    /**
     * Used to get an indication for when the search is done.
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
    
        if (tube.world.isRemote) throw new IllegalArgumentException("[Pneumatic Tube] You can't inject items from the client side!");
        TubeStack tubeStack = new TubeStack(stack.copy(), from, itemColor);
        Pair<ForgeDirection, TileEntity> heading = getHeadingForItem(tubeStack, simulate);
        if (heading != null && heading.getKey() != from.getOpposite()) {
            if (!simulate) {
                tubeStacks.add(tubeStack);
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
    Contains the distance and a reference to a connected node, directional.
    */
    public class TubeNode {
        
        public TubeEdge[] edges;
        public Object     target; //Either a TileEntity (inventory), or a PneumaticTube
                                  
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
                    
                    if (tube != null) {
                        int dist = tube.getWeigth();
                        short colorMask = tube.getColor() != TubeColor.NONE ? (short) (1 << tube.getColor().ordinal()) : (short) 0;
                        ForgeDirection curDir = ForgeDirection.getOrientation(i);
                        while (!tube.isCrossOver && tube.initialized) {//traverse the tubes
                            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                                if (dir != curDir.getOpposite() && tube.connections[dir.ordinal()]) {
                                    curDir = dir;
                                    break;
                                }
                            }
                            neighbor = tube.getTileCache()[curDir.ordinal()].getTileEntity();
                            if (neighbor != null) {
                                tube = compat.getBPPart(neighbor, PneumaticTube.class);
                                if (tube == null) {
                                    edges[i] = new TubeEdge(new TubeNode(neighbor), curDir, colorMask, dist + (neighbor instanceof IWeightedTubeInventory ? ((IWeightedTubeInventory) neighbor).getWeigth(curDir) : 0));
                                    break;
                                } else {
                                    if (!tube.initialized) break;
                                    dist += tube.getWeigth();
                                    if (tube.getColor() != TubeColor.NONE) colorMask = (short) (colorMask | 1 << tube.getColor().ordinal());
                                }
                            }
                        }
                        if (tube != null && tube != nodeTube) edges[i] = new TubeEdge(tube.getLogic().getNode(), curDir, colorMask, dist);//only add an edge that isn't just connected to itself.
                        
                    } else if (neighbor != null) {
                        edges[i] = new TubeEdge(new TubeNode(neighbor), ForgeDirection.getOrientation(i), (short) 0, neighbor instanceof IWeightedTubeInventory ? ((IWeightedTubeInventory) neighbor).getWeigth(ForgeDirection.getOrientation(i)) : 0);
                    }
                }
            }
        }
    }
    
    public class TubeEdge {
        
        public TubeNode              target;
        private final ForgeDirection targetConnectionSide;
        public final int             distance;
        public final short           colorMask;           //bitmask of disallowed colored items through the tube. Least significant bit is TubeColor.values()[0].
                                                           
        public TubeEdge(TubeNode target, ForgeDirection targetConnectionSide, short colorMask, int distance) {
        
            this.target = target;
            this.targetConnectionSide = targetConnectionSide;
            this.distance = distance;
            this.colorMask = colorMask;
        }
        
        public boolean isValidForExportItem(ItemStack stack) {
        
            if (target.target instanceof PneumaticTube) return false;
            ItemStack remainder = IOHelper.insert((TileEntity) target.target, stack.copy(), targetConnectionSide.getOpposite(), true);
            return remainder == null || remainder.stackSize < stack.stackSize;
        }
        
        public boolean isValidForImportItem(ItemStack stack) {
        
            if (target.target instanceof PneumaticTube) return false;
            ItemStack extractedItems = IOHelper.extract((TileEntity) target.target, targetConnectionSide, stack, true);
            return extractedItems != null;
        }
    }
    
}
