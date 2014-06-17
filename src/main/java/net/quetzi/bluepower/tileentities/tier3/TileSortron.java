package net.quetzi.bluepower.tileentities.tier3;

import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.api.tube.IPneumaticTube;
import net.quetzi.bluepower.part.tube.TubeStack;
import net.quetzi.bluepower.references.Dependencies;
import net.quetzi.bluepower.tileentities.TileMachineBase;

import java.util.HashSet;
import java.util.Set;

@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = Dependencies.COMPUTER_CRAFT)
public class TileSortron extends TileMachineBase implements IPeripheral{

    private Set<IComputerAccess> connectedComputers = new HashSet<IComputerAccess>();
    private IInventory connectedInventory;
    private byte       acceptedColor = -1;
    private ItemStack  acceptedStack = null;
    private int        acceptedStackSize = 0;

    @Override
    public void onBlockNeighbourChanged() {

        super.onBlockNeighbourChanged();
        ForgeDirection direction = ForgeDirection.getOrientation(getBlockMetadata());
        TileEntity tile = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (tile instanceof IInventory) {
            connectedInventory = (IInventory) tile;
        }
    }

    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public String getType() {

        return "BluePower.Sortron";
    }

    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public String[] getMethodNames() {

        return new String[] { "setAcceptedCol", "getAcceptedCol", "setAcceptedItem", "getAcceptedItem",
                "getNumSlots", "getSlotContents", "pullFromSlot", "sort" };
    }

    @Override
    @Optional.Method(modid = Dependencies.COMPUTER_CRAFT)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {

        switch (method) {
            case 0:
                if (arguments.length > 0) {
                    acceptedColor = parseColorFromObject(arguments[0]);
                    return new Boolean[] { true };
                }
                throw new IllegalArgumentException("No expected argument was given");
            case 1:
                return new Integer[] { (int) acceptedColor };
            case 2:
                if (arguments.length > 0 && arguments[0] instanceof String) {
                    String unlocalizedName = ((String) arguments[0]);
                    if (unlocalizedName.isEmpty()) {
                        acceptedStack = null;
                        return new Boolean[] { true };
                    }
                    Item item = (Item) Item.itemRegistry.getObject(unlocalizedName);
                    if (item != null) {
                        ItemStack stack;
                        if (arguments.length > 1 && arguments[1] instanceof Double) {
                            int meta = ((Double)arguments[1]).intValue();
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
            case 3:
                return new String[]{ getStringFromStack(acceptedStack) };
            case 4:
                if (connectedInventory != null) {
                    return new Integer[] { connectedInventory.getSizeInventory() };
                }
                throw new Exception("Sortron has no connected Inventory");
            case 5:
                if (connectedInventory != null) {
                    if (arguments.length > 0 && arguments[0] instanceof Double) {
                        int slot = ((Double)arguments[0]).intValue();
                        if (slot >= 0 && slot < connectedInventory.getSizeInventory()) {
                            return new String[] { getStringFromStack(connectedInventory.getStackInSlot(slot))};
                        }
                        throw new IllegalArgumentException("Slot value should be greater than or equal to 0 and smaller than the number of slots");
                    }
                    throw new IllegalArgumentException("No expected argument was given");
                }
                throw new Exception("Sortron has no connected Inventory");
            case 6:
                if (connectedInventory != null) {
                    if (arguments.length > 0 && arguments[0] instanceof Double) {
                        int slot = ((Double)arguments[0]).intValue();
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
                        }
                        throw new IllegalArgumentException("Slot value should be greater than or equal to 0 and smaller than the number of slots");
                    }
                    throw new IllegalArgumentException("No expected argument was given");
                }
                throw new Exception("Sortron has no connected Inventory");
            case 7:
                if (arguments.length > 0 && arguments[0] instanceof Double) {
                    int stackSize = ((Double)arguments[0]).intValue();
                    if (stackSize >= 0) {
                        acceptedStackSize = stackSize;
                        return new Boolean[] { true };
                    }
                    throw new IllegalArgumentException("StackSize value must be greater than or equal to 0");
                }
                throw new IllegalArgumentException("No expected argument was given");
        }
        return new Object[0];
    }

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

    public static byte parseColorFromObject(Object argument) throws Exception {

        if (argument instanceof Double) {
            byte color = ((Double) argument).byteValue();
            if (color >= -1 && color < 16) {
                return color;
            }
            throw new IllegalArgumentException("Color values should be greater than or equal to -1 and smaller than 16");
        } else if (argument instanceof String) {
            String input = (String) argument;
            if (input.isEmpty()) {
                return -1;
            }
            for (byte color = 0; color < ItemDye.field_150923_a.length; color++) {
                String colorName = ItemDye.field_150923_a[color];
                if (colorName.equals(input)) {
                    return color;
                }
            }
            throw new IllegalArgumentException("Given String is not a color");
        }
        throw new IllegalArgumentException("No expected argument was given");
    }

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from) {

        if (acceptedStackSize <= 0) return stack;

        if ((acceptedStack == null || doItemStacksMatch(stack.stack, acceptedStack)) && (acceptedColor == -1 || acceptedColor == stack.color.ordinal())) {
            int acceptedSize = Math.min(stack.stack.stackSize, acceptedStackSize);
            acceptedStackSize -= acceptedSize;
            ItemStack stack1 = stack.stack.splitStack(acceptedSize);
            TubeStack tubeStack = super.acceptItemFromTube(new TubeStack(stack1, from, stack.color), from);
            if (tubeStack != null)
                stack.stack.stackSize += tubeStack.stack.stackSize;
            if (stack.stack.stackSize == 0) return null;
            return stack;
        }
        return stack;
    }

    public static boolean doItemStacksMatch(ItemStack itemStack1, ItemStack itemStack2) {

        return (itemStack1 == null && itemStack2 == null) || ( !(itemStack1 == null || itemStack2 == null) && itemStack1.getItem().equals(itemStack2.getItem())
                && (itemStack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemStack2.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || itemStack1.getItemDamage() == itemStack2.getItemDamage()));
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

        return other.getType().equals(this.getType());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
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

    @Override
    public void writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        acceptedColor = compound.getByte("acceptedCol");
        if (compound.hasKey("ItemStack")) {
            NBTTagList list = compound.getTagList("ItemStack", 10);
            acceptedStack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(0));
        }
        acceptedStackSize = compound.getInteger("stackSize");
    }
}
