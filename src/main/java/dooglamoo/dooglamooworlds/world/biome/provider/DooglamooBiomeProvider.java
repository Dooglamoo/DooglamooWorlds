/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.biome.provider;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import dooglamoo.dooglamooworlds.DooglamooConfig;
import dooglamoo.dooglamooworlds.dict.DictionaryFactory;
import dooglamoo.dooglamooworlds.dict.GeoFeature;
import dooglamoo.dooglamooworlds.world.gen.GeoData;
import dooglamoo.dooglamooworlds.world.gen.NoiseGenerator;
import dooglamoo.dooglamooworlds.world.gen.NoiseGeneratorPerlin;
import dooglamoo.dooglamooworlds.world.gen.RiverRegion;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

public class DooglamooBiomeProvider extends BiomeProvider
{
	private static final Set<Biome> field_226847_e_ = ImmutableSet.of(Biomes.OCEAN, Biomes.PLAINS, Biomes.DESERT, Biomes.MOUNTAINS, Biomes.FOREST, Biomes.TAIGA, Biomes.SWAMP, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_MOUNTAINS, Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELD_SHORE, Biomes.BEACH, Biomes.DESERT_HILLS, Biomes.WOODED_HILLS, Biomes.TAIGA_HILLS, Biomes.MOUNTAIN_EDGE, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.JUNGLE_EDGE, Biomes.DEEP_OCEAN, Biomes.STONE_SHORE, Biomes.SNOWY_BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.GIANT_TREE_TAIGA, Biomes.GIANT_TREE_TAIGA_HILLS, Biomes.WOODED_MOUNTAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.BADLANDS, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.BADLANDS_PLATEAU, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT_LAKES, Biomes.GRAVELLY_MOUNTAINS, Biomes.FLOWER_FOREST, Biomes.TAIGA_MOUNTAINS, Biomes.SWAMP_HILLS, Biomes.ICE_SPIKES, Biomes.MODIFIED_JUNGLE, Biomes.MODIFIED_JUNGLE_EDGE, Biomes.TALL_BIRCH_FOREST, Biomes.TALL_BIRCH_HILLS, Biomes.DARK_FOREST_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS, Biomes.GIANT_SPRUCE_TAIGA, Biomes.GIANT_SPRUCE_TAIGA_HILLS, Biomes.MODIFIED_GRAVELLY_MOUNTAINS, Biomes.SHATTERED_SAVANNA, Biomes.SHATTERED_SAVANNA_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.MODIFIED_BADLANDS_PLATEAU);
	
	public static final int TEMP_COLD = 0 << 14;
	public static final int TEMP_COOL = 1 << 14;
	public static final int TEMP_WARM = 2 << 14;
	public static final int TEMP_HOT = 3 << 14;
	public static final int TEMP_MASK = 3 << 14;
	
	public static final int PRECIP_DRY = 0 << 12;
	public static final int PRECIP_MOIST = 1 << 12;
	public static final int PRECIP_HUMID = 2 << 12;
	public static final int PRECIP_WET = 3 << 12;
	public static final int PRECIP_MASK = 3 << 12;
	
	public static final int LIFT_SUNK = 0 << 10;
	public static final int LIFT_LEVEL = 1 << 10;
	public static final int LIFT_HILLS = 2 << 10;
	public static final int LIFT_MOUNTAINS = 3 << 10;
	public static final int LIFT_MASK = 3 << 10;
	
	public static final int EROSION_NONE = 0 << 8;
	public static final int EROSION_SOME = 1 << 8;
	public static final int EROSION_MOST = 2 << 8;
	public static final int EROSION_FULL = 3 << 8;
	public static final int EROSION_MASK = 3 << 8;
	
	public static final int THERMAL_NONE = 0 << 6;
	public static final int THERMAL_SOME = 1 << 6;
	public static final int THERMAL_MOST = 2 << 6;
	public static final int THERMAL_FULL = 3 << 6;
	public static final int THERMAL_MASK = 3 << 6;
	
	public static final int AGE_YOUNGEST = 0 << 4;
	public static final int AGE_YOUNG = 1 << 4;
	public static final int AGE_OLD = 2 << 4;
	public static final int AGE_OLDEST = 3 << 4;
	public static final int AGE_MASK = 3 << 4;
	
	public static final int PLATES_OCEAN = 0 << 2;
	public static final int PLATES_SHALLOWS = 1 << 2;
	public static final int PLATES_PLAINS = 2 << 2;
	public static final int PLATES_PLATEAU = 3 << 2;
	public static final int PLATES_MASK = 3 << 2;
	
	public static final int ROCK_SEDIMENTARY = 0;
	public static final int ROCK_SOFT = 1;
	public static final int ROCK_HARD = 2;
	public static final int ROCK_IGNEOUS = 3;
	public static final int ROCK_MASK = 3;
	
	public final NoiseGenerator surfaceGen;
	public final NoiseGenerator elevationGen;
	public final NoiseGenerator densityGen;
	public final NoiseGenerator upliftGen;
	public final NoiseGenerator volcanismGen;
	public final NoiseGenerator eraGen;
	public final NoiseGenerator erosionGen;
	public final NoiseGenerator temperatureGen;
	public final NoiseGenerator precipitationGen;
	
	// scale: 1.0 to 3.0
	public final double elevationScale;
	public final double densityScale;
	public final double upliftScale;
	public final double volcanismScale;
	public final double eraScale;
	public final double erosionScale;
	public final double temperatureScale;
	public final double precipitationScale;
	
	// amplifier: 0.0 to 2.0
	public final double elevationAmplitude;
	public final double densityAmplitude;
	public final double upliftAmplitude;
	public final double volcanismAmplitude;
	public final double eraAmplitude;
	public final double erosionAmplitude;
	public final double temperatureAmplitude;
	public final double precipitationAmplitude;
	
	// weight: -0.6 to 0.6
	public final double elevationWeight;
	public final double densityWeight;
	public final double upliftWeight;
	public final double volcanismWeight;
	public final double eraWeight;
	public final double erosionWeight;
	public final double temperatureWeight;
	public final double precipitationWeight;
	
	private final double riverWiggleHi;
	private final double riverWiggleLo;
	private final double riverWidthSquared;
	private final double riverBank;
	private final double riverShore;
	private final double riverChannel;
	private final double riverShallow;
	
	public final int riverRegionHalfWidth;
	public final int riverRegionWidth;
	
	public final double xClimateFactor;
	public final double zClimateFactor;
	private final Random rand;
	private final DooglamooBiomeProviderSettings settings;
	protected final Map<Long, GeoData> geoDataCache = new ConcurrentHashMap<Long, GeoData>();
	protected final Map<Long, RiverRegion> riverRegionCache = new ConcurrentHashMap<Long, RiverRegion>();
	
	public DooglamooBiomeProvider(DooglamooBiomeProviderSettings settings)
	{
		super(field_226847_e_);
		
		this.settings = settings;
		
		elevationScale = MathHelper.clamp(settings.getGeneratorSettings().elevationScale, 1.0, 3.0);
		densityScale = MathHelper.clamp(settings.getGeneratorSettings().densityScale, 1.0, 3.0);
		upliftScale = MathHelper.clamp(settings.getGeneratorSettings().upliftScale, 1.0, 3.0);
		volcanismScale = MathHelper.clamp(settings.getGeneratorSettings().volcanismScale, 1.0, 3.0);
		eraScale = MathHelper.clamp(settings.getGeneratorSettings().eraScale, 1.0, 3.0);
		erosionScale = MathHelper.clamp(settings.getGeneratorSettings().erosionScale, 1.0, 3.0);
		temperatureScale = MathHelper.clamp(settings.getGeneratorSettings().temperatureScale, 1.0, 3.0);
		precipitationScale = MathHelper.clamp(settings.getGeneratorSettings().precipitationScale, 1.0, 3.0);
		
		this.rand = new Random(settings.getWorldInfo().getSeed());
		
		surfaceGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.05 * 4.0);
		elevationGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.00016 * (-4.0 * elevationScale + 13.0));
		densityGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.0006 * (-4.0 * densityScale + 13.0));
		upliftGen = new NoiseGeneratorPerlin(rand, 6).setScale(0.004 * (-4.0 * upliftScale + 13.0));
		volcanismGen = new NoiseGeneratorPerlin(rand, 3).setScale(0.0001 * (-4.0 * volcanismScale + 13.0));
		eraGen = new NoiseGeneratorPerlin(rand, 4).setScale(0.0005 * (-4.0 * eraScale + 13.0));
		erosionGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.006 * (-4.0 * erosionScale + 13.0));
		temperatureGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.00016 * (-4.0 * temperatureScale + 13.0));
		precipitationGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.00016 * (-4.0 * precipitationScale + 13.0));
		
		elevationWeight = MathHelper.clamp(settings.getGeneratorSettings().elevationWeight, -0.6, 0.6);
		densityWeight = MathHelper.clamp(settings.getGeneratorSettings().densityWeight, -0.6, 0.6);
		upliftWeight = MathHelper.clamp(settings.getGeneratorSettings().upliftWeight, -0.6, 0.6);
		volcanismWeight = MathHelper.clamp(settings.getGeneratorSettings().volcanismWeight, -0.6, 0.6);
		eraWeight = MathHelper.clamp(settings.getGeneratorSettings().eraWeight, -0.6, 0.6);
		erosionWeight = MathHelper.clamp(settings.getGeneratorSettings().erosionWeight, -0.6, 0.6);
		temperatureWeight = MathHelper.clamp(settings.getGeneratorSettings().temperatureWeight, -0.6, 0.6);
		precipitationWeight = MathHelper.clamp(settings.getGeneratorSettings().precipitationWeight, -0.6, 0.6);
		
		elevationAmplitude = MathHelper.clamp(settings.getGeneratorSettings().elevationAmplitude, 0.0, 2.0);
		densityAmplitude = MathHelper.clamp(settings.getGeneratorSettings().densityAmplitude, 0.0, 2.0);
		upliftAmplitude = MathHelper.clamp(settings.getGeneratorSettings().upliftAmplitude, 0.0, 2.0);
		volcanismAmplitude = MathHelper.clamp(settings.getGeneratorSettings().volcanismAmplitude, 0.0, 2.0);
		eraAmplitude = MathHelper.clamp(settings.getGeneratorSettings().eraAmplitude, 0.0, 2.0);
		erosionAmplitude = MathHelper.clamp(settings.getGeneratorSettings().erosionAmplitude, 0.0, 2.0);
		temperatureAmplitude = MathHelper.clamp(settings.getGeneratorSettings().temperatureAmplitude, 0.0, 2.0);
		precipitationAmplitude = MathHelper.clamp(settings.getGeneratorSettings().precipitationAmplitude, 0.0, 2.0);
		
		riverRegionHalfWidth = (int)(8192 * elevationScale) / 2;
		riverRegionWidth = riverRegionHalfWidth * 2;
		riverWiggleHi = 400.0 * elevationScale;
		riverWiggleLo = riverWiggleHi * 1.3;
		riverWidthSquared = Math.pow(256, 2) * elevationScale;
		riverBank = riverWidthSquared * 0.2;
		riverShore = riverWidthSquared - riverBank;
		riverChannel = riverBank * 0.2;
		riverShallow = riverBank - riverChannel;
		
		xClimateFactor = DooglamooConfig.GENERAL.swapEastWestPrecipitation.get() ? -0.00001875 : 0.00001875;
		zClimateFactor = DooglamooConfig.GENERAL.swapNorthSouthTemperature.get() ? -0.00001875 : 0.00001875;
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z)
	{
		// adjust for Overworld dimension's fuzzy magnification of biomes
		x <<= 2;
		z <<= 2;
		
		GeoData geoData = getGeoData((x >> 4) << 4, (z >> 4) << 4);
		
		int n = Math.floorMod(x, 16);
		int m = Math.floorMod(z, 16);
	    
		int code = geoData.code[n][m];
		int rockLevel = geoData.rockLevel[n][m];
		int surfaceActualLevel = geoData.surfaceActualLevel[n][m];
		int plate = code & PLATES_MASK;
		
		if (DooglamooConfig.GENERAL.customGenHellBiome.get() && (plate == PLATES_PLAINS || plate == PLATES_PLATEAU)
				&& (code & THERMAL_MASK) == THERMAL_FULL
				&& ((code & AGE_MASK) == AGE_YOUNGEST || (code & AGE_MASK) == AGE_YOUNG))
		{
			return Biomes.NETHER;
		}
		else if ((plate == PLATES_PLAINS || plate == PLATES_PLATEAU) && (surfaceActualLevel < /*ChunkGeneratorDooglamoo.SEA_LEVEL*/63 || surfaceActualLevel < rockLevel - 1))
		{
			return Biomes.RIVER;
		}
		else if ((plate == PLATES_OCEAN || plate == PLATES_SHALLOWS) && surfaceActualLevel >= /*ChunkGeneratorDooglamoo.SEA_LEVEL*/63 - 1)
		{
			if ((code & TEMP_MASK) != TEMP_COLD)
			{
				return Biomes.BEACH;
			}
			else
			{
				return Biomes.SNOWY_BEACH;
			}
		}
		else
		{
			List<GeoFeature> biomeList = DictionaryFactory.biomes.get(code);
			if (biomeList == null || biomeList.isEmpty())
			{
				return Biomes.THE_VOID;
			}
			else
			{
				return biomeList.get(0).getBiome();
			}
		}

	}

//	@Override
//	public Set<Biome> getBiomes(int x, int y, int z, int radius)
//	{
//		int i = x - radius >> 2;
//	    int j = y - radius >> 2;
//	    int k = z - radius >> 2;
//	    int l = x + radius >> 2;
//	    int i1 = y + radius >> 2;
//	    int j1 = z + radius >> 2;
//	    int k1 = l - i + 1;
//	    int l1 = i1 - j + 1;
//	    int i2 = j1 - k + 1;
//	    Set<Biome> set = Sets.newHashSet();
//
//	    for(int j2 = 0; j2 < i2; ++j2) {
//	       for(int k2 = 0; k2 < k1; ++k2) {
//	          for(int l2 = 0; l2 < l1; ++l2) {
//	             int i3 = i + k2;
//	             int j3 = j + l2;
//	             int k3 = k + j2;
//	             set.add(this.getNoiseBiome(i3, j3, k3));
//	          }
//	       }
//	    }
//
//	    return set;
//	}

	@Override
	public boolean hasStructure(Structure<?> structureKey)
	{
		return this.hasStructureCache.computeIfAbsent(structureKey, (structure) -> {
	         for (Biome biome : DictionaryFactory.baseBiomes)
	         {
	            if (biome.hasStructure(structure))
	            {
	               return true;
	            }
	         }
	         for (Biome biome : DictionaryFactory.addedBiomes)
	         {
	            if (biome.hasStructure(structure))
	            {
	               return true;
	            }
	         }

	         return false;
	      });
	}

	@Override
	public Set<BlockState> getSurfaceBlocks()
	{
		if (this.topBlocksCache.isEmpty())
		{
	       this.topBlocksCache.add(Blocks.GRASS_BLOCK.getDefaultState());
	       this.topBlocksCache.add(Blocks.DIRT.getDefaultState());
	       this.topBlocksCache.add(Blocks.COARSE_DIRT.getDefaultState());
	       this.topBlocksCache.add(Blocks.PODZOL.getDefaultState());
	       this.topBlocksCache.add(Blocks.SAND.getDefaultState());
	    }
		
		return this.topBlocksCache;
	}
	
	// x,z must be on chunk boundary
	public GeoData getGeoData(int x, int z)
	{
		long riverKey = (long)(Math.floorDiv(x + riverRegionHalfWidth, riverRegionWidth)) & 4294967295L | ((long)(Math.floorDiv(z + riverRegionHalfWidth, riverRegionWidth)) & 4294967295L) << 32;
		RiverRegion riverRegion = riverRegionCache.get(riverKey);
	    if (riverRegion == null)
	    {
	    	int startX = Math.floorDiv(x + riverRegionHalfWidth, riverRegionWidth) * riverRegionWidth - riverRegionHalfWidth;
	    	int startZ = Math.floorDiv(z + riverRegionHalfWidth, riverRegionWidth) * riverRegionWidth - riverRegionHalfWidth;
	    	riverRegion = new RiverRegion(settings.getWorldInfo().getSeed() & riverKey, startX, startZ, riverRegionWidth, riverRegionWidth, 40, elevationGen, precipitationGen);
	    	if (riverRegionCache.size() > 4096)
			{
	    		riverRegionCache.clear();
			}
	    	riverRegionCache.put(riverKey, riverRegion);
	    }
		
		long key = (long)(x >> 4) & 4294967295L | ((long)(z >> 4) & 4294967295L) << 32;
		GeoData cachegeofactors = geoDataCache.get(key);
		if (cachegeofactors == null)
		{
			cachegeofactors = new GeoData();
			for (int n = 0; n < 16; ++n)
			{
				int x1 = x + n;
				
				for (int m = 0; m < 16; ++m)
				{
					int z1 = z + m;
					
					double surface = surfaceGen.noise(x1, z1);
					double elevation = elevationGen.noise(x1, z1) * elevationAmplitude + elevationWeight;
					double density = densityGen.noise(x1, z1) * densityAmplitude + densityWeight;
					double uplift = upliftGen.noise(x1, z1) * upliftAmplitude + upliftWeight;
					double volcanism = volcanismGen.noise(x1, z1) * volcanismAmplitude + volcanismWeight;
					double era = eraGen.noise(x1, z1) * eraAmplitude + eraWeight;
					double erosion = erosionGen.noise(x1, z1) * erosionAmplitude + erosionWeight;
					double temperature = temperatureGen.noise(x1, z1) * temperatureAmplitude + MathHelper.clamp(temperatureWeight + z1 * zClimateFactor, -0.6, 0.6);
					double precipitation = precipitationGen.noise(x1, z1) * precipitationAmplitude + MathHelper.clamp(precipitationWeight + x1 * xClimateFactor, -0.6, 0.6);
					
					// rivers
					if (elevation > -0.2)
					{
						double riverUplift = uplift;
						for (int t = 0; t < riverRegion.rivers.size(); t++)
						{
							RiverRegion.River river = riverRegion.rivers.get(t);
							double d = RiverRegion.distToSegmentSquared(x1, z1,
									river.vHi.x + riverWiggleHi * precipitation,
									river.vHi.z + riverWiggleHi * uplift,
									river.vLo.x + riverWiggleLo * precipitation,
									river.vLo.z + riverWiggleLo * uplift
									) + surface * 6400.0;
							if (d < riverChannel)
							{
								riverUplift = -0.8;
							}
							else if (d < riverBank)
							{
								riverUplift = Math.min(((d - riverChannel) / riverShallow) * 0.8 - 0.8, riverUplift);
							}
							else if (d < riverWidthSquared)
							{
								riverUplift = Math.min((d - riverBank) / riverShore, riverUplift);
							}
						}
						if (elevation < 0.0)
						{
							riverUplift = MathHelper.lerp(-elevation * 5.0, riverUplift, uplift);
						}
						uplift = riverUplift;
					}
					
					cachegeofactors.surface[n][m] = surface;
					cachegeofactors.elevation[n][m] = elevation;
					cachegeofactors.density[n][m] = density;
					cachegeofactors.uplift[n][m] = uplift;
					cachegeofactors.volcanism[n][m] = volcanism;
					cachegeofactors.era[n][m] = era;
					cachegeofactors.erosion[n][m] = erosion;
					cachegeofactors.temperature[n][m] = temperature;
					cachegeofactors.precipitation[n][m] = precipitation;
					
					double r = surface * 0.1 + (rand.nextDouble() * 0.1 - 0.05);
					
					int code = 0;
					
					if (elevation + surface * 0.008 < -0.5)
					{
						code |= PLATES_OCEAN;
					}
					else if (elevation + surface * 0.008 < 0.0)
					{
						code |= PLATES_SHALLOWS;
					}
					else if (elevation + surface * 0.008 < 0.5)
					{
						code |= PLATES_PLAINS;
					}
					else
					{
						code |= PLATES_PLATEAU;
					}
					
					if (density + r < -0.5)
					{
						code |= ROCK_SEDIMENTARY;
					}
					else if (density + r < 0.0)
					{
						code |= ROCK_SOFT;
					}
					else if (density + r < 0.5)
					{
						code |= ROCK_HARD;
					}
					else
					{
						code |= ROCK_IGNEOUS;
					}
					
					if (uplift + r < -0.5)
					{
						code |= LIFT_SUNK;
					}
					else if (uplift + r < 0.0)
					{
						code |= LIFT_LEVEL;
					}
					else if (uplift + r < 0.5)
					{
						code |= LIFT_HILLS;
					}
					else
					{
						code |= LIFT_MOUNTAINS;
					}
					
					if (volcanism + r < -0.5)
					{
						code |= THERMAL_NONE;
					}
					else if (volcanism + r < 0.0)
					{
						code |= THERMAL_SOME;
					}
					else if (volcanism + r < 0.5)
					{
						code |= THERMAL_MOST;
					}
					else
					{
						code |= THERMAL_FULL;
					}
					
					if (era + r < -0.5)
					{
						code |= AGE_YOUNGEST;
					}
					else if (era + r < 0.0)
					{
						code |= AGE_YOUNG;
					}
					else if (era + r < 0.5)
					{
						code |= AGE_OLD;
					}
					else
					{
						code |= AGE_OLDEST;
					}
					
					if (erosion + r < -0.5)
					{
						code |= EROSION_NONE;
					}
					else if (erosion + r < 0.0)
					{
						code |= EROSION_SOME;
					}
					else if (erosion + r < 0.5)
					{
						code |= EROSION_MOST;
					}
					else
					{
						code |= EROSION_FULL;
					}
					
					if (temperature + r < -0.5)
					{
						code |= TEMP_COLD;
					}
					else if (temperature + r < 0.0)
					{
						code |= TEMP_COOL;
					}
					else if (temperature + r < 0.5)
					{
						code |= TEMP_WARM;
					}
					else
					{
						code |= TEMP_HOT;
					}
					
					if (precipitation + r < -0.5)
					{
						code |= PRECIP_DRY;
					}
					else if (precipitation + r < 0.0)
					{
						code |= PRECIP_MOIST;
					}
					else if (precipitation + r < 0.5)
					{
						code |= PRECIP_HUMID;
					}
					else
					{
						code |= PRECIP_WET;
					}
					
					cachegeofactors.code[n][m] = code;
					
					// calculate levels
					double surfaceDepthPlateFactor = Math.max(Math.min((elevation + 0.02) * 25.0, 1.0), 0.08);
					int surfaceDepth = (int)((((surface + 1.0) * (uplift > -0.5 ? uplift + 0.5 : 0.0)) * 6.0 + (era + 0.5) * 12.0) * surfaceDepthPlateFactor);
					if (surfaceDepth < 1)
					{
						surfaceDepth = 1;
					}
					
					double erosionFactor = surface * 2.0; // to get "tabletop" effect
					if (erosionFactor > 0.0)
					{
						erosionFactor = -erosionFactor;
					}
					if (erosionFactor > -0.1)
					{
						erosionFactor = -0.1;
					}
					erosionFactor = 1.0 + erosionFactor;
					int erosionDepth;
					if (density < -0.5)
					{
						erosionDepth = erosion > 0.0 ? (int)(erosion * 50.0) : 0;
					}
					else if (density < 0.0)
					{
						erosionDepth = erosion > 0.0 ? (int)(erosion * 40.0) : 0;
					}
					else if (density < 0.5)
					{
						erosionDepth = erosion > 0.0 ? (int)(erosion * 32.0) : 0;
					}
					else
					{
						erosionDepth = erosion > 0.0 ? (int)(erosion * 26.0) : 0;
					}
					
					cachegeofactors.rockLevel[n][m] = (int)(((((elevation) + 1.0) / 2.0) * 128.0) + surface * 0.3);
					
					double upliftFactor = (erosion < 0.0 && uplift > 0.1 && uplift <= 0.6 ? Math.min(Math.max((uplift - 0.1) * (-erosion * 6.0), uplift), 0.6) : uplift) * 128.0;
					
					cachegeofactors.upliftLevel[n][m] = cachegeofactors.rockLevel[n][m] + (int)((uplift > 0.0 ? upliftFactor * Math.min(surfaceDepthPlateFactor + (volcanism > 0.45 ? (volcanism - 0.45) * 4.5 : 0.0), 1.0) : 0.0));
					cachegeofactors.surfaceVirtualLevel[n][m] = cachegeofactors.upliftLevel[n][m] + surfaceDepth;
					cachegeofactors.surfaceActualLevel[n][m] = cachegeofactors.surfaceVirtualLevel[n][m];
					if (erosionFactor > 0.0)
					{
						cachegeofactors.surfaceActualLevel[n][m] -= erosionFactor * erosionDepth;
					}
					if (uplift < -0.5)
					{
						cachegeofactors.surfaceActualLevel[n][m] -= (-uplift - 0.5) * 50.0;
					}
					
					cachegeofactors.mantleLevel[n][m] = cachegeofactors.upliftLevel[n][m] - (int)(200.0 - (volcanism > 0.0 ? volcanism * 256.0 : 0.0));
					if (cachegeofactors.mantleLevel[n][m] > cachegeofactors.upliftLevel[n][m])
					{
						cachegeofactors.mantleLevel[n][m] = cachegeofactors.upliftLevel[n][m];
					}
					if (cachegeofactors.upliftLevel[n][m] >= cachegeofactors.surfaceActualLevel[n][m] || uplift >= 0.5)
					{
						cachegeofactors.upliftLevel[n][m] = cachegeofactors.surfaceActualLevel[n][m] - 1;
					}
					// end calculation
				}
			}
			if (geoDataCache.size() > 4096)
			{
				geoDataCache.clear();
			}
			geoDataCache.put(key, cachegeofactors);
		}
		return cachegeofactors;
	}
}
