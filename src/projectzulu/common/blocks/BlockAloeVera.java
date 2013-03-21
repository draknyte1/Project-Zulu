package projectzulu.common.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import projectzulu.common.api.BlockList;
import projectzulu.common.api.ItemList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAloeVera extends BlockFlower {
	public static final String[] imageSuffix = new String[] {"_b0", "_b1", "_b2", "_b3","_t0", "_t1", "_t2", "_t3", "_t4"};
    @SideOnly(Side.CLIENT)
    private Icon[] blockIcons;

	public BlockAloeVera(int i, int j){
		super(i, Material.plants);
		setTickRandomly(true);
		this.disableStats();
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
    	return blockIcons[par2];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister){
        this.blockIcons = new Icon[imageSuffix.length];
        for (int i = 0; i < this.blockIcons.length; ++i){
            this.blockIcons[i] = par1IconRegister.registerIcon(getUnlocalizedName2()+imageSuffix[i]);
        }
    }
    
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4,
			Random par5Random) {
    	//See if Aloe Vera Should Grow
		//Control Bottom Alor Vera
		int tempAVMeta = par1World.getBlockMetadata(par2, par3, par4);	
		int waterRate = waterGrowthRate(par1World, par2, par3, par4);
		int weedRate = weedGrowthRate(par1World, par2, par3, par4);
		int probabilityOutOf = 200;
		
		if (tempAVMeta < 3) {
			int holdRand = par5Random.nextInt(probabilityOutOf);
			//Growth is proportional to the lightlevel, more light more likely to advance
			if( par1World.getLightBrightness(par2, par3, par4) - holdRand >= 0 ){
				par1World.setBlock(par2, par3, par4, BlockList.aloeVera.get().blockID, tempAVMeta+1, 3);
			}
		}
		
		//Final Stage of Bottom AloeVera, Created the flower block above
		if(tempAVMeta == 3 && waterRate > 1){
			//If Meta Data is at 3 or then we check to see if we can grow flower above and tumbleweed
			//Check if Air is above and the block below is not aloeVera. If So, place the top 'Flower Aloe Vera' above
			if(par1World.getBlockId(par2, par3+1, par4) == 0 && par1World.getBlockId(par2, par3-1, par4) != BlockList.aloeVera.get().blockID){
				int holdRand = par5Random.nextInt(probabilityOutOf);
				if( par1World.getLightBrightness(par2, par3+1, par4) - holdRand >= 0 ){
					par1World.setBlock(par2, par3+1, par4, BlockList.aloeVera.get().blockID, 4, 3);
				}
			}
		}
		
		//Following Section of Code deals with Flower block, but WaterRate needs to be recalculate based on water level not to
		//to Flower block coords but to base coords
		waterRate = waterGrowthRate(par1World, par2, par3-1, par4);
		weedRate = weedGrowthRate(par1World, par2, par3-1, par4);
		
		
		//Deals with the flower block, if its 3-8 and is above an Aloe Vera Block (The Bottom) then it needs to grow
		if(tempAVMeta > 3 && tempAVMeta < 8 && waterRate > 1 && par1World.getBlockId(par2, par3-1, par4) == BlockList.aloeVera.get().blockID){
			int holdRand = par5Random.nextInt(probabilityOutOf);
			//Growth is proportional to the lightlevel, more light more likely to advance
			if( par1World.getLightBrightness(par2, par3, par4) - holdRand >= 0 ){
				par1World.setBlock(par2, par3, par4, BlockList.aloeVera.get().blockID, tempAVMeta+1, 3);
			}			
		}

		//If Flower is at final stage, it needs to spawn tumbleweed and destroy itself
		if(tempAVMeta == 8 && par1World.getBlockId(par2, par3, par4) == BlockList.aloeVera.get().blockID && BlockList.tumbleweed.isPresent()){
			int holdRand = par5Random.nextInt(probabilityOutOf);
			if(weedRate - holdRand >= 0){
				//Check in 3*3 square For air to place tumbleweed, cannot be placed over water

				Vec3 suitableBlockLoc = findSuitableBlockLoc(par1World, par2, par3-1, par4);
				//Place Tumbleweed
				if (suitableBlockLoc != null){
					//Place Tumbleweed at that desired location
					par1World.setBlock((int)suitableBlockLoc.xCoord, (int)suitableBlockLoc.yCoord, (int)suitableBlockLoc.zCoord, BlockList.tumbleweed.get().blockID);
					//Remove Flower as Tumbleweed is dead plant I guess
					par1World.setBlock(par2, par3, par4, 0);
				}
			}
		}
		
		//Water Food Growth
		//Check That Block Below is Sand and if Random replace with watered dirt stage 1
		int holdRand = par5Random.nextInt(probabilityOutOf/2);
		if(par1World.getBlockId(par2, par3-1, par4) == Block.dirt.blockID && waterRate - holdRand >= 0){
			if( BlockList.wateredDirt.isPresent() ){
				par1World.setBlock(par2, par3-1, par4, BlockList.wateredDirt.get().blockID, 0, 3);
			}
		}
		if(par1World.getBlockId(par2, par3-1, par4) == Block.sand.blockID && waterRate - holdRand >= 0){
			if( BlockList.wateredDirt.isPresent() ){
				par1World.setBlock(par2, par3-1, par4, BlockList.wateredDirt.get().blockID, 4, 3);
			}
		}

		
		if(BlockList.wateredDirt.isPresent() && par1World.getBlockId(par2, par3-1, par4) == BlockList.wateredDirt.get().blockID && waterRate - holdRand >= 0){
			int tempMeta = par1World.getBlockMetadata(par2, par3-1, par4);
			if(tempMeta != 3 &&  tempMeta != 7){
				par1World.setBlock(par2, par3-1, par4, BlockList.wateredDirt.get().blockID, tempMeta+1, 3);
			}
		}
		super.updateTick(par1World, par2, par3, par4, par5Random);
		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, 1); //225/2
	}

	private Vec3 findSuitableBlockLoc(World par1World, int par2, int par3, int par4){
		for(int i = -1; i < 2; i++){
			//Will Only Place Tumbleweed on same level or down one
			for (int j = -1; j < 1; j++) {
				for (int k = -1; k < 2 ; k++) {
					
					if ( par1World.getBlockId(par2+i, par3+j, par4+k) == 0 
							&& par1World.getBlockId(par2+i, par3+j-1, par4+k) != Block.waterMoving.blockID
							&& par1World.getBlockId(par2+i, par3+j-1, par4+k) != Block.waterStill.blockID
							&& par1World.getBlockId(par2+i, par3+j-1, par4+k) != 0){
						return Vec3.createVectorHelper(par2+i, par3+j, par4+k);
					}
				}
			}
		}
		return null;
	}	
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		par1World.scheduleBlockUpdate(par2, par3, par4, BlockList.aloeVera.get().blockID, 4);
		
		super.onBlockAdded(par1World, par2, par3, par4);
	}

	private int waterGrowthRate(World par1World, int par2, int par3, int par4){
		//Blocks to Check
		//Check Blocks in a 5x5 Grid centered on Block
		int waterGrowthRate = 1;
		for(int i = -4; i < 5; i++){
			for (int j = -4; j < 5; j++) {
				//y axis
				for(int k = -1; k<1;k++){
					int iD = par1World.getBlockId(par2+i, par3+k, par4+j);
					if(iD == Block.waterMoving.blockID || iD == Block.waterStill.blockID){
						waterGrowthRate +=1;
						break;
					}
				}
			}
		}
		return Math.min(waterGrowthRate, 30);
	}
    private int weedGrowthRate(World par1World, int par2, int par3, int par4){
    	//Blocks to Check
    	//Check Blocks in a 5x5 Grid centered on Block
    	int weedGrowthRate = 0;
    	for(int i = -4; i < 5; i++){
    		for (int j = -4; j < 5; j++) {
    			//y axis
    			for(int k = -1; k<1;k++){

    				int iD = par1World.getBlockId(par2+i, par3+k, par4+j);
    				//Nearby Water Speeds Up Growth
    				if(iD == Block.waterMoving.blockID || iD == Block.waterStill.blockID){
    					weedGrowthRate +=1;
    					break;
    				}
    			}
    		}
    	}
    	return Math.min(weedGrowthRate, 30);
    }
        
    @Override 
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune){
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (metadata < 3){
        	if(ItemList.aloeVeraSeeds.isPresent()){
                ret.add(new ItemStack(ItemList.aloeVeraSeeds.get()));
        	}
            return ret;
        }
        
        if(metadata == 3){
            ret.add(new ItemStack(this));
        	if(ItemList.aloeVeraSeeds.isPresent()){
                ret.add(new ItemStack(ItemList.aloeVeraSeeds.get()));
                ret.add(new ItemStack(ItemList.aloeVeraSeeds.get()));
        	}
            return ret;
        }
        
        if (metadata == 8){
        	if(ItemList.aloeVeraSeeds.isPresent()){
    			ret.add(new ItemStack(Item.dyePowder.itemID, 1, 11));
    			ret.add(new ItemStack(Item.dyePowder.itemID, 1, 11));
        	}
            return ret;
        }
        
        if(metadata == 7){
			ret.add(new ItemStack(Item.dyePowder.itemID, 1, 11));
        }        
        return ret;
    }
	
    public boolean isOpaqueCube(){
            return false;
    }

    public boolean renderAsNormalBlock(){
            return false;
    }

    public int getRenderType(){
            return 1;
    }
        
    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int i){
    	if(BlockList.wateredDirt.isPresent() && i == BlockList.wateredDirt.get().blockID){
    		return true;
    	}
    	return i == Block.grass.blockID || i == Block.dirt.blockID || i == Block.tilledField.blockID 
            		|| i == Block.sand.blockID || i == BlockList.aloeVera.get().blockID;
    }
}
