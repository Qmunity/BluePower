package com.bluepowermod.items;

public class ItemPaintBrush extends ItemDamageableColorableOverlay {
    
    public ItemPaintBrush(String name) {
    
        super(name);
        
    }
    
    @Override
    protected int getMaxUses() {
    
        return 256;
    }
    
}
