package com.bluepowermod.api.gate.ic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.misc.ICachedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IIntegratedCircuitRegistry {

    public void registerProxy(IIntegratedCircuitProxy proxy);

    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack);

    public void onBlockUpdate(ICachedBlock block);

    public void onTileUpdate(ICachedBlock block);

    public boolean onActivated(ICachedBlock block, MovingObjectPosition mop, EntityPlayer player, ItemStack item);

    public void onClicked(ICachedBlock block, EntityPlayer player, ItemStack item);

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(ICachedBlock block, RenderHelper helper, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame);

}
