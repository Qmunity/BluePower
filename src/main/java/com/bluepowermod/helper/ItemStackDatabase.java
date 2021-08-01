/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import com.bluepowermod.BluePower;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemStackDatabase {

    private static final String FILE_EXTENSION = ".cdf";
    private static final String DATABASE_FOLDER_NAME = "bluepower" + File.separator + "circuitDatabase" + File.separator;
    private final String saveLocation;
    private List<ItemStack> cache;

    public ItemStackDatabase() {

        saveLocation = BluePower.proxy.getSavePath() + File.separator + DATABASE_FOLDER_NAME;
    }

    public void saveItemStack(ItemStack stack) {

        new File(saveLocation).mkdirs();
        File targetRegistryName = new File(saveLocation + stack.getDisplayName() + FILE_EXTENSION);

        CompoundTag tag = new CompoundTag();
        stack.save(tag);

        ResourceLocation ui = stack.getItem().getRegistryName();
        tag.putString("owner", ui.getNamespace());
        tag.putString("name", ui.getPath());

        try {
            FileOutputStream fos = new FileOutputStream(targetRegistryName);
            DataOutputStream dos = new DataOutputStream(fos);

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            CompressedStreamTools.writeCompressed(tag, byteStream);
            byte[] abyte = byteStream.toByteArray();
            dos.writeShort((short) abyte.length);
            dos.write(abyte);

            dos.close();

        } catch (IOException e) {
            BluePower.log.error("IOException when trying to save an ItemStack in the database: " + e);
        }
        cache = null;
    }

    public void deleteStack(ItemStack deletingStack) {

        File targetRegistryName = new File(saveLocation);

        if (targetRegistryName.exists()) {
            File[] files = targetRegistryName.listFiles();
            for (File file : files) {
                if (deletingStack.getDisplayName().equals(file.getName().substring(0, file.getName().length() - 4))) {
                    file.delete();
                    cache = null;
                    return;
                }
            }
        }
    }

    public List<ItemStack> loadItemStacks() {

        if (cache == null) {
            File targetRegistryName = new File(saveLocation);

            List<ItemStack> stacks = new ArrayList<ItemStack>();
            if (targetRegistryName.exists()) {
                File[] files = targetRegistryName.listFiles();
                for (File file : files) {
                    try {
                        FileInputStream fos = new FileInputStream(file);
                        DataInputStream dos = new DataInputStream(fos);

                        short short1 = dos.readShort();
                        byte[] abyte = new byte[short1];
                        dos.read(abyte);
                        ByteArrayInputStream byteStream = new ByteArrayInputStream(abyte);
                        CompoundTag tag = CompressedStreamTools.readCompressed(byteStream);
                        ItemStack stack = ItemStack.of(tag);
                        if (stack.getItem() != Items.AIR) {
                            stacks.add(stack);
                        } else {
                            BluePower.log.error("Couldn't retrieve an itemstack with item id: " + tag.getShort("id"));
                            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("owner"), tag.getString("name")));
                            if (item != null && item != Items.AIR) {
                                ItemStack backupStack = new ItemStack(item, stack.getCount());
                                backupStack.setDamageValue(tag.getShort("Damage"));
                                if (stack.hasTag()) {
                                    backupStack.setTag(stack.getTag());
                                }
                                stacks.add(backupStack);
                                BluePower.log.info("Successfully retrieved stack via its name: " + tag.getString("owner") + ":"
                                        + tag.getString("name"));
                            } else {
                                BluePower.log.error("Couldn't retrieve the item via its name: " + tag.getString("owner") + ":"
                                        + tag.getString("name"));
                            }
                        }
                        dos.close();
                    } catch (IOException e) {
                        BluePower.log.error("Exception : " + e);
                    }
                }
            }
            cache = stacks;
        }
        return cache;
    }

}
