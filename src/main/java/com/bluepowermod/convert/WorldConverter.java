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
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.storage.RegionFile;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.fmp.FMPPart;

public class WorldConverter {

    private static final List<IPartConverter> converters = new ArrayList<IPartConverter>();

    static {
        converters.add(new ConverterTest());
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

    private NBTTagCompound getChunk(File file, int x, int y) {

        try {
            RegionFile reg = new RegionFile(file);
            DataInputStream inputStream = reg.getChunkDataInputStream(x & 31, y & 31);
            NBTTagCompound tag = CompressedStreamTools.read(inputStream).getCompoundTag("Level");
            reg.close();
            return tag;
        } catch (Exception exception) {
        }
        return null;
    }

    private void saveChunk(File file, int x, int y, NBTTagCompound chunkData) {

        try {
            RegionFile reg = new RegionFile(file);
            DataOutputStream outputStream = reg.getChunkDataOutputStream(x & 31, y & 31);
            CompressedStreamTools.write(chunkData, outputStream);
            outputStream.close();
            reg.close();
        } catch (Exception exception) {
        }
    }

    private void convertRegion(File file) {

        NBTTagCompound chunk = getChunk(file, 0, 0);
        if (chunk == null)
            return;

        NBTTagList tileEntities = chunk.getTagList("TileEntities", new NBTTagCompound().getId());

        for (int i = 0; i < tileEntities.tagCount(); i++) {
            NBTTagCompound te = tileEntities.getCompoundTagAt(i);
            if (te.getString("id").equals("savedMultipart")) {
                convertTile(te);
            }
        }

        saveChunk(file, 0, 0, chunk);
    }

    private void convertTile(NBTTagCompound tag) {

        NBTTagList parts = tag.getTagList("parts", new NBTTagCompound().getId());

        FMPPart fmppart = new FMPPart();

        for (int i = 0; i < parts.tagCount(); i++) {
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
        }
        if (fmppart.getParts().size() > 0) {
            NBTTagCompound part = new NBTTagCompound();
            fmppart.save(part);
            parts.appendTag(part);
        }
    }
}
