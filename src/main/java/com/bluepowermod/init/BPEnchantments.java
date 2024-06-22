/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class BPEnchantments {

	public static ResourceKey<Enchantment> VORPAL = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Refs.MODID, "vorpal"));
	public static ResourceKey<Enchantment> DISJUNCTION = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Refs.MODID, "disjunction"));
	
}
