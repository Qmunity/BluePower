package com.bluepowermod.helper;

import com.bluepowermod.init.BPItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SilverItemTier implements Tier {

    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int harvestLevel;
    private final int enchantability;

    public SilverItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability){
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
        return Ingredient.of(BPItems.silver_ingot.get());
    }
}