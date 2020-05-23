/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DooglamooGenSettings extends GenerationSettings
{
	// scale: 1.0 to 3.0
	public double elevationScale = 2.0;
	public double densityScale = 2.0;
	public double upliftScale = 2.0;
	public double volcanismScale = 2.0;
	public double eraScale = 2.0;
	public double erosionScale = 2.0;
	public double temperatureScale = 2.0;
	public double precipitationScale = 2.0;
	
	// amplitude: 0.0 to 2.0
	public double elevationAmplitude = 1.0;
	public double densityAmplitude = 1.0;
	public double upliftAmplitude = 1.0;
	public double volcanismAmplitude = 1.0;
	public double eraAmplitude = 1.0;
	public double erosionAmplitude = 1.0;
	public double temperatureAmplitude = 1.0;
	public double precipitationAmplitude = 1.0;
	
	// weight: -0.6 to 0.6
	public double elevationWeight = 0.0;
	public double densityWeight = 0.0;
	public double upliftWeight = 0.0;
	public double volcanismWeight = 0.0;
	public double eraWeight = 0.0;
	public double erosionWeight = 0.0;
	public double temperatureWeight = 0.0;
	public double precipitationWeight = 0.0;
	
	@OnlyIn(Dist.CLIENT)
	public <T> Dynamic<T> buildSettings(DynamicOps<T> dyn)
	{
		ImmutableMap.Builder<T, T> map = new ImmutableMap.Builder<T, T>();
		
		map.put(dyn.createString("elevationScale"), dyn.createDouble(elevationScale));
		map.put(dyn.createString("densityScale"), dyn.createDouble(densityScale));
		map.put(dyn.createString("upliftScale"), dyn.createDouble(upliftScale));
		map.put(dyn.createString("volcanismScale"), dyn.createDouble(volcanismScale));
		map.put(dyn.createString("eraScale"), dyn.createDouble(eraScale));
		map.put(dyn.createString("erosionScale"), dyn.createDouble(erosionScale));
		map.put(dyn.createString("temperatureScale"), dyn.createDouble(temperatureScale));
		map.put(dyn.createString("precipitationScale"), dyn.createDouble(precipitationScale));
		map.put(dyn.createString("elevationAmplitude"), dyn.createDouble(elevationAmplitude));
		map.put(dyn.createString("densityAmplitude"), dyn.createDouble(densityAmplitude));
		map.put(dyn.createString("upliftAmplitude"), dyn.createDouble(upliftAmplitude));
		map.put(dyn.createString("volcanismAmplitude"), dyn.createDouble(volcanismAmplitude));
		map.put(dyn.createString("eraAmplitude"), dyn.createDouble(eraAmplitude));
		map.put(dyn.createString("erosionAmplitude"), dyn.createDouble(erosionAmplitude));
		map.put(dyn.createString("temperatureAmplitude"), dyn.createDouble(temperatureAmplitude));
		map.put(dyn.createString("precipitationAmplitude"), dyn.createDouble(precipitationAmplitude));
		map.put(dyn.createString("elevationWeight"), dyn.createDouble(elevationWeight));
		map.put(dyn.createString("densityWeight"), dyn.createDouble(densityWeight));
		map.put(dyn.createString("upliftWeight"), dyn.createDouble(upliftWeight));
		map.put(dyn.createString("volcanismWeight"), dyn.createDouble(volcanismWeight));
		map.put(dyn.createString("eraWeight"), dyn.createDouble(eraWeight));
		map.put(dyn.createString("erosionWeight"), dyn.createDouble(erosionWeight));
		map.put(dyn.createString("temperatureWeight"), dyn.createDouble(temperatureWeight));
		map.put(dyn.createString("precipitationWeight"), dyn.createDouble(precipitationWeight));
		
		return new Dynamic<>(dyn, dyn.createMap(map.build()));
	}
	
	public static DooglamooGenSettings createDooglamooGenerator(Dynamic<?> dyn)
	{
		DooglamooGenSettings settings = new DooglamooGenSettings();
		
		settings.elevationScale = dyn.get("elevationScale").asDouble(settings.elevationScale);
		settings.densityScale = dyn.get("densityScale").asDouble(settings.densityScale);
		settings.upliftScale = dyn.get("upliftScale").asDouble(settings.upliftScale);
		settings.volcanismScale = dyn.get("volcanismScale").asDouble(settings.volcanismScale);
		settings.eraScale = dyn.get("eraScale").asDouble(settings.eraScale);
		settings.erosionScale = dyn.get("erosionScale").asDouble(settings.erosionScale);
		settings.temperatureScale = dyn.get("temperatureScale").asDouble(settings.temperatureScale);
		settings.precipitationScale = dyn.get("precipitationScale").asDouble(settings.precipitationScale);
		settings.elevationAmplitude = dyn.get("elevationAmplitude").asDouble(settings.elevationAmplitude);
		settings.densityAmplitude = dyn.get("densityAmplitude").asDouble(settings.densityAmplitude);
		settings.upliftAmplitude = dyn.get("upliftAmplitude").asDouble(settings.upliftAmplitude);
		settings.volcanismAmplitude = dyn.get("volcanismAmplitude").asDouble(settings.volcanismAmplitude);
		settings.eraAmplitude = dyn.get("eraAmplitude").asDouble(settings.eraAmplitude);
		settings.erosionAmplitude = dyn.get("erosionAmplitude").asDouble(settings.erosionAmplitude);
		settings.temperatureAmplitude = dyn.get("temperatureAmplitude").asDouble(settings.temperatureAmplitude);
		settings.precipitationAmplitude = dyn.get("precipitationAmplitude").asDouble(settings.precipitationAmplitude);
		settings.elevationWeight = dyn.get("elevationWeight").asDouble(settings.elevationWeight);
		settings.densityWeight = dyn.get("densityWeight").asDouble(settings.densityWeight);
		settings.upliftWeight = dyn.get("upliftWeight").asDouble(settings.upliftWeight);
		settings.volcanismWeight = dyn.get("volcanismWeight").asDouble(settings.volcanismWeight);
		settings.eraWeight = dyn.get("eraWeight").asDouble(settings.eraWeight);
		settings.erosionWeight = dyn.get("erosionWeight").asDouble(settings.erosionWeight);
		settings.temperatureWeight = dyn.get("temperatureWeight").asDouble(settings.temperatureWeight);
		settings.precipitationWeight = dyn.get("precipitationWeight").asDouble(settings.precipitationWeight);
		
		return settings;
	}
	
	public static DooglamooGenSettings getDefaultDooglamooGenerator()
	{
		return new DooglamooGenSettings();
	}
}
