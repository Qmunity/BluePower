package com.bluepowermod.api.fluid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ICast {

    public String getType();

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg);

    @SideOnly(Side.CLIENT)
    public IIcon getIcon();

}
