package com.bluepowermod.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.StatCollector;

public class EnchantmentVorpal extends Enchantment {

	public EnchantmentVorpal(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.weapon);
	}
	
	@Override
	public int getMaxLevel() {
		return 2;
	}
	
	@Override
	public int getMinEnchantability(int par1) {
		return 10 + 20 * (par1 - 1);
	}

	@Override
	public int getMaxEnchantability(int par1) {
		return super.getMinEnchantability(par1) + 50;
	}
	
	@Override
	public String getTranslatedName(int level) {
		return StatCollector.translateToLocal("enchantment.vorpal.name") + " " + StatCollector.translateToLocal("enchantment.level." + level);
	}

}
