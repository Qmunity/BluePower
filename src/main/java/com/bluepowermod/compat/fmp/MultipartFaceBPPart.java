/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat.fmp;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.IBPFacePart;
import com.bluepowermod.api.part.RedstoneConnection;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TileMultipart;

public class MultipartFaceBPPart extends MultipartBPPart implements TFacePart, IFaceRedstonePart {
    
    private IBPFacePart facePart;
    
    public MultipartFaceBPPart(IBPFacePart part) {
    
        super((BPPart) part);
        facePart = part;
    }
    
    @Override
    public int getSlotMask() {
    
        return 1 << getFace();
    }
    
    @Override
    public int redstoneConductionMap() {
    
        return 0;
    }
    
    @Override
    public boolean solid(int side) {
    
        return false;
    }
    
    @Override
    public int getFace() {
    
        return facePart.getFace() ^ 1;
    }
    
    @Override
    public void onNeighborChanged() {
    
        if (!facePart.canStay()) {
            for (ItemStack is : getPart().getDrops()) {
                TileMultipart.dropItem(is, world(), Vector3.fromTileEntityCenter(tile()));
            }
            tile().remPart(this);
            return;
        }
        
        super.onNeighborChanged();
    }
    
    @Override
    public void update() {
    
        super.update();
    }
    
    @Override
    public void writeDesc(MCDataOutput packet) {
    
        super.writeDesc(packet);
        packet.writeInt(facePart.getFace());
        packet.writeInt(facePart.getRotation());
        for (int i = 0; i < 4; i++) {
            RedstoneConnection c = ((BPPartFace) getPart()).getConnection(FaceDirection.getDirection(i));
            if (c != null) {
                packet.writeNBTTagCompound(c.getNBTTag());
            } else {
                NBTTagCompound t = new NBTTagCompound();
                t.setBoolean("___error", true);
                packet.writeNBTTagCompound(t);
            }
        }
    }
    
    @Override
    public void readDesc(MCDataInput packet) {
    
        super.readDesc(packet);
        facePart.setFace(packet.readInt());
        facePart.setRotation(packet.readInt());
        for (int i = 0; i < 4; i++) {
            RedstoneConnection c = ((BPPartFace) getPart()).getConnection(FaceDirection.getDirection(i));
            NBTTagCompound t = packet.readNBTTagCompound();
            if (t.hasKey("___error") && t.getBoolean("___error")) {
                continue;
            } else {
                if (c == null) {
                    c = ((BPPartFace) getPart()).getConnectionOrCreate(FaceDirection.getDirection(i));
                }
                c.load(t);
            }
        }
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setInteger("face", facePart.getFace());
        tag.setInteger("rotation", facePart.getRotation());
        for (int i = 0; i < 4; i++) {
            RedstoneConnection c = ((BPPartFace) getPart()).getConnection(FaceDirection.getDirection(i));
            if (c != null) {
                tag.setTag("con_" + i, c.getNBTTag());
            } else {
                NBTTagCompound t = new NBTTagCompound();
                t.setBoolean("___error", true);
                tag.setTag("con_" + i, t);
            }
        }
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        facePart.setFace(tag.getInteger("face"));
        facePart.setRotation(tag.getInteger("rotation"));
        for (int i = 0; i < 4; i++) {
            RedstoneConnection c = ((BPPartFace) getPart()).getConnection(FaceDirection.getDirection(i));
            NBTTagCompound t = tag.getCompoundTag("con_" + i);
            if (t.hasKey("___error") && t.getBoolean("___error")) {
                continue;
            } else {
                if (c == null) {
                    c = ((BPPartFace) getPart()).getConnectionOrCreate(FaceDirection.getDirection(i));
                }
                c.load(t);
            }
        }
    }
    
}
