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

/*
 * @author Quetzi
 */

package com.bluepowermod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class BPGuiFactory implements IModGuiFactory {

    /**
     * Called when instantiated to initialize with the active minecraft instance.
     *
     * @param minecraftInstance
     *            the instance
     */
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return false;
    }

    @Override
    public Screen createConfigGui(Screen parentScreen) {
        return null;
    }

    /**
     * Return a list of the "runtime" categories this mod wishes to populate with GUI elements.
     * <p/>
     * Runtime categories are created on demand and organized in a 'lite' tree format. The parent represents the parent node in the tree. There is one
     * special parent 'Help' that will always list first, and is generally meant to provide Help type content for mods. The remaining parents will
     * sort alphabetically, though this may change if there is a lot of alphabetic abuse. "AAA" is probably never a valid category parent.
     * <p/>
     * Runtime configuration itself falls into two flavours: in-game help, which is generally non interactive except for the text it wishes to show,
     * and client-only affecting behaviours. This would include things like toggling minimaps, or cheat modes or anything NOT affecting the behaviour
     * of the server. Please don't abuse this to change the state of the server in any way, this is intended to behave identically when the server is
     * local or remote.
     *
     * @return the set of options this mod wishes to have available, or empty if none
     */
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {

        return null;
    }

}
