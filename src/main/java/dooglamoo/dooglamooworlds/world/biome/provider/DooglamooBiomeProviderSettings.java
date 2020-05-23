/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.biome.provider;

import dooglamoo.dooglamooworlds.world.gen.DooglamooGenSettings;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.storage.WorldInfo;

public class DooglamooBiomeProviderSettings implements IBiomeProviderSettings
{
	private WorldInfo worldInfo;
	private DooglamooGenSettings generatorSettings;

	public DooglamooBiomeProviderSettings setWorldInfo(WorldInfo info)
	{
		this.worldInfo = info;
		return this;
	}

	public DooglamooBiomeProviderSettings setGeneratorSettings(DooglamooGenSettings generator)
	{
		this.generatorSettings = generator;
		return this;
	}

	public WorldInfo getWorldInfo()
	{
		return this.worldInfo;
	}

	public DooglamooGenSettings getGeneratorSettings()
	{
		return this.generatorSettings;
	}
}
