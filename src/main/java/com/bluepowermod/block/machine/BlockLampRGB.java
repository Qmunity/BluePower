package com.bluepowermod.block.machine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.tile.tier1.TileLamp;
import com.bluepowermod.util.Refs;

public class BlockLampRGB extends BlockLamp {

    public BlockLampRGB(boolean isInverted) {

        super(isInverted, MinecraftColor.NONE);

        setBlockName(Refs.LAMP_NAME + ".rgb" + (isInverted ? ".inverted" : ""));
    }

    @Override
    public int getColor(IBlockAccess w, int x, int y, int z) {

        RGBLampBundledDevice d = RGBLampBundledDevice.getDeviceAt(w, x, y, z);

        if (d == null)
            return 0;

        return d.getColor();
    }

    @Override
    public int getColor() {

        return Color.getHSBColor((System.currentTimeMillis() % 10000) / 10000F, 1, 1).getRGB();
    }

    public static class RGBLampBundledDevice implements IBundledDevice {

        private static List<RGBLampBundledDevice> list = new ArrayList<RGBLampBundledDevice>();

        public static RGBLampBundledDevice getDeviceAt(IBlockAccess world, int x, int y, int z) {

            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile == null || !(tile instanceof TileLamp))
                return null;

            for (RGBLampBundledDevice c : list)
                if (c.tile.equals(tile))
                    return c;

            RGBLampBundledDevice c = new RGBLampBundledDevice((TileLamp) tile);
            list.add(c);
            return c;
        }

        private byte[] power = new byte[16];
        private TileLamp tile;

        private IBundledDevice[] devices = new IBundledDevice[6];

        public RGBLampBundledDevice(TileLamp tile) {

            this.tile = tile;
        }

        @Override
        public World getWorld() {

            return tile.getWorldObj();
        }

        @Override
        public int getX() {

            return tile.xCoord;
        }

        @Override
        public int getY() {

            return tile.yCoord;
        }

        @Override
        public int getZ() {

            return tile.zCoord;
        }

        @Override
        public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

            return side != ForgeDirection.UNKNOWN;
        }

        @Override
        public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

            return false;
        }

        @Override
        public void onConnect(ForgeDirection side, IBundledDevice device) {

            devices[side.ordinal()] = device;
        }

        @Override
        public void onDisconnect(ForgeDirection side) {

            devices[side.ordinal()] = null;
        }

        @Override
        public IBundledDevice getBundledDeviceOnSide(ForgeDirection side) {

            return devices[side.ordinal()];
        }

        @Override
        public byte[] getBundledOutput(ForgeDirection side) {

            return new byte[16];
        }

        @Override
        public void setBundledPower(ForgeDirection side, byte[] power) {

            this.power = power;
        }

        @Override
        public byte[] getBundledPower(ForgeDirection side) {

            return power;
        }

        @Override
        public void onBundledUpdate() {

            tile.onUpdate();
        }

        @Override
        public MinecraftColor getBundledColor() {

            return MinecraftColor.NONE;
        }

        @Override
        public boolean isBundled() {

            return true;
        }

        @Override
        public boolean isNormalBlock() {

            return true;
        }

        public int getColor() {

            int r = MathHelper.map(power[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
            int g = MathHelper.map(power[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
            int b = MathHelper.map(power[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

            return (r << 16) + (g << 8) + b;
        }

    }

}
