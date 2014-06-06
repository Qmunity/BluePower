package net.quetzi.bluepower.api.part;

import java.util.Arrays;
import java.util.List;

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
     * @return The unlocalized name of this part
     */
    public abstract String getUnlocalizedName();
    
    /**
     * Gets all the collision boxes for this block
     * 
     * @return A list with the collision boxes
     */
    public List<AxisAlignedBB> getCollisionBoxes() {
    
        return Arrays.asList(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
    }
    
    /**
     * Gets all the selection boxes for this block
     * 
     * @return A list with the selection boxes
     */
    public List<AxisAlignedBB> getSelectionBoxes() {
    
        return Arrays.asList(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
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
    
    }
    
    /**
     * This render method gets called whenever there's a block update in the
     * chunk. You should use this to remove load from the renderer if a part of
     * the rendering code doesn't need to get called too often or just doesn't
     * change at all. To call a render update to re-render this just call
     * {@link BPPart#markPartForRenderUpdate()}
     * 
     * @param loc
     * @param pass
     */
    public void renderStatic(Vector3 loc, int pass) {
    
    }
    
    /**
     * Marks the part (and the entire chunk) for a render update on the next
     * render tick
     */
    public final void markPartForRenderUpdate() {
    
        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
    }
    
}
