package com.bluepowermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.StatCollector;

public class EnchantmentDisjunction extends Enchantment {

	public EnchantmentDisjunction(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.weapon);
	}
	
	@Override
	public int getMaxLevel() {
		return 5;
	}
	
	@Override
	public int getMinEnchantability(int par1) {
        return 5 + (par1 - 1) * 8;
    }

	@Override
    public int getMaxEnchantability(int par1) {
        return this.getMinEnchantability(par1) + 20;
    }
	// TODO: Broken on 1.7.10 update
//	@SuppressWarnings("cast")
//	@Override
//	public float calcModifierLiving(int par1, EntityLivingBase entity) {
//		return entity instanceof EntityEnderman ? (float)par1 * 2.5F : (entity instanceof EntitySkeleton ? (((EntitySkeleton) entity).getSkeletonType() == 1 ? (float)par1 * 2.5F : 0.0F) : 0.0F);
//	}
	
	@Override
	public String getTranslatedName(int level) {
		return StatCollector.translateToLocal("enchantment.disjunction.name") + " " + StatCollector.translateToLocal("enchantment.level." + level);
	}
}
