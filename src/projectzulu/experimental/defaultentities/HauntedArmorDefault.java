package projectzulu.experimental.defaultentities;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import projectzulu.common.api.CustomEntityList;
import projectzulu.common.api.CustomMobData;
import projectzulu.common.api.ItemList;
import projectzulu.common.core.ItemGenerics;
import projectzulu.common.mobs.EntityArmadillo;
import projectzulu.common.mobs.EntityHauntedArmor;

import com.google.common.base.Optional;

public class HauntedArmorDefault extends DefaultWithEgg{
	
	public HauntedArmorDefault(){
		super("Haunted Armor", EntityHauntedArmor.class);		
		setRegistrationProperties(128, 3, true);
		
		eggColor1 = (194 << 16) + (194 << 8) + 194;		eggColor2 = (251 << 16) + (246 << 8) + 36;
	}
	
	@Override
	public void outputDataToList() {
		if(shouldExist){
			CustomMobData customMobData = new CustomMobData(mobName, reportSpawningInLog);
			if(ItemList.genericCraftingItems1.isPresent()){
				customMobData.addLootToMob(new ItemStack(ItemList.genericCraftingItems1.get().itemID, 1, ItemGenerics.Properties.Ectoplasm.meta()), 4);
			}
			CustomEntityList.hauntedArmor = Optional.of(customMobData);	
		}
	}
}
