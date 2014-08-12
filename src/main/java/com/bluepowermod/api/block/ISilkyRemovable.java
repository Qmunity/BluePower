package com.bluepowermod.api.block;

/**
 * This interface, when implemented by a block or BPPart, will be called by a Silky Screwdriver upon right clicking.
 * It will get the TileEntity / part, write the NBT to the item in the 'tileData' tag, and break the block/part.
 * 
 * Now make sure on block/part placement to call BPApi.getInstance().loadSilkySettings(args) to load the tag back.
 * @author MineMaarten
 */
public interface ISilkyRemovable {
}
