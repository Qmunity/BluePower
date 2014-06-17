package net.quetzi.bluepower.tileentities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.tube.IPneumaticTube.TubeColor;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.part.tube.ITubeConnection;
import net.quetzi.bluepower.part.tube.TubeStack;

public class TileMachineBase extends TileBase implements ITubeConnection {
    
    protected boolean             spawnItemsInWorld       = true;
    private final List<TubeStack> internalItemStackBuffer = new ArrayList<TubeStack>();
    protected TileEntity          tileAtOutput;
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        
        if (!worldObj.isRemote) {
            if (getTicker() % 10 == 0 && !internalItemStackBuffer.isEmpty()) {
                if (IOHelper.canInterfaceWith(tileAtOutput, getFacingDirection())) {
                    for (Iterator<TubeStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext();) {
                        TubeStack tubeStack = iterator.next();
                        ItemStack returnedStack = IOHelper.insert(tileAtOutput, tubeStack.stack, getFacingDirection(), tubeStack.color, false);
                        if (returnedStack == null) {
                            iterator.remove();
                            markDirty();
                        } else if (returnedStack.stackSize != tubeStack.stack.stackSize) {
                            markDirty();
                        }
                    }
                } else if (spawnItemsInWorld) {
                    ForgeDirection direction = getFacingDirection().getOpposite();
                    if (worldObj.isAirBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)) {
                        for (Iterator<TubeStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext();) {
                            ItemStack itemStack = iterator.next().stack;
                            ejectItemInWorld(itemStack, direction);
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }
    
    protected void addItemToOutputBuffer(ItemStack stack, TubeColor color) {
    
        internalItemStackBuffer.add(new TubeStack(stack, ForgeDirection.getOrientation(blockMetadata).getOpposite(), color));
    }
    
    protected void addItemToOutputBuffer(ItemStack stack) {
    
        addItemToOutputBuffer(stack, TubeColor.NONE);
        
    }
    
    protected void addItemsToOutputBuffer(Iterable<ItemStack> stacks) {
    
        addItemsToOutputBuffer(stacks, TubeColor.NONE);
    }
    
    protected void addItemsToOutputBuffer(Iterable<ItemStack> stacks, TubeColor color) {
    
        for (ItemStack stack : stacks) {
            addItemToOutputBuffer(stack, color);
        }
    }
    
    protected boolean isBufferEmpty() {
    
        return internalItemStackBuffer.isEmpty();
    }
    
    @Override
    public void onBlockNeighbourChanged() {
    
        super.onBlockNeighbourChanged();
        ForgeDirection direction = ForgeDirection.getOrientation(getBlockMetadata()).getOpposite();
        if (direction != ForgeDirection.UNKNOWN) {
            tileAtOutput = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        } else {
            tileAtOutput = null;
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("ItemBuffer", 10);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            
            internalItemStackBuffer.add(TubeStack.loadFromNBT(nbttagcompound1));
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound compound) {
    
        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();
        
        for (TubeStack tubeStack : internalItemStackBuffer) {
            if (tubeStack != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                tubeStack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        
        compound.setTag("ItemBuffer", nbttaglist);
    }
    
    public void ejectItemInWorld(ItemStack stack, ForgeDirection oppDirection) {
    
        float spawnX = xCoord + 0.5F + oppDirection.offsetX * 0.8F;
        float spawnY = yCoord + 0.5F + oppDirection.offsetY * 0.8F;
        float spawnZ = zCoord + 0.5F + oppDirection.offsetZ * 0.8F;
        
        EntityItem droppedItem = new EntityItem(worldObj, spawnX, spawnY, spawnZ, stack);
        
        droppedItem.motionX = oppDirection.offsetX * 0.20F;
        droppedItem.motionY = oppDirection.offsetY * 0.20F;
        droppedItem.motionZ = oppDirection.offsetZ * 0.20F;
        
        worldObj.spawnEntityInWorld(droppedItem);
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (TubeStack stack : internalItemStackBuffer)
            drops.add(stack.stack);
        return drops;
    }
    
    @Override
    public boolean isConnectedTo(ForgeDirection from) {
    
        return from == ForgeDirection.getOrientation(blockMetadata).getOpposite();
    }
    
    @Override
    public void acceptItemFromTube(TubeStack stack, ForgeDirection from) {
    
        internalItemStackBuffer.add(stack);
    }
}
