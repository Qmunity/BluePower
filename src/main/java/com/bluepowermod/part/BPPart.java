/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCollidable;
import uk.co.qmunity.lib.part.IPartInteractable;
import uk.co.qmunity.lib.part.IPartOccluding;
import uk.co.qmunity.lib.part.IPartSelectable;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.part.PartBase;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.item.IDatabaseSaveable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BPPart extends PartBase implements IPartSelectable, IPartCollidable, IPartOccluding, IPartUpdateListener,
IPartInteractable, IDatabaseSaveable {

    public abstract String getUnlocalizedName();

    private PartInfo partInfo;

    @Override
    public ItemStack getItem() {

        if (partInfo == null)
            partInfo = PartManager.getPartInfo(getType());

        return partInfo.getItem().copy();
    }

    @Override
    public boolean renderBreaking(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass, QMovingObjectPosition mop) {

        return renderStatic(translation, renderer, renderBlocks, pass);
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

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onNeighborBlockChange() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onNeighborTileChange() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onAdded() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onLoaded() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void onUnloaded() {

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

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        return false;
    }

    @Override
    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

    }

    public void onUpdate() {

    }

    public void notifyUpdate() {

        getWorld().notifyBlockChange(getX(), getY(), getZ(), Blocks.air);
    }

    @SideOnly(Side.CLIENT)
    public CreativeTabs[] getCreativeTabs() {

        return new CreativeTabs[] { getCreativeTab() };
    }

    @SideOnly(Side.CLIENT)
    public CreativeTabs getCreativeTab() {

        return null;
    }

    @SideOnly(Side.CLIENT)
    public Block.SoundType getPlacementSound() {

        return Block.soundTypeGlass;
    }

    public void addTooltip(List<String> tip) {

    }

}
