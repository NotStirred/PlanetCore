package com.PlanetCore.blocks;

import com.PlanetCore.init.ModBlocks;
import com.PlanetCore.util.IMetaName;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CorestoneOreCompact extends Corestone implements IMetaName {

    public final PlanetMaterial planetMaterial;
    public final PlanetHardness planetHardness;
    private static final float [] coreHardnessByMeta = {11158, 16737, 25105, 37658, 56488, 84732, 127098};

    public CorestoneOreCompact(String name, Material material, PlanetMaterial planetMaterial, PlanetHardness planetHardness)
    {
        super(name, material);
        this.planetMaterial = planetMaterial;
        this.planetHardness = planetHardness;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        int meta = getMetaFromState(blockState);
        return (planetHardness.hardness * 4) + coreHardnessByMeta[meta];
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int i) {
        return this.planetMaterial.droppedItem;
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            int i = 0;
            if (this == ModBlocks.CORESTONE_COMPACT_EMERALD) {
                i = MathHelper.getInt(rand, 6, 12);
            } else if (this == ModBlocks.CORESTONE_COMPACT_RUBY || this == ModBlocks.CORESTONE_COMPACT_SAPPHIRE || this == ModBlocks.CORESTONE_COMPACT_DIAMOND) {
                i = MathHelper.getInt(rand, 9, 18);
            } else if (this == ModBlocks.CORESTONE_COMPACT_OLIVINE || this == ModBlocks.CORESTONE_COMPACT_WADSLEYITE || this == ModBlocks.CORESTONE_COMPACT_RINGWOODITE || this == ModBlocks.CORESTONE_COMPACT_BRIGMANITE || this == ModBlocks.CORESTONE_COMPACT_MAJORITE) {
                i = MathHelper.getInt(rand, 18, 36);
            } else if (this == ModBlocks.CORESTONE_COMPACT_AMAZONITE || this == ModBlocks.CORESTONE_COMPACT_ONYX) {
                i = MathHelper.getInt(rand, 36, 72);
            }
            return i;
        }
        else return 0;
    }
}
