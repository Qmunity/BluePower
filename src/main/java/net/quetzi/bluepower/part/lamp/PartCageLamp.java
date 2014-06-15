package net.quetzi.bluepower.part.lamp;

import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.RenderHelper;

public class PartCageLamp extends PartLamp{

	public PartCageLamp(String colorName, Integer colorVal) {
		
		super(colorName, colorVal);
	}
	
	
	@Override
    public void renderBase(int pass){
		
		RenderHelper.drawTesselatedColoredCube(new Vector3Cube(0.25, 0.0, 0.25, 0.75, 0.375, 0.75));
    }
    
	@Override
    public void renderLamp(int pass){
    	
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
