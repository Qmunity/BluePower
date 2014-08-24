package com.bluepowermod.client.gui.widget;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.util.ItemStackUtils;
import com.bluepowermod.util.Refs;

public class WidgetFuzzySetting extends WidgetMode {

    public WidgetFuzzySetting(int id, int x, int y) {
        super(id, x, y, 0, 3, new String[] { Refs.MODID + ":textures/gui/widgets/fuzzy_widget_normal.png",
                Refs.MODID + ":textures/gui/widgets/fuzzy_widget_fuzzy.png", Refs.MODID + ":textures/gui/widgets/fuzzy_widget_exact.png" });

    }

    @Override
    public void render(int mouseX, int mouseY) {
        textureIndex = value;
        super.render(mouseX, mouseY);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
        curTip.add("gui.widget.fuzzy");
        switch (value) {
        case 0:
            curTip.add("gui.widget.fuzzy.normal");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.normal.info");
            break;
        case 1:
            curTip.add("gui.widget.fuzzy.fuzzy");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.fuzzy.info");
            break;
        case 2:
            curTip.add("gui.widget.fuzzy.exact");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.exact.info");
            break;
        }
        if (!shiftPressed)
            curTip.add("gui.sneakForInfo");
    }

    @Override
    protected int getTextureV() {

        return 0;
    }

    @Override
    protected int getTextureWidth() {

        return 14;
    }

    @Override
    protected int getTextureHeight() {

        return 14;
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, int mode) {
        if (stack1 == null && stack2 != null)
            return false;
        if (stack1 != null && stack2 == null)
            return false;
        if (stack1 == null && stack2 == null)
            return true;

        if (mode == 0) {
            return OreDictionary.itemMatches(stack1, stack2, false);
        } else if (mode == 1) {
            return ItemStackUtils.isItemFuzzyEqual(stack1, stack2);
        } else {
            return OreDictionary.itemMatches(stack1, stack2, false) && ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }
}
