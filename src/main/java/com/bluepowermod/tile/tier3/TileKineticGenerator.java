/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

public class TileKineticGenerator extends TileBase implements WorldlyContainer{

	public int windspeed = 10;
	public int windtick = 0;
	public static final int SLOTS = 1;
	public TileKineticGenerator(BlockPos pos, BlockState state){
	    super(BPBlockEntityType.KINETIC_GENERATOR.get(), pos, state);
	}

	public static void tickKinetic(TileKineticGenerator tileKineticGenerator) {
		
        if (tileKineticGenerator.windspeed < 0){
            tileKineticGenerator.windtick += tileKineticGenerator.windspeed;
		}
	}
    private final NonNullList<ItemStack> allInventories = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Optional<ValueInput.ValueInputList> inputList = input.childrenList("inventory");

        if(inputList.isPresent()) {
            for (ValueInput inputItem : inputList.get()) {
                Optional<ItemStack> itemStack = inputItem.read("itemStack", ItemStack.CODEC);
                itemStack.ifPresent(allInventories::add);
            }
        }
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        ValueOutput.ValueOutputList outputList = valueOutput.childrenList("inventory");

        for (ItemStack itemStack : allInventories) {
            ValueOutput output = outputList.addChild();
            output.store("itemStack", ItemStack.CODEC, itemStack);
        }
    }

    @Override
    public int getContainerSize() {

        return allInventories.size();
    }


    @Override
    public ItemStack getItem(int i) {

        return this.allInventories.get(i);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {

        // this needs to be side aware as well
        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return getItem(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

        this.allInventories.set(i, itemStack);
        this.setChanged();
    }

    @Override
    public int getMaxStackSize() {

        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.blockPosition().closerThan(worldPosition,64.0D);
    }

    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {

        return true;
    }

    @Override
    public boolean isEmpty() {
        return allInventories.isEmpty();
    }


    @Override
    public void clearContent() {

    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (!stack.isEmpty()) drops.add(stack);
        return drops;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }
}
