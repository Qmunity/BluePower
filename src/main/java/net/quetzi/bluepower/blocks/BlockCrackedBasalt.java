package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.BPBlocks;

public class BlockCrackedBasalt extends BlockStoneOre{
    public BlockCrackedBasalt(String name){
        super(name);
        setTickRandomly(true);
        setResistance(25.0F);
        setHarvestLevel("pickaxe",1);
    }

    /* Debug, when testing, also change the 'random.nextInt(100)' to 'random.nextInt(1)'
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        updateTick(world, x,y,z,new Random());
        return true;
    }*/

    public void updateTick(World world, int x, int y, int z, Random random) {
        int meta = world.getBlockMetadata(x,y,z);
        //When this block was active already (meta > 0) or when the random chance hit, spew lava.
        if(!world.isRemote && (meta > 0 || random.nextInt(100) == 0)){
            spawnLava(world, x, y, z, random);
            if(meta < 15){
                if(random.nextInt(20) == 0) world.setBlockMetadataWithNotify(x, y, z, meta + 1, 0);
                world.scheduleBlockUpdate(x, y, z, this, 1);
            }else{
                world.setBlock(x, y, z, Blocks.flowing_lava);
            }
        }
    }

    private void spawnLava(World world, int x, int y, int z, Random random){
        EntityFallingBlock entity = new EntityFallingBlock(world, x + 0.5, y + 0.5, z + 0.5, Blocks.flowing_lava);
        entity.motionY = 1 + random.nextDouble();
        entity.motionX = (random.nextDouble() - 0.5) * 0.8D;
        entity.motionZ = (random.nextDouble() - 0.5) * 0.8D;
        entity.field_145812_b = 1;//make this field that keeps track of the ticks set to 1, so it doesn't kill itself when it searches for a lava block.
        entity.field_145813_c = false; //disable item drops when the falling block fails to place.
        world.spawnEntityInWorld(entity);
    }
    
    public Item getItemDropped(int par1, Random par2, int par3) {
        return Item.getItemFromBlock(BPBlocks.basalt_cobble);
    }
    
    protected boolean canSilkHarvest(){
        return false;
    }
}
