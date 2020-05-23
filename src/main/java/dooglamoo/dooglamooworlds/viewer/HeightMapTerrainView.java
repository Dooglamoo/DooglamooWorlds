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

public class HeightMapTerrainView implements TerrainView
{
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		int surface = levels[SURFACE_ACTUAL_LEVEL];
		int water = surface + (surface < 63 ? 20 : 0);
		surface = MathHelper.clamp(surface, 0, 255);
		water = MathHelper.clamp(water, 0, 255);
		return new Color(surface, surface, water);
	}
}
