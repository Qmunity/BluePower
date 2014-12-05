package com.bluepowermod.part.gate.wireless;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.friend.FriendManager;
import uk.co.qmunity.lib.friend.IPlayer;

import com.bluepowermod.api.misc.Accessability;
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;
import com.bluepowermod.api.wireless.IWirelessManager;
import com.bluepowermod.part.gate.wireless.Frequency.BundledFrequency;
import com.bluepowermod.part.gate.wireless.Frequency.RedstoneFrequency;

public final class WirelessManager implements IWirelessManager {

    public static final WirelessManager INSTANCE = new WirelessManager();

    private WirelessManager() {

    }

    private List<IRedstoneFrequency> redstoneFrequencies = new ArrayList<IRedstoneFrequency>();
    private List<IBundledFrequency> bundledFrequencies = new ArrayList<IBundledFrequency>();

    @Override
    public List<IRedstoneFrequency> getRedstoneFrequencies() {

        return redstoneFrequencies;
    }

    @Override
    public List<IBundledFrequency> getBundledFrequencies() {

        return bundledFrequencies;
    }

    @Override
    public List<IRedstoneFrequency> getUseableRedstoneFrequencies(EntityPlayer player) {

        UUID uuid = player.getGameProfile().getId();
        List<IPlayer> friends = FriendManager.getFriends(uuid);
        List<IRedstoneFrequency> frequencies = new ArrayList<IRedstoneFrequency>();

        for (IRedstoneFrequency freq : redstoneFrequencies)
            if (canAccess(freq, uuid, friends))
                frequencies.add(freq);

        return frequencies;
    }

    @Override
    public List<IBundledFrequency> getUseableBundledFrequencies(EntityPlayer player) {

        UUID uuid = player.getGameProfile().getId();
        List<IPlayer> friends = FriendManager.getFriends(uuid);
        List<IBundledFrequency> frequencies = new ArrayList<IBundledFrequency>();

        for (IBundledFrequency freq : bundledFrequencies)
            if (canAccess(freq, uuid, friends))
                frequencies.add(freq);

        return frequencies;
    }

    private boolean canAccess(IFrequency freq, UUID uuid, List<IPlayer> friends) {

        if (freq.getOwner().equals(uuid) || freq.getAccessability() == Accessability.PUBLIC) {
            return true;
        }
        if (freq.getAccessability() == Accessability.SHARED) {
            for (IPlayer friend : friends) {
                if (freq.getOwner().equals(friend.getUUID())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IRedstoneFrequency registerRedstoneFrequency(EntityPlayer owner, String frequency, Accessability accessability) {

        RedstoneFrequency freq = new RedstoneFrequency(accessability, owner == null ? UUID.randomUUID() : owner.getGameProfile().getId(),
                frequency);
        redstoneFrequencies.add(freq);
        return freq;
    }

    @Override
    public IBundledFrequency registerBundledFrequency(EntityPlayer owner, String frequency, Accessability accessability) {

        BundledFrequency freq = new BundledFrequency(accessability, owner == null ? UUID.randomUUID() : owner.getGameProfile().getId(),
                frequency);
        bundledFrequencies.add(freq);
        return freq;
    }

    @Override
    public IFrequency registerFrequency(EntityPlayer owner, String frequency, Accessability accessability, boolean isBundled) {

        return isBundled ? registerBundledFrequency(owner, frequency, accessability) : registerRedstoneFrequency(owner, frequency,
                accessability);
    }

    @Override
    public void unregisterFrequency(IFrequency frequency) {

        redstoneFrequencies.remove(frequency);
        bundledFrequencies.remove(frequency);
    }

}
