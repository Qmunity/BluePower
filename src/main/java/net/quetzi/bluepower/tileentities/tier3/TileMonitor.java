package net.quetzi.bluepower.tileentities.tier3;

import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileMonitor extends TileBase implements IRedBusWindow  {
	public byte framebufferRow = 0;
	public byte cursorX = 0;
	public byte cursorY = 0;
	
	public byte blit_command = 0;
	public byte blit_sourceX = 0;
	public byte blit_sourceY = 0;
	public byte blit_destX = 0;
	public byte blit_destY = 0;
	public byte blit_rectWidth = 0;
	public byte blit_rectHeight = 0;
	
	public byte[] framebuffer = new byte[80];
	public byte[] keybuffer = new byte[16]; //TODO: check length against specs
	
	//Also used by GUI
	public byte[] screenMemory = new byte[4000];
	public static boolean mode80x40 = false;
	public static float[] screenColor = new float[]{1.0F, 0.8F, 0.0F};
	
	public TileMonitor() {
		blankMemory();
		testMemory();
	}
	
	private void blankMemory ()
	{
		for (int i=0; i<4000; i++) {
			screenMemory[i] = (byte)32;
		}
	}
	
	private void testMemory ()
	{
		if (mode80x40) {
			for (int i=0; i<256; i++) {
				screenMemory[i] = (byte)i;
			}
		}
		else {
			for (int i=0; i<40; i++) {
				screenMemory[i*2] = (byte)i;
				screenMemory[(i+80)*2] = (byte)(i+40);
				//screenMemory[(i+160)*2] = (byte)(i+80);
				//screenMemory[(i+240)*2] = (byte)(i+120);
				//screenMemory[(i+320)*2] = (byte)(i+160);
			}
		}
	}
	
	public byte[] getRedbusMemory () {
		return new byte[95];
		// first 16 addresses for control
		// remaining 80 char FrameBuffer data window
	}
	
	/** redbus memory block
	 *  addr size function
	 *  0x00  1   Framebuffer access row
	 *  0x01  1   Cursor X position
	 *  0x02  1   Cursor Y position
	 *  0x03  1   Cursor style
	 *  0x04  1   Keyboard buffer tail pointer
	 *  0x05  1   Keyboard buffer head pointer
	 *  0x06  1   Keyboard buffer data window
	 *  0x07  1   Blitter command
	 *  0x08  1   Blitter source rectangle X or fill character
	 *  0x09  1   Blitter source rectangle Y
	 *  0x0A  1   Blitter destination rectangle X
	 *  0x0B  1   Blitter destination rectangle Y
	 *  0x0C  1   Blitter rectangle width
	 *  0x0D  1   Blitter rectangle height
	 *  0x10  80  Framebuffer data window
	 */
}
