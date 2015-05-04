package com.bluepowermod.part.fluid;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.BPPartFace;

public class PartFaucet extends BPPartFace {

    @Override
    public String getType() {

        return "faucet";
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    @Override
    public boolean canStay() {

        if (getFace() == ForgeDirection.DOWN || getFace() == ForgeDirection.UP)
            return false;

        return super.canStay();
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        int rot = getFace() == ForgeDirection.WEST ? 3 : (getFace() == ForgeDirection.NORTH ? 2 : (getFace() == ForgeDirection.EAST ? 1 : 0));

        return Arrays.asList(VectorHelper.rotateBox(new Vec3dCube(0.5 - 2 / 16D, 0, 0.5 + 1 / 16D, 0.5 + 2 / 16D, 0.25, 0.5 + 4 / 16D), getFace(),
                rot));
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        boxes.addAll(getSelectionBoxes());
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return getSelectionBoxes();
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        int rot = getFace() == ForgeDirection.WEST ? 3 : (getFace() == ForgeDirection.NORTH ? 2 : (getFace() == ForgeDirection.EAST ? 1 : 0));

        renderer.renderBox(
                VectorHelper.rotateBox(new Vec3dCube(0.5 - 2 / 16D, 0, 0.5 + 1 / 16D, 0.5 + 2 / 16D, 0.25, 0.5 + 2 / 16D), getFace(), rot),
                IconSupplier.castingTableBottom);
        renderer.renderBox(
                VectorHelper.rotateBox(new Vec3dCube(0.5 - 2 / 16D, 0, 0.5 + 2 / 16D, 0.5 - 1 / 16D, 0.25, 0.5 + 4 / 16D), getFace(), rot),
                IconSupplier.castingTableBottom);
        renderer.renderBox(
                VectorHelper.rotateBox(new Vec3dCube(0.5 + 1 / 16D, 0, 0.5 + 2 / 16D, 0.5 + 2 / 16D, 0.25, 0.5 + 4 / 16D), getFace(), rot),
                IconSupplier.castingTableBottom);
        return true;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        setFace(ForgeDirection.SOUTH);

        GL11.glPushMatrix();
        GL11.glTranslated(-0.5, -1, -1.25);
        GL11.glScaled(2, 2, 2);

        Tessellator t = Tessellator.instance;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        t.startDrawingQuads();

        RenderHelper renderer = RenderHelper.instance;
        renderer.fullReset();
        RenderBlocks rb = new RenderBlocks();

        renderStatic(new Vec3i(0, 0, 0), renderer, rb, 0);

        t.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

        GL11.glPopMatrix();
    }

}
