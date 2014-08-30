package com.bluepowermod.part.fluid;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.cast.ICast;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.items.ItemCast;
import com.bluepowermod.util.Refs;

public class PartCastingTable extends BPPart implements IFluidHandler {

    public final static int wallThickness = 2;

    private ResourceLocation side;
    private ResourceLocation top;
    private ResourceLocation bottom;

    private FluidTank tank = new FluidTank(1000);

    private ICast cast;
    private ItemStack result;
    private double progress = 0;

    private boolean inUse = false;

    @Override
    public String getType() {

        return "castingTable";
    }

    @Override
    public String getUnlocalizedName() {

        return "castingTable";
    }

    @Override
    public List<AxisAlignedBB> getCollisionBoxes() {

        return getSelectionBoxes();
    }

    @Override
    public List<AxisAlignedBB> getSelectionBoxes() {

        return Arrays.asList(new AxisAlignedBB[] { AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 8 / 16D, 1) });
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);

        if (tank != null) {
            try {
                NBTTagCompound tank = new NBTTagCompound();
                this.tank.writeToNBT(tank);
                tag.setTag("tank", tank);
                tag.setInteger("tankSize", this.tank.getCapacity());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
        }
        if (cast != null)
            tag.setString("cast", cast.getCastType());
        if (result != null) {
            NBTTagCompound t = new NBTTagCompound();
            result.writeToNBT(t);
            tag.setTag("result", t);
        }
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);

        try {
            tank.drain(Integer.MAX_VALUE, true);
            tank.readFromNBT(tag.getCompoundTag("tank"));
            tank.setCapacity(tag.getInteger("tankSize"));
        } catch (Exception ex) {
        }
        cast = BPApi.getInstance().getCastRegistry().getCast(tag.getString("cast"));
        result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("result"));
    }

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack item) {

        if (result != null) {
            if (!getWorld().isRemote) {
                IOHelper.spawnItemInWorld(getWorld(), result, getX(), getY(), getZ());
                result = null;
                sendUpdatePacket();
            }
            return true;
        } else {
            if (cast == null) {
                if (item != null) {
                    if (item.getItem() instanceof ItemCast) {
                        cast = BPApi.getInstance().getCastRegistry().getCastFromStack(item);
                        if (cast != null) {
                            if (!getWorld().isRemote) {
                                if (!player.capabilities.isCreativeMode)
                                    item.stackSize--;
                                sendUpdatePacket();
                            }
                            return true;
                        }
                    }
                }
            } else {
                if (tank.getFluidAmount() > 0)
                    return false;

                if (!getWorld().isRemote) {
                    IOHelper.spawnItemInWorld(getWorld(), ItemCast.createCast(cast), getX(), getY(), getZ());
                    cast = null;
                    sendUpdatePacket();
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public void update() {

        super.update();

        if (!getWorld().isRemote) {
            boolean sendUpdate = false;
            if (tank.getFluidAmount() >= tank.getCapacity()) {
                progress += (1 / 5D) / 20D;
                sendUpdate = true;
            }
            if (progress >= 1) {
                result = BPApi.getInstance().getCastRegistry().getResult(getCast(), getFluid()).copy();
                tank.drain(Integer.MAX_VALUE, true);
                progress = 0;
                sendUpdate = true;
            }
            if (sendUpdate)
                sendUpdatePacket();
        }
    }

    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {

        int height = 8;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
            if (side == null) {
                side = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_side.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                top = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_top.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(top);
                bottom = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_bottom.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
            }

            // Outer area
            GL11.glPushMatrix();
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(top);
                // Top
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(0, height / 16D, 0, 0, 0);
                    RenderHelper.addVertexWithTexture(0, height / 16D, 1, 0, 1);
                    RenderHelper.addVertexWithTexture(1, height / 16D, 1, 1, 1);
                    RenderHelper.addVertexWithTexture(1, height / 16D, 0, 1, 0);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
                // Bottom
                GL11.glNormal3d(0, -1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
                    RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
                    RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
                    RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                // Side
                GL11.glNormal3d(1, 0, 0);
                for (int i = 0; i < 4; i++) {
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture(0, 0, 0, 0, 1);
                        RenderHelper.addVertexWithTexture(0, height / 16D, 0, 0, 0.5);
                        RenderHelper.addVertexWithTexture(1, height / 16D, 0, 1, 0.5);
                        RenderHelper.addVertexWithTexture(1, 0, 0, 1, 1);
                    }
                    GL11.glEnd();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated(90, 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                }
            }
            GL11.glPopMatrix();

            // Inner area
            GL11.glPushMatrix();
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
                // Bottom
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, wallThickness / 16D, wallThickness / 16D,
                            wallThickness / 16D);
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, 1 - (wallThickness / 16D), wallThickness / 16D,
                            1 - (wallThickness / 16D));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, 1 - (wallThickness / 16D),
                            1 - (wallThickness / 16D), 1 - (wallThickness / 16D));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, wallThickness / 16D, 1 - (wallThickness / 16D),
                            wallThickness / 16D);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                // Side
                GL11.glNormal3d(-1.5, 0, 0);
                for (int i = 0; i < 4; i++) {
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, wallThickness / 16D, wallThickness / 16D,
                                1 - (7 / 16D));
                        RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, wallThickness / 16D,
                                1 - (wallThickness / 16D), 1 - (7 / 16D));
                        RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), height / 16D, wallThickness / 16D, 1 - (wallThickness / 16D),
                                0.5);
                        RenderHelper.addVertexWithTexture(wallThickness / 16D, height / 16D, wallThickness / 16D, wallThickness / 16D, 0.5);
                    }
                    GL11.glEnd();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated(90, 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                }
            }
            GL11.glPopMatrix();

            // Fluid
            if (getFluid() != null) {
                IIcon icon = getFluid().getStillIcon();
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                double fluidHeight = 0.875 * (tank.getFluidAmount() / ((double) tank.getCapacity()));

                net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1 + fluidHeight) / 16D, wallThickness / 16D,
                            icon.getInterpolatedU(wallThickness), icon.getInterpolatedV(wallThickness));
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1 + fluidHeight) / 16D, 1 - (wallThickness / 16D),
                            icon.getInterpolatedU(wallThickness), icon.getInterpolatedV(16 - wallThickness));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1 + fluidHeight) / 16D, 1 - (wallThickness / 16D),
                            icon.getInterpolatedU(16 - wallThickness), icon.getInterpolatedV(16 - wallThickness));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1 + fluidHeight) / 16D, wallThickness / 16D,
                            icon.getInterpolatedU(16 - wallThickness), icon.getInterpolatedV(wallThickness));
                }
                GL11.glEnd();

                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            }

            GL11.glPushMatrix();
            {
                ItemStack item = null;
                if (cast != null)
                    item = ItemCast.createCast(cast);
                if (item != null) {

                    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

                    GL11.glTranslated(0.5, 0.3, 0.5);
                    GL11.glScaled(1.5, 1.5, 1.5);
                    GL11.glTranslated(0, 0.125, -0.225);
                    GL11.glRotated(90, 1, 0, 0);

                    com.bluepowermod.client.renderers.RenderHelper.renderItem(item);

                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                }
            }
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            {
                if (result != null) {

                    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

                    GL11.glTranslated(0.5, 0.3, 0.5);
                    GL11.glScaled(1.5, 1.5, 1.5);
                    GL11.glTranslated(0, 0.125, -0.225);
                    GL11.glRotated(90, 1, 0, 0);

                    com.bluepowermod.client.renderers.RenderHelper.renderItem(result);

                    net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerMachines;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {

        if (!canFill(from, resource.getFluid()))
            return 0;

        int amt = tank.fill(resource, doFill);
        if (amt > 0)
            tank.setCapacity(BPApi.getInstance().getCastRegistry().getRecipe(cast, resource.getFluid()).getKey().amount);
        sendUpdatePacket();
        return amt;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {

        return fluid != null && (tank.getFluid() == null || (tank.getFluid() != null && tank.getFluid().getFluid() == fluid)) && result == null
                && BPApi.getInstance().getCastRegistry().getResult(cast, fluid) != null;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {

        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {

        if (from == ForgeDirection.UP)
            return new FluidTankInfo[0];

        return new FluidTankInfo[] { new FluidTankInfo(tank) };
    }

    public Fluid getFluid() {

        return tank.getFluid() != null ? tank.getFluid().getFluid() : null;
    }

    public ICast getCast() {

        return cast;
    }

    public boolean isInUse() {

        return inUse;
    }

    public void setInUse(boolean inUse) {

        this.inUse = inUse;
    }

    public ItemStack getResult() {

        return result;
    }

    @Override
    public float getHardness() {

        return 2.5F;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();

        if (cast != null)
            drops.add(ItemCast.createCast(cast));
        if (result != null)
            drops.add(result);

        return drops;
    }

}
