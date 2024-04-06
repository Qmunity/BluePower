/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

import com.bluepowermod.enchant.EnchantmentDisjunction;
import com.bluepowermod.enchant.EnchantmentVorpal;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BPEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, Refs.MODID);

	public static DeferredHolder<Enchantment, Enchantment> vorpal = ENCHANTMENT.register("vorpal", () -> new EnchantmentVorpal(Enchantment.Rarity.COMMON));
	public static DeferredHolder<Enchantment, Enchantment> disjunction = ENCHANTMENT.register("disjunction", () -> new EnchantmentDisjunction(Enchantment.Rarity.COMMON));
	
}
