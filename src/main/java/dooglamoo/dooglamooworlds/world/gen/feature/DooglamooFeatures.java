/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen.feature;

import net.minecraft.world.gen.feature.*;
import net.minecraftforge.registries.IForgeRegistry;

public class DooglamooFeatures
{
	public static final Feature<NoFeatureConfig> ORE = new DooglamooOreFeature(NoFeatureConfig::deserialize);
	public static final Feature<BlockClusterFeatureConfig> FALLEN_TREE = new FallenTreeFeature(BlockClusterFeatureConfig::deserialize);
	
	public static void register(IForgeRegistry<Feature<?>> registry)
	{
		registry.register(ORE.setRegistryName("ore"));
		registry.register(FALLEN_TREE.setRegistryName("fallen_tree"));
	}
}
