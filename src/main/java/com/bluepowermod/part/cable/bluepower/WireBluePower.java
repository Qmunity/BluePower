package com.bluepowermod.part.cable.bluepower;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.part.ICableSize;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.part.cable.CableWall;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K4Unl);
 */

public class WireBluePower extends CableWall implements ICableSize, IBluePowered {

    protected static Vector3Cube SELECTION_BOX = new Vector3Cube(0, 0, 0, 1, 2 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX = new Vector3Cube(7 / 16D, 0 / 16D, 7 / 16D, 9 / 16D, 2 / 16D, 9 / 16D);
    private IPowerBase handler;

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(SELECTION_BOX.clone().toAABB());


    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(OCCLUSION_BOX.clone().toAABB());
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        return cable instanceof WireBluePower;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        return false;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        return tile instanceof IBluePowered;
    }

    @Override
    public String getType() {

        return "bluepowerWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluepowerWire";
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerPower;
    }

    @Override
    public boolean renderStatic(Vector3 loc, int pass) {
        if(pass == 0){

            Tessellator t = Tessellator.instance;
            t.draw();

            rotateAndTranslateDynamic(loc, pass, 0);

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(-90 * (getRotation() == 0 || getRotation() == 2 ? (getRotation() + 2) % 4 : getRotation()), 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);

            double center = 0.5;
            double minX = center - ((getCableWidth() * pixel) / 2.0);
            double minZ = center - ((getCableWidth() * pixel) / 2.0);
            double maxX = center + ((getCableWidth() * pixel) / 2.0);
            double maxZ = center + ((getCableWidth() * pixel) / 2.0);
            t.startDrawingQuads();
            RenderHelper.drawTesselatedColoredCube(new Vector3Cube(minX, 0.0, minZ, maxX, getCableHeight()*pixel, maxZ));

            List<ForgeDirection> validDirs = new ArrayList<ForgeDirection>();
            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
                if(connections[dir.ordinal()] != null){
                    validDirs.add(dir);
                }
            }

            if(((getFace() == 0) && validDirs.contains(ForgeDirection.NORTH)) || ((getFace() == 1 || getFace() == 5 || getFace() == 4) && validDirs.contains(ForgeDirection.NORTH)) || (getFace() == 2 && validDirs.contains(ForgeDirection.UP)) || (getFace() == 3 && validDirs.contains(ForgeDirection.DOWN))){
                RenderHelper.drawTesselatedColoredCube(new Vector3Cube(minX, 0.0, 0.0, maxX, getCableHeight()*pixel, minZ));
            }
            if(((getFace() == 0) && validDirs.contains(ForgeDirection.SOUTH)) || ((getFace() == 1 || getFace() == 5 || getFace() == 4) && validDirs.contains(ForgeDirection.SOUTH)) || (getFace() == 3 && validDirs.contains(ForgeDirection.UP)) || (getFace() == 2 && validDirs.contains(ForgeDirection.DOWN))){
                RenderHelper.drawTesselatedColoredCube(new Vector3Cube(minX, 0.0, maxZ, maxX, getCableHeight()*pixel, 1.0));
            }
            if(((getFace() == 1 || getFace() == 0 || getFace() == 3 || getFace() == 2) && validDirs.contains(ForgeDirection.EAST)) || (getFace() == 5 && validDirs.contains(ForgeDirection.UP)) || (getFace() == 4 && validDirs.contains(ForgeDirection.DOWN))){
                RenderHelper.drawTesselatedColoredCube(new Vector3Cube(maxX, 0.0, minZ, 1.0, getCableHeight()*pixel, maxZ));
            }
            if(((getFace() == 1 || getFace() == 0 || getFace() == 3 || getFace() == 2) && validDirs.contains(ForgeDirection.WEST)) || (getFace() == 4 && validDirs.contains(ForgeDirection.UP)) || (getFace() == 5 && validDirs.contains(ForgeDirection.DOWN))){
                RenderHelper.drawTesselatedColoredCube(new Vector3Cube(0.0, 0.0, minZ, minX, getCableHeight()*pixel, maxZ));
            }

            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            t.startDrawingQuads();
            GL11.glPopMatrix();


            return true;
        }else{
            return false;
        }
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        //isItemRenderer = true;

        if (connections[ForgeDirectionUtils.getSide(ForgeDirection.EAST)] == null) {
            loc = new Vector3(0, -1, 0);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN)
                    continue;
                connections[ForgeDirectionUtils.getSide(d)] = new Vector3(0, 0, 0).add(d);
            }
        }
        GL11.glPushMatrix();
        {
            GL11.glTranslated(0.1, 1, 0);
            switch (type) {
                case ENTITY:
                    if (item.getItemFrame() != null) {
                        GL11.glTranslated(0.5, 0.5, 0.5);
                        GL11.glRotated(90, 0, 0, -1);
                        GL11.glTranslated(-0.375, -1, -0.5);
                    }
                    break;
                case EQUIPPED:
                    GL11.glTranslated(0, -0.5, 0);
                    break;
                case EQUIPPED_FIRST_PERSON:
                    GL11.glTranslated(-0.5, 0, 0);
                    break;
                case INVENTORY:
                    GL11.glTranslated(-0.125, -0.625, 0);
                    GL11.glScaled(1.125, 1.125, 1.125);
                    break;
                default:
                    break;
            }
            Tessellator.instance.startDrawingQuads();
            renderStatic(new Vector3(0, 0, 0), 0);
            Tessellator.instance.draw();

        }
        GL11.glPopMatrix();
    }

    @Override
    public int getCableWidth() {

        return 4;
    }

    @Override
    public int getCableHeight() {

        return 2;
    }

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public BluePowerTier getTier() {

        return null;
    }

    @Override
    public IPowerBase getHandler() {
        if(handler == null){
            handler = BPApi.getInstance().getNewPowerHandler();
            handler.init(this);
        }
        return handler;
    }
}
