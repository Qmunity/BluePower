/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.BluePower;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class ItemStackDatabase {
    
    private static final String FILE_EXTENSION       = ".cdf";
    private static final String DATABASE_FOLDER_NAME = "bluepower\\circuitDatabase\\";
    private final String        saveLocation;
    private List<ItemStack>     cache;
    
    public ItemStackDatabase() {
    
        saveLocation = BluePower.proxy.getSavePath() + "\\" + DATABASE_FOLDER_NAME;
    }
    
    public void saveItemStack(ItemStack stack) {
    
        new File(saveLocation).mkdirs();
        File targetLocation = new File(saveLocation + stack.getDisplayName() + FILE_EXTENSION);
        
        NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
        
        UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        tag.setString("owner", ui.modId);
        tag.setString("name", ui.name);
        
        try {
            FileOutputStream fos = new FileOutputStream(targetLocation);
            DataOutputStream dos = new DataOutputStream(fos);
            
            byte[] abyte = CompressedStreamTools.compress(tag);
            dos.writeShort((short) abyte.length);
            dos.write(abyte);
            
            dos.close();
            
        } catch (IOException e) {
            BluePower.log.error("IOException when trying to save an ItemStack in the database: " + e);
        }
        cache = null;
    }
    
    public void deleteStack(ItemStack deletingStack) {
    
        File targetLocation = new File(saveLocation);
        
        if (targetLocation.exists()) {
            File[] files = targetLocation.listFiles();
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
            File targetLocation = new File(saveLocation);
            
            List<ItemStack> stacks = new ArrayList<ItemStack>();
            if (targetLocation.exists()) {
                File[] files = targetLocation.listFiles();
                for (File file : files) {
                    try {
                        FileInputStream fos = new FileInputStream(file);
                        DataInputStream dos = new DataInputStream(fos);
                        
                        short short1 = dos.readShort();
                        byte[] abyte = new byte[short1];
                        dos.read(abyte);
                        NBTTagCompound tag = CompressedStreamTools.func_152457_a(abyte, new NBTSizeTracker(2097152L));
                        ItemStack stack = new ItemStack(Items.stick);
                        stack.readFromNBT(tag);
                        if (stack.getItem() != null) {
                            stacks.add(stack);
                        } else {
                            BluePower.log.error("Couldn't retrieve an itemstack with item id: " + tag.getShort("id"));
                            Item item = GameRegistry.findItem(tag.getString("owner"), tag.getString("name"));
                            if (item != null) {
                                ItemStack backupStack = new ItemStack(item, stack.stackSize, tag.getShort("Damage"));
                                if (stack.hasTagCompound()) {
                                    backupStack.setTagCompound(stack.getTagCompound());
                                }
                                stacks.add(backupStack);
                                BluePower.log.info("Successfully retrieved stack via its name: " + tag.getString("owner") + ":" + tag.getString("name"));
                            } else {
                                BluePower.log.error("Couldn't retrieve the item via its name: " + tag.getString("owner") + ":" + tag.getString("name"));
                            }
                        }
                        dos.close();
                    } catch (IOException e) {
                        System.out.println("Exception : " + e);
                    }
                }
            }
            cache = stacks;
        }
        return cache;
    }
    
}
