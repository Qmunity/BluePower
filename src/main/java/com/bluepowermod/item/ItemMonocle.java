package com.bluepowermod.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.BluePower;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMonocle extends ItemArmor {

    public static ArmorMaterial material_monocle = EnumHelper.addArmorMaterial(Refs.MONOCLE_NAME, 1024, new int[] { 0, 0, 0, 0 }, 0);

    @SideOnly(Side.CLIENT)
    private IIcon standard;
    @SideOnly(Side.CLIENT)
    private IIcon magnifying;

    @SideOnly(Side.CLIENT)
    public static KeyBinding keybind = new KeyBinding("key." + Refs.MODID + ":monocle", Keyboard.KEY_M, "key.categories.misc");
    public static boolean active = false;
    public static double fovMultipier = 1;
    public static double rate = 0.05;

    public ItemMonocle() {

        super(material_monocle, RenderingRegistry.addNewArmourRendererPrefix(Refs.MODID + ":" + Refs.MONOCLE_NAME), 0);

        setMaxDamage(0);
        setCreativeTab(BPCreativeTabs.items);
        setUnlocalizedName(Refs.MONOCLE_NAME);

        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        itemIcon = standard = reg.registerIcon(Refs.MODID + ":monocle");
        magnifying = reg.registerIcon(Refs.MODID + ":monocle_magnifying");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {

        return damage == 1 ? magnifying : standard;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "item." + Refs.MODID + ":" + Refs.MONOCLE_NAME + (stack.getItemDamage() == 1 ? "_magnifying" : "");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

        if (stack.getItem() != this)
            return null;

        return Refs.MODID + ":textures/armor/" + Refs.MONOCLE_NAME + (stack.getItemDamage() == 1 ? "_magnifying" : "") + ".png";
    }

    @Override
    public boolean isRepairable() {

        return false;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List l) {

        l.add(new ItemStack(this, 1, 0));
        l.add(new ItemStack(this, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {

        EntityPlayer player = BluePower.proxy.getPlayer();
        if (player == null)
            return;
        ItemStack helm = player.getCurrentArmor(3);

        if (active)
            fovMultipier = Math.max(fovMultipier - rate, 0.25);
        else
            fovMultipier = Math.min(fovMultipier + rate, 1);

        if (helm == null || helm.getItem() != this || helm.getItemDamage() != 1) {
            if (active) {
                active = false;
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Toggled Magnificent Magnifying Monocle: "
                        + (active ? EnumChatFormatting.GREEN + "Active" : EnumChatFormatting.RED + "Inactive")));
            }
            return;
        }

        if (keybind.isPressed()) {
            active = !active;
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Toggled Magnificent Magnifying Monocle: "
                    + (active ? EnumChatFormatting.GREEN + "Active" : EnumChatFormatting.RED + "Inactive")));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {

        event.newfov *= fovMultipier;
    }
}
