package com.bluepowermod.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.BluePower;

public class ItemStackDatabase {
    
    private static final String FILE_EXTENSION       = ".cdf";
    private static final String DATABASE_FOLDER_NAME = "bluepower/circuitDatabase/";
    private final String        mcDataLocation       = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    private final String        saveLocation         = mcDataLocation.substring(0, mcDataLocation.length() - 2) + "/" + DATABASE_FOLDER_NAME;
    private List<ItemStack>     cache;
    
    public void saveItemStack(ItemStack stack) {
    
        new File(saveLocation).mkdirs();
        File targetLocation = new File(saveLocation + stack.getDisplayName() + FILE_EXTENSION);
        
        NBTTagCompound tag = new NBTTagCompound();
        stack.writeToNBT(tag);
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
    
    public List<ItemStack> loadItemStacks() {
    
        if (cache == null) {
            File targetLocation = new File(saveLocation);
            
            File[] files = targetLocation.listFiles();
            List<ItemStack> stacks = new ArrayList<ItemStack>();
            
            for (File file : files) {
                try {
                    FileInputStream fos = new FileInputStream(file);
                    DataInputStream dos = new DataInputStream(fos);
                    
                    short short1 = dos.readShort();
                    byte[] abyte = new byte[short1];
                    dos.read(abyte);
                    NBTTagCompound tag = CompressedStreamTools.func_152457_a(abyte, new NBTSizeTracker(2097152L));
                    stacks.add(ItemStack.loadItemStackFromNBT(tag));
                    dos.close();
                } catch (IOException e) {
                    System.out.println("Exception : " + e);
                }
            }
            cache = stacks;
        }
        return cache;
    }
    
}
