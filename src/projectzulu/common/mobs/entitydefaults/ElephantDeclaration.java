package projectzulu.common.mobs.entitydefaults;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import projectzulu.common.api.CustomMobData;
import projectzulu.common.api.ItemList;
import projectzulu.common.core.ConfigHelper;
import projectzulu.common.core.DefaultProps;
import projectzulu.common.core.ItemGenerics;
import projectzulu.common.core.entitydeclaration.SpawnableDeclaration;
import projectzulu.common.mobs.entity.EntityElephant;
import projectzulu.common.mobs.models.ModelElephant;
import projectzulu.common.mobs.renders.RenderGenericLiving;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElephantDeclaration extends SpawnableDeclaration {

    public ElephantDeclaration() {
        super("Elephant", EntityElephant.class, EnumCreatureType.creature);
        setSpawnProperties(10, 100, 1, 2);
        setRegistrationProperties(128, 3, true);
        setDropAmount(0, 4);

        eggColor1 = (88 << 16) + (67 << 8) + 50;
        eggColor2 = (190 << 16) + (165 << 8) + 145;

        defaultBiomesToSpawn.add(BiomeGenBase.plains.biomeName);
        defaultBiomesToSpawn.add("Savanna");
    }

    @Override
    public void outputDataToList(Configuration config, CustomMobData customMobData) {
        ConfigHelper.configDropToMobData(config, "MOB CONTROLS." + mobName, customMobData, ItemList.scaleItem, 0, 10);
        ConfigHelper.configDropToMobData(config, "MOB CONTROLS." + mobName, customMobData, ItemList.scrapMeat, 0, 10);
        ConfigHelper.configDropToMobData(config, "MOB CONTROLS." + mobName, customMobData,
                ItemList.genericCraftingItems, ItemGenerics.Properties.SmallHeart.meta(), 5);
        super.outputDataToList(config, customMobData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderLiving getEntityrender(Class<? extends EntityLivingBase> entityClass) {
        return new RenderGenericLiving(new ModelElephant(), 0.5f, new ResourceLocation(DefaultProps.mobKey,
                "elephant.png"));
    }
}
