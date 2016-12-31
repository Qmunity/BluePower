package com.bluepowermod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.gate.component.GateComponentQuartzResonator;
import com.bluepowermod.part.gate.component.GateComponentSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentTaintedSiliconChip;
import com.bluepowermod.part.wire.redstone.WireHelper;

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

            TextureAtlasSprite wireTexture = IconSupplier.wire;

            // Bluestone tiles
            rh.setColor(WireHelper.getColorForPowerLevel(RedwireType.BLUESTONE, (byte) (255 / 2)));
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
            rh.setColor(WireHelper.getColorForPowerLevel(RedwireType.RED_ALLOY, (byte) (255 / 2)));
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

            rh.setColor(0xFFFFFF);
            if (item.getItem() == BPItems.stone_bundle) {
                GL11.glPushMatrix();
                {
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glScaled(0.5, 0.5, 0.5);
                    GL11.glTranslated(-0.5, 0.125, -0.5);
                    Tessellator.instance.startDrawingQuads();
                    rh.renderBox(new Vec3dCube(0.5 - (bundledWidth / 2), 2 / 16D, 0, 0.5 + (bundledWidth / 2), 2 / 16D + bundledHeight, 1),
                            null, IconSupplier.wireBundledStraight1, IconSupplier.wireBundledSide1, IconSupplier.wireBundledSide2,
                            IconSupplier.wireBundledConnection, IconSupplier.wireBundledConnection);
                    Tessellator.instance.draw();
                }
                GL11.glPopMatrix();
            }

            // Misc renderers
            rh.setColor(0xFFFFFF);
            rh.addTransformation(new Translation(0.375, 0, 0.375));
            Tessellator.instance.startDrawingQuads();
            if (item.getItem() == BPItems.quartz_resonator_tile) {
                new GateComponentQuartzResonator(null, -1).renderStatic(new BlockPos(0, 0, 0), rh, 0);
            }
            if (item.getItem() == BPItems.silicon_chip_tile) {
                new GateComponentSiliconChip(null, -1).setState(true).renderStatic(new BlockPos(0, 0, 0), rh, 0);
            }
            if (item.getItem() == BPItems.tainted_silicon_chip_tile) {
                new GateComponentTaintedSiliconChip(null, -1).setState(true).renderStatic(new BlockPos(0, 0, 0), rh, 0);
            }
            Tessellator.instance.draw();
            rh.reset();
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
    }
}
