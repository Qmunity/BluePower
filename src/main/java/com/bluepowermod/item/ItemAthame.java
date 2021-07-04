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
import com.bluepowermod.helper.BPItemTier;
import com.bluepowermod.init.BPCreativeTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAthame extends SwordItem {
    
    private float               damageDealt;
    private static IItemTier athameMaterial = new BPItemTier(100, 6.0F, 2.0F, 0, 10, Ingredient.of(BPItems.silver_ingot));
    
    public ItemAthame() {
        super(athameMaterial, 1, -3, new Properties().tab(BPCreativeTabs.tools));
        this.setRegistryName(Refs.MODID + ":" + Refs.ATHAME_NAME);
        BPItems.itemList.add(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(MinecraftColor.PURPLE.getChatColor())
                .append(new TranslationTextComponent("item." + Refs.MODID + "." + Refs.ATHAME_NAME + ".info")) );
    }

    public float getDamageDealt() {
        return this.damageDealt;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
    
        this.damageDealt = athameMaterial.getAttackDamageBonus();
        if ((entity instanceof EndermanEntity) || (entity instanceof EnderDragonEntity)) {
            this.damageDealt += 18.0F;
        }
        entity.hurt(DamageSource.playerAttack((PlayerEntity) player), this.damageDealt);
        return super.hurtEnemy(stack, entity, player);
    }

    @Override
    public boolean isValidRepairItem(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == BPItems.silver_ingot || is2.getItem() == BPItems.silver_ingot));
    }
}
