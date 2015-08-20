package com.bluepowermod.part.gate.supported;

import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPartSelectableCustom;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateSupported extends GateBase implements IGateLogic<GateSupported>, IPartSelectableCustom {

    public GateSupported() {

    }

    @Override
    protected void initConnections() {

    }

    @Override
    protected void initComponents() {

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean changeMode() {

        return false;
    }

    // Redwire connectivity

    // Rendering

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        IIcon planks = Blocks.planks.getIcon(0, 0);

        renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D), planks);
        renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D), planks);
        renderer.renderBox(new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D), planks);

        renderer.renderBox(new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D), planks);
        renderer.renderBox(new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D), planks);
        renderer.renderBox(new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D), planks);

        renderer.setColor(0xFFFFFF);

        return true;
    }

    // Misc

    @Override
    public IGateLogic<GateSupported> logic() {

        return this;
    }

    @Override
    public GateSupported getGate() {

        return this;
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        super.addSelectionBoxes(boxes);
        addBoxes(boxes);
    }

    @Override
    public void addCollisionBoxes(List<Vec3dCube> boxes, Entity entity) {

        super.addCollisionBoxes(boxes, entity);
        addBoxes(boxes);
    }

    protected void addBoxes(List<Vec3dCube> boxes) {

        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D));

        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D));
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);

        if (mop != null && this.getClass() == GateSupported.class)
            mop = new QMovingObjectPosition(mop, mop.getPart(), Vec3dCube.merge(getSelectionBoxes()));

        return mop;
    }

    @Override
    public boolean drawHighlight(QMovingObjectPosition mop, EntityPlayer player, float frame) {

        return false;
    }

}
