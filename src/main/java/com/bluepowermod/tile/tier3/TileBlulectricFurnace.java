package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlulectricFurnace extends TileMachineBase implements ISidedInventory, ITickable, INamedContainerProvider {
    private final BlutricityStorage storage = new BlutricityStorage(1000, 100);
    private LazyOptional<IPowerBase> blutricityCap;
    private boolean isActive;
    private int currentProcessTime;
    public static final int SLOTS = 2;
    private ItemStack inventory;
    private ItemStack outputInventory;
    private FurnaceRecipe currentRecipe;
    private boolean updatingRecipe = true;


    public TileBlulectricFurnace() {
        super(BPTileEntityType.BLULECTRIC_FURNACE);
        this.inventory = ItemStack.EMPTY;
        this.outputInventory = ItemStack.EMPTY;
    }

    @Override
    public void update() {
        if (world != null && !world.isRemote) {
            storage.resetCurrent();
            //Balance power of attached blulectric blocks.
            for (EnumFacing facing : EnumFacing.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(facing));
                if (tile != null)
                    tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                            exStorage -> EnergyHelper.balancePower(exStorage, storage));
            }
            if (updatingRecipe) {
                if(this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).isPresent()) {
                    currentRecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).get();
                }else{
                    currentRecipe = null;
                }
                updatingRecipe = false;
            }
            if (currentRecipe != null) {
                if((storage.getEnergy() / storage.getMaxEnergy()) > 0.5) {
                    storage.addEnergy(-1, false);
                    this.setIsActive(true);
                    //Check if progress completed, and output slot is empty and less then a stack of the same item.
                    if (++currentProcessTime >= (100 / (storage.getEnergy() / storage.getMaxEnergy())) && ((outputInventory.getItem() == currentRecipe.getRecipeOutput().getItem()
                            && (outputInventory.getCount() + currentRecipe.getCraftingResult(this).getCount()) <= 64)
                            || outputInventory.isEmpty())) {
                        currentProcessTime = 0;
                        if (!outputInventory.isEmpty()) {
                            outputInventory.setCount(outputInventory.getCount() + currentRecipe.getCraftingResult(this).getCount());
                        } else {
                            outputInventory = currentRecipe.getCraftingResult(this).copy();
                        }
                        this.decrStackSize(0, 1);
                        updatingRecipe = true;
                    }
                }else{
                    this.setIsActive(false);
                }
            } else {
                currentProcessTime = 0;
                this.setIsActive(false);
            }
        }

    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
            if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
            return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(storage);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null )
        {
            blutricityCap.invalidate();
            blutricityCap = null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isActive = compound.getBoolean("isActive");
        currentProcessTime = compound.getInteger("currentProcessTime");
        markForRenderUpdate();
        if(compound.contains("energy")) {
            NBTBase nbtstorage = compound.getTag("energy");
            CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
        NBTTagCompound tc = compound.getCompoundTag("inventory");
        inventory = ItemStack.read(tc);
        outputInventory = ItemStack.read(compound.getCompoundTag("outputInventory"));
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagCompound tc = new NBTTagCompound();
        inventory.write(tc);
        compound.setTag("inventory", tc);

        if (outputInventory != null) {
            NBTTagCompound outputCompound = new NBTTagCompound();
            outputInventory.write(outputCompound);
            compound.setTag("outputInventory", outputCompound);
        }

        compound.setInteger("currentProcessTime", currentProcessTime);
        compound.setBoolean("isActive", isActive);
        NBTBase nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
        compound.setTag("energy", nbtstorage);
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }


    protected final IIntArray fields = new IIntArray() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return (int) TileBlulectricFurnace.this.storage.getEnergy();
                case 1:
                    return TileBlulectricFurnace.this.currentProcessTime;
                case 2:
                    return (int) TileBlulectricFurnace.this.storage.getMaxEnergy();
                default:
                    return 0;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                case 0:
                    break;
                case 1:
                    TileBlulectricFurnace.this.currentProcessTime = value;
                    break;
                case 2:
                    break;
            }

        }

        public int size() {
            return 3;
        }
    };

    /**
     * ************* ADDED FUNCTIONS *************
     */

    public boolean getIsActive() {

        return isActive;
    }

    private void setIsActive(boolean _isActive) {
        if (world != null && _isActive != isActive && world.getGameTime() % 4 == 0) {
            isActive = _isActive;
            BlockBlulectricFurnace.setState(isActive, world, pos);
            sendUpdatePacket();
        }
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getSizeInventory() {

        return 2; // 1 inventory, 1 output
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        updatingRecipe = true;
        if (var1 == 0) {
            return inventory;
        } else if (var1 == 1) {
            return outputInventory;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getStackInSlot(index);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack itemStack) {

        if (var1 == 0) {
            inventory = itemStack;
        } else {
            outputInventory = itemStack;
        }
        updatingRecipe = true;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getPosition().withinDistance(pos, 64.0D);
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {

        if (slot == 0) {
            return true;
        } else {// Output slot
            return false;
        }
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        if (!outputInventory.isEmpty())
            drops.add(outputInventory);
        if (!inventory.isEmpty())
            drops.add(inventory);
        return drops;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing direction) {
        return isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
        return slot == 1;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(Refs.BLULECTRICFURNACE_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerBlulectricFurnace(id, inventory, this, fields);
    }
}
