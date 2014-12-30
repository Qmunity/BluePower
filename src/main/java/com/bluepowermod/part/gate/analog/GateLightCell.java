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

package com.bluepowermod.part.gate.analog;

import com.bluepowermod.part.gate.GateBase;


public class GateLightCell extends GateBase {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "lightCell";
    }

    @Override
    public void renderTop(float frame) {

        renderTop("front", front());
    }

    @Override
    public void tick() {

        if (getWorld().getWorldTime() % 5 == 0) {
            front().setOutput(getWorld().getBlockLightValue(getX(), getY(), getZ()));
        }
    }

    @Override
    public void doLogic() {

    }

}
