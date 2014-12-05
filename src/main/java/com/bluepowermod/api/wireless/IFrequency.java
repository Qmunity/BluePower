package com.bluepowermod.api.wireless;

import java.util.UUID;

import com.bluepowermod.api.misc.Accessability;

public interface IFrequency {

    public Accessability getAccessability();

    public UUID getOwner();

    public String getFrequencyName();

}
