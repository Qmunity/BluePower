/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import uk.co.qmunity.lib.util.Dir;
import net.minecraft.client.resources.I18n;

import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Color;

public class GateAnd extends GateBase {

    private boolean power = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public String getId() {

        return "and";
    }

    @Override
    public void doLogic() {

        power = true;

        if (left().isEnabled()) {
            power &= left().getInput() > 0;
        }
        if (back().isEnabled()) {
            power &= back().getInput() > 0;
        }
        if (right().isEnabled()) {
            power &= right().getInput() > 0;
        }

        front().setOutput(power ? 15 : 0);
    }

    @Override
    protected boolean changeMode() {

        if (left().isEnabled() && back().isEnabled() && right().isEnabled()) {
            right().disable();
        } else if (left().isEnabled() && back().isEnabled()) {
            back().disable();
            right().enable();
        } else if (left().isEnabled() && right().isEnabled()) {
            left().disable();
            back().enable();
        } else if (back().isEnabled() && right().isEnabled()) {
            left().enable();
            back().disable();
            right().disable();
        } else if (left().isEnabled()) {
            left().disable();
            back().enable();
        } else if (back().isEnabled()) {
            back().disable();
            right().enable();
        } else {// right enabled
            left().enable();
            back().enable();
        }
        return true;
    }

    @Override
    public void addWailaInfo(List<String> info) {

        info.add(Color.YELLOW + I18n.format("gui.connections") + ":");
        info.add("  " + Dir.LEFT.getLocalizedName() + ": "
                + (getConnection(Dir.LEFT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
        info.add("  " + Dir.BACK.getLocalizedName() + ": "
                + (getConnection(Dir.BACK).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
        info.add("  " + Dir.RIGHT.getLocalizedName() + ": "
                + (getConnection(Dir.RIGHT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
    }

    @Override
    protected void renderTop(float frame) {

        renderTop("front", front().getOutput() == 0 ? "on" : "off");
        renderTop("right", right());
        renderTop("back", back());
        renderTop("left", left());

        RenderHelper.renderRedstoneTorch(3 / 16D, 0, 0, 12 / 16D, left().getInput() == 0 && left().isEnabled());
        RenderHelper.renderRedstoneTorch(-3 / 16D, 0, 0, 12 / 16D, right().getInput() == 0 && right().isEnabled());
        RenderHelper.renderRedstoneTorch(0, 0, 0, 12 / 16D, back().getInput() == 0 && back().isEnabled());

        RenderHelper.renderRedstoneTorch(0, 0, 4 / 16D, 14 / 16D, front().getOutput() > 0);
    }

    @Override
    public void tick() {

        if (left().getInput() == 0 && left().isEnabled())
            spawnBlueParticle(5 / 16D, 6 / 16D, 8 / 16D);
        if (right().getInput() == 0 && right().isEnabled())
            spawnBlueParticle(11 / 16D, 6 / 16D, 8 / 16D);
        if (back().getInput() == 0 && back().isEnabled())
            spawnBlueParticle(8 / 16D, 6 / 16D, 8 / 16D);

        if (front().getOutput() > 0)
            spawnBlueParticle(8 / 16D, 8 / 16D, 4 / 16D);
    }
}
