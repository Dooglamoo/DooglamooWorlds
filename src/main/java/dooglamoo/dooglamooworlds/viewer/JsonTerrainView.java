/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.viewer;

import java.awt.Color;

import dooglamoo.dooglamooworlds.dict.MinMaxBounds;
import com.google.gson.JsonObject;

import net.minecraft.util.JSONUtils;

public class JsonTerrainView implements TerrainView
{
	private final MinMaxBounds temperature;
	private final MinMaxBounds precipitation;
	private final MinMaxBounds uplift;
	private final MinMaxBounds erosion;
	private final MinMaxBounds volcanism;
	private final MinMaxBounds era;
	private final MinMaxBounds elevation;
	private final MinMaxBounds density;
	
	public JsonTerrainView(String jsonStr)
	{
		JsonObject json = JSONUtils.fromJson(jsonStr);
		
		temperature = MinMaxBounds.deserialize(json.get("temperature"));
		precipitation = MinMaxBounds.deserialize(json.get("precipitation"));
		uplift = MinMaxBounds.deserialize(json.get("uplift"));
		erosion = MinMaxBounds.deserialize(json.get("erosion"));
		volcanism = MinMaxBounds.deserialize(json.get("volcanism"));
		era = MinMaxBounds.deserialize(json.get("era"));
		elevation = MinMaxBounds.deserialize(json.get("elevation"));
		density = MinMaxBounds.deserialize(json.get("density"));
	}
	
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		Color color = Color.WHITE;
		start:
		for (int t = temperature.getMin(); t <= temperature.getMax(); t++)
		{
			for (int w = precipitation.getMin(); w <= precipitation.getMax(); w++)
			{
				for (int u = uplift.getMin(); u <= uplift.getMax(); u++)
				{
					for (int e = erosion.getMin(); e <= erosion.getMax(); e++)
					{
						for (int g = volcanism.getMin(); g <= volcanism.getMax(); g++)
						{
							for (int a = era.getMin(); a <= era.getMax(); a++)
							{
								for (int p = elevation.getMin(); p <= elevation.getMax(); p++)
								{
									for (int r = density.getMin(); r <= density.getMax(); r++)
									{
										int code1 = (t << 14) | (w << 12) | (u << 10) | (e << 8) | (g << 6) | (a << 4) | (p << 2) | (r << 0);
										if (code1 == code)
										{
											color = Color.BLACK;
											break start;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return color;
	}
}
