package com.bluepowermod.helper;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class BPItemTier implements IItemTier {

    private Ingredient ingredient;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int harvestLevel;
    private final int enchantability;

    public BPItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Ingredient ingredient){
        this.ingredient = ingredient;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
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