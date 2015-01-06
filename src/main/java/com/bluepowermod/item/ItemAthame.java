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

import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

public class ItemAthame extends ItemSword {
    
    private float               damageDealt;
    private static ToolMaterial athameMaterial = EnumHelper.addToolMaterial("SILVER", 0, 100, 6.0F, 2.0F, 10);
    
    public ItemAthame() {
    
        super(athameMaterial);
        this.setCreativeTab(BPCreativeTabs.tools);
        this.setMaxDamage(100);
        this.setUnlocalizedName(Refs.ATHAME_NAME);
        this.setTextureName(Refs.MODID + ":" + Refs.ATHAME_NAME);
        this.maxStackSize = 1;
        this.setFull3D();
    }
    
    @Override
    public float func_150931_i() {
    
        return this.damageDealt;
    }
    
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase player) {
    
        this.damageDealt = athameMaterial.getDamageVsEntity();
        if ((entity instanceof EntityEnderman) || (entity instanceof EntityDragon)) {
            this.damageDealt += 18.0F;
        }
        entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), this.damageDealt);
        return super.hitEntity(stack, entity, player);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isRepairable() {

        return canRepair && isDamageable();
    }

    @Override
    public boolean getIsRepairable(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == BPItems.silver_ingot || is2.getItem() == BPItems.silver_ingot));
    }
}
