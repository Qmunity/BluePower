/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.item;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.helper.GemItemTier;
import com.bluepowermod.helper.SilverItemTier;
import com.bluepowermod.init.BPCreativeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAthame extends SwordItem {
    
    private float damageDealt;
    private static Tier athameMaterial = new SilverItemTier(100, 6.0F, 2.0F, 0, 10);
    
    public ItemAthame() {
        super(athameMaterial, 1, -3, new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal(MinecraftColor.PURPLE.getChatColor())
                .append(Component.translatable("item." + Refs.MODID + "." + Refs.ATHAME_NAME + ".info")) );
    }

    public float getDamageDealt() {
        return this.damageDealt;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
    
        this.damageDealt = athameMaterial.getAttackDamageBonus();
        if ((entity instanceof EnderMan) || (entity instanceof EnderDragon)) {
            this.damageDealt += 18.0F;
        }
        entity.hurt(DamageSource.playerAttack((Player) player), this.damageDealt);
        return super.hurtEnemy(stack, entity, player);
    }

    @Override
    public boolean isValidRepairItem(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == BPItems.silver_ingot.get() || is2.getItem() == BPItems.silver_ingot.get()));
    }
}
