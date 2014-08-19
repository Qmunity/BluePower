package com.bluepowermod.api.cast;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ICast {

    public String getCastType();

    @SideOnly(Side.CLIENT)
    public void registerIcon(IIconRegister reg);

    public IIcon getIcon();

}
