package com.bluepowermod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.part.wire.redstone.WireCommons;

public class RenderCircuitTile implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                GL11.glTranslated(-0.5, 0, -0.5);
                break;
            case EQUIPPED:
                GL11.glTranslated(-0.375, 0.5, -0.125);
                GL11.glScaled(1.5, 1.5, 1.5);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(-0.375, 0.75, -0.125);
                GL11.glScaled(1.5, 1.5, 1.5);
                break;
            case INVENTORY:
                GL11.glTranslated(0, 0.4, 0);
                GL11.glScaled(1.75, 1.75, 1.75);
                break;
            default:
                break;
            }
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            Tessellator.instance.startDrawingQuads();
            RenderHelper rh = RenderHelper.instance;
            rh.renderBox(new Vec3dCube(4 / 16D, 0, 4 / 16D, 12 / 16D, 2 / 16D, 12 / 16D), Blocks.stone_slab.getIcon(0, 0));
            rh.reset();
            Tessellator.instance.draw();

            double wireHeight = 1 / 32D;
            double wireWidth = 1.5 / 16D;

            double bundledHeight = 4 / 32D;
            double bundledWidth = 6 / 16D;

            IIcon wireTexture = IconSupplier.wire;

            // Bluestone tiles
            rh.setColor(WireCommons.getColorForPowerLevel(RedwireType.BLUESTONE.getColor(), (byte) (255 / 2)));
            if (item.getItem() == BPItems.bluestone_cathode_tile) {
                com.bluepowermod.client.render.RenderHelper.renderDigitalRedstoneTorch(0, 0, 0, 13 / 16D, true);
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.bluestone_pointer_tile) {
                com.bluepowermod.client.render.RenderHelper.renderDigitalRedstoneTorch(0, 0, 0, 13 / 16D, true);
                GL11.glPushMatrix();
                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glScaled(0.875, 1, 0.875);
                GL11.glTranslated(-0.5, 0, -0.5);
                com.bluepowermod.client.render.RenderHelper.renderPointer(0, 3 / 16D, 0, 0.25);
                GL11.glPopMatrix();
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.bluestone_anode_tile) {
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(
                        new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7.5 / 16D),
                        wireTexture);
                rh.renderBox(new Vec3dCube(4 / 16D, 2 / 16D, 0.5 - (wireWidth / 2), 12 / 16D, 2 / 16D + wireHeight, 0.5 + (wireWidth / 2)),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.bluestone_wire_tile) {
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 12 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }

            // Redstone tiles
            rh.setColor(WireCommons.getColorForPowerLevel(RedwireType.RED_ALLOY.getColor(), (byte) (255 / 2)));
            if (item.getItem() == BPItems.redstone_cathode_tile) {
                com.bluepowermod.client.render.RenderHelper.renderAnalogRedstoneTorch(0, 0, 0, 13 / 16D, true);
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.redstone_pointer_tile) {
                com.bluepowermod.client.render.RenderHelper.renderAnalogRedstoneTorch(0, 0, 0, 13 / 16D, true);
                GL11.glPushMatrix();
                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glScaled(0.875, 1, 0.875);
                GL11.glTranslated(-0.5, 0, -0.5);
                com.bluepowermod.client.render.RenderHelper.renderPointer(0, 3 / 16D, 0, 0.25);
                GL11.glPopMatrix();
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.redstone_anode_tile) {
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(
                        new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 7.5 / 16D),
                        wireTexture);
                rh.renderBox(new Vec3dCube(4 / 16D, 2 / 16D, 0.5 - (wireWidth / 2), 12 / 16D, 2 / 16D + wireHeight, 0.5 + (wireWidth / 2)),
                        wireTexture);
                Tessellator.instance.draw();
            }
            if (item.getItem() == BPItems.redstone_wire_tile) {
                Tessellator.instance.startDrawingQuads();
                rh.renderBox(new Vec3dCube(0.5 - (wireWidth / 2), 2 / 16D, 4 / 16D, 0.5 + (wireWidth / 2), 2 / 16D + wireHeight, 12 / 16D),
                        wireTexture);
                Tessellator.instance.draw();
            }

            // Misc renderers
            rh.setColor(0xFFFFFF);
            if (item.getItem() == BPItems.quartz_resonator_tile) {
                com.bluepowermod.client.render.RenderHelper.renderQuartzResonator(0, 0, -0.125);
            }
            if (item.getItem() == BPItems.silicon_chip_tile) {
                com.bluepowermod.client.render.RenderHelper.renderRandomizerButton(0, 0, -0.125, false);
            }
            if (item.getItem() == BPItems.taintedsilicon_chip_tile) {
                com.bluepowermod.client.render.RenderHelper.renderRandomizerButton(0, 0, -0.125, true);
            }
            if (item.getItem() == BPItems.stone_bundle_tile) {
                GL11.glPushMatrix();
                {
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glScaled(0.5, 0.5, 0.5);
                    GL11.glTranslated(-0.5, 0.125, -0.5);
                    Tessellator.instance.startDrawingQuads();
                    rh.renderBox(new Vec3dCube(0.5 - (bundledWidth / 2), 2 / 16D, 0, 0.5 + (bundledWidth / 2), 2 / 16D + bundledHeight, 1),
                            IconSupplier.wireBundled, IconSupplier.wireBundled, IconSupplier.wireBundled2, IconSupplier.wireBundled3,
                            IconSupplier.wireBundled, IconSupplier.wireBundled);
                    Tessellator.instance.draw();
                }
                GL11.glPopMatrix();
            }

            rh.reset();
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
    }
}
