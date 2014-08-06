package com.bluepowermod.helper;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageDebugBlock;

/**
 * Class aimed for debugging purposes only
 * @author MineMaarten
 */
public class Debugger {
    
    private static Random rand = new Random();
    
    public static void indicateBlock(TileEntity te) {
    
        indicateBlock(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
    }
    
    public static void indicateBlock(BPPart part) {
    
        indicateBlock(part.getWorld(), part.getX(), part.getY(), part.getZ());
    }
    
    public static void indicateBlock(World world, int x, int y, int z) {
    
        if (world != null) {
            if (world.isRemote) {
                for (int i = 0; i < 5; i++) {
                    double dx = x + 0.5;
                    double dy = y + 0.5;
                    double dz = z + 0.5;
                    world.spawnParticle("flame", dx, dy, dz, 0, 0, 0);
                }
            } else {
                NetworkHandler.sendToAllAround(new MessageDebugBlock(x, y, z), world);
            }
        }
    }
}
