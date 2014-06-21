/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.blocks.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileRelay;

public class BlockRelay extends BlockContainerBase {

    private IIcon textureFront;
    private IIcon textureBack;
    private IIcon textureSide1;
    private IIcon textureSide2;
    private IIcon textureSide2Active;
    public boolean isActive = false;

    public BlockRelay() {

        super(Material.rock);
        this.setBlockName(Refs.RELAY_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
    }

    /**
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    @Override protected Class<? extends TileEntity> getTileEntity() {

        return TileRelay.class;
    }

    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    @Override public GuiIDs getGuiID() {

        return GuiIDs.RELAY_ID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        this.textureFront = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.RELAY_NAME + "_front");
        this.textureBack = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.RELAY_NAME + "_back");
        this.textureSide1 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.RELAY_NAME + "_side_0");
        this.textureSide2 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.RELAY_NAME + "_side_1");
        this.textureSide2Active = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.RELAY_NAME + "_side_1_active");
        this.blockIcon = this.textureSide1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection direction = ForgeDirection.getOrientation(meta);
        if (side == direction.ordinal()) {
            return textureFront;
        } else if (side == direction.getOpposite().ordinal()) {
            return textureBack;
        } if ((side == 3) || (side == 2)) {
            return isActive ? textureSide2Active : textureSide2;
        }
        return blockIcon;
    }
}
