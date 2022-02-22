package com.globbypotato.rockhounding_chemistry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.items.io.ArrayIO;
import com.globbypotato.rockhounding_chemistry.utils.ModUtils;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ProbeItems extends ArrayIO{

	public ProbeItems(String name, String[] array) {
		super(name, array);
		setMaxStackSize(1);
	}

	public static int orbiterUpgrade(ItemStack insertingStack) {
		if(ModUtils.isOrbiterProbe(insertingStack)){
			switch(insertingStack.getItemDamage()){
				case 0: return 1;
				case 1: return 4;
				case 2: return 12;
				case 3: return 24;
				default: return 0;
			}
		}
		return 0;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(I18n.format("tooltip.probe_items", orbiterUpgrade(stack)));
	}
}