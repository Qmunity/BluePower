package com.bluepowermod.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.BPPartFace;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.item.ItemMultipart;
import com.qmunity.lib.part.IPart;

public class ItemPart extends ItemMultipart implements IDatabaseSaveable {

    public ItemPart() {

        setUnlocalizedName("part." + Refs.MODID + ":");
        setCreativeTab(CustomTabs.tabBluePowerCircuits);
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return super.getUnlocalizedName() + PartManager.getPartType(item);
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {

        for (PartInfo i : PartManager.getRegisteredParts())
            l.add(i.getItem());
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        BPPart part = PartManager.getExample(stack);
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.canGoInCopySlot(stack);
    }

    @Override
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack) {

        String templateType = PartManager.getPartType(templateStack);
        String outputType = PartManager.getPartType(outputStack);

        if (templateType == null || outputType == null)
            return false;

        if (templateType.equals(outputType)) {
            return canGoInCopySlot(templateStack);
        } else {
            return false;
        }
    }

    @Override
    public List<ItemStack> getItemsOnStack(ItemStack stack) {

        BPPart part = PartManager.getExample(stack);
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.getItemsOnStack(stack);
    }

    @Override
    public String getCreatedPartType(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        return PartManager.getPartType(item);
    }

    @Override
    public IPart createPart(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        IPart part = super.createPart(item, player, world, mop);

        if (part instanceof BPPartFace)
            ((BPPartFace) part).setFace(ForgeDirection.getOrientation(mop.sideHit).getOpposite());

        return part;
    }

}
