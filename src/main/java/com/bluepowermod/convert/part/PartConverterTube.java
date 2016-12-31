package com.bluepowermod.convert.part;

import com.bluepowermod.convert.IPartConverter;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.tube.PneumaticTube;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.part.IPart;

public class PartConverterTube implements IPartConverter {

    @Override
    public boolean matches(String id) {

        return id.startsWith("bluepower_pneumaticTube") || id.startsWith("bluepower_accelerator") || id.startsWith("bluepower_magTube")
                || id.startsWith("bluepower_pneumaticTubeOpaque") || id.startsWith("bluepower_restrictionTube")
                || id.startsWith("bluepower_restrictionTubeOpaque");
    }

    @Override
    public IPart convert(NBTTagCompound old) {

        PartInfo info = PartManager.getPartInfo(old.getString("part_id"));
        if (info == null)
            return null;

        PneumaticTube part = (PneumaticTube) info.create();
        part.readFromNBT(old.getCompoundTag("partData"));

        return part;
    }

}
