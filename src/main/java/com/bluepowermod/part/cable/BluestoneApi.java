package com.bluepowermod.part.cable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.vec.Vector3;
import com.jcraft.jorbis.Block;

public class BluestoneApi implements IBluestoneApi {

    private static BluestoneApi INSTANCE = new BluestoneApi();

    private BluestoneApi() {

    }

    public static BluestoneApi getInstance() {

        return INSTANCE;
    }

    private List<IBluestoneConnect> connectionHandlers = new ArrayList<IBluestoneConnect>();

    @Override
    public void registerSpecialConnection(Block block, int metadata, ForgeDirection cableSide, int extraLength) {

    }

    @Override
    public void registerSpecialConnection(TileEntity te, ForgeDirection cableSide, int extraLength) {

    }

    @Override
    public void registerSpecialConnection(IBluestoneConnect connection) {

        if (connection == null)
            return;
        if (connectionHandlers.contains(connection))
            return;

        connectionHandlers.add(connection);
    }

    @Override
    public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

        int extra = 0;
        for (IBluestoneConnect c : connectionHandlers)
            extra = Math.max(extra, c.getExtraLength(block, cableFace, cableSide));

        return extra;
    }

    @Override
    public boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

        for (IBluestoneConnect c : connectionHandlers)
            if (c.canConnect(block, cableFace, cableSide))
                return true;

        return false;
    }

    // Register default handling
    static {

        BluestoneApi api = getInstance();

        api.registerSpecialConnection(new BluestoneConnectVanilla());

    }

    // Vanilla handling
    private static class BluestoneConnectVanilla implements IBluestoneConnect {

        @Override
        public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            if (block.getBlock() == Blocks.lever)
                return 5;
            if (block.getBlock() == Blocks.wooden_pressure_plate || block.getBlock() == Blocks.stone_pressure_plate
                    || block.getBlock() == Blocks.heavy_weighted_pressure_plate || block.getBlock() == Blocks.light_weighted_pressure_plate)
                return 1;

            return 0;
        }

        @Override
        public boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            if (block.getBlock() == Blocks.redstone_lamp || block.getBlock() == Blocks.noteblock || block.getBlock() == Blocks.piston
                    || block.getBlock() == Blocks.sticky_piston || block.getBlock() == Blocks.dispenser || block.getBlock() == Blocks.dropper
                    || block.getBlock() == Blocks.hopper)
                return true;

            return false;
        }

    }

}
