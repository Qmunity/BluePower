package com.bluepowermod.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import com.bluepowermod.api.item.IDatabaseSaveable;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartCollidable;
import com.qmunity.lib.part.IPartOccluding;
import com.qmunity.lib.part.IPartRenderable;
import com.qmunity.lib.part.IPartSelectable;
import com.qmunity.lib.part.IPartUpdateListener;
import com.qmunity.lib.part.PartBase;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.raytrace.RayTracer;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BPPart extends PartBase implements IPartSelectable, IPartCollidable, IPartOccluding, IPartRenderable, IPartUpdateListener,
IDatabaseSaveable {

    public abstract String getUnlocalizedName();

    private PartInfo partInfo;

    @Override
    public ItemStack getItem() {

        if (partInfo == null)
            partInfo = PartManager.getPartInfo(getType());

        return partInfo.getItem().copy();
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass) {

        return false;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    @Override
    public boolean shouldRenderOnPass(int pass) {

        return pass == 0;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return new ArrayList<Vec3dCube>();
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        return RayTracer.instance().rayTraceCubes(this, start, end);
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return new ArrayList<Vec3dCube>();
    }

    @Override
    public ItemStack getPickedItem(QMovingObjectPosition mop) {

        return getItem();
    }

    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    }

    @Override
    public void onPartChanged(IPart part) {

    }

    @Override
    public void onNeighborBlockChange() {

    }

    @Override
    public void onNeighborTileChange() {

    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onUnloaded() {

    }

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack item) {

        return false;
    }

    @Override
    public void onClicked(EntityPlayer player, ItemStack item) {

    }

    public void addWailaInfo(List<String> info) {

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

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

    }

}
