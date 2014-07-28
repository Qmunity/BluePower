package com.bluepowermod.init;

import net.minecraft.enchantment.Enchantment;
import com.bluepowermod.enchantments.EnchantmentDisjunction;
import com.bluepowermod.enchantments.EnchantmentVorpal;

public class BPEnchantments {

	public static Enchantment vorpal;
	public static Enchantment disjunction;
	
	public static void init() {
		vorpal = new EnchantmentVorpal(Config.vorpalEnchantmentId, 10);
		disjunction = new EnchantmentDisjunction(Config.disjunctionEnchantmentId, 10);
	}
	
}
