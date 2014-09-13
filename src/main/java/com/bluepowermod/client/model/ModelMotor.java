package com.bluepowermod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelMotor extends ModelBase {

    // fields
    ModelRenderer body1;
    ModelRenderer body2;
    ModelRenderer body3;
    ModelRenderer body4;
    ModelRenderer body5;
    ModelRenderer body6;
    ModelRenderer body7;
    ModelRenderer rotatingThingy;
    ModelRenderer rotatingThingyBase;
    ModelRenderer rotatingThingyBaseBase;
    ModelRenderer thingyInTheBack;
    ModelRenderer baseLeft;
    ModelRenderer supportLeft1;
    ModelRenderer supportLeft2;
    ModelRenderer baseRight;
    ModelRenderer supportRight1;
    ModelRenderer supportRight2;

    public ModelMotor() {

        textureWidth = 64;
        textureHeight = 64;

        body1 = new ModelRenderer(this, 0, 4);
        body1.addBox(0F, 0F, 0F, 4, 1, 10);
        body1.setRotationPoint(-2F, 11F, -4F);
        body1.setTextureSize(64, 64);
        body1.mirror = true;
        setRotation(body1, 0F, 0F, 0F);
        body2 = new ModelRenderer(this, 28, 4);
        body2.addBox(0F, 0F, 0F, 8, 1, 10);
        body2.setRotationPoint(-4F, 12F, -4F);
        body2.setTextureSize(64, 64);
        body2.mirror = true;
        setRotation(body2, 0F, 0F, 0F);
        body3 = new ModelRenderer(this, 0, 15);
        body3.addBox(0F, 0F, 0F, 10, 2, 10);
        body3.setRotationPoint(-5F, 13F, -4F);
        body3.setTextureSize(64, 64);
        body3.mirror = true;
        setRotation(body3, 0F, 0F, 0F);
        body4 = new ModelRenderer(this, 0, 27);
        body4.addBox(0F, 0F, 0F, 12, 4, 10);
        body4.setRotationPoint(-6F, 15F, -4F);
        body4.setTextureSize(64, 64);
        body4.mirror = true;
        setRotation(body4, 0F, 0F, 0F);
        body5 = new ModelRenderer(this, 0, 41);
        body5.addBox(0F, 0F, 0F, 10, 2, 10);
        body5.setRotationPoint(-5F, 19F, -4F);
        body5.setTextureSize(64, 64);
        body5.mirror = true;
        setRotation(body5, 0F, 0F, 0F);
        body6 = new ModelRenderer(this, 28, 53);
        body6.addBox(0F, 0F, 0F, 8, 1, 10);
        body6.setRotationPoint(-4F, 21F, -4F);
        body6.setTextureSize(64, 64);
        body6.mirror = true;
        setRotation(body6, 0F, 0F, 0F);
        body7 = new ModelRenderer(this, 0, 53);
        body7.addBox(0F, 0F, 0F, 4, 1, 10);
        body7.setRotationPoint(-2F, 22F, -4F);
        body7.setTextureSize(64, 64);
        body7.mirror = true;
        setRotation(body7, 0F, 0F, 0F);
        rotatingThingy = new ModelRenderer(this, 0, 15);
        rotatingThingy.addBox(-0.5F, -0.5F, 0F, 1, 1, 2);
        rotatingThingy.setRotationPoint(0F, 17F, -8F);
        rotatingThingy.setTextureSize(64, 64);
        rotatingThingy.mirror = true;
        setRotation(rotatingThingy, 0F, 0F, 0F);
        rotatingThingyBase = new ModelRenderer(this, 0, 18);
        rotatingThingyBase.addBox(0F, 0F, 0F, 2, 2, 1);
        rotatingThingyBase.setRotationPoint(-1F, 16F, -6F);
        rotatingThingyBase.setTextureSize(64, 64);
        rotatingThingyBase.mirror = true;
        setRotation(rotatingThingyBase, 0F, 0F, 0F);
        rotatingThingyBaseBase = new ModelRenderer(this, 30, 15);
        rotatingThingyBaseBase.addBox(0F, 0F, 0F, 6, 6, 1);
        rotatingThingyBaseBase.setRotationPoint(-3F, 14F, -5F);
        rotatingThingyBaseBase.setTextureSize(64, 64);
        rotatingThingyBaseBase.mirror = true;
        setRotation(rotatingThingyBaseBase, 0F, 0F, 0F);
        thingyInTheBack = new ModelRenderer(this, 0, 21);
        thingyInTheBack.addBox(0F, 0F, 0F, 2, 2, 1);
        thingyInTheBack.setRotationPoint(-1F, 16F, 6F);
        thingyInTheBack.setTextureSize(64, 64);
        thingyInTheBack.mirror = true;
        setRotation(thingyInTheBack, 0F, 0F, 0F);
        baseLeft = new ModelRenderer(this, 38, 38);
        baseLeft.addBox(0F, 0F, 0F, 1, 1, 12);
        baseLeft.setRotationPoint(-7F, 23F, -6F);
        baseLeft.setTextureSize(64, 64);
        baseLeft.mirror = true;
        setRotation(baseLeft, 0F, 0F, 0F);
        supportLeft1 = new ModelRenderer(this, 44, 15);
        supportLeft1.addBox(0F, 0F, 0F, 1, 6, 1);
        supportLeft1.setRotationPoint(-4.3F, 18.8F, 0F);
        supportLeft1.setTextureSize(64, 64);
        supportLeft1.mirror = true;
        setRotation(supportLeft1, -0.6108652F, 0F, 0.5235988F);
        supportLeft2 = new ModelRenderer(this, 44, 15);
        supportLeft2.addBox(0F, 0F, 0F, 1, 6, 1);
        supportLeft2.setRotationPoint(-4.6F, 19.2F, 0F);
        supportLeft2.setTextureSize(64, 64);
        supportLeft2.mirror = true;
        setRotation(supportLeft2, 0.6108652F, 0F, 0.5235988F);
        baseRight = new ModelRenderer(this, 38, 38);
        baseRight.addBox(0F, 0F, 0F, 1, 1, 12);
        baseRight.setRotationPoint(6F, 23F, -6F);
        baseRight.setTextureSize(64, 64);
        baseRight.mirror = true;
        setRotation(baseRight, 0F, 0F, 0F);
        supportRight1 = new ModelRenderer(this, 44, 15);
        supportRight1.addBox(0F, 0F, 0F, 1, 6, 1);
        supportRight1.setRotationPoint(3.4F, 19.3F, 0F);
        supportRight1.setTextureSize(64, 64);
        supportRight1.mirror = true;
        setRotation(supportRight1, -0.6108652F, 0F, -0.5235988F);
        supportRight2 = new ModelRenderer(this, 44, 15);
        supportRight2.addBox(0F, 0F, 0F, 1, 6, 1);
        supportRight2.setRotationPoint(3.7F, 19.8F, 0F);
        supportRight2.setTextureSize(64, 64);
        supportRight2.mirror = true;
        setRotation(supportRight2, 0.6108652F, 0F, -0.5235988F);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {

        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {

    }

    public void render(boolean rotate) {

        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(180, 0, 0, 1);
        GL11.glTranslated(-0.5, 0, -0.5);

        GL11.glTranslated(0.5, -1.5, 0.5);

        float f5 = 0.0625F;

        body1.render(f5);
        body2.render(f5);
        body3.render(f5);
        body4.render(f5);
        body5.render(f5);
        body6.render(f5);
        body7.render(f5);

        if (rotate) {
            rotatingThingy.rotateAngleZ = (float) ((System.currentTimeMillis() / 10D) % 360);
            rotatingThingy.render(f5);
        } else {
            rotatingThingy.render(f5);
        }

        rotatingThingyBase.render(f5);
        rotatingThingyBaseBase.render(f5);
        thingyInTheBack.render(f5);
        baseLeft.render(f5);
        supportLeft1.render(f5);
        supportLeft2.render(f5);
        baseRight.render(f5);
        supportRight1.render(f5);
        supportRight2.render(f5);
    }

}
