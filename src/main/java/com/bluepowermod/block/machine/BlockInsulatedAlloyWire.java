package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.TileInsulatedWire;
import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockInsulatedAlloyWire extends BlockAlloyWire{

    public BlockInsulatedAlloyWire(String type) {
        super(type, 2F , 2F);
        setRegistryName(Refs.MODID + ":" + "insulatedwire." + type );
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileInsulatedWire();
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        TileEntity tileentity = builder.get(LootParameters.BLOCK_ENTITY);
        List<ItemStack> itemStacks = new ArrayList<>();
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileInsulatedWire) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("color", ((TileInsulatedWire)tileentity).getColor().name());
            ItemStack stack = new ItemStack(this, 1, nbt);
            itemStacks.add(stack);
        }
        return itemStacks;
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);
        ItemStack stack = ItemStack.EMPTY;
        if(tileentity instanceof TileBPMultipart){
            tileentity = ((TileBPMultipart) tileentity).getTileForState(state);
        }
        if (tileentity instanceof TileInsulatedWire) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("color", ((TileInsulatedWire)tileentity).getColor().name());
            stack = new ItemStack(this, 1, nbt);
        }
        return stack;
    }


    @Override
    public int getColor(BlockState state, IBlockReader world, BlockPos pos, int tintIndex) {
        //Color for Block
        TileEntity tile = (world.getTileEntity(pos));
        if(tile instanceof TileBPMultipart){
            tile = ((TileBPMultipart)tile).getTileForState(state);
        }
        if(tile instanceof TileWire && tintIndex == 1) {
            return tile.getCapability(CapabilityRedstoneDevice.INSULATED_CAPABILITY).orElse(null).getInsulationColor(null).getHex();
        }
        return RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex();
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        //Color for Block
        MinecraftColor color = MinecraftColor.BLUE;
        if(stack.getTag() != null && stack.getTag().contains("color"))
            color = MinecraftColor.valueOf(stack.getTag().getString("color"));
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

}
