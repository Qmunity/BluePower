/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.event;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.client.gui.GuiCircuitDatabaseSharing;
import com.bluepowermod.container.ContainerSeedBag;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemSeedBag;
import com.bluepowermod.item.ItemSickle;
import com.bluepowermod.util.MultipartUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class BPEventHandler {

    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.level.getGameTime() % 200 == 0) {
                //double tickTime = MathHelper.mean(event.world.getServer().tickTimeArray) * 1.0E-6D;
                //In case world are going to get their own thread: MinecraftServer.getServer().worldTickTimes.get(event.world.provider.dimensionId)
                //BPNetworkHandler.wrapper.send(PacketDistributor.DIMENSION.setValue(event.world.getDimension().getType()), new MessageServerTickTime(tickTime));
            }
        }
    }

    @SubscribeEvent
    public void onAnvilEvent(AnvilUpdateEvent event) {

        if (!event.getLeft().isEmpty() && event.getLeft().getItem() == BPItems.screwdriver.get()) {
            if (!event.getRight().isEmpty() && event.getRight().getItem() == Items.ENCHANTED_BOOK) {
                if (EnchantmentHelper.getEnchantments(event.getRight()).get(Enchantments.SILK_TOUCH) != null) {
                    event.setOutput(new ItemStack(BPItems.silky_screwdriver.get(), 1));
                    event.setCost(20);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        if (event.getEntity().isCreative()) {
            ItemStack heldItem = event.getEntity().getItemInHand(event.getHand());
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSickle) {
                heldItem.getItem().mineBlock(heldItem, event.getLevel(), event.getLevel().getBlockState(event.getPos()), event.getPos(), event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public void itemPickUp(EntityItemPickupEvent event) {

        Player player = event.getEntity();
        ItemStack pickUp = event.getItem().getItem();
        if (!(player.containerMenu instanceof ContainerSeedBag)) {
            for (ItemStack is : player.getInventory().items) {
                if (!is.isEmpty() && is.getItem() instanceof ItemSeedBag) {
                    ItemStack seedType = ItemSeedBag.getSeedType(is);
                    if (!seedType.isEmpty() && seedType.sameItem(pickUp)) {
                        ItemStackHandler seedBagInvHandler = new ItemStackHandler(9);

                        //Get Items from the NBT Handler
                        if (is.hasTag()) seedBagInvHandler.deserializeNBT(is.getTag().getCompound("inv"));

                        //Attempt to insert items
                        for(int j = 0; j < 9 && !pickUp.isEmpty(); ++j) {
                            pickUp = seedBagInvHandler.insertItem(j, pickUp, false);
                        }

                        //Update items in the NBT
                        if (!is.hasTag())
                            is.setTag(new CompoundTag());
                        if (is.getTag() != null) {
                            is.getTag().put("inv", seedBagInvHandler.serializeNBT());
                        }

                        //Pickup Leftovers
                        if (pickUp.isEmpty()) {
                            event.setResult(Event.Result.ALLOW);
                            event.getItem().remove(Entity.RemovalReason.DISCARDED);
                            return;
                        } else {
                            event.getItem().setItem(pickUp);
                        }
                    }
                }
            }
        }
    }

    private boolean isAttacking = false;

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event) {

        if (!isAttacking && event.getSource() instanceof EntityDamageSource entitySource) {// this event will be trigger recursively by EntityLiving#hurt,
            // so we need to stop the loop.

            if (entitySource.getEntity() instanceof Player killer) {

                if (!killer.getInventory().getSelected().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.getInventory().getSelected()).containsKey(BPEnchantments.disjunction)) {
                        if (event.getEntity() instanceof EnderMan || event.getEntity() instanceof EnderDragon) {
                            int level = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.disjunction.get(), killer.getInventory().getSelected());
                            isAttacking = true;
                            event.getEntity().hurt(entitySource, event.getAmount() * (level * 0.5F + 1));
                            isAttacking = false;
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

            if (entitySource.getEntity() instanceof Player) {
                Player killer = (Player) entitySource.getEntity();

                if (!killer.getInventory().getSelected().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.getInventory().getSelected()).containsKey(BPEnchantments.vorpal)) {
                        int level = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.vorpal.get(), killer.getInventory().getSelected());

                        if (level == 1) {
                            if (killer.level.random.nextInt(6) == 1) {
                                dropHeads(event);
                            }
                        } else if (level == 2) {
                            if (killer.level.random.nextInt(3) == 1) {
                                dropHeads(event);
                            }
                        }
                    }
                }
            }
        }
    }

    private void dropHeads(LivingDeathEvent event) {

        if (event.getEntity() instanceof Creeper) {
            event.getEntity().spawnAtLocation(new ItemStack(Items.CREEPER_HEAD, 1), 0.0F);
        }

        if (event.getEntity() instanceof Player) {
            ItemStack drop = new ItemStack(Items.PLAYER_HEAD, 1);
            drop.setTag(new CompoundTag());
            drop.getTag().putString("SkullOwner", event.getEntity().getDisplayName().getString());
            event.getEntity().spawnAtLocation(drop, 0.0F);
        }

        if (event.getEntity() instanceof AbstractSkeleton) {
            AbstractSkeleton sk = (AbstractSkeleton) event.getEntity();

            if (sk instanceof Skeleton) {
                event.getEntity().spawnAtLocation(new ItemStack(Items.SKELETON_SKULL, 1), 0.0F);
            } else {
                event.getEntity().spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 0.0F);
            }
        }

        if (event.getEntity() instanceof Zombie) {
            event.getEntity().spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD, 1), 0.0F);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onItemTooltip(ItemTooltipEvent event) {

        if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("tileData")
                && !event.getItemStack().getTag().getBoolean("hideSilkyTooltip")) {
            event.getToolTip().add(Component.literal("gui.tooltip.hasSilkyData"));
        }

        if (ClientProxy.getOpenedGui() instanceof GuiCircuitDatabaseSharing) {
            ItemStack deletingStack = ((GuiCircuitDatabaseSharing) ClientProxy.getOpenedGui()).getCurrentDeletingTemplate();
            if (!deletingStack.isEmpty() && deletingStack == event.getItemStack()) {
                event.getToolTip().add(Component.literal("gui.circuitDatabase.info.sneakClickToConfirmDeleting"));
            } else {
                event.getToolTip().add(Component.literal("gui.circuitDatabase.info.sneakClickToDelete"));
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {

        Item item = event.getCrafting().getItem();
        if (item == Item.byBlock(Blocks.AIR))
            return;
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {

        if (!event.getLevel().isClientSide) {
            if (event.getBlock().getBlock() instanceof GrassBlock) {
                for (int x = event.getPos().getX() - 2; x < event.getPos().getX() + 3; x++) {
                    for (int z = event.getPos().getZ() - 2; z < event.getPos().getZ() + 3; z++) {
                        if (event.getLevel().isEmptyBlock(new BlockPos(x, event.getPos().getY() + 1, z))) {
                            if (event.getLevel().random.nextInt(50) == 1) {
                                if (BPBlocks.indigo_flower.get().canSustainPlant(event.getLevel().getBlockState(event.getPos().above()), event.getLevel(), event.getPos().above(), Direction.UP, BPBlocks.indigo_flower.get())) {
                                    event.getLevel().setBlock(event.getPos().above(), BPBlocks.indigo_flower.get().defaultBlockState(), 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void blockHighlightEvent(RenderHighlightEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        Level world = player.level;
        HitResult mop = event.getTarget();
        if(mop instanceof BlockHitResult) {
            BlockPos pos = ((BlockHitResult) mop).getBlockPos();
            BlockState state = world.getBlockState(pos);
            if(state.getBlock() instanceof BlockBPMultipart){
                BlockState partstate = MultipartUtils.getClosestState(player, pos);
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.lines());
                if(partstate != null) {
                    VoxelShape shape = partstate.getShape(world, pos, CollisionContext.of(player));
                    Vec3 projectedView = event.getCamera().getPosition();
                    double d0 = pos.getX() - projectedView.x();
                    double d1 = pos.getY() - projectedView.y();
                    double d2 = pos.getZ() - projectedView.z();
                    Matrix4f matrix4f = event.getPoseStack().last().pose();
                    //shape.forAllEdges((startX, startY, startZ, endX, endY, endZ) -> {
                    //    builder.vertex(matrix4f, (float)(startX + d0), (float)(startY + d1), (float)(startZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                    //    builder.vertex(matrix4f, (float)(endX + d0), (float)(endY + d1), (float)(endZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                    //});
                    event.setCanceled(true);
                }
            }
        }
    }
}
