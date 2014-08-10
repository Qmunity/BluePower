package com.bluepowermod.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.bluepowermod.containers.ContainerCircuitTable;
import com.bluepowermod.containers.slots.SlotCircuitTableCrafting;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageUpdateTextfield;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCircuitTable extends GuiBase {
    
    private static final ResourceLocation guiTexture    = new ResourceLocation(Refs.MODID, "textures/gui/circuit_table.png");
    private static final ResourceLocation scrollTexture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
    private final TileCircuitTable        circuitTable;
    
    /** Amount scrolled in Creative mode inventory (0 = top, 1 = bottom) */
    private float                         currentScroll;
    
    /** True if the scrollbar is being dragged */
    private boolean                       isScrolling;
    
    /**
     * True if the left mouse button was held down last time drawScreen was called.
     */
    private boolean                       wasClicking;
    private GuiTextField                  searchField;
    private final boolean                 firstRun      = true;
    private int                           ticksExisted;
    
    /**
     * Used to back up the ContainerSearcher's inventory slots before filling it with the player's inventory slots for
     * the inventory tab.
     */
    //private List backupContainerSlots;
    private boolean                       field_74234_w;
    
    private final boolean[]               displayRed    = new boolean[24];
    
    public GuiCircuitTable(InventoryPlayer invPlayer, TileCircuitTable circuitTable) {
    
        super(circuitTable, new ContainerCircuitTable(invPlayer, circuitTable), guiTexture);
        allowUserInput = true;
        ySize = 224;
        this.circuitTable = circuitTable;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
    
        super.initGui();
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        searchField = new GuiTextField(fontRendererObj, guiLeft + 8, guiTop + 20, 89, fontRendererObj.FONT_HEIGHT);
        searchField.setMaxStringLength(15);
        searchField.setEnableBackgroundDrawing(true);
        searchField.setVisible(true);
        searchField.setTextColor(16777215);
        searchField.setText(circuitTable.getText(0));
        
    }
    
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {
    
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) {
    
        super.mouseClicked(x, y, button);
        searchField.mouseClicked(x, y, button);
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {
    
        if (field_74234_w) {
            field_74234_w = false;
            //  searchField.setText("");
        }
        
        if (par2 == 1)//esc
        {
            super.keyTyped(par1, par2);
        } else {
            if (searchField.textboxKeyTyped(par1, par2)) {
                circuitTable.setText(0, searchField.getText());
                NetworkHandler.sendToServer(new MessageUpdateTextfield(circuitTable, 0));
            } else {
                super.keyTyped(par1, par2);
            }
        }
    }
    
    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    
        mc.getTextureManager().bindTexture(guiTexture);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        drawTexturedModalRect(xStart, yStart, 0, 0, xSize, ySize);
        searchField.drawTextBox();
        
        int i1 = guiLeft + 156;
        int k = guiTop + 48;
        int l = k + 112;
        mc.getTextureManager().bindTexture(scrollTexture);
        //drawTexturedModalRect(i1, k + (int) ((l - k - 17) * currentScroll), 232 + (needsScrollBars() ? 0 : 12), 0, 12, 15);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if (displayRed[i * 8 + j]) {
                    drawRect(guiLeft + 8 + j * 18, guiTop + 33 + i * 18, guiLeft + 24 + j * 18, guiTop + 49 + i * 18, 0x55FF0000);
                }
            }
        }
    }
    
    @Override
    public void updateScreen() {
    
        super.updateScreen();
        for (int i = 0; i < 24; i++) {
            displayRed[i] = inventorySlots.getSlot(i).getHasStack() ? !SlotCircuitTableCrafting.canCraft(inventorySlots.getSlot(i).getStack(), circuitTable) : false;
        }
    }
    
}
