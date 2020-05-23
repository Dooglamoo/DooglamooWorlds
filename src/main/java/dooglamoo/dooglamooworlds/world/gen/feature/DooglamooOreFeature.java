/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import dooglamoo.dooglamooworlds.dict.DictionaryFactory;
import dooglamoo.dooglamooworlds.dict.GeoFeature;
import dooglamoo.dooglamooworlds.world.gen.DooglamooChunkGenerator;
import dooglamoo.dooglamooworlds.world.gen.GeoData;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class DooglamooOreFeature extends Feature<NoFeatureConfig>
{
	public double[] factors = new double[18];
	public int[] levels = new int[9];
	public List<GeoFeature> density0;
	public List<GeoFeature> density1;
	public List<GeoFeature> density2;
	public List<GeoFeature> density3;
	
	public DooglamooOreFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> deserializer)
	{
		super(deserializer);
		
		factors[GeoFeature.INDEX_ZERO] = 0.0;
		factors[GeoFeature.INDEX_ONE] = 1.0;
    }

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		if (!(generator instanceof DooglamooChunkGenerator))
		{
			return false;
		}
		
		BlockPos.Mutable blockpos = new BlockPos.Mutable();
		
		DooglamooChunkGenerator gen = (DooglamooChunkGenerator)generator;
		
		GeoData geoData = gen.biomeProvider.getGeoData(pos.getX(), pos.getZ());
		
		density0 = DictionaryFactory.ores.get(0);
		if (density0 == null)
		{
			density0 = new ArrayList<GeoFeature>();
		}
		density1 = DictionaryFactory.ores.get(1);
		if (density1 == null)
		{
			density1 = new ArrayList<GeoFeature>();
		}
		density2 = DictionaryFactory.ores.get(2);
		if (density2 == null)
		{
			density2 = new ArrayList<GeoFeature>();
		}
		density3 = DictionaryFactory.ores.get(3);
		if (density3 == null)
		{
			density3 = new ArrayList<GeoFeature>();
		}
		
		for (int n = 0; n < 16; ++n)
		{
			int x1 = pos.getX() + n;
			
			for (int m = 0; m < 16; ++m)
			{
				int z1 = pos.getZ() + m;
				
				double elevation = geoData.elevation[n][m];
				double density = geoData.density[n][m];
				double uplift = geoData.uplift[n][m];
				double volcanism = geoData.volcanism[n][m];
				double era = geoData.era[n][m];
				double erosion = geoData.erosion[n][m];
				double temperature = geoData.temperature[n][m];
				double precipitation = geoData.precipitation[n][m];
				
				factors[GeoFeature.INDEX_ELEVATION_LOW] = elevation < 0.0 ? -elevation : 0.0;
				factors[GeoFeature.INDEX_DENSITY_LOW] = density < 0.0 ? -density : 0.0;
				factors[GeoFeature.INDEX_UPLIFT_LOW] = uplift < 0.0 ? -uplift : 0.0;
				factors[GeoFeature.INDEX_VOLCANISM_LOW] = volcanism < 0.0 ? -volcanism : 0.0;
				factors[GeoFeature.INDEX_ERA_LOW] = era < 0.0 ? -era : 0.0;
				factors[GeoFeature.INDEX_EROSION_LOW] = erosion < 0.0 ? -erosion : 0.0;
				factors[GeoFeature.INDEX_TEMPERATURE_LOW] = temperature < 0.0 ? -temperature : 0.0;
				factors[GeoFeature.INDEX_PRECIPITATION_LOW] = precipitation < 0.0 ? -precipitation : 0.0;
				factors[GeoFeature.INDEX_ELEVATION_HIGH] = elevation > 0.0 ? elevation : 0.0;
				factors[GeoFeature.INDEX_DENSITY_HIGH] = density > 0.0 ? density : 0.0;
				factors[GeoFeature.INDEX_UPLIFT_HIGH] = uplift > 0.0 ? uplift : 0.0;
				factors[GeoFeature.INDEX_VOLCANISM_HIGH] = volcanism > 0.0 ? volcanism : 0.0;
				factors[GeoFeature.INDEX_ERA_HIGH] = era > 0.0 ? era : 0.0;
				factors[GeoFeature.INDEX_EROSION_HIGH] = erosion > 0.0 ? erosion : 0.0;
				factors[GeoFeature.INDEX_TEMPERATURE_HIGH] = temperature > 0.0 ? temperature : 0.0;
				factors[GeoFeature.INDEX_PRECIPITATION_HIGH] = precipitation > 0.0 ? precipitation : 0.0;
				
				int upliftLevel = geoData.upliftLevel[n][m];
				
				for (int y = 0; y < 256; y++)
				{
					BlockState currentstate = world.getBlockState(blockpos.setPos(x1, y, z1));
					if (currentstate.getMaterial() != Material.ROCK || currentstate.getBlock() == Blocks.BEDROCK || currentstate.getBlock() == Blocks.MAGMA_BLOCK)
					{
						continue;
					}
					
					BlockState blockstate = Blocks.AIR.getDefaultState();
					
					if (y <= upliftLevel) // also check y > mantleLevel
					{
						// move filtering check into here

						if (density < -0.5)
						{
							// sedimentary rock (sandstone)
							for (int i = 0; i < density0.size(); i++)
							{
								GeoFeature ore = density0.get(i);
								if (y % ore.spacing == 0)
								{
									if (ore.rarity[0] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.0 && rand.nextFloat() < ore.rarity[0] + factors[ore.modifier[0]] * ore.rarity[0] + factors[ore.modifier[1]] * ore.rarity[0])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 1) % ore.spacing == 0)
								{
									if (ore.rarity[1] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.3 && rand.nextFloat() < ore.rarity[1] + factors[ore.modifier[0]] * ore.rarity[1] + factors[ore.modifier[1]] * ore.rarity[1])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 2) % ore.spacing == 0)
								{
									if (ore.rarity[2] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.7 && rand.nextFloat() < ore.rarity[2] + factors[ore.modifier[0]] * ore.rarity[2] + factors[ore.modifier[1]] * ore.rarity[2])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
							}
						}
						else if (density < 0.0)
						{
							// soft rock (limestone)
							for (int i = 0; i < density1.size(); i++)
							{
								GeoFeature ore = density1.get(i);
								if (y % ore.spacing == 0)
								{
									if (ore.rarity[0] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.0 && rand.nextFloat() < ore.rarity[0] + factors[ore.modifier[0]] * ore.rarity[0] + factors[ore.modifier[1]] * ore.rarity[0])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 1) % ore.spacing == 0)
								{
									if (ore.rarity[1] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.3 && rand.nextFloat() < ore.rarity[1] + factors[ore.modifier[0]] * ore.rarity[1] + factors[ore.modifier[1]] * ore.rarity[1])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 2) % ore.spacing == 0)
								{
									if (ore.rarity[2] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.7 && rand.nextFloat() < ore.rarity[2] + factors[ore.modifier[0]] * ore.rarity[2] + factors[ore.modifier[1]] * ore.rarity[2])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
							}
						}
						else if (density < 0.5)
						{
							// hard rock (granite)
							for (int i = 0; i < density2.size(); i++)
							{
								GeoFeature ore = density2.get(i);
								if (y % ore.spacing == 0)
								{
									if (ore.rarity[0] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.0 && rand.nextFloat() < ore.rarity[0] + factors[ore.modifier[0]] * ore.rarity[0] + factors[ore.modifier[1]] * ore.rarity[0])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 1) % ore.spacing == 0)
								{
									if (ore.rarity[1] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.3 && rand.nextFloat() < ore.rarity[1] + factors[ore.modifier[0]] * ore.rarity[1] + factors[ore.modifier[1]] * ore.rarity[1])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 2) % ore.spacing == 0)
								{
									if (ore.rarity[2] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.7 && rand.nextFloat() < ore.rarity[2] + factors[ore.modifier[0]] * ore.rarity[2] + factors[ore.modifier[1]] * ore.rarity[2])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
							}
						}
						else
						{
							// igneous rock (diorite)
							for (int i = 0; i < density3.size(); i++)
							{
								GeoFeature ore = density3.get(i);
								if (y % ore.spacing == 0)
								{
									if (ore.rarity[0] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.0 && rand.nextFloat() < ore.rarity[0] + factors[ore.modifier[0]] * ore.rarity[0] + factors[ore.modifier[1]] * ore.rarity[0])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 1) % ore.spacing == 0)
								{
									if (ore.rarity[1] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.3 && rand.nextFloat() < ore.rarity[1] + factors[ore.modifier[0]] * ore.rarity[1] + factors[ore.modifier[1]] * ore.rarity[1])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
								else if ((y + 2) % ore.spacing == 0)
								{
									if (ore.rarity[2] != 0.0 && y <= (ore.level < 9 ? levels[ore.level] : ore.level) && factors[ore.requirement] > 0.7 && rand.nextFloat() < ore.rarity[2] + factors[ore.modifier[0]] * ore.rarity[2] + factors[ore.modifier[1]] * ore.rarity[2])
									{
										blockstate = ore.blockstate;
										break;
									}
								}
							}
						}
					}
					
					if (blockstate.getMaterial() != Material.AIR)
					{
						world.setBlockState(blockpos.setPos(x1, y, z1), blockstate, 2);
					}
				}
			}
		}
		
		return true;
	}
}
