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
package net.quetzi.bluepower.client.gui.widget;

/**
 * @author MineMaarten
 */
public class WidgetMode extends BaseWidget {
    
    public final int maxMode;
    
    public WidgetMode(int id, int x, int y, int textureX, int maxMode, String texture) {
    
        super(id, x, y, 14, 14, textureX, 0, texture);
        this.maxMode = maxMode;
    }
    
    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
    
        if (button == 0) {
            if (++value >= maxMode) value = 0;
        } else if (button == 1) {
            if (--value < 0) value = maxMode - 1;
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    protected int getTextureV() {
    
        return value * 14;
    }
    
    @Override
    protected int getTextureWidth() {
    
        return 256;
    }
    
    @Override
    protected int getTextureHeight() {
    
        return 256;
    }
    
}
