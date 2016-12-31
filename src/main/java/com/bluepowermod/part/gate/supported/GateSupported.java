package com.bluepowermod.part.gate.supported;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPartSelectableCustom;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.List;

public abstract class GateSupported<C_BOTTOM extends GateConnectionBase, C_TOP extends GateConnectionBase, C_LEFT extends GateConnectionBase, C_RIGHT extends GateConnectionBase, C_FRONT extends GateConnectionBase, C_BACK extends GateConnectionBase>
        extends GateBase<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK> implements
        IGateLogic<GateSupported<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK>>, IPartSelectableCustom {

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
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, VertexBuffer buffer, int pass) {

        super.renderStatic(translation, renderer, buffer, pass);

        TextureAtlasSprite planks = Blocks.PLANKS.getIcon(0, 0);

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
    public IGateLogic<GateSupported<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK>> logic() {

        return this;
    }

    @Override
    public GateSupported<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK> getGate() {

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
    public QRayTraceResult rayTrace(Vec3d start, Vec3d end) {

        QRayTraceResult mop = super.rayTrace(start, end);

        if (mop != null && this.getClass() == GateSupported.class)
            mop = new QRayTraceResult(mop, mop.getPart(), Vec3dCube.merge(getSelectionBoxes()));

        return mop;
    }

    @Override
    public boolean drawHighlight(QRayTraceResult mop, EntityPlayer player, float frame) {

        return false;
    }

}
