package com.bluepowermod.compat.mcmp;

/*
import com.bluepowermod.block.machine.BlockLampSurface;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.compat.mcmp.parts.PartLamp;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.multipart.IMultipartRegistry;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.ref.MCMPCapabilities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.ICapabilityProvider;
import net.neoforged.event.AttachCapabilitiesEvent;
import net.neoforged.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@mcmultipart.api.addon.MCMPAddon
public class MCMPAddon implements IMCMPAddon{

    @Override
    public void registerParts(IMultipartRegistry registry) {
        for (Block block: BPBlocks.blockList) {
            if(block instanceof BlockLampSurface){
                registry.registerPartWrapper(block, new PartLamp(block));
                registry.registerStackWrapper(Item.getItemFromBlock(block), s -> true, block);
            }

        }
    }

    @SubscribeEvent
    public void onAttachTileCaps(AttachCapabilitiesEvent<BlockEntity> te) {
        if (te.getObject() instanceof TileLamp) {
            te.addCapability(ResourceLocation.fromNamespaceAndPath(Refs.MODID, "multipart"), new ICapabilityProvider() {
                IMultipartTile multipartTileLamp = IMultipartTile.wrap(te.getObject());

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
                    return Objects.equals(capability, MCMPCapabilities.MULTIPART_TILE);
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
                    return capability == MCMPCapabilities.MULTIPART_TILE ? MCMPCapabilities.MULTIPART_TILE.cast(multipartTileLamp) : null;
                }
            });
        }
    }

}
*/
