package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBase;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class BlockInsulatedAlloyWire extends BlockAlloyWire implements IBlockColor, IItemColor, ICustomModelBlock{

    private final MinecraftColor color;
    private final String type;

    public static final PropertyInteger STRAIGHT = PropertyInteger.create("straight", 0, 5);

    public BlockInsulatedAlloyWire(String type, MinecraftColor color) {
        super(Material.CIRCUITS);
        this.color = color;
        this.type = type;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(CONNECTED_FRONT, false).withProperty(CONNECTED_BACK, false).withProperty(CONNECTED_LEFT, false).withProperty(CONNECTED_RIGHT, false).withProperty(STRAIGHT, 1).withProperty(POWERED, false));
        setTranslationKey("wire." + type + "." + color.name().toLowerCase());
        setCreativeTab(BPCreativeTabs.wiring);
        setRegistryName(Refs.MODID + ":" + type + "_wire." + color.name());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        //All wires need to use the same blockstate
        StateMapperBase stateMapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation(Refs.MODID + ":insulated_alloy_wire", getPropertyString(iBlockState.getProperties()));
            }
        };
        ModelLoader.setCustomStateMapper(this, stateMapper);
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Refs.MODID + ":insulated_alloy_wire", "inventory"));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1,0.1875,1);
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, STRAIGHT, POWERED);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Boolean connected_back = state.getValue(CONNECTED_BACK);
        Boolean connected_front = state.getValue(CONNECTED_FRONT);
        Boolean connected_left = state.getValue(CONNECTED_LEFT);
        Boolean connected_right = state.getValue(CONNECTED_RIGHT);

        int connections = 0;
        int straight = 1;

        for (EnumFacing face : FACING.getAllowedValues()){
            switch (face){
                case NORTH:
                    connected_front = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_front){
                        connections += 1;
                        straight = 4;
                    }
                    break;
                case SOUTH:
                    connected_back = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_back){
                        connections += 1;
                        straight = 3;
                    }
                    break;
                case WEST:
                    connected_left = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_left){
                        connections += 1;
                        straight = 5;
                    }
                    break;
                case EAST:
                    connected_right = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_right){
                        connections += 1;
                        straight = 2;
                    }
                    break;
            }
        }
        if(connections > 1){
            straight = 0;
        }

        return super.getActualState(state, worldIn, pos)
                .withProperty(CONNECTED_RIGHT, connected_right)
                .withProperty(CONNECTED_LEFT, connected_left)
                .withProperty(CONNECTED_FRONT, connected_front)
                .withProperty(CONNECTED_BACK, connected_back)
                .withProperty(STRAIGHT, straight);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        //Color for Block
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        //Color for Itemstack
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }
}