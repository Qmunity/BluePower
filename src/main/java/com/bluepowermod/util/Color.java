/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;

/**
 * @author amadornes
 * 
 */
public enum Color {
    
    BLACK("\u00A70"), //
    DARK_BLUE("\u00A71"), //
    DARK_GREEN("\u00A72"), //
    DARK_AQUA("\u00A73"), //
    DARK_RED("\u00A74"), //
    DARK_PURPLE("\u00A75"), //
    GOLD("\u00A76"), //
    GRAY("\u00A77"), //
    DARK_GRAY("\u00A78"), //
    BLUE("\u00A79"), //
    GREEN("\u00A7a"), //
    AQUA("\u00A7b"), //
    RED("\u00A7c"), //
    LIGHT_PURPLE("\u00A7d"), //
    YELLOW("\u00A7e"), //
    WHITE("\u00A7f"), //
    
    STRIKE_THROUGH("\u00A7m"), //
    UNDERLINE("\u00A7n"), //
    BOLD("\u00A7l"), //
    RANDOM("\u00A7k"), //
    ITALIC("\u00A7o"), //
    RESET("\u00A7r");//
    
    public String code = "";
    
    private Color(String code) {
    
        this.code = code;
    }
    
    @Override
    public String toString() {
    
        return code;
    }
    
}
