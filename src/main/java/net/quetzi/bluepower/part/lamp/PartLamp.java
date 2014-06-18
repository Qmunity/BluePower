package net.quetzi.bluepower.part.lamp;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.IconSupplier;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.helper.RedstoneHelper;
import net.quetzi.bluepower.init.CustomTabs;

/**
 * Base class for the lamps that are multiparts.
 * @author Koen Beckers (K4Unl)
 *
 */
public class PartLamp extends BPPartFace {
    
    protected String colorName;
    private int      colorVal;
    
    protected int      power = 0;
    
    /**
     * @author amadornes
     * @param colorName
     * @param colorVal
     */
    public PartLamp(String colorName, Integer colorVal) {
    
        this.colorName = colorName;
        this.colorVal = colorVal.intValue();
        
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
        
        for (RedstoneConnection c : connections) {
            c.enable();
            c.setInput();
        }
    }
    
    @Override
    public String getType() {
    
        return "lamp" + colorName;
    }
    
    /**
     * @author amadornes
     */
    @Override
    public String getUnlocalizedName() {
    
        return "lamp." + colorName;
    }
    
    /**
     * @author amadornes
     */
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    /**
     * @author Koen Beckers (K4Unl)
     */
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 2, 0.0, pixel * 2, 1.0 - (pixel*2), pixel * 2, 1.0 - pixel * 2));
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 3, pixel * 2, pixel * 3, 1.0 - (pixel*3), pixel * 8, 1.0 - pixel * 3));
    }
    
    /**
     * @author amadornes
     */
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    /**
     * @author Koen Beckers (K4Unl)
     */
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glRotated(180, 1, 0, 0);
        GL11.glTranslated(-0.5, -0.5, -0.5);
        Tessellator t = Tessellator.instance;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        t.startDrawingQuads();
        renderStatic(new Vector3(0, 0, 0), 0);
        t.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        GL11.glPopMatrix();
    }
    
    /**
     * @author Koen Beckers (K4Unl)
     */
    @Override
    public boolean renderStatic(Vector3 loc, int pass) {
    
        rotateAndTranslateDynamic(loc, pass, 0);
        Tessellator t = Tessellator.instance;
        t.setColorOpaque_F(1, 1, 1);
        translateStatic(loc, pass);
        // Render base here
        //Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/lamps/side.png"));
        
        // Render base
        renderBase(pass);
        
        // Color multiplier
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
        int r = (colorVal & redMask) >> 16;
        int g = (colorVal & greenMask) >> 8;
        int b = (colorVal & blueMask);
        
        t.setColorOpaque(r, g, b);
        // Render lamp itself here
        renderLamp(pass, r, g, b);
        
        // Reset color
        
        // Re-bind blocks texture
        //Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        undoTranslateStatic(loc, pass);
        return true;
    }
    
    /**
     * Code to render the base portion of the lamp. Will not be colored
     * @author Koen Beckers (K4Unl)
     * @param pass The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     */
    public void renderBase(int pass) {
    	
    	Tessellator t = Tessellator.instance;
		Vector3Cube vector = new Vector3Cube(pixel * 2, 0.0, pixel * 2, 1.0 - (pixel*2), pixel * 2, 1.0 - pixel * 2);
		IIcon topIcon = IconSupplier.fixtureFootTop;
		//IIcon sideIcon = IconSupplier.fixtureFootSide;
		
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
        
        //FIXME: Icons for the side aren't rendered..
        
        /*minU = sideIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = sideIcon.getInterpolatedV((vector.getMinY() + (3*pixel) )* 16);
        maxV = sideIcon.getInterpolatedV((vector.getMaxY() + (3*pixel) )* 16);*/
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
    
    /**
     * Code to render the actual lamp portion of the lamp. Will be colored
     * 
     * @author Koen Beckers (K4Unl)
     * @param pass The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     * @param r The ammount of red in the lamp
     * @param g The ammount of green in the lamp
     * @param b The ammount of blue in the lamp
     */
    public void renderLamp(int pass, int r, int g, int b) {
    	Tessellator t = Tessellator.instance;
		IIcon iconToUseTop;
		IIcon iconToUseSide;
		if(power == 0){
			iconToUseSide = IconSupplier.fixtureLampSideOff;
			iconToUseTop = IconSupplier.fixtureLampTopOff;
		}else{
			iconToUseSide = IconSupplier.fixtureLampSideOn;
			iconToUseTop = IconSupplier.fixtureLampTopOn;
			
			t.setColorRGBA(r, g, b, 20);
			RenderHelper.drawTesselatedCube(new Vector3Cube(pixel * 4.5, pixel * 2, pixel * 4.5, 1.0 - (pixel*4.5), 1.0 - (pixel * 4.5), 1.0 - pixel * 4.5));
			t.setColorRGBA(r, g, b, 255);
		}
		
		Vector3Cube vector = new Vector3Cube(pixel * 3, pixel * 2, pixel * 3, 1.0 - (pixel*3), pixel * 8, 1.0 - pixel * 3);
		
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
        

        //I think there might be something wrong with the textures here..
        minU = iconToUseSide.getInterpolatedU(vector.getMinZ() * 16);
        maxU = iconToUseSide.getInterpolatedU(vector.getMaxZ() * 16);
        minV = iconToUseSide.getInterpolatedV((vector.getMinY() + (3*pixel) )* 16);
        maxV = iconToUseSide.getInterpolatedV((vector.getMaxY() + (3*pixel) )* 16);
        
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
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
    }
    
    @Override
    public int getLightValue() {
    
        return power;
    }
    
    
    /**
     * @author amadornes
     */
    @Override
    public void update() {
    
        super.update();
        
        int old = power;
        
        power = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            power = Math.max(power, RedstoneHelper.getInput(world, x, y, z, d));
        
        if (old != power) {
            notifyUpdate();
            world.updateLightByType(EnumSkyBlock.Block, x, y, z);
        }
    }
    
    /**
     * @author amadornes
     */
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerLighting;
    }

    
}
