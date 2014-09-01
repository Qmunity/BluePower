/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleFMPAlt extends CompatModule implements IMultipartCompat {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

        MultipartCompat.tile = BPTileMultipart.class;
    }

    @Override
    public void init(FMLInitializationEvent ev) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerRenders() {

    }

    @Override
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, EntityPlayer player, TileEntity tile) {

        return null;
    }

    @Override
    public void removePart(TileEntity tile, BPPart part) {

        BPTileMultipart te = (BPTileMultipart) tile;
        te.removePart(part);
    }

    @Override
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        return 0;
    }

    @Override
    public void sendUpdatePacket(BPPart part) {

        TileEntity te = part.getWorld().getTileEntity(part.getX(), part.getY(), part.getZ());
        if (isMultipart(te)) {
            ((BPTileMultipart) te).sendUpdatePacket();
        }
    }

    @Override
    public boolean isMultipart(TileEntity te) {

        return te instanceof BPTileMultipart;
    }

    @Override
    public boolean isOccupied(TileEntity tile, AxisAlignedBB box) {

        if (isMultipart(tile)) {
            BPTileMultipart multipart = (BPTileMultipart) tile;
            for (BPPart part : multipart.getParts()) {
                for (AxisAlignedBB occBox : part.getOcclusionBoxes()) {
                    if (occBox.intersectsWith(box))
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public <T> T getBPPart(TileEntity te, Class<T> searchedClass) {

        List<T> l = getBPParts(te, searchedClass);
        if (l.size() >= 1)
            return l.get(0);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getBPParts(TileEntity te, Class<T> searchedClass) {

        List<T> l = new ArrayList<T>();
        if (isMultipart(te)) {
            for (BPPart part : ((BPTileMultipart) te).getParts()) {
                if (searchedClass.isAssignableFrom(part.getClass())) {
                    l.add((T) part);
                }
            }
        }
        return l;
    }

    @Override
    public <T> T getBPPartOnFace(TileEntity te, Class<T> searchedClass, ForgeDirection face) {

        List<T> parts = getBPParts(te, searchedClass);
        for (T p : parts)
            if (p instanceof BPPartFace)
                if (ForgeDirection.getOrientation(((BPPartFace) p).getFace()) == face)
                    return p;
        return null;
    }

    @Override
    public int getMOPData(MovingObjectPosition mop) {

        return (Integer) mop.hitInfo;// TODO assign the subpart index hit to right clicking (for the Pneumatic Tube).
    }

}
