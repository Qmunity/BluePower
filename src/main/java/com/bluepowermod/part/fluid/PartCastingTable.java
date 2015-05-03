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

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.BPPart;

public class PartCastingTable extends BPPart {

    @Override
    public String getType() {

        return "castingtable";
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return Arrays.asList(new Vec3dCube(0, 0, 0, 1, 0.5, 1));
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

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 0.5, 1), IconSupplier.castingTableBottom, IconSupplier.castingTableTop,
                IconSupplier.castingTableSide, IconSupplier.castingTableSide, IconSupplier.castingTableSide, IconSupplier.castingTableSide);
        renderer.setRenderFromInside(true);
        renderer.renderBox(new Vec3dCube(2 / 16D, 0.5 - 1 / 16D, 2 / 16D, 1 - 2 / 16D, 0.5, 1 - 2 / 16D), IconSupplier.castingTableBottom, null,
                IconSupplier.castingTableSide, IconSupplier.castingTableSide, IconSupplier.castingTableSide, IconSupplier.castingTableSide);
        renderer.setRenderFromInside(false);
        return false;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        GL11.glTranslated(0, -0.125D, 0);

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
