package net.quetzi.bluepower.part.lamp;

import net.minecraft.client.renderer.Tessellator;
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
		
		
		Tessellator t = Tessellator.instance;
		Vector3Cube vector = new Vector3Cube(pixel * 3, 0.0, pixel * 3, 1.0 - (pixel*3), pixel * 2, 1.0 - pixel * 3);
		IIcon topIcon = IconSupplier.cagedLampFootTop;
		IIcon sideIcon = IconSupplier.cagedLampFootSide;
		
		double minU = topIcon.getInterpolatedU(vector.getMinX() * 16);
        double maxU = topIcon.getInterpolatedU(vector.getMaxX() * 16);
        double minV = topIcon.getInterpolatedV(vector.getMinZ() * 16);
        double maxV = topIcon.getInterpolatedV(vector.getMaxZ() * 16);
    	
        //Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        minU = sideIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = sideIcon.getInterpolatedV(vector.getMinY() * 16);
        maxV = sideIcon.getInterpolatedV(vector.getMaxY() * 16);
        //Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        //Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        //Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        
        //Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, maxV);
		
        
        //And now, the cage itself!
        //No. Not Nicholas Cage. The lamp-cage!
        vector = new Vector3Cube(pixel * 4, pixel * 2, pixel * 4, 1.0 - (pixel*4), 1.0 - (pixel * 4), 1.0 - pixel * 4);
        topIcon = IconSupplier.cagedLampCageTop;
		sideIcon = IconSupplier.cagedLampCageSide;
		
		minU = topIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = topIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = topIcon.getInterpolatedV(vector.getMinZ() * 16);
        maxV = topIcon.getInterpolatedV(vector.getMaxZ() * 16);
    	
        //Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        minU = sideIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = sideIcon.getInterpolatedV(vector.getMinY() * 16);
        maxV = sideIcon.getInterpolatedV(vector.getMaxY() * 16);
        //Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);

        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        //Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        //Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        
        //Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, maxV);
        
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
    }
    
	@Override
    public void renderLamp(int pass, int r, int g, int b){
		
		Tessellator t = Tessellator.instance;
		IIcon iconToUseTop;
		IIcon iconToUseSide;
		if(power == 0){
			iconToUseSide = IconSupplier.cagedLampLampInactive;
			iconToUseTop = IconSupplier.cagedLampLampInactiveTop;
		}else{
			iconToUseSide = IconSupplier.cagedLampLampActive;
			iconToUseTop = IconSupplier.cagedLampLampActiveTop;
			
			t.setColorRGBA(r, g, b, 20);
			//RenderHelper.drawTesselatedCube(new Vector3Cube(pixel * 4.5, pixel * 2, pixel * 4.5, 1.0 - (pixel*4.5), 1.0 - (pixel * 4.5), 1.0 - pixel * 4.5));
			t.setColorRGBA(r, g, b, 255);
		}
		
		Vector3Cube vector = new Vector3Cube(pixel * 5, pixel * 2, pixel * 5, 1.0 - (pixel*5), 1.0 - (pixel * 5), 1.0 - pixel * 5);
		
		double minU = iconToUseTop.getInterpolatedU(vector.getMinX() * 16);
        double maxU = iconToUseTop.getInterpolatedU(vector.getMaxX() * 16);
        double minV = iconToUseTop.getInterpolatedV(vector.getMinZ() * 16);
        double maxV = iconToUseTop.getInterpolatedV(vector.getMaxZ() * 16);
    	
        //Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        

        minU = iconToUseSide.getInterpolatedU(vector.getMinX() * 16);
        maxU = iconToUseSide.getInterpolatedU(vector.getMaxX() * 16);
        minV = iconToUseSide.getInterpolatedV(vector.getMinZ() * 16);
        maxV = iconToUseSide.getInterpolatedV(vector.getMaxZ() * 16);
        
        //Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        //Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        //Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        
        //Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, maxV);
		
		
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
