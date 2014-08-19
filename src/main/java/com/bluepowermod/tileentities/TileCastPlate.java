package com.bluepowermod.tileentities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.cast.ICastRegistry;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.items.ItemCast;

public class TileCastPlate extends TileBase implements IInventory {

    private static ICastRegistry reg = BPApi.getInstance().getCastRegistry();

    private ItemStack template = null;
    private ItemStack cast = null;
    private boolean hasClay = false;

    private double cookProgress = 0;

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) {

        if (world.isRemote)
            return true;

        ItemStack held = player.getCurrentEquippedItem();

        if (cookProgress > 0 && cookProgress != 1) {

            if (held != null && held.getItem() instanceof ItemBlock && Block.getBlockFromItem(held.getItem()) == Blocks.clay)
                return true;

            return false;
        }

        if (cast != null) {
            IOHelper.spawnItemInWorld(world, cast, x, y, z);
            cast = null;
            sendUpdatePacket();
            cookProgress = 0;
            return true;
        } else {
            if (template == null) {
                if (held == null) {
                    if (hasClay) {
                        IOHelper.spawnItemInWorld(world, template, x, y, z);
                        template = null;
                        sendUpdatePacket();
                        return true;
                    }
                } else {
                    if (reg.getCreatedCast(held) != null) {
                        template = new ItemStack(held.getItem(), 1, held.getItemDamage());
                        template.stackTagCompound = held.stackTagCompound;
                        if (!player.capabilities.isCreativeMode)
                            held.stackSize--;
                        sendUpdatePacket();
                        return true;
                    }
                }
            } else if (!hasClay) {
                if (held != null && held.getItem() instanceof ItemBlock && Block.getBlockFromItem(held.getItem()) == Blocks.clay) {
                    hasClay = true;
                    if (!player.capabilities.isCreativeMode)
                        held.stackSize--;
                    sendUpdatePacket();
                    return true;
                } else {
                    IOHelper.spawnItemInWorld(world, template, x, y, z);
                    template = null;
                    sendUpdatePacket();
                    return true;
                }
            }
        }

        if (held != null && held.getItem() instanceof ItemBlock && Block.getBlockFromItem(held.getItem()) == Blocks.clay)
            return true;

        return false;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (worldObj.isRemote)
            return;

        if (hasClay() && hasTemplate() && cookProgress < 1) {
            Vector3 v = new Vector3(this).getRelative(ForgeDirection.DOWN);
            if (reg.isCookingHeatSource(v.getBlock(), v.getBlockMeta())) {
                cookProgress += (1 / 3D) / 20D;
                sendUpdatePacket();
            }
        }
        if (cookProgress >= 1) {
            cookProgress = 1;
            if (hasClay) {
                hasClay = false;
                cast = ItemCast.createCast(reg.getCreatedCast(template));
                sendUpdatePacket();
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        if (slot == 0)
            return template;
        else if (slot == 1)
            return cast;

        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {

        if (slot == 0)
            template = null;
        else if (slot == 1)
            cast = null;

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {

        return getStackInSlot(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {

        if (slot == 0)
            template = stack;
        else if (slot == 1)
            cast = stack;
    }

    @Override
    public String getInventoryName() {

        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {

        return false;
    }

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {

        super.writeToPacketNBT(tCompound);

        for (int i = 0; i < 3; i++) {
            ItemStack is = getStackInSlot(i);
            if (is != null) {
                NBTTagCompound tc = new NBTTagCompound();
                is.writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }

        tCompound.setBoolean("hasClay", hasClay);
        tCompound.setDouble("cookProgress", cookProgress);
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {

        super.readFromPacketNBT(tCompound);

        for (int i = 0; i < 2; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tc));
        }

        hasClay = tCompound.getBoolean("hasClay");
        cookProgress = tCompound.getDouble("cookProgress");
    }

    public boolean hasClay() {

        return hasClay;
    }

    public boolean hasTemplate() {

        return template != null;
    }

    public ItemStack getTemplate() {

        return template;
    }

    public boolean hasCast() {

        return cast != null;
    }

    public ItemStack getCast() {

        return cast;
    }

    public double getCookProgress() {

        return cookProgress;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = new ArrayList<ItemStack>();

        if (hasClay())
            drops.add(new ItemStack(Blocks.clay));
        if (template != null)
            drops.add(template);
        if (cast != null)
            drops.add(cast);

        return drops;
    }

}
