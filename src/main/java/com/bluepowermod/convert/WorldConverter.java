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

package com.bluepowermod.convert;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.storage.RegionFile;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.fmp.FMPPart;
import uk.co.qmunity.lib.vec.Vec2d;

import com.bluepowermod.convert.part.PartConverterGate;
import com.bluepowermod.convert.part.PartConverterLamp;
import com.bluepowermod.convert.part.PartConverterWire;
import com.bluepowermod.part.PartManager;

public class WorldConverter {

    private static final List<IPartConverter> converters = new ArrayList<IPartConverter>();

    static {
        converters.add(new PartConverterWire());
        converters.add(new PartConverterLamp());
        converters.add(new PartConverterGate());
    }

    public static void main(String[] args) {

        PartManager.registerParts();

        // Temporary, just for testing :P
        new WorldConverter(new File("C:/modding/1.7.10/rundir/client/saves/Multipart Conversion Test/")).convert();
        // .convertRegion(new File(
        // "C:/modding/1.7.10/rundir/client/saves/Multipart Conversion Test/region/r.4.-1.mca"));
    }

    private final File worldFolder;

    public WorldConverter(File worldFolder) {

        this.worldFolder = worldFolder;
    }

    public void convertIfNeeded() {

        if (needsConversion())
            convert();
    }

    public boolean needsConversion() {

        return false;
    }

    public void convert() {

        for (File regionFolder : getRegionFolders())
            for (File region : getRegionFiles(regionFolder))
                convertRegion(region);
    }

    private List<File> getRegionFolders() {

        List<File> folders = new ArrayList<File>();

        File regFolder = new File(worldFolder, "region/");

        if (regFolder.exists() && regFolder.isDirectory())
            folders.add(regFolder);

        for (File f : worldFolder.listFiles()) {
            if (!f.isDirectory())
                continue;
            regFolder = new File(f, "region/");
            if (regFolder.exists() && regFolder.isDirectory())
                folders.add(regFolder);
        }

        return folders;
    }

    private List<File> getRegionFiles(File regionFolder) {

        List<File> files = new ArrayList<File>();

        for (File f : regionFolder.listFiles()) {
            if (!f.isFile())
                continue;
            if (!f.getName().toLowerCase().startsWith("r.") || !f.getName().toLowerCase().endsWith(".mca"))
                continue;
            files.add(f);
        }

        return files;
    }

    private NBTTagCompound getChunk(RegionFile reg, int x, int y) {

        try {
            DataInputStream inputStream = reg.getChunkDataInputStream(x & 31, y & 31);
            if (inputStream == null)
                return null;
            NBTTagCompound tag = CompressedStreamTools.read(inputStream).getCompoundTag("Level");
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveChunk(RegionFile reg, int x, int y, NBTTagCompound chunkData) {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Level", chunkData);

        try {
            DataOutputStream outputStream = reg.getChunkDataOutputStream(x & 31, y & 31);
            CompressedStreamTools.write(tag, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertRegion(File file) {

        RegionFile reg = new RegionFile(file);

        System.out.println("Region! " + file.getName());

        String filename = file.getName();
        int dot = filename.indexOf(".", 3);
        int regX = Integer.parseInt(filename.substring(2, dot));
        int regZ = Integer.parseInt(filename.substring(dot + 1, filename.lastIndexOf(".")));
        System.out.println(" This region goes from (" + (regX * 32) + ", " + (regZ * 32) + ") to (" + ((regX * 32) + 32) + ", "
                + ((regZ * 32) + 32) + ")");

        for (int x = 0; x < 32; x++) {
            for (int z = 0; z < 32; z++) {
                NBTTagCompound chunk = getChunk(reg, x, z);
                if (chunk == null)
                    continue;

                Vec2d c = new Vec2d(x + 32 * regX, z + 32 * regZ);

                if (c.getX() == 129 && c.distance(new Vec2d(129, -25)) < 2)
                    System.out.println("    Chunk: (" + (x + 32 * regX) + ", " + (z + 32 * regZ) + ") " + chunk);

                NBTTagList tileEntities = chunk.getTagList("TileEntities", new NBTTagCompound().getId());

                boolean changed = false;

                for (int i = 0; i < tileEntities.tagCount(); i++) {
                    NBTTagCompound te = tileEntities.getCompoundTagAt(i);
                    if (te.getString("id").equals("savedMultipart"))
                        changed |= convertTile(te);
                }

                if (changed) {
                    saveChunk(reg, x, z, chunk);
                    System.out.println("Changed!");
                }
            }
        }

        try {
            reg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean convertTile(NBTTagCompound tag) {

        NBTTagList parts = tag.getTagList("parts", new NBTTagCompound().getId());
        int count = parts.tagCount();

        FMPPart fmppart = new FMPPart();

        for (int i = 0; i < count; i++) {
            NBTTagCompound part = parts.getCompoundTagAt(i);
            String id = part.getString("id");
            for (IPartConverter c : converters) {
                if (c.matches(id)) {
                    IPart p = c.convert(part);
                    if (p == null)
                        continue;
                    fmppart.addPart(p);
                    parts.removeTag(i);
                    i--;
                    break;
                }
            }
            count = parts.tagCount();
        }
        if (fmppart.getParts().size() > 0) {
            NBTTagCompound part = new NBTTagCompound();
            fmppart.save(part);
            part.setString("id", fmppart.getType());
            parts.appendTag(part);

            return true;
        }
        return false;
    }
}
