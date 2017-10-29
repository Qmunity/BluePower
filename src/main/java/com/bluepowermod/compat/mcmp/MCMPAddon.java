package com.bluepowermod.compat.mcmp;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.compat.mcmp.parts.PartLamp;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.multipart.IMultipartRegistry;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.ref.MCMPCapabilities;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@mcmultipart.api.addon.MCMPAddon
public class MCMPAddon implements IMCMPAddon{

    @Override
    public void registerParts(IMultipartRegistry registry) {
        registry.registerPartWrapper(BPBlocks.cagedLamp[0], new PartLamp(BPBlocks.cagedLamp[0]));
        registry.registerStackWrapper(Item.getItemFromBlock(BPBlocks.cagedLamp[0]), s -> true, BPBlocks.cagedLamp[0]);
    }

    @SubscribeEvent
    public void onAttachTileCaps(AttachCapabilitiesEvent<TileEntity> te) {
        if (te.getObject() instanceof TileLamp) {
            te.addCapability(new ResourceLocation(Refs.MODID, "multipart"), new ICapabilityProvider() {
                IMultipartTile multipartTileLamp = IMultipartTile.wrap(te.getObject());

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return Objects.equals(capability, MCMPCapabilities.MULTIPART_TILE);
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == MCMPCapabilities.MULTIPART_TILE ? (T)multipartTileLamp : null;
                }
            });
        }
    }

}
