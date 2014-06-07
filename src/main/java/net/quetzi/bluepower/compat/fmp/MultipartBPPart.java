package net.quetzi.bluepower.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPRedstonePart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.references.Refs;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.TMultiPart;

public class MultipartBPPart extends TMultiPart implements IRedstonePart,
        JNormalOcclusion {
    
    private BPPart part;
    
    public MultipartBPPart(BPPart part) {
    
        this.part = part;
    }
    
    @Override
    public String getType() {
    
        return Refs.MODID + "_" + part.getType();
    }
    
    @Override
    public void writeDesc(MCDataOutput packet) {
    
        super.writeDesc(packet);
        
        packet.writeString(part.getType());
    }
    
    @Override
    public void readDesc(MCDataInput packet) {
    
        super.readDesc(packet);
        
        String type = packet.readString();
        if (part == null) part = PartRegistry.createPart(type);
    }
    
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        
        for (AxisAlignedBB aabb : part.getCollisionBoxes())
            cubes.add(new Cuboid6(aabb));
        
        return cubes;
    }
    
    @Override
    public Iterable<IndexedCuboid6> getSubParts() {
    
        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();
        
        for (AxisAlignedBB aabb : part.getSelectionBoxes())
            cubes.add(new IndexedCuboid6(0, new Cuboid6(aabb)));
        
        return cubes;
    }
    
    // Redstone
    
    @Override
    public boolean canConnectRedstone(int side) {
    
        if (part instanceof IBPRedstonePart) return ((IBPRedstonePart) part)
                .canConnect(ForgeDirection.getOrientation(side));
        
        return false;
    }
    
    @Override
    public int strongPowerLevel(int side) {
    
        if (part instanceof IBPRedstonePart) return ((IBPRedstonePart) part)
                .getStrongOutput(ForgeDirection.getOrientation(side));
        
        return 0;
    }
    
    @Override
    public int weakPowerLevel(int side) {
    
        if (part instanceof IBPRedstonePart) return ((IBPRedstonePart) part)
                .getWeakOutput(ForgeDirection.getOrientation(side));
        
        return 0;
    }
    
    // Occlusion
    
    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        
        for (Cuboid6 c : getSubParts())
            cubes.add(c);
        
        return cubes;
    }
    
}
