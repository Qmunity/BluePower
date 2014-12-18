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

package com.bluepowermod.part;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.util.Dir;

public class RedstoneConnection {

    private final IPart part;
    private final Dir dir;

    private int out = 0;
    private int in = 0;
    private boolean caching = true;

    private boolean enabled = false;
    private boolean outOnly = false;

    public RedstoneConnection(IPart part, Dir direction) {

        this.part = part;
        dir = direction;
    }

    public Dir getDirection() {

        return dir;
    }

    public int getOutput() {

        return out;
    }

    public int getInput() {

        if (caching)
            return in;

        caching = true;
        update();
        caching = false;
        return in;
    }

    /**
     * Used in Integrated Circuits
     *
     * @param input
     */
    public void setInput(int input) {

        in = input;
        caching = true;
    }

    public RedstoneConnection setOutput(boolean out) {

        return setOutput(out ? 15 : 0);
    }

    public RedstoneConnection setOutput(int out) {

        return setOutput(out, true);
    }

    public RedstoneConnection setOutput(int out, boolean update) {

        int last = this.out;
        this.out = out;

        if (last != out && update)
            forceUpdateNeighbor();

        return this;
    }

    public void forceUpdateNeighbor() {

        ForgeDirection d = getFD();
        World world = part.getWorld();
        int x = part.getX(), y = part.getY(), z = part.getZ();

        RedstoneHelper.notifyRedstoneUpdate(world, x, y, z, d, world.getBlock(x, y, z).isNormalCube(world, x, y, z));
    }

    public boolean isEnabled() {

        return enabled;
    }

    public RedstoneConnection setEnabled(boolean enabled) {

        this.enabled = enabled;

        return this;
    }

    public RedstoneConnection enable() {

        enabled = true;

        return this;
    }

    public RedstoneConnection disable() {

        enabled = false;

        return this;
    }

    public void setOutputOnly() {

        outOnly = true;
    }

    public void setBidirectional() {

        outOnly = false;
    }

    public boolean isOutputOnly() {

        return outOnly;
    }

    public void update() {

        if (part.getWorld().isRemote)
            return;

        if (caching) {
            if (part instanceof IPartFace) {
                in = RedstoneHelper.getInput(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD(), ((IPartFace) part).getFace());
            } else {
                in = RedstoneHelper.getInput(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD());
            }
        }
    }

    private ForgeDirection getFD() {

        if (part instanceof IPartFace) {
            if (part instanceof BPPartFaceRotate) {
                return getDirection().toForgeDirection(((IPartFace) part).getFace(), ((BPPartFaceRotate) part).getRotation());
            } else {
                return getDirection().toForgeDirection(((IPartFace) part).getFace(), 0);
            }
        }
        return getDirection().getFD();
    }

    public void writeToNBT(NBTTagCompound tag) {

        tag.setBoolean("enabled", enabled);
        tag.setInteger("in", in);
        tag.setInteger("out", out);
    }

    public void readFromNBT(NBTTagCompound tag) {

        enabled = tag.getBoolean("enabled");
        in = tag.getInteger("in");
        out = tag.getInteger("out");
    }

}