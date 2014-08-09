package com.bluepowermod.blocks.machines.power.generators;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.tileentities.tier2.TileSolarPanel.TileSolarPanel;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * @author Koen Beckers (K4Unl)
 */

public class BlockSolarPanel extends BlockContainerBase{

    private IIcon topIcon;
    private IIcon bottomIcon;
    private IIcon sideIcon;

    public BlockSolarPanel() {

        super(Material.iron, TileSolarPanel.class);
        setBlockName(Refs.SOLAR_PANEL_NAME);
        setCreativeTab(CustomTabs.tabBluePowerPower);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        topIcon = iconRegister.registerIcon(Refs.MODID + ":machines/solar_panel_top");
        bottomIcon = iconRegister.registerIcon(Refs.MODID + ":machines/solar_panel_bottom");
        sideIcon = iconRegister.registerIcon(Refs.MODID + ":machines/solar_panel_side");
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderLamp.RENDER_ID;
    }
*/
}
