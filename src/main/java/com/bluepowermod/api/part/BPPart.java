/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.api.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import codechicken.multipart.BlockMultipart;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.compat.fmp.IMultipartCompat;
import com.bluepowermod.references.Dependencies;
import com.bluepowermod.util.RayTracer;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public abstract class BPPart {

    public World world = null;
    public int x = 0;
    public int y = 0;
    public int z = 0;

    /**
     * Holds the exact width of 1 pixel, texturewise
     */
    protected static double pixel = 1.0 / 16.0;

    /**
     * Checks if this tile is part of an FMP block
     * 
     * @return Whether it's part of an FMP block or not
     */
    public final boolean isFMPMultipart() {

        if (Loader.isModLoaded(Dependencies.FMP))
            return isFMPMultipart_();
        return false;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private final boolean isFMPMultipart_() {

        return world.getBlock(x, y, z) instanceof BlockMultipart;
    }

    /**
     * Gets an unique part type identifier, used to distinguish between this part and others
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

    public boolean canPlacePart(ItemStack is, EntityPlayer player, Vector3 block, MovingObjectPosition mop) {

        return true;
    }

    protected int tick = 0;

    /**
     * Called every tick to update the part
     */
    public void update() {

        if (tick == 0)
            onFirstTick();
        if (tick == 2)
            shouldNotifyUpdates = true;
        tick++;
    }

    public void onFirstTick() {

    }

    /**
     * Gets all the collision boxes for this block
     * 
     * @return A list with the collision boxes
     */
    public List<AxisAlignedBB> getCollisionBoxes() {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        aabbs.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
        return aabbs;
    }

    /**
     * Gets all the selection boxes for this block
     * 
     * @return A list with the selection boxes
     */
    public List<AxisAlignedBB> getSelectionBoxes() {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        aabbs.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1));
        return aabbs;
    }

    /**
     * Gets all the occlusion boxes for this block
     * 
     * @return A list with the occlusion boxes
     */
    public List<AxisAlignedBB> getOcclusionBoxes() {

        return getSelectionBoxes();
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
     * This render method gets called every tick. You should use this if you're doing animations
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

    public boolean shouldRenderDynamicOnPass(int pass) {

        return pass == 0;
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
    public boolean renderStatic(Vector3 loc, int pass) {

        return false;
    }

    public boolean shouldRenderStaticOnPass(int pass) {

        return pass == 0;
    }

    /**
     * Used to render the item when it's not placed in the world (in the player's hand, as a dropped item, in an inventory...)
     * 
     * @param type
     *            Render type (Inventory, dropped item, held (first person), held (third person)...)
     * @param item
     *            The item that will be rendered. This can be useful if special data is stored in the item's NBT tag
     * @param data
     *            Extra data provided by the renderer (check {@link ItemRenderType} for javadocs on what data each type will have)
     */
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    }

    private boolean shouldReRender = true;

    /**
     * Marks the part (and the entire chunk) for a render update on the next render tick
     */
    public final void markPartForRenderUpdate() {

        shouldReRender = true;
    }

    /**
     * If this part was marked for a render update, unmarks it
     */
    public final void resetRenderUpdate() {

        shouldReRender = false;
    }

    public final boolean shouldReRender() {

        return shouldReRender;
    }

    /**
     * Gets the amount of light emitted by this block. If you want this to update in real time you'll probably need to call
     * {@link BPPart#markPartForRenderUpdate()}
     * 
     * @return The light value (0-15)
     */
    public int getLightValue() {

        return 0;
    }

    /**
     * Gets the hardness of this part
     * 
     * @return The hardness of the part
     */
    public float getHardness() {

        return 0;
    }

    /**
     * Gets the hardness of this part based on the player
     * 
     * @param player
     *            The player that's trying to break the block
     * @return The hardness of the part depending on the player
     */
    public float getHardness(EntityPlayer player) {

        return getHardness();
    }

    /**
     * Gets the hardnes of this part based on the player and the location where it's clicking
     * 
     * @param mop
     *            Point in the block at which the player is looking when breaking it
     * @param player
     *            The player that's trying to break the block
     * @return The hardness of the part depending on the player and where it's looking when breaking it
     */
    public float getHardness(MovingObjectPosition mop, EntityPlayer player) {

        return getHardness(player);
    }

    /**
     * Gets the explosion resistance of this part
     * 
     * @return The resistance
     */
    public float getExplosionResistance() {

        return 0;
    }

    /**
     * Gets the picked item when middle-clicked
     * 
     * @return The item that the player will pickup
     */
    public ItemStack pickItem() {

        return PartRegistry.getItemForPart(getType());
    }

    /**
     * Gets the picked item when middle-clicked
     * 
     * @param hit
     *            Position at which the player is looking at when middle clicking
     * @return The item that the player will pickup
     */
    public ItemStack getPickedItem(MovingObjectPosition hit) {

        return pickItem();
    }

    /**
     * Gets a list of items that will drop when the part is broken
     * 
     * @return The list of items dropped by this part
     */
    public List<ItemStack> getDrops() {

        List<ItemStack> items = new ArrayList<ItemStack>();

        items.add(PartRegistry.getItemForPart(getType()));

        return items;
    }

    /**
     * Event called when the part is placed in the world
     */
    public void onAdded() {

    }

    /**
     * Event called when the part is removed from the world
     */
    public void onRemoved() {

    }

    /**
     * Event called when another part in the same block is changed
     */
    public void onPartChanged() {

    }

    /**
     * Event called when an entity collides with the part
     * 
     * @param entity
     *            The entity that collided with the part
     */
    public void onEntityCollision(Entity entity) {

    }

    /**
     * Event called whenever a nearby block updates
     */
    public void onNeighborUpdate() {

    }

    /**
     * Event called whenever a nearby TileEntity updates
     */
    public void onNeighborTileUpdate() {

    }

    /**
     * Event called when the part is activated (right clicked)
     * 
     * @param item
     *            Item that was used to click it
     * @return Whether or not an action occurred
     */
    public boolean onActivated(ItemStack item) {

        return false;
    }

    /**
     * Event called when the part is activated (right clicked)
     * 
     * @param player
     *            Player that right clicked the part
     * @param item
     *            Item that was used to click it
     * @return Whether or not an action occurred
     */
    public boolean onActivated(EntityPlayer player, ItemStack item) {

        return onActivated(item);
    }

    /**
     * Event called when the part is activated (right clicked)
     * 
     * @param player
     *            Player that right clicked the part
     * @param mop
     *            Location in the part that was clicked by the player
     * @param item
     *            Item that was used to click it
     * @return Whether or not an action occurred
     */
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {

        return onActivated(player, item);
    }

    /**
     * Notifies surrounding blocks of a part update in this block
     */
    private boolean shouldNotifyUpdates = false;

    public void notifyUpdate() {

        if (!shouldNotifyUpdates)
            return;
        if (world == null)
            return;

        world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
        markPartForRenderUpdate();

        world.notifyBlockOfNeighborChange(x, y, z, world.getBlock(x, y + 1, z));
    }

    /**
     * Sends this part's data to the client
     */
    public void sendUpdatePacket() {

        if (world == null)
            return;

        ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).sendUpdatePacket(this);
    }

    /**
     * Saves the data either to the save file or to an update packet
     * 
     * @param tag
     *            NBT tag the data will be written to
     */
    public void save(NBTTagCompound tag) {

    }

    /**
     * Loads the data either from the save file or from an update packet
     * 
     * @param tag
     *            NBT tag with the data
     */
    public void load(NBTTagCompound tag) {

    }

    /**
     * Gets the creative tab to display on
     * 
     * @return The creative tab instance
     */
    public CreativeTabs getCreativeTab() {

        return null;
    }

    /**
     * Gets the creative tab index to display on
     * 
     * @return The index in the creative tab to display on
     */
    public int getCreativeTabIndex() {

        return 0;
    }

    /**
     * Gets the creative tabs to display on
     * 
     * @return The creative tab instances
     */
    public CreativeTabs[] getCreativeTabs() {

        return new CreativeTabs[] { getCreativeTab() };
    }

    /**
     * Gets the creative tab indexes to display on
     * 
     * @return The indexes in the creative tabs to display on
     */
    public int[] getCreativeTabIndexes() {

        int[] indexes = new int[getCreativeTabs().length];
        Arrays.fill(indexes, getCreativeTabIndex());
        return indexes;
    }

    /**
     * Renders the highlight for the selected cube
     * 
     * @param cube
     *            Cube that's highlighted
     * @return False if it wasn't rendered, true if it was
     */
    public boolean drawHighlight(AxisAlignedBB cube, MovingObjectPosition mop) {

        return false;
    }

    /**
     * Checks if there's another part colliding with the cube passed as an argument
     * 
     * @param cube
     *            Cube that will be checked for collision
     * @return True if there's another part in that position, false if there's not
     */
    public boolean checkOcclusion(AxisAlignedBB cube) {

        return ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).checkOcclusion(world.getTileEntity(x, y, z), cube);
    }

    /**
     * Adds information to the waila tooltip
     * 
     * @author amadornes
     * 
     * @param info
     */
    public void addWailaInfo(List<String> info) {

    }

    public boolean hasCustomItemEntity() {

        return false;
    }

    public EntityItem createItemEntity(World w, double x, double y, double z, ItemStack item) {

        return null;
    }

}
