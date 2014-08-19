package com.bluepowermod.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.client.renderers.RenderCastPlate;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.TileCastPlate;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCastPlate extends BlockContainerBase {

    public BlockCastPlate() {

        super(Material.rock, TileCastPlate.class);
        setBlockName(Refs.CASTPLATE_NAME);

        setHardness(1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {

        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + (2 / 16D), z + 1);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {

        return getCollisionBoundingBoxFromPool(w, x, y, z);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start,
            Vec3 end) {

        setBlockBounds(0, 0, 0, 1, 2 / 16F, 1);

        return super.collisionRayTrace(w, x, y, z, start, end);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        return BPBlocks.marble.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        return BPBlocks.marble.getIcon(0, 0);
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {

        return false;
    }

    @Override
    public int getRenderType() {

        return RenderCastPlate.RENDER_ID;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        TileCastPlate te = (TileCastPlate) world.getTileEntity(x, y, z);
        return te.onBlockActivated(world, x, y, z, player);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        return new ArrayList<ItemStack>();
    }

}
