package com.bluepowermod.compat.nei;

/*import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.container.InventoryCraftingDummy;
import codechicken.nei.recipe.ShapedRecipeHandler;
import com.bluepowermod.BluePower;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.supported.GateNullCell;
import com.bluepowermod.recipe.RecipeNullCell;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class NullCellHandler extends ShapedRecipeHandler {

    @Override
    public String getRecipeName() {

        return "Null Cell Crafting";
    }

    @Override
    public void loadTransferRects() {

        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), getRecipesID()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {

        if (outputId.equals(getRecipesID())) {
            try {
                for (int i = 0; i < 4; i++) {
                    GateNullCell base = new GateNullCell(null, false, null, false);

                    for (int j = 0; j < (i == 0 || i == 3 ? 1 : RedwireType.values().length); j++) {
                        if (i == 1 || i == 2)
                            base = new GateNullCell(i == 1 ? RedwireType.values()[j] : null, false,
                                    i == 2 ? RedwireType.values()[j] : null, false);
                        if (i < 3) {
                            for (RedwireType t1 : RedwireType.values()) {
                                for (RedwireType t2 : RedwireType.values()) {
                                    NullCellRecipe r = new NullCellRecipe(base, t1, false, t2, false);
                                    if (r.getResult() != null)
                                        arecipes.add(r);
                                }
                                NullCellRecipe r = new NullCellRecipe(base, t1, false, null, false);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, null, false, t1, false);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                            }
                        } else {
                            for (RedwireType t1 : RedwireType.values()) {
                                for (RedwireType t2 : RedwireType.values()) {
                                    base = new GateNullCell(t1, false, t2, false);
                                    NullCellRecipe r = new NullCellRecipe(base, true, false);
                                    if (r.getResult() != null)
                                        arecipes.add(r);
                                    r = new NullCellRecipe(base, true, true);
                                    if (r.getResult() != null)
                                        arecipes.add(r);
                                    r = new NullCellRecipe(base, false, true);
                                    if (r.getResult() != null)
                                        arecipes.add(r);
                                }
                                base = new GateNullCell(t1, false, null, false);
                                NullCellRecipe r = new NullCellRecipe(base, true, false);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, true, true);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, false, true);
                                if (r.getResult() != null)
                                    arecipes.add(r);

                                base = new GateNullCell(null, false, t1, false);
                                r = new NullCellRecipe(base, true, false);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, true, true);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, false, true);
                                if (r.getResult() != null)
                                    arecipes.add(r);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        super.loadCraftingRecipes(outputId, results);
    }

    private String getRecipesID() {

        return "nullcell";
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {

        if (result == null || !(result.getItem() instanceof ItemPart))
            return;

        try {
            for (int i = 0; i < 4; i++) {
                GateNullCell base = new GateNullCell(null, false, null, false);

                for (int j = 0; j < (i == 0 || i == 3 ? 1 : RedwireType.values().length); j++) {
                    if (i == 1 || i == 2)
                        base = new GateNullCell(i == 1 ? RedwireType.values()[j] : null, false, i == 2 ? RedwireType.values()[j] : null,
                                false);
                    if (i < 3) {
                        for (RedwireType t1 : RedwireType.values()) {
                            for (RedwireType t2 : RedwireType.values()) {
                                NullCellRecipe r = new NullCellRecipe(base, t1, false, t2, false);
                                if (r.getResult() != null
                                        && ((ItemPart) r.getResult().item.getItem()).createPart(r.getResult().item,
                                                BluePower.proxy.getPlayer(), null, null).equals(
                                                        ((ItemPart) result.getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                    arecipes.add(r);
                            }
                            NullCellRecipe r = new NullCellRecipe(base, t1, false, null, false);
                            if (r.getResult() != null
                                    && ((ItemPart) r.getResult().item.getItem()).createPart(r.getResult().item,
                                            BluePower.proxy.getPlayer(), null, null).equals(
                                                    ((ItemPart) result.getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, null, false, t1, false);
                            if (r.getResult() != null
                                    && ((ItemPart) r.getResult().item.getItem()).createPart(r.getResult().item,
                                            BluePower.proxy.getPlayer(), null, null).equals(
                                                    ((ItemPart) result.getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                        }
                    } else {
                        for (RedwireType t1 : RedwireType.values()) {
                            for (RedwireType t2 : RedwireType.values()) {
                                base = new GateNullCell(t1, false, t2, false);
                                NullCellRecipe r = new NullCellRecipe(base, true, false);
                                if (r.getResult() != null
                                        && new GateNullCell(null, false, base.getTypeB(), base.isBundledB()).equals(((ItemPart) result
                                                .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, true, true);
                                if (r.getResult() != null
                                        && new GateNullCell(null, false, null, false).equals(((ItemPart) result.getItem()).createPart(
                                                result, BluePower.proxy.getPlayer(), null, null)))
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, false, true);
                                if (r.getResult() != null
                                        && new GateNullCell(base.getTypeB(), base.isBundledB(), null, false).equals(((ItemPart) result
                                                .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                    arecipes.add(r);
                            }
                            base = new GateNullCell(t1, false, null, false);
                            NullCellRecipe r = new NullCellRecipe(base, true, false);
                            if (r.getResult() != null
                                    && new GateNullCell(null, false, base.getTypeB(), base.isBundledB()).equals(((ItemPart) result
                                            .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, true, true);
                            if (r.getResult() != null
                                    && new GateNullCell(null, false, null, false).equals(((ItemPart) result.getItem()).createPart(result,
                                            BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, false, true);
                            if (r.getResult() != null
                                    && new GateNullCell(base.getTypeB(), base.isBundledB(), null, false).equals(((ItemPart) result
                                            .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);

                            base = new GateNullCell(null, false, t1, false);
                            r = new NullCellRecipe(base, true, false);
                            if (r.getResult() != null
                                    && new GateNullCell(null, false, base.getTypeB(), base.isBundledB()).equals(((ItemPart) result
                                            .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, true, true);
                            if (r.getResult() != null
                                    && new GateNullCell(null, false, null, false).equals(((ItemPart) result.getItem()).createPart(result,
                                            BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, false, true);
                            if (r.getResult() != null
                                    && new GateNullCell(base.getTypeB(), base.isBundledB(), null, false).equals(((ItemPart) result
                                            .getItem()).createPart(result, BluePower.proxy.getPlayer(), null, null)))
                                arecipes.add(r);
                        }
                        NullCellRecipe r = new NullCellRecipe(base, true, false);
                        if (r.getResult() != null
                                && new GateNullCell(null, false, base.getTypeB(), base.isBundledB()).equals(((ItemPart) result.getItem())
                                        .createPart(result, BluePower.proxy.getPlayer(), null, null)))
                            arecipes.add(r);
                        r = new NullCellRecipe(base, true, true);
                        if (r.getResult() != null
                                && new GateNullCell(null, false, null, false).equals(((ItemPart) result.getItem()).createPart(result,
                                        BluePower.proxy.getPlayer(), null, null)))
                            arecipes.add(r);
                        r = new NullCellRecipe(base, false, true);
                        if (r.getResult() != null
                                && new GateNullCell(base.getTypeB(), base.isBundledB(), null, false).equals(((ItemPart) result.getItem())
                                        .createPart(result, BluePower.proxy.getPlayer(), null, null)))
                            arecipes.add(r);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        try {
            for (int i = 0; i < 4; i++) {
                GateNullCell base = new GateNullCell(null, false, null, false);

                for (int j = 0; j < (i == 0 || i == 3 ? 1 : RedwireType.values().length); j++) {
                    if (i == 1 || i == 2)
                        base = new GateNullCell(i == 1 ? RedwireType.values()[j] : null, false, i == 2 ? RedwireType.values()[j] : null,
                                false);
                    if (i < 3) {
                        for (RedwireType t1 : RedwireType.values()) {
                            for (RedwireType t2 : RedwireType.values()) {
                                NullCellRecipe r = new NullCellRecipe(base, t1, false, t2, false);
                                if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                    arecipes.add(r);
                            }
                            NullCellRecipe r = new NullCellRecipe(base, t1, false, null, false);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, null, false, t1, false);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                        }
                    } else {
                        for (RedwireType t1 : RedwireType.values()) {
                            for (RedwireType t2 : RedwireType.values()) {
                                base = new GateNullCell(t1, false, t2, false);
                                NullCellRecipe r = new NullCellRecipe(base, true, false);
                                if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, true, true);
                                if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                    arecipes.add(r);
                                r = new NullCellRecipe(base, false, true);
                                if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                    arecipes.add(r);
                            }
                            base = new GateNullCell(t1, false, null, false);
                            NullCellRecipe r = new NullCellRecipe(base, true, false);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, true, true);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, false, true);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);

                            base = new GateNullCell(null, false, t1, false);
                            r = new NullCellRecipe(base, true, false);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, true, true);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                            r = new NullCellRecipe(base, false, true);
                            if (r.getResult() != null && r.contains(r.ingredients, ingredient))
                                arecipes.add(r);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static ItemStack setItemAt(IInventory inv, int x, int y, ItemStack item) {

        inv.setInventorySlotContents(y * 3 + x, item);
        return item;
    }

    private static ItemStack setItemAt(Object[] grid, int x, int y, ItemStack item) {

        grid[y * 3 + x] = item;
        return item;
    }

    private static ItemStack getOutput(GateNullCell original, boolean screwdriverTop, boolean screwdriverBottom) {

        ItemStack inputNullCell = GateNullCell.getStackWithData(original);
        ItemStack inputScrewdriver = new ItemStack(BPItems.screwdriver);

        InventoryCraftingDummy r = new InventoryCraftingDummy();
        setItemAt(r, 1, 1, inputNullCell);

        if (screwdriverTop && screwdriverBottom)
            setItemAt(r, 0, 1, inputScrewdriver);
        else if (screwdriverTop)
            setItemAt(r, 1, 0, inputScrewdriver);
        else if (screwdriverBottom)
            setItemAt(r, 1, 2, inputScrewdriver);

        ItemStack result = RecipeNullCell.instance.getCraftingResult(r);
        if (result == null)
            return error;
        return result;
    }

    private static Object[] getItems(GateNullCell original, boolean screwdriverTop, boolean screwdriverBottom) {

        Object[] grid = new Object[3 * 3];

        ItemStack inputNullCell = GateNullCell.getStackWithData(original);
        ItemStack inputScrewdriver = new ItemStack(BPItems.screwdriver);

        setItemAt(grid, 1, 1, inputNullCell);

        if (screwdriverTop && screwdriverBottom)
            setItemAt(grid, 0, 1, inputScrewdriver);
        else if (screwdriverTop)
            setItemAt(grid, 1, 0, inputScrewdriver);
        else if (screwdriverBottom)
            setItemAt(grid, 1, 2, inputScrewdriver);

        return grid;
    }

    private static ItemStack getOutput(GateNullCell original, RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

        ItemStack inputNullCell = GateNullCell.getStackWithData(original);
        ItemStack wireA = typeA != null ? PartManager.getPartInfo("wire." + typeA.getName() + (bundledA ? ".bundled" : "")).getStack()
                : null;
        ItemStack wireB = typeB != null ? PartManager.getPartInfo("wire." + typeB.getName() + (bundledB ? ".bundled" : "")).getStack()
                : null;

        InventoryCraftingDummy r = new InventoryCraftingDummy();
        setItemAt(r, 1, 1, inputNullCell);

        if (wireA != null)
            setItemAt(r, 1, 0, wireA);
        if (wireB != null)
            setItemAt(r, 1, 2, wireB);

        ItemStack result = RecipeNullCell.instance.getCraftingResult(r);
        if (result == null)
            return error;
        return result;
    }

    private static Object[] getItems(GateNullCell original, RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

        Object[] grid = new Object[3 * 3];

        ItemStack inputNullCell = GateNullCell.getStackWithData(original);
        ItemStack wireA = typeA != null ? PartManager.getPartInfo("wire." + typeA.getName() + (bundledA ? ".bundled" : "")).getStack()
                : null;
        ItemStack wireB = typeB != null ? PartManager.getPartInfo("wire." + typeB.getName() + (bundledB ? ".bundled" : "")).getStack()
                : null;

        setItemAt(grid, 1, 1, inputNullCell);

        if (wireA != null)
            setItemAt(grid, 1, 0, wireA);
        if (wireB != null)
            setItemAt(grid, 1, 2, wireB);

        return grid;
    }

    private static final ItemStack error = new ItemStack(Blocks.COCOA);

    private class NullCellRecipe extends CachedShapedRecipe {

        public NullCellRecipe(GateNullCell original, boolean screwdriverTop, boolean screwdriverBottom) {

            super(3, 3, NullCellHandler.getItems(original, screwdriverTop, screwdriverBottom), NullCellHandler.getOutput(original,
                    screwdriverTop, screwdriverBottom));
        }

        public NullCellRecipe(GateNullCell original, RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

            super(3, 3, NullCellHandler.getItems(original, typeA, bundledA, typeB, bundledB), NullCellHandler.getOutput(original, typeA,
                    bundledA, typeB, bundledB));
        }

        @Override
        public PositionedStack getResult() {

            if (super.getResult().contains(error))
                return null;

            return super.getResult();
        }
    }

}*/
