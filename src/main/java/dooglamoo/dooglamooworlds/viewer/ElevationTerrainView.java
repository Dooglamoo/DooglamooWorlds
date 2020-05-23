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

import net.minecraft.util.math.MathHelper;

public class ElevationTerrainView implements TerrainView
{
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		float px = MathHelper.clamp(levels[SURFACE_ACTUAL_LEVEL] / 256.0f, 0.0f, 1.0f);
		Color color = Color.MAGENTA;
		
		if (levels[SURFACE_ACTUAL_LEVEL] < levels[SEA_LEVEL] - 1 || levels[SURFACE_ACTUAL_LEVEL] < levels[ROCK_LEVEL] - 1)
		{
			Color color1 = geofactors[ELEVATION_GEOFACTOR] < -0.5 ? new Color(0.0f, 0.0f, 0.5f) : new Color(px, px, 1.0f);
			color = levels[ROCK_LEVEL] < levels[SEA_LEVEL] - 1 ? color1.darker().darker() : color1;
		}
		else if (levels[SURFACE_ACTUAL_LEVEL] < levels[SEA_LEVEL])
		{
			Color color1 = new Color(1.0f, 1.0f, px);
			color = levels[ROCK_LEVEL] < levels[SEA_LEVEL] - 1 ? color1.darker().darker() : color1;
		}
		else
		{
			Color color1 = geofactors[ELEVATION_GEOFACTOR] > 0.5 ? new Color(0.8f, 1.0f, 0.8f) : new Color(px, 1.0f, px);
			if (geofactors[VOLCANISM_GEOFACTOR] > 0.5)
			{
				color1 = new Color(1.0f, 0.5f, px);
			}
			else if (geofactors[UPLIFT_GEOFACTOR] < -0.5)
			{
				color1 = new Color(px, 1.0f, 1.0f);
			}
			color = levels[ROCK_LEVEL] < levels[SEA_LEVEL] - 1 ? color1.darker().darker() : color1;
		}
		return color;
	}
}
