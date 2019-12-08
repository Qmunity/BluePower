package com.bluepowermod.tile;

import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.HashMap;
import java.util.Map;

public class TileBPMultipart extends TileEntity implements ITickable, IMultipartTile {
    public static final ModelProperty<Map<IBlockState, IModelData>> PROPERTY_INFO = new ModelProperty<>();
    private Map<IBlockState, TileEntity> stateMap = new HashMap<>();

    @Override
    public void update() {

    }
}
