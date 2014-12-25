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

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.util.Refs;

public class GateWire extends GateBase {

    public static final String ID = "icWire";
    private static IIcon topIcon;

    @Override
    public void initializeConnections() {

        front().enable();
        left().enable();
        back().enable();
        right().enable();

        /*  front.setOutput();
          left.setOutput();
          back.setOutput();
          right.setOutput();*/
    }

    @Override
    public String getId() {

        return ID;
    }

    @Override
    protected void renderTop(float frame) {

        boolean isOn = false;
        for (Dir dir : Dir.values()) {
            if (getConnection(dir).isEnabled()) {
                isOn = getConnection(dir).getInput() > 0 || getConnection(dir).getOutput() > 0;
                break;
            }
        }
        this.renderTop("front", front());
        renderTop("back", back());
        renderTop("left", left());
        renderTop("right", right());
        renderTop("center_" + (isOn ? "on" : "off"));
    }

    @Override
    public boolean changeMode() {

        if (front().isEnabled() && left().isEnabled() && back().isEnabled() && right().isEnabled()) {
            right().disable();
        } else if (front().isEnabled() && left().isEnabled() && back().isEnabled()) {
            left().disable();
        } else if (front().isEnabled() && back().isEnabled()) {
            left().enable();
            front().disable();
        } else if (left().isEnabled() && back().isEnabled()) {
            front().enable();
            right().enable();
        }
        return true;
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

    public static void registerTopIcon(IIconRegister reg) {
        topIcon = reg.registerIcon(Refs.MODID + ":gates/" + ID + "/base");
    }

    @Override
    public IIcon getIcon(ForgeDirection face) {
        if (face == ForgeDirection.UP)
            return topIcon;

        return super.getIcon(face);
    }

}
