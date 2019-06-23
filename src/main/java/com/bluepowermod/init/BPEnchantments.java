/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.init;

import net.minecraft.enchantment.Enchantment;

import com.bluepowermod.enchant.EnchantmentDisjunction;
import com.bluepowermod.enchant.EnchantmentVorpal;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BPEnchantments {

	public static Enchantment vorpal;
	public static Enchantment disjunction;
	
	public static void init() {
		vorpal = new EnchantmentVorpal(Config.vorpalEnchantmentId, Enchantment.Rarity.COMMON);
		disjunction = new EnchantmentDisjunction(Config.disjunctionEnchantmentId, Enchantment.Rarity.COMMON);
	}

	@SubscribeEvent
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(vorpal.setRegistryName("bluepower:vorpal"));
		event.getRegistry().register(disjunction.setRegistryName("bluepower:disjunction"));
	}

	
}
