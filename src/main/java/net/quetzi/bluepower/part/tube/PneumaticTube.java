package net.quetzi.bluepower.part.tube;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.helper.TileEntityCache;
import net.quetzi.bluepower.references.Dependencies;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.NormallyOccludedPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.Optional;

public class PneumaticTube extends BPPart {
    
    private final boolean[]   connections = new boolean[6];
    private TileEntityCache[] tileCache;
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
                aabbs.add(sideBB.rotate90Degrees(ForgeDirection.getOrientation(i)).toAABB());
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
    
    private TileEntityCache[] getTileCache() {
    
        if (tileCache == null) {
            tileCache = TileEntityCache.getDefaultCache(world, x, y, z);
        }
        return tileCache;
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
        //      updateConnections();
    }
    
    private void updateConnections() {
    
        if (!world.isRemote) {
            for (int i = 0; i < 6; i++) {
                getTileCache()[i].update();
                TileEntity neighbor = getTileCache()[i].getTileEntity();
                connections[i] = IOHelper.canInterfaceWith(neighbor, ForgeDirection.getOrientation(i).getOpposite());
                // if (connections[i] && isFMPMultipart()) connections[i] = !isOccludedByMultipart(ForgeDirection.getOrientation(i));
            }
            sendUpdatePacket();
        }
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    private boolean isOccludedByMultipart(ForgeDirection side) {
    
        return !((TileMultipart) tile()).canAddPart(new NormallyOccludedPart(new Cuboid6(sideBB.rotate90Degrees(side).toAABB())));
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
    
    /**
     * TODO improve handling on this, probably put this in the BPPart class, and give it the non FMP TileEntity when no part.
     * if not possible (should be), cache this.
     * @return
     */
    private TileEntity tile() {
    
        return world.getTileEntity(x, y, z);
    }
    
}
