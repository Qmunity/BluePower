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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.helper.TileEntityCache;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

public class PneumaticTube extends BPPart {
    
    private final boolean[]        connections     = new boolean[6];
    private final Vector3Cube      sideBB          = new Vector3Cube(AxisAlignedBB.getBoundingBox(0.25, 0, 0.25, 0.75, 0.25, 0.75));
    private TileEntityCache[]      tileCache;
    private final ResourceLocation tubeSideTexture = new ResourceLocation(Refs.MODID + ":textures/blocks/Tubes/pneumatic_tube_side.png");
    private final ResourceLocation tubeNodeTexture = new ResourceLocation(Refs.MODID + ":textures/blocks/Tubes/tube_end.png");
    
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
                
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN) d = d.getOpposite();
                if (connections[i]) {
                    // connections[i] = !checkOcclusion(sideBB.clone().rotate90Degrees(d).toAABB());
                }
            }
            // BluePower.log.info("connections: " + Arrays.toString(connections) + "  object : " + this);
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
    
        GL11.glPushMatrix();
        GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
        GL11.glDisable(GL11.GL_CULL_FACE);
        
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
            Minecraft.getMinecraft().renderEngine.bindTexture(tubeNodeTexture);
            renderMiddle(aabbs.get(0));
            Minecraft.getMinecraft().renderEngine.bindTexture(tubeSideTexture);
        } else {
            Minecraft.getMinecraft().renderEngine.bindTexture(tubeSideTexture);
            renderMiddle(aabbs.get(0));
        }
        for (int i = 1; i < aabbs.size(); i++) {
            renderTexturedCuboid(aabbs.get(i));
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        /*    GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            
            List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
            aabbs.addAll(getCollisionBoxes());
            aabbs.add(sideBB.clone().toAABB());
            aabbs.add(sideBB.clone().rotate90Degrees(ForgeDirection.UP).toAABB());
            
            boolean shouldRenderNode = false;
            int connectionCount = 0;
            for (int i = 0; i < 6; i += 2) {
                if (connections[i] ^ connections[i + 1]) {
                    shouldRenderNode = true;
                    break;
                }
                if (connections[i]) connectionCount++;
                if (connections[i + 1]) connectionCount++;
            }
            if (shouldRenderNode || connectionCount == 0 || connectionCount > 2) {
                Minecraft.getMinecraft().renderEngine.bindTexture(tubeNodeTexture);
                renderMiddle(aabbs.get(0));
                Minecraft.getMinecraft().renderEngine.bindTexture(tubeSideTexture);
            } else {
                Minecraft.getMinecraft().renderEngine.bindTexture(tubeSideTexture);
                renderMiddle(aabbs.get(0));
            }
            for (int i = 1; i < aabbs.size(); i++) {
                renderTexturedCuboid(aabbs.get(i));
            }
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();*/
    }
    
    private void renderMiddle(AxisAlignedBB aabb) {
    
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        
        if (!connections[2]) {
            t.setNormal(0, 0, -1);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY);// minZ
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.minY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY);
                
            } else {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY);// minZ
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.minX, aabb.minY);
            }
        }
        
        if (!connections[3]) {
            t.setNormal(0, 0, 1);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxX, aabb.maxY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.maxY);// maxZ
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxX, aabb.minY);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.minY);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.maxY);// maxZ
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.maxY);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxX, aabb.minY);
            }
        }
        
        if (!connections[0]) {
            t.setNormal(0, -1, 0);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxZ);// bottom
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minZ);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.maxZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxZ);// bottom
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minZ);
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.minZ);
            }
        }
        
        if (!connections[1]) {
            t.setNormal(0, 1, 0);
            if (connections[4]) {//or 5
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxZ);// top
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.maxZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.minZ);// top
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.minX, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxX, aabb.maxZ);
            }
        }
        
        if (!connections[4]) {
            t.setNormal(-1, 0, 0);
            if (connections[0]) {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.maxY, aabb.maxZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minY, aabb.maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, aabb.minY, aabb.minZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxY, aabb.minZ);
            } else {
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.minY, aabb.minZ);// minX
                t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minY, aabb.maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxX, aabb.maxY, aabb.maxZ);
                t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxY, aabb.minZ);
            }
        }
        
        if (!connections[5]) {
            t.setNormal(1, 0, 0);
            if (connections[0]) {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minY, aabb.maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.minY, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxY, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxY, aabb.maxZ);
            } else {
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minY, aabb.maxZ);// maxX
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxY, aabb.maxZ);
                t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxY, aabb.minZ);
                t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minY, aabb.minZ);
            }
        }
        t.draw();
    }
    
    public void renderTexturedCuboid(AxisAlignedBB aabb) {
    
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        
        if (aabb.minZ != 0 && (!connections[3] || aabb.minZ != 0.75)) {
            t.setNormal(0, 0, -1);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxY, aabb.maxX);// minZ
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.maxY, aabb.minX);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.minY, aabb.minX);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.minY, aabb.maxX);
        }
        
        if (aabb.maxZ != 1 && (!connections[2] || aabb.maxZ != 0.25)) {
            t.setNormal(0, 0, 1);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.minY, aabb.minX);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.maxY, aabb.minX);// maxZ
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxY, aabb.maxX);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.minY, aabb.maxX);
        }
        
        if (aabb.minY != 0 && (!connections[1] || aabb.minY != 0.75)) {
            t.setNormal(0, -1, 0);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxZ);// bottom
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.minZ);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minZ);
        }
        
        if (aabb.maxY != 1 && (!connections[0] || aabb.maxY != 0.25)) {
            t.setNormal(0, 1, 0);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.minZ, aabb.minX);// top
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.minZ, aabb.maxX);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxZ, aabb.maxX);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxZ, aabb.minX);
        }
        
        if (aabb.minX != 0 && (!connections[5] || aabb.minX != 0.75)) {
            t.setNormal(-1, 0, 0);
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.minZ, aabb.minZ, aabb.minY);// minX
            t.addVertexWithUV(aabb.minX, aabb.minY, aabb.maxZ, aabb.maxZ, aabb.minY);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.maxZ, aabb.maxZ, aabb.maxY);
            t.addVertexWithUV(aabb.minX, aabb.maxY, aabb.minZ, aabb.minZ, aabb.maxY);
        }
        
        if (aabb.maxX != 1 && (!connections[4] || aabb.maxX != 0.25)) {
            t.setNormal(1, 0, 0);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.minZ, aabb.minY, aabb.maxZ);// maxX
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxY, aabb.maxZ);
            t.addVertexWithUV(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.maxY, aabb.minZ);
            t.addVertexWithUV(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minY, aabb.minZ);
        }
        t.draw();
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
    
        return false;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerMachines;
    }
}
