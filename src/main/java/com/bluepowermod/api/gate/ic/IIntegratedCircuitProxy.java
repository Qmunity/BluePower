package com.bluepowermod.api.gate.ic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.bluepowermod.api.misc.ICachedBlock;

public interface IIntegratedCircuitProxy {

    public boolean canHandlePlacement(ItemStack stack);

    /**
     * @param player
     *            Fake player until there's an implementation of the API that requires an actual player (physical raytracing).
     */
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack);

    public boolean canHandle(ICachedBlock block);

    public void onBlockUpdate(ICachedBlock block);

    public void onTileUpdate(ICachedBlock block);

    /**
     * @param player
     *            Fake player until there's an implementation of the API that requires an actual player (physical raytracing).
     */
    public boolean onActivated(ICachedBlock block, MovingObjectPosition mop, EntityPlayer player, ItemStack item);

    /**
     * @param player
     *            Fake player until there's an implementation of the API that requires an actual player (physical raytracing).
     */
    public void onClicked(ICachedBlock block, EntityPlayer player, ItemStack item);

}
