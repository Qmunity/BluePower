package com.bluepowermod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.tileentities.TileMotor;
import com.bluepowermod.util.Refs;

public class BlockMotor extends BlockContainer {

    public BlockMotor() {

        super(Material.iron);
        setBlockName(Refs.MOTOR_NAME);
    }

    @Override
    public TileEntity createNewTileEntity(World w, int m) {

        return new TileMotor();
    }

    @Override
    public int getRenderType() {

        return -1;
    }
}
