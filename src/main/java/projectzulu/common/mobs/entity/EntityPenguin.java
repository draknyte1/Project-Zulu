package projectzulu.common.mobs.entity;

import java.util.EnumSet;

import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import projectzulu.common.api.BlockList;
import projectzulu.common.core.DefaultProps;
import projectzulu.common.mobs.entityai.EntityAIAttackOnCollide;
import projectzulu.common.mobs.entityai.EntityAIFollowParent;
import projectzulu.common.mobs.entityai.EntityAIHurtByTarget;
import projectzulu.common.mobs.entityai.EntityAIMate;
import projectzulu.common.mobs.entityai.EntityAINearestAttackableTarget;
import projectzulu.common.mobs.entityai.EntityAIPanic;
import projectzulu.common.mobs.entityai.EntityAITempt;
import projectzulu.common.mobs.entityai.EntityAIWander;
import cpw.mods.fml.common.Loader;

public class EntityPenguin extends EntityGenericAnimal implements IAnimals {

    public EntityPenguin(World par1World) {
        super(par1World);
        setSize(0.9f, 1.5f);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25f));

        tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0f, false));
        // tasks.addTask(4, new EntityAIFollowOwner(this, moveSpeed, 10.0F, 2.0F));

        tasks.addTask(5, new EntityAIMate(this, 1.0f));
        tasks.addTask(6, new EntityAITempt(this, 1.2f, Items.fish, false));
        tasks.addTask(6, new EntityAITempt(this, 1.2f, Items.cooked_fished, false));
        tasks.addTask(7, new EntityAIFollowParent(this, 1.1f));

        tasks.addTask(9, new EntityAIWander(this, 1.0f, 120));

        targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, false));
        targetTasks.addTask(4,
                new EntityAINearestAttackableTarget(this, EnumSet.of(EntityStates.attacking, EntityStates.looking),
                        EntityPlayer.class, 16.0F, 0, true));
        // targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLiving.class, 16.0F, 0, false, true,
        // IMob.mobSelector));
    }
    
    @Override
    protected boolean isValidLocation(World world, int xCoord, int yCoord, int zCoord) {
        return worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord);
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    @Override
    public int getTotalArmorValue() {
        return 0;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return DefaultProps.coreKey + ":" + DefaultProps.entitySounds + "penguinhurt";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return DefaultProps.coreKey + ":" + DefaultProps.entitySounds + "penguinhurt";
    }

    /**
     * Checks if the Provided ItemStack is considered an item that should be used for Breeding This is overriden by each
     * Entity if deviations from default are desired
     */
    @Override
    public boolean isValidBreedingItem(ItemStack itemStack) {
        if (itemStack != null
                && (itemStack.getItem() == Items.fish || itemStack.getItem() == Items.cooked_fished)) {
            return true;
        } else {
            return super.isValidBreedingItem(itemStack);
        }
    }

    @Override
    protected void dropRareDrop(int par1) {
        if (Loader.isModLoaded(DefaultProps.BlocksModId) && BlockList.mobHeads.isPresent()) {
            entityDropItem(new ItemStack(BlockList.mobHeads.get(), 1, 13), 1);
        }
        super.dropRareDrop(par1);
    }

}