package net.quetzi.bluepower.blocks;


import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAlloyFurnace extends BlockContainer {
    public static boolean isActivated = false;
    public ContainerAlloyFurnace() {
        super(Material.rock);
        this.setHardness(1.5F);
        this.setResistance(20.0F);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
    }
    
    private static IIcon iconSide;
    private static IIcon iconTop;
    private static IIcon iconFront;
    private TileAlloyFurnace tileFurnace;
    
    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileAlloyFurnace();
    }
    
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconFront : (side == 0 ? this.iconFront : (side != meta ? this.iconSide : this.iconTop));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconSide = iconRegister.registerIcon(Refs.MODID + ":" + Refs.ALLOYFURNACE_NAME + "_side");
        this.iconFront = iconRegister.registerIcon(this.isActivated ? Refs.MODID + ":" + Refs.ALLOYFURNACE_NAME + "_front_on" : Refs.MODID + ":" + Refs.ALLOYFURNACE_NAME + "_front_off");
        this.iconTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.ALLOYFURNACE_NAME + "_top");
    }
}
