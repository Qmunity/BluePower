package net.quetzi.bluepower.util;

import net.minecraftforge.common.util.ForgeDirection;


public class ForgeDirectionUtils {
    
    public static int getSide(ForgeDirection dir){
        for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++){
            if(ForgeDirection.VALID_DIRECTIONS[i] == dir)
                return i;
        }
        return -1;
    }
    
}
