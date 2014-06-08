package net.quetzi.bluepower.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemCropSeed extends ItemSeeds implements IPlantable
{

    public static Block field_150925_a;

    @SuppressWarnings("static-access")
    public ItemCropSeed(Block blockCrop, Block blockSoil)
    {
        super(blockCrop, blockSoil);
        this.field_150925_a = blockCrop;
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setTextureName(Refs.MODID + ":" + Refs.FLAXSEED_NAME);
    }

    @SuppressWarnings("static-access")
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (side != 1) {
            return false;
        } else if (player.canPlayerEdit(x, y, z, side, itemStack) && player.canPlayerEdit(x, y + 1, z, side, itemStack)) {
            if (world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && world.isAirBlock(x, y + 1, z) && (world.getBlock(x, y, z).isFertile(world, x, y, z))) {
                world.setBlock(x, y + 1, z, this.field_150925_a, 0, 2);
                --itemStack.stackSize;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return field_150925_a;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return 0;
    }
}
