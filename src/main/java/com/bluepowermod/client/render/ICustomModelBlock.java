package com.bluepowermod.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MoreThanHidden
 */
public interface ICustomModelBlock {

    @OnlyIn(Dist.CLIENT)
    void initModel();

}
