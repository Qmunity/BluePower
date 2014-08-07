package com.bluepowermod.part.cable.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.minecraft.ButtonPart;
import codechicken.multipart.minecraft.LeverPart;
import codechicken.multipart.minecraft.RedstoneTorchPart;

import com.bluepowermod.api.bluestone.ABluestoneConnectFMP;
import com.bluepowermod.api.bluestone.IBluestoneWire;

public class BluestoneConnectFMP extends ABluestoneConnectFMP {

    @Override
    public int getExtraLength(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        ForgeDirection f = ForgeDirection.getOrientation(wire.getFace());
        if (part instanceof LeverPart) {
            if (((LeverPart) part).getFace() == wire.getFace()) {
                if (f != ForgeDirection.UP && f != ForgeDirection.DOWN) {
                    if (cableSide == ForgeDirection.UP || cableSide == ForgeDirection.DOWN) {
                        return 4;
                    } else {
                        return 5;
                    }
                } else {
                    return 5;
                }
            }
        }
        if (part instanceof ButtonPart) {
            if (((ButtonPart) part).getFace() == wire.getFace()) {
                if (f != ForgeDirection.UP && f != ForgeDirection.DOWN) {
                    if (cableSide == ForgeDirection.UP || cableSide == ForgeDirection.DOWN) {
                        return 6;
                    } else {
                        return 5;
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public boolean canConnect(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        return false;
    }

    @Override
    public void renderExtraCables(TMultiPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        BluestoneApi api = BluestoneApi.getInstance();

        GL11.glPushMatrix();

        if (part instanceof ButtonPart || part instanceof LeverPart)
            renderExtraCablesButtonLever(api, (IFaceRedstonePart) part, wire, cableSide);

        if (part instanceof RedstoneTorchPart)
            renderExtraCablesRedstoneTorch(api, (RedstoneTorchPart) part, wire, cableSide);

        GL11.glPopMatrix();
    }

    private void renderExtraCablesButtonLever(BluestoneApi api, IFaceRedstonePart part, IBluestoneWire wire, ForgeDirection cableSide) {

        if (part.getFace() == wire.getFace())
            return;

        ForgeDirection f = ForgeDirection.getOrientation(wire.getFace());
        switch (f) {
        case UP:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case DOWN:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case EAST:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(0, 1, 7, 1, 5, 9);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 5, 9);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case WEST:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(0, 1, 7, 1, 5, 9);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 5, 9);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case NORTH:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case SOUTH:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        default:
            break;
        }
    }

    private void renderExtraCablesRedstoneTorch(BluestoneApi api, RedstoneTorchPart part, IBluestoneWire wire, ForgeDirection cableSide) {

        ForgeDirection f = ForgeDirection.getOrientation(wire.getFace());
        switch (f) {
        case UP:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 3, 16);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 3, 1);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 3, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 3, 9);
                break;
            }
            break;
        case DOWN:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 5, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 5, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 5, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 5, 9);
                break;
            }
            break;
        case EAST:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(0, 1, 7, 1, 5, 9);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 5, 9);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case WEST:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(0, 1, 7, 1, 5, 9);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 5, 9);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case NORTH:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        case SOUTH:
            switch (part.getFace()) {
            case 0:// Not available until 1.8
                break;
            case 1:// Not available until 1.8
                break;
            case 2:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 0, 9, 6, 1);
                break;
            case 3:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(7, 1, 15, 9, 6, 16);
                break;
            case 4:
                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                api.renderBox(15, 1, 7, 16, 6, 9);
                break;
            case 5:
                api.renderBox(0, 1, 7, 1, 6, 9);
                break;
            }
            break;
        default:
            break;
        }
    }

}
