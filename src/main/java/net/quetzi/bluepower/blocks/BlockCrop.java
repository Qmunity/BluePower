package net.quetzi.bluepower.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrop extends BlockCrops implements IGrowable {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockCrop() {
        this.setTickRandomly(true);
        float f = 0.5F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
        this.setBlockName(Refs.FLAXCROP_NAME);
        this.setBlockTextureName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.farmland;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);

        if (world.getBlockLightValue(x, y + 1, z) >= 9) {
            int meta = world.getBlockMetadata(x, y, z);
            if ((meta == 4) || (meta == 5)) {
                return;
            }
            if ((world.getBlock(x, y - 1, z) != Blocks.farmland) || (world.getBlock(x, y - 1, z) == BPBlocks.flax_crop) || (!world.isAirBlock(x, y + 1, z))) {
              return;
            }
            if (random.nextInt(30) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
              }
        }
    }

    public void func_149863_m(World world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z)
                + MathHelper.getRandomIntegerInRange(world.rand, 2, 5);
        if (l > 7) {
            l = 7;
        }
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }
    private float func_149864_n(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;
                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1,
                        ForgeDirection.UP, this)) {
                    f1 = 1.0F;
                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1)) {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }
                f += f1;
            }
        }
        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }
        return f;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 7) {
            meta = 7;
        }

        return this.iconArray[meta];
    }
    
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 6;
    }

    protected Item func_149866_i() {
        return BPItems.flax_seed;
    }

    protected Item func_149865_P() {
        return Items.string;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified
     * items
     */
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_,
            float p_149690_6_, int p_149690_7_) {
        super.dropBlockAsItemWithChance(world, x, y, z, p_149690_5_, p_149690_6_, 0);
    }

    public Item getItemDropped(int meta, Random random, int p_149650_3_) {
        return meta == 7 ? this.func_149865_P() : this.func_149866_i();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return random.nextInt(2);
    }

    public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
        return world.getBlockMetadata(x, y, z) != 7;
    }

    public boolean func_149852_a(World world, Random random, int x, int y, int z) {
        return true;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z) {
        return this.func_149866_i();
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.iconArray = new IIcon[8];

        for (int i = 0; i < this.iconArray.length; ++i) {
            int tex = 0;
            if (i == 0 || i == 1) { tex = 0; }
            else if (i == 2) { tex = 1; }
            else if (i == 3 || i == 4) { tex = 2; }
            else if (i == 5) { tex = 3; }
            else if (i == 6) { tex = 4; }
            else if (i == 7) { tex = 5; }

            this.iconArray[i] = iconRegister.registerIcon(this.getTextureName() + "_stage_" + tex);
        }
    }

    public void func_149853_b(World world, Random random, int x, int y, int z) {
        this.func_149863_m(world, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

        if (metadata >= 7) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (world.rand.nextInt(15) <= metadata) {
                    ret.add(new ItemStack(this.func_149866_i(), 1, 0));
                    ret.add(new ItemStack(this.func_149865_P(), 1, 0));
                }
            }
        }
        return ret;
    }
}
