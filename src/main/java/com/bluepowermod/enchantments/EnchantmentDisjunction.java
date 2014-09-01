/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
	
	@Override
	public String getTranslatedName(int level) {
		return StatCollector.translateToLocal("enchantment.disjunction.name") + " " + StatCollector.translateToLocal("enchantment.level." + level);
	}
}
