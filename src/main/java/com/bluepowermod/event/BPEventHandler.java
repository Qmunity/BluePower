/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.event;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.client.gui.GuiCircuitDatabaseSharing;
import com.bluepowermod.container.ContainerSeedBag;
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemSeedBag;
import com.bluepowermod.item.ItemSickle;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageServerTickTime;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class BPEventHandler {

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.getGameTime() % 200 == 0) {
                double tickTime = MathHelper.mean(event.world.getServer().tickTimeArray) * 1.0E-6D;//In case world are going to get their own thread: MinecraftServer.getServer().worldTickTimes.get(event.world.provider.dimensionId)
                //TODO: Network Handler
                //BPNetworkHandler.INSTANCE.sendToDimension(new MessageServerTickTime(tickTime), event.world.getDimension());
            }
        }
    }

    @SubscribeEvent
    public void onAnvilEvent(AnvilUpdateEvent event) {

        if (!event.getLeft().isEmpty() && event.getLeft().getItem() == BPItems.screwdriver) {
            if (!event.getRight().isEmpty() && event.getRight().getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.getEnchantments(event.getRight()).get(Enchantments.SILK_TOUCH) != null) {
                    event.setOutput(new ItemStack(BPItems.silky_screwdriver, 1));
                    event.setCost(20);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        if (event.getEntityPlayer().isCreative()) {
            ItemStack heldItem = event.getEntityPlayer().getHeldItem(event.getHand());
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSickle) {
                heldItem.getItem().onBlockDestroyed(heldItem, event.getWorld(), event.getWorld().getBlockState(event.getPos()), event.getPos(), event.getEntityPlayer());
            }
        }
    }

    @SubscribeEvent
    public void itemPickUp(EntityItemPickupEvent event) {

        PlayerEntity player = event.getEntityPlayer();
        ItemStack pickUp = event.getItem().getItem();
        if (!(player.openContainer instanceof ContainerSeedBag)) {
            for (ItemStack is : player.inventory.mainInventory) {
                if (!is.isEmpty() && is.getItem() instanceof ItemSeedBag) {
                    ItemStack seedType = ItemSeedBag.getSeedType(is);
                    if (!seedType.isEmpty() && seedType.isItemEqual(pickUp)) {
                        InventoryItem inventory = InventoryItem.getItemInventory(is, "Seed Bag", 9);
                        inventory.openInventory(event.getEntityPlayer());
                        ItemStack pickedUp = HopperTileEntity.putStackInInventoryAllSlots(null, inventory, pickUp, null);
                        inventory.closeInventory(is);

                        if (pickedUp.isEmpty()) {
                            event.setResult(Event.Result.ALLOW);
                            event.getItem().remove();
                            return;
                        } else {
                            event.getItem().setItem(pickedUp);
                        }
                    }
                }
            }
        }
    }

    private boolean isAttacking = false;

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event) {

        if (!isAttacking && event.getSource() instanceof EntityDamageSource) {// this event will be trigger recursively by EntityLiving#attackEntityFrom,
            // so we need to stop the loop.
            EntityDamageSource entitySource = (EntityDamageSource) event.getSource();

            if (entitySource.getTrueSource() instanceof PlayerEntity) {
                PlayerEntity killer = (PlayerEntity) entitySource.getTrueSource();

                if (!killer.inventory.getCurrentItem().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.disjunction)) {
                        if (event.getEntityLiving() instanceof EndermanEntity || event.getEntityLiving() instanceof EnderDragonEntity) {
                            int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.disjunction, killer.inventory.getCurrentItem());
                            isAttacking = true;
                            event.getEntityLiving().attackEntityFrom(event.getSource(), event.getAmount() * (level * 0.5F + 1));
                            isAttacking = false;
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {

        if (event.getSource() instanceof EntityDamageSource) {
            EntityDamageSource entitySource = (EntityDamageSource) event.getSource();

            if (entitySource.getTrueSource() instanceof PlayerEntity) {
                PlayerEntity killer = (PlayerEntity) entitySource.getTrueSource();

                if (!killer.inventory.getCurrentItem().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getCurrentItem()).containsKey(BPEnchantments.vorpal)) {
                        int level = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.vorpal, killer.inventory.getCurrentItem());

                        if (level == 1) {
                            if (killer.world.rand.nextInt(6) == 1) {
                                dropHeads(event);
                            }
                        } else if (level == 2) {
                            if (killer.world.rand.nextInt(3) == 1) {
                                dropHeads(event);
                            }
                        }
                    }
                }
            }
        }
    }

    private void dropHeads(LivingDeathEvent event) {

        if (event.getEntityLiving() instanceof CreeperEntity) {
            event.getEntityLiving().entityDropItem(new ItemStack(Items.CREEPER_HEAD, 1), 0.0F);
        }

        if (event.getEntityLiving() instanceof PlayerEntity) {
            ItemStack drop = new ItemStack(Items.PLAYER_HEAD, 1);
            drop.setTag(new CompoundNBT());
            drop.getTag().putString("SkullOwner", ((PlayerEntity) event.getEntityLiving()).getDisplayName().getFormattedText());
            event.getEntityLiving().entityDropItem(drop, 0.0F);
        }

        if (event.getEntityLiving() instanceof AbstractSkeletonEntity) {
            AbstractSkeletonEntity sk = (AbstractSkeletonEntity) event.getEntityLiving();

            if (sk instanceof SkeletonEntity) {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.SKELETON_SKULL, 1), 0.0F);
            } else {
                event.getEntityLiving().entityDropItem(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 0.0F);
            }
        }

        if (event.getEntityLiving() instanceof ZombieEntity) {
            event.getEntityLiving().entityDropItem(new ItemStack(Items.ZOMBIE_HEAD, 1), 0.0F);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {

        if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("tileData")
                && !event.getItemStack().getTag().getBoolean("hideSilkyTooltip")) {
            event.getToolTip().add(new StringTextComponent("gui.tooltip.hasSilkyData"));
        }

        if (ClientProxy.getOpenedGui() instanceof GuiCircuitDatabaseSharing) {
            ItemStack deletingStack = ((GuiCircuitDatabaseSharing) ClientProxy.getOpenedGui()).getCurrentDeletingTemplate();
            if (!deletingStack.isEmpty() && deletingStack == event.getItemStack()) {
                event.getToolTip().add(new StringTextComponent("gui.circuitDatabase.info.sneakClickToConfirmDeleting"));
            } else {
                event.getToolTip().add(new StringTextComponent("gui.circuitDatabase.info.sneakClickToDelete"));
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {

        Item item = event.getCrafting().getItem();
        if (item == Item.getItemFromBlock(Blocks.AIR))
            return;
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {

        if (!event.getWorld().isRemote) {
            if (event.getBlock().getBlock() instanceof GrassBlock) {
                for (int x = event.getPos().getX() - 2; x < event.getPos().getX() + 3; x++) {
                    for (int z = event.getPos().getZ() - 2; z < event.getPos().getZ() + 3; z++) {
                        if (event.getWorld().isAirBlock(new BlockPos(x, event.getPos().getY() + 1, z))) {
                            if (event.getWorld().rand.nextInt(50) == 1) {
                                //TODO: Flower Chance
                                //if (BPBlocks.indigo_flower.canSustainPlant(event.getWorld().getBlockState(event.getPos().up()), event.getWorld(), event.getPos().up())) {
                                //    event.getWorld().setBlockState(event.getPos().up(), BPBlocks.indigo_flower.getDefaultState());
                                //}
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void blockHighlightEvent(DrawBlockHighlightEvent event) {
        RayTraceResult mop = event.getTarget();
        Block block = Block.getBlockFromItem(Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND).getItem());
        if(block instanceof BlockGateBase && mop.getType() == RayTraceResult.Type.BLOCK){
            BlockPos position = ((BlockRayTraceResult)mop).getPos().offset(((BlockRayTraceResult)mop).getFace());
            Entity entity = Minecraft.getInstance().getRenderViewEntity();
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks();
            double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks()) + entity.getEyeHeight();
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            vertexbuffer.setTranslation(-d0, -d1, -d2 );
            GlStateManager.pushMatrix();
            GlStateManager.enableAlphaTest();
            position.add(0.5, 0.1, 0.5);
            vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            BlockState state = block.getDefaultState().with(BlockGateBase.FACING, ((BlockRayTraceResult)mop).getFace());
            IBakedModel ibakedmodel = blockrendererdispatcher.getModelForState(state);
            blockrendererdispatcher.getBlockModelRenderer().renderModel(Minecraft.getInstance().world, ibakedmodel, state, position, vertexbuffer, false, new Random(), 0);
            tessellator.draw();
            GlStateManager.popMatrix();
            vertexbuffer.setTranslation(0, 0, 0);
        }
    }

}
