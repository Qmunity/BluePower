package com.bluepowermod.client.gui.gate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.gui.widget.BaseWidget;
import uk.co.qmunity.lib.client.gui.widget.IGuiWidget;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.gui.GuiContainerBaseBP;
import com.bluepowermod.container.ContainerFake;
import com.bluepowermod.container.inventory.InventoryFake;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageICPlace;
import com.bluepowermod.part.gate.ic.GateIntegratedCircuit;
import com.bluepowermod.reference.Refs;

public class GuiGateIC extends GuiContainerBaseBP {

    private static ResourceLocation texture = new ResourceLocation(Refs.MODID, "textures/gui/integrated_circuit.png");

    private GateIntegratedCircuit gate;

    private int slot = -1;

    public GuiGateIC(GateIntegratedCircuit gate, EntityPlayer player) {

        super(new InventoryFake("IC"), new ContainerFake(player, 0, 60), texture);
        xSize = 176;
        ySize = 226;

        this.gate = gate;
    }

    @Override
    public void initGui() {

        super.initGui();
    }

    @Override
    public void drawScreen(int mx, int my, float partialTick) {

        super.drawScreen(mx, my, partialTick);

        GL11.glColor4d(1, 1, 1, 1);

        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            GL11.glTranslated(guiLeft + 24, guiTop + 9, 10);

            GL11.glScaled(128, 128, 128);

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(-90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

            Tessellator t = Tessellator.instance;
            RenderHelper rh = RenderHelper.instance;
            rh.fullReset();
            t.startDrawingQuads();
            if (gate.shouldRenderOnPass(0))
                gate.renderStatic_do(new Vec3i(0, 0, 0), rh, 0);
            if (gate.shouldRenderOnPass(1))
                gate.renderStatic_do(new Vec3i(0, 0, 0), rh, 1);
            t.draw();
        }
        GL11.glPopMatrix();
        if (mx >= guiLeft + 24 + 128 * 1 / 16D && my >= guiTop + 9 + 128 * 1 / 16D && mx < guiLeft + 24 + 128 * 15 / 16D
                && my < guiTop + 9 + 128 * 15 / 16D) {
            int size = gate.getSize();
            double cx = (((mx - (guiLeft + 24 + 128 * 1 / 16D)) / 128D) / (14 / 16D)) * size;
            double cz = (((my - (guiTop + 9 + 128 * 1 / 16D)) / 128D) / (14 / 16D)) * size;

            GL11.glPushMatrix();
            {
                GL11.glTranslated(guiLeft + 24, guiTop + 9, 100);

                GL11.glTranslated(128 * 1 / 16D, 128 * 1 / 16D, 128 * 1 / 16D);
                GL11.glScaled((128 / (double) size) * 14 / 16D, (128 / (double) size) * 14 / 16D, (128 / (double) size) * 14 / 16D);

                GL11.glTranslated(cx, cz, 0);

                GL11.glTranslated(0.5, 0.5, 0.5);
                GL11.glRotated(-90, 1, 0, 0);
                GL11.glTranslated(-0.5, -0.5, -0.5);

                EntityPlayer player = Minecraft.getMinecraft().thePlayer;

                World world = player.worldObj;
                double px = player.posX, py = player.posY, pz = player.posZ;
                double lx = player.lastTickPosX, ly = player.lastTickPosY, lz = player.lastTickPosZ;
                float p = player.rotationPitch, y = player.rotationYaw;

                player.worldObj = gate.load();
                player.posX = player.lastTickPosX = cx;
                player.posY = player.lastTickPosY = 67;
                player.posZ = player.lastTickPosZ = cz;
                player.rotationPitch = 90;
                player.rotationYaw = 0;
                MovingObjectPosition mop = new MovingObjectPosition((int) Math.floor(cx), 64, (int) Math.floor(cz), 1, Vec3.createVectorHelper(cx,
                        65, cz));
                MovingObjectPosition original = Minecraft.getMinecraft().objectMouseOver;
                Minecraft.getMinecraft().objectMouseOver = mop;
                try {
                    DrawBlockHighlightEvent event = new DrawBlockHighlightEvent(Minecraft.getMinecraft().renderGlobal, player, mop, 0,
                            ((Slot) inventorySlots.inventorySlots.get((slot + 27) % 36)).getStack(), partialTick);
                    MinecraftForge.EVENT_BUS.post(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Minecraft.getMinecraft().objectMouseOver = original;
                player.worldObj = world;
                player.posX = px;
                player.lastTickPosX = lx;
                player.posY = py;
                player.lastTickPosY = ly;
                player.posZ = pz;
                player.lastTickPosZ = lz;
                player.rotationPitch = p;
                player.rotationYaw = y;
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }

    @Override
    protected void mouseClicked(int mx, int my, int button) {

        for (IGuiWidget widget : widgets)
            if (widget.getBounds().contains(mx, my) && (!(widget instanceof BaseWidget) || ((BaseWidget) widget).enabled))
                widget.onMouseClicked(mx, my, button);

        for (Object o : inventorySlots.inventorySlots) {
            Slot s = (Slot) o;
            if (mx >= guiLeft + s.xDisplayPosition && my >= guiTop + s.yDisplayPosition && mx < guiLeft + s.xDisplayPosition + 16
                    && my < guiTop + s.yDisplayPosition + 16) {
                slot = s.slotNumber < 27 ? s.slotNumber + 9 : s.slotNumber - 27;
            }
        }

        if (slot == -1)
            return;

        int size = gate.getSize();

        if (mx >= guiLeft + 24 + 128 * 1 / 16D && my >= guiTop + 9 + 128 * 1 / 16D && mx < guiLeft + 24 + 128 * 15 / 16D
                && my < guiTop + 9 + 128 * 15 / 16D) {
            double cx = (((mx - (guiLeft + 24 + 128 * 1 / 16D)) / 128D) / (14 / 16D)) * size;
            double cz = (((my - (guiTop + 9 + 128 * 1 / 16D)) / 128D) / (14 / 16D)) * size;
            BPNetworkHandler.INSTANCE.sendToServer(new MessageICPlace(gate, slot, cx, cz));
        }
    }

}
