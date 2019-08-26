package com.bluepowermod.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MoreThanHidden
 */
public interface ICustomModelBlock {

    @OnlyIn(Dist.CLIENT)
    void initModel();

}
