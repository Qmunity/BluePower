/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import net.minecraft.world.item.enchantment.Enchantment;

import com.bluepowermod.enchant.EnchantmentDisjunction;
import com.bluepowermod.enchant.EnchantmentVorpal;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPEnchantments {

	public static Enchantment vorpal;
	public static Enchantment disjunction;

	@SubscribeEvent
	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
		vorpal = new EnchantmentVorpal(Enchantment.Rarity.COMMON);
		disjunction = new EnchantmentDisjunction(Enchantment.Rarity.COMMON);

		event.getRegistry().register(vorpal.setRegistryName("bluepower:vorpal"));
		event.getRegistry().register(disjunction.setRegistryName("bluepower:disjunction"));
	}

	
}
