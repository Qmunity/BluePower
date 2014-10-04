package com.bluepowermod.part;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartFactory;

public class PartFactory implements IPartFactory {

    @Override
    public IPart createPart(String type, boolean client) {

        return PartManager.getPartInfo(type).create();
    }
}
