/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */
package net.quetzi.bluepower.api.vec;

import codechicken.lib.vec.BlockCoord;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.references.Dependencies;

import java.util.StringTokenizer;

public class Vector3 {
    
    protected double x, y, z;
    protected World  w = null;
    
    public Vector3(double x, double y, double z) {
    
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(double x, double y, double z, World w) {
    
        this(x, y, z);
        this.w = w;
    }
    
    public Vector3(TileEntity te) {
    
        this(te.xCoord, te.yCoord, te.zCoord, te.getWorldObj());
    }
    
    public Vector3(Vec3 vec) {
    
        this(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vector3(Vec3 vec, World w) {
    
        this(vec.xCoord, vec.yCoord, vec.zCoord);
        this.w = w;
    }
    
    public static Vector3 fromString(String s) {
    
        if (s.startsWith("Vector3{") && s.endsWith("}")) {
            World w = null;
            double x = 0, y = 0, z = 0;
            String s2 = s.substring(s.indexOf("{") + 1, s.lastIndexOf("}"));
            StringTokenizer st = new StringTokenizer(s2, ";");
            while (st.hasMoreTokens()) {
                String t = st.nextToken();
                
                if (t.toLowerCase().startsWith("w")) {
                    int world = Integer.parseInt(t.split("=")[1]);
                    if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                        for (World wo : MinecraftServer.getServer().worldServers) {
                            if (wo.provider.dimensionId == world) {
                                w = wo;
                                break;
                            }
                        }
                    } else {
                        w = getClientWorld(world);
                    }
                }
                
                if (t.toLowerCase().startsWith("x")) x = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("y")) y = Double.parseDouble(t.split("=")[1]);
                if (t.toLowerCase().startsWith("z")) z = Double.parseDouble(t.split("=")[1]);
            }
            
            if (w != null) {
                return new Vector3(x, y, z, w);
            } else {
                return new Vector3(x, y, z);
            }
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    private static World getClientWorld(int world) {
    
        if (Minecraft.getMinecraft().theWorld.provider.dimensionId != world) return null;
        return Minecraft.getMinecraft().theWorld;
    }
    
    public boolean hasWorld() {
    
        return w != null;
    }
    
    public Vector3 add(double x, double y, double z) {
    
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3 add(ForgeDirection dir) {
    
        return add(dir.offsetX, dir.offsetY, dir.offsetZ);
    }
    
    public Vector3 add(Vector3 vec) {
    
        return add(vec.x, vec.y, vec.z);
    }
    
    public Vector3 subtract(double x, double y, double z) {
    
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3 subtract(ForgeDirection dir) {
    
        return subtract(dir.offsetX, dir.offsetY, dir.offsetZ);
    }
    
    public Vector3 subtract(Vector3 vec) {
    
        return subtract(vec.x, vec.y, vec.z);
    }
    
    public Vector3 multiply(double x, double y, double z) {
    
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vector3 multiply(double multiplier) {
    
        return multiply(multiplier, multiplier, multiplier);
    }
    
    public Vector3 multiply(ForgeDirection direction) {
    
        return multiply(direction.offsetX, direction.offsetY, direction.offsetZ);
    }
    
    public Vector3 divide(double x, double y, double z) {
    
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    public Vector3 divide(double multiplier) {
    
        return divide(multiplier, multiplier, multiplier);
    }
    
    public Vector3 divide(ForgeDirection direction) {
    
        return divide(direction.offsetX, direction.offsetY, direction.offsetZ);
    }
    
    public Vector3 getRelative(double x, double y, double z) {
    
        return clone().add(x, y, z);
    }
    
    public Vector3 getRelative(ForgeDirection dir) {
    
        return getRelative(dir.offsetX, dir.offsetY, dir.offsetZ);
    }
    
    public ForgeDirection getDirectionTo(Vector3 vec) {
    
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            if (getBlockX() + d.offsetX == vec.getBlockX() && getBlockY() + d.offsetY == vec.getBlockY()
                    && getBlockZ() + d.offsetZ == vec.getBlockZ()) return d;
        return null;
    }
    
    public boolean hasTileEntity() {
    
        if (hasWorld()) { return w.getTileEntity((int) x, (int) y, (int) z) != null; }
        return false;
    }
    
    public TileEntity getTileEntity() {
    
        if (hasTileEntity()) { return w.getTileEntity((int) x, (int) y, (int) z); }
        return null;
    }
    
    public boolean isBlock(Block b) {
    
        return isBlock(b, false);
    }
    
    public boolean isBlock(Block b, boolean checkAir) {
    
        if (hasWorld()) {
            Block bl = w.getBlock((int) x, (int) y, (int) z);
            
            if (b == null && bl == Blocks.air) return true;
            if (b == null && checkAir && bl.getMaterial() == Material.air) return true;
            if (b == null && checkAir && bl.isAir(w, (int) x, (int) y, (int) z)) return true;
            
            return bl.getClass().isInstance(b);
        }
        return false;
    }
    
    public int getBlockMeta() {
    
        if (hasWorld()) { return w.getBlockMetadata((int) x, (int) y, (int) z); }
        return -1;
    }
    
    public Block getBlock() {
    
        return getBlock(true);
    }
    
    public Block getBlock(boolean airIsNull) {
    
        if (hasWorld()) {
            if (airIsNull && isBlock(null, true)) return null;
            return w.getBlock((int) x, (int) y, (int) z);
            
        }
        return null;
    }
    
    public World getWorld() {
    
        return w;
    }
    
    public Vector3 setWorld(World world) {
    
        this.w = world;
        
        return this;
    }
    
    public double getX() {
    
        return x;
    }
    
    public double getY() {
    
        return y;
    }
    
    public double getZ() {
    
        return z;
    }
    
    public int getBlockX() {
    
        return (int) Math.floor(x);
    }
    
    public int getBlockY() {
    
        return (int) Math.floor(y);
    }
    
    public int getBlockZ() {
    
        return (int) Math.floor(z);
    }
    
    public double distanceTo(Vector3 vec) {
    
        return distanceTo(vec.x, vec.y, vec.z);
    }
    
    public double distanceTo(double x, double y, double z) {
    
        double dx = x - this.x;
        double dy = y - this.y;
        double dz = z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    @Override
    public boolean equals(Object obj) {
    
        if (obj instanceof Vector3) {
            Vector3 vec = (Vector3) obj;
            return vec.w == w && vec.x == x && vec.y == y && vec.z == z;
        }
        return false;
    }
    
    public Vector3 clone() {
    
        return new Vector3(x, y, z, w);
    }
    
    @Override
    public String toString() {
    
        String s = "Vector3{";
        if (hasWorld()) s += "w=" + w.provider.dimensionId + ";";
        s += "x=" + x + ";y=" + y + ";z=" + z + "}";
        return s;
    }
    
    public Vec3 toVec3() {
    
        return Vec3.createVectorHelper(x, y, z);
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    public BlockCoord toBlockCoord() {
    
        return new BlockCoord((int) x, (int) y, (int) z);
    }
    
    public Vector3 toAbsoulte() {
    
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }
    
}
