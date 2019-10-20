package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlockInsulatedAlloyWire extends BlockAlloyWire {

    private final MinecraftColor color;

    public BlockInsulatedAlloyWire(String type, MinecraftColor color) {
        super(type, 2, 3);
        this.color = color;
        setRegistryName(Refs.MODID + ":" + "wire." + type + "." + color.name().toLowerCase());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileWire(color);
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityRedstoneDevice.INSULATED_CAPABILITY;
    }

    @Override
    public int getColor(IBlockReader world, BlockPos pos, int tintIndex) {
        //Color for Block
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : color.getHex();
    }

    @Override
    public int getColor(int tintIndex) {
        //Color for Block
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() :  color.getHex();
    }

}
