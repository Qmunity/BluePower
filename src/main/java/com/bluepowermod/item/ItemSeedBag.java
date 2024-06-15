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
 *     
 *     @author Lumien
 */

package com.bluepowermod.item;

import com.bluepowermod.container.ContainerSeedBag;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ItemSeedBag extends ItemBase implements MenuProvider {

    public ItemSeedBag() {
        super(new Properties().stacksTo(1));
    }

    public static ItemStack getSeedType(HolderLookup.Provider provider, ItemStack seedBag) {
        ItemStack seed = ItemStack.EMPTY;

        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Items from the NBT Handler
        if (seedBag.has(DataComponents.CUSTOM_DATA)) seedBagInvHandler.deserializeNBT(provider, seedBag.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("inv"));

        for (int i = 0; i < 9; i++) {
            ItemStack is = seedBagInvHandler.getStackInSlot(i);
            if (!is.isEmpty()) {
                seed = is;
            }
        }

        return seed;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        return 1 - getItemDamageForDisplay(Minecraft.getInstance().level.registryAccess(), stack) / 576;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {

        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY) != CustomData.EMPTY;
    }

    public int getItemDamageForDisplay(HolderLookup.Provider provider, ItemStack stack) {

        int items = 0;
        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Items from the NBT Handler
        if (stack.has(DataComponents.CUSTOM_DATA)) seedBagInvHandler.deserializeNBT(provider, stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("inv"));

        for (int i = 0; i < 8; i++) {
            ItemStack is = seedBagInvHandler.getStackInSlot(i);
            if (!is.isEmpty()) {
                items += is.getCount();
            }
        }
        return items;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {

        return 576;
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
        if (!world.isClientSide && player.isCrouching()) {
            player.openMenu(this);
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, player.getItemInHand(handIn));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level worldIn = context.getLevel();
        InteractionHand hand = context.getHand();
        BlockPos pos = context.getClickedPos();

        if (player.isCrouching()) {
            return InteractionResult.PASS;
        }

        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

        //Get Active hand
        InteractionHand activeHand = InteractionHand.MAIN_HAND;
        ItemStack seedBag = player.getItemInHand(activeHand);
        if(!(seedBag.getItem() instanceof ItemSeedBag)){
            seedBag = player.getOffhandItem();
            activeHand = InteractionHand.OFF_HAND;
        }

        //Get Items from the NBT Handler
        if (seedBag.has(DataComponents.CUSTOM_DATA)) seedBagInvHandler.deserializeNBT(context.getPlayer().level().registryAccess(), seedBag.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound("inv"));

        ItemStack seed = getSeedType(player.level().registryAccess(), player.getItemInHand(hand));
        Block block = Block.byItem(seed.getItem());
        if (!seed.isEmpty() && block instanceof IPlantable) {
            IPlantable plant = (IPlantable) block;
            BlockState b = worldIn.getBlockState(pos);
            if (b.getBlock().canSustainPlant(b, worldIn, pos, Direction.UP, plant)
                    && worldIn.isEmptyBlock(pos.relative(Direction.UP))) {
                for (int i = 0; i < 9; i++) {
                    ItemStack is = seedBagInvHandler.getStackInSlot(i);
                    if (!is.isEmpty()) {
                        worldIn.setBlock(pos.relative(Direction.UP), block.defaultBlockState(), 0);
                        seedBagInvHandler.extractItem(i, 1, false);
                        break;
                    }
                }

                //Update items in the NBT
                CompoundTag tag =  new CompoundTag();
                tag.put("inv", seedBagInvHandler.serializeNBT(context.getPlayer().level().registryAccess()));
                seedBag.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.SEEDBAG_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerSeedBag(id, inventory);
    }

}
