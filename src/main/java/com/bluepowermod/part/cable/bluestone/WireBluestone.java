/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.cable.bluestone;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.helper.RedstoneHelper;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.ICableSize;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.compat.fmp.MultipartBPPart;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.part.cable.CableWall;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class WireBluestone extends CableWall implements IBluestoneWire, ICableSize {

    protected static Vector3Cube SELECTION_BOX = new Vector3Cube(0, 0, 0, 1, 1 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX = new Vector3Cube(7 / 16D, 0 / 16D, 7 / 16D, 9 / 16D, 1 / 16D, 9 / 16D);
    protected static Vector3Cube SELECTION_BOX_INSULATED = new Vector3Cube(0, 0, 0, 1, 3 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX_INSULATED = new Vector3Cube(7 / 16D, 0 / 16D, 7 / 16D, 9 / 16D, 3 / 16D, 9 / 16D);

    private static ResourceLocation textureOn;
    private static ResourceLocation textureOff;
    private static ResourceLocation textureInsulationFB;
    private static ResourceLocation textureInsulationLR;
    private static ResourceLocation textureBundledFB;
    private static ResourceLocation textureBundledLR;

    private int power = 0;
    private int powerSelf = 0;
    private int[] powerArray = new int[16];

    private static boolean updateState;

    private boolean isItemRenderer = false;

    private int colorId = -1;
    private boolean isBundled = false;
    private boolean hasTicked = false;

    public WireBluestone(Integer colorId, Boolean bundled) {

        this.colorId = colorId.intValue();
        isBundled = bundled.booleanValue();
    }

    public WireBluestone(Boolean bundled) {

        isBundled = bundled.booleanValue();
    }

    public WireBluestone() {

    }

    @Override
    public String getType() {

        return "bluestoneWire" + (isBundled ? ".bundled" : "") + (colorId >= 0 ? "." + ItemDye.field_150921_b[colorId] : "");
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestoneWire" + (isBundled ? ".bundled" : "") + (colorId >= 0 ? "." + ItemDye.field_150921_b[colorId] : "");
    }

    @Override
    public int getRotation() {

        return 0;
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        if (cable != null && cable instanceof WireBluestone) {
            WireBluestone w = (WireBluestone) cable;

            if (isBundled) {
                if (w.isBundled) {
                    if (colorId == w.colorId || colorId == -1 || w.colorId == -1)
                        return true;
                } else {
                    if (w.colorId != -1)
                        return true;
                }
            } else {
                if (w.isBundled) {
                    if (colorId != -1)
                        return true;
                } else {
                    if (colorId == w.colorId || colorId == -1 || w.colorId == -1)
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        if (isBundled)
            return false;

        if (Loader.isModLoaded(Dependencies.FMP))
            if (location.hasTileEntity() && isFMPTile(location.getTileEntity()))
                return false;

        boolean cc = BPApi.getInstance().getBluestoneApi().canConnect(location, this, loc.getDirectionTo(location));
        try {
            return cc
                    || block.canConnectRedstone(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                            ForgeDirectionUtils.getSide(loc.getDirectionTo(location)));
        } catch (Exception ex) {
        }
        return cc;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT))
            if (canConnectToComputer(tile))
                return true;
        return false;
    }

    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    private boolean canConnectToComputer(TileEntity tile) {

        return tile.getClass().getName().equals("dan200.computercraft.shared.computer.blocks.TileComputer");
    }

    @Optional.Method(modid = Dependencies.FMP)
    private boolean isFMPTile(TileEntity tile) {

        return tile instanceof TileMultipart;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private boolean isFMPPart(Object o) {

        return o != null && o instanceof TMultiPart;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private int getFMPPower(Object o, ForgeDirection side) {

        TMultiPart part = (TMultiPart) o;

        if (part instanceof MultipartBPPart) {
            if (((MultipartBPPart) part).getPart() instanceof IBPRedstonePart) {
                return ((IBPRedstonePart) ((MultipartBPPart) part).getPart()).getWeakOutput(side);
            }
        }
        if (part instanceof IRedstonePart) {
            return ((IRedstonePart) part).weakPowerLevel(ForgeDirectionUtils.getSide(side));
        }

        return 0;
    }

    @Optional.Method(modid = Dependencies.FMP)
    public static TMultiPart getFMPPartOnSide(IBluestoneWire wire, ForgeDirection dir) {

        if (((WireBluestone) wire).isBundled)
            return null;

        IMultipartCompat compat = BPApi.getInstance().getMultipartCompat();

        Vector3 vec = wire.getLocation().getRelative(dir);
        if (!vec.hasTileEntity())
            return null;
        if (!(vec.getTileEntity() instanceof TileMultipart))
            return null;

        TileMultipart te = (TileMultipart) wire.getLocation().getTileEntity();

        if (te == null)
            return null;

        if (compat.isOccupied(te, getStripHitboxForSide(ForgeDirection.getOrientation(wire.getFace()), dir)))
            return null;

        // Check for parts in the same block
        List<TMultiPart> l = te.jPartList();
        for (TMultiPart p : l) {
            if (p instanceof IFaceRedstonePart) {
                ForgeDirection d = dir;
                if (p instanceof MultipartBPPart) {
                    if (((MultipartBPPart) p).getPart() instanceof WireBluestone)
                        continue;
                    d = d.getOpposite();
                }
                if (ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()) == d)
                    return p;
            }
        }

        // ForgeDirection dir2 = ForgeDirection.getOrientation(wire.getFace());

        // Check for parts next to this one
        if (vec.hasTileEntity() && vec.getTileEntity() instanceof TileMultipart) {
            te = ((TileMultipart) vec.getTileEntity());
            l = te.jPartList();
            for (TMultiPart p : l)
                if (p instanceof IFaceRedstonePart) {
                    // ForgeDirection d = dir2;
                    if (p instanceof MultipartBPPart) {
                        if (((MultipartBPPart) p).getPart() instanceof WireBluestone)
                            continue;
                        // d = d.getOpposite();
                    }
                    // if ((p instanceof MultipartBPPart && ((MultipartBPPart) p).getPart() instanceof GateBase)
                    // || !compat.isOccupied(
                    // te,
                    // getStripHitboxForSide(ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()).getOpposite(),
                    // dir.getOpposite())))
                    // if (ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()) == d)
                    return p;
                }
        }

        return null;
    }

    @Override
    protected TMultiPart getPartOnSide(ForgeDirection dir) {

        return getFMPPartOnSide(this, dir);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add((colorId == -1 && !isBundled ? SELECTION_BOX : SELECTION_BOX_INSULATED).clone().toAABB());
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add((colorId == -1 && !isBundled ? OCCLUSION_BOX : OCCLUSION_BOX_INSULATED).clone().toAABB());
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerCircuits;
    }

    @Override
    public boolean renderStatic(Vector3 loc, int pass) {

        if (pass == 0) {
            Tessellator t = Tessellator.instance;
            t.draw();

            if (textureOn == null)
                textureOn = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOn.png");
            if (textureOff == null)
                textureOff = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOff.png");
            if (textureInsulationFB == null)
                textureInsulationFB = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/insulation_fb.png");
            if (textureInsulationLR == null)
                textureInsulationLR = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/insulation_lr.png");
            if (textureBundledFB == null)
                textureBundledFB = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/bundled_nocolor_fb.png");
            if (textureBundledLR == null)
                textureBundledLR = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/bundled_nocolor_lr.png");

            GL11.glPushMatrix();
            {
                rotateAndTranslateDynamic(loc, pass, 0);

                int[] sides = new int[] { 0, 0, 0, 0 };

                ForgeDirection f = ForgeDirection.getOrientation(getFace());
                int id = 0;
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (d == f || d == f.getOpposite())
                        continue;

                    Vector3 v = (Vector3) connections[ForgeDirectionUtils.getSide(d)];
                    if (v == null) {
                        id++;
                        continue;
                    }

                    int len = 0;

                    if (!isItemRenderer)
                        len = BPApi.getInstance().getBluestoneApi().getExtraLength(v, this, d);
                    int val = 1 + (v.distanceTo(this.loc) > 1 ? 1 : len);

                    if (!isItemRenderer) {
                        GL11.glPushMatrix();
                        {
                            int times = 0;
                            switch (id) {
                            case 0:
                                times = 1;
                                break;
                            case 1:
                                times = 3;
                                break;
                            case 2:
                                times = 2;
                                break;
                            case 3:
                                break;
                            }
                            GL11.glTranslated(0.5, 0.5, 0.5);
                            GL11.glRotated(90 * times, 0, 1, 0);
                            GL11.glTranslated(-0.5, -0.5, -0.5);
                            BPApi.getInstance().getBluestoneApi().renderExtraCables(v, this, d);
                        }
                        GL11.glPopMatrix();
                    }

                    switch (f) {
                    case UP:
                        switch (d) {
                        case EAST:
                            sides[3] = val;
                            break;
                        case WEST:
                            sides[2] = val;
                            break;
                        case NORTH:
                            sides[1] = val;
                            break;
                        case SOUTH:
                            sides[0] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    case DOWN:
                        switch (d) {
                        case EAST:
                            sides[3] = val;
                            break;
                        case WEST:
                            sides[2] = val;
                            break;
                        case NORTH:
                            sides[0] = val;
                            break;
                        case SOUTH:
                            sides[1] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                        case UP:
                            sides[3] = val;
                            break;
                        case DOWN:
                            sides[2] = val;
                            break;
                        case NORTH:
                            sides[0] = val;
                            break;
                        case SOUTH:
                            sides[1] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                        case UP:
                            sides[2] = val;
                            break;
                        case DOWN:
                            sides[3] = val;
                            break;
                        case NORTH:
                            sides[0] = val;
                            break;
                        case SOUTH:
                            sides[1] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                        case UP:
                            sides[0] = val;
                            break;
                        case DOWN:
                            sides[1] = val;
                            break;
                        case EAST:
                            sides[3] = val;
                            break;
                        case WEST:
                            sides[2] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                        case UP:
                            sides[1] = val;
                            break;
                        case DOWN:
                            sides[0] = val;
                            break;
                        case EAST:
                            sides[3] = val;
                            break;
                        case WEST:
                            sides[2] = val;
                            break;
                        default:
                            break;
                        }
                        break;
                    default:
                        break;
                    }
                    id++;
                }

                if (power > 0)
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureOn);
                else
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureOff);

                if (!isBundled) {
                    if (colorId == -1) {
                        // Render center
                        renderBox(7, 0, 7, 9, 1, 9);
                    }
                    if (sides[3] > 0)// East
                        renderBox(0 - (sides[3] - 1), 0, 7, 7, 1, 9);
                    if (sides[2] > 0)// West
                        renderBox(9, 0, 7, 16 + (sides[2] - 1), 1, 9);
                    if (sides[1] > 0)// South
                        renderBox(7, 0, 0 - (sides[1] - 1), 9, 1, 7);
                    if (sides[0] > 0)// North
                        renderBox(7, 0, 9, 9, 1, 16 + (sides[0] - 1));
                }

                if (colorId >= 0 || isBundled) {
                    if (isBundled) {
                        int color = 0xFFFFFF;
                        if (colorId >= 0)
                            color = ItemDye.field_150922_c[colorId];
                        double r = ((color >> 16) & 0xFF) / 255D;
                        double g = ((color >> 8) & 0xFF) / 255D;
                        double b = (color & 0xFF) / 255D;
                        GL11.glColor4d((r * 0.7) + 0.1, (g * 0.7) + 0.1, (b * 0.7) + 0.1, 1);
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                        renderBox(7, 0, 7, 9, 3, 9);
                        if (sides[3] > 0) {// East
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            renderBox(0, 0, 5, 7, 3, 11);
                        } else {
                            if (sides[3] == 0 && sides[0] == 0 && sides[1] == 0 && sides[2] != 0)
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            else
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            renderBox(5, 0, 6 - (sides[1] == 0 ? 1 : 0), 7, 3, 10 + (sides[0] == 0 ? 1 : 0));
                        }
                        if (sides[2] > 0) {// West
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            renderBox(9, 0, 5, 16, 3, 11);
                        } else {
                            if (sides[2] == 0 && sides[0] == 0 && sides[1] == 0 && sides[3] != 0)
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            else
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            renderBox(9, 0, 6 - (sides[1] == 0 ? 1 : 0), 11, 3, 10 + (sides[0] == 0 ? 1 : 0));
                        }
                        if (sides[1] > 0) {// South
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            renderBox(5, 0, 0, 11, 3, 7);
                        } else {
                            if ((sides[1] == 0 && sides[2] == 0 && sides[3] == 0 && sides[0] != 0)
                                    || (sides[0] == 0 && sides[1] == 0 && sides[2] == 0 && sides[3] == 0))
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            else
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            renderBox(7, 0, 5, 9, 3, 7);
                        }
                        if (sides[0] > 0) {// North
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            renderBox(5, 0, 9, 11, 3, 16);
                        } else {
                            if ((sides[0] == 0 && sides[2] == 0 && sides[3] == 0 && sides[1] != 0)
                                    || (sides[0] == 0 && sides[1] == 0 && sides[2] == 0 && sides[3] == 0))
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledFB);
                            else
                                Minecraft.getMinecraft().renderEngine.bindTexture(textureBundledLR);
                            renderBox(7, 0, 9, 9, 3, 11);
                        }
                        GL11.glColor4d(1D, 1D, 1D, 1D);
                    } else {
                        int color = ItemDye.field_150922_c[colorId];
                        double r = ((color >> 16) & 0xFF) / 255D;
                        double g = ((color >> 8) & 0xFF) / 255D;
                        double b = (color & 0xFF) / 255D;
                        GL11.glColor4d((r * 0.7) + 0.1, (g * 0.7) + 0.1, (b * 0.7) + 0.1, 1);
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                        renderBox(7, 0, 7, 9, 3, 9);
                        if (sides[3] > 0) {// East
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                            renderBox(0, 0, 6, 7, 3, 10);
                        } else {
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                            renderBox(6, 0, 7 - (sides[1] == 0 ? 1 : 0), 7, 3, 9 + (sides[0] == 0 ? 1 : 0));
                        }
                        if (sides[2] > 0) {// West
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                            renderBox(9, 0, 6, 16, 3, 10);
                        } else {
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                            renderBox(9, 0, 7 - (sides[1] == 0 ? 1 : 0), 10, 3, 9 + (sides[0] == 0 ? 1 : 0));
                        }
                        if (sides[1] > 0) {// South
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                            renderBox(6, 0, 0, 10, 3, 7);
                        } else {
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                            renderBox(7, 0, 6, 9, 3, 7);
                        }
                        if (sides[0] > 0) {// North
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                            renderBox(6, 0, 9, 10, 3, 16);
                        } else {
                            Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                            renderBox(7, 0, 9, 9, 3, 10);
                        }
                        GL11.glColor4d(1D, 1D, 1D, 1D);
                    }
                }
            }
            GL11.glPopMatrix();

            t.startDrawingQuads();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        isItemRenderer = true;

        if (connections[ForgeDirectionUtils.getSide(ForgeDirection.EAST)] == null) {
            loc = new Vector3(0, -1, 0);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN)
                    continue;
                connections[ForgeDirectionUtils.getSide(d)] = new Vector3(0, 0, 0).add(d);
            }
            power = 15;
        }
        GL11.glPushMatrix();
        {
            GL11.glTranslated(0.1, 1, 0);
            switch (type) {
            case ENTITY:
                if (item.getItemFrame() != null) {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    GL11.glRotated(90, 0, 0, -1);
                    GL11.glTranslated(-0.375, -1, -0.5);
                }
                break;
            case EQUIPPED:
                GL11.glTranslated(0, -0.5, 0);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(-0.5, 0, 0);
                break;
            case INVENTORY:
                GL11.glTranslated(-0.125, -0.625, 0);
                GL11.glScaled(1.125, 1.125, 1.125);
                break;
            default:
                break;
            }
            Tessellator.instance.startDrawingQuads();
            renderStatic(new Vector3(0, 0, 0), 0);
            Tessellator.instance.draw();

        }
        GL11.glPopMatrix();
    }

    private void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz) {

        BPApi.getInstance().getBluestoneApi().renderBox(minx, miny, minz, maxx, maxy, maxz);
    }

    private void recalculatePower() {

        if (loc == null)
            loc = new Vector3(getX(), getY(), getZ(), getWorld());
        loc.setX(getX());
        loc.setY(getY());
        loc.setZ(getZ());
        loc.setWorld(getWorld());

        powerSelf = 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {

            ForgeDirection dir = d;
            if (dir == ForgeDirection.getOrientation(getFace()).getOpposite())// If it's in the opposite face, don't do anything
                continue;

            Vector3 l = null;// Get the block in that set of coords

            // If it's either disconnected on that side OR connected and it's not bluestone wire
            if (dir == ForgeDirection.getOrientation(getFace())
                    || (connections[ForgeDirectionUtils.getSide(d)] != null && !(connections[ForgeDirectionUtils.getSide(d)] instanceof WireBluestone))) {
                if (Loader.isModLoaded(Dependencies.FMP) && isFMPPart(connections[ForgeDirectionUtils.getSide(d)])) {
                    powerSelf = Math.max(powerSelf, getFMPPower(connections[ForgeDirectionUtils.getSide(d)], d.getOpposite()));
                } else {
                    if (connections[ForgeDirectionUtils.getSide(d)] instanceof Vector3) {
                        l = (Vector3) connections[ForgeDirectionUtils.getSide(d)];
                    } else if (connections[ForgeDirectionUtils.getSide(d)] instanceof BPPart) {
                        BPPart part = (BPPart) connections[ForgeDirectionUtils.getSide(d)];
                        l = new Vector3(part.getX(), part.getY(), part.getZ(), part.getWorld());
                    } else {
                        if (connections[ForgeDirectionUtils.getSide(d)] == null)
                            l = loc.getRelative(d);
                        else
                            continue;
                    }
                    if (l.getBlock(true) == null)
                        continue;
                    if (l.getBlock() instanceof BlockRedstoneWire)
                        continue;
                    if (l.getBlock().isOpaqueCube()) {
                        for (ForgeDirection di : ForgeDirection.VALID_DIRECTIONS) {
                            if (di == d.getOpposite())
                                continue;
                            if (BPApi.getInstance().getMultipartCompat()
                                    .getBPPartOnFace(l.getRelative(di).getTileEntity(), WireBluestone.class, di.getOpposite()) != null)
                                continue;
                            int p = RedstoneHelper.getInput(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), di, null, true, false,
                                    dir != ForgeDirection.getOrientation(getFace()));
                            powerSelf = Math.max(powerSelf, p);
                        }
                    } else {
                        powerSelf = Math.max(powerSelf, RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d));
                    }
                }
            }
        }

    }

    @Override
    public void onFirstTick() {

        for (FaceDirection d : FaceDirection.values()) {
            RedstoneConnection c = getConnectionOrCreate(d);
            c.enable();
            c.setOutput();
        }

        super.onFirstTick();

        if (isBundled) {
            try {
                propagate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void update() {

        super.update();

        hasTicked = true;
    }

    @Override
    public void onUpdate() {

        if (!updateState) {
            updateState = true;

            super.onUpdate();

            if (getWorld() != null && !getWorld().isRemote) {
                if (!isBundled) {
                    try {
                        propagate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            updateState = false;
        }
    }

    private void propagate() {

        if (getWorld() == null)
            return;
        if (getWorld().isRemote)
            return;

        List<Entry<WireBluestone, Integer>> wires = new ArrayList<Entry<WireBluestone, Integer>>();
        int[] power = new int[] { 0 };

        if (isBundled) {
            for (int i = 0; i < 16; i++)
                propagate(wires, power, i);
        } else {
            propagate(wires, power, colorId);
        }

        for (Entry<WireBluestone, Integer> entry : wires) {
            WireBluestone wire = entry.getKey();
            int wireColorId = entry.getValue().intValue();
            if (!wire.isBundled) {
                wire.power = power[0];

                if (wire.hasSetFace()) {
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        RedstoneConnection con = wire.getConnection(d);
                        if (con != null)
                            con.setPower(0, false);
                    }
                    for (int i = 0; i < 6; i++) {
                        Object o = wire.connections[i];
                        RedstoneConnection con = wire.getConnection(ForgeDirection.getOrientation(i));
                        if (con != null) {
                            if (o != null) {
                                if (!(o instanceof WireBluestone)) {
                                    con.setPower(power[0], false);
                                } else {
                                    con.setPower(0, false);
                                }
                            } else {
                                con.setPower(0, false);
                            }
                        }
                    }
                    wire.notifyRedstoneUpdate();
                }
            } else {
                try {
                    wire.powerArray[wireColorId] = power[0];
                } catch (Exception ex) {
                }
            }

            wire.sendUpdatePacket();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void propagate(List<Entry<WireBluestone, Integer>> wires, int[] power, int colorId) {

        if (!isBundled)
            recalculatePower();

        wires.add(new AbstractMap.SimpleEntry(this, isBundled ? colorId : this.colorId));

        power[0] = Math.max(power[0], powerSelf);

        for (int i = 0; i < 6; i++)
            if (connections[i] != null)
                if (connections[i] instanceof WireBluestone)
                    if (((WireBluestone) connections[i]).colorId == colorId || colorId == -1 || (!isBundled && this.colorId == -1)
                            || ((WireBluestone) connections[i]).colorId == -1)
                        if (!isInList(wires, (WireBluestone) connections[i]))
                            ((WireBluestone) connections[i]).propagate(wires, power, isBundled ? colorId : this.colorId);
    }

    private static boolean isInList(List<Entry<WireBluestone, Integer>> wires, WireBluestone wire) {

        for (Entry<WireBluestone, Integer> w : wires)
            if (w.getKey() == wire)
                return true;
        return false;
    }

    private void notifyRedstoneUpdate() {

        if (loc != null) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                Vector3 v = loc.getRelative(d);
                getWorld().notifyBlockChange(v.getBlockX(), v.getBlockY(), v.getBlockZ(), loc.getBlock());
                getWorld().markBlockForUpdate(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            }
        }
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        tag.setInteger("power", power);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        power = tag.getInteger("power");
    }

    public int getPower() {

        return colorId == -1 && !isBundled ? power : 0;
    }

    @Override
    public Vector3 getLocation() {

        return loc;
    }

    @Override
    public int getCableWidth() {

        return 2 + (colorId >= 0 ? 2 : 0);
    }

    @Override
    public int getCableHeight() {

        return 1 + (colorId >= 0 ? 2 : 0);
    }

    @Override
    public void onConnect(Object o) {

        if (updateState) {
            if (o instanceof WireBluestone) {
                WireBluestone w = (WireBluestone) o;
                if (w.loc != null) {
                    if (loc.getDirectionTo(w.loc) == null || !hasTicked) {
                        updateState = false;
                        w.onUpdate();
                        updateState = true;
                    }
                }
            }
        }
    }

    @Override
    public void onDisconnect(Object o) {

        if (updateState) {
            if (o instanceof WireBluestone) {
                WireBluestone w = (WireBluestone) o;
                if (w.loc != null) {
                    if (loc.getDirectionTo(w.loc) == null || !hasTicked) {
                        updateState = false;
                        w.onUpdate();
                        updateState = true;
                    }
                }
            }
        }
    }

    private boolean emit = true;

    @Override
    public int getRedstonePower() {

        if (colorId != -1 || isBundled)
            return 0;

        return emit ? power : 0;
    }

    @Override
    public int getStrongOutput(ForgeDirection side) {

        return getWeakOutput(side);
    }

    @Override
    public int getWeakOutput(ForgeDirection side) {

        if (colorId != -1 || isBundled)
            return 0;

        return emit ? super.getWeakOutput(side) : 0;
    }

    @Override
    public float getHardness() {

        return 0.25F;
    }

    @Override
    public boolean canStay() {

        return super.canStay();
    }

    @Override
    public void onRemoved() {

        emit = false;

        super.onRemoved();

        notifyRedstoneUpdate();
    }

    public boolean isBundled() {

        return isBundled;
    }

    public int[] getPowerArray() {

        return powerArray;
    }

}
