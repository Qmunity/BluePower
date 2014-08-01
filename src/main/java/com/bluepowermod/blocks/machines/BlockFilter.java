package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier2.TileFilter;

public class BlockFilter extends BlockContainerBase {
    
    public BlockFilter() {
    
        super(Material.rock);
        setBlockName(Refs.FILTER_NAME);
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileFilter.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.FILTER_ID;
    }
}
