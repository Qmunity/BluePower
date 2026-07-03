/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class BPClientConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final Client CONFIG = new Client(BUILDER);

    public static class Client {

        public final ForgeConfigSpec.BooleanValue renderPlacementPreview;
        public final ForgeConfigSpec.ConfigValue<Double> previewPlacementOpacity;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("Rendering").comment("Rendering configs");
                renderPlacementPreview = builder
			    .comment("Render preview of gates before placing them")
			    .translation("bluepower.client_config.render_placement_preview")
			    .define("renderPlacementPreview", true);
		        previewPlacementOpacity = builder
			    .comment("Opacity of the render preview. Higher value = less transparent, lower = more transparent")
			    .translation("bluepower.client_config.show_placement_preview")
			    .defineInRange("previewPlacementOpacity", 0.4D, 0D, 1D);
		    builder.pop();
            }

    }
    public static final ForgeConfigSpec spec = BUILDER.build();

}
