package net.quetzi.bluepower.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

public class RedstoneHelper {
    
    public static final int getInput(World w, int x, int y, int z, ForgeDirection side) {
    
        int power = 0;
        
        Block b = w.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        if (b != null) {
            power = Math.max(power,
                    b.isProvidingStrongPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
            power = Math.max(power,
                    b.isProvidingWeakPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
            power = Math.max(power,
                    w.getIndirectPowerLevelTo(x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
            power = Math.max(power, b instanceof BlockRedstoneWire ? w.getBlockMetadata(x + side.offsetX, y + side.offsetY, z + side.offsetZ) : 0);
        }
        
        return power;
    }
    
}
