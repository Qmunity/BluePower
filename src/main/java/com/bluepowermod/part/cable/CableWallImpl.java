package com.bluepowermod.part.cable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import scala.actors.threadpool.Arrays;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.init.CustomTabs;

public class CableWallImpl extends CableWall {

    protected static Vector3Cube BOX = new Vector3Cube(0, 0, 0, 1, 1D / 8D, 1);

    @Override
    public String getType() {

        return "cableWallImpl";
    }

    @Override
    public String getUnlocalizedName() {

        return "cableWallImpl";
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        return cable instanceof CableWallImpl;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        return block == Blocks.lapis_block;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        return tile instanceof TileEntityFurnace;
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(BOX.clone().toAABB());
    }

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack item) {

        player.addChatMessage(new ChatComponentText((world.isRemote ? "Client" : "Server") + ": " + Arrays.asList(connections) + " Face: "
                + getFace() + "(" + ForgeDirection.getOrientation(getFace()) + ")"));
        return true;
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerCircuits;
    }

}
