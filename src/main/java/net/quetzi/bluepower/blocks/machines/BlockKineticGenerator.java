package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileKinectGenerator;

/**
 * 
 * @author TheFjong
 * 
 */
public class BlockKineticGenerator extends BlockContainerBase {

    public BlockKineticGenerator() {
        
        super(Material.iron);
        setCreativeTab(CustomTabs.tabBluePowerMachines);
        setBlockName(Refs.KINETICGENERATOR_NAME);
        this.setBlockTextureName(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.KINETICGENERATOR_NAME + "_front");
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity() {

        return TileKinectGenerator.class;
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.KINETICGENERATOR_ID;
    }

}
