package net.quetzi.bluepower.part.tube;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.IconSupplier;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.helper.TileEntityCache;
import net.quetzi.bluepower.init.CustomTabs;

public class PneumaticTube extends BPPart {
    
    private final boolean[]   connections = new boolean[6];
    private final Vector3Cube sideBB      = new Vector3Cube(AxisAlignedBB.getBoundingBox(0.25, 0, 0.25, 0.75, 0.25, 0.75));
    private TileEntityCache[] tileCache;
    
    // private final ResourceLocation tubeSideTexture = new ResourceLocation(Refs.MODID + ":textures/blocks/Tubes/pneumatic_tube_side.png");
    // private final ResourceLocation tubeNodeTexture = new ResourceLocation(Refs.MODID + ":textures/blocks/Tubes/tube_end.png");
    
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
        // updateConnections();
    }
    
    private TileEntityCache[] getTileCache() {
    
        if (tileCache == null) {
            tileCache = TileEntityCache.getDefaultCache(world, x, y, z);
        }
        return tileCache;
    }
    
    private void updateConnections() {
    
        if (world == null) return;
        
        if (!world.isRemote) {
            for (int i = 0; i < 6; i++) {
                getTileCache()[i].update();
                ForgeDirection d = ForgeDirection.getOrientation(i);
                TileEntity neighbor = getTileCache()[i].getTileEntity();
                connections[i] = IOHelper.canInterfaceWith(neighbor, ForgeDirection.getOrientation(i).getOpposite());
                
                if (connections[i]) {
                    connections[i] = isConnected(d);
                }
            }
            sendUpdatePacket();
        }
    }
    
    public boolean isConnected(ForgeDirection dir) {
    
        if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) dir = dir.getOpposite();
        return world == null || !checkOcclusion(sideBB.clone().rotate90Degrees(dir).toAABB());
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
     * This render method gets called every tick. You should use this if you're doing animations
     * 
     * @param loc
     *            Distance from the player's position
     * @param pass
     *            Render pass (0 or 1)
     * @param frame
     *            Partial tick for smoother animations
     */
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        connections[0] = true;
        
        Tessellator t = Tessellator.instance;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        t.startDrawingQuads();
        List<AxisAlignedBB> aabbs = getSelectionBoxes();
        
        boolean shouldRenderNode = false;
        int connectionCount = 0;
        for (int i = 0; i < 6; i += 2) {
            if (connections[i] != connections[i + 1]) {
                shouldRenderNode = true;
                break;
            }
            if (connections[i]) connectionCount++;
            if (connections[i + 1]) connectionCount++;
        }
        if (shouldRenderNode || connectionCount == 0 || connectionCount > 2) {
            renderMiddle(aabbs.get(0), IconSupplier.pneumaticTubeNodeIcon);
        } else {
            renderMiddle(aabbs.get(0), IconSupplier.pneumaticTubeSideIcon);
        }
        for (int i = 1; i < aabbs.size(); i++) {
            renderTexturedCuboid(aabbs.get(i), IconSupplier.pneumaticTubeSideIcon);
        }
        t.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
    }
    
    private void renderMiddle(AxisAlignedBB aabb, IIcon icon) {
    
        Tessellator t = Tessellator.instance;
        
        if (!connections[2]) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minY = icon.getInterpolatedV(aabb.minY * 16);
            double maxY = icon.getInterpolatedV(aabb.maxY * 16);
            
            t.setNormal(0, 0, -1);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxY);// minZ
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, minY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, maxY);
                
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxY);// minZ
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, maxY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, minY);
            } else {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxY);// minZ
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxX, maxY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minX, minY);
                
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxY);// minZ
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minX, minY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxX, maxY);
            }
        }
        
        if (!connections[3]) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minY = icon.getInterpolatedV(aabb.minY * 16);
            double maxY = icon.getInterpolatedV(aabb.maxY * 16);
            t.setNormal(0, 0, 1);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxX, maxY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
                
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxX, maxY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minX, minY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
            } else {
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, minY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, maxY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
                
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, maxY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
            }
        }
        
        if (!connections[0]) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(0, -1, 0);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxX, maxZ);// bottom
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, maxX, minZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxX, maxZ);// bottom
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minX, minZ);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, maxX, minZ);
                
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxX, maxZ);// bottom
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, maxX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minX, maxZ);
            }
        }
        
        if (!connections[1]) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(0, 1, 0);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, maxZ);// top
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, minX, minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, maxZ);
                
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, maxZ);// top
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, minX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minZ);// top
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minX, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, minX, maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxX, maxZ);
                
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxX, minZ);// top
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxX, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, minX, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minX, minZ);
            }
        }
        
        if (!connections[4]) {
            double minY = icon.getInterpolatedU(aabb.minY * 16);
            double maxY = icon.getInterpolatedU(aabb.maxY * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(-1, 0, 0);
            if (connections[0]) {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxY, maxZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, minY, minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
                
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, maxY, maxZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, minY, minZ);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minY, minZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, maxY, maxZ);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
                
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minY, minZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, maxY, maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
            }
        }
        
        if (!connections[5]) {
            double minY = icon.getInterpolatedU(aabb.minY * 16);
            double maxY = icon.getInterpolatedU(aabb.maxY * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(1, 0, 0);
            if (connections[0]) {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxY, maxZ);
                
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxY, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, minY, minZ);
            } else {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxY, maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minY, minZ);
                
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxY, maxZ);
            }
        }
    }
    
    public void renderTexturedCuboid(AxisAlignedBB aabb, IIcon icon) {
    
        Tessellator t = Tessellator.instance;
        
        if (aabb.minZ != 0 && (!connections[3] || aabb.minZ != 0.75)) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minY = icon.getInterpolatedV(aabb.minY * 16);
            double maxY = icon.getInterpolatedV(aabb.maxY * 16);
            t.setNormal(0, 0, -1);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, maxX, maxY);// minZ
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, maxY);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, minX, minY);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minY);
            
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, maxX, maxY);// minZ
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minY);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, minX, minY);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, maxY);
        }
        
        if (aabb.maxZ != 1 && (!connections[2] || aabb.maxZ != 0.25)) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minY = icon.getInterpolatedV(aabb.minY * 16);
            double maxY = icon.getInterpolatedV(aabb.maxY * 16);
            t.setNormal(0, 0, 1);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, minY);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, maxY);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
            
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, minY);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, minY);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, maxY);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, maxY);// maxZ
        }
        
        if (aabb.minY != 0 && (!connections[1] || aabb.minY != 0.75)) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(0, -1, 0);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, maxZ);// bottom
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, maxX, maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, minZ);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, minZ);
            
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minX, maxZ);// bottom
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minX, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, maxX, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, maxX, maxZ);
        }
        
        if (aabb.maxY != 1 && (!connections[0] || aabb.maxY != 0.25)) {
            double minX = icon.getInterpolatedU(aabb.minX * 16);
            double maxX = icon.getInterpolatedU(aabb.maxX * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(0, 1, 0);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, minX, minZ);// top
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, maxZ);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, maxZ);
            
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, minX, minZ);// top
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, minX, maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxX, maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxX, minZ);
        }
        
        if (aabb.minX != 0 && (!connections[5] || aabb.minX != 0.75)) {
            double minY = icon.getInterpolatedU(aabb.minY * 16);
            double maxY = icon.getInterpolatedU(aabb.maxY * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(-1, 0, 0);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minY, minZ);// minX
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxY, maxZ);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, minY, minZ);// minX
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, maxY, minZ);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, maxY, maxZ);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, minY, maxZ);
        }
        
        if (aabb.maxX != 1 && (!connections[4] || aabb.maxX != 0.25)) {
            double minY = icon.getInterpolatedU(aabb.minY * 16);
            double maxY = icon.getInterpolatedU(aabb.maxY * 16);
            double minZ = icon.getInterpolatedV(aabb.minZ * 16);
            double maxZ = icon.getInterpolatedV(aabb.maxZ * 16);
            t.setNormal(1, 0, 0);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxY, maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minY, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, minY, maxZ);// maxX
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, minY, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, maxY, minZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, maxY, maxZ);
            
        }
    }
    
    /**
     * This render method gets called whenever there's a block update in the chunk. You should use this to remove load from the renderer if a part of
     * the rendering code doesn't need to get called too often or just doesn't change at all. To call a render update to re-render this just call
     * {@link BPPart#markPartForRenderUpdate()}
     * 
     * @param loc
     *            Distance from the player's position
     * @param pass
     *            Render pass (0 or 1)
     * @return Whether or not it rendered something
     */
    @Override
    public boolean renderStatic(Vector3 loc, int pass) {
    
        Tessellator t = Tessellator.instance;
        t.addTranslation((float) loc.getX(), (float) loc.getY(), (float) loc.getZ());
        List<AxisAlignedBB> aabbs = getSelectionBoxes();
        
        boolean shouldRenderNode = false;
        int connectionCount = 0;
        for (int i = 0; i < 6; i += 2) {
            if (connections[i] != connections[i + 1]) {
                shouldRenderNode = true;
                break;
            }
            if (connections[i]) connectionCount++;
            if (connections[i + 1]) connectionCount++;
        }
        if (shouldRenderNode || connectionCount == 0 || connectionCount > 2) {
            renderMiddle(aabbs.get(0), IconSupplier.pneumaticTubeNodeIcon);
        } else {
            renderMiddle(aabbs.get(0), IconSupplier.pneumaticTubeSideIcon);
        }
        for (int i = 1; i < aabbs.size(); i++) {
            renderTexturedCuboid(aabbs.get(i), IconSupplier.pneumaticTubeSideIcon);
        }
        t.addTranslation((float) -loc.getX(), (float) -loc.getY(), (float) -loc.getZ());
        return true;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerMachines;
    }
}
