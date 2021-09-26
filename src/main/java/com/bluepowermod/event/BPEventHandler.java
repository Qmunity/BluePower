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
import com.bluepowermod.init.BPEnchantments;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemSeedBag;
import com.bluepowermod.item.ItemSickle;
import com.bluepowermod.util.MultipartUtils;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent;
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
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.world.getGameTime() % 200 == 0) {
                //double tickTime = MathHelper.mean(event.world.getServer().tickTimeArray) * 1.0E-6D;
                //In case world are going to get their own thread: MinecraftServer.getServer().worldTickTimes.get(event.world.provider.dimensionId)
                //BPNetworkHandler.wrapper.send(PacketDistributor.DIMENSION.setValue(event.world.getDimension().getType()), new MessageServerTickTime(tickTime));
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

        if (event.getPlayer().isCreative()) {
            ItemStack heldItem = event.getPlayer().getItemInHand(event.getHand());
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSickle) {
                heldItem.getItem().mineBlock(heldItem, event.getWorld(), event.getWorld().getBlockState(event.getPos()), event.getPos(), event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public void itemPickUp(EntityItemPickupEvent event) {

        PlayerEntity player = event.getPlayer();
        ItemStack pickUp = event.getItem().getItem();
        if (!(player.containerMenu instanceof ContainerSeedBag)) {
            for (ItemStack is : player.inventory.items) {
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
                            is.setTag(new CompoundNBT());
                        if (is.getTag() != null) {
                            is.getTag().put("inv", seedBagInvHandler.serializeNBT());
                        }

                        //Pickup Leftovers
                        if (pickUp.isEmpty()) {
                            event.setResult(Event.Result.ALLOW);
                            event.getItem().remove();
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

        if (!isAttacking && event.getSource() instanceof EntityDamageSource) {// this event will be trigger recursively by EntityLiving#hurt,
            // so we need to stop the loop.
            EntityDamageSource entitySource = (EntityDamageSource) event.getSource();

            if (entitySource.getEntity() instanceof PlayerEntity) {
                PlayerEntity killer = (PlayerEntity) entitySource.getEntity();

                if (!killer.inventory.getSelected().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getSelected()).containsKey(BPEnchantments.disjunction)) {
                        if (event.getEntityLiving() instanceof EndermanEntity || event.getEntityLiving() instanceof EnderDragonEntity) {
                            int level = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.disjunction, killer.inventory.getSelected());
                            isAttacking = true;
                            event.getEntityLiving().hurt(event.getSource(), event.getAmount() * (level * 0.5F + 1));
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

            if (entitySource.getEntity() instanceof PlayerEntity) {
                PlayerEntity killer = (PlayerEntity) entitySource.getEntity();

                if (!killer.inventory.getSelected().isEmpty()) {
                    if (EnchantmentHelper.getEnchantments(killer.inventory.getSelected()).containsKey(BPEnchantments.vorpal)) {
                        int level = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.vorpal, killer.inventory.getSelected());

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

        if (event.getEntityLiving() instanceof CreeperEntity) {
            event.getEntityLiving().spawnAtLocation(new ItemStack(Items.CREEPER_HEAD, 1), 0.0F);
        }

        if (event.getEntityLiving() instanceof PlayerEntity) {
            ItemStack drop = new ItemStack(Items.PLAYER_HEAD, 1);
            drop.setTag(new CompoundNBT());
            drop.getTag().putString("SkullOwner", event.getEntityLiving().getDisplayName().getString());
            event.getEntityLiving().spawnAtLocation(drop, 0.0F);
        }

        if (event.getEntityLiving() instanceof AbstractSkeletonEntity) {
            AbstractSkeletonEntity sk = (AbstractSkeletonEntity) event.getEntityLiving();

            if (sk instanceof SkeletonEntity) {
                event.getEntityLiving().spawnAtLocation(new ItemStack(Items.SKELETON_SKULL, 1), 0.0F);
            } else {
                event.getEntityLiving().spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), 0.0F);
            }
        }

        if (event.getEntityLiving() instanceof ZombieEntity) {
            event.getEntityLiving().spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD, 1), 0.0F);
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
        if (item == Item.byBlock(Blocks.AIR))
            return;
    }

    @SubscribeEvent
    public void onBonemealEvent(BonemealEvent event) {

        if (!event.getWorld().isClientSide) {
            if (event.getBlock().getBlock() instanceof GrassBlock) {
                for (int x = event.getPos().getX() - 2; x < event.getPos().getX() + 3; x++) {
                    for (int z = event.getPos().getZ() - 2; z < event.getPos().getZ() + 3; z++) {
                        if (event.getWorld().isEmptyBlock(new BlockPos(x, event.getPos().getY() + 1, z))) {
                            if (event.getWorld().random.nextInt(50) == 1) {
                                //TODO: Flower Chance
                                //if (BPBlocks.indigo_flower.canSustainPlant(event.getLevel().getBlockState(event.getPos().up()), event.getLevel(), event.getPos().up())) {
                                //    event.getLevel().setBlock(event.getPos().up(), BPBlocks.indigo_flower.defaultBlockState());
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
    public void blockHighlightEvent(DrawHighlightEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        World world = player.level;
        if (world == null) {
            return;
        }
        RayTraceResult mop = event.getTarget();
        if(mop instanceof BlockRayTraceResult) {
            BlockPos pos = ((BlockRayTraceResult) mop).getBlockPos();
            BlockState state = world.getBlockState(pos);
            if(state.getBlock() instanceof BlockBPMultipart){
                BlockState partstate = MultipartUtils.getClosestState(player, pos);
                IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.lines());
                if(partstate != null) {
                    VoxelShape shape = partstate.getShape(world, pos, ISelectionContext.of(player));
                    Vector3d projectedView = event.getInfo().getPosition();
                    double d0 = pos.getX() - projectedView.x();
                    double d1 = pos.getY() - projectedView.y();
                    double d2 = pos.getZ() - projectedView.z();
                    Matrix4f matrix4f = event.getMatrix().last().pose();
                    shape.forAllEdges((startX, startY, startZ, endX, endY, endZ) -> {
                        builder.vertex(matrix4f, (float)(startX + d0), (float)(startY + d1), (float)(startZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                        builder.vertex(matrix4f, (float)(endX + d0), (float)(endY + d1), (float)(endZ + d2)).color(0.0F, 0.0F, 0.0F, 0.4F).endVertex();
                    });
                    event.setCanceled(true);
                }
            }
        }
    }
}
