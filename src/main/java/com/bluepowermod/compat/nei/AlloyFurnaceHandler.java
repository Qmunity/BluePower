package com.bluepowermod.compat.nei;

import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.IContainerDrawHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.FurnaceRecipeHandler;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.blocks.BlockBPFluid;
import com.bluepowermod.client.gui.GuiAlloyCrucible;
import com.bluepowermod.client.gui.widget.WidgetTank;
import com.bluepowermod.helper.FluidHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.recipe.AlloyCrucibleRegistry;
import com.bluepowermod.recipe.AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * 
 * @author MineMaarten
 */

public class AlloyFurnaceHandler extends FurnaceRecipeHandler implements IContainerTooltipHandler, IContainerDrawHandler {

    private static final WidgetTank[] tank = new WidgetTank[2];
    private static boolean renderingAlloyFurnaceHandler;
    private Field guiLeftField, guiTopField;

    public AlloyFurnaceHandler() {

        tank[0] = new WidgetTank(0, 134, 8, 16, 48, null);
        tank[0].setCapacity(AlloyCrucibleRegistry.TANK_SIZE);
        tank[1] = new WidgetTank(0, 134, 8, 16, 48, null);
        tank[1].setCapacity(AlloyCrucibleRegistry.TANK_SIZE);
    }

    @Override
    public String getRecipeName() {

        return BPBlocks.alloy_crucible.getLocalizedName();
    }

    @Override
    public String getGuiTexture() {

        return Refs.MODID + ":textures/gui/alloy_crucible.png";// Doing this comment here so git transfers over the full line and doesn't ignore the
                                                               // 'GUI' vs 'gui' change.
    }

    /**
     * @return The class of the GuiContainer that this recipe would be crafted in.
     */
    @Override
    public Class<? extends GuiContainer> getGuiClass() {

        return GuiAlloyCrucible.class;
    }

    /**
     * Loads a rectangle that can be clicked in the Alloy Furnace GUI that will load up every recipe of the Alloy Furnace
     */
    @Override
    public void loadTransferRects() {

        transferRects.add(new RecipeTransferRect(new Rectangle(17, 43, 18, 18), "fuel"));
        transferRects.add(new RecipeTransferRect(new Rectangle(97, 24, 22, 14), getRecipesID()));
    }

    @Override
    public void drawExtras(int recipe) {

        drawProgressBar(17, 43, 177, 0, 14, 14, 48, 7);
        drawProgressBar(97, 24, 177, 14, 24, 16, 48, 0);
    }

    @Override
    public void drawForeground(int recipe) {

        super.drawForeground(recipe);

        ItemStack[] input = new ItemStack[9];

        int i = 0;
        for (PositionedStack ps : getIngredientStacks(recipe)) {
            input[i] = ps.item;
            i++;
        }

        tank[recipe % 2].setFluid(AlloyCrucibleRegistry.getInstance().getMatchingRecipe(input, null).getResult(null));
        tank[recipe % 1].render(0, 0);

        renderingAlloyFurnaceHandler = true;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {

        if (outputId.equals(getRecipesID())) {
            for (IAlloyFurnaceRecipe recipe : AlloyCrucibleRegistry.getInstance().getAllRecipes())
                if (recipe instanceof AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe)
                    arecipes.add(new AlloyRecipe((AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe) recipe));
        } else if (outputId.equals("fuel")) {

        } else
            super.loadCraftingRecipes(outputId, results);
    }

    private String getRecipesID() {

        return "alloyCrucible";
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {

        if (result.getItem() instanceof ItemBlock) {
            Block b = Block.getBlockFromItem(result.getItem());
            if (b instanceof BlockBPFluid) {

                Fluid fluid = FluidHelper.getFluid(b.getUnlocalizedName());

                for (IAlloyFurnaceRecipe recipe : AlloyCrucibleRegistry.getInstance().getAllRecipes())
                    if (recipe instanceof AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe) {
                        if (recipe.getResult(null).getFluid() == fluid) {
                            arecipes.add(new AlloyRecipe((AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe) recipe));
                        }
                    }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (IAlloyFurnaceRecipe recipe : AlloyCrucibleRegistry.getInstance().getAllRecipes()) {
            if (recipe instanceof AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe) {
                AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe standardAlloyRecipe = (AlloyCrucibleRegistry.StandardAlloyFurnaceRecipe) recipe;
                for (ItemStack input : standardAlloyRecipe.getRequiredItems()) {
                    if (NEIServerUtils.areStacksSameTypeCrafting(input, ingredient)) {
                        arecipes.add(new AlloyRecipe(standardAlloyRecipe));
                        break;
                    }
                }
            }
        }
    }

    public class AlloyRecipe extends CachedRecipe {

        private final List<PositionedStack> requiredItems;

        public AlloyRecipe(StandardAlloyFurnaceRecipe alloyRecipe) {

            requiredItems = new ArrayList<PositionedStack>();
            int x = 0, y = 0;
            for (ItemStack requiredItem : alloyRecipe.getRequiredItems()) {
                requiredItems.add(new PositionedStack(requiredItem, 42 + x * 18, 6 + y * 18));
                if (++x > 2) {
                    x = 0;
                    y++;
                }
            }
        }

        @Override
        public PositionedStack getResult() {

            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {

            return requiredItems;
        }

        @Override
        public PositionedStack getOtherStack() {

            PositionedStack stack = afuels.get(cycleticks / 48 % afuels.size()).stack;
            stack = stack.copy();
            stack.relx = 16;
            stack.rely = 24;
            return stack;
        }
    }

    @Override
    public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {

        if (renderingAlloyFurnaceHandler) {
            int guiTop = 0;
            int guiLeft = 0;
            if (guiTopField == null)
                guiTopField = ReflectionHelper.findField(GuiContainer.class, "field_74197_n", "guiTop");
            if (guiLeftField == null)
                guiLeftField = ReflectionHelper.findField(GuiContainer.class, "field_74198_m", "guiLeft");
            try {
                guiTop = guiTopField.getInt(gui);
                guiLeft = guiLeftField.getInt(gui);
            } catch (Exception e) {
            }
            if (tank[0].getBounds().contains(-guiLeft + mousex - 5, -guiTop + mousey - 15)) {
                tank[0].addTooltip(currenttip, false);
            } else if (tank[1].getBounds().contains(-guiLeft + mousex - 5, -guiTop + mousey - 80)) {
                tank[1].addTooltip(currenttip, false);
            }
        }
        renderingAlloyFurnaceHandler = false;
        return currenttip;
    }

    @Override
    public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {

        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {

        return currenttip;
    }

    @Override
    public void onPreDraw(GuiContainer gui) {

    }

    @Override
    public void renderObjects(GuiContainer gui, int mousex, int mousey) {

    }

    @Override
    public void postRenderObjects(GuiContainer gui, int mousex, int mousey) {

    }

    @Override
    public void renderSlotUnderlay(GuiContainer gui, Slot slot) {

    }

    @Override
    public void renderSlotOverlay(GuiContainer gui, Slot slot) {

    }
}
