package com.bluepowermod.containers.slots;

/**
 * This class is copied from the BuildCraft code, which can be found here: https://github.com/BuildCraft/BuildCraft
 * @author CovertJaguar
 */
public interface IPhantomSlot {
    
    /*
     * Phantom Slots don't "use" items, they are used for filters and various
     * other logic slots.
     */
    boolean canAdjust();
}
