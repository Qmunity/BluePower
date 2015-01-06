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

import com.bluepowermod.convert.part.PartConverterGate;
import com.bluepowermod.convert.part.PartConverterLamp;
import com.bluepowermod.convert.part.PartConverterTube;
import com.bluepowermod.convert.part.PartConverterWire;

public class WorldConverter {

    private static final List<IPartConverter> converters = new ArrayList<IPartConverter>();

    static {
        converters.add(new PartConverterWire());
        converters.add(new PartConverterLamp());
        converters.add(new PartConverterGate());
        converters.add(new PartConverterTube());
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

        return !new File(worldFolder, "bluepower/worldconversion").exists();
    }

    public void convert() {

        File regFolder = new File(worldFolder, "region/");

        if (regFolder.exists())
            for (File region : getRegionFiles(regFolder))
                convertRegion(region);
        try {
            File f = new File(worldFolder, "bluepower/worldconversion");
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (Exception e) {
        }
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

        for (int x = 0; x < 32; x++) {
            for (int z = 0; z < 32; z++) {
                NBTTagCompound chunk = getChunk(reg, x, z);
                if (chunk == null)
                    continue;

                NBTTagList tileEntities = chunk.getTagList("TileEntities", new NBTTagCompound().getId());

                boolean changed = false;

                for (int i = 0; i < tileEntities.tagCount(); i++) {
                    NBTTagCompound te = tileEntities.getCompoundTagAt(i);
                    if (te.getString("id").equals("savedMultipart"))
                        changed |= convertTile(te);
                }

                if (changed)
                    saveChunk(reg, x, z, chunk);
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

        FMPPart fmppart = new FMPPart(true);

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
