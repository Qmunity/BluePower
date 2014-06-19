package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.tileentities.TileMachineBase;

/**
 * 
 * @author TheFjong
 *
 */
public class TileEngine extends TileMachineBase{

	public boolean isActive = false;
	
	public TileEngine(){
		
		
	}
	
	@Override
	public void updateEntity() {
		isActive = worldObj.provider.isDaytime();
	}
	
	
	public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }
}
