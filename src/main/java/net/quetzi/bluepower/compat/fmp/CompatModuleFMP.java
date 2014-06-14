/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat.fmp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.compat.CompatModule;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.util.RayTracer;
import codechicken.lib.raytracer.ExtendedMOP;
import codechicken.lib.vec.Cuboid6;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.handler.MultipartProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CompatModuleFMP extends CompatModule implements IMultipartCompat {
    
    @Override
    public void preInit(FMLPreInitializationEvent ev) {
    
    }
    
    @Override
    public void init(FMLInitializationEvent ev) {
    
        RegisterMultiparts.register();
        
        registerBlocksAsMicroblock();
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent ev) {
    
        BPBlocks.multipart = MultipartProxy.block();
    }
    
    private void registerBlocksAsMicroblock() {
    
        registerBlockAsMicroblock(BPBlocks.basalt);
        registerBlockAsMicroblock(BPBlocks.basalt_brick);
        registerBlockAsMicroblock(BPBlocks.basalt_brick_small);
        registerBlockAsMicroblock(BPBlocks.basalt_cobble);
        registerBlockAsMicroblock(BPBlocks.basalt_tile);
        registerBlockAsMicroblock(BPBlocks.basalt_paver);
        
        registerBlockAsMicroblock(BPBlocks.fancy_basalt);
        registerBlockAsMicroblock(BPBlocks.fancy_marble);
        
        registerBlockAsMicroblock(BPBlocks.marble);
        registerBlockAsMicroblock(BPBlocks.marble_brick);
        registerBlockAsMicroblock(BPBlocks.marble_brick_small);
        registerBlockAsMicroblock(BPBlocks.marble_tile);
        registerBlockAsMicroblock(BPBlocks.marble_paver);
        
        registerBlockAsMicroblock(BPBlocks.amethyst_block);
        registerBlockAsMicroblock(BPBlocks.ruby_block);
        registerBlockAsMicroblock(BPBlocks.sapphire_block);
        registerBlockAsMicroblock(BPBlocks.copper_block);
        registerBlockAsMicroblock(BPBlocks.silver_block);
        registerBlockAsMicroblock(BPBlocks.tin_block);
    }
    
    private void registerBlockAsMicroblock(Block b) {
    
        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(b, 0), b.getUnlocalizedName());
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
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, ItemStack item, EntityPlayer player) {
    
        TileMultipart te = (TileMultipart) loc.getTileEntity();
        
        List<ExtendedMOP> mops = new ArrayList<ExtendedMOP>();
        for (int i = 0; i < te.jPartList().size(); i++) {
            ExtendedMOP mop = te.jPartList().get(i).collisionRayTrace(RayTracer.getStartVec(player), RayTracer.getEndVec(player));
            if (mop != null) {
                mop.setData(i);
                mops.add(mop);
            }
        }
        if (mops.isEmpty()) return null;
        Collections.sort(mops);
        TMultiPart p = te.jPartList().get((Integer) ExtendedMOP.getData(mops.get(0)));
        
        if (p instanceof MultipartBPPart) {
            return ((MultipartBPPart) p).getPart();
        } else {
            return null;
        }
        
    }
    
    @Override
    public int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {
    
        return 0;
    }
    
    @Override
    public void sendUpdatePacket(BPPart part) {
    
        TileEntity tile = part.world.getTileEntity(part.x, part.y, part.z);
        if (tile != null && tile instanceof TileMultipart) {
            TileMultipart te = (TileMultipart) tile;
            for (TMultiPart p : te.jPartList()) {
                if (p instanceof MultipartBPPart) {
                    MultipartBPPart bpp = (MultipartBPPart) p;
                    if (bpp.getPart() == part) {
                        bpp.sendDescUpdate();
                        return;
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isMultipart(TileEntity te) {
    
        return te instanceof TileMultipart;
    }
    
    @Override
    public boolean checkOcclusion(TileEntity tile, AxisAlignedBB box) {
    
        if (tile != null && tile instanceof TileMultipart) {
            TileMultipart te = (TileMultipart) tile;
            
            NormallyOccludedPart noc = new NormallyOccludedPart(new Cuboid6(box));
            
            for (TMultiPart p : te.jPartList()) {
                if (!p.occlusionTest(noc)) return true;
            }
        }
        
        return false;
    }
}
