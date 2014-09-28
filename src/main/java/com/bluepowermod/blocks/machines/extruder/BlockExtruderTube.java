package com.bluepowermod.blocks.machines.extruder;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.blocks.BlockMotor;
import com.bluepowermod.client.renderers.RenderExtruderTube;
import com.bluepowermod.util.Refs;

public class BlockExtruderTube extends Block {

    public BlockExtruderTube() {

        super(Material.iron);
        setBlockName(Refs.EXTRUDERTUBE_NAME);
    }

    @Override
    public int getRenderType() {

        return RenderExtruderTube.RENDER_ID;
    }

    public static boolean canConnect(IBlockAccess world, int x, int y, int z) {

        Block block = world.getBlock(x, y, z);

        return block instanceof BlockExtruderTube || block instanceof BlockMotor;
    }

    public static int getExtension(IBlockAccess world, int x, int y, int z) {

        Block block = world.getBlock(x, y, z);

        return block instanceof BlockMotor ? 0 : 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB boundaries, List list, Entity entity) {

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);

        if (box.intersectsWith(boundaries))
            list.add(box);

        // West
        if (canConnect(world, x - 1, y, z)) {
            box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);
            box.minX = x;

            if (box.intersectsWith(boundaries))
                list.add(box);
        }
        // East
        if (canConnect(world, x + 1, y, z)) {
            box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);
            box.maxX = x + 1;

            if (box.intersectsWith(boundaries))
                list.add(box);
        }
        // North
        if (canConnect(world, x, y, z - 1)) {
            box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);
            box.minZ = z;

            if (box.intersectsWith(boundaries))
                list.add(box);
        }
        // South
        if (canConnect(world, x, y, z + 1)) {
            box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);
            box.maxZ = z + 1;

            if (box.intersectsWith(boundaries))
                list.add(box);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {

        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75);

        // West
        if (canConnect(world, x - 1, y, z)) {
            box.minX = x;
        }
        // East
        if (canConnect(world, x + 1, y, z)) {
            box.maxX = x + 1;
        }
        // North
        if (canConnect(world, x, y, z - 1)) {
            box.minZ = z;
        }
        // South
        if (canConnect(world, x, y, z + 1)) {
            box.maxZ = z + 1;
        }

        return box;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {

        MovingObjectPosition mop = null;

        setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
        mop = super.collisionRayTrace(world, x, y, z, start, end);

        // West
        if (mop == null && canConnect(world, x - 1, y, z)) {
            setBlockBounds(0F, 0.25F, 0.25F, 0.25F, 0.75F, 0.75F);
            mop = super.collisionRayTrace(world, x, y, z, start, end);
        }
        // East
        if (mop == null && canConnect(world, x + 1, y, z)) {
            setBlockBounds(0.75F, 0.25F, 0.25F, 1F, 0.75F, 0.75F);
            mop = super.collisionRayTrace(world, x, y, z, start, end);
        }
        // North
        if (mop == null && canConnect(world, x, y, z - 1)) {
            setBlockBounds(0.25F, 0.25F, 0F, 0.75F, 0.75F, 0.25F);
            mop = super.collisionRayTrace(world, x, y, z, start, end);
        }
        // South
        if (mop == null && canConnect(world, x, y, z + 1)) {
            setBlockBounds(0.25F, 0.25F, 0.75F, 0.75F, 0.75F, 1F);
            mop = super.collisionRayTrace(world, x, y, z, start, end);
        }

        return mop;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        blockIcon = reg.registerIcon(Refs.MODID + ":" + Refs.EXTRUDERTUBE_NAME);
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }

}
