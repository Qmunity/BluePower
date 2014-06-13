package net.quetzi.bluepower.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.references.Dependencies;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

public class RedstoneHelper {
    
    public static final int getInput(World w, int x, int y, int z, ForgeDirection side) {
    
        return getInput(w, x, y, z, side, null);
    }
    
    public static final int getInput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face) {
    
        int power = 0;
        
        Block b = w.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        if (b != null) {
            boolean shouldCheck = true;
            
            if (b instanceof BlockRedstoneWire && (face != ForgeDirection.DOWN && face != null)) shouldCheck = false;
            
            if (shouldCheck) {
                power = Math.max(power,
                        b.isProvidingStrongPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                power = Math.max(power,
                        b.isProvidingWeakPower(w, x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                power = Math.max(power,
                        w.getIndirectPowerLevelTo(x + side.offsetX, y + side.offsetY, z + side.offsetZ, ForgeDirectionUtils.getSide(side)));
                power = Math
                        .max(power, b instanceof BlockRedstoneWire ? w.getBlockMetadata(x + side.offsetX, y + side.offsetY, z + side.offsetZ) : 0);
            }
        }
        
        power = Math.max(power, ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getInput(w, x, y, z, side, face));
        
        return power;
    }
    
    public static final int setOutput(World w, int x, int y, int z, ForgeDirection side, ForgeDirection face, int p) {
    
        int power = 0;
        
        Block b = w.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        if (b != null) {
            boolean shouldOutput = true;
            
            if (b instanceof BlockRedstoneWire && (face != ForgeDirection.DOWN && face != null)) shouldOutput = false;
            
            if(shouldOutput)
                power = p;
        }
        
        return power;
    }
    
}
