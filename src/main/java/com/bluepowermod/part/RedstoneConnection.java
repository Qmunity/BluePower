package com.bluepowermod.part;

import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.util.Dir;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

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
        RedstoneHelper.notifyRedstoneUpdate(part.getWorld(), part.getX(), part.getY(), part.getZ(), getFD(), true);
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