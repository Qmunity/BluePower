package net.quetzi.bluepower.tileentities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.helper.IOHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileMachineBase extends TileBase {

    protected boolean         spawnItemsInWorld       = true;
    protected List<ItemStack> internalItemStackBuffer = new ArrayList<ItemStack>();
    protected TileEntity tileAtOutput;

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!worldObj.isRemote) {
            if (getTicker() % 10 == 0 && !internalItemStackBuffer.isEmpty()) {
                if (IOHelper.canInterfaceWith(tileAtOutput, getFacingDirection())) {
                    for (Iterator<ItemStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext(); ) {
                        ItemStack itemStack = iterator.next();
                        ItemStack returnedStack = IOHelper.insert(tileAtOutput, itemStack, getFacingDirection(), false);
                        if (returnedStack == null) {
                            iterator.remove();
                            markDirty();
                        } else if (returnedStack.stackSize != itemStack.stackSize) {
                            markDirty();
                        }
                    }
                } else if (spawnItemsInWorld) {
                    ForgeDirection direction = getFacingDirection().getOpposite();
                    if (worldObj.isAirBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)) {
                        for (Iterator<ItemStack> iterator = internalItemStackBuffer.iterator(); iterator.hasNext(); ) {
                            ItemStack itemStack = iterator.next();
                            ejectItemInWorld(itemStack, direction);
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBlockNeighbourChanged() {

        super.onBlockNeighbourChanged();
        ForgeDirection direction = ForgeDirection.getOrientation(this.blockMetadata).getOpposite();
        if (direction != ForgeDirection.UNKNOWN) {
            tileAtOutput = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        } else {
            tileAtOutput = null;
        }
    }

    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("ItemBuffer", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);

            internalItemStackBuffer.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }

    public void writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        NBTTagList nbttaglist = new NBTTagList();

        for (ItemStack itemStack : internalItemStackBuffer) {
            if (itemStack != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                itemStack.writeToNBT(nbttagcompound1);
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
        System.out.println(internalItemStackBuffer);
        drops.addAll(internalItemStackBuffer);
        return drops;
    }
}
