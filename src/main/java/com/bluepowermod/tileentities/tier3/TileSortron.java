package com.bluepowermod.tileentities.tier3;

import java.util.HashSet;
import java.util.Set;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.api.tube.IPneumaticTube;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.references.Dependencies;
import com.bluepowermod.tileentities.TileMachineBase;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

/**
 * @author Dynious
 */
@Optional.InterfaceList(value = { @Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = Dependencies.COMPUTER_CRAFT), @Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = Dependencies.OPEN_COMPUTERS) })
public class TileSortron extends TileMachineBase implements IPeripheral, SimpleComponent {
    
    private static final String        NAME                         = "BluePower.Sortron";
    private static final int           ANIMATION_TIME               = 10;
    private final Set<IComputerAccess> connectedComputers           = new HashSet<IComputerAccess>();
    private final Set<Context>         contexts                     = new HashSet<Context>();
    private IInventory                 connectedInventory;
    private byte                       acceptedColor                = -1;
    private ItemStack                  acceptedStack                = null;
    private int                        acceptedStackSize            = 0;
    private final byte                 ticksLeftToShowItemTransport = 0;
    
    @Override
    public void onBlockNeighbourChanged() {
    
        super.onBlockNeighbourChanged();
        ForgeDirection direction = ForgeDirection.getOrientation(getBlockMetadata());
        TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (tile instanceof IInventory) {
            connectedInventory = (IInventory) tile;
        } else {
            connectedInventory = null;
        }
    }
    
    /*
    Sortron functions
     */
    
    public Object[] setAcceptedCol(Object[] arguments) throws LuaException {
    
        if (arguments.length > 0) {
            acceptedColor = parseColorFromObject(arguments[0]);
            return new Boolean[] { true };
        }
        throw new IllegalArgumentException("No expected argument was given");
    }
    
    public Object[] getAcceptedCol(Object[] arguments) throws LuaException {
    
        return new Integer[] { (int) acceptedColor };
    }
    
    public Object[] setAcceptedItem(Object[] arguments) throws LuaException {
    
        if (arguments.length > 0 && arguments[0] instanceof String) {
            String unlocalizedName = (String) arguments[0];
            if (unlocalizedName.isEmpty()) {
                acceptedStack = null;
                return new Boolean[] { true };
            }
            Item item = (Item) Item.itemRegistry.getObject(unlocalizedName);
            if (item != null) {
                ItemStack stack;
                if (arguments.length > 1 && arguments[1] instanceof Double) {
                    int meta = ((Double) arguments[1]).intValue();
                    if (meta >= 0) {
                        stack = new ItemStack(item, 0, meta);
                    } else {
                        throw new IllegalArgumentException("Metadata must be greater than or equal to 0");
                    }
                } else {
                    stack = new ItemStack(item, 0, OreDictionary.WILDCARD_VALUE);
                }
                acceptedStack = stack;
                return new Boolean[] { true };
            }
            throw new IllegalArgumentException("Item not found");
        }
        throw new IllegalArgumentException("No expected argument was given");
    }
    
    public Object[] getAcceptedItem(Object[] arguments) throws LuaException {
    
        return new String[] { getStringFromStack(acceptedStack) };
    }
    
    public Object[] getNumSlots(Object[] arguments) throws LuaException {
    
        if (connectedInventory != null) { return new Integer[] { connectedInventory.getSizeInventory() }; }
        throw new LuaException("Sortron has no connected Inventory");
    }
    
    public Object[] getSlotContents(Object[] arguments) throws LuaException {
    
        if (connectedInventory != null) {
            if (arguments.length > 0 && arguments[0] instanceof Double) {
                int slot = ((Double) arguments[0]).intValue();
                if (slot >= 0 && slot < connectedInventory.getSizeInventory()) { return new String[] { getStringFromStack(connectedInventory.getStackInSlot(slot)) }; }
                throw new IllegalArgumentException("Slot value should be greater than or equal to 0 and smaller than the number of slots");
            }
            throw new IllegalArgumentException("No expected argument was given");
        }
        throw new LuaException("Sortron has no connected Inventory");
    }
    
    public Object[] pullFromSlot(Object[] arguments) throws LuaException {
    
        if (connectedInventory != null) {
            if (arguments.length > 0 && arguments[0] instanceof Double) {
                int slot = ((Double) arguments[0]).intValue();
                if (slot >= 0 && slot < connectedInventory.getSizeInventory()) {
                    ItemStack stack = connectedInventory.getStackInSlot(slot);
                    if (stack != null) {
                        if (arguments.length > 1) {
                            byte color = parseColorFromObject(arguments[1]);
                            addItemToOutputBuffer(stack, IPneumaticTube.TubeColor.values()[color]);
                        } else {
                            addItemToOutputBuffer(stack);
                        }
                        connectedInventory.setInventorySlotContents(slot, null);
                        return new String[] { getStringFromStack(stack) };
                    }
                    return new Boolean[] { false };
                }
                throw new IllegalArgumentException("Slot value should be greater than or equal to 0 and smaller than the number of slots");
            }
            throw new IllegalArgumentException("No expected argument was given");
        }
        throw new LuaException("Sortron has no connected Inventory");
    }
    
    public Object[] sort(Object[] arguments) throws LuaException {
    
        if (arguments.length > 0 && arguments[0] instanceof Double) {
            int stackSize = ((Double) arguments[0]).intValue();
            if (stackSize >= 0) {
                acceptedStackSize = stackSize;
                return new Boolean[] { true };
            }
            throw new IllegalArgumentException("StackSize value must be greater than or equal to 0");
        }
        throw new IllegalArgumentException("No expected argument was given");
    }
    
    public Object[] getStackSizeLeft(Object[] arguments) throws LuaException {
    
        return new Integer[] { acceptedStackSize };
    }
    
    /*
    Sortron helper functions
     */
    
    public static String getStringFromStack(ItemStack stack) {
    
        if (stack != null) {
            String string = Item.itemRegistry.getNameForObject(stack.getItem());
            if (stack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
                string += " " + stack.getItemDamage();
            }
            if (stack.stackSize != 0) {
                string += " " + stack.stackSize;
            }
            return string;
        }
        return null;
    }
    
    public static byte parseColorFromObject(Object argument) throws LuaException {
    
        if (argument instanceof Double) {
            byte color = ((Double) argument).byteValue();
            if (color >= -1 && color < 16) { return color; }
            throw new IllegalArgumentException("Color values should be greater than or equal to -1 and smaller than 16");
        } else if (argument instanceof String) {
            String input = (String) argument;
            if (input.isEmpty()) { return -1; }
            for (byte color = 0; color < ItemDye.field_150923_a.length; color++) {
                String colorName = ItemDye.field_150923_a[color];
                if (colorName.equals(input)) { return color; }
            }
            throw new IllegalArgumentException("Given String is not a color");
        }
        throw new IllegalArgumentException("No expected argument was given");
    }
    
    public static boolean doItemStacksMatch(ItemStack itemStack1, ItemStack itemStack2) {
    
        return itemStack1 == null && itemStack2 == null || !(itemStack1 == null || itemStack2 == null) && itemStack1.getItem().equals(itemStack2.getItem())
                && (itemStack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemStack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemStack1.getItemDamage() == itemStack2.getItemDamage());
    }
    
    private void removeFromAcceptedStack(int amount) {
    
        acceptedStackSize -= amount;
        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT)) {
            for (IComputerAccess computer : connectedComputers) {
                computer.queueEvent("sortChange", new Integer[] { acceptedStackSize });
            }
        }
    }
    
    /*
    ITubeConnection implementation
     */
    
    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {
    
        if (acceptedStackSize <= 0) return stack;
        
        if ((acceptedStack == null || doItemStacksMatch(stack.stack, acceptedStack)) && (acceptedColor == -1 || acceptedColor == stack.color.ordinal())) {
            int acceptedSize = Math.min(stack.stack.stackSize, acceptedStackSize);
            removeFromAcceptedStack(acceptedSize);
            ItemStack stack1 = stack.stack.splitStack(acceptedSize);
            TubeStack tubeStack = super.acceptItemFromTube(new TubeStack(stack1, from, stack.color), from, simulate);
            if (tubeStack != null) stack.stack.stackSize += tubeStack.stack.stackSize;
            if (stack.stack.stackSize == 0) return null;
            return stack;
        }
        return stack;
    }
    
    /*
    NBT saving
     */
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
    
        super.readFromNBT(compound);
        acceptedColor = compound.getByte("acceptedCol");
        if (compound.hasKey("ItemStack")) {
            NBTTagList list = compound.getTagList("ItemStack", 10);
            acceptedStack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(0));
        }
        acceptedStackSize = compound.getInteger("stackSize");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound compound) {
    
        super.writeToNBT(compound);
        compound.setByte("acceptedCol", acceptedColor);
        if (acceptedStack != null) {
            NBTTagList list = new NBTTagList();
            NBTTagCompound compound1 = new NBTTagCompound();
            acceptedStack.writeToNBT(compound1);
            list.appendTag(compound1);
            compound.setTag("ItemStack", list);
        }
        compound.setInteger("stackSize", acceptedStackSize);
    }
    
    /*
    ComputerCraft implementation
     */
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public String getType() {
    
        return NAME;
    }
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public String[] getMethodNames() {
    
        return new String[] { "setAcceptedCol", "getAcceptedCol", "setAcceptedItem", "getAcceptedItem", "getNumSlots", "getSlotContents", "pullFromSlot", "sort", "getStackSizeLeft" };
    }
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
    
        switch (method) {
            case 0:
                return setAcceptedCol(arguments);
            case 1:
                return getAcceptedCol(arguments);
            case 2:
                return setAcceptedItem(arguments);
            case 3:
                return getAcceptedItem(arguments);
            case 4:
                return getNumSlots(arguments);
            case 5:
                return getSlotContents(arguments);
            case 6:
                return pullFromSlot(arguments);
            case 7:
                return sort(arguments);
            case 8:
                return getStackSizeLeft(arguments);
        }
        return new Object[0];
    }
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public void attach(IComputerAccess computer) {
    
        connectedComputers.add(computer);
    }
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public void detach(IComputerAccess computer) {
    
        connectedComputers.remove(computer);
    }
    
    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public boolean equals(IPeripheral other) {
    
        return other.getType().equals(getType());
    }
    
    /*
    OpenComputers implementation
     */
    
    @Override
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public String getComponentName() {
    
        return NAME;
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] setAcceptedCol(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return setAcceptedCol(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] getAcceptedCol(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return getAcceptedCol(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] setAcceptedItem(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return setAcceptedItem(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] getAcceptedItem(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return getAcceptedItem(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] getNumSlots(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return getNumSlots(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] getSlotContents(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return getSlotContents(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] pullFromSlot(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return pullFromSlot(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] sort(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return sort(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] getStackSizeLeft(Context context, Arguments arguments) throws LuaException {
    
        Object[] args = new Object[arguments.count()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.checkAny(i);
        }
        return getStackSizeLeft(args);
    }
    
    @Callback
    @Optional.Method(modid = Dependencies.OPEN_COMPUTERS)
    public Object[] greet(Context context, Arguments arguments) {
    
        contexts.add(context);
        return null;
    }
}
