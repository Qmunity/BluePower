package net.quetzi.bluepower.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCustomFlower extends BlockBush {
    public static final String[] field_149858_b = new String[] { "indigo_flower" };
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;
    private int meta;

    public BlockCustomFlower(int par1) {
        super();
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Plains;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta >= this.iconArray.length) {
            meta = 0;
        }
        return this.iconArray[meta];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < this.iconArray.length; ++i) {
            this.iconArray[i] = iconRegister.registerIcon(Refs.MODID + ":"
                    + this.getUnlocalizedName().substring(5));
        }
    }

    public int damageDropped(int damage) {
        return damage;
    }

    public static BlockFlower func_149857_e(String name) {
        String[] astring = field_149858_b;
        int i = astring.length;
        int j;
        String s1;

        for (j = 0; j < i; ++j) {
            s1 = astring[j];

            if (s1.equals(name)) {
                return (BlockFlower) BPBlocks.indigo_flower;
            }
        }
        return null;
    }

    public static int func_149856_f(String name) {
        int i;

        for (i = 0; i < field_149858_b.length; ++i) {
            if (field_149858_b[i].equals(name)) {
                return i;
            }
        }
        return 0;
    }
}
