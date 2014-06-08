package net.quetzi.bluepower.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneLight;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

import java.util.Random;

public class BlockCustomLamp extends BlockRedstoneLight
{
    private boolean isInverted;
    private int     colour;
    private boolean isPowered;
    private boolean isOn;

    public BlockCustomLamp(String name, int colour, boolean isInverted)
    {
        super(isInverted);
        this.setBlockName(name);
        this.setBlockTextureName(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
        this.isInverted = isInverted;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.isOn = !isInverted;
        this.colour = colour;
        this.setCreativeTab(CustomTabs.tabBluePowerLighting);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (!world.isRemote) {
            if (this.isInverted && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = false;
            } else if (!this.isInverted && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = true;
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote) {
            if (this.isInverted && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = false;
            } else if (!this.isInverted && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = true;
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote) {
            if (this.isInverted && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = false;
            } else if (!this.isInverted && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                this.isOn = true;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getItem(World world, int x, int y, int z)
    {
        switch (colour) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
        }
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }
}
