package net.quetzi.bluepower.part.tube;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.tube.IPneumaticTube;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.references.Dependencies;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TubeLogic implements IPneumaticTube {
    
    private final PneumaticTube tube;
    public World                world;
    public int                  x, y, z;
    public List<TubeStack>      tubeStacks = new ArrayList<TubeStack>();
    private static final double ITEM_SPEED = 0.04;
    
    public TubeLogic(PneumaticTube tube) {
    
        this.tube = tube;
    }
    
    public void update() {
    
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
                
                    //TODO
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
                } else if (!world.isRemote) {
                    
                    ItemStack remainder = IOHelper.insert(output, tubeStack.stack, tubeStack.heading.getOpposite(), false);
                    if (remainder != null) {
                        EntityItem entity = new EntityItem(world, 0.5 + tubeStack.heading.offsetX * tubeStack.progress * 0.5, 0.5 + tubeStack.heading.offsetY * tubeStack.progress * 0.5, 0.5 + tubeStack.heading.offsetX * tubeStack.progress * 0.5, remainder);
                        world.spawnEntityInWorld(entity);
                    }
                }
                iterator.remove();
            }
        }
    }
    
    @Override
    public ItemStack injectStack(ItemStack stack, ForgeDirection from, TubeColor itemColor, boolean simulate) {
    
        if (world.isRemote) throw new IllegalArgumentException("[Pneumatic Tube] You can't inject items from the client side!");
        TubeStack tubeStack = new TubeStack(stack, from, itemColor);
        tubeStacks.add(tubeStack);
        tube.sendUpdatePacket();
        return null;
    }
    
    public void writeToNBT(NBTTagCompound tag) {
    
        NBTTagList tagList = new NBTTagList();
        for (TubeStack stack : tubeStacks) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            tagList.appendTag(stackTag);
        }
        tag.setTag("tubeStacks", tagList);
    }
    
    public void readFromNBT(NBTTagCompound tag) {
    
        tubeStacks = new ArrayList<TubeStack>();
        NBTTagList tagList = tag.getTagList("tubeStacks", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound stackTag = tagList.getCompoundTagAt(i);
            tubeStacks.add(TubeStack.loadFromNBT(stackTag));
        }
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
    
}
