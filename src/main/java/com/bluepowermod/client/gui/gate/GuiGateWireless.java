package com.bluepowermod.client.gui.gate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.api.wireless.IWirelessDevice;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageWirelessNewFreq;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.wireless.Frequency;
import com.bluepowermod.part.gate.wireless.IWirelessGate;
import com.bluepowermod.part.gate.wireless.WirelessMode;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGateWireless extends GuiGate {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/wirelessRedstone.png");

    private WidgetMode filterAccessLevel;

    private WidgetMode accessLevel;

    private WidgetMode addFrequency;
    private WidgetMode removeFrequency;
    private WidgetMode saveFrequency;

    private WidgetMode modeSelector;

    private GuiTextField frequencyName;
    private Accessibility acc = Accessibility.PUBLIC;

    private IWirelessGate gate;

    private int selected = 0;

    public GuiGateWireless(GateBase gate, boolean bundled, WirelessMode mode) {

        super(gate, 228, 181);
        this.gate = (IWirelessGate) gate;
    }

    @Override
    protected ResourceLocation getTexture() {

        return resLoc;
    }

    @Override
    public void initGui() {

        super.initGui();
        Keyboard.enableRepeatEvents(true);

        addWidget(accessLevel = new WidgetMode(0, guiLeft + 10, guiTop + ySize - 24, 228, 0, Accessibility.values().length, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));

        addWidget(filterAccessLevel = new WidgetMode(1, guiLeft + 12, guiTop + 35, 228, 0, Accessibility.values().length + 2, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        filterAccessLevel.value = 4;

        addWidget(addFrequency = new WidgetMode(3, guiLeft + 10, guiTop + ySize - 24 - 14 - 3, 228 + 14, 0, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        addWidget(saveFrequency = new WidgetMode(4, guiLeft + 37, guiTop + ySize - 24 - 14 - 3, 228 + 14, 28, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        addWidget(removeFrequency = new WidgetMode(5, guiLeft + 88 - 10 - 14, guiTop + ySize - 24 - 14 - 3, 228 + 14, 14, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));

        addWidget(modeSelector = new WidgetMode(6, guiLeft + 10, guiTop + 57, 228, (14 * 5), 3, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        modeSelector.value = gate.getMode().ordinal();

        frequencyName = new GuiTextField(fontRendererObj, guiLeft + 88, guiTop + 22, 133, 10);

        if (gate.getFrequency() == null) {
            accessLevel.enabled = false;
            saveFrequency.enabled = false;
            removeFrequency.enabled = false;
        }

        addFrequency.enabled = false;
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {

        super.keyTyped(p_73869_1_, p_73869_2_);

        frequencyName.textboxKeyTyped(p_73869_1_, p_73869_2_);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);

        frequencyName.mouseClicked(x, y, button);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        super.actionPerformed(widget);

        IWirelessDevice dev = ((IWirelessDevice) getGate());
        Frequency f = (Frequency) dev.getFrequency();

        if (widget == filterAccessLevel) {
            if (filterAccessLevel.value == 3 && !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
                filterAccessLevel.value++;
            }

            // TODO: Add list with all the available frequencies
        }

        if (widget == modeSelector)
            sendToServer(0, modeSelector.value);

        if (widget == addFrequency)
            NetworkHandler.sendToServer(new MessageWirelessNewFreq(gate, acc, frequencyName.getText().trim(), gate.isBundled()));

        if (widget == removeFrequency)
            sendToServer(1, selected);

        if (widget == accessLevel) {
            acc = Accessibility.values()[accessLevel.value];
        }
    }

    @Override
    protected void renderGUI(int x, int y, float partialTick) {

        super.renderGUI(x, y, partialTick);

        String txt = frequencyName.getText().trim();
        accessLevel.enabled = addFrequency.enabled = !(txt.length() == 0 || (gate.getFrequency() != null && gate.getFrequency()
                .getFrequencyName().equals(txt)));

        removeFrequency.enabled = gate.getFrequency() != null;

        drawCenteredString(fontRendererObj, I18n.format("bluepower.gui.wireless"), guiLeft + (xSize / 2), guiTop + 8, 0xEFEFEF);

        {
            drawCenteredString(fontRendererObj, "Filter", guiLeft + 45, guiTop + 22 + 3, 0xEFEFEF);
            String accessLevelLabel = filterAccessLevel.value == 0 ? "bluepower.accessability.public"
                    : (filterAccessLevel.value == 1 ? "bluepower.accessability.shared"
                            : (filterAccessLevel.value == 2 ? "bluepower.accessability.private"
                                    : (filterAccessLevel.value == 3 ? "bluepower.gui.admin" : "bluepower.gui.none")));
            drawString(fontRendererObj, I18n.format(accessLevelLabel), guiLeft + 12 + 14 + 3, guiTop + 35 + 3,
                    filterAccessLevel.enabled ? 0xEFEFEF : 0x565656);
        }

        String accessLevelLabel = accessLevel.value == 0 ? "bluepower.accessability.public"
                : (accessLevel.value == 1 ? "bluepower.accessability.shared" : "bluepower.accessability.private");
        drawString(fontRendererObj, I18n.format(accessLevelLabel), guiLeft + 10 + 14 + 3, guiTop + ySize - 24 + 3,
                accessLevel.enabled ? 0xEFEFEF : 0x565656);

        frequencyName.drawTextBox();
    }

}
