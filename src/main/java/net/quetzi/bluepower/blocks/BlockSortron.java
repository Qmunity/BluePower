package net.quetzi.bluepower.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.TileSortron;

/**
 * @author Dynious
 */
public class BlockSortron extends BlockContainerBase {

    public BlockSortron() {

        super(Material.rock);
        setBlockName(Refs.BLOCKSORTRON_NAME);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {

        return TileSortron.class;
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.INVALID;
    }
}
