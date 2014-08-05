/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.Refs;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.PartRegistry;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.BPTileMultipart;

public class ItemBPPart extends Item {

    public ItemBPPart() {

        super();
        setUnlocalizedName("part." + Refs.MODID + ":");
    }

    public static String getUnlocalizedName_(ItemStack item) {

        return "part." + Refs.MODID + ":" + PartRegistry.getPartIdFromItem(item);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void getSubItems(List l) {

        for (String id : PartRegistry.getRegisteredParts())
            l.add(PartRegistry.getItemForPart(id));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {

        boolean flag = true;

        if (flag) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeWood.soundName, Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());

            if (!w.isRemote) {
                Vector3 v = new Vector3(x, y, z, w);
                if ((v.getTileEntity() != null && v.getTileEntity() instanceof BPTileMultipart) && !player.isSneaking()) {
                    BPTileMultipart te = (BPTileMultipart) v.getTileEntity();
                    te.addPart(PartRegistry.createPartFromItem(stack));
                } else {
                    v.add(ForgeDirection.getOrientation(side));
                    if (v.getBlock(true) == null) {
                        w.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), BPBlocks.multipart);
                        BPTileMultipart te = new BPTileMultipart();
                        BPPart part = PartRegistry.createPartFromItem(stack);
                        if (part.canPlacePart(stack, player, v.getRelative(ForgeDirection.getOrientation(side).getOpposite()),
                                player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4.5, 0))) {
                            te.addPart(part);
                            w.setTileEntity(v.getBlockX(), v.getBlockY(), v.getBlockZ(), te);
                            w.markBlockForUpdate(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return getUnlocalizedName_(item);
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {

        getSubItems(l);
    }

}
