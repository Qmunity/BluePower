package com.bluepowermod.part.fluid;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

public class PartFaucet extends BPPartFace {

    private static final int length = 5;
    private static final int width = 4;
    private static final int height = 3;
    private static final int y = 9;

    private static final int maxStorage = 1000;
    private static final int maxTransfer = 20;

    private ResourceLocation texture;

    private double progressFaucetStart = 0;
    private double progressFaucetEnd = 0;
    private double progressDownStart = 0;
    private double progressDownEnd = 0;
    private int totalDown = y + 2;

    private boolean castingTable = false;
    private IFluidHandler attached = null;

    private IFluidHandler destTank = null;

    private FluidTank inputTank;
    private FluidTank outputTank;

    public PartFaucet() {

        inputTank = new FluidTank(maxStorage);
        outputTank = new FluidTank(maxTransfer);
    }

    @Override
    public String getType() {

        return "faucet";
    }

    @Override
    public String getUnlocalizedName() {

        return "faucet";
    }

    @Override
    public boolean canPlacePart(ItemStack is, EntityPlayer player, Vector3 block, MovingObjectPosition mop) {

        if (ForgeDirection.getOrientation(mop.sideHit) == ForgeDirection.DOWN || ForgeDirection.getOrientation(mop.sideHit) == ForgeDirection.UP)
            return false;

        TileEntity te = block.getTileEntity();
        if (te == null)
            return false;
        if (!(te instanceof IFluidHandler))
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit).getOpposite();
        if (dir == ForgeDirection.DOWN) {
            dir = ForgeDirection.UP;
        } else {
            if (dir == ForgeDirection.UP)
                dir = ForgeDirection.DOWN;
        }

        setFace(ForgeDirectionUtils.getSide(dir));

        return true;// FIXME super.canPlacePart(is, player, block, mop);
    }

    @Override
    public boolean canStay() {

        if (getWorld() == null)
            return true;

        return new Vector3(getX(), getY(), getZ(), getWorld()).getRelative(ForgeDirection.getOrientation(getFace())).getTileEntity() == attached;
    }

    @Override
    public int getRotation() {

        switch (ForgeDirection.getOrientation(getFace())) {
        case EAST:
            return 3;
        case WEST:
            return 1;
        case NORTH:
            return 2;
        case SOUTH:
            break;
        default:
            break;
        }

        return 0;
    }

    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {

        addSelectionBoxes(boxes);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(AxisAlignedBB.getBoundingBox((8 - (width / 2D)) / 16D, 0, y / 16D, (8 + (width / 2D)) / 16D, length / 16D, (y + height) / 16D));
    }

    @Override
    public void onFirstTick() {

        super.onFirstTick();

        TileEntity te = new Vector3(getX(), getY(), getZ(), getWorld()).getRelative(ForgeDirection.getOrientation(getFace())).getTileEntity();
        if (te == null)
            return;
        if (!(te instanceof IFluidHandler))
            return;
        attached = (IFluidHandler) te;

        refresh();
    }

    @Override
    public void update() {

        super.update();

        if (getWorld() == null)
            return;

        if (attached == null)
            return;

        totalDown = y;
        if (castingTable) {
            totalDown -= 7;
        } else {
            TileEntity below = getWorld().getTileEntity(getX(), getY() - 1, getZ());
            if (below != null) {
                if (below instanceof IFluidHandler) {
                    Block b = getWorld().getBlock(getX(), getY() - 1, getZ());
                    if (b != null) {
                        AxisAlignedBB aabb = b.getCollisionBoundingBoxFromPool(getWorld(), getX(), getY() - 1, getZ());
                        if (aabb != null)
                            totalDown += Math.max(0, (getY() + 2) - aabb.maxY);
                    }
                }
            }
        }

        int maxExtracted = 20;
        int maxTransfered = 5;

        Fluid contained = getFluid();

        boolean sendUpdate = false;

        if (destTank != null) {

            if (!getWorld().isRemote) {// Server
                FluidTankInfo[] information = attached.getTankInfo(ForgeDirection.getOrientation(getFace()));
                if (information != null) {
                    for (FluidTankInfo info : information) {
                        if (info.fluid != null && info.fluid.amount > 0 && (contained == null || info.fluid.getFluid() == contained)) {
                            int filled = inputTank.fill(attached.drain(ForgeDirection.getOrientation(getFace()),
                                    new FluidStack(info.fluid, Math.min(maxExtracted, inputTank.getCapacity() - inputTank.getFluidAmount())), true),
                                    true);
                            if (filled > 0)
                                sendUpdate = true;
                            break;
                        }
                    }
                }
            }

            if (inputTank.getFluidAmount() > 10) {
                if (progressFaucetEnd < length + 1) {
                    progressFaucetStart = 0;
                    progressFaucetEnd += 0.3;
                } else if (progressDownEnd < totalDown) {
                    progressDownStart = 0;
                    progressDownEnd += 0.25;
                }
            }
            if (inputTank.getFluidAmount() <= 10) {
                if (progressFaucetEnd > 0 || progressFaucetStart > 0) {
                    progressFaucetStart += 0.55;
                    if (progressFaucetStart >= length) {
                        progressFaucetStart = 0;
                        progressFaucetEnd = 0;
                    }
                } else if (progressDownEnd > 0 || progressDownStart > 0) {
                    progressDownStart += 0.4;
                    if (progressDownStart >= totalDown) {
                        progressDownStart = 0;
                        progressDownEnd = 0;
                    }
                }
            }

            progressDownEnd = Math.max(0, Math.min(totalDown, progressDownEnd));
            progressDownStart = Math.max(0, Math.min(totalDown, progressDownStart));
            progressFaucetEnd = Math.max(0, Math.min(length + 1, progressFaucetEnd));
            progressFaucetStart = Math.max(0, Math.min(length + 1, progressFaucetStart));

            if (!getWorld().isRemote) {// Server
                if (progressDownEnd >= totalDown) {
                    outputTank.fill(inputTank.drain(Math.min(maxTransfered, outputTank.getCapacity() - outputTank.getFluidAmount()), true), true);
                    sendUpdate = true;
                }

                if (outputTank.getFluidAmount() > 0) {
                    outputTank.drain(destTank.fill(ForgeDirection.UP, outputTank.getFluid(), true), true);
                    sendUpdate = true;
                }
            }
        }

        if (sendUpdate)
            sendUpdatePacket();
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        if (inputTank != null) {
            NBTTagCompound inTank = new NBTTagCompound();
            inputTank.writeToNBT(inTank);
            tag.setTag("inTank", inTank);
        }

        if (outputTank != null) {
            NBTTagCompound outTank = new NBTTagCompound();
            outputTank.writeToNBT(outTank);
            tag.setTag("outTank", outTank);
        }
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);

        try {
            inputTank.readFromNBT(tag.getCompoundTag("inTank"));
        } catch (Exception ex) {
            // There was no tank tag
        }

        try {
            outputTank.readFromNBT(tag.getCompoundTag("outTank"));
        } catch (Exception ex) {
            // There was no tank tag
        }
    }

    @Override
    public void onPartChanged() {

        super.onPartChanged();

        refresh();
    }

    @Override
    public void onNeighborUpdate() {

        super.onNeighborUpdate();

        refresh();
    }

    @Override
    public void onNeighborTileUpdate() {

        super.onNeighborTileUpdate();

        refresh();
    }

    private void refresh() {

        if (getWorld() == null)
            return;

        PartCastingTable table = BPApi.getInstance().getMultipartCompat()
                .getBPPart(getWorld().getTileEntity(getX(), getY(), getZ()), PartCastingTable.class);
        if (table != null) {
            castingTable = true;
            destTank = table;
        } else {
            TileEntity te = getWorld().getTileEntity(getX(), getY() - 1, getZ());
            if (te != null && te instanceof IFluidHandler)
                destTank = (IFluidHandler) te;
        }
    }

    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {

        if (texture == null) {
            texture = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_bottom.png");
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glPushMatrix();
        {
            rotateAndTranslateDynamic(loc, pass, frame);

            // Bottom part
            {
                // Bottom
                GL11.glNormal3d(0, -1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - (y / 16D), (8 + (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();

                // Top
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 + (width / 2D)) / 16D, 1 - (length / 16D));
                }
                GL11.glEnd();

                // Front
                GL11.glNormal3d(0, 0, 1);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();

                // Left
                GL11.glNormal3d(-1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                }
                GL11.glEnd();

                // Right
                GL11.glNormal3d(1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - (y / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();
            }

            // Left wall
            {
                // Top
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, 0, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D,
                            1 - (length / 16D));
                }
                GL11.glEnd();

                // Front
                GL11.glNormal3d(0, 0, 1);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();

                // Left
                GL11.glNormal3d(-1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D)) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                }
                GL11.glEnd();

                // Right
                GL11.glNormal3d(1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();
            }

            // Right wall
            {
                // Top
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D,
                            1 - (length / 16D));
                }
                GL11.glEnd();

                // Front
                GL11.glNormal3d(0, 0, 1);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 + (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();

                // Left
                GL11.glNormal3d(-1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                }
                GL11.glEnd();

                // Right
                GL11.glNormal3d(1, 0, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, 0, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D,
                            1 - (length / 16D));
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + height) / 16D), (8 - (width / 2D)) / 16D, 1);
                    RenderHelper.addVertexWithTexture((8 + (width / 2D)) / 16D, length / 16D, 1 - ((y + 1) / 16D), (8 - (width / 2D)) / 16D, 1);
                }
                GL11.glEnd();
            }

            if (isTransferingLiquid()) {
                // Liquid (Horizontal)
                if (progressFaucetStart > 0 || progressFaucetEnd > 0) {
                    IIcon icon = getFluid().getStillIcon();
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                    // Top
                    GL11.glNormal3d(0, 1, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        // TODO CHANGE THIS VERY PLZ
                        double u1 = icon.getMinU() + ((icon.getMaxU() - icon.getMinU()) * ((8 - (width / 2D) + 1) / 16D));
                        double v1 = icon.getMinV() + ((icon.getMaxV() - icon.getMinV()) * ((progressFaucetStart) / (length + 1)));
                        double u2 = icon.getMaxU() - ((icon.getMaxU() - icon.getMinU()) * ((8 - (width / 2D) + 1) / 16D));
                        double v2 = icon.getMinV() + ((icon.getMaxV() - icon.getMinV()) * (((progressFaucetEnd) / (icon.getIconHeight()))));

                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, progressFaucetStart / 16D, 1 - ((y + height - 1) / 16D), u1,
                                v1);
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D), u1,
                                v2);
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D), u2,
                                v2);
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, progressFaucetStart / 16D, 1 - ((y + height - 1) / 16D), u2,
                                v1);
                    }
                    GL11.glEnd();

                    // Front
                    GL11.glNormal3d(0, 0, 1);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU((8 + (width / 2D) - 1)), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU((8 + (width / 2D) - 1)), icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU((8 - (width / 2D) + 1)), icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU((8 - (width / 2D) + 1)), icon.getInterpolatedV(0));
                    }
                    GL11.glEnd();

                    // Left
                    GL11.glNormal3d(-1, 0, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetStart) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(progressFaucetStart));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(progressFaucetEnd));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(progressFaucetEnd));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (progressFaucetStart) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(progressFaucetStart));
                    }
                    GL11.glEnd();

                    // Right
                    GL11.glNormal3d(1, 0, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetStart) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU(16), icon.getInterpolatedV(progressFaucetStart));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetStart) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU(16), icon.getInterpolatedV(progressFaucetEnd));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + height - 1) / 16D),
                                icon.getInterpolatedU(16), icon.getInterpolatedV(progressFaucetEnd));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (progressFaucetEnd) / 16D, 1 - ((y + 1) / 16D),
                                icon.getInterpolatedU(16), icon.getInterpolatedV(progressFaucetStart));
                    }
                    GL11.glEnd();
                }

                // Liquid (Vertical)
                if (progressDownStart > 0 || progressDownEnd > 0) {
                    IIcon icon = getFluid().getStillIcon();
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                    // Top
                    GL11.glNormal3d(0, 1, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 - (width / 2D) + 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 + (width / 2D) - 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 + (width / 2D) - 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 - (width / 2D) + 1)),
                                icon.getInterpolatedV(1));
                    }
                    GL11.glEnd();

                    // Front
                    GL11.glNormal3d(0, 0, 1);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length + 1) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU((8 + (width / 2D) - 1)), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 + (width / 2D) - 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 - (width / 2D) + 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length + 1) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU((8 - (width / 2D) + 1)), icon.getInterpolatedV(0));
                    }
                    GL11.glEnd();

                    // Back
                    GL11.glNormal3d(0, 0, 1);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU((8 + (width / 2D) - 1)), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU((8 - (width / 2D) + 1)), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 - (width / 2D) + 1)),
                                icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU((8 + (width / 2D) - 1)),
                                icon.getInterpolatedV(1));
                    }
                    GL11.glEnd();

                    // Left
                    GL11.glNormal3d(-1, 0, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length + 1) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU(0), icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 - (width / 2D) + 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU(0), icon.getInterpolatedV(0));
                    }
                    GL11.glEnd();

                    // Left
                    GL11.glNormal3d(1, 0, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU(0), icon.getInterpolatedV(0));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length + 1) / 16D,
                                1 - ((y + height - 2 - progressDownStart) / 16D), icon.getInterpolatedU(0), icon.getInterpolatedV(1));
                        RenderHelper.addVertexWithTexture((8 + (width / 2D) - 1) / 16D, (length + 1) / 16D, 1 - ((y - progressDownEnd + 1) / 16D),
                                icon.getInterpolatedU(0), icon.getInterpolatedV(1));
                    }
                    GL11.glEnd();
                }
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerMachines;
    }

    private Fluid getFluid() {

        if (inputTank.getFluid() != null)
            return inputTank.getFluid().getFluid();
        if (outputTank.getFluid() != null)
            return outputTank.getFluid().getFluid();
        return null;
    }

    private boolean isTransferingLiquid() {

        return getFluid() != null;
    }
}
