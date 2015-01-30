package com.bluepowermod.part.gate.supported;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.wire.redstone.WireCommons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateNullCell
        extends
        GateSupported<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
        implements IAdvancedSilkyRemovable {

    private RedwireType typeA = null, typeB = null;
    private boolean bundledA = false, bundledB = false;
    private boolean inWorldA = false, inWorldB = false;
    private byte powerA, powerB;
    private boolean[] nullcells = new boolean[6];

    public GateNullCell() {

    }

    private GateNullCell(RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

        this.typeA = typeA;
        this.bundledA = bundledA;

        this.typeB = typeB;
        this.bundledB = bundledB;
    }

    @Override
    protected String getGateType() {

        return "nullcell";
    }

    // Redwire connectivity

    // Rendering

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        double height = 2 / 16D;

        IIcon wire = IconSupplier.wire;

        if (typeA != null) { // Flat
            renderer.setColor(WireCommons.getColorForPowerLevel(typeA.getColor(), powerA));

            ForgeDirection dir = ForgeDirection.NORTH;
            if (getRotation() % 2 == 1)
                dir = dir.getRotation(getFace());

            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D,
                    2 / 16D + (height / /* (nullcells[dir.ordinal()] ? 1 : */2/* ) */), 1 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / (nullcells[dir.getOpposite()
                    .ordinal()] ? 1 : 2)), 16 / 16D), wire);
        }

        if (typeB != null) { // Supported
            renderer.setColor(WireCommons.getColorForPowerLevel(typeB.getColor(), powerB));

            ForgeDirection dir2 = ForgeDirection.WEST;
            if (getRotation() % 2 == 1)
                dir2 = dir2.getRotation(getFace());

            // if (!nullcells[dir2.ordinal()])
            renderer.renderBox(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 10 / 16D, 9 / 16D), wire);
            // if (!nullcells[dir2.getOpposite().ordinal()])
            renderer.renderBox(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 10 / 16D, 9 / 16D), wire);
            renderer.renderBox(new Vec3dCube(0 / 16D, 10 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D), wire);
        }

        renderer.setColor(0xFFFFFF);

        renderer.resetTransformations();
        return true;
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        try {
            GateNullCell gnc = (GateNullCell) ((ItemPart) item.getItem()).createPart(item, BluePower.proxy.getPlayer(), null, null);

            tip.add(gnc.typeA + " x " + gnc.typeB);
        } catch (Exception ex) {
        }
    }

    @Override
    public List<ItemStack> getSubItems() {

        List<ItemStack> l = new ArrayList<ItemStack>();

        l.addAll(super.getSubItems());
        for (int i = 0; i < 2; i++)
            for (RedwireType t : RedwireType.values()) {
                for (RedwireType t2 : RedwireType.values())
                    l.add(getStackWithData(new GateNullCell(t, i == 1, t2, i == 1)));
                l.add(getStackWithData(new GateNullCell(t, i == 1, null, i == 1)));
                l.add(getStackWithData(new GateNullCell(null, i == 1, t, i == 1)));
            }

        return l;
    }

    private ItemStack getStackWithData(GateNullCell gate) {

        ItemStack is = getItem();
        if (is.getTagCompound() == null)
            is.setTagCompound(new NBTTagCompound());

        NBTTagCompound tag = new NBTTagCompound();
        gate.writeToNBT(tag);
        is.getTagCompound().setTag("tileData", tag);
        is.getTagCompound().setBoolean("hideSilkyTooltip", true);

        return is;
    }

    @Override
    public boolean preSilkyRemoval(World world, int x, int y, int z) {

        return true;
    }

    @Override
    public void postSilkyRemoval(World world, int x, int y, int z) {

    }

    @Override
    public boolean writeSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

        if (typeA != null) {
            tag.setInteger("typeA", typeA.ordinal());
            tag.setBoolean("bundledA", bundledA);
        }
        if (typeB != null) {
            tag.setInteger("typeB", typeB.ordinal());
            tag.setBoolean("bundledB", bundledB);
        }

        return true;
    }

    @Override
    public void readSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

        if (tag.hasKey("typeA")) {
            typeA = RedwireType.values()[tag.getInteger("typeA")];
            bundledA = tag.getBoolean("bundledA");
        }
        if (tag.hasKey("typeB")) {
            typeB = RedwireType.values()[tag.getInteger("typeB")];
            bundledB = tag.getBoolean("bundledB");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        if (typeA != null) {
            tag.setInteger("typeA", typeA.ordinal());
            tag.setBoolean("bundledA", bundledA);
            tag.setBoolean("inWorldA", inWorldA);
        }
        if (typeB != null) {
            tag.setInteger("typeB", typeB.ordinal());
            tag.setBoolean("bundledB", bundledB);
            tag.setBoolean("inWorldB", inWorldB);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        if (tag.hasKey("typeA")) {
            typeA = RedwireType.values()[tag.getInteger("typeA")];
            bundledA = tag.getBoolean("bundledA");
            inWorldA = tag.getBoolean("inWorldA");
        } else {
            typeA = null;
            bundledA = false;
            inWorldA = false;
        }
        if (tag.hasKey("typeB")) {
            typeB = RedwireType.values()[tag.getInteger("typeB")];
            bundledB = tag.getBoolean("bundledB");
            inWorldB = tag.getBoolean("inWorldB");
        } else {
            typeB = null;
            bundledB = false;
            inWorldB = false;
        }
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        if (channel == -1) {
            buffer.writeBoolean(typeA != null);
            if (typeA != null) {
                buffer.writeInt(typeA.ordinal());
                buffer.writeBoolean(bundledA);
            }
            buffer.writeBoolean(typeB != null);
            if (typeB != null) {
                buffer.writeInt(typeB.ordinal());
                buffer.writeBoolean(bundledB);
            }
        }
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        if (channel == -1) {
            if (buffer.readBoolean()) {
                typeA = RedwireType.values()[buffer.readInt()];
                bundledA = buffer.readBoolean();
            }
            if (buffer.readBoolean()) {
                typeB = RedwireType.values()[buffer.readInt()];
                bundledB = buffer.readBoolean();
            }
        }
    }

}
