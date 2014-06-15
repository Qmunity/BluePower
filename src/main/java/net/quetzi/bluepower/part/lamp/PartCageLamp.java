package net.quetzi.bluepower.part.lamp;

import net.minecraft.util.IIcon;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.IconSupplier;
import net.quetzi.bluepower.client.renderers.RenderHelper;

public class PartCageLamp extends PartLamp{

	public PartCageLamp(String colorName, Integer colorVal) {
		
		super(colorName, colorVal);
	}
	
	
	@Override
    public void renderBase(int pass){
		
		RenderHelper.drawTesselatedColoredCube(new Vector3Cube(pixel * 3, 0.0, pixel * 3, 1.0 - (pixel*3), pixel * 2, 1.0 - pixel * 3));
		//RenderHelper.drawTesselatedColoredCube(new Vector3Cube(pixel * 4, pixel * 2, pixel * 4, 1.0 - (pixel*4), 1.0 - (pixel * 4), 1.0 - pixel * 4));
    }
    
	@Override
    public void renderLamp(int pass){
		
		IIcon iconToUse;
		if(power == 0){
			iconToUse = IconSupplier.cagedLampLampInactive;
		}else{
			iconToUse = IconSupplier.cagedLampLampActive;
		}
		RenderHelper.drawTesselatedTexturedCube(new Vector3Cube(pixel * 5, pixel * 2, pixel * 5, 1.0 - (pixel*5), 1.0 - (pixel * 5), 1.0 - pixel * 5), iconToUse);
    }
	
	@Override
    public String getType() {
    
        return "cagelamp" + colorName;
    }
	
	@Override
    public String getUnlocalizedName() {
    
        return "cagelamp." + colorName;
    }

}
