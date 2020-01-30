package com.bluepowermod.util;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Data Pack Generator functions closely derived from "MoreThanHidden/RestrictedPortals" - AdvancementHelper
 * @author MoreThanHidden
 */
public class DatapackUtils {

    /**
     * Generates an Alloy Furnace Recipe Data Pack.
     */
    public static void generateAlloyFurnaceRecipe(IAlloyFurnaceRecipe recipe, String path) {
        try {
            Optional<Ingredient> ingredient = recipe.getRequiredItems().stream().findFirst();
            if(ingredient.isPresent()) {
                Optional<ItemStack> requiredItem = Arrays.stream(ingredient.get().getMatchingStacks()).findFirst();
                if (requiredItem.isPresent()) {
                    //Name the file
                    String shortname = requiredItem.get().getItem().getRegistryName().getPath() +
                            recipe.getRecipeOutput().getItem().getRegistryName().getPath() + "_generated";

                    //Serialize Json
                    JsonArray ingredientArray = new JsonArray();

                    for (int i = 0; i < recipe.getRequiredItems().size(); i++) {
                        JsonObject ingredients = new JsonObject();
                        ingredients.add("item", new JsonPrimitive(recipe.getRequiredItems().get(i).getMatchingStacks()[0].getItem().getRegistryName().toString()));
                        ingredients.add("count", new JsonPrimitive(recipe.getRequiredCount().get(i)));
                        ingredientArray.add(ingredients);
                    }

                    //Output Item
                    ItemStack outputItem = recipe.getRecipeOutput();
                    JsonObject outitem = new JsonObject();
                    outitem.add("item", new JsonPrimitive(outputItem.getItem().getRegistryName().toString()));
                    outitem.add("count", new JsonPrimitive(outputItem.getCount()));

                    JsonObject json = new JsonObject();
                    json.add("type", new JsonPrimitive("bluepower:alloy_smelting"));
                    json.add("ingredients", ingredientArray);
                    json.add("result", outitem);

                    //Create Json File
                    File file = new File(path + "/bluepower/data/bluepower/recipes/alloy_furnace/" + shortname + ".json");

                    FileWriter fileWriter = new FileWriter(file.getPath());
                    fileWriter.write(json.toString());
                    fileWriter.close();
                }
            }

        } catch (IOException e) {
                BluePower.log.error("Error creating Alloy Furnace recipe - " + e.getLocalizedMessage());
        }

    }

    /**
     * Clears the existing Alloy Furnace Data Pack, useful on reload or recipe changes.
     */
    public static void clearBPAlloyFurnaceDatapack(String path) {
        try {
            FileUtils.cleanDirectory(new File(path + "/bluepower/data/bluepower/recipes/alloy_furnace"));
        } catch (Exception e) {
            //BluePower.log.info("Failed to clean Alloy Furnace Recipe Folder");
        }
    }

    /**
     * Create a new Data Pack, mainly used for Dynamic Alloy Furnace Recipes.
     */
    public static void createBPDatapack(String path){

        //Make DataPack Folders
        File folder = new File(path + "/bluepower");
        if(folder.mkdir()){
            //BluePower.log.info("Created DataPack Folder: " + folder.getPath());
        }else{
            //BluePower.log.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        //Pack MCMeta(Json)
        JsonObject pack = new JsonObject();
        pack.add("description",  new JsonPrimitive("bluepower resources"));
        pack.add("pack_format",  new JsonPrimitive(4));

        JsonObject mcmeta = new JsonObject();
        mcmeta.add("pack", pack);

        File file = new File(path + "/bluepower/pack.mcmeta");
        try {
            if(file.createNewFile()){
                //BluePower.log.info("Created Data Pack mcmeta: " + file.getPath());
            }else{
                //BluePower.log.info("File already Exists: " + file.getPath());
            }
        } catch (IOException e) {
            //BluePower.log.info("Creating file failed: " + file.getPath());
        }

        try {
            FileWriter fileWriter = new FileWriter(file.getPath());
            fileWriter.write(mcmeta.toString());
            fileWriter.close();

        } catch (IOException e) {
            //BluePower.log.info("Editing File Failed: " + file.getPath());
        }


        folder = new File(path + "/bluepower/data");
        if(folder.mkdir()){
            //BluePower.log.info("Created DataPack Folder: " + folder.getPath());
        }else{
            //BluePower.log.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        folder = new File(path + "/bluepower/data/bluepower");
        if(folder.mkdir()){
            //BluePower.log.info("Created DataPack Folder: " + folder.getPath());
        }else{
            //BluePower.log.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        folder = new File(path + "/bluepower/data/bluepower/recipes");
        if(folder.mkdir()){
            //BluePower.log.info("Created DataPack Folder: " + folder.getPath());
        }else{
            //BluePower.log.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

        folder = new File(path + "/bluepower/data/bluepower/recipes/alloy_furnace");
        if(folder.mkdir()){
            //BluePower.log.info("Created DataPack Folder: " + folder.getPath());
        }else{
            //BluePower.log.info("DataPack Already Exists or Failed: " + folder.getPath());
        }

    }

}
