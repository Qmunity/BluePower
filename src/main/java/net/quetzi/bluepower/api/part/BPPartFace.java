/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.api.part;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

import org.lwjgl.opengl.GL11;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {
    
    private int                  face        = 0;
    private int                  rotation    = 0;
    
    protected RedstoneConnection[] connections = new RedstoneConnection[4];
    
    public BPPartFace() {
        super();
    
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
    }
    
    @Override
    public int getFace() {
    
        return face;
    }
    
    public void setFace(int face) {
    
        this.face = face;
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
        
        return true;
    }
    
    @Override
    public boolean canStay() {
    
        return true;
    }
    
    @Override
    public final boolean canConnect(ForgeDirection side) {
        
        return getConnection(side).isEnabled();
    }
    
    @Override
    public final int getStrongOutput(ForgeDirection side) {
    
        return getConnection(side).isOutput() ? getConnection(side).getPower() : 0;
    }
    
    @Override
    public final int getWeakOutput(ForgeDirection side) {
    
        return getConnection(side).isOutput() ? getConnection(side).getPower() : 0;
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
        
        for (int i = 0; i < rotation; i++)
            GL11.glRotated(90, 0, 1, 0);
        
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
        }
        GL11.glTranslated(-0.5, -0.5, -0.5);
    }
    
    public RedstoneConnection getConnection(int id) {
    
        try {
            return connections[id];
        } catch (Exception ex) {
        }
        return null;
    }
    
    public RedstoneConnection getConnection(String id) {
    
        for (RedstoneConnection c : connections)
            if (c.getID().equalsIgnoreCase(id)) return c;
        
        return null;
    }
    
    public RedstoneConnection getConnection(ForgeDirection dir) {
    
        int id = -1;
        
        ForgeDirection face = ForgeDirection.getOrientation(getFace());
        
        if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
            id = ForgeDirectionUtils.getSide(dir) - 2;
        } else if (face == ForgeDirection.NORTH || face == ForgeDirection.SOUTH) {
            switch (dir) {
                case DOWN:
                    id = 0;
                    break;
                case UP:
                    id = 1;
                    break;
                case EAST:
                    id = 2;
                    break;
                case WEST:
                    id = 3;
                    break;
                default:
                    break;
            }
        } else if (face == ForgeDirection.EAST || face == ForgeDirection.WEST) {
            switch (dir) {
                case DOWN:
                    id = 0;
                    break;
                case UP:
                    id = 1;
                    break;
                case NORTH:
                    id = 2;
                    break;
                case SOUTH:
                    id = 3;
                    break;
                default:
                    break;
            }
        }
        
        return getConnection(id);
    }
    
    @Override
    public void update() {
    
        super.update();
        
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            RedstoneConnection rc = getConnection(d);
            if (rc != null && rc.isInput()) {
                Block b = world.getBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ);
                rc.setPower(Math.max(b.isProvidingWeakPower(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, ForgeDirectionUtils.getSide(d)),
                        b.isProvidingStrongPower(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, ForgeDirectionUtils.getSide(d))));
            }
        }
    }
    
}
