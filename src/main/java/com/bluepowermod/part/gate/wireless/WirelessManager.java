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

package com.bluepowermod.part.gate.wireless;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import uk.co.qmunity.lib.friend.FriendManager;
import uk.co.qmunity.lib.friend.IPlayer;

import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;
import com.bluepowermod.api.wireless.IWirelessManager;
import com.bluepowermod.part.gate.wireless.Frequency.BundledFrequency;
import com.bluepowermod.part.gate.wireless.Frequency.RedstoneFrequency;

import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
    public List<IRedstoneFrequency> getAvailableRedstoneFrequencies(EntityPlayer player) {

        UUID uuid = player.getGameProfile().getId();
        List<IPlayer> friends = FriendManager.getFriends(uuid);
        List<IRedstoneFrequency> frequencies = new ArrayList<IRedstoneFrequency>();

        for (IRedstoneFrequency freq : redstoneFrequencies)
            if (canAccess(freq, uuid, friends))
                frequencies.add(freq);

        return frequencies;
    }

    @Override
    public List<IBundledFrequency> getAvailableBundledFrequencies(EntityPlayer player) {

        UUID uuid = player.getGameProfile().getId();
        List<IPlayer> friends = FriendManager.getFriends(uuid);
        List<IBundledFrequency> frequencies = new ArrayList<IBundledFrequency>();

        for (IBundledFrequency freq : bundledFrequencies)
            if (canAccess(freq, uuid, friends))
                frequencies.add(freq);

        return frequencies;
    }

    private boolean canAccess(IFrequency freq, UUID uuid, List<IPlayer> friends) {

        if (freq.getOwner().equals(uuid) || freq.getAccessibility() == com.bluepowermod.api.misc.Accessibility.PUBLIC) {
            return true;
        }
        if (freq.getAccessibility() == com.bluepowermod.api.misc.Accessibility.SHARED) {
            for (IPlayer friend : friends) {
                if (freq.getOwner().equals(friend.getUUID())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IRedstoneFrequency registerRedstoneFrequency(EntityPlayer owner, String frequency, com.bluepowermod.api.misc.Accessibility accessibility) {

        RedstoneFrequency freq = new RedstoneFrequency(accessibility, owner == null ? UUID.randomUUID() : owner.getGameProfile().getId(),
                frequency);
        redstoneFrequencies.add(freq);
        return freq;
    }

    @Override
    public IBundledFrequency registerBundledFrequency(EntityPlayer owner, String frequency, com.bluepowermod.api.misc.Accessibility accessibility) {

        BundledFrequency freq = new BundledFrequency(accessibility, owner == null ? UUID.randomUUID() : owner.getGameProfile().getId(),
                frequency);
        bundledFrequencies.add(freq);
        return freq;
    }

    @Override
    public IFrequency registerFrequency(EntityPlayer owner, String frequency, com.bluepowermod.api.misc.Accessibility accessibility, boolean isBundled) {

        return isBundled ? registerBundledFrequency(owner, frequency, accessibility) : registerRedstoneFrequency(owner, frequency,
                                                                                                                 accessibility);
    }

    @Override
    public void unregisterFrequency(IFrequency frequency) {

        redstoneFrequencies.remove(frequency);
        bundledFrequencies.remove(frequency);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {

    }

    @SubscribeEvent
    public void onServerStop(FMLServerStoppingEvent event) {

    }

    public void loadFrequencies(World world) {

    }

    public void unloadFrequencies() {

    }

}
