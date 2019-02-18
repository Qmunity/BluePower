package com.bluepowermod.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MoreThanHidden
 */
public interface ICustomModelBlock {

    @SideOnly(Side.CLIENT)
    void initModel();

}
