/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.part;

import java.util.Comparator;

import net.minecraft.creativetab.CreativeTabs;

/**
 * @author amadornes
 * 
 */
public class ComparatorCreativeTabIndex implements Comparator<BPPart> {
    
    private CreativeTabs tab;
    
    /**
     * @author amadornes
     * 
     */
    public ComparatorCreativeTabIndex(CreativeTabs tab) {
    
        this.tab = tab;
    }
    
    @Override
    public int compare(BPPart o1, BPPart o2) {
    
        int i1 = 0;
        CreativeTabs[] t1 = o1.getCreativeTabs();
        for (int i = 0; i < t1.length; i++) {
            if (t1[i] == tab) {
                i1 = o1.getCreativeTabIndexes()[i];
                break;
            }
        }
        
        int i2 = 0;
        CreativeTabs[] t2 = o2.getCreativeTabs();
        for (int i = 0; i < t2.length; i++) {
            if (t2[i] == tab) {
                i2 = o1.getCreativeTabIndexes()[i];
                break;
            }
        }
        
        return i1 - i2;
    }
    
}
