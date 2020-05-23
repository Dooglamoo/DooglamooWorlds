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

public class BiomeTerrainView implements TerrainView
{
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		int red = ((code >> 11) & 0x1F) << (geofactors[ELEVATION_GEOFACTOR] < 0.0 ? 2 : 3);
		int green = ((code >> 5) & 0x2F) << (geofactors[ELEVATION_GEOFACTOR] < 0.0 ? 1 : 2);
		int blue = (code & 0x1F) << (geofactors[ELEVATION_GEOFACTOR] < 0.0 ? 2 : 3);
		return new Color(red, green, blue);
	}
}
