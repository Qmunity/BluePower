package com.bluepowermod.client.render;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * @author MoreThanHidden
 */
public interface ICustomModelBlock {

    @OnlyIn(Dist.CLIENT)
    void initModel();

}
