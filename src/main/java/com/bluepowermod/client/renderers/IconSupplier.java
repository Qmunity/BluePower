/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.renderers;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * @author MineMaarten
 */
public class IconSupplier {

    public static IIcon pneumaticTubeSide;
    public static IIcon pneumaticTubeNode;
    public static IIcon pneumaticTubeOpaqueSide;
    public static IIcon pneumaticTubeOpaqueNode;
    public static IIcon pneumaticTubeColorNode;
    public static IIcon pneumaticTubeColorSide;

    public static IIcon restrictionTubeNode;
    public static IIcon restrictionTubeSide;
    public static IIcon restrictionTubeNodeOpaque;
    public static IIcon restrictionTubeSideOpaque;

    public static IIcon magTubeNode;
    public static IIcon magTubeSide;
    public static IIcon magCoilSide;
    public static IIcon magCoilFront;

    public static IIcon acceleratorFront;
    public static IIcon acceleratorSide;
    public static IIcon acceleratorFrontPowered;
    public static IIcon acceleratorSidePowered;
    public static IIcon acceleratorInside;

    public static IIcon cagedLampFootSide;
    public static IIcon cagedLampFootTop;
    public static IIcon cagedLampCageSide;
    public static IIcon cagedLampCageTop;
    public static IIcon cagedLampLampActive;
    public static IIcon cagedLampLampInactive;
    public static IIcon cagedLampLampActiveTop;
    public static IIcon cagedLampLampInactiveTop;
    public static IIcon fixtureFootSide;
    public static IIcon fixtureFootTop;
    public static IIcon fixtureLampSideOn;
    public static IIcon fixtureLampTopOn;
    public static IIcon fixtureLampSideOff;
    public static IIcon fixtureLampTopOff;
    public static IIcon lampOn;
    public static IIcon lampOff;
    public static IIcon bluestoneTorchOn;
    public static IIcon bluestoneTorchOff;

    public static IIcon bluestoneOn;
    public static IIcon bluestoneOff;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        if (event.map.getTextureType() == 0) {
            pneumaticTubeSide = event.map.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_side");
            pneumaticTubeNode = event.map.registerIcon(Refs.MODID + ":tubes/tube_end");
            pneumaticTubeOpaqueSide = event.map.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_side_opaque");
            pneumaticTubeOpaqueNode = event.map.registerIcon(Refs.MODID + ":tubes/tube_end_opaque");
            pneumaticTubeColorSide = event.map.registerIcon(Refs.MODID + ":tubes/tube_color_side");
            pneumaticTubeColorNode = event.map.registerIcon(Refs.MODID + ":tubes/tube_color_end");
            restrictionTubeNode = event.map.registerIcon(Refs.MODID + ":tubes/restriction_tube_end");
            restrictionTubeSide = event.map.registerIcon(Refs.MODID + ":tubes/restriction_tube_side");
            restrictionTubeNodeOpaque = event.map.registerIcon(Refs.MODID + ":tubes/restriction_tube_end_opaque");
            restrictionTubeSideOpaque = event.map.registerIcon(Refs.MODID + ":tubes/restriction_tube_side_opaque");
            magTubeNode = event.map.registerIcon(Refs.MODID + ":tubes/mag_end");
            magTubeSide = event.map.registerIcon(Refs.MODID + ":tubes/mag_side");
            magCoilSide = event.map.registerIcon(Refs.MODID + ":tubes/mag_casing");
            magCoilFront = event.map.registerIcon(Refs.MODID + ":tubes/mag_casing_end");
            acceleratorFront = event.map.registerIcon(Refs.MODID + ":machines/accelerator_front");
            acceleratorFrontPowered = event.map.registerIcon(Refs.MODID + ":machines/accelerator_front_powered");
            acceleratorSide = event.map.registerIcon(Refs.MODID + ":machines/accelerator_side");
            acceleratorSidePowered = event.map.registerIcon(Refs.MODID + ":machines/accelerator_side_powered");
            acceleratorInside = event.map.registerIcon(Refs.MODID + ":machines/accelerator_inside");

            cagedLampFootSide = event.map.registerIcon(Refs.MODID + ":lamps/cage_foot_side");
            cagedLampFootTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_foot_top");
            cagedLampCageSide = event.map.registerIcon(Refs.MODID + ":lamps/cage");
            cagedLampCageTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_top");

            cagedLampLampActive = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_on");
            cagedLampLampInactive = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_off");

            cagedLampLampActiveTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_on_top");
            cagedLampLampInactiveTop = event.map.registerIcon(Refs.MODID + ":lamps/cage_lamp_off_top");

            fixtureFootSide = event.map.registerIcon(Refs.MODID + ":lamps/fixture_foot_side");
            fixtureFootTop = event.map.registerIcon(Refs.MODID + ":lamps/fixture_foot_top");
            fixtureLampSideOn = event.map.registerIcon(Refs.MODID + ":lamps/fixture_lamp_on");
            fixtureLampTopOn = event.map.registerIcon(Refs.MODID + ":lamps/fixture_lamp_on_top");

            fixtureLampSideOff = event.map.registerIcon(Refs.MODID + ":lamps/fixture_lamp_off");
            fixtureLampTopOff = event.map.registerIcon(Refs.MODID + ":lamps/fixture_lamp_off_top");

            lampOn = event.map.registerIcon(Refs.MODID + ":lamps/lamp_off");
            lampOff = event.map.registerIcon(Refs.MODID + ":lamps/lamp_on");

            bluestoneTorchOff = event.map.registerIcon(Refs.MODID + ":bluestone_torch_off");
            bluestoneTorchOn = event.map.registerIcon(Refs.MODID + ":bluestone_torch_on");

            bluestoneOff = event.map.registerIcon(Refs.MODID + ":bluestone_off");
            bluestoneOn = event.map.registerIcon(Refs.MODID + ":bluestone_on");

            for (PartInfo i : PartManager.getRegisteredParts())
                i.getExample().registerIcons(event.map);
        }
    }
}
