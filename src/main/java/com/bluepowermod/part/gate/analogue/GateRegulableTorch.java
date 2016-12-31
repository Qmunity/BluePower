package com.bluepowermod.part.gate.analogue;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.gui.gate.GuiGateSingleCounter;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentBorderDark;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentWire;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class GateRegulableTorch extends GateSimpleAnalogue implements IGuiButtonSensitive {

    private GateComponentPointer torch;
    private int power = 0;

    @Override
    protected String getGateType() {

        return "regulabletorch";
    }

    @Override
    protected void initializeConnections() {

        front().enable();
    }

    @Override
    protected void initComponents() {

        addComponent((torch = new GateComponentPointer(this, 0xffff00, 7 / 16D, false).setAngle(1 / 8D)).setState(true));
        addComponent(new GateComponentWire(this, 0x3E94DC, RedwireType.RED_ALLOY).bind(front()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
        addComponent(new GateComponentBorderDark(this, 0x555555));
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateSingleCounter(this) {

            @Override
            protected int getCurrentAmount() {

                return power;
            }

            @Override
            protected String[] getButtonText() {

                return new String[] { "-100", "-10", "-1", "+1", "+10", "+100" };
            }

            @Override
            protected int[] getButtonActions() {

                return new int[] { -100, -10, -1, 1, 10, 100 };
            }

            @Override
            protected String getDisplayedString() {

                return "" + getCurrentAmount();
            }

            @Override
            protected String getTitle() {

                return "gui.bluepower:regulabletorch";
            }

            @Override
            protected int getMinValue() {

                return 0;
            }

            @Override
            protected int getMaxValue() {

                return 255;
            }

        };
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0) {
            power = value;
            front().setOutput((byte) power);
            torch.setAngle(1 / 8D + (power / 255D) * (6 / 8D));
            sendUpdatePacket();
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, QRayTraceResult mop, ItemStack item) {

        return super.onActivated(player, mop, item);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        tag.setInteger("power", power);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        power = tag.getInteger("power");
        front().setOutput((byte) power);
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        buffer.writeInt(power);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);

        power = buffer.readInt();
    }
}
