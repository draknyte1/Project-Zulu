package projectzulu.common;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.MinecraftForge;
import projectzulu.common.api.ItemBlockList;
import projectzulu.common.blocks.ArmorManager;
import projectzulu.common.blocks.ItemBlockManager;
import projectzulu.common.blocks.ItemBlockRecipeManager;
import projectzulu.common.blocks.RenderCampFire;
import projectzulu.common.blocks.RenderSpike;
import projectzulu.common.blocks.RenderUniversalFlowerPot;
import projectzulu.common.blocks.ZuluGuiHandler;
import projectzulu.common.core.CreativeTab;
import projectzulu.common.core.DefaultProps;
import projectzulu.common.core.ItemGenerics;
import projectzulu.common.core.ProjectZuluLog;
import projectzulu.common.potion.EventHandleNullPotions;
import projectzulu.common.potion.PotionManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
@Mod(modid = DefaultProps.BlocksModId, name = "Project Zulu Block and Items", version = DefaultProps.VERSION_STRING, dependencies = DefaultProps.DEPENDENCY_CORE)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class ProjectZulu_Blocks {
	
	private static int defaultBlockID = 1200;
	public static int getNextDefaultBlockID(){ return defaultBlockID++; }
	private static int defaultItemID = 9000;
	public static int getNextDefaultItemID(){ return defaultItemID++; }
	public static final CreativeTabs projectZuluCreativeTab = new CreativeTab(CreativeTabs.creativeTabArray.length, "projectZuluTab");

	/*Armor Indexes*/
	public static int scaleIndex;
	public static int whiteWoolIndex;
	public static int redWoolIndex;
	public static int greenWoolIndex;
	public static int blueWoolIndex;
	public static int goldScaleIndex;
	public static int ironScaleIndex;
	public static int diamondScaleIndex;
	public static int cactusIndex;
	public static int furIndex;
	
	@Instance(DefaultProps.BlocksModId)
	public static ProjectZulu_Blocks modInstance;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration zuluConfig = new Configuration(  new File(event.getModConfigurationDirectory(), DefaultProps.configDirectory + DefaultProps.defaultConfigFile) );
        zuluConfig.load();
        
		scaleIndex = ProjectZulu_Core.proxy.addArmor("scaleArmor");
		goldScaleIndex = ProjectZulu_Core.proxy.addArmor("goldscale");
		ironScaleIndex = ProjectZulu_Core.proxy.addArmor("ironscale");
		diamondScaleIndex = ProjectZulu_Core.proxy.addArmor("diamondscale");
		
		whiteWoolIndex = ProjectZulu_Core.proxy.addArmor("whitedesertcloth");
		redWoolIndex = ProjectZulu_Core.proxy.addArmor("reddesertcloth");
		greenWoolIndex = ProjectZulu_Core.proxy.addArmor("greendesertcloth");
		blueWoolIndex = ProjectZulu_Core.proxy.addArmor("bluedesertcloth");
		cactusIndex = ProjectZulu_Core.proxy.addArmor("cactusarmor");
		furIndex = ProjectZulu_Core.proxy.addArmor("mammothfur");
        
		ProjectZuluLog.info("Starting ItemBlock Init ");
        try {
			ItemBlockManager.preInit(zuluConfig);
			ArmorManager.preInit(zuluConfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProjectZuluLog.info("Finished ItemBlock Init ");
		ProjectZuluLog.info("Starting Potion Init ");
		PotionManager.loadSettings(zuluConfig);
		ProjectZuluLog.info("Finsished Potion Init ");
        zuluConfig.save();
	}
	
	@Init
	public void load(FMLInitializationEvent event){
		
		ProjectZuluLog.info("Starting ItemBlock Setup ");
		try {
			ItemBlockManager.init();
			ArmorManager.init();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		ProjectZuluLog.info("Finished ItemBlock Setup ");

		if(ItemBlockList.spike.isPresent()){
			ProjectZulu_Core.spikeRenderID = ProjectZulu_Core.spikeRenderID == -1 ? RenderingRegistry.getNextAvailableRenderId() : ProjectZulu_Core.spikeRenderID;
			RenderingRegistry.registerBlockHandler(ProjectZulu_Core.spikeRenderID, new RenderSpike() );
			ProjectZuluLog.info("Spike Render ID Registed to %s", ProjectZulu_Core.spikeRenderID);
		}
		
		if(ItemBlockList.campfire.isPresent()){
			ProjectZulu_Core.campFireRenderID = ProjectZulu_Core.campFireRenderID == -1 ? RenderingRegistry.getNextAvailableRenderId() : ProjectZulu_Core.campFireRenderID;
			RenderingRegistry.registerBlockHandler(ProjectZulu_Core.campFireRenderID, new RenderCampFire() );
			ProjectZuluLog.info("Campfire Render ID Registed to %s", ProjectZulu_Core.campFireRenderID);
		}
		
		if(ItemBlockList.universalFlowerPot.isPresent()){
			ProjectZulu_Core.universalFlowerPotRenderID = ProjectZulu_Core.universalFlowerPotRenderID == -1 ? RenderingRegistry.getNextAvailableRenderId() : ProjectZulu_Core.universalFlowerPotRenderID;
			RenderingRegistry.registerBlockHandler(ProjectZulu_Core.universalFlowerPotRenderID, new RenderUniversalFlowerPot() );
			ProjectZuluLog.info("Universal Flower Pot Render ID Registed to %s", ProjectZulu_Core.universalFlowerPotRenderID);
		}

		ProjectZulu_Core.proxy.registerBlockRenders();
        NetworkRegistry.instance().registerGuiHandler(ProjectZulu_Core.modInstance, new ZuluGuiHandler());
        
		/* War Axe Declaration */
//		TickRegistry.registerScheduledTickHandler(new RenderCustomArmorTicker(), Side.CLIENT);
//		goldScaleIndex = mod_ProjectZulu.proxy.addArmor("Armor Sets/goldscale");
//		vikingArmorHead = (new ItemVikingHelmet(vikingHelmetID, mod_ProjectZulu.goldScaleMaterial,goldScaleIndex,0)).setIconIndex(48).setItemName("vikingArmorHead");
//		LanguageRegistry.addName(vikingArmorHead, "vikingArmorHead"); 
//		MinecraftForgeClient.registerItemRenderer(vikingHelmetID+256, new ItemRendererVikingHelmet());
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event){
		if(ItemBlockList.genericCraftingItems1.isPresent()){
			DungeonHooks.addDungeonLoot(new ItemStack(ItemBlockList.genericCraftingItems1.get(), 1, ItemGenerics.Properties.ShinyBauble.meta()), 50, 3, 8);
		}
			
		ItemBlockRecipeManager.setupBlockModuleRecipies();
		LanguageRegistry.instance().addStringLocalization("itemGroup.projectZuluTab", "en_US", "Project Zulu");
		
		if(!PotionManager.potionModuleEnabled){
			ProjectZuluLog.info("Skipping Potion Setup, Potion Module Disabled");
		}else{
			ProjectZuluLog.info("Starting Potion Setup ");
			PotionManager.setupAndRegisterPotions();
			ProjectZuluLog.info("Finsished Potion Setup ");
		}
		
		/* Turn on NullPotionHandler */
		if(PotionManager.enableNullPotionHandler){
			MinecraftForge.EVENT_BUS.register(new EventHandleNullPotions());
		}
	}
	
	
	@ServerStarting
	public void serverStart(FMLServerStartingEvent event){
		/* Add Custom GameRules */
		GameRules gameRule = event.getServer().worldServerForDimension(0).getGameRules();
		/* Add Does Campfire Burn GameRule: Only if not Present */
		String ruleName = "doesCampfireBurn";
		if(gameRule.hasRule(ruleName)){
		}else {
			gameRule.addGameRule(ruleName, "false");
		}
	}
}
