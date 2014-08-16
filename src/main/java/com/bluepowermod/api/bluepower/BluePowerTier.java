package com.bluepowermod.api.bluepower;

/**
 * @author Koen Beckers (K4Unl)
 */
public enum BluePowerTier {
    LOWVOLTAGE(0), MEDIUMVOLTAGE(1), HIGHVOLTAGE(2), INVALID(-1);

    private final int tier;

    public static final BluePowerTier[] VALID_TIERS = { LOWVOLTAGE, MEDIUMVOLTAGE, HIGHVOLTAGE };

    private BluePowerTier(int _tier) {

        this.tier = _tier;
    }

    @Override
    public String toString() {

        return "TIER-" + tier;
    }

    public static BluePowerTier fromOrdinal(int tier){
        if(tier <= 2 && tier >= 0){
            return VALID_TIERS[tier];
        }else{
            return INVALID;
        }
    }
}
