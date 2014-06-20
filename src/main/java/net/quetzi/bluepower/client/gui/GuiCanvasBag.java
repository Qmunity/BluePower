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
 *     
 *     @author Lumien
 */

package net.quetzi.bluepower.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerCanvasBag;
import net.quetzi.bluepower.references.Refs;

public class GuiCanvasBag extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/canvas_bag.png");
    ItemStack                             bag;

    public GuiCanvasBag(ItemStack bag, IInventory playerInventory, IInventory canvasBagInventory) {

        super(new ContainerCanvasBag(bag, playerInventory, canvasBagInventory), resLoc);

        this.bag = bag;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        this.fontRendererObj.drawString(bag.hasDisplayName() ? bag.getDisplayName() : I18n.format("item.canvas_bag.name", new Object[] {}), 8, 6, 4210752);
    }
}
