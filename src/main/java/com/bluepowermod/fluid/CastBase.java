package com.bluepowermod.fluid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.bluepowermod.api.fluid.ICast;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CastBase implements ICast {

    public static final CastBase INGOT = new CastBase("ingot", Refs.MODID + ":cast/ingot");
    public static final CastBase NUGGET = new CastBase("nugget", Refs.MODID + ":cast/nugget");

    private String type, texture;

    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public CastBase(String type, String texture) {

        this.type = type;
        this.texture = texture;
    }

    public CastBase(String type) {

        this(type, Refs.MODID + ":cast/" + type);
    }

    @Override
    public String getType() {

        return type;
    }

    @Override
    public void registerIcons(IIconRegister reg) {

        icon = reg.registerIcon(texture);
    }

    @Override
    public IIcon getIcon() {

        return icon;
    }

}
