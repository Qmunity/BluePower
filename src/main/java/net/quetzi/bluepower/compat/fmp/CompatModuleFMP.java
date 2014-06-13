/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.compat.CompatModule;
import net.quetzi.bluepower.init.BPBlocks;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
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
    public BPPart getClickedPart(Vector3 loc, Vector3 subLoc, MovingObjectPosition original, ItemStack item, EntityPlayer player) {
    
        BPPart part = null;
        
        TileMultipart te = (TileMultipart) loc.getTileEntity();
        for (TMultiPart p : te.jPartList()) {
            if (!(p instanceof MultipartBPPart)) continue;
            
            List<IndexedCuboid6> l = new ArrayList<IndexedCuboid6>();
            for (IndexedCuboid6 c : p.getSubParts()) {
                IndexedCuboid6 c1 = (IndexedCuboid6) c;
                c1.min.add(loc.getX(), loc.getY(), loc.getZ());
                c1.max.add(loc.getX(), loc.getY(), loc.getZ());
                l.add(c1);
            }
            
            MovingObjectPosition mop = RayTracer.instance().rayTraceCuboids(new codechicken.lib.vec.Vector3(RayTracer.getStartVec(player)),
                    new codechicken.lib.vec.Vector3(RayTracer.getEndVec(player)), l, loc.toBlockCoord(), loc.getBlock());
            if (mop == null) continue;
            
            float dist = (float) new Vector3(mop.hitVec).distanceTo(new Vector3(original.hitVec));
            
            if (dist > 0) return null;
            return ((MultipartBPPart) p).getPart();
        }
        
        return part;
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
                    if(bpp.getPart() == part){
                        bpp.sendDescUpdate();
                        return;
                    }
                }
            }
        }
    }
}
