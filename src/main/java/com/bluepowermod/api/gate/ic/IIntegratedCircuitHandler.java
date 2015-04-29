package com.bluepowermod.api.gate.ic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.misc.ICachedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IIntegratedCircuitHandler {

    public boolean canPlaceOnIntegratedCircuit(ItemStack stack);

    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack);

    @SideOnly(Side.CLIENT)
    public boolean canRender(ICachedBlock block);

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(int x, int y, ICachedBlock block, RenderHelper helper, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame);

}
