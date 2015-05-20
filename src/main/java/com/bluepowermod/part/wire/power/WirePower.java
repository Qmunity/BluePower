package com.bluepowermod.part.wire.power;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.network.PacketHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.wire.PartWireFace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl);
 */

public class WirePower extends PartWireFace implements IPowered, IPartTicking, IConnectionListener {

    private IPowerBase handler = BPApi.getInstance().getPowerApi().createPowerHandler(this);

    @Override
    public String getType() {

        return "powerWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "powerWire";
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.power;
    }

    @Override
    public void update() {

        if (!getWorld().isRemote) {
            handler.update();
        }
        handler.getConnectionCache().listen(this);
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return Arrays.asList(new Vec3dCube(0, 0, 0, 1, getHeight() / 16D, 1).rotate(getFace(), Vec3d.center));
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return getSelectionBoxes();
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
    public PowerTier getPowerTier() {

        return PowerTier.LOWVOLTAGE;
    }

    @Override
    public IPowerBase getPowerHandler(ForgeDirection side) {

        return handler;
    }

    @Override
    public boolean canConnectPower(ForgeDirection side, IPowered dev, ConnectionType type) {

        return true;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return false;
    }

    @Override
    public float getMaxPowerStorage() {

        return 10;
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return handler.isConnected(side);
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

        return side == ForgeDirection.UP || side == ForgeDirection.DOWN ? IconSupplier.powerCableLV1 : IconSupplier.powerCableLV2;
    }

    @Override
    public void onNeighborBlockChange() {

        super.onNeighborBlockChange();
        handler.onNeighborUpdate();
    }

    @Override
    public void onNeighborTileChange() {

        super.onNeighborTileChange();
        handler.onNeighborUpdate();
    }

    @Override
    public void onPartChanged(IPart part) {

        super.onPartChanged(part);
        handler.onNeighborUpdate();
    }

    @Override
    public void onAdded() {

        super.onAdded();
        handler.onNeighborUpdate();
    }

    @Override
    public void onConverted() {

        super.onConverted();
        handler.onNeighborUpdate();
    }

    @Override
    public void onLoaded() {

        super.onLoaded();
        handler.onNeighborUpdate();
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
        handler.disconnect();
    }

    @Override
    public void onConnect(IConnection<?> connection) {

        sendUpdatePacket();
    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

        sendUpdatePacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        handler.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        handler.readFromNBT(tag);
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        NBTTagCompound tag = new NBTTagCompound();
        handler.writeUpdateToNBT(tag);
        PacketHelper.writeNBT(buffer, tag);
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        NBTTagCompound tag = PacketHelper.readNBT(buffer);
        handler.readUpdateFromNBT(tag);

        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }
}
