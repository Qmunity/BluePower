package com.bluepowermod.part;

import com.bluepowermod.api.item.IDatabaseSaveable;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.*;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BPPart extends PartBase implements IPartSelectable, IPartCollidable, IPartOccluding, IPartUpdateListener,
        IPartInteractable, IDatabaseSaveable, IPartWAILAProvider {


    @Override
    public World getWorld() {

        if (getParent() == null)
            return null;

        return super.getWorld();
    }

    @Override
    public BlockPos getPos(){

        if (getParent() == null)
            return null;

        return super.getPos();
    }

    public abstract String getUnlocalizedName();

    private PartInfo partInfo;

    @Override
    public ItemStack getItem() {

        if (partInfo == null)
            partInfo = PartManager.getPartInfo(getType());

        return partInfo.getStack();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderBreaking(BlockPos translation, RenderHelper renderer, VertexBuffer buffer, int pass, QRayTraceResult mop) {

        return renderStatic(translation, renderer, buffer, pass);
    }

    
    
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return new ArrayList<Vec3dCube>();
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

    }

    @Override
    public QRayTraceResult rayTrace(Vec3d start, Vec3d end) {

        return RayTracer.instance().rayTraceCubes(this, start, end);
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return new ArrayList<Vec3dCube>();
    }

    @Override
    public ItemStack getPickedItem(QRayTraceResult mop) {

        return getItem();
    }

    @Override
    public void onPartChanged(IPart part) {

        if (getWorld() != null && !getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onNeighborBlockChange() {

        if (getWorld() != null && !getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onNeighborTileChange() {

    }

    @Override
    public void onAdded() {

        if (getWorld() != null && !getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onLoaded() {

        if (getWorld() != null && !getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onUnloaded() {

    }

    @Override
    public void onConverted() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text) {

    }

    @Override
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack) {

        return false;
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        return false;
    }

    @Override
    public List<ItemStack> getItemsOnStack(ItemStack stack) {

        return new ArrayList<ItemStack>();
    }

    @Override
    public boolean onActivated(EntityPlayer player, QRayTraceResult hit, ItemStack item) {

        return false;
    }

    @Override
    public void onClicked(EntityPlayer player, QRayTraceResult hit, ItemStack item) {

    }

    public void onUpdate() {

    }

    public void notifyUpdate() {

        getWorld().updateObservingBlocksAt(getPos(), Blocks.AIR);
    }

    public CreativeTabs[] getCreativeTabs() {

        return new CreativeTabs[] { getCreativeTab() };
    }

    public CreativeTabs getCreativeTab() {

        return null;
    }

    public SoundType getPlacementSound() {

        return SoundType.GLASS;
    }

    @SideOnly(Side.CLIENT)
    public void addTooltip(ItemStack item, List<String> tip) {

    }

    public List<ItemStack> getSubItems() {

        return Arrays.asList(getItem());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap reg) {

    }

}
