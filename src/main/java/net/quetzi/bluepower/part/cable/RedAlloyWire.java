package net.quetzi.bluepower.part.cable;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.redstone.IBPRedstoneConductor;
import net.quetzi.bluepower.api.part.redstone.IBPRedstonePart;
import net.quetzi.bluepower.api.part.redstone.RedstoneNetwork;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.init.CustomTabs;

public class RedAlloyWire extends BPPartFace implements IBPRedstoneConductor {
    
    private int    color = -1;
    private String name  = "";
    
    /**
     * @author amadornes
     * 
     */
    public RedAlloyWire(String name, Integer color) {
    
        this.color = color.intValue();
        this.name = name;
    }
    
    /**
     * @author amadornes
     * 
     */
    public RedAlloyWire() {
    
    }
    
    @Override
    public String getType() {
    
        return "redalloy" + (color >= 0 ? "." + name : "");
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "redalloy" + (color >= 0 ? "." + name : "");
    }
    
    @Override
    public List<IBPRedstonePart> getConnections(ForgeDirection side) {
    
        return null;
    }
    
    private RedstoneNetwork net;
    
    @Override
    public RedstoneNetwork getNetwork() {
    
        return net;
    }
    
    @Override
    public void setNetwork(RedstoneNetwork network) {
    
        net = network;
    }
    
    @Override
    public void onUpdateConnection() {
    
        markPartForRenderUpdate();
    }
    
    @Override
    public void onRedstoneUpdate() {
    
        markPartForRenderUpdate();
    }
    
    @Override
    public boolean renderStatic(Vector3 loc, int pass) {
    
        return true;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerCircuits;
    }
    
    @Override
    public int getCreativeTabIndex() {
    
        return 1;
    }
    
}
