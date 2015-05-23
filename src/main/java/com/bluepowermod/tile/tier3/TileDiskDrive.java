/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import uk.co.qmunity.lib.tile.TileBase;

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
