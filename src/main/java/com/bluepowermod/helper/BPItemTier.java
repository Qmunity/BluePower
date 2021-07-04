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
    public int getUses() {
        return maxUses;
    }

    @Override
    public float getSpeed() {
        return efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ingredient;
    }
}