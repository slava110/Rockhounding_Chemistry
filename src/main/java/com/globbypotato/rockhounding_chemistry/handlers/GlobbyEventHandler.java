package com.globbypotato.rockhounding_chemistry.handlers;

import java.util.ArrayList;
import java.util.Random;

import com.globbypotato.rockhounding_chemistry.ModBlocks;
import com.globbypotato.rockhounding_chemistry.enums.EnumMiscBlocksA;
import com.globbypotato.rockhounding_chemistry.enums.machines.EnumMachinesB;
import com.globbypotato.rockhounding_chemistry.enums.machines.EnumMachinesD;
import com.globbypotato.rockhounding_chemistry.fluids.ModFluids;
import com.globbypotato.rockhounding_chemistry.machines.io.MachineIO;
import com.globbypotato.rockhounding_chemistry.machines.recipe.ChemicalExtractorRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.LeachingVatRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.MineralSizerRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.RetentionVatRecipes;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.ChemicalExtractorRecipe;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.LeachingVatRecipe;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.MineralSizerRecipe;
import com.globbypotato.rockhounding_chemistry.machines.recipe.construction.RetentionVatRecipe;
import com.globbypotato.rockhounding_chemistry.utils.ModUtils;
import com.globbypotato.rockhounding_core.enums.EnumFluidNbt;
import com.globbypotato.rockhounding_core.utils.CoreUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class GlobbyEventHandler {
	ItemStack petroStack;
    ItemStack mineralStack;
	Random rand = new Random();

	@SubscribeEvent
    @SideOnly(Side.CLIENT)
	public void handleTooltip(ItemTooltipEvent event){
		ItemStack itemstack = event.getItemStack();
		if(itemstack != ItemStack.EMPTY){
			// TODO optimize recipe lookup (integrated server loading process slowed down by this in big modpacks)
	    	for(MineralSizerRecipe recipe : MineralSizerRecipes.mineral_sizer_recipes){
		    	for(int y = 0; y < recipe.getOutput().size(); y++){
					if(ItemStack.areItemsEqual(recipe.getOutput().get(y), itemstack)){
						String comText = TextFormatting.GRAY + "Required Comminution Level: " + TextFormatting.GREEN + recipe.getComminution().get(y);
			    		if(!event.getToolTip().contains(comText)){
			    			event.getToolTip().add(comText);
						}
					}
				}
	    	}

	    	for(LeachingVatRecipe recipe : LeachingVatRecipes.leaching_vat_recipes){
	    		if(recipe.getOutput().size() >= 1){
			    	for(int y = 0; y < recipe.getOutput().size(); y++){
						if(ItemStack.areItemsEqual(recipe.getOutput().get(y), itemstack)){
							float realgravity = recipe.getGravity().get(y);
							event.getToolTip().add(TextFormatting.GRAY + "Specific Gravity: " + TextFormatting.LIGHT_PURPLE + realgravity);
						}
					}
	    		}
	    	}

	    	for(RetentionVatRecipe recipe : RetentionVatRecipes.retention_vat_recipes){
	    		if(recipe.getOutput().size() >= 1){
			    	for(int y = 0; y < recipe.getOutput().size(); y++){
						if(ItemStack.areItemsEqual(recipe.getOutput().get(y), itemstack)){
							float realgravity = recipe.getGravity().get(y);
							String gravText = TextFormatting.GRAY + "Specific Gravity: " + TextFormatting.LIGHT_PURPLE + realgravity;
							if(!event.getToolTip().contains(gravText)){
								event.getToolTip().add(gravText);
							}
						}
					}
	    		}
	    	}

	    	for(ChemicalExtractorRecipe recipe: ChemicalExtractorRecipes.extractor_recipes){
	    		if(recipe.getType()){
					ArrayList<Integer> inputOreIDs = CoreUtils.intArrayToList(OreDictionary.getOreIDs(itemstack));
					if(inputOreIDs.size() > 0 && inputOreIDs.contains(OreDictionary.getOreID(recipe.getOredict()))){
						addExtractorRecipe(recipe, event);
					}
	    		}else{
					if(!recipe.getInput().isEmpty() && itemstack.isItemEqual(recipe.getInput())){
						addExtractorRecipe(recipe, event);
					}
	    		}
	    	}

			if(itemstack.hasTagCompound()){

				if(itemstack.isItemEqual(new ItemStack(ModBlocks.MACHINES_D, 1, EnumMachinesD.ORBITER.ordinal()))){
					NBTTagCompound tag = itemstack.getTagCompound();
					if(tag.hasKey("XPCount")){
						int xpCount = tag.getInteger("XPCount");
						if(xpCount > 0){
							String xp = TextFormatting.GRAY + "Stored Experience: " + TextFormatting.BOLD + TextFormatting.DARK_GREEN + xpCount + " xp";
							event.getToolTip().add(xp);
						}
					}
				}

		    	if(!ModUtils.hasWawla()){
			    	if(itemstack.getItem() instanceof UniversalBucket){
						if(FluidUtil.getFluidContained(itemstack) != null){
							FluidStack filterfluid = FluidUtil.getFluidContained(itemstack);
			        		event.getToolTip().add(TextFormatting.GRAY + "Temperature: " + TextFormatting.BOLD +  TextFormatting.YELLOW + filterfluid.getFluid().getTemperature() + "K");
						}
			    	}
		    	}

		    	if(itemstack.isItemEqual(new ItemStack(ModBlocks.MACHINES_B, 1, EnumMachinesB.PRESSURE_VESSEL.ordinal()))){
		    		if(itemstack.getTagCompound().hasKey(EnumFluidNbt.GAS.nameTag())){
						FluidStack filterfluid = FluidStack.loadFluidStackFromNBT(itemstack.getTagCompound().getCompoundTag(EnumFluidNbt.GAS.nameTag()));
						if(filterfluid != null){
							event.getToolTip().add(TextFormatting.GRAY + "Temperature: " + TextFormatting.BOLD +  TextFormatting.YELLOW + filterfluid.getFluid().getTemperature() + "K");
						}
		    		}
		    	}
			}
		}
	}

	private void addExtractorRecipe(ChemicalExtractorRecipe recipe, ItemTooltipEvent event) {
		String inhibit = "";
		event.getToolTip().add(TextFormatting.GRAY + "Category: " + TextFormatting.YELLOW + recipe.getCategory());
		for(int x = 0; x < recipe.getElements().size(); x++){
			inhibit = "";
			String recipeDict = recipe.getElements().get(x);
			ArrayList<ItemStack> firstDict = new ArrayList<ItemStack>();
			if(!OreDictionary.getOres(recipeDict).isEmpty()){
				firstDict.addAll(OreDictionary.getOres(recipeDict));
				if(!firstDict.isEmpty() && firstDict.size() > 0){
				   for(int ix = 0; ix < ChemicalExtractorRecipes.inhibited_elements.size(); ix++){
					   if(recipeDict.toLowerCase().matches(ChemicalExtractorRecipes.inhibited_elements.get(ix).toLowerCase())){
						   inhibit = " - (Inhibited)";
					   }
				   }
				   event.getToolTip().add(TextFormatting.GRAY + firstDict.get(0).getDisplayName() + " - " + TextFormatting.WHITE + TextFormatting.BOLD + recipe.getQuantities().get(x) + "%" + TextFormatting.RESET + TextFormatting.RED + inhibit);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockBurn(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().isItemEqual(new ItemStack(ModBlocks.MISC_BLOCKS_A, 1, EnumMiscBlocksA.CHARCOAL_BLOCK.ordinal())) ){
			event.setBurnTime(16000);
		}
	}

	@SubscribeEvent
	public void onInteract(RightClickBlock event){
		if(Loader.isModLoaded(ModUtils.thermal_f_id)){
        	if(event.getWorld().getBlockState(event.getPos()) != null && event.getWorld().getBlockState(event.getPos()).getBlock() instanceof MachineIO){
	            if (!event.getItemStack().isEmpty() && !ModUtils.thermal_f_wrench().isEmpty() && event.getItemStack().isItemEqual(ModUtils.thermal_f_wrench())){
	            	event.setCanceled(true);
	            }
        	}
		}
	}

    @SubscribeEvent
    public void killDrops(LivingDeathEvent event){
    	if(ModConfig.fluidDamage && ModConfig.xpDrop){
	    	EntityLivingBase entity = event.getEntityLiving();
	    	World world = entity.getEntityWorld();
	    	if(event.getSource() == ModFluids.SPILL){
				double fx = world.rand.nextDouble();
				double fy = world.rand.nextDouble();
				double fz = world.rand.nextDouble();    		
				EntityXPOrb orb = new EntityXPOrb(world, entity.posX + fx, entity.posY + fy, entity.posZ + fz, world.rand.nextInt(3)+1);
				if(!world.isRemote){
					world.spawnEntity(orb);
				}
	    	}
    	}
    }

    private static boolean isValidNBT(NBTTagCompound tag) {
		return (tag.hasKey("Device") && tag.getInteger("Device") > -1) && tag.hasKey("Cycle") && tag.hasKey("Recipe") && tag.hasKey("Amount") && tag.hasKey("Done");
	}

}