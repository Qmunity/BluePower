/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.api.part;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.helper.RedstoneHelper;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

import org.lwjgl.opengl.GL11;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {
    
    private int                    face        = 0;
    private int                    rotation    = 0;
    
    protected RedstoneConnection[] connections = new RedstoneConnection[4];
    
    @Override
    public int getFace() {
    
        return face;
    }
    
    public void setFace(int face) {
    
        this.face = face;
        notifyUpdate();
    }
    
    @Override
    public int getRotation() {
    
        return rotation;
    }
    
    @Override
    public void setRotation(int rotation) {
    
        this.rotation = rotation % 4;
        notifyUpdate();
    }
    
    @Override
    public boolean canPlacePart(ItemStack is, EntityPlayer player, Vector3 block, MovingObjectPosition mop) {
    
        this.world = block.getWorld();
        this.x = block.getBlockX();
        this.y = block.getBlockY();
        this.z = block.getBlockZ();
        
        ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit).getOpposite();
        if (dir == ForgeDirection.DOWN) {
            dir = ForgeDirection.UP;
        } else {
            if (dir == ForgeDirection.UP) dir = ForgeDirection.DOWN;
        }
        
        setFace(ForgeDirectionUtils.getSide(dir));
        
        int rotation = 0;
        
        {
            double s1 = 0;
            double s2 = 0;
            
            switch(dir){
                case WEST:
                case EAST:
                    s1 = mop.hitVec.yCoord - player.posY;
                    s2 = mop.hitVec.zCoord - player.posZ;
                    break;
                case DOWN:
                case UP:
                    s1 = mop.hitVec.xCoord - player.posX;
                    s2 = mop.hitVec.zCoord - player.posZ;
                    break;
                case NORTH:
                case SOUTH:
                    s1 = mop.hitVec.xCoord - player.posX;
                    s2 = mop.hitVec.yCoord - player.posY;
                    break;
                default:
                    break;
            }
            
            if(Math.abs(s1) >= Math.abs(s2)){
                if(s1 >= 0){
                    rotation = 3;
                }else{
                    rotation = 1;
                }
            }else{
                if(s2 >= 0){
                    rotation = 0;
                }else{
                    rotation = 2;
                }
            }
        }
        
        setRotation(rotation);
        
        return world.isSideSolid(x, y, z, dir);
    }
    
    @Override
    public boolean canStay() {
    
        ForgeDirection d = ForgeDirection.getOrientation(getFace());
        
        if(d == ForgeDirection.UP || d == ForgeDirection.DOWN)
            d = d.getOpposite();
        
        return world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite());
    }
    
    @Override
    public final boolean canConnect(ForgeDirection side) {
    
        RedstoneConnection con = getConnection(FaceDirection.getDirection(ForgeDirection.getOrientation(getFace()), side, rotation));
        if (con == null) return false;
        
        return con.isEnabled();
    }
    
    @Override
    public final int getStrongOutput(ForgeDirection side) {
    
        return getWeakOutput(side);
    }
    
    @Override
    public final int getWeakOutput(ForgeDirection side) {
    
        RedstoneConnection con = getConnection(side);
        if (con == null) return 0;
        
        return con.isOutput() ? con.getPower() : 0;
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
        
        GL11.glTranslated(0.5, 0.5, 0.5);
        {
            
            switch (getFace()) {
                case 0:
                    GL11.glRotated(180, 1, 0, 0);
                    break;
                case 1:
                    break;
                case 2:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case 3:
                    GL11.glRotated(90, -1, 0, 0);
                    break;
                case 4:
                    GL11.glRotated(90, 0, 0, -1);
                    break;
                case 5:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
            }
            GL11.glRotated(90 * rotation, 0, 1, 0);
        }
        GL11.glTranslated(-0.5, -0.5, -0.5);
    }
    
    public RedstoneConnection getConnection(FaceDirection dir) {
    
        if (dir == null) return null;
        
        try {
            return connections[dir.getDirection()];
        } catch (Exception ex) {
        }
        return null;
    }
    
    public RedstoneConnection getConnection(ForgeDirection dir) {
    
        return getConnection(FaceDirection.getDirection(ForgeDirection.getOrientation(getFace()), dir, rotation));
    }
    
    @Override
    public void update() {
    
        super.update();
        
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            RedstoneConnection rc = getConnection(d);
            if (rc != null && rc.isInput()) {
                rc.setPower(RedstoneHelper.getInput(world, x, y, z, d));
            }
        }
    }
    
}
