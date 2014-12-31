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

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

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

    /**
     * Return the name of a class extending {@link net.minecraft.client.gui.GuiScreen}. This class will be instantiated when the "config" button is
     * pressed in the mod list. It will have a single argument constructor - the "parent" screen, the same as all Minecraft GUIs. The expected
     * behaviour is that this screen will replace the "mod list" screen completely, and will return to the mod list screen through the parent link,
     * once the appropriate action is taken from the config screen.
     * <p/>
     * A null from this method indicates that the mod does not provide a "config" button GUI screen, and the config button will be hidden/disabled.
     * <p/>
     * This config GUI is anticipated to provide configuration to the mod in a friendly visual way. It should not be abused to set internals such as
     * IDs (they're gonna keep disappearing anyway), but rather, interesting behaviours. This config GUI is never run when a server game is running,
     * and should be used to configure desired behaviours that affect server state. Costs, mod game modes, stuff like that can be changed here.
     *
     * @return A class that will be instantiated on clicks on the config button or null if no GUI is desired.
     */
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {

        return BPGuiConfig.class;
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

    /**
     * Return an instance of a {@link cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler} that handles painting the right hand side option
     * screen for the specified {@link cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement}.
     *
     * @param element
     *            The element we wish to paint for
     * @return The Handler for painting it
     */
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {

        return null;
    }
}
