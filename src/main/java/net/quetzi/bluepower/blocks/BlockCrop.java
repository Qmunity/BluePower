package net.quetzi.bluepower.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.references.Refs;

import java.util.ArrayList;
import java.util.Random;

public class BlockCrop extends BlockCrops implements IGrowable {
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public BlockCrop() {
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs) null);
        this.setHardness(0.0F);
        this.setStepSound(soundTypeGrass);
        this.disableStats();
        this.setBlockName(Refs.FLAXCROP_NAME);
        this.setBlockTextureName(Refs.MODID + ":" + Refs.FLAXCROP_NAME);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int l = world.getBlockMetadata(x, y, z);
        if (l <= 2) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
        } else if (l <= 4) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else if (l <= 6) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.farmland;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    /**
     * checks if the block can stay, if not drop as item
     */
    protected void checkAndDropBlock(World world, int x, int y, int z) {
        if (!this.canBlockStay(world, x, y, z)) {
            int l = world.getBlockMetadata(x, y, z);

            if (!func_149887_c(l)) {
                this.dropBlockAsItem(world, x, y, z, l, 0);

                if (world.getBlock(x, y + 1, z) == this) {
                    world.setBlock(x, y + 1, z, Blocks.air, 0, 2);
                }
            }

            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    public static boolean func_149887_c(int meta) {
        return (meta & 8) != 0;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) != this)
            return super.canBlockStay(world, x, y, z); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        int l = world.getBlockMetadata(x, y, z);
        if ((world.getBlock(x, y - 1, z) instanceof BlockFarmland) || (world.getBlock(x, y - 1, z) instanceof BlockCrop)) {
            return true;
        } else
            return false;
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
            if ((world.getBlock(x, y - 1, z) != Blocks.farmland)
                    || (world.getBlock(x, y - 1, z) == BPBlocks.flax_crop)
                    || (!world.isAirBlock(x, y + 1, z))) {
                return;
            }
            if (random.nextInt(30) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
            }
            if ((meta > 5) && (world.getBlock(x, y - 1, z) == Blocks.farmland) && (world.getBlock(x, y + 1, z) == Blocks.air)) {
                world.setBlockMetadataWithNotify(x, y, z, 7, 2);
                world.setBlock(x, y + 1, z, BPBlocks.flax_crop, 8, 2);
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

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 8) {
            meta = 8;
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
        return meta == 8 ? this.func_149865_P() : this.func_149866_i();
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
        this.iconArray = new IIcon[9];

        for (int i = 0; i < this.iconArray.length; ++i) {
            int tex = 0;
            if (i == 0 || i == 1) {
                tex = 0;
            } else if (i == 2) {
                tex = 1;
            } else if (i == 3 || i == 4) {
                tex = 2;
            } else if (i == 5) {
                tex = 3;
            } else if ((i == 6) || (i == 7)) {
                tex = 4;
            } else if (i == 8) {
                tex = 5;
            }

            this.iconArray[i] = iconRegister.registerIcon(this.getTextureName() + "_stage_" + tex);
        }
    }

    public void func_149853_b(World world, Random random, int x, int y, int z) {
        this.func_149863_m(world, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);

        if (metadata >= 8) {
            for (int i = 0; i < 3 + fortune; ++i) {
                if (world.rand.nextInt(15) <= metadata) {
                    ret.add(new ItemStack(this.func_149865_P(), 1, 0));
                }
            }
        }
        return ret;
    }
}
