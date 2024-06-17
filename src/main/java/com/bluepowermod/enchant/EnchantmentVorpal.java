/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.enchant;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

public class EnchantmentVorpal extends Enchantment {

    public EnchantmentVorpal() {
        //TODO: 1.20.6 - Confirm Values
        super(definition(
                        ItemTags.WEAPON_ENCHANTABLE, //EnchantmentTag
                        2, //Weight
                        2, //Max Level
                        dynamicCost(10, 20), //Cost Min
                        dynamicCost(30, 20),  //Cost Max
                        20, //Cost Anvil
                        EquipmentSlot.values()
                )
        );
    }

}
