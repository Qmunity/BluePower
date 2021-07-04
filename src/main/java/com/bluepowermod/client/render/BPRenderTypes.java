package com.bluepowermod.client.render;

import com.bluepowermod.reference.Refs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class BPRenderTypes extends RenderType {

    public static final RenderType LAMP_GLOW = create(Refs.MODID +  ":lamp_glow", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, true, true,RenderType.State.builder().setTransparencyState(LIGHTNING_TRANSPARENCY).setShadeModelState(FLAT_SHADE).createCompositeState(false));

    public BPRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }
}
