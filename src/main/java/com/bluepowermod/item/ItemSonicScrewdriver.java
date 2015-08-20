package com.bluepowermod.item;

import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.PowerConstants;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartInteractable;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3i;

/**
 * @author Koen Beckers (K-4U)
 */
public class ItemSonicScrewdriver extends ItemBattery implements IScrewdriver {

    public ItemSonicScrewdriver() {

        super(1000);//How much power this screwdriver holds.

        setUnlocalizedName(Refs.SONIC_SCREWDRIVER_NAME);
        setTextureName(Refs.MODID + ":" + Refs.SONIC_SCREWDRIVER_NAME);
    }

    @Override
    public boolean damage(ItemStack stack, int damage, EntityPlayer player, boolean simulated) {
        if (player != null && player.capabilities.isCreativeMode)
            return true;

        if (!simulated) {
            addEnergy(stack, -(int)PowerConstants.POWER_PER_ACTION);
        }

        return getVoltage(stack) > 0;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        Block block = world.getBlock(x, y, z);

        if (player.isSneaking()) {
            ITilePartHolder itph = MultipartCompatibility.getPartHolder(world, new Vec3i(x, y, z));

            if (itph != null) {
                QMovingObjectPosition mop = itph.rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
                if (mop == null)
                    return false;
                IPart p = mop.getPart();
                if (p instanceof IPartInteractable) {
                    if (((IPartInteractable) p).onActivated(player, mop, stack)) {
                        damage(stack, 1, player, false);
                        return true;
                    }
                }
            }
        }

        if (block instanceof BlockContainerBase) {
            if (((BlockContainerBase) block).getGuiId() >= 0) {
                if (player.isSneaking()) {
                    if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                        damage(stack, 1, player, false);
                        return true;
                    }
                }
            } else {
                if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                    damage(stack, 1, player, false);
                    return true;
                }
            }
        } else {
            if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                damage(stack, 1, player, false);
                return true;
            }
        }
        return false;
    }
}
