package com.bluepowermod.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.bluepowermod.api.item.IMachineBlueprint;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMachineBlueprint extends ItemBase implements IMachineBlueprint {

    @SideOnly(Side.CLIENT)
    private IIcon empty;
    @SideOnly(Side.CLIENT)
    private IIcon withdata;

    public ItemMachineBlueprint() {

        this.setCreativeTab(BPCreativeTabs.tools);
        this.setUnlocalizedName(Refs.MACHINE_BLUEPRINT_NAME);
        this.setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        itemIcon = empty = reg.registerIcon(Refs.MODID + ":blueprint_empty");
        withdata = reg.registerIcon(Refs.MODID + ":blueprint_filled");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {

        return damage == 1 ? withdata : empty;
    }

    @Override
    public int getDamage(ItemStack stack) {

        return getStoredSettings(stack) != null ? 1 : 0;
    }

    @Override
    public void saveMachineSettings(ItemStack blueprint, String type, NBTTagCompound settings) {

        blueprint.stackTagCompound = new NBTTagCompound();
        blueprint.stackTagCompound.setString("type", type);
        blueprint.stackTagCompound.setTag("settings", settings);
    }

    @Override
    public void clearSettings(ItemStack blueprint) {

        blueprint.stackTagCompound = null;
    }

    @Override
    public String getStoredSettingsType(ItemStack blueprint) {

        return blueprint.stackTagCompound == null || !blueprint.stackTagCompound.hasKey("type") ? null : blueprint.stackTagCompound.getString("type");
    }

    @Override
    public NBTTagCompound getStoredSettings(ItemStack blueprint) {

        return blueprint.stackTagCompound == null || !blueprint.stackTagCompound.hasKey("settings") ? null : blueprint.stackTagCompound
                .getCompoundTag("settings");
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote && player.isSneaking() && getStoredSettings(stack) != null) {
            clearSettings(stack);
            player.addChatComponentMessage(new ChatComponentText("Cleared Machine Blueprint"));
        }
        return stack;
    }

}
