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

package com.bluepowermod.blocks.machines;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RenderAlloyCrucible;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier1.TileAlloyCrucible;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyCrucible extends BlockContainerBase {

    public static IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide;
    private IIcon textureFrontOn;
    private IIcon textureFrontOff;

    public BlockAlloyCrucible() {

        super(Material.rock, TileAlloyCrucible.class);
        setBlockName(Refs.ALLOYCRUCIBLE_NAME);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity) {

        int depth = 9;
        double thickness = 1.5;

        // DOWN
        {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + (1 - (depth / 16D)), z + 1);
            if (box.intersectsWith(mask))
                list.add(box);
        }

        // SOUTH
        {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y + (1 - (depth / 16D)), z + (1 - (thickness / 16D)), x + 1, y + 1, z + 1);
            if (box.intersectsWith(mask))
                list.add(box);
        }
        // NORTH
        {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y + (1 - (depth / 16D)), z, x + 1, y + 1, z + (thickness / 16D));
            if (box.intersectsWith(mask))
                list.add(box);
        }

        // EAST
        {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x + (1 - (thickness / 16D)), y + (1 - (depth / 16D)), z, x + 1, y + 1, z + 1);
            if (box.intersectsWith(mask))
                list.add(box);
        }
        // WEST
        {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y + (1 - (depth / 16D)), z, x + (thickness / 16D), y + 1, z + 1);
            if (box.intersectsWith(mask))
                list.add(box);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {

        int depth = 9;

        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + (1 - (depth / 16D)), z + 1);
    }

    @Override
    public void velocityToAddToEntity(World w, int x, int y, int z, Entity entity, Vec3 vel) {

    }

    @Override
    public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity entity) {

        double fluidAmount = 0;

        TileEntity te = w.getTileEntity(x, y, z);
        if (te instanceof TileAlloyCrucible)
            fluidAmount = ((TileAlloyCrucible) te).getTank().getFluidAmount() / (double) ((TileAlloyCrucible) te).getTank().getCapacity();

        if (fluidAmount > 0) {
            if (entity.motionY < 0) {
                entity.motionY *= 0.015 * (1 / Math.min(entity.fallDistance, 1));
                entity.fallDistance *= 0.5;
            } else if (entity.motionY > 0) {
                entity.motionY *= 0.6;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        ForgeDirection facingDirection = ForgeDirection.NORTH;
        boolean active = false;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileAlloyCrucible) {
            facingDirection = ((TileAlloyCrucible) tile).getFacingDirection();
            active = ((TileAlloyCrucible) tile).getIsActive();
        }
        ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
        if (forgeSide == ForgeDirection.UP)
            return textureTop;
        if (forgeSide == ForgeDirection.DOWN)
            return textureBottom;
        if (forgeSide == facingDirection) {
            return active ? textureFrontOn : textureFrontOff;
        }
        return textureSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection s = ForgeDirection.getOrientation(side);
        // If is facing

        if (3 == side) {
            return textureFrontOff;
        }
        switch (s) {
        case UP:
            return textureTop;
        case DOWN:
            return textureBottom;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
        case UNKNOWN:
            return textureSide;
        default:
            break;

        }
        return null;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean isBlockNormalCube() {

        return false;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd) {

        TileAlloyCrucible te = (TileAlloyCrucible) world.getTileEntity(x, y, z);
        if (te.getIsActive()) {
            int l = te.getFacingDirection().ordinal();
            float f = x + 0.5F;
            float f1 = y + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = z + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    // Not sure if you need this function.
    @Override
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {

        return Item.getItemFromBlock(BPBlocks.alloy_crucible);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textureTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYCRUCIBLE_NAME + "_top");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYCRUCIBLE_NAME + "_bottom");
        textureSide = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYCRUCIBLE_NAME + "_side");
        textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYCRUCIBLE_NAME + "_front_on");
        textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.ALLOYCRUCIBLE_NAME + "_front_off");
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {

        TileAlloyCrucible te = (TileAlloyCrucible) world.getTileEntity(x, y, z);
        return te.getIsActive() ? 13 : 0;
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.ALLOY_CRUCIBLE;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

    @Override
    public int getRenderType() {

        return RenderAlloyCrucible.RENDER_ID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        TileAlloyCrucible te = (TileAlloyCrucible) world.getTileEntity(x, y, z);

        ItemStack item = player.getCurrentEquippedItem();
        if (FluidContainerRegistry.isEmptyContainer(item)) {
            int maxAmt = FluidContainerRegistry.getContainerCapacity(item);
            if (te.getTank().getFluidAmount() > 0 && te.getTank().getFluidAmount() >= maxAmt) {
                FluidStack fluid = te.getTank().getFluid().copy();
                fluid.amount = maxAmt;
                ItemStack filled = FluidContainerRegistry.fillFluidContainer(fluid, item);
                if (filled != null) {
                    if (item.stackSize == 1) {
                        player.setItemInUse(filled, 0);
                    } else {
                        if (!player.inventory.addItemStackToInventory(filled)) {
                            if (!world.isRemote)
                                world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY + player.eyeHeight, player.posZ, filled));
                        }
                    }
                    return true;
                }
            }
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }
}
