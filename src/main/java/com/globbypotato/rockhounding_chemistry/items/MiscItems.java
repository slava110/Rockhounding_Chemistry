package com.globbypotato.rockhounding_chemistry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.enums.EnumMiscItems;
import com.globbypotato.rockhounding_chemistry.enums.utils.EnumCasting;
import com.globbypotato.rockhounding_chemistry.enums.utils.EnumServer;
import com.globbypotato.rockhounding_chemistry.items.io.ArrayIO;
import com.globbypotato.rockhounding_chemistry.machines.recipe.BedReactorRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.DepositionChamberRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.GasReformerRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.LabOvenRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.MetalAlloyerRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.PrecipitationRecipes;
import com.google.common.base.Strings;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class MiscItems extends ArrayIO {

	public MiscItems(String name, String[] array) {
		super(name, array);
	}

	@Override
    public int getItemStackLimit(ItemStack stack){
    	return stack.getItemDamage() == 15 
    		|| stack.getItemDamage() == 16 
    		|| stack.getItemDamage() == 0 
    		|| stack.getItemDamage() == 9 
    		|| stack.getItemDamage() == 30 
    		|| stack.getItemDamage() == 31 
    	    || stack.getItemDamage() == 32 
    	    || stack.getItemDamage() == 35 
    		? 1 : 64;
    }

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.getItemDamage() == EnumMiscItems.SERVER_FILE.ordinal()) {
			if(stack.hasTagCompound()){
				NBTTagCompound tag = stack.getTagCompound();
				if(isValidNBT(tag)){
					int device = tag.getInteger("Device");
					boolean cycle = tag.getBoolean("Cycle");
					int recipe = tag.getInteger("Recipe");
					int amount = tag.getInteger("Amount");
					int done = tag.getInteger("Done");
					tooltip.add(TextFormatting.GRAY + "Served Device: " + TextFormatting.BOLD + TextFormatting.YELLOW + EnumServer.values()[device].getName());
					tooltip.add(TextFormatting.GRAY + "Repeatable: " + TextFormatting.BOLD + TextFormatting.YELLOW + cycle);
					if(device == EnumServer.LAB_OVEN.ordinal()){
						if(Strings.isNullOrEmpty(LabOvenRecipes.lab_oven_recipes.get(recipe).getRecipeName())){
							tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + LabOvenRecipes.lab_oven_recipes.get(recipe).getSolution().getLocalizedName());
						}else{
							tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + LabOvenRecipes.lab_oven_recipes.get(recipe).getRecipeName());
						}
					}else if(device == EnumServer.METAL_ALLOYER.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + MetalAlloyerRecipes.metal_alloyer_recipes.get(recipe).getOutput().getDisplayName());
					}else if(device == EnumServer.DEPOSITION.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + DepositionChamberRecipes.deposition_chamber_recipes.get(recipe).getOutput().getDisplayName());
					}else if(device == EnumServer.SIZER.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Comminution Level: " + TextFormatting.BOLD +  TextFormatting.YELLOW + recipe);
					}else if(device == EnumServer.LEACHING.ordinal()){
						float currentGravity = (recipe * 2) + 2F;
						tooltip.add(TextFormatting.GRAY + "Gravity: " + TextFormatting.BOLD +  TextFormatting.YELLOW + (currentGravity - 2F) + " to " + (currentGravity + 2F));
					}else if(device == EnumServer.RETENTION.ordinal()){
						float currentGravity = (recipe * 2) + 2F;
						tooltip.add(TextFormatting.GRAY + "Gravity: " + TextFormatting.BOLD +  TextFormatting.YELLOW + (currentGravity - 2F) + " to " + (currentGravity + 2F));
					}else if(device == EnumServer.CASTING.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Pattern: " + TextFormatting.BOLD +  TextFormatting.YELLOW + EnumCasting.getFormalName(recipe));
					}else if(device == EnumServer.REFORMER.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + GasReformerRecipes.gas_reformer_recipes.get(recipe).getOutput().getLocalizedName());
					}else if(device == EnumServer.EXTRACTOR.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Intensity Level: " + TextFormatting.BOLD +  TextFormatting.YELLOW + recipe);
					}else if(device == EnumServer.PRECIPITATOR.ordinal()){
						if(Strings.isNullOrEmpty(PrecipitationRecipes.precipitation_recipes.get(recipe).getRecipeName())){
							tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + PrecipitationRecipes.precipitation_recipes.get(recipe).getSolution().getLocalizedName());
						}else{
							tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + PrecipitationRecipes.precipitation_recipes.get(recipe).getRecipeName());
						}
					}else if(device == EnumServer.BED_REACTOR.ordinal()){
						tooltip.add(TextFormatting.GRAY + "Recipe: " + TextFormatting.BOLD +  TextFormatting.YELLOW + BedReactorRecipes.bed_reactor_recipes.get(recipe).getOutput().getLocalizedName());
					}
					if(tag.hasKey("FilterStack")){
						ItemStack filter = new ItemStack(tag.getCompoundTag("FilterStack"));
						if(!filter.isEmpty()){
							tooltip.add(TextFormatting.GRAY + "Filter: " + TextFormatting.BOLD +  TextFormatting.DARK_GREEN + filter.getDisplayName());
						}
					}
					if(tag.hasKey("FilterFluid")){
						FluidStack filter = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("FilterFluid"));
						if(filter != null){
							tooltip.add(TextFormatting.GRAY + "Filter: " + TextFormatting.BOLD +  TextFormatting.DARK_GREEN + filter.getLocalizedName());
						}
					}
					tooltip.add(TextFormatting.GRAY + "Amount: " + TextFormatting.BOLD +  TextFormatting.YELLOW + amount + " scheduled");
					tooltip.add(TextFormatting.GRAY + "Process: " + TextFormatting.BOLD +  TextFormatting.YELLOW + done + " to do");
				}
			}
		}
	}

	private static boolean isValidNBT(NBTTagCompound tag) {
		return (tag.hasKey("Device") && tag.getInteger("Device") > -1) && tag.hasKey("Cycle") && tag.hasKey("Recipe") && tag.hasKey("Amount") && tag.hasKey("Done");
	}
}