package net.quetzi.bluepower.init;

import net.minecraft.enchantment.Enchantment;
import net.quetzi.bluepower.enchantments.EnchantmentDisjunction;
import net.quetzi.bluepower.enchantments.EnchantmentVorpal;

public class BPEnchantments {

	public static Enchantment vorpal;
	public static Enchantment disjunction;
	
	public static void init() {
		vorpal = new EnchantmentVorpal(Config.vorpalEnchantmentId, 10);
		disjunction = new EnchantmentDisjunction(Config.disjunctionEnchantmentId, 10);
	}
	
}
