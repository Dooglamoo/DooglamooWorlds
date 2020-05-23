/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
//import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.IPlantable;

public class FallenTreeFeature extends Feature<BlockClusterFeatureConfig>
{
	public FallenTreeFeature(Function<Dynamic<?>, ? extends BlockClusterFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config)
	{
        if (!canPlace(worldIn, pos))
        {
            return false;
        }
        
        BlockState blockstate = config.stateProvider.getBlockState(rand, pos);
        int dir = rand.nextInt(4);
        if (dir == 0)
        {
            for (int length = 2 + rand.nextInt(4), x = 0; x < length && this.canPlace(worldIn, pos.add(x, 0, 0)); x++)
            {
            	worldIn.setBlockState(pos.add(x, 0, 0), blockstate.with(LogBlock.AXIS, Direction.Axis.X), 3 & -2 | 16);
            }
            return true;
        }
        else if (dir == 1)
        {
            for (int length = 2 + rand.nextInt(4), z = 0; z < length && this.canPlace(worldIn, pos.add(0, 0, z)); z++)
            {
            	worldIn.setBlockState(pos.add(0, 0, z), blockstate.with(LogBlock.AXIS, Direction.Axis.Z), 3 & -2 | 16);
            }
            return true;
        }
        
        worldIn.setBlockState(pos, blockstate, 3 & -2 | 16);
        return true;
	}
	
	private boolean canPlace(IWorld worldIn, BlockPos pos)
    {
    	BlockState state = worldIn.getBlockState(pos.down());
        return state.getBlock().canSustainPlant(state, worldIn, pos.down(), Direction.UP, (IPlantable)Blocks.OAK_SAPLING) && worldIn.getBlockState(pos).getMaterial().isReplaceable();
    }
}
