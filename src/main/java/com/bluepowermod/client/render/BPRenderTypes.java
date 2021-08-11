package com.bluepowermod.client.render;

import com.bluepowermod.reference.Refs;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL11;

public class BPRenderTypes extends RenderType {

    public static final RenderType LAMP_GLOW = create(Refs.MODID +  ":lamp_glow", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, true,RenderType.CompositeState.builder().setTransparencyState(LIGHTNING_TRANSPARENCY).setShaderState(RENDERTYPE_SOLID_SHADER).createCompositeState(false));

    public BPRenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }
}
