package net.quetzi.bluepower.tileentities.tier3;

import net.quetzi.bluepower.tileentities.TileBase;

/**
 * @author fabricator77
 */
public class TileDiskDrive extends TileBase implements IRedBusWindow {
	/** redbus memory block
	 * addr size function
	 * 0x00 128 Data buffer
	 * 0x80 2 Sector number
	 * 0x82 1 Command
	 * 
	 * command modes
	 * command function
	 * 0x01    Read name
	 * 0x02    Write name
	 * 0x03    Read serial
	 * 0x04    Read sector
	 * 0x05    Write sector
	*/  
}
