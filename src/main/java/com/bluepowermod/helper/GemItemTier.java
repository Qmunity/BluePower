package com.bluepowermod.helper;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class GemItemTier implements IItemTier {

    private Ingredient ingredient;

    public GemItemTier(Ingredient ingredient){
        this.ingredient = ingredient;
    }

    @Override
    public int getMaxUses() {
        return 750;
    }

    @Override
    public float getEfficiency() {
        return 6;
    }

    @Override
    public float getAttackDamage() {
        return 2;
    }

    @Override
    public int getHarvestLevel() {
        return 4;
    }

    @Override
    public int getEnchantability() {
        return 18;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return ingredient;
    }
}