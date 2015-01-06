package com.bluepowermod.convert.part;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.convert.IPartConverter;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.lamp.PartLamp;

public class PartConverterLamp implements IPartConverter {

    @Override
    public boolean matches(String id) {

        return id.contains("cagelamp") || id.contains("fixture");
    }

    @Override
    public IPart convert(NBTTagCompound old) {

        String id = old.getString("part_id");
        id = id.replace("cagelamp", "cagelamp.").replace("fixture", "fixture.").replace("silver", "light_gray");
        if (id.startsWith("inverted"))
            id = id.substring("inverted".length()) + ".inverted";
        PartInfo info = PartManager.getPartInfo(id);
        if (info == null)
            return null;
        PartLamp part = (PartLamp) info.create();

        NBTTagCompound data = old.getCompoundTag("partData");

        part.setFace(ForgeDirection.getOrientation(data.getInteger("face")));

        return part;
    }

}
