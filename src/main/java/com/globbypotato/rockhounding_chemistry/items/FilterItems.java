package com.globbypotato.rockhounding_chemistry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.items.io.ArrayIO;
import com.globbypotato.rockhounding_chemistry.utils.ModUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FilterItems extends ArrayIO{

	public FilterItems(String name, String[] array) {
		super(name, array);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(stack.getItemDamage() > 0) {
			tooltip.add(I18n.format("tooltip.filter_items.0", stack.getItemDamage() * 0.5F));
			tooltip.add(I18n.format("tooltip.filter_items.1", ModUtils.stepDivision(stack.getItemDamage())));
		}
	}
}