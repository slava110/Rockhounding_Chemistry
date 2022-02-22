package com.globbypotato.rockhounding_chemistry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.items.io.ItemIO;
import com.globbypotato.rockhounding_core.enums.EnumFluidNbt;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class SamplingAmpoule extends ItemIO {

	public SamplingAmpoule(String name) {
		super(name);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		stack.setTagCompound(new NBTTagCompound());
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		FluidStack sampled = getFluidFromStack(stack);

		if(sampled != null) {
			tooltip.add(I18n.format(sampled.getFluid().isGaseous() ? "tooltip.samplingampoule.gas" : "tooltip.samplingampoule.fluid", sampled.getLocalizedName()));
		} else {
			tooltip.add(I18n.format("tooltip.samplingampoule.empty"));
		}
	}

	@Nullable
	private FluidStack getFluidFromStack(ItemStack stack) {
		if(!stack.hasTagCompound())
			return null;

		NBTTagCompound tag = stack.getTagCompound();

		EnumFluidNbt type;
		if (tag.hasKey(EnumFluidNbt.GAS.nameTag()))
			type = EnumFluidNbt.GAS;
		else if (tag.hasKey(EnumFluidNbt.FLUID.nameTag()))
			type = EnumFluidNbt.FLUID;
		else
			return null;

		FluidStack sampled = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(type.nameTag()));

		if (sampled != null && sampled.getFluid() != null && ( type == EnumFluidNbt.FLUID || ( type == EnumFluidNbt.GAS && sampled.getFluid().isGaseous() ) ) ) {
			return sampled;
		}
		return null;
	}
}