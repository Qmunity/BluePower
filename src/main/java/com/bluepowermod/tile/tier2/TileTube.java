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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class TileTube extends TileBase implements ITubeConnection {
    public MinecraftColor color = MinecraftColor.ANY;
    public List<BlockPos> connectedPipes = new ArrayList<>();
    public List<BlockPos> neighboringBlockEntities = new ArrayList<>();
    public List<TubeStack> tubeStacks = new ArrayList<>();

    public TileTube(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.TUBE.get(), pos, state);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        color = MinecraftColor.values()[input.getIntOr("color", MinecraftColor.ANY.ordinal())];

        //Read the tube stacks
        Optional<ValueInput.ValueInputList> tubeList = input.childrenList("tubeStacks");
        if(tubeList.isPresent()){
            for (ValueInput child : tubeList.get()) {
                this.tubeStacks.add(TubeStack.loadFromInput(child));
            }
        }

        //Read the connected pipes
        Optional<ValueInput.ValueInputList> pipesList = input.childrenList("connectedPipes");
        if(pipesList.isPresent()){
            for (ValueInput child : pipesList.get()) {
                BlockPos pos = child.read("pos", BlockPos.CODEC).orElse(BlockPos.ZERO);
                this.connectedPipes.add(pos);
            }
        }

        //Read the neighboring block entities
        Optional<ValueInput.ValueInputList> neighborList = input.childrenList("neighboringBlockEntities");
        if(neighborList.isPresent()){
            for (ValueInput child : neighborList.get()) {
                BlockPos pos = child.read("pos", BlockPos.CODEC).orElse(BlockPos.ZERO);
                this.neighboringBlockEntities.add(pos);
            }
        }

    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putInt("color", color.ordinal());
        ValueOutput.ValueOutputList outputList = valueOutput.childrenList("tubeStacks");
        //Write the tube stacks
        for (TubeStack tubeStack : tubeStacks) {
            tubeStack.writeToOutput(outputList.addChild());
        }

        //Write the connected pipes
        ValueOutput.ValueOutputList pipeList = valueOutput.childrenList("connectedPipes");
        for (BlockPos pos : connectedPipes) {
            outputList.addChild().store("pos", BlockPos.CODEC, pos);
        }

        //Write the neighboring block entities
        ValueOutput.ValueOutputList neighborList = valueOutput.childrenList("neighboringBlockEntities");
        for (BlockPos pos : neighboringBlockEntities) {
            outputList.addChild().store("pos", BlockPos.CODEC, pos);
        }
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
