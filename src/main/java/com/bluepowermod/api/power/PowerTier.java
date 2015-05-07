package com.bluepowermod.api.power;

/**
 * @author Koen Beckers (K4Unl)
 */
public enum PowerTier {
    LOWVOLTAGE(0), MEDIUMVOLTAGE(1), HIGHVOLTAGE(2), INVALID(-1);

    private final int tier;

    public static final PowerTier[] VALID_TIERS = { LOWVOLTAGE, MEDIUMVOLTAGE, HIGHVOLTAGE };

    private PowerTier(int _tier) {

        this.tier = _tier;
    }

    @Override
    public String toString() {

        return "TIER-" + tier;
    }

    public static PowerTier fromOrdinal(int tier){
        if(tier <= 2 && tier >= 0){
            return VALID_TIERS[tier];
        }else{
            return INVALID;
        }
    }
}
