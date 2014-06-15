package net.quetzi.bluepower.part.lamp;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.helper.RedstoneHelper;
import net.quetzi.bluepower.init.CustomTabs;

public class PartLamp extends BPPartFace {
    
    protected String colorName;
    private int      colorVal;
    
    protected int      power = 0;
    
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
    
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 3, 0.0, pixel * 3, 1.0 - (pixel*3), pixel * 2, 1.0 - pixel * 3));
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 4, pixel * 2, pixel * 4, 1.0 - (pixel*4), 1.0 - (pixel * 4), 1.0 - pixel * 4));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
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
    
    public void renderBase(int pass) {
    
    }
    
    public void renderLamp(int pass, int r, int g, int b) {
    
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
