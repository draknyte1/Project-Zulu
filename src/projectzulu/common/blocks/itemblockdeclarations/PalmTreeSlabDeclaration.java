package projectzulu.common.blocks.itemblockdeclarations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import projectzulu.common.api.BlockList;
import projectzulu.common.blocks.BlockZuluSlab;
import projectzulu.common.blocks.ItemZuluSlab;
import projectzulu.common.core.DefaultProps;
import projectzulu.common.core.itemblockdeclaration.BlockDeclaration;

import com.google.common.base.Optional;

import cpw.mods.fml.common.registry.GameRegistry;

public class PalmTreeSlabDeclaration extends BlockDeclaration {

    public PalmTreeSlabDeclaration() {
        super("PalmTreeSlab", 1);
    }

    @Override
    protected boolean createBlock(int iD) {
        if (BlockList.palmTreePlank.isPresent()) {
            BlockList.palmTreeSlab = Optional.of((new BlockZuluSlab(iD, false, BlockList.palmTreePlank.get()))
                    .setUnlocalizedName(name.toLowerCase()).func_111022_d(
                            DefaultProps.blockKey + ":" + name.toLowerCase()));
            return true;
        }
        return false;
    }

    @Override
    protected void registerBlock() {
        if (BlockList.palmTreeDoubleSlab.isPresent() && BlockList.palmTreeSlab.isPresent()) {
            Block block = BlockList.palmTreeSlab.get();
            ItemZuluSlab.initialise((BlockHalfSlab) BlockList.palmTreeSlab.get(),
                    (BlockHalfSlab) BlockList.palmTreeDoubleSlab.get());
            GameRegistry.registerBlock(block, ItemZuluSlab.class, name.toLowerCase());
            OreDictionary.registerOre("slabWood", new ItemStack(block));
            OreDictionary.registerOre("slabPalm", new ItemStack(block));
        }
    }
}
