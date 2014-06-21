package net.quetzi.bluepower.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.tileentities.BPTileMultipart;
import net.quetzi.bluepower.util.RayTracer;

public class BPBlockMultipart extends BlockContainer {
    
    protected BPBlockMultipart() {
    
        super(Material.rock);
    }
    
    private BPPart hovered = null;
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
    
        return new BPTileMultipart();
    }
    
    private BPTileMultipart getTile(IBlockAccess w, int x, int y, int z) {
    
        TileEntity tile = w.getTileEntity(x, y, z);
        if (tile == null) return null;
        if (tile instanceof BPTileMultipart) return ((BPTileMultipart) tile);
        return null;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addCollisionBoxesToList(World w, int x, int y, int z, AxisAlignedBB unused, List l, Entity entity) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return;
        
        l.addAll(t.getCollisionBoxes());
    }
    
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
    
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess w, int x, int y, int z, int side) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return false;
        
        return super.canConnectRedstone(w, x, y, z, side);
    }
    
    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
    
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 start, Vec3 end) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return null;
        
        MovingObjectPosition mop = t.rayTrace(new Vector3(start), new Vector3(end));
        
        if (mop == null) return null;
        
        @SuppressWarnings("unused")
        EntityPlayer p = null;// FIXME BLUEPOWER Return the player that's raytracing
        
        hovered = null;// FIXME BLUEPOWER Get selected part
        
        return mop;
    }
    
    @Override
    public float getBlockHardness(World w, int x, int y, int z) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return 0;
        
        if (hovered != null && hovered.world == w && hovered.x == x && hovered.y == y && hovered.z == z) {
            EntityPlayer player = null;// FIXME BLUEPOWER Get player that's breaking it
            
            return hovered.getHardness(t.rayTrace(new Vector3(RayTracer.getCorrectedHeadVec(player)), new Vector3(RayTracer.getEndVec(player))),
                    player);
        }
        
        hovered = null;
        
        return 0;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World w, int x, int y, int z, int metadata, int fortune) {
    
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return items;
        
        for (BPPart p : t.getParts()) {
            List<ItemStack> i = p.getDrops();
            if (i != null) items.addAll(i);
        }
        
        return items;
    }
    
    @Override
    public float getExplosionResistance(Entity par1Entity, World w, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return -1;
        return t.getExplosionResistance();
    }
    
    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
    
        return 0;
    }
    
    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return -1;
        return t.getLightValue();
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World w, int x, int y, int z) {
    
        BPTileMultipart t = getTile(w, x, y, z);
        if (t == null) return null;
        
        return t.getPickedItem(target);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {
    
//        BPTileMultipart t = getTile(w, x, y, z);
//        if (t == null) return null;
//        
//        if (hovered != null && hovered.world == w && hovered.x == x && hovered.y == y && hovered.z == z) {
//            EntityPlayer player = null;// FIXME BLUEPOWER Get player that's breaking it
//            
//            return hovered.getSelectionBoxes();
//        }
//        
//        hovered = null;
        
        return null;
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
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
    
        return super.isProvidingWeakPower(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, p_149709_5_);
    }
    
    @Override
    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
    
        return super.isProvidingStrongPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
    }
    
    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_,
            float p_149727_7_, float p_149727_8_, float p_149727_9_) {
    
        return super.onBlockActivated(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_, p_149727_5_, p_149727_6_, p_149727_7_, p_149727_8_,
                p_149727_9_);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_) {
    
        super.onEntityCollidedWithBlock(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, p_149670_5_);
    }
    
    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
    
        return 0;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
    
        return false;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
    
        super.setBlockBoundsBasedOnState(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);
    }
    
}
