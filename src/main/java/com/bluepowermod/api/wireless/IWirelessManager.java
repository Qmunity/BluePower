package com.bluepowermod.api.wireless;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.api.misc.Accessability;

public interface IWirelessManager {

    public List<IRedstoneFrequency> getRedstoneFrequencies();

    public List<IBundledFrequency> getBundledFrequencies();

    public List<IRedstoneFrequency> getAvailableRedstoneFrequencies(EntityPlayer player);

    public List<IBundledFrequency> getAvailableBundledFrequencies(EntityPlayer player);

    public IRedstoneFrequency registerRedstoneFrequency(EntityPlayer owner, String frequency, Accessability accessability);

    public IBundledFrequency registerBundledFrequency(EntityPlayer owner, String frequency, Accessability accessability);

    public IFrequency registerFrequency(EntityPlayer owner, String frequency, Accessability accessability, boolean isBundled);

    public void unregisterFrequency(IFrequency frequency);

}
