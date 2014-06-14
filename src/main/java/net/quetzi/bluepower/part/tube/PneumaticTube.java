package net.quetzi.bluepower.part.tube;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.references.Dependencies;

public class PneumaticTube extends BPPart {
    
    private final boolean[]   connections = new boolean[6];
    private final Vector3Cube sideBB      = new Vector3Cube(AxisAlignedBB.getBoundingBox(0.25, 0, 0.25, 0.75, 0.25, 0.75));
    
    @Override
    public String getType() {
    
        return "pneumaticTube";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "pneumaticTube";
    }
    
    /**
     * Gets all the collision boxes for this block
     * 
     * @return A list with the collision boxes
     */
    @Override
    public List<AxisAlignedBB> getCollisionBoxes() {
    
        return getSelectionBoxes();
    }
    
    /**
     * Gets all the selection boxes for this block
     * 
     * @return A list with the selection boxes
     */
    @Override
    public List<AxisAlignedBB> getSelectionBoxes() {
    
        List<AxisAlignedBB> aabbs = getOcclusionBoxes();
        for (int i = 0; i < 6; i++) {
            if (connections[i]) {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN) d = d.getOpposite();
                Vector3Cube c = sideBB.clone().rotate90Degrees(d);
                aabbs.add(c.toAABB());
            }
        }
        return aabbs;
    }
    
    /**
     * Gets all the occlusion boxes for this block
     * 
     * @return A list with the occlusion boxes
     */
    @Override
    public List<AxisAlignedBB> getOcclusionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        aabbs.add(AxisAlignedBB.getBoundingBox(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
        return aabbs;
    }
    
    /**
     * Event called whenever a nearby block updates
     */
    @Override
    public void onNeighborUpdate() {
    
        updateConnections();
    }
    
    @Override
    public void onPartChanged() {
    
        updateConnections();
    }
    
    /**
     * Event called when the part is placed in the world
     */
    @Override
    public void onAdded() {
    
        super.onAdded();
        updateConnections();
    }
    
    private void updateConnections() {
    
        if (world == null) return;
        
        if (!world.isRemote) {
            Vector3 loc = new Vector3(x, y, z, world);
            IMultipartCompat compat = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP));
            for (int i = 0; i < 6; i++) {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                Vector3 l = loc.getRelative(d);
                TileEntity neighbor = world.getTileEntity(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                connections[i] = IOHelper.canInterfaceWith(neighbor, ForgeDirection.getOrientation(i).getOpposite());
                
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN) d = d.getOpposite();
                if (!connections[i]) {
                    connections[i] = checkOcclusion(sideBB.clone().rotate90Degrees(d).toAABB());
                    if (compat.isMultipart(neighbor)) {
                        connections[i] = compat.checkOcclusion(neighbor, sideBB.clone().rotate90Degrees(d.getOpposite()).toAABB());
                    }
                }
            }
            sendUpdatePacket();
        }
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        for (int i = 0; i < 6; i++) {
            tag.setBoolean("connections" + i, connections[i]);
        }
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        for (int i = 0; i < 6; i++) {
            connections[i] = tag.getBoolean("connections" + i);
        }
    }
    
}
