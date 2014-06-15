package net.quetzi.bluepower.part.lamp;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.helper.RedstoneHelper;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class PartLamp extends BPPartFace {
    
    protected String colorName;
    private int      colorVal;
    
    private int      power = 0;
    
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
    
    @Override
    public String getUnlocalizedName() {
    
        return "lamp." + colorName;
    }
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(AxisAlignedBB.getBoundingBox(1D / 4D, 0, 1D / 4D, 3D / 4D, 3D / 8D, 3D / 4D));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    @Override
    public boolean renderStatic(Vector3 loc, int pass) {
    
        super.renderStatic(loc, pass);
        Tessellator t = Tessellator.instance;
        t.setColorOpaque_F(1, 1, 1);
        t.addTranslation((float) loc.getX(), (float) loc.getY(), (float) loc.getZ());
        // Render base here
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/lamps/side.png"));
        
        // Render base
        renderBase(pass);
        
        // Color multiplier
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
        int r = (colorVal & redMask) >> 16;
        int g = (colorVal & greenMask) >> 8;
        int b = (colorVal & blueMask);
        // GL11.glColor4d(r / 256D, g / 256D, b / 256D, 1);
        
        // Render lamp itself here
        // GL11.glBegin(GL11.GL_QUADS);
        renderLamp(pass);
        // GL11.glEnd();
        
        // Reset color
        // GL11.glColor4d(1, 1, 1, 1);
        
        // Re-bind blocks texture
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        t.addTranslation((float) -loc.getX(), (float) -loc.getY(), (float) -loc.getZ());
        return true;
    }
    
    public void renderBase(int pass) {
    
    }
    
    public void renderLamp(int pass) {
    
    }
    
    @Override
    public int getLightValue() {
    
        return power;
    }
    
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
    
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerLighting;
    }
    
}
