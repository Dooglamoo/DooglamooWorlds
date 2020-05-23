/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen;

import java.util.Collections;
import java.util.List;

import dooglamoo.dooglamooworlds.DooglamooConfig;
import dooglamoo.dooglamooworlds.dict.DictionaryFactory;
import dooglamoo.dooglamooworlds.dict.GeoFeature;
import dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProvider;
import dooglamoo.dooglamooworlds.world.gen.feature.DooglamooFeatures;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
//import net.minecraft.world.gen.feature.IcebergConfig;
//import net.minecraft.world.gen.feature.LakesConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.BuriedTreasureConfig;
import net.minecraft.world.gen.feature.structure.BuriedTreasureStructure;
import net.minecraft.world.gen.feature.structure.DesertPyramidStructure;
import net.minecraft.world.gen.feature.structure.IglooStructure;
import net.minecraft.world.gen.feature.structure.JunglePyramidStructure;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.OceanMonumentStructure;
import net.minecraft.world.gen.feature.structure.OceanRuinConfig;
import net.minecraft.world.gen.feature.structure.OceanRuinStructure;
//import net.minecraft.world.gen.feature.structure.PillagerOutpostConfig;
import net.minecraft.world.gen.feature.structure.PillagerOutpostStructure;
import net.minecraft.world.gen.feature.structure.ShipwreckConfig;
import net.minecraft.world.gen.feature.structure.ShipwreckStructure;
import net.minecraft.world.gen.feature.structure.StrongholdStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.SwampHutStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.structure.VillageStructure;
import net.minecraft.world.gen.feature.structure.WoodlandMansionStructure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
//import net.minecraft.world.gen.placement.LakeChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.CatSpawner;
import net.minecraft.world.spawner.PatrolSpawner;
import net.minecraft.world.spawner.PhantomSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class DooglamooChunkGenerator extends ChunkGenerator<DooglamooGenSettings>
{
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	private static final BlockState MAGMA_BLOCK = Blocks.MAGMA_BLOCK.getDefaultState();
	private static final BlockState OBSIDIAN = Blocks.OBSIDIAN.getDefaultState();
	private static final BlockState STONE = Blocks.STONE.getDefaultState();
	private static final BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
	private static final BlockState SAND = Blocks.SAND.getDefaultState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	private static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.getDefaultState();
	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();
	private static final BlockState ANDESITE = Blocks.ANDESITE.getDefaultState();
	private static final BlockState GRANITE = Blocks.GRANITE.getDefaultState();
	private static final BlockState DIORITE = Blocks.DIORITE.getDefaultState();
	private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.getDefaultState();
	private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.getDefaultState();
	private static final BlockState RED_TERRACOTTA = Blocks.RED_TERRACOTTA.getDefaultState();
	private static final BlockState LIGHT_GRAY_TERRACOTTA = Blocks.LIGHT_GRAY_TERRACOTTA.getDefaultState();
	private static final BlockState BROWN_TERRACOTTA = Blocks.BROWN_TERRACOTTA.getDefaultState();
	private static final BlockState YELLOW_TERRACOTTA = Blocks.YELLOW_TERRACOTTA.getDefaultState();
	private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.getDefaultState();
	private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	
	public final DooglamooBiomeProvider biomeProvider;
	
	private final ConfiguredFeature<?, ?> oreFeature;
	private final ConfiguredFeature<?, ?> packedicebergFeature;
	private final ConfiguredFeature<?, ?> blueicebergFeature;
	private final ConfiguredFeature<?, ?> lavaLakeFeature;
	
	private final PhantomSpawner phantomSpawner = new PhantomSpawner();
	private final PatrolSpawner patrolSpawner = new PatrolSpawner();
	private final CatSpawner catSpawner = new CatSpawner();
	private final VillageSiege villageSiege = new VillageSiege();
	
	public DooglamooChunkGenerator(IWorld world, DooglamooBiomeProvider biomeProvider, DooglamooGenSettings generationSettings)
	{
		super(world, biomeProvider, generationSettings);
		this.biomeProvider = biomeProvider;
		
		oreFeature = DooglamooFeatures.ORE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
		packedicebergFeature = Feature.ICEBERG.withConfiguration(new BlockStateFeatureConfig(Blocks.PACKED_ICE.getDefaultState())).withPlacement(Placement.ICEBERG.configure(new ChanceConfig(16)));
		blueicebergFeature = Feature.ICEBERG.withConfiguration(new BlockStateFeatureConfig(Blocks.BLUE_ICE.getDefaultState())).withPlacement(Placement.ICEBERG.configure(new ChanceConfig(200)));
		lavaLakeFeature = Feature.LAKE.withConfiguration(new BlockStateFeatureConfig(Blocks.LAVA.getDefaultState())).withPlacement(Placement.LAVA_LAKE.configure(new ChanceConfig(80)));
	}

	@Override
	public void generateSurface(WorldGenRegion region, IChunk chunk)
	{

	}

	@Override
	public int getGroundHeight()
	{
		IChunk ichunk = this.world.getChunk(0, 0);
	    return ichunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, 8, 8);
	}
	
	@Override
	public int getSeaLevel()
	{
	   return 63;
	}

	@Override
	public void makeBase(IWorld world, IChunk chunk)
	{
		ChunkPos chunkpos = chunk.getPos();
	    int l = chunkpos.x << 4;
	    int i = chunkpos.z << 4;
	    
		// for villages
		ObjectList<AbstractVillagePiece> objectlist = new ObjectArrayList<>(10);
	    ObjectList<JigsawJunction> objectlist1 = new ObjectArrayList<>(32);

	    for (Structure<?> structure : Feature.ILLAGER_STRUCTURES)
	    {
	       String s = structure.getStructureName();
	       LongIterator longiterator = chunk.getStructureReferences(s).iterator();

	       while (longiterator.hasNext())
	       {
	          long j1 = longiterator.nextLong();
	          ChunkPos chunkpos1 = new ChunkPos(j1);
	          IChunk ichunk = world.getChunk(chunkpos1.x, chunkpos1.z);
	          StructureStart structurestart = ichunk.getStructureStart(s);
	          if (structurestart != null && structurestart.isValid())
	          {
	             for (StructurePiece structurepiece : structurestart.getComponents())
	             {
	                if (structurepiece.func_214810_a(chunkpos, 12) && structurepiece instanceof AbstractVillagePiece)
	                {
	                   AbstractVillagePiece abstractvillagepiece = (AbstractVillagePiece)structurepiece;
	                   JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = abstractvillagepiece.getJigsawPiece().getPlacementBehaviour();
	                   if (jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID)
	                   {
	                      objectlist.add(abstractvillagepiece);
	                   }

	                   for (JigsawJunction jigsawjunction : abstractvillagepiece.getJunctions())
	                   {
	                      int k1 = jigsawjunction.getSourceX();
	                      int l1 = jigsawjunction.getSourceZ();
	                      if (k1 > l - 12 && l1 > i - 12 && k1 < l + 15 + 12 && l1 < i + 15 + 12)
	                      {
	                         objectlist1.add(jigsawjunction);
	                      }
	                   }
	                }
	             }
	          }
	       }
	    }
	    ObjectListIterator<AbstractVillagePiece> objectlistiterator = objectlist.iterator();
	    ObjectListIterator<JigsawJunction> objectlistiterator1 = objectlist1.iterator();
		// end villages
		
		BlockPos.Mutable blockpos = new BlockPos.Mutable();
		
		ChunkPrimer chunkprimer = (ChunkPrimer)chunk;
		Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
	    
	    SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
	    sharedseedrandom.setBaseChunkSeed(chunk.getPos().x, chunk.getPos().z);
	    
	    GeoData geoData = biomeProvider.getGeoData(l, i);

		for (int n = 0; n < 16; ++n)
		{
			int x1 = l + n;
			
			for (int m = 0; m < 16; ++m)
			{
				int z1 = i + m;
				
				double surface = geoData.surface[n][m];
				double density = geoData.density[n][m];
				double uplift = geoData.uplift[n][m];
				double temperature = geoData.temperature[n][m];
				double precipitation = geoData.precipitation[n][m];
				
				int code = geoData.code[n][m];
				
				int mantle = geoData.mantleLevel[n][m];
				int rockLevel = geoData.rockLevel[n][m];
				int upliftLevel = geoData.upliftLevel[n][m];
				int surfaceActualLevel = geoData.surfaceActualLevel[n][m];
				int surfaceVirtualLevel = geoData.surfaceVirtualLevel[n][m];
				
				// for villages
				while (objectlistiterator.hasNext())
				{
                    AbstractVillagePiece abstractvillagepiece1 = objectlistiterator.next();
                    MutableBoundingBox mutableboundingbox = abstractvillagepiece1.getBoundingBox();
                    if (mutableboundingbox.intersectsWith(x1, z1, x1, z1))
                    {
                    	surfaceActualLevel = mutableboundingbox.minY + abstractvillagepiece1.getGroundLevelDelta() - 1;
                    }
                 }
                 objectlistiterator.back(objectlist.size());
                 while (objectlistiterator1.hasNext())
                 {
                    JigsawJunction jigsawjunction1 = objectlistiterator1.next();
                    if (jigsawjunction1.getSourceX() == x1 && jigsawjunction1.getSourceZ() == z1)
                    {
                    	surfaceActualLevel = jigsawjunction1.getSourceGroundY() - 1;
                    }
                 }
                 objectlistiterator1.back(objectlist1.size());
				// end villages
				
				for (int y = 0; y < 256; y++)
				{
					BlockState blockstate = AIR;
					
					if (y < 1)
					{
						blockstate = BEDROCK;
					}
					else if (y < 2 && surface > 0.0)
					{
						blockstate = BEDROCK;
					}
					else if (y <= mantle)
					{
						if (y == mantle && y >= surfaceActualLevel && (y < this.getSeaLevel() || y < rockLevel - 1))
						{
							if (sharedseedrandom.nextInt(6) == 0)
							{
								chunkprimer.setBlockState(blockpos.setPos(n, y, m), MAGMA_BLOCK, false);
				                chunkprimer.getBlocksToBeTicked().scheduleTick(blockpos.setPos(n, y, m), Blocks.MAGMA_BLOCK, 0);
								heightmap.update(n, y, m, blockstate);
								heightmap1.update(n, y, m, blockstate);
								continue;
							}
							else
							{
								blockstate = OBSIDIAN;
							}
						}
						else
						{
							blockstate = MAGMA_BLOCK;
						}
					}
					else if (y <= upliftLevel)
					{
						if (density < -0.5)
						{
							// sedimentary rock (sandstone) strata
							if (y % 67 == 0)
							{
								blockstate = STONE;
							}
							else if (y % 17 == 0)
							{
								blockstate = ORANGE_TERRACOTTA;
							}
							else if (y % 15 == 0)
							{
								blockstate = RED_TERRACOTTA;
							}
							else if (y % 11 == 0)
							{
								blockstate = LIGHT_GRAY_TERRACOTTA;
							}
							else if (y % 9 == 0)
							{
								blockstate = BROWN_TERRACOTTA;
							}
							else if (y % 19 == 0)
							{
								blockstate = YELLOW_TERRACOTTA;
							}
							else if (y % 7 == 0)
							{
								blockstate = WHITE_TERRACOTTA;
							}
							else
							{
								blockstate = SANDSTONE;
							}
						}
						else if (density < 0.0)
						{
							// soft rock (limestone) strata
							if (y % 12 == 0)
							{
								blockstate = COARSE_DIRT;
							}
							else if (y % 16 == 0)
							{
								blockstate = GRAVEL;
							}
							else if (y % 71 == 0)
							{
								blockstate = ANDESITE;
							}
							else
							{
								blockstate = STONE;
							}
						}
						else if (density < 0.5)
						{
							// hard rock (granite) strata
							if (y % 7 == 0)
							{
								blockstate = ANDESITE;
							}
							else if (y % 35 == 0)
							{
								blockstate = STONE;
							}
							else
							{
								blockstate = GRANITE;
							}
						}
						else
						{
							// igneous rock (diorite) strata
							if (y % 17 == 0)
							{
								blockstate = OBSIDIAN;
							}
							else if (y % 15 == 0)
							{
								blockstate = ANDESITE;
							}
							else if (y % 9 == 0)
							{
								blockstate = GRANITE;
							}
							else
							{
								blockstate = DIORITE;
							}
						}
						
						if ((y >= surfaceActualLevel - 1 && surfaceActualLevel - 1 <= this.getSeaLevel()) || (y >= rockLevel && rockLevel <= this.getSeaLevel()))
						{
							if (y == mantle + 1)
							{
								blockstate = OBSIDIAN;
							}
						}
					}
					else if (y <= surfaceActualLevel)
					{
						if (y == surfaceActualLevel)
						{
							// chunk primer should be indexed by n,m?
							BlockState below = chunkprimer.getBlockState(blockpos.setPos(x1, y - 1, z1));
							if ((uplift < 0.5 || surface < 0.0) && y > 0 && below.getMaterial() != Material.AIR)
							{
								List<GeoFeature> blockList = DictionaryFactory.blocks.get(code);
								if (y > 0 && (below.getBlock() == Blocks.LAVA || below.getBlock() == Blocks.MAGMA_BLOCK))
								{
									blockstate = OBSIDIAN;
								}
								else if (y < getSeaLevel())
								{
									if (surface < -0.5)
									{
										blockstate = GRAVEL;
									}
									else
									{
										blockstate = SAND;
									}
								}
								else if (blockList != null && !blockList.isEmpty())
								{
									blockstate = blockList.get(sharedseedrandom.nextInt(blockList.size())).getBlockType();
								}
								else
								{
									if ((code & DooglamooBiomeProvider.PRECIP_MASK) == DooglamooBiomeProvider.PRECIP_DRY)
									{
										blockstate = SAND;
									}
									else
									{
										blockstate = DIRT;
									}
								}
							}
							else if (y < getSeaLevel())
							{
								blockstate = WATER;
							}
						}
						else if (y <= surfaceActualLevel - ((surfaceVirtualLevel - upliftLevel) / 2))
						{
							if (!(surfaceActualLevel - mantle < 5 && surface < 0.0))
							{
								if (uplift > 0.5)
								{
									blockstate = STONE;
								}
								else
								{
									blockstate = COARSE_DIRT;
								}
							}
						}
						else
						{
							if (!(surfaceActualLevel - mantle < 5 && surface < 0.0))
							{
								if (precipitation < -0.5)
								{
									if (temperature > 0.5)
									{
										blockstate = SAND;
									}
									else if (temperature < -0.5)
									{
										blockstate = PACKED_ICE;
									}
									else
									{
										if (y % 13 == 0)
										{
											blockstate = RED_TERRACOTTA;
										}
										else if (y % 15 == 0)
										{
											blockstate = BROWN_TERRACOTTA;
										}
										else if (y % 17 == 0)
										{
											blockstate = YELLOW_TERRACOTTA;
										}
										else if (y % 19 == 0)
										{
											blockstate = ORANGE_TERRACOTTA;
										}
										else
										{
											blockstate = TERRACOTTA;
										}
									}
								}
								else
								{
									blockstate = DIRT;
								}
							}
						}
					}
					else if (y < surfaceVirtualLevel + (Math.min(precipitation, 0.7) - 0.7) * 15 - Math.max(uplift, 0.0) * 25)
					{
						if (temperature < -0.5)
						{
							blockstate = PACKED_ICE;
						}
						else
						{
							if (y < getSeaLevel() || y < rockLevel - 1)
							{
								blockstate = WATER;
							}
						}
					}
					else if (y < getSeaLevel() || y < rockLevel - 1)
					{
						blockstate = WATER;
					}
					
					if (blockstate.getMaterial() != Material.AIR)
					{
						chunkprimer.setBlockState(blockpos.setPos(n, y, m), blockstate, false);
						heightmap.update(n, y, m, blockstate);
						heightmap1.update(n, y, m, blockstate);
					}
				}
			}
		}
	}

	@Override
	// for structures
	public int func_222529_a(int x, int z, Heightmap.Type type)
	{
		if (type == Heightmap.Type.WORLD_SURFACE_WG)
		{
			int n = Math.floorMod(x, 16);
			int m = Math.floorMod(z, 16);
			
			GeoData geoData = biomeProvider.getGeoData((x >> 4) << 4, (z >> 4) << 4);
			
			int rockLevel = geoData.rockLevel[n][m];
			int surfaceActualLevel = geoData.surfaceActualLevel[n][m];
			
			if (surfaceActualLevel < getSeaLevel())
			{
				return getSeaLevel();
			}
			
			if (surfaceActualLevel < rockLevel - 1)
			{
				return rockLevel - 1;
			}
			
			return surfaceActualLevel + 1;
		}
	    return 0;
	}
	
	// Hack. Prevent from returning null.
	@Override
	public <C extends IFeatureConfig> C getStructureConfig(Biome biomeIn, Structure<C> structureIn)
	{
		C c = super.getStructureConfig(biomeIn, structureIn);
		if (c == null)
		{
			if (structureIn instanceof BuriedTreasureStructure)
			{
				return (C)new BuriedTreasureConfig(0.01f);
			}
			else if (structureIn instanceof DesertPyramidStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof IglooStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof JunglePyramidStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof MineshaftStructure)
			{
				return (C)new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL);
			}
			else if (structureIn instanceof OceanMonumentStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof OceanRuinStructure)
			{
				if (biomeIn.getDefaultTemperature() < 0.2f)
				{
					return (C)new OceanRuinConfig(OceanRuinStructure.Type.COLD, 0.3F, 0.9F);
				}
				else
				{
					return (C)new OceanRuinConfig(OceanRuinStructure.Type.WARM, 0.3F, 0.9F);
				}
			}
			else if (structureIn instanceof PillagerOutpostStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof ShipwreckStructure)
			{
				if (biomeIn.getCategory() == Biome.Category.OCEAN)
				{
					return (C)new ShipwreckConfig(false);
				}
				else
				{
					return (C)new ShipwreckConfig(true);
				}
			}
			else if (structureIn instanceof StrongholdStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof SwampHutStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
			else if (structureIn instanceof VillageStructure)
			{
				return (C)new VillageConfig("village/plains/town_centers", 6);
			}
			else if (structureIn instanceof WoodlandMansionStructure)
			{
				return (C)IFeatureConfig.NO_FEATURE_CONFIG;
			}
		}
		return c;
	}
	
	@Override
	public void decorate(WorldGenRegion region)
	{
		int i = region.getMainChunkX();
	    int j = region.getMainChunkZ();
	    int k = i * 16;
	    int l = j * 16;
	    BlockPos blockpos = new BlockPos(k, 0, l);
	    Biome biome = this.getBiome(region.getBiomeManager(), blockpos.add(8, 8, 8));
	    SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
	    long i1 = sharedseedrandom.setDecorationSeed(region.getSeed(), k, l);
	    
	    if (DictionaryFactory.addedBiomes.contains(biome))
        {
	    	GenerationStage.Decoration stage = null;
		    try
		    {
		    	biome.decorate(stage = GenerationStage.Decoration.RAW_GENERATION, this, region, i1, sharedseedrandom, blockpos);
		    	biome.decorate(stage = GenerationStage.Decoration.LOCAL_MODIFICATIONS, this, region, i1, sharedseedrandom, blockpos);
		    	biome.decorate(stage = GenerationStage.Decoration.UNDERGROUND_STRUCTURES, this, region, i1, sharedseedrandom, blockpos);
			    biome.decorate(stage = GenerationStage.Decoration.SURFACE_STRUCTURES, this, region, i1, sharedseedrandom, blockpos);
			    stage = GenerationStage.Decoration.UNDERGROUND_ORES;
			    if (DooglamooConfig.GENERAL.standardGenOres.get())
			    {
			    	biome.decorate(stage, this, region, i1, sharedseedrandom, blockpos);
			    }
			    if (DooglamooConfig.GENERAL.customGenOres.get())
			    {
			    	oreFeature.place(region, this, sharedseedrandom, blockpos);
			    }
			    biome.decorate(stage = GenerationStage.Decoration.UNDERGROUND_DECORATION, this, region, i1, sharedseedrandom, blockpos);
	        	biome.decorate(stage = GenerationStage.Decoration.VEGETAL_DECORATION, this, region, i1, sharedseedrandom, blockpos);
	        	biome.decorate(stage = GenerationStage.Decoration.TOP_LAYER_MODIFICATION, this, region, i1, sharedseedrandom, blockpos);
		    }
		    catch (Exception exception)
		    {
	            CrashReport crashreport = CrashReport.makeCrashReport(exception, "Biome decoration");
	            crashreport.makeCategory("Generation").addDetail("CenterX", i).addDetail("CenterZ", j).addDetail("Step", stage).addDetail("Seed", i1).addDetail("Biome", Registry.BIOME.getKey(biome));
	            throw new ReportedException(crashreport);
	        }
        }
        else
        {
		    GeoData geoData = biomeProvider.getGeoData(k, l);
			
			double elevation = geoData.elevation[8][8];
			double volcanism = geoData.volcanism[8][8];
			double era = geoData.era[8][8];
			double temperature = geoData.temperature[8][8];
			
			int code = geoData.code[8][8];
			
			GenerationStage.Decoration stage = null;
		    try
		    {
		    	stage = GenerationStage.Decoration.LOCAL_MODIFICATIONS;
		    	if (elevation < -0.1 && temperature < -0.55)
		    	{
			    	packedicebergFeature.place(region, this, sharedseedrandom, blockpos);
			    	blueicebergFeature.place(region, this, sharedseedrandom, blockpos);
		    	}
		    	if (volcanism > 0.0)
		    	{
		    		lavaLakeFeature.place(region, this, sharedseedrandom, blockpos);
		    	}
		    	
			    biome.decorate(stage = GenerationStage.Decoration.UNDERGROUND_STRUCTURES, this, region, i1, sharedseedrandom, blockpos);
			    biome.decorate(stage = GenerationStage.Decoration.SURFACE_STRUCTURES, this, region, i1, sharedseedrandom, blockpos);
			    stage = GenerationStage.Decoration.UNDERGROUND_ORES;
			    if (DooglamooConfig.GENERAL.standardGenOres.get())
			    {
			    	biome.decorate(stage, this, region, i1, sharedseedrandom, blockpos);
			    }
			    if (DooglamooConfig.GENERAL.customGenOres.get())
			    {
			    	oreFeature.place(region, this, sharedseedrandom, blockpos);
			    }
			    biome.decorate(stage = GenerationStage.Decoration.UNDERGROUND_DECORATION, this, region, i1, sharedseedrandom, blockpos);
		    }
		    catch (Exception exception)
		    {
	            CrashReport crashreport = CrashReport.makeCrashReport(exception, "Biome decoration");
	            crashreport.makeCategory("Generation").addDetail("CenterX", i).addDetail("CenterZ", j).addDetail("Step", stage).addDetail("Seed", i1).addDetail("Biome", Registry.BIOME.getKey(biome));
	            throw new ReportedException(crashreport);
	        }
			
			float occuranceMod = ((((float)era + 1.0f) / 4.0f) + 0.5f) * 64.0f;
			
			int t = 0;
			List<GeoFeature> featureList = DictionaryFactory.features.get(code);
			if (featureList != null && !featureList.isEmpty())
			{
				if (featureList.size() > 1)
				{
					Collections.shuffle(featureList, sharedseedrandom);
				}
				for (GeoFeature geo : featureList)
				{
					if (sharedseedrandom.nextFloat() < geo.getRarity() * occuranceMod)
					{
						sharedseedrandom.setFeatureSeed(i1, t, GenerationStage.Decoration.VEGETAL_DECORATION.ordinal());
						geo.getFeature().place(region, this, sharedseedrandom, blockpos);
						t++;
					}
				}
			}
			
			try
		    {
			    biome.decorate(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, this, region, i1, sharedseedrandom, blockpos);
		    }
		    catch (Exception exception)
		    {
	            CrashReport crashreport = CrashReport.makeCrashReport(exception, "Biome decoration");
	            crashreport.makeCategory("Generation").addDetail("CenterX", i).addDetail("CenterZ", j).addDetail("Step", GenerationStage.Decoration.TOP_LAYER_MODIFICATION).addDetail("Seed", i1).addDetail("Biome", Registry.BIOME.getKey(biome));
	            throw new ReportedException(crashreport);
	        }
        }
	}
	
	// Normal chunk generation spawning
	@Override
	public void spawnMobs(WorldGenRegion region)
	{
		int i = region.getMainChunkX();
	    int j = region.getMainChunkZ();
	    Biome biome = region.getBiome((new ChunkPos(i, j)).asBlockPos());
	    SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
	    sharedseedrandom.setDecorationSeed(region.getSeed(), i << 4, j << 4);
	    WorldEntitySpawner.performWorldGenSpawning(region, biome, i, j, sharedseedrandom);
	}
	
	// Custom spawning
	@Override
	public void spawnMobs(ServerWorld worldIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs)
	{
		this.phantomSpawner.tick(worldIn, spawnHostileMobs, spawnPeacefulMobs);
		this.patrolSpawner.tick(worldIn, spawnHostileMobs, spawnPeacefulMobs);
		this.catSpawner.tick(worldIn, spawnHostileMobs, spawnPeacefulMobs);
		this.villageSiege.tick(worldIn, spawnHostileMobs, spawnPeacefulMobs);
	}
	
	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
	{
		if (Feature.SWAMP_HUT.func_202383_b(this.world, pos))
		{
			if (creatureType == EntityClassification.MONSTER)
			{
				return Feature.SWAMP_HUT.getSpawnList();
			}

			if (creatureType == EntityClassification.CREATURE)
			{
				return Feature.SWAMP_HUT.getCreatureSpawnList();
			}
		}
		else if (creatureType == EntityClassification.MONSTER)
		{
			if (Feature.PILLAGER_OUTPOST.isPositionInStructure(this.world, pos))
			{
				return Feature.PILLAGER_OUTPOST.getSpawnList();
			}

			if (Feature.OCEAN_MONUMENT.isPositionInStructure(this.world, pos))
			{
				return Feature.OCEAN_MONUMENT.getSpawnList();
			}
		}

		return super.getPossibleCreatures(creatureType, pos);
	}
}
