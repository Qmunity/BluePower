package net.quetzi.bluepower.api.part;

import net.minecraft.nbt.NBTTagCompound;

public class RedstoneConnection {
    
    private BPPartFace part;
    
    private boolean    isInput   = true;
    
    private int        power     = 0;
    
    private boolean    isEnabled = false;
    
    private String     id        = "";
    
    public RedstoneConnection(BPPartFace part, String id) {
    
        this.part = part;
        this.id = id;
    }
    
    public RedstoneConnection(BPPartFace part, String id, boolean isInput) {
    
        this(part, id);
        this.isInput = isInput;
    }
    
    public RedstoneConnection(BPPartFace part, String id, boolean isInput, boolean isEnabled) {
    
        this(part, id, isInput);
        this.isEnabled = isEnabled;
    }
    
    public void setInput() {
    
        this.isInput = true;
        part.notifyUpdate();
    }
    
    public void setOutput() {
    
        this.isInput = false;
        part.notifyUpdate();
    }
    
    public boolean isInput() {
    
        return isInput;
    }
    
    public boolean isOutput() {
    
        return !isInput;
    }
    
    public void enable() {
    
        this.isEnabled = true;
        part.notifyUpdate();
    }
    
    public void disable() {
    
        this.isEnabled = false;
        part.notifyUpdate();
    }
    
    public boolean isEnabled() {
    
        return isEnabled;
    }
    
    public void setPower(int power) {
    
        this.power = power;
        part.notifyUpdate();
    }
    
    public int getPower() {
    
        return power;
    }
    
    public void setID(String id) {
    
        this.id = id;
    }
    
    public String getID() {
    
        return id;
    }
    
    public NBTTagCompound getNBTTag() {
    
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isInput", isInput);
        tag.setBoolean("isEnabled", isEnabled);
        tag.setInteger("power", power);
        tag.setString("id", id);
        return tag;
    }
    
    public void load(NBTTagCompound tag) {
    
        isInput = tag.getBoolean("isInput");
        isEnabled = tag.getBoolean("isEnabled");
        power = tag.getInteger("power");
        id = tag.getString("id");
    }
    
}
