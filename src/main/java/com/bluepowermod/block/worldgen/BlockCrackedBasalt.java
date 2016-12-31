/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import java.util.Random;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.util.DateEventHandler;
import com.bluepowermod.util.DateEventHandler.Event;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author MineMaarten
 */

public class BlockCrackedBasalt extends BlockStoneOre {

    public BlockCrackedBasalt(String name) {

        super(name);
        setTickRandomly(true);
        setResistance(25.0F);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {

        if (world.getBlockMetadata(x, y, z) == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 1, 2);
            world.scheduleBlockUpdate(x, y, z, this, 1);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        int meta = world.getBlockMetadata(x, y, z);
        // When this block was active already (meta > 0) or when the random chance hit, spew lava.
        if (!world.isRemote && (meta > 0 || random.nextInt(100) == 0)) {

            if (meta > 4) {
                spawnLava(world, x, y, z, random);
            }
            if (meta < 15) {
                if (random.nextInt(40) == 0 || meta == 0)
                    world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
                world.scheduleBlockUpdate(x, y, z, this, 1);
            } else {
                world.setBlock(x, y, z, Blocks.flowing_lava);
            }
        }
    }

    @Override
    protected boolean canSilkHarvest() {

        return false;
    }

    private void spawnLava(World world, int x, int y, int z, Random random) {
        if (DateEventHandler.isEvent(Event.NEW_YEAR)) {
            DateEventHandler.spawnFirework(world, x + 0.5, y + 0.5, z + 0.5);
        } else {
            EntityFallingBlock entity = new EntityFallingBlock(world, x + 0.5, y + 0.5, z + 0.5,
                    DateEventHandler.isEvent(Event.HALLOWEEN) ? Blocks.lit_pumpkin : Blocks.flowing_lava);
            entity.motionY = 1 + random.nextDouble();
            entity.motionX = (random.nextDouble() - 0.5) * 0.8D;
            entity.motionZ = (random.nextDouble() - 0.5) * 0.8D;
            entity.field_145812_b = 1;// make this field that keeps track of the ticks set to 1, so it doesn't kill itself when it searches for a lava
            // block.
            entity.field_145813_c = false; // disable item drops when the falling block fails to place.
            world.spawnEntityInWorld(entity);
        }
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3) {

        return Item.getItemFromBlock(BPBlocks.basalt_cobble);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {

        if (world.getBlockMetadata(x, y, z) > 0) {
            for (int i = 0; i < 10; i++)
                world.spawnParticle("largesmoke", x + rand.nextDouble(), y + 1, z + rand.nextDouble(), (rand.nextDouble() - 0.5) * 0.2,
                        rand.nextDouble() * 0.1, (rand.nextDouble() - 0.5) * 0.2);
        }
    }

}
