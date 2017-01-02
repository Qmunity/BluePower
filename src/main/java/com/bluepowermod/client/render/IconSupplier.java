/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
@SideOnly(Side.CLIENT)
public class IconSupplier {

    public static TextureAtlasSprite pneumaticTubeSide;
    public static TextureAtlasSprite pneumaticTubeOpaqueSide;
    public static TextureAtlasSprite pneumaticTubeOpaqueNode;
    public static TextureAtlasSprite pneumaticTubeColoring;
    public static TextureAtlasSprite pneumaticTubeGlass;
    public static TextureAtlasSprite pneumaticTubeGlass2;

    public static TextureAtlasSprite restrictionTubeSide;
    public static TextureAtlasSprite restrictionTubeNodeOpaque;
    public static TextureAtlasSprite restrictionTubeSideOpaque;

    public static TextureAtlasSprite magTubeSide;
    public static TextureAtlasSprite magCoilSide;
    public static TextureAtlasSprite magCoilFront;
    public static TextureAtlasSprite magTubeGlass;
    public static TextureAtlasSprite magTubeGlass2;

    public static TextureAtlasSprite acceleratorFront;
    public static TextureAtlasSprite acceleratorSide;
    public static TextureAtlasSprite acceleratorFrontPowered;
    public static TextureAtlasSprite acceleratorSidePowered;
    public static TextureAtlasSprite acceleratorInside;

    public static TextureAtlasSprite cagedLampFootSide;
    public static TextureAtlasSprite cagedLampFootTop;
    public static TextureAtlasSprite cagedLampCageSide;
    public static TextureAtlasSprite cagedLampCageTop;
    public static TextureAtlasSprite cagedLampLampActive;
    public static TextureAtlasSprite cagedLampLampInactive;
    public static TextureAtlasSprite cagedLampLampActiveTop;
    public static TextureAtlasSprite cagedLampLampInactiveTop;
    public static TextureAtlasSprite fixtureFootSide;
    public static TextureAtlasSprite fixtureFootTop;
    public static TextureAtlasSprite fixtureLampSideOn;
    public static TextureAtlasSprite fixtureLampTopOn;
    public static TextureAtlasSprite fixtureLampSideOff;
    public static TextureAtlasSprite fixtureLampTopOff;
    public static TextureAtlasSprite lampOn;
    public static TextureAtlasSprite lampOff;
    public static TextureAtlasSprite bluestoneTorchOn;
    public static TextureAtlasSprite bluestoneTorchOff;

    public static TextureAtlasSprite wire;
    public static TextureAtlasSprite wireInsulation1;
    public static TextureAtlasSprite wireInsulation2;

    public static TextureAtlasSprite wireBundledCross;
    public static TextureAtlasSprite wireBundledStraight1;
    public static TextureAtlasSprite wireBundledStraight2;
    public static TextureAtlasSprite wireBundledSide1;
    public static TextureAtlasSprite wireBundledSide2;
    public static TextureAtlasSprite wireBundledConnection;

    public static TextureAtlasSprite gateButton = null;
    public static TextureAtlasSprite siliconChipOff = null;
    public static TextureAtlasSprite siliconChipOn = null;
    public static TextureAtlasSprite taintedSiliconChipOff = null;
    public static TextureAtlasSprite taintedSiliconChipOn = null;
    public static TextureAtlasSprite quartzResonator = null;
    public static TextureAtlasSprite gateSolarPanel = null;

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        TextureMap reg = event.getMap();

            pneumaticTubeSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/pneumatic_tube_side"));
            pneumaticTubeGlass = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/glass"));
            pneumaticTubeGlass2 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/glass2"));
            pneumaticTubeOpaqueSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/pneumatic_tube_side_opaque"));
            pneumaticTubeOpaqueNode = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/pneumatic_tube_end_opaque"));
            restrictionTubeSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/restriction_tube_side"));
            restrictionTubeNodeOpaque = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/restriction_tube_end_opaque"));
            restrictionTubeSideOpaque = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/restriction_tube_side_opaque"));
            magTubeSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/mag_side"));
            magCoilSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/mag_casing"));
            magCoilFront = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/mag_casing_end"));
            magTubeGlass = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/glass_reinforced"));
            magTubeGlass2 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/glass_reinforced2"));
            acceleratorFront = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/machines/accelerator_front"));
            acceleratorFrontPowered = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/machines/accelerator_front_powered"));
            acceleratorSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/machines/accelerator_side"));
            acceleratorSidePowered = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/machines/accelerator_side_powered"));
            acceleratorInside = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/machines/accelerator_inside"));
            pneumaticTubeColoring = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/tubes/coloring"));

            cagedLampFootSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_foot_side"));
            cagedLampFootTop = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_foot_top"));
            cagedLampCageSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage"));
            cagedLampCageTop = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_top"));

            cagedLampLampActive = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_lamp_on"));
            cagedLampLampInactive = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_lamp_off"));

            cagedLampLampActiveTop = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_lamp_on_top"));
            cagedLampLampInactiveTop = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/cage_lamp_off_top"));

            fixtureFootSide = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_foot_side"));
            fixtureFootTop = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_foot_top"));
            fixtureLampSideOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_lamp_on"));
            fixtureLampTopOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_lamp_on_top"));

            fixtureLampSideOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_lamp_off"));
            fixtureLampTopOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/fixture_lamp_off_top"));

            lampOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/lamp_off"));
            lampOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/lamps/lamp_on"));

            bluestoneTorchOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/bluestone_torch_off"));
            bluestoneTorchOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/bluestone_torch_on"));

            wire = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/wire"));
            wireInsulation1 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/insulation1"));
            wireInsulation2 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/insulation2"));

            wireBundledConnection = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_connection"));
            wireBundledCross = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_cross"));
            wireBundledStraight1 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_straight_1"));
            wireBundledStraight2 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_straight_2"));
            wireBundledSide1 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_side_1"));
            wireBundledSide2 = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/wire/bundled_side_2"));

            gateButton = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/button"));
            siliconChipOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/silicon_chip_off"));
            siliconChipOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/silicon_chip_on"));
            taintedSiliconChipOff = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/tainted_silicon_chip_off"));
            taintedSiliconChipOn = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/tainted_silicon_chip_on"));
            quartzResonator = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/resonator"));
            gateSolarPanel = reg.registerSprite(new ResourceLocation(Refs.MODID + ":blocks/gates/components/solarpanel"));

            for (PartInfo i : PartManager.getRegisteredParts())
                i.getExample().registerIcons(reg);
    }
}
