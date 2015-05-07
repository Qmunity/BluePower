package com.bluepowermod.part.wire.bluepower;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.bluepower.IPowerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.wire.PartWireFace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl);
 */

public class WireBluePower extends PartWireFace implements IBluePowered, IPartTicking {

    protected final boolean[] connections = new boolean[6];

    private IPowerBase handler;

    @Override
    public String getType() {

        return "bluepowerWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluepowerWire";
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.power;
    }

    @Override
    public void update() {

        if (!getWorld().isRemote) {
            getHandler().update();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {

        double scale = 1.25;
        double translation = 0.25;
        double droppedTranslation = 0;

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(droppedTranslation, 0, droppedTranslation);
            GL11.glTranslated(0, translation, 0);
            GL11.glScaled(scale, scale, scale);
            Tessellator.instance.startDrawingQuads();
            renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
            Tessellator.instance.draw();
        }
        GL11.glPopMatrix();

        rh.reset();
    }

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public BluePowerTier getTier() {

        return BluePowerTier.LOWVOLTAGE;
    }

    @Override
    public IPowerBase getHandler() {

        if (handler == null) {
            handler = BPApi.getInstance().getNewPowerHandler(this);
        }
        return handler;
    }

    @Override
    public boolean canConnectTo(ForgeDirection dir) {

        Vec3i thisLoc = new Vec3i(this);
        Vec3i target = thisLoc.add(dir).setWorld(getWorld());

        if (target.getTileEntity() instanceof IBluePowered) {
            return true;
        }
        IBluePowered p = MultipartCompatibility.getPart(getWorld(), target.getX(), target.getY(), target.getZ(), IBluePowered.class);
        return p != null;
    }

    @Override
    public float getMaxStorage() {

        return 10;
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return canConnectTo(side);
    }

    @Override
    protected double getWidth() {

        return 4;
    }

    @Override
    protected double getHeight() {

        return 3;
    }

    @Override
    protected IIcon getWireIcon(ForgeDirection side) {

        return Blocks.planks.getIcon(0, 0);
    }
}
