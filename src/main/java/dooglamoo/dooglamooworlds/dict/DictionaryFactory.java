/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.BlockBlobConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureRadiusConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SeaGrassConfig;
import net.minecraft.world.gen.feature.SingleRandomFeature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.HeightWithChanceConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import dooglamoo.dooglamooworlds.DooglamooConfig;
import dooglamoo.dooglamooworlds.DooglamooWorldsMod;
import dooglamoo.dooglamooworlds.world.gen.feature.DooglamooFeatures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class DictionaryFactory
{
	private static final BlockState DIRT = Blocks.DIRT.getDefaultState();
	private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	private static final BlockState SAND = Blocks.SAND.getDefaultState();
	private static final BlockState CLAY = Blocks.CLAY.getDefaultState();
	private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
	private static final BlockState MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.getDefaultState();
	private static final BlockState COBBLESTONE = Blocks.COBBLESTONE.getDefaultState();

	public static final BlockClusterFeatureConfig BIRCH_FALLEN_TREE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.BIRCH_LOG.getDefaultState()), new SimpleBlockPlacer())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig OAK_FALLEN_TREE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockPlacer())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig SPRUCE_FALLEN_TREE_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.SPRUCE_LOG.getDefaultState()), new SimpleBlockPlacer())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig DANDELION_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.DANDELION.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig POPPY_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.POPPY.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig ALLIUM_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ALLIUM.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig AZURE_BLUET_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.AZURE_BLUET.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig RED_TULIP_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.RED_TULIP.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig ORANGE_TULIP_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.ORANGE_TULIP.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig WHITE_TULIP_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.WHITE_TULIP.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig PINK_TULIP_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.PINK_TULIP.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig OXEYE_DAISY_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OXEYE_DAISY.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	public static final BlockClusterFeatureConfig CORNFLOWER_CONFIG = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.CORNFLOWER.getDefaultState()), new SimpleBlockPlacer())).tries(64).build();
	
	@ObjectHolder("lostcities:lostcityfeature")
	public static final Feature<NoFeatureConfig> LOSTCITY_FEATURE = null;
	
	@ObjectHolder("biomesoplenty:pink_cherry_tree")
	public static final Feature<BaseTreeFeatureConfig> PINK_CHERRY_TREE = null;
	@ObjectHolder("biomesoplenty:big_pink_cherry_tree")
	public static final Feature<BaseTreeFeatureConfig> BIG_PINK_CHERRY_TREE = null;
	@ObjectHolder("biomesoplenty:white_cherry_tree")
	public static final Feature<BaseTreeFeatureConfig> WHITE_CHERRY_TREE = null;
	@ObjectHolder("biomesoplenty:big_white_cherry_tree")
	public static final Feature<BaseTreeFeatureConfig> BIG_WHITE_CHERRY_TREE = null;
	@ObjectHolder("biomesoplenty:fir_tree")
	public static final Feature<BaseTreeFeatureConfig> FIR_TREE = null;
	@ObjectHolder("biomesoplenty:fir_tree_large")
	public static final Feature<BaseTreeFeatureConfig> FIR_TREE_LARGE = null;
	@ObjectHolder("biomesoplenty:willow_tree")
	public static final Feature<BaseTreeFeatureConfig> WILLOW_TREE = null;
	@ObjectHolder("biomesoplenty:yellow_autumn_tree")
	public static final Feature<BaseTreeFeatureConfig> YELLOW_AUTUMN_TREE = null;
	@ObjectHolder("biomesoplenty:orange_autumn_tree")
	public static final Feature<BaseTreeFeatureConfig> ORANGE_AUTUMN_TREE = null;
	@ObjectHolder("biomesoplenty:jungle_twiglet_tree")
	public static final Feature<BaseTreeFeatureConfig> JUNGLE_TWIGLET_TREE = null;
	@ObjectHolder("biomesoplenty:dying_tree")
	public static final Feature<BaseTreeFeatureConfig> DYING_TREE = null;
	@ObjectHolder("biomesoplenty:dead_tree")
	public static final Feature<BaseTreeFeatureConfig> DEAD_TREE = null;
	@ObjectHolder("biomesoplenty:dark_oak_poplar")
	public static final Feature<BaseTreeFeatureConfig> DARK_OAK_POPLAR = null;
	@ObjectHolder("biomesoplenty:birch_poplar")
	public static final Feature<BaseTreeFeatureConfig> BIRCH_POPLAR = null;
	@ObjectHolder("biomesoplenty:jacaranda_tree")
	public static final Feature<BaseTreeFeatureConfig> JACARANDA_TREE = null;
	@ObjectHolder("biomesoplenty:maple_tree")
	public static final Feature<BaseTreeFeatureConfig> MAPLE_TREE = null;
	@ObjectHolder("biomesoplenty:magic_tree")
	public static final Feature<BaseTreeFeatureConfig> MAGIC_TREE = null;
	@ObjectHolder("biomesoplenty:palm_tree")
	public static final Feature<BaseTreeFeatureConfig> PALM_TREE = null;
	@ObjectHolder("biomesoplenty:tall_umbran_tree")
	public static final Feature<BaseTreeFeatureConfig> TALL_UMBRAN_TREE = null;
	@ObjectHolder("biomesoplenty:acacia_bush")
	public static final Feature<BaseTreeFeatureConfig> ACACIA_BUSH = null;
	@ObjectHolder("biomesoplenty:acacia_twiglet_tree")
	public static final Feature<BaseTreeFeatureConfig> ACACIA_TWIGLET_TREE = null;
	@ObjectHolder("biomesoplenty:mahogany_tree")
	public static final Feature<BaseTreeFeatureConfig> MAHOGANY_TREE = null;
	@ObjectHolder("biomesoplenty:redwood_tree")
	public static final Feature<BaseTreeFeatureConfig> REDWOOD_TREE = null;
	@ObjectHolder("biomesoplenty:redwood_tree_medium")
	public static final Feature<BaseTreeFeatureConfig> REDWOOD_TREE_MEDIUM = null;
	@ObjectHolder("biomesoplenty:lavender_flowers")
	public static final FlowersFeature<NoFeatureConfig> LAVENDER_FLOWERS = null;
	
	public static HashMap<String, BlockState> blockDictionary;
	public static HashMap<String, ConfiguredFeature<?, ?>> featureDictionary;
	
	public static List<Biome> baseBiomes;
	public static List<Biome> addedBiomes;
	
	public static Map<Integer, List<GeoFeature>> blocks = new HashMap<Integer, List<GeoFeature>>();
	public static Map<Integer, List<GeoFeature>> features = new HashMap<Integer, List<GeoFeature>>();
	public static Map<Integer, List<GeoFeature>> biomes = new HashMap<Integer, List<GeoFeature>>();
	public static Map<Integer, List<GeoFeature>> ores = new HashMap<Integer, List<GeoFeature>>();
	
	public static int biomeCount;
	public static int blockCount;
	public static int featureCount;
	
	public static void clear()
	{
		baseBiomes.clear();
		addedBiomes.clear();
		blocks.clear();
		features.clear();
		biomes.clear();
		ores.clear();
		biomeCount = 0;
		blockCount = 0;
		featureCount = 0;
	}
	
	public static void init()
	{
		blockDictionary = new HashMap<String, BlockState>();
		featureDictionary = new HashMap<String, ConfiguredFeature<?, ?>>();
		baseBiomes = new ArrayList<Biome>();
		addedBiomes = new ArrayList<Biome>();
		
		blockDictionary.put("air", Blocks.AIR.getDefaultState());
		blockDictionary.put("lava", Blocks.LAVA.getDefaultState());
		blockDictionary.put("water", Blocks.WATER.getDefaultState());
		blockDictionary.put("water_salt", Blocks.WATER.getDefaultState());
		blockDictionary.put("magma", Blocks.MAGMA_BLOCK.getDefaultState());
		blockDictionary.put("stone", Blocks.STONE.getDefaultState());
		blockDictionary.put("stone_andesite", Blocks.ANDESITE.getDefaultState());
		blockDictionary.put("stone_granite", Blocks.GRANITE.getDefaultState());
		blockDictionary.put("stone_diorite", Blocks.DIORITE.getDefaultState());
		blockDictionary.put("dirt", Blocks.DIRT.getDefaultState());
		blockDictionary.put("coarsedirt", Blocks.COARSE_DIRT.getDefaultState());
		blockDictionary.put("loam", Blocks.COARSE_DIRT.getDefaultState());
		blockDictionary.put("podzol", Blocks.PODZOL.getDefaultState());
		blockDictionary.put("mycelium", Blocks.MYCELIUM.getDefaultState());
		blockDictionary.put("grass", Blocks.GRASS_BLOCK.getDefaultState());
		blockDictionary.put("heath", Blocks.GRASS_BLOCK.getDefaultState());
		blockDictionary.put("terracotta", Blocks.TERRACOTTA.getDefaultState());
		blockDictionary.put("sandstone", Blocks.SANDSTONE.getDefaultState());
		blockDictionary.put("terracotta_white", Blocks.WHITE_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_orange", Blocks.ORANGE_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_yellow", Blocks.YELLOW_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_gray", Blocks.GRAY_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_silver", Blocks.LIGHT_GRAY_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_brown", Blocks.BROWN_TERRACOTTA.getDefaultState());
		blockDictionary.put("terracotta_red", Blocks.RED_TERRACOTTA.getDefaultState());
		blockDictionary.put("cobblestone", Blocks.COBBLESTONE.getDefaultState());
		blockDictionary.put("obsidian", Blocks.OBSIDIAN.getDefaultState());
		blockDictionary.put("gravel", Blocks.GRAVEL.getDefaultState());
		blockDictionary.put("sand", Blocks.SAND.getDefaultState());
		blockDictionary.put("sand_red", Blocks.RED_SAND.getDefaultState());
		blockDictionary.put("ice", Blocks.ICE.getDefaultState());
		blockDictionary.put("snow", Blocks.SNOW_BLOCK.getDefaultState());
		blockDictionary.put("snowpack", Blocks.SNOW_BLOCK.getDefaultState());
		blockDictionary.put("icepack", Blocks.PACKED_ICE.getDefaultState());
		blockDictionary.put("silverfish", Blocks.INFESTED_STONE.getDefaultState());
		blockDictionary.put("clay", Blocks.CLAY.getDefaultState());

		if (DooglamooConfig.GENERAL.customGenSlimeOre.get())
		{
			blockDictionary.put("oil", Blocks.SLIME_BLOCK.getDefaultState());
		}
		if (DooglamooConfig.GENERAL.customGenGlowstoneOre.get())
		{
			blockDictionary.put("glowstone", Blocks.GLOWSTONE.getDefaultState());
		}
		blockDictionary.put("coal_ore", Blocks.COAL_ORE.getDefaultState());
		blockDictionary.put("iron_ore", Blocks.IRON_ORE.getDefaultState());
		blockDictionary.put("gold_ore", Blocks.GOLD_ORE.getDefaultState());
		blockDictionary.put("emerald_ore", Blocks.EMERALD_ORE.getDefaultState());
		blockDictionary.put("lapis_ore", Blocks.LAPIS_ORE.getDefaultState());
		blockDictionary.put("redstone_ore", Blocks.REDSTONE_ORE.getDefaultState());
		blockDictionary.put("diamond_ore", Blocks.DIAMOND_ORE.getDefaultState());
		if (DooglamooConfig.GENERAL.customGenQuartzOre.get())
		{
			blockDictionary.put("quartz_ore", Blocks.NETHER_QUARTZ_ORE.getDefaultState());
		}

		// minecraft
		addFeature("tree_spruce_mega", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.MEGA_SPRUCE_TREE.withConfiguration(DefaultBiomeFeatures.MEGA_PINE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
		addFeature("tree_spruce_redwood", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.MEGA_SPRUCE_TREE.withConfiguration(DefaultBiomeFeatures.MEGA_SPRUCE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
		addFeature("tree_spruce", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.PINE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(6/*10*/, 0.1F, 1))));
		addFeature("tree_spruce_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.SPRUCE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(8/*10*/, 0.1F, 1))));
		addFeature("tree_birch", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.field_230129_h_).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(/*10*/6, 0.1F, 1))));
		addFeature("tree_birch_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.withConfiguration(new MultipleRandomFeatureConfig(ImmutableList.of(Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.field_230130_i_).withChance(0.5F)), Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.field_230129_h_))).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(/*10*/8, 0.1F, 1))));
		addFeature("tree_jungle_mega", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.MEGA_JUNGLE_TREE.withConfiguration(DefaultBiomeFeatures.MEGA_JUNGLE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(50, 0.1F, 1))));
		addFeature("tree_jungle", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.JUNGLE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		addFeature("tree_jungle_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.JUNGLE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		addFeature("tree_jungle_vines", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.JUNGLE_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		addFeature("tree_acacia", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.ACACIA_TREE.withConfiguration(DefaultBiomeFeatures.ACACIA_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(8/*1*/, 0.1F, 1))));
		addFeature("tree_oak_dark", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.DARK_OAK_TREE.withConfiguration(DefaultBiomeFeatures.DARK_OAK_TREE_CONFIG).withPlacement(Placement.DARK_OAK_TREE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		addFeature("tree_oak_big", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FANCY_TREE.withConfiguration(DefaultBiomeFeatures.FANCY_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
		addFeature("tree_oak", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(6/*10*/, 0.1F, 1))));
		addFeature("tree_oak_swamp", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.NORMAL_TREE.withConfiguration(DefaultBiomeFeatures.SWAMP_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		addFeature("clay", GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(CLAY, 4, 1, Lists.newArrayList(DIRT, CLAY))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
		addFeature("sand", GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(SAND, 7, 2, Lists.newArrayList(DIRT, GRASS_BLOCK))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(3))));
		addFeature("gravel", GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(GRAVEL, 6, 2, Lists.newArrayList(DIRT, GRASS_BLOCK))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(1))));
		addFeature("mushroom_brown", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE/*COUNT_CHANCE_HEIGHTMAP*/.configure(new HeightWithChanceConfig(8, 0.25F))));
		addFeature("mushroom_red", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP_DOUBLE.configure(new HeightWithChanceConfig(8, 0.25F/*0.125F*/))));
		addFeature("reed"/*"sugar_cane"*/, GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.SUGAR_CANE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(10))));
		addFeature("cactus", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.CACTUS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(5))));
		addFeature("waterlily"/*"lily_pad"*/, GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILY_PAD_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(4))));
		addFeature("ice_spike", GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.ICE_SPIKE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(3))));
		addFeature("ice_path"/*"ice_patch"*/, GenerationStage.Decoration.SURFACE_STRUCTURES, Feature.ICE_PATCH.withConfiguration(new FeatureRadiusConfig(2)).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(2))));
		addFeature("shrub_jungle", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.JUNGLE_GROUND_BUSH.withConfiguration(DefaultBiomeFeatures.JUNGLE_GROUND_BUSH_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		addFeature("fern", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.TAIGA_GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(7))));
		addFeature("grass", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("grass_tussock", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.GRASS_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("blob"/*"forest_rock"*/, GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK.withConfiguration(new BlockBlobConfig(MOSSY_COBBLESTONE, 0)).withPlacement(Placement.FOREST_ROCK.configure(new FrequencyConfig(3))));
		addFeature("bush_dead", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.DEAD_BUSH_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_dandelion", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(DANDELION_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_poppy", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(POPPY_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_orchid_blue", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(DefaultBiomeFeatures.BLUE_ORCHID_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(1))));
		addFeature("flower_allium", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(ALLIUM_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_houstonia", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(AZURE_BLUET_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_tulip_red", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(RED_TULIP_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_tulip_orange", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(ORANGE_TULIP_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_tulip_white", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(WHITE_TULIP_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_tulip_pink", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(PINK_TULIP_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_oxeyedaisy", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(OXEYE_DAISY_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_cornflower", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(CORNFLOWER_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("flower_lilyofthevalley", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(DefaultBiomeFeatures.LILY_OF_THE_VALLEY_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		addFeature("pumpkin", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.PUMPKIN_PATCH_CONFIG).withPlacement(Placement./*COUNT_HEIGHTMAP_DOUBLE*/CHANCE_HEIGHTMAP_DOUBLE.configure(new /*FrequencyConfig(1)*/ChanceConfig(32))));
		addFeature("melon", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.MELON_PATCH_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
		addFeature("flower_rose_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.ROSE_BUSH_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(10))));
		addFeature("flower_syringa_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LILAC_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(10))));
		addFeature("flower_paeonia_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.PEONY_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(10))));
		addFeature("flower_sunflower_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.SUNFLOWER_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(10))));
		addFeature("grass_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.TALL_GRASS_CONFIG).withPlacement(Placement./*NOISE_HEIGHTMAP_32*/COUNT_HEIGHTMAP_32.configure(new /*NoiseDependant(-0.8D, 0, 7)*/FrequencyConfig(7))));
		addFeature("fern_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.LARGE_FERN_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_32.configure(new FrequencyConfig(7))));
		addFeature("water_flowing", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(DefaultBiomeFeatures.WATER_SPRING_CONFIG).withPlacement(Placement.COUNT_BIASED_RANGE.configure(new CountRangeConfig(50, 8, 8, 256))));
		addFeature("lava_flowing", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SPRING_FEATURE.withConfiguration(DefaultBiomeFeatures.LAVA_SPRING_CONFIG).withPlacement(Placement.COUNT_VERY_BIASED_RANGE.configure(new CountRangeConfig(20, 8, 16, 256))));
		addFeature("mushroom_brown_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.HUGE_BROWN_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_BROWN_MUSHROOM).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
		addFeature("mushroom_red_tall", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.HUGE_RED_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_RED_MUSHROOM).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));
		addFeature("coral", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(ImmutableList.of(Feature.CORAL_TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), Feature.CORAL_CLAW.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG), Feature.CORAL_MUSHROOM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)))).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(20, 400.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG))));
		addFeature("kelp", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.KELP.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(80, 80.0D, 0.0D, Heightmap.Type.OCEAN_FLOOR_WG))));
		addFeature("seagrass", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SEAGRASS.withConfiguration(new SeaGrassConfig(80, 0.3D)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		addFeature("bush_berry_sweet", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.SWEET_BERRY_BUSH_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(1))));
		addFeature("sea_pickle", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.SEA_PICKLE.withConfiguration(new CountConfig(20)).withPlacement(Placement.CHANCE_TOP_SOLID_HEIGHTMAP.configure(new ChanceConfig(16))));
		addFeature("bamboo", GenerationStage.Decoration.VEGETAL_DECORATION, Feature.BAMBOO.withConfiguration(new ProbabilityConfig(0.2F)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(160, 80.0D, 0.3D, Heightmap.Type.WORLD_SURFACE_WG))));
		addFeature("tree_birch_fallen", GenerationStage.Decoration.VEGETAL_DECORATION, DooglamooFeatures.FALLEN_TREE.withConfiguration(BIRCH_FALLEN_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.2F, 1))));
		addFeature("tree_oak_fallen", GenerationStage.Decoration.VEGETAL_DECORATION, DooglamooFeatures.FALLEN_TREE.withConfiguration(OAK_FALLEN_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.2F, 1))));
		addFeature("tree_spruce_fallen", GenerationStage.Decoration.VEGETAL_DECORATION, DooglamooFeatures.FALLEN_TREE.withConfiguration(SPRUCE_FALLEN_TREE_CONFIG).withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(new AtSurfaceWithExtraConfig(2, 0.2F, 1))));
		addFeature("rock_river_mossy", GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK.withConfiguration(new BlockBlobConfig(MOSSY_COBBLESTONE, 0)).withPlacement(Placement.FOREST_ROCK.configure(new FrequencyConfig(6))));
		addFeature("rock_river", GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.FOREST_ROCK.withConfiguration(new BlockBlobConfig(COBBLESTONE, 0)).withPlacement(Placement.FOREST_ROCK.configure(new FrequencyConfig(6))));
		
		// biomesoplenty
		if (PINK_CHERRY_TREE != null) addFeature("tree_cherry_pink", GenerationStage.Decoration.VEGETAL_DECORATION, PINK_CHERRY_TREE.withConfiguration(DefaultBiomeFeatures.field_230133_p_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (BIG_PINK_CHERRY_TREE != null) addFeature("tree_cherry_pink_large", GenerationStage.Decoration.VEGETAL_DECORATION, BIG_PINK_CHERRY_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (WHITE_CHERRY_TREE != null) addFeature("tree_cherry_white", GenerationStage.Decoration.VEGETAL_DECORATION, WHITE_CHERRY_TREE.withConfiguration(DefaultBiomeFeatures.field_230133_p_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (BIG_WHITE_CHERRY_TREE != null) addFeature("tree_cherry_white_large", GenerationStage.Decoration.VEGETAL_DECORATION, BIG_WHITE_CHERRY_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (FIR_TREE != null) addFeature("tree_fir", GenerationStage.Decoration.VEGETAL_DECORATION, FIR_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (FIR_TREE_LARGE != null) addFeature("tree_fir_mega", GenerationStage.Decoration.VEGETAL_DECORATION, FIR_TREE_LARGE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (WILLOW_TREE != null) addFeature("tree_willow", GenerationStage.Decoration.VEGETAL_DECORATION, WILLOW_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (YELLOW_AUTUMN_TREE != null) addFeature("tree_autumn_yellow", GenerationStage.Decoration.VEGETAL_DECORATION, YELLOW_AUTUMN_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (ORANGE_AUTUMN_TREE != null) addFeature("tree_autumn_orange", GenerationStage.Decoration.VEGETAL_DECORATION, ORANGE_AUTUMN_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (JUNGLE_TWIGLET_TREE != null) addFeature("tree_jungle_twiglet", GenerationStage.Decoration.VEGETAL_DECORATION, JUNGLE_TWIGLET_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (DYING_TREE != null) addFeature("tree_dying", GenerationStage.Decoration.VEGETAL_DECORATION, DYING_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (DEAD_TREE != null) addFeature("tree_dead", GenerationStage.Decoration.VEGETAL_DECORATION, DEAD_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (DARK_OAK_POPLAR != null) addFeature("tree_poplar_dark", GenerationStage.Decoration.VEGETAL_DECORATION, DARK_OAK_POPLAR.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (BIRCH_POPLAR != null) addFeature("tree_poplar", GenerationStage.Decoration.VEGETAL_DECORATION, BIRCH_POPLAR.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (JACARANDA_TREE != null) addFeature("tree_jacaranda", GenerationStage.Decoration.VEGETAL_DECORATION, JACARANDA_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (MAPLE_TREE != null) addFeature("tree_maple", GenerationStage.Decoration.VEGETAL_DECORATION, MAPLE_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (MAGIC_TREE != null) addFeature("tree_magic", GenerationStage.Decoration.VEGETAL_DECORATION, MAGIC_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (PALM_TREE != null) addFeature("tree_palm", GenerationStage.Decoration.VEGETAL_DECORATION, PALM_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (TALL_UMBRAN_TREE != null) addFeature("tree_umbran_mega", GenerationStage.Decoration.VEGETAL_DECORATION, TALL_UMBRAN_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (ACACIA_BUSH != null) addFeature("bush_acacia", GenerationStage.Decoration.VEGETAL_DECORATION, ACACIA_BUSH.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (ACACIA_TWIGLET_TREE != null) addFeature("tree_acacia_twiglet", GenerationStage.Decoration.VEGETAL_DECORATION, ACACIA_TWIGLET_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (MAHOGANY_TREE != null) addFeature("tree_mahogany", GenerationStage.Decoration.VEGETAL_DECORATION, MAHOGANY_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (REDWOOD_TREE != null) addFeature("tree_redwood", GenerationStage.Decoration.VEGETAL_DECORATION, REDWOOD_TREE.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (REDWOOD_TREE_MEDIUM != null) addFeature("tree_redwood_thin", GenerationStage.Decoration.VEGETAL_DECORATION, REDWOOD_TREE_MEDIUM.withConfiguration(DefaultBiomeFeatures.field_230132_o_).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(5))));
		if (LAVENDER_FLOWERS != null) addFeature("flower_lavender", GenerationStage.Decoration.VEGETAL_DECORATION, LAVENDER_FLOWERS.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		
		// lostcities
		if (LOSTCITY_FEATURE != null) addFeature("lostcity", GenerationStage.Decoration.LOCAL_MODIFICATIONS, LOSTCITY_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 0, 0, 1))));
	}
	
	private static void addFeature(String name, GenerationStage.Decoration decorationStage, ConfiguredFeature<?, ?> feature)
	{
		if (!featureDictionary.containsKey(name))
		{
			featureDictionary.put(name, feature);
		}
		else
		{
			DooglamooWorldsMod.LOGGER.info("Duplicate feature: " + name);
		}
	}
	
	public static void tagsUpdated(TagsUpdatedEvent event)
	{
		if (EffectiveSide.get() == LogicalSide.SERVER)
    	{
			findModdedOre("copper", event);
			findModdedOre("tin", event);
			findModdedOre("aluminum", event);
			findModdedOre("lead", event);
			findModdedOre("silver", event);
			findModdedOre("nickel", event);
			findModdedOre("uranium", event);
    	}
	}
	
	public static void loadComplete()
	{
		registerBlock("biomesoplenty", "ash_block", "ash_block");
		registerBlock("biomesoplenty", "mud", "mud_mud");
		registerBlock("biomesoplenty", "white_sand", "sand_white");
	}
	
	private static void findModdedOre(String name, TagsUpdatedEvent event)
	{
		if (event.getTagManager().getBlocks().get(new ResourceLocation("forge", "ores/" + name)) != null)
		{
			Optional<Block> block = event.getTagManager().getBlocks().get(new ResourceLocation("forge", "ores/" + name)).getAllElements().stream().findFirst();
			if (block.isPresent())
			{
				blockDictionary.put(name + "_ore", block.get().getDefaultState());
			}
		}
	}
	
	private static void registerBlock(String domain, String id, String name)
	{
		Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(domain, id));
		if (block != null && block != Blocks.AIR)
		{
			blockDictionary.put(name, block.getDefaultState());
		}
//		else
//		{
//			ExampleMod.LOGGER.info("No registered block found: " + new ResourceLocation(domain, id));
//		}
	}
	
	public static GeoFeature getGeoFeature(String key, JsonObject json)
	{
		if (json == null || json.isJsonNull())
		{
            throw new JsonSyntaxException("Json cannot be null");
		}

		String type = JSONUtils.getString(json, "type");
        if (type.isEmpty())
        {
            throw new JsonSyntaxException("Material type can not be an empty string");
        }
		
		if (!type.contains(":"))
		{
			type = "dooglamooworlds:" + type;
		}
        
        key = key.substring(key.lastIndexOf("/") + 1);
        
        int level = key.split("_").length;
        
        GeoFeature geo = null;
        
        switch (type)
        {
        case "dooglamooworlds:block":
        	{
        		BlockState state = DictionaryFactory.blockDictionary.get(key);
        		if (state != null)
        		{
	        		geo = new GeoFeature(state, level);
	        		MinMaxBounds temp = MinMaxBounds.deserialize(json.get("temperature"));
	        		MinMaxBounds precip = MinMaxBounds.deserialize(json.get("precipitation"));
	        		MinMaxBounds lift = MinMaxBounds.deserialize(json.get("uplift"));
	        		MinMaxBounds erosion = MinMaxBounds.deserialize(json.get("erosion"));
	        		MinMaxBounds thermal = MinMaxBounds.deserialize(json.get("volcanism"));
	        		MinMaxBounds age = MinMaxBounds.deserialize(json.get("era"));
	        		MinMaxBounds plates = MinMaxBounds.deserialize(json.get("elevation"));
	        		MinMaxBounds rock = MinMaxBounds.deserialize(json.get("density"));
	        		geo.setMinTemperature(temp.getMin());
	        		geo.setMaxTemperature(temp.getMax());
	        		geo.setMinPrecipitation(precip.getMin());
	        		geo.setMaxPrecipitation(precip.getMax());
	        		geo.setMinUplift(lift.getMin());
	        		geo.setMaxUplift(lift.getMax());
	        		geo.setMinErosion(erosion.getMin());
	        		geo.setMaxErosion(erosion.getMax());
	        		geo.setMinVolcanism(thermal.getMin());
	        		geo.setMaxVolcanism(thermal.getMax());
	        		geo.setMinEra(age.getMin());
	        		geo.setMaxEra(age.getMax());
	        		geo.setMinElevation(plates.getMin());
	        		geo.setMaxElevation(plates.getMax());
	        		geo.setMinDensity(rock.getMin());
	        		geo.setMaxDensity(rock.getMax());
        		}
        		else
        		{
        			DooglamooWorldsMod.LOGGER.info("No registered block found: " + key);
        		}
        		break;
        	}
        case "dooglamooworlds:block_ore":
    	{
    		BlockState state = DictionaryFactory.blockDictionary.get(key);
    		if (state != null)
    		{
        		geo = new GeoFeature(state, JSONUtils.getInt(json, "spacing", 23), JSONUtils.getInt(json, "depth", 255));
        		JsonArray rar = JSONUtils.getJsonArray(json, "rarity");
    			if (rar.size() > 0)
    			{
    				geo.rarity[0] = rar.get(0).getAsFloat();
    			}
    			if (rar.size() > 1)
    			{
    				geo.rarity[1] = rar.get(1).getAsFloat();
    			}
    			if (rar.size() > 2)
    			{
    				geo.rarity[2] = rar.get(2).getAsFloat();
    			}
    			JsonArray req = JSONUtils.getJsonArray(json, "requirements", new JsonArray());
    			int requirement = GeoFeature.INDEX_ONE;
    			if (req.size() > 0)
    			{
	    			for (Entry<String, JsonElement> entry : req.get(0).getAsJsonObject().entrySet())
	    			{
	    				switch (entry.getKey())
	    				{
	    				case "elevation": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ELEVATION_LOW : GeoFeature.INDEX_ELEVATION_HIGH; break;
	    				case "density": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_DENSITY_LOW : GeoFeature.INDEX_DENSITY_HIGH; break;
	    				case "uplift": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_UPLIFT_LOW : GeoFeature.INDEX_UPLIFT_HIGH; break;
	    				case "volcanism": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_VOLCANISM_LOW : GeoFeature.INDEX_VOLCANISM_HIGH; break;
	    				case "era": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ERA_LOW : GeoFeature.INDEX_ERA_HIGH; break;
	    				case "erosion": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_EROSION_LOW : GeoFeature.INDEX_EROSION_HIGH; break;
	    				case "temperature": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_TEMPERATURE_LOW : GeoFeature.INDEX_TEMPERATURE_HIGH; break;
	    				case "precipitation": requirement = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_PRECIPITATION_LOW : GeoFeature.INDEX_PRECIPITATION_HIGH; break;
	    				}
	    			}
    			}
    			geo.requirement = requirement;
    			JsonArray mod = JSONUtils.getJsonArray(json, "modifiers", new JsonArray());
    			int modifier1 = GeoFeature.INDEX_ZERO;
    			int modifier2 = GeoFeature.INDEX_ZERO;
    			if (mod.size() > 0)
    			{
	    			for (Entry<String, JsonElement> entry : mod.get(0).getAsJsonObject().entrySet())
	    			{
	    				switch (entry.getKey())
	    				{
	    				case "elevation": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ELEVATION_LOW : GeoFeature.INDEX_ELEVATION_HIGH; break;
	    				case "density": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_DENSITY_LOW : GeoFeature.INDEX_DENSITY_HIGH; break;
	    				case "uplift": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_UPLIFT_LOW : GeoFeature.INDEX_UPLIFT_HIGH; break;
	    				case "volcanism": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_VOLCANISM_LOW : GeoFeature.INDEX_VOLCANISM_HIGH; break;
	    				case "era": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ERA_LOW : GeoFeature.INDEX_ERA_HIGH; break;
	    				case "erosion": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_EROSION_LOW : GeoFeature.INDEX_EROSION_HIGH; break;
	    				case "temperature": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_TEMPERATURE_LOW : GeoFeature.INDEX_TEMPERATURE_HIGH; break;
	    				case "precipitation": modifier1 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_PRECIPITATION_LOW : GeoFeature.INDEX_PRECIPITATION_HIGH; break;
	    				}
	    			}
    			}
    			if (mod.size() > 1)
    			{
	    			for (Entry<String, JsonElement> entry : mod.get(1).getAsJsonObject().entrySet())
	    			{
	    				switch (entry.getKey())
	    				{
	    				case "elevation": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ELEVATION_LOW : GeoFeature.INDEX_ELEVATION_HIGH; break;
	    				case "density": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_DENSITY_LOW : GeoFeature.INDEX_DENSITY_HIGH; break;
	    				case "uplift": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_UPLIFT_LOW : GeoFeature.INDEX_UPLIFT_HIGH; break;
	    				case "volcanism": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_VOLCANISM_LOW : GeoFeature.INDEX_VOLCANISM_HIGH; break;
	    				case "era": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_ERA_LOW : GeoFeature.INDEX_ERA_HIGH; break;
	    				case "erosion": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_EROSION_LOW : GeoFeature.INDEX_EROSION_HIGH; break;
	    				case "temperature": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_TEMPERATURE_LOW : GeoFeature.INDEX_TEMPERATURE_HIGH; break;
	    				case "precipitation": modifier2 = "low".equals(entry.getValue().getAsString()) ? GeoFeature.INDEX_PRECIPITATION_LOW : GeoFeature.INDEX_PRECIPITATION_HIGH; break;
	    				}
	    			}
    			}
    			geo.modifier[0] = modifier1;
    			geo.modifier[1] = modifier2;
        		MinMaxBounds density = MinMaxBounds.deserialize(json.get("density"));
        		geo.setMinDensity(density.getMin());
        		geo.setMaxDensity(density.getMax());
    		}
    		else
    		{
    			DooglamooWorldsMod.LOGGER.info("No registered block found: " + key);
    		}
    		break;
    	}
        case "dooglamooworlds:feature":
        case "dooglamooworlds:feature_ocean":
    	{
    		ConfiguredFeature<?, ?> gen = DictionaryFactory.featureDictionary.get(key);
    		if (gen != null)
    		{
        		geo = new GeoFeature(gen, JSONUtils.getFloat(json, "rarity"));
        		MinMaxBounds temp = MinMaxBounds.deserialize(json.get("temperature"));
        		MinMaxBounds precip = MinMaxBounds.deserialize(json.get("precipitation"));
        		MinMaxBounds lift = MinMaxBounds.deserialize(json.get("uplift"));
        		MinMaxBounds erosion = MinMaxBounds.deserialize(json.get("erosion"));
        		MinMaxBounds thermal = MinMaxBounds.deserialize(json.get("volcanism"));
        		MinMaxBounds age = MinMaxBounds.deserialize(json.get("era"));
        		MinMaxBounds plates = MinMaxBounds.deserialize(json.get("elevation"));
        		MinMaxBounds rock = MinMaxBounds.deserialize(json.get("density"));
        		geo.setMinTemperature(temp.getMin());
        		geo.setMaxTemperature(temp.getMax());
        		geo.setMinPrecipitation(precip.getMin());
        		geo.setMaxPrecipitation(precip.getMax());
        		geo.setMinUplift(lift.getMin());
        		geo.setMaxUplift(lift.getMax());
        		geo.setMinErosion(erosion.getMin());
        		geo.setMaxErosion(erosion.getMax());
        		geo.setMinVolcanism(thermal.getMin());
        		geo.setMaxVolcanism(thermal.getMax());
        		geo.setMinEra(age.getMin());
        		geo.setMaxEra(age.getMax());
        		geo.setMinElevation(plates.getMin());
        		geo.setMaxElevation(plates.getMax());
        		geo.setMinDensity(rock.getMin());
        		geo.setMaxDensity(rock.getMax());
    		}
    		else
    		{
    			DooglamooWorldsMod.LOGGER.info("No registered feature found: " + key);
    		}
    		break;
    	}
        case "dooglamooworlds:biome":
        case "dooglamooworlds:biome_ocean":
    	{
    		String domain = JSONUtils.getString(json, "domain", "minecraft");
    		Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(domain, key));
    		if (biome != null)
    		{
        		geo = new GeoFeature(biome, "minecraft".equals(domain) ? 0 : 1);
        		MinMaxBounds temp = MinMaxBounds.deserialize(json.get("temperature"));
        		MinMaxBounds precip = MinMaxBounds.deserialize(json.get("precipitation"));
        		MinMaxBounds lift = MinMaxBounds.deserialize(json.get("uplift"));
        		MinMaxBounds erosion = MinMaxBounds.deserialize(json.get("erosion"));
        		MinMaxBounds thermal = MinMaxBounds.deserialize(json.get("volcanism"));
        		MinMaxBounds age = MinMaxBounds.deserialize(json.get("era"));
        		MinMaxBounds plates = MinMaxBounds.deserialize(json.get("elevation"));
        		MinMaxBounds rock = MinMaxBounds.deserialize(json.get("density"));
        		geo.setMinTemperature(temp.getMin());
        		geo.setMaxTemperature(temp.getMax());
        		geo.setMinPrecipitation(precip.getMin());
        		geo.setMaxPrecipitation(precip.getMax());
        		geo.setMinUplift(lift.getMin());
        		geo.setMaxUplift(lift.getMax());
        		geo.setMinErosion(erosion.getMin());
        		geo.setMaxErosion(erosion.getMax());
        		geo.setMinVolcanism(thermal.getMin());
        		geo.setMaxVolcanism(thermal.getMax());
        		geo.setMinEra(age.getMin());
        		geo.setMaxEra(age.getMax());
        		geo.setMinElevation(plates.getMin());
        		geo.setMaxElevation(plates.getMax());
        		geo.setMinDensity(rock.getMin());
        		geo.setMaxDensity(rock.getMax());
    		}
    		else
    		{
    			DooglamooWorldsMod.LOGGER.info("No registered biome found: " + key);
    		}
    		break;
    	}
        default: DooglamooWorldsMod.LOGGER.info("Unknown geofeature type: " + type);
        }
        
		return geo;
	}
	
	public static void register(GeoFeature geo)
	{
		if (geo == null)
		{
			return;
		}
		
		if (geo.getType() == GeoFeature.Type.Biome)
		{
			if (geo.level == 0)
			{
				baseBiomes.add(geo.getBiome());
			}
			else
			{
				addedBiomes.add(geo.getBiome());
			}
			biomeCount++;
		}
		else if (geo.getType() == GeoFeature.Type.Block)
		{
			blockCount++;
		}
		else if (geo.getType() == GeoFeature.Type.Feature)
		{
			featureCount++;
		}
		else if (geo.getType() == GeoFeature.Type.OreBlock)
		{
			for (int i = geo.getMinDensity(); i <= geo.getMaxDensity(); i++)
			{
				List<GeoFeature> list = ores.get(i);
				if (list == null)
				{
					list = new ArrayList<GeoFeature>();
					ores.put(i, list);
				}
				int origIndex = -1;
				for (int j = 0; j < list.size(); j++)
				{
					GeoFeature orig = list.get(j);
					if (orig.blockstate.equals(geo.blockstate))
					{
						origIndex = j;
						break;
					}
				}
				if (origIndex >= 0)
				{
					list.set(origIndex, geo);
				}
				else
				{
					list.add(geo);
				}
			}
			return;
		}
		
		for (int t = geo.getMinTemperature(); t <= geo.getMaxTemperature(); t++)
		{
			for (int w = geo.getMinPrecipitation(); w <= geo.getMaxPrecipitation(); w++)
			{
				for (int u = geo.getMinUplift(); u <= geo.getMaxUplift(); u++)
				{
					for (int e = geo.getMinErosion(); e <= geo.getMaxErosion(); e++)
					{
						for (int g = geo.getMinVolcanism(); g <= geo.getMaxVolcanism(); g++)
						{
							for (int a = geo.getMinEra(); a <= geo.getMaxEra(); a++)
							{
								for (int p = geo.getMinElevation(); p <= geo.getMaxElevation(); p++)
								{
									for (int r = geo.getMinDensity(); r <= geo.getMaxDensity(); r++)
									{
										int id = (t << 14) | (w << 12) | (u << 10) | (e << 8) | (g << 6) | (a << 4) | (p << 2) | (r << 0);
										
										List<GeoFeature> list = null;
										switch (geo.getType())
										{
										case Block: list = DictionaryFactory.blocks.get(id); break;
										case OreBlock: break;
										case Feature: list = DictionaryFactory.features.get(id); break;
										case Biome: list = DictionaryFactory.biomes.get(id); break;
										}
										if (list == null)
										{
											list = new ArrayList<GeoFeature>();
											switch (geo.getType())
											{
											case Block: DictionaryFactory.blocks.put(id, list); break;
											case OreBlock: break;
											case Feature: DictionaryFactory.features.put(id, list); break;
											case Biome: DictionaryFactory.biomes.put(id, list); break;
											}
										}
										if (geo.getType() == GeoFeature.Type.Biome)
										{
											if (list.size() > 0 && geo.getLevel() <= list.get(0).getLevel())
											{
												if (geo.getLevel() == list.get(0).getLevel())
												{
													DooglamooWorldsMod.LOGGER.info("registering biome mapping failed: " + geo.getBiome() + " overlaps " + list.get(0).getBiome());
												}
											}
											else
											{
												list.clear();
												list.add(geo);
											}
										}
										else if (geo.getType() == GeoFeature.Type.Block)
										{
											boolean skip = false;
											Iterator<GeoFeature> it = list.iterator();
											while (it.hasNext())
											{
												GeoFeature feature = it.next();
												if (feature.getLevel() < geo.getLevel())
												{
													it.remove();
												}
												else if (feature.getLevel() > geo.getLevel())
												{
													skip = true;
												}
											}
											if (!skip)
											{
												list.add(geo);
											}
										}
										else
										{
											list.add(geo);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
