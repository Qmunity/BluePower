package com.bluepowermod.tile.tier2;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.tube.ITubeConnection;
import com.bluepowermod.block.machine.BlockTube;
import com.bluepowermod.container.stack.TubeStack;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileTube extends TileBase implements ITubeConnection {
    public MinecraftColor color = MinecraftColor.ANY;
    public List<BlockPos> connectedPipes = new ArrayList<>();
    public List<BlockPos> neighboringBlockEntities = new ArrayList<>();
    public List<TubeStack> tubeStacks = new ArrayList<>();

    public TileTube(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.TUBE.get(), pos, state);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        color = MinecraftColor.values()[tCompound.getInt("color")];

        //Read the tube stacks
        int size = tCompound.getInt("tubeStacksSize");
        for (int j = 0; j < size; j++) {
            CompoundTag tag = tCompound.getCompound("tubeStack" + j);
            TubeStack tubeStack = TubeStack.loadFromNBT(level.registryAccess(), tag);
            this.tubeStacks.set(j, tubeStack);
        }

        //Read the connected pipes
        size = tCompound.getInt("connectedPipesSize");
        for (int j = 0; j < size; j++) {
            BlockPos pos = BlockPos.of(tCompound.getLong("connectedPipe" + j));
            this.connectedPipes.set(j, pos);
        }

        //Read the neighboring block entities
        size = tCompound.getInt("neighboringBlockEntitiesSize");
        for (int j = 0; j < size; j++) {
            BlockPos pos = BlockPos.of(tCompound.getLong("neighboringBlockEntity" + j));
            this.neighboringBlockEntities.set(j, pos);
        }

    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        tCompound.putInt("color", color.ordinal());

        //Write the tube stacks
        int i = 0;
        for (TubeStack tubeStack : tubeStacks) {
            CompoundTag tag = new CompoundTag();
            tubeStack.writeToNBT(level.registryAccess(), tag);
            tCompound.put("tubeStack" + i, tag);
            i++;
        }
        tCompound.putInt("tubeStacksSize", tubeStacks.size());

        //Write the connected pipes
        i = 0;
        for (BlockPos pos : connectedPipes) {
            tCompound.putLong("connectedPipe" + i, pos.asLong());
            i++;
        }
        tCompound.putInt("connectedPipesSize", connectedPipes.size());

        //Write the neighboring block entities
        i = 0;
        for (BlockPos pos : neighboringBlockEntities) {
            tCompound.putLong("neighboringBlockEntity" + i, pos.asLong());
            i++;
        }
        tCompound.putInt("neighboringBlockEntitiesSize", neighboringBlockEntities.size());
    }

    public boolean setColor(MinecraftColor color) {
        if(this.color != color) {
            this.color = color;
            return true;
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {
        NonNullList<ItemStack> itemStacks = super.getDrops();
        for (TubeStack tubeStack : tubeStacks) {
            itemStacks.add(tubeStack.stack);
        }
        return itemStacks;
    }

    public static void tickTube(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!(blockEntity instanceof TileTube)) {
            return;
        }
        for (Iterator<TubeStack> iterator = ((TileTube) blockEntity).tubeStacks.iterator(); iterator.hasNext();) {
            TubeStack tubeStack = iterator.next();
            if (IOHelper.canInterfaceWith(level.getBlockEntity(pos.relative(tubeStack.heading)), tubeStack.heading)) {
                ItemStack returnedStack = IOHelper.insert(level.getBlockEntity(pos.relative(tubeStack.heading)), tubeStack.stack, tubeStack.heading, tubeStack.color, false);
                if (returnedStack.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public boolean isConnectedTo(Direction from) {
        return level.getBlockState(getBlockPos()).getValue(BlockTube.PROPERTY_BY_DIRECTION.get(from));
    }

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, Direction from, boolean simulate) {
        if(!simulate) {
            tubeStacks.add(stack);
        }
        return stack;
    }
}
