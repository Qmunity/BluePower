package net.quetzi.bluepower.api.part;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.references.Dependencies;
import net.quetzi.bluepower.util.RayTracer;
import codechicken.multipart.BlockMultipart;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public abstract class BPPart {
    
    public World world = null;
    public int   x     = 0;
    public int   y     = 0;
    public int   z     = 0;
    
    /**
     * Checks if this tile is part of an FMP block
     * 
     * @return Whether it's part of an FMP block or not
     */
    protected final boolean isFMPMultipart() {
    
        if (Loader.isModLoaded(Dependencies.FMP)) return isFMPMultipart_();
        return false;
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    private final boolean isFMPMultipart_() {
    
        return world.getBlock(x, y, z) instanceof BlockMultipart;
    }
    
    /**
     * Gets an unique part type identifier, used to distinguish between this
     * part and others
     * 
     * @return The part type
     */
    public abstract String getType();
    
    /**
     * Gets the unlocalized name for this part (used for the item name)
     * 
     * @return The unlocalized name of this part
     */
    public abstract String getUnlocalizedName();
    
    /**
     * Gets all the collision boxes for this block
     * 
     * @return A list with the collision boxes
     */
    public List<AxisAlignedBB> getCollisionBoxes() {
    
//        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
//        aabbs.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
//        return aabbs;
        return null;
    }
    
    /**
     * Gets all the selection boxes for this block
     * 
     * @return A list with the selection boxes
     */
    public List<AxisAlignedBB> getSelectionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        aabbs.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.125, 1));
        return aabbs;
    }
    
    /**
     * Raytraces the block
     * 
     * @param start
     *            The start of the raytrace
     * @param end
     *            The end of the raytrace
     * @return The result of the raytrace
     */
    public MovingObjectPosition rayTrace(Vector3 start, Vector3 end) {
    
        return RayTracer.rayTrace(start, end, getSelectionBoxes(), x, y, z);
    }
    
    /**
     * This render method gets called every tick. You should use this if you're
     * doing animations
     * 
     * @param loc
     *            Distance from the player's position
     * @param pass
     *            Render pass (0 or 1)
     * @param frame
     *            Partial tick for smoother animations
     */
    public void renderDynamic(Vector3 loc, int pass, float frame) {
        GL11.glPushMatrix();
        {
            GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(0, 1, 0, 1);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(100);
            t.addVertex(0, 0, 0);
            t.addVertex(1, 0, 0);
            t.addVertex(1, 1, 0);
            t.addVertex(0, 1, 0);
            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
    /**
     * This render method gets called whenever there's a block update in the
     * chunk. You should use this to remove load from the renderer if a part of
     * the rendering code doesn't need to get called too often or just doesn't
     * change at all. To call a render update to re-render this just call
     * {@link BPPart#markPartForRenderUpdate()}
     * 
     * @param loc
     *            Distance from the player's position
     * @param pass
     *            Render pass (0 or 1)
     * @return Whether or not it rendered something
     */
    public boolean renderStatic(Vector3 loc, int pass) {
    
        return false;
    }
    
    /**
     * Marks the part (and the entire chunk) for a render update on the next
     * render tick
     */
    public final void markPartForRenderUpdate() {
    
        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    }
    
    public int getLightValue() {
    
        return 0;
    }
    
    public float getHardness() {
    
        return 0;
    }
    
    public float getHardness(EntityPlayer player) {
    
        return getHardness();
    }
    
    public float getHardness(MovingObjectPosition mop, EntityPlayer player) {
    
        return getHardness(player);
    }
    
    public ItemStack getPickedItem(MovingObjectPosition hit) {
    
        return pickItem();
    }
    
    public ItemStack pickItem() {
    
        return PartRegistry.getItemForPart(getType());
    }
    
    public List<ItemStack> getDrops() {
    
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        items.add(PartRegistry.getItemForPart(getType()));
        
        return items;
    }
    
    public void onAdded() {
    
    }
    
    public void onRemoved() {
    
    }
    
    public void onPartChanged() {
    
    }
    
    public void onEntityCollision(Entity entity) {
    
    }
    
    public void onNeigbourUpdate() {
    
    }
    
    public boolean onActivated(ItemStack item) {
    
        return false;
    }
    
    public boolean onActivated(EntityPlayer player, ItemStack item) {
    
        return onActivated(item);
    }
    
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop,
            ItemStack item) {
    
        return onActivated(player, item);
    }
    
}
