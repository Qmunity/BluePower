package net.quetzi.bluepower.tileentities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.tube.IPneumaticTube.TubeColor;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.helper.TileEntityCache;
import net.quetzi.bluepower.part.tube.ITubeConnection;
import net.quetzi.bluepower.part.tube.IWeightedTubeInventory;
import net.quetzi.bluepower.part.tube.TubeStack;

public class TileMachineBase extends TileBase implements ITubeConnection, IWeightedTubeInventory {
    
    protected boolean             spawnItemsInWorld       = true;
    protected boolean             acceptsTubeItems        = true;
    private final List<TubeStack> internalItemStackBuffer = new ArrayList<TubeStack>();
    private TileEntityCache[]     tileCache;
    public static final int       BUFFER_EMPTY_INTERVAL   = 10;
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        
        if (!worldObj.isRemote) {
            if (getTicker() % BUFFER_EMPTY_INTERVAL == 0 && !internalItemStackBuffer.isEmpty()) {
                if (IOHelper.canInterfaceWith(getTileCache()[getOutputDirection().ordinal()].getTileEntity(), getFacingDirection())) {
                    TubeStack tubeStack = internalItemStackBuffer.get(0);
                    ItemStack returnedStack = IOHelper.insert(getTileCache()[getOutputDirection().ordinal()].getTileEntity(), tubeStack.stack, getFacingDirection(), tubeStack.color, false);
                    if (returnedStack == null) {
                        internalItemStackBuffer.remove(0);
                        onItemOutputted();
                        markDirty();
                    } else if (returnedStack.stackSize != tubeStack.stack.stackSize) {
                        onItemOutputted();
                        markDirty();
                    }
                }
            } else if (spawnItemsInWorld) {
                ForgeDirection direction = getFacingDirection().getOpposite();
                if (worldObj.isAirBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)) {
                    for (Iterator<TubeStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext();) {
                        ItemStack itemStack = iterator.next().stack;
                        ejectItemInWorld(itemStack, direction);
                        onItemOutputted();
                        iterator.remove();
                    }
                }
            }
        }
    }
    
    @Override
    public void onBlockNeighbourChanged() {
    
        super.onBlockNeighbourChanged();
        for (TileEntityCache cache : getTileCache()) {
            cache.update();
        }
    }
    
    protected void addItemToOutputBuffer(ItemStack stack, TubeColor color) {
    
        if (!worldObj.isRemote) internalItemStackBuffer.add(new TubeStack(stack, getOutputDirection().getOpposite(), color));
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
    
    /**
     * Called when an item is outputted from this block, can be used for animation (See Sortron)
     */
    protected void onItemOutputted() {
    
    }
    
    public TileEntityCache[] getTileCache() {
    
        if (tileCache == null) {
            tileCache = TileEntityCache.getDefaultCache(worldObj, xCoord, yCoord, zCoord);
        }
        return tileCache;
    }
    
    public ForgeDirection getOutputDirection() {
    
        return ForgeDirection.getOrientation(getBlockMetadata()).getOpposite();
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
    
        ForgeDirection dir = getOutputDirection();
        return from == dir.getOpposite() || acceptsTubeItems && from == dir;
    }
    
    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {
    
        if (!simulate) internalItemStackBuffer.add(stack);
        return null;
    }
    
    @Override
    public int getWeight(ForgeDirection from) {
    
        return from == getOutputDirection().getOpposite() ? 1000000 : 0;//make the buffer side the last place to go
    }
}
