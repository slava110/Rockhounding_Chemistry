package com.globbypotato.rockhounding_chemistry.machines.recipe;

import java.util.ArrayList;

import com.globbypotato.rockhounding_chemistry.ModItems;
import com.globbypotato.rockhounding_chemistry.enums.EnumChemicals;
import com.globbypotato.rockhounding_chemistry.enums.EnumFluid;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.PrecipitationRecipe;
import com.globbypotato.rockhounding_chemistry.utils.BaseRecipes;
import com.globbypotato.rockhounding_core.utils.CoreBasics;

import net.minecraft.item.ItemStack;

public class PrecipitationRecipes extends BaseRecipes{
	public static ArrayList<PrecipitationRecipe> precipitation_recipes = new ArrayList<PrecipitationRecipe>();

	public static void machineRecipes() {
		precipitation_recipes.add(new PrecipitationRecipe(	"Zeolite Compound",
															"compoundFlyash",
															ItemStack.EMPTY,
															getFluid(EnumFluid.HYDROCHLORIC_ACID, 500),
															getFluid(EnumFluid.TOXIC_WASTE, 300),
															chemicals(1, EnumChemicals.ZEOLITE_COMPOUND) ));

		precipitation_recipes.add(new PrecipitationRecipe(	"Pure YAG Compound",
															yag_compound,
															ItemStack.EMPTY,
															getFluid(EnumFluid.LIQUID_AMMONIA, 100),
															getFluid(EnumFluid.TOXIC_WASTE, 100), 
															chemicals(1, EnumChemicals.PURE_YAG_COMPOUND) ));

		precipitation_recipes.add(new PrecipitationRecipe(	"Potassium Carbonate", 
															cracked_charcoal, 
															ItemStack.EMPTY,
															CoreBasics.waterStack(1000),
															getFluid(EnumFluid.TOXIC_WASTE, 700), 
															chemicals(1, EnumChemicals.POTASSIUM_CARBONATE) ));

		precipitation_recipes.add(new PrecipitationRecipe(	"Potassium Nitrate", 
															potassium_carbonate,
															ItemStack.EMPTY,
															getFluid(EnumFluid.NITRIC_ACID, 200), 
															getFluid(EnumFluid.TOXIC_WASTE, 200), 
															chemicals(1, EnumChemicals.POTASSIUM_NITRATE) ));

		precipitation_recipes.add(new PrecipitationRecipe(	"Sodium Polyacrylate", 
															salt_compound,
															ItemStack.EMPTY,
															getFluid(EnumFluid.ACRYLIC_ACID, 200), 
															getFluid(EnumFluid.TOXIC_WASTE, 200), 
															new ItemStack(ModItems.SODIUM_POLYACRYLATE) ));

		precipitation_recipes.add(new PrecipitationRecipe(	"Ammonium Chloride",
															"compoundFlyash", 
															ItemStack.EMPTY, 
															getFluid(EnumFluid.HYDROCHLORIC_ACID, 200), 
															getFluid(EnumFluid.TOXIC_WASTE, 100), 
															chemicals(1, EnumChemicals.AMMONIUM_CHLORIDE) ));

	}

}