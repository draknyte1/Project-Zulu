package projectzulu.experimental.defaultentities;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import projectzulu.common.api.CustomEntityList;
import projectzulu.common.api.CustomMobData;
import projectzulu.common.api.ItemList;
import projectzulu.common.core.ItemGenerics;
import projectzulu.common.mobs.EntityArmadillo;
import projectzulu.common.mobs.EntityHornBill;

import com.google.common.base.Optional;

public class HornbillDefault extends DefaultSpawnable{
	
	public HornbillDefault(){
		super("Horn Bill", EntityHornBill.class);		
		setSpawnProperties(EnumCreatureType.monster, 10, 25, 1, 1);
		setRegistrationProperties(128, 3, true);
		
		eggColor1 =  (26 << 16) + (19 << 8) + 15;						eggColor2 = (199 << 16) + (33 << 8) + 14;
		defaultBiomesToSpawn.add(BiomeGenBase.jungle.biomeName); 		defaultBiomesToSpawn.add(BiomeGenBase.jungleHills.biomeName); 			
		defaultBiomesToSpawn.add("Mini Jungle");						defaultBiomesToSpawn.add("Extreme Jungle");
	}
	
	@Override
	public void outputDataToList() {
		if(shouldExist){
			CustomMobData customMobData = new CustomMobData(mobName, secondarySpawnRate, reportSpawningInLog);
			customMobData.addLootToMob(new ItemStack(Item.chickenRaw), 10);
			customMobData.addLootToMob(new ItemStack(Item.feather), 8);
			if(ItemList.genericCraftingItems1.isPresent()){
				customMobData.addLootToMob(new ItemStack(ItemList.genericCraftingItems1.get().itemID, 1, ItemGenerics.Properties.SmallHeart.meta()), 4);
				customMobData.addLootToMob(new ItemStack(ItemList.genericCraftingItems1.get().itemID, 1, ItemGenerics.Properties.Talon.meta()), 4);
			}
			CustomEntityList.hornBill = Optional.of(customMobData);	
		}
	}
}
