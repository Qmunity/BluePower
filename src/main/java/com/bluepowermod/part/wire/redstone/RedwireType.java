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

package com.bluepowermod.part.wire.redstone;

public enum RedwireType {

    BLUESTONE(false, false, 0x0000FF), RED_ALLOY(true, true, 0xFF0000), INFUSED_TESLATITE(true, false, 0xFF00FF);

    private boolean analog, loss;
    private int color;

    private RedwireType(boolean analog, boolean loss, int color) {

        this.analog = analog;
        this.loss = loss;
        this.color = color;
    }

    public boolean isAnalog() {

        return analog;
    }

    public boolean hasLoss() {

        return loss;
    }

    public String getName() {

        return name().toLowerCase().replace("_", "");
    }

    public int getColor() {

        return color;
    }

}
