package com.globbypotato.rockhounding_chemistry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.globbypotato.rockhounding_chemistry.items.io.ArrayIO;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class SpeedItems extends ArrayIO{

	public SpeedItems(String name, String[] array) {
		super(name, array);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)){
            for (int i = 0; i < this.itemArray.length; ++i){
            	if(i != 0){
            		items.add(new ItemStack(this, 1, i));
            	}
            }
        }
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if (stack.getItemDamage() > 0) {
			tooltip.add(I18n.format("tooltip.speed_items", stack.getItemDamage() + 1));
		} else {
			// For some reason it is Metal Alloyer ingredient display
			// TODO localization maybe and maybe separate item for that instead of speed_items
			if(stack.getTagCompound().hasKey("Title")){
				tooltip.add(stack.getTagCompound().getString("Title"));
			}
			if(stack.getTagCompound().hasKey("DustList")){
				NBTTagList dustList = stack.getTagCompound().getTagList("DustList", Constants.NBT.TAG_COMPOUND);
				for(int i = 0; i < dustList.tagCount(); i++){
					NBTTagCompound getQuantities = dustList.getCompoundTagAt(i);
					tooltip.add(getQuantities.getString("Ingr" + i));
				}
			}
		}
	}
}