package com.bluepowermod.client.gui.gate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.gui.widget.WidgetMode;
import uk.co.qmunity.lib.util.AlphanumComparator;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageWirelessNewFreq;
import com.bluepowermod.network.message.MessageWirelessRemoveFreq;
import com.bluepowermod.network.message.MessageWirelessSaveFreq;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.wireless.Frequency;
import com.bluepowermod.part.gate.wireless.IWirelessGate;
import com.bluepowermod.part.gate.wireless.WirelessManager;
import com.bluepowermod.part.gate.wireless.WirelessMode;
import com.bluepowermod.reference.Refs;

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

    private Frequency selected = null;

    private String filter = "";

    private int scrolled = 0;

    public GuiGateWireless(GateBase<?, ?, ?, ?, ?, ?> gate, boolean bundled, WirelessMode mode) {

        super(gate, 228, 184);
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
        Mouse.getDWheel();

        addWidget(accessLevel = new WidgetMode(0, guiLeft + 10, guiTop + ySize - 24, 228, 0, Accessibility.values().length, Refs.MODID
                + ":textures/gui/wirelessRedstone.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add((accessLevel.enabled ? "" : EnumChatFormatting.GRAY) + "Accessability");
            }
        });

        addWidget(filterAccessLevel = new WidgetMode(1, guiLeft + 12, guiTop + 35, 228, 0, Accessibility.values().length + 2, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        filterAccessLevel.value = 4;

        addWidget(addFrequency = new WidgetMode(3, guiLeft + 10, guiTop + ySize - 24 - 14 - 3, 228 + 14, 0, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add((addFrequency.enabled ? "" : EnumChatFormatting.GRAY) + "Add frequency");
            }
        });
        addWidget(saveFrequency = new WidgetMode(4, guiLeft + 37, guiTop + ySize - 24 - 14 - 3, 228 + 14, 28, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add((saveFrequency.enabled ? "" : EnumChatFormatting.GRAY) + "Save changes");
            }
        });
        addWidget(removeFrequency = new WidgetMode(5, guiLeft + 88 - 10 - 14, guiTop + ySize - 24 - 14 - 3, 228 + 14, 14, 1, Refs.MODID
                + ":textures/gui/wirelessRedstone.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                if (gate.getFrequency() != null && (selected == null || selected.equals(gate.getFrequency()))) {
                    curTip.add("Unselect frequency");
                } else if (selected != null) {
                    curTip.add("Remove frequency");
                } else {
                    curTip.add(EnumChatFormatting.GRAY + "Select a frequency");
                }
            }
        });

        addWidget(modeSelector = new WidgetMode(6, guiLeft + 10, guiTop + 57, 228, (14 * 5), 3, Refs.MODID
                + ":textures/gui/wirelessRedstone.png"));
        modeSelector.value = gate.getMode().ordinal();

        frequencyName = new GuiTextField(fontRendererObj, guiLeft + 88, guiTop + 22, 133, 10);

        accessLevel.enabled = false;
        saveFrequency.enabled = false;
        addFrequency.enabled = false;
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char c, int key) {

        super.keyTyped(c, key);
        if (c == 13) {
            actionPerformed(addFrequency);
            return;
        }

        frequencyName.textboxKeyTyped(c, key);
        if (selected == null) {
            filter = frequencyName.getText().trim();
            fixScrollWheel();
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);

        frequencyName.mouseClicked(x, y, button);
        if (x > frequencyName.xPosition && x < frequencyName.xPosition + frequencyName.width && y > frequencyName.yPosition
                && y < frequencyName.yPosition + frequencyName.height && button == 1) {
            frequencyName.setText("");
            if (selected == null)
                filter = "";
        }

        List<Frequency> frequencies = getFrequencies();
        if (x > guiLeft + 88 && x <= guiLeft + 88 + 133 - (frequencies.size() > 12 ? 11 : 0)) {
            for (int i = 0; i < Math.min(frequencies.size(), 12); i++) {
                Frequency f = frequencies.get(i + scrolled);
                if (f.isBundled() == gate.isBundled()) {
                    int yPos = guiTop + 22 + 10 + 2 + (i * 12);
                    if (y > yPos && y < yPos + 11) {
                        if (button == 0) {
                            if (f.equals(selected)) {
                                BPNetworkHandler.INSTANCE.sendToServer(new MessageWirelessNewFreq(gate, f.getAccessibility(), f
                                        .getFrequencyName(), f.isBundled()));
                            } else {
                                selected = f;
                                frequencyName.setText(f.getFrequencyName());
                                accessLevel.value = (acc = f.getAccessibility()).ordinal();
                            }
                        } else if (button == 1) {
                            selected = null;
                            frequencyName.setText(filter);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void handleMouseInput() {

        super.handleMouseInput();

        int scrolled = Mouse.getEventDWheel();
        if (scrolled != 0) {
            if (scrolled < 0) {
                this.scrolled++;
            } else {
                this.scrolled--;
            }
            fixScrollWheel();
        }
    }

    private void fixScrollWheel() {

        scrolled = Math.max(Math.min(scrolled, getFrequencies().size() - 12), 0);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        super.actionPerformed(widget);

        if (widget == filterAccessLevel) {
            if (filterAccessLevel.value == 3 && !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
                filterAccessLevel.value++;
            }
            if (selected != null && !selected.equals(gate.getFrequency())) {
                frequencyName.setText("");
                filter = "";
            }
            selected = null;
        }

        if (widget == modeSelector)
            sendToServer(0, modeSelector.value);

        if (widget == addFrequency)
            BPNetworkHandler.INSTANCE.sendToServer(new MessageWirelessNewFreq(gate, acc, frequencyName.getText().trim(), gate.isBundled()));

        if (widget == saveFrequency) {
            BPNetworkHandler.INSTANCE.sendToServer(new MessageWirelessSaveFreq(selected, acc, frequencyName.getText().trim()));
            filter = "";
        }

        if (widget == removeFrequency) {
            if (gate.getFrequency() != null && (selected == null || selected.equals(gate.getFrequency()))) {
                sendToServer(1, 0);
            } else if (selected != null) {
                BPNetworkHandler.INSTANCE.sendToServer(new MessageWirelessRemoveFreq(selected));
                selected = null;
            }
        }

        if (widget == accessLevel) {
            acc = Accessibility.values()[accessLevel.value];
        }
    }

    @Override
    protected void renderGUI(int x, int y, float partialTick) {

        super.renderGUI(x, y, partialTick);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        // Enable/disable components depending on our needs
        {
            String txt = frequencyName.getText();

            removeFrequency.enabled = (gate.getFrequency() != null && (gate.getFrequency().getOwner()
                    .equals(player.getGameProfile().getId()) || player.capabilities.isCreativeMode))
                    || (selected != null && (selected.getOwner().equals(player.getGameProfile().getId()) || player.capabilities.isCreativeMode));

            accessLevel.enabled = selected != null || (txt.trim().length() > 0 && checkNoMatches());

            saveFrequency.enabled = selected != null
                    && (acc != selected.getAccessibility() || (txt.trim().length() > 0 && !txt.trim().equals(selected.getFrequencyName())));

            addFrequency.enabled = selected == null && txt.trim().length() > 0 && checkNoMatches();
        }

        // Render title
        drawCenteredString(fontRendererObj, I18n.format("bluepower.gui.wireless"), guiLeft + (xSize / 2), guiTop + 8, 0xEFEFEF);

        // Filter
        {
            drawCenteredString(fontRendererObj, "Filter", guiLeft + 45, guiTop + 22 + 3, 0xEFEFEF);
            String accessLevelLabel = filterAccessLevel.value == 0 ? "bluepower.accessability.public"
                    : (filterAccessLevel.value == 1 ? "bluepower.accessability.shared"
                            : (filterAccessLevel.value == 2 ? "bluepower.accessability.private"
                                    : (filterAccessLevel.value == 3 ? "bluepower.gui.admin" : "bluepower.gui.none")));
            drawString(fontRendererObj, I18n.format(accessLevelLabel), guiLeft + 12 + 14 + 3, guiTop + 35 + 3,
                    filterAccessLevel.enabled ? 0xEFEFEF : 0x565656);
        }

        // Label for the access level
        String accessLevelLabel = accessLevel.value == 0 ? "bluepower.accessability.public"
                : (accessLevel.value == 1 ? "bluepower.accessability.shared" : "bluepower.accessability.private");
        drawString(fontRendererObj, I18n.format(accessLevelLabel), guiLeft + 10 + 14 + 3, guiTop + ySize - 24 + 3,
                accessLevel.enabled ? 0xEFEFEF : 0x565656);

        // Label for the mode
        String modeLabel = modeSelector.value == 0 ? "bluepower.mode.sendreceive" : (modeSelector.value == 1 ? "bluepower.mode.send"
                : "bluepower.mode.receive");
        drawString(fontRendererObj, I18n.format(modeLabel), guiLeft + 10 + 14 + 3, guiTop + 57 + 3, 0xEFEFEF);

        // Render the textbox
        frequencyName.drawTextBox();

        // Get all frequencies and sort them
        List<Frequency> frequencies = getFrequencies();

        // Render the list
        for (int i = 0; i < Math.min(frequencies.size(), 12); i++) {
            Frequency f = frequencies.get(i + scrolled);
            int yPos = guiTop + 22 + 10 + 2 + (i * 12);
            int color = f.equals(gate.getFrequency()) ? 0x00CCCC : (f.equals(selected) ? 0x888888 : ((x > guiLeft + 88
                    && x <= guiLeft + 88 + 133 - (frequencies.size() > 12 ? 11 : 0) && y > yPos && y <= yPos + 11 && f.isBundled() == gate
                    .isBundled()) ? 0xAAAAAA : 0x333333));
            int textColor = f.equals(gate.getFrequency()) ? 0x333333 : (f.isBundled() != gate.isBundled() ? 0x999999 : 0xFFFFFF);
            drawRect(guiLeft + 88, yPos, guiLeft + 88 + 133 - (frequencies.size() > 12 ? 11 : 0), yPos + 11, (0xFF << 24) + color);

            String format = f.isBundled() != gate.isBundled() ? EnumChatFormatting.STRIKETHROUGH.toString() : "";
            String txt = format
                    + f.getFrequencyName()
                    + ((filterAccessLevel.value == 3 || filterAccessLevel.value == 4) ? " ["
                            + StringUtils.capitalize(f.getAccessibility().name().toLowerCase()) + "]" : "");
            fontRendererObj.drawString(txt, guiLeft + 88 + 2 + 12 + 2, yPos + 2, textColor, !f.equals(gate.getFrequency()));

            ItemStack item = PartManager.getPartInfo("wire.bluestone" + (f.isBundled() ? ".bundled" : "")).getStack();
            GL11.glPushMatrix();
            {
                if (f.isBundled() != gate.isBundled())
                    GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glTranslated(guiLeft + 88 + 1, yPos - 2, 0);
                GL11.glScaled(0.75, 0.75, 0.75);
                ForgeHooksClient
                .renderInventoryItem(RenderBlocks.getInstance(), Minecraft.getMinecraft().renderEngine, item, true, 1, 1, 1);
                if (f.isBundled() != gate.isBundled())
                    GL11.glDisable(GL11.GL_LIGHTING);
            }
            GL11.glPopMatrix();
        }
        if (frequencies.size() > 12) {
            drawRect(guiLeft + 88 + 133 - 10, guiTop + 22 + 10 + 2, guiLeft + 88 + 133, guiTop + 22 + 10 + 1 + (12 * 12), 0xFF565656);
        }

        for (int i = 0; i < Math.min(frequencies.size(), 12); i++) {
            Frequency f = frequencies.get(i + scrolled);
            int yPos = guiTop + 22 + 10 + 2 + (i * 12);
            if (x > guiLeft + 88 && x <= guiLeft + 88 + 133 - (frequencies.size() > 12 ? 11 : 0) && y > yPos && y <= yPos + 11
                    && f.isBundled() == gate.isBundled()) {
                func_146283_a(
                        Arrays.asList(
                                "Frequency: " + f.getFrequencyName(),
                                EnumChatFormatting.GRAY + "Accessibility: "
                                        + StringUtils.capitalize(f.getAccessibility().name().toLowerCase()), EnumChatFormatting.GRAY
                                        + "Owner: " + f.getOwnerName(), "Devices: " + f.getDevices()), x, y);
            }
        }
    }

    private boolean checkNoMatches() {

        for (IFrequency f : WirelessManager.CLIENT_INSTANCE.getFrequencies())
            if (f.getAccessibility() == acc && f.getFrequencyName().toLowerCase().equals(frequencyName.getText().trim().toLowerCase()))
                return false;

        return true;
    }

    private List<Frequency> getFrequencies() {

        List<Frequency> frequencies = new ArrayList<Frequency>();
        for (IFrequency f : WirelessManager.CLIENT_INSTANCE.getFrequencies()) {
            if (f.getAccessibility().ordinal() == filterAccessLevel.value || filterAccessLevel.value == 3 || filterAccessLevel.value == 4)
                if (f.getFrequencyName().toLowerCase().contains(filter.toLowerCase()))
                    frequencies.add((Frequency) f);
        }
        Collections.sort(frequencies, new FrequencySorter(this));

        return frequencies;
    }

    private static class FrequencySorter implements Comparator<Frequency> {

        private GuiGateWireless gui;

        public FrequencySorter(GuiGateWireless gui) {

            this.gui = gui;
        }

        @Override
        public int compare(Frequency o1, Frequency o2) {

            // if (o1.equals(gui.gate.getFrequency()))
            // return Integer.MIN_VALUE;
            // if (o2.equals(gui.gate.getFrequency()))
            // return Integer.MAX_VALUE;

            if (o1.isBundled() != o2.isBundled()) {
                if (o1.isBundled() == gui.gate.isBundled())
                    return Integer.MIN_VALUE;
                if (o2.isBundled() == gui.gate.isBundled())
                    return Integer.MAX_VALUE;
            }
            if (o1.getAccessibility() != o2.getAccessibility())
                return o1.getAccessibility().compareTo(o2.getAccessibility());

            return new AlphanumComparator().compare(o1.getFrequencyName(), o2.getFrequencyName());
        }

    }
}
