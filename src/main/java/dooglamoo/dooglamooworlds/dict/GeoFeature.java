/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.dict;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class GeoFeature
{
	public enum Type {Block, OreBlock, Feature, Biome};
	
	public static final int INDEX_ZERO = 0;
	public static final int INDEX_ONE = 1;
	public static final int INDEX_ELEVATION_LOW = 2;
	public static final int INDEX_DENSITY_LOW = 3;
	public static final int INDEX_UPLIFT_LOW = 4;
	public static final int INDEX_VOLCANISM_LOW = 5;
	public static final int INDEX_ERA_LOW = 6;
	public static final int INDEX_EROSION_LOW = 7;
	public static final int INDEX_TEMPERATURE_LOW = 8;
	public static final int INDEX_PRECIPITATION_LOW = 9;
	public static final int INDEX_ELEVATION_HIGH = 10;
	public static final int INDEX_DENSITY_HIGH = 11;
	public static final int INDEX_UPLIFT_HIGH = 12;
	public static final int INDEX_VOLCANISM_HIGH = 13;
	public static final int INDEX_ERA_HIGH = 14;
	public static final int INDEX_EROSION_HIGH = 15;
	public static final int INDEX_TEMPERATURE_HIGH = 16;
	public static final int INDEX_PRECIPITATION_HIGH = 17;
	
	public static final int INDEX_CRUST = 0;
	
	private Type type;
	public BlockState blockstate = null;
	private ConfiguredFeature<?, ?> feature;
	public int spacing = 127;
	public Biome biome = Biomes.THE_VOID;
	public float[] rarity = new float[3];
	public int level;
	public int requirement;
	public int[] modifier = new int[2];
	
	private int minTemp = 0, maxTemp = 3;
	private int minPrecip = 0, maxPrecip = 3;
	private int minLift = 0, maxLift = 3;
	private int minErosion = 0, maxErosion = 3;
	private int minThermal = 0, maxThermal = 3;
	private int minAge = 0, maxAge = 3;
	private int minPlates = 0, maxPlates = 3;
	private int minRock = 0, maxRock = 3;
	
	public GeoFeature(BlockState blockType, int level)
	{
		this.blockstate = blockType;
		this.type = Type.Block;
		this.level = level;
	}
	
	public GeoFeature(BlockState blockType, int spacing, int depth)
	{
		this.blockstate = blockType;
		this.type = Type.OreBlock;
		this.spacing = spacing;
		this.level = depth == -1 ? INDEX_CRUST : Math.max(depth, 9);
	}
	
	public GeoFeature(ConfiguredFeature<?, ?> feature, float occurance)
	{
		if (feature == null)
		{
			Thread.dumpStack();
		}
		this.feature = feature;
		rarity[0] = occurance;
		if (rarity[0] < 0.0f)
		{
			rarity[0] = 0.0f;
		}
		else if (rarity[0] > 1.0f)
		{
			rarity[0] = 1.0f;
		}
		type = Type.Feature;
	}
	
	public GeoFeature(Biome biome, int level)
	{
		this.biome = biome;
		this.level = level;
		type = Type.Biome;
	}
	
	@Override
	public String toString()
	{
		switch (type)
		{
		case Block: return "Block " + blockstate;
		case OreBlock: return "OreBlock " + blockstate + ","	+ spacing + "," + level + ","	+ requirement + ","	+ rarity[0] + "," + rarity[1] + ","	+ rarity[2] + "," + modifier[0] + "," + modifier[1] + "," + minRock + "," + maxRock;
		case Feature: return "Feature " + feature.getClass();
		case Biome: return "Biome " + biome.getRegistryName();
		default: return "Unknown material";
		}
	}
	
	public Type getType()
	{
		return type;
	}
	
	public Biome getBiome()
	{
		return biome;
	}
	
	public int getSpacing()
	{
		return spacing;
	}
	
	public BlockState getBlockType()
	{
		return blockstate;
	}
	
	public float getRarity()
	{
		return rarity[0];
	}
	
	public ConfiguredFeature<?, ?> getFeature()
	{
		return feature;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setMinTemperature(int temp) { minTemp = temp; }
	public void setMaxTemperature(int temp) { maxTemp = temp; }
	public void setMinPrecipitation(int percip) { minPrecip = percip; }
	public void setMaxPrecipitation(int percip) { maxPrecip = percip; }
	public void setMinUplift(int lift) { minLift = lift; }
	public void setMaxUplift(int lift) { maxLift = lift; }
	public void setMinErosion(int erosion) { minErosion = erosion; }
	public void setMaxErosion(int erosion) { maxErosion = erosion; }
	public void setMinVolcanism(int thermal) { minThermal = thermal; }
	public void setMaxVolcanism(int thermal) { maxThermal = thermal; }
	public void setMinEra(int age) { minAge = age; }
	public void setMaxEra(int age) { maxAge = age; }
	public void setMinElevation(int plates) { minPlates = plates; }
	public void setMaxElevation(int plates) { maxPlates = plates; }
	public void setMinDensity(int rock) { minRock = rock; }
	public void setMaxDensity(int rock) { maxRock = rock; }
	
	public int getMinTemperature() { return minTemp; }
	public int getMaxTemperature() { return maxTemp; }
	public int getMinPrecipitation() { return minPrecip; }
	public int getMaxPrecipitation() { return maxPrecip; }
	public int getMinUplift() { return minLift; }
	public int getMaxUplift() { return maxLift; }
	public int getMinErosion() { return minErosion; }
	public int getMaxErosion() { return maxErosion; }
	public int getMinVolcanism() { return minThermal; }
	public int getMaxVolcanism() { return maxThermal; }
	public int getMinEra() { return minAge; }
	public int getMaxEra() { return maxAge; }
	public int getMinElevation() { return minPlates; }
	public int getMaxElevation() { return maxPlates; }
	public int getMinDensity() { return minRock; }
	public int getMaxDensity() { return maxRock; }
}
