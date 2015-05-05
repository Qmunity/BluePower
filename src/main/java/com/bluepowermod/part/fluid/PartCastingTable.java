package com.bluepowermod.part.fluid;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.client.render.RenderUtils;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.fluid.ICast;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.fluid.CastBase;
import com.bluepowermod.fluid.CastRegistry;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.item.ItemCast;
import com.bluepowermod.part.BPPart;

public class PartCastingTable extends BPPart {

    private ICast cast;

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
        return true;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        if (cast == null)
            return;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(0.5, 0.5 - 1 / 64D, 0.125 + 1 / 32D);
            GL11.glRotated(90, 1, 0, 0);
            GL11.glScaled(1.5, 1.5, 1.5);

            RenderUtils.renderItem(ItemCast.createCast(cast));

            if (cast == CastBase.INGOT)
                RenderUtils.renderItem(new ItemStack(Items.iron_ingot));
            if (cast == CastBase.NUGGET)
                RenderUtils.renderItem(new ItemStack(Items.gold_nugget));
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        GL11.glTranslated(1 / 32D, 0.125D, 0);

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

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (cast == null) {
            ICast c = CastRegistry.getInstance().getCastFromStack(item);
            if (c != null) {
                cast = c;
                item.stackSize--;
                return true;
            }
        } else {
            IOHelper.spawnItemInWorld(getWorld(), ItemCast.createCast(cast), getX(), getY() + 0.75, getZ());
            cast = null;
            return true;
        }

        return false;
    }

}
