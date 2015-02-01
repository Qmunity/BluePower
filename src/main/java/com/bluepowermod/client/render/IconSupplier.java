/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
@SideOnly(Side.CLIENT)
public class IconSupplier {

    public static IIcon pneumaticTubeSide;
    public static IIcon pneumaticTubeOpaqueSide;
    public static IIcon pneumaticTubeOpaqueNode;
    public static IIcon pneumaticTubeColoring;

    public static IIcon restrictionTubeSide;
    public static IIcon restrictionTubeNodeOpaque;
    public static IIcon restrictionTubeSideOpaque;

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

    public static IIcon wire;
    public static IIcon wireInsulation1;
    public static IIcon wireInsulation2;
    public static IIcon wireBundled;
    public static IIcon wireBundled2;
    public static IIcon wireBundled3;

    public static IIcon gateButton = null;
    public static IIcon siliconChipOff = null;
    public static IIcon siliconChipOn = null;
    public static IIcon taintedSiliconChipOff = null;
    public static IIcon taintedSiliconChipOn = null;
    public static IIcon quartzResonator = null;
    public static IIcon gateSolarPanel = null;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        TextureMap reg = event.map;

        if (reg.getTextureType() == 0) {
            pneumaticTubeSide = reg.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_side");
            pneumaticTubeOpaqueSide = reg.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_side_opaque");
            pneumaticTubeOpaqueNode = reg.registerIcon(Refs.MODID + ":tubes/pneumatic_tube_end_opaque");
            restrictionTubeSide = reg.registerIcon(Refs.MODID + ":tubes/restriction_tube_side");
            restrictionTubeNodeOpaque = reg.registerIcon(Refs.MODID + ":tubes/restriction_tube_end_opaque");
            restrictionTubeSideOpaque = reg.registerIcon(Refs.MODID + ":tubes/restriction_tube_side_opaque");
            magTubeSide = reg.registerIcon(Refs.MODID + ":tubes/mag_side");
            magCoilSide = reg.registerIcon(Refs.MODID + ":tubes/mag_casing");
            magCoilFront = reg.registerIcon(Refs.MODID + ":tubes/mag_casing_end");
            acceleratorFront = reg.registerIcon(Refs.MODID + ":machines/accelerator_front");
            acceleratorFrontPowered = reg.registerIcon(Refs.MODID + ":machines/accelerator_front_powered");
            acceleratorSide = reg.registerIcon(Refs.MODID + ":machines/accelerator_side");
            acceleratorSidePowered = reg.registerIcon(Refs.MODID + ":machines/accelerator_side_powered");
            acceleratorInside = reg.registerIcon(Refs.MODID + ":machines/accelerator_inside");
            pneumaticTubeColoring = reg.registerIcon(Refs.MODID + ":tubes/coloring");

            cagedLampFootSide = reg.registerIcon(Refs.MODID + ":lamps/cage_foot_side");
            cagedLampFootTop = reg.registerIcon(Refs.MODID + ":lamps/cage_foot_top");
            cagedLampCageSide = reg.registerIcon(Refs.MODID + ":lamps/cage");
            cagedLampCageTop = reg.registerIcon(Refs.MODID + ":lamps/cage_top");

            cagedLampLampActive = reg.registerIcon(Refs.MODID + ":lamps/cage_lamp_on");
            cagedLampLampInactive = reg.registerIcon(Refs.MODID + ":lamps/cage_lamp_off");

            cagedLampLampActiveTop = reg.registerIcon(Refs.MODID + ":lamps/cage_lamp_on_top");
            cagedLampLampInactiveTop = reg.registerIcon(Refs.MODID + ":lamps/cage_lamp_off_top");

            fixtureFootSide = reg.registerIcon(Refs.MODID + ":lamps/fixture_foot_side");
            fixtureFootTop = reg.registerIcon(Refs.MODID + ":lamps/fixture_foot_top");
            fixtureLampSideOn = reg.registerIcon(Refs.MODID + ":lamps/fixture_lamp_on");
            fixtureLampTopOn = reg.registerIcon(Refs.MODID + ":lamps/fixture_lamp_on_top");

            fixtureLampSideOff = reg.registerIcon(Refs.MODID + ":lamps/fixture_lamp_off");
            fixtureLampTopOff = reg.registerIcon(Refs.MODID + ":lamps/fixture_lamp_off_top");

            lampOn = reg.registerIcon(Refs.MODID + ":lamps/lamp_off");
            lampOff = reg.registerIcon(Refs.MODID + ":lamps/lamp_on");

            bluestoneTorchOff = reg.registerIcon(Refs.MODID + ":bluestone_torch_off");
            bluestoneTorchOn = reg.registerIcon(Refs.MODID + ":bluestone_torch_on");

            wire = reg.registerIcon(Refs.MODID + ":wire/wire");
            wireInsulation1 = reg.registerIcon(Refs.MODID + ":wire/insulation1");
            wireInsulation2 = reg.registerIcon(Refs.MODID + ":wire/insulation2");
            wireBundled = reg.registerIcon(Refs.MODID + ":wire/bundled");
            wireBundled2 = reg.registerIcon(Refs.MODID + ":wire/bundled2");
            wireBundled3 = reg.registerIcon(Refs.MODID + ":wire/bundled3");

            gateButton = reg.registerIcon(Refs.MODID + ":gates/components/button");
            siliconChipOff = reg.registerIcon(Refs.MODID + ":gates/components/silicon_chip_off");
            siliconChipOn = reg.registerIcon(Refs.MODID + ":gates/components/silicon_chip_on");
            taintedSiliconChipOff = reg.registerIcon(Refs.MODID + ":gates/components/tainted_silicon_chip_off");
            taintedSiliconChipOn = reg.registerIcon(Refs.MODID + ":gates/components/tainted_silicon_chip_on");
            quartzResonator = reg.registerIcon(Refs.MODID + ":gates/components/resonator");
            gateSolarPanel = reg.registerIcon(Refs.MODID + ":gates/components/solarpanel");

            for (PartInfo i : PartManager.getRegisteredParts())
                i.getExample().registerIcons(reg);
        }
    }
}
