package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockItemOre extends Block
{
    private Random rand = new Random();

    public BlockItemOre(String type)
    {
        super(Material.iron);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
        this.setBlockName(type);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return this == BPBlocks.nikolite_ore ? 4 + rand.nextInt(2) : 1;
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3)
    {
        return getDropFromBlockName(this.getUnlocalizedName().substring(5));
    }

    @Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    public int quantityDroppedWithBonus(int quantity, Random rand)
    {
        if (quantity > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, rand, quantity)) {
            int j = rand.nextInt(quantity + 2) - 1;
            if (j < 0) {
                j = 0;
            }
            return this.quantityDropped(rand) * (j + 1);
        } else {
            return this.quantityDropped(rand);
        }
    }

    @Override
    public int getExpDrop(IBlockAccess par1, int par2, int par3)
    {
        if (this.getItemDropped(par2, rand, par3) != Item.getItemFromBlock(this)) {
            int j1 = 0;
            if (this == BPBlocks.amethyst_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.ruby_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.sapphire_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.nikolite_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 2, 5);
            }
            return j1;
        }
        return 0;
    }

    public static Item getDropFromBlockName(String blockName)
    {
        if (blockName.equalsIgnoreCase(Refs.NIKOLITEORE_NAME))
        {
            return BPItems.nikolite;
        }
        else if (blockName.equalsIgnoreCase(Refs.RUBYORE_NAME))
        {
            return BPItems.ruby;
        }
        else if (blockName.equalsIgnoreCase(Refs.SAPPHIREORE_NAME))
        {
            return BPItems.sapphire;
        }
        else if (blockName.equalsIgnoreCase(Refs.AMETHYSTORE_NAME))
        {
            return BPItems.amethyst;
        }
        else
        {
            return null;
        }
    }
}
