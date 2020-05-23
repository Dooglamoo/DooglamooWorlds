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

public interface TerrainView
{
	public static final int SURFACE_GEOFACTOR = 0;
	public static final int ELEVATION_GEOFACTOR = 1;
	public static final int DENSITY_GEOFACTOR = 2;
	public static final int UPLIFT_GEOFACTOR = 3;
	public static final int VOLCANISM_GEOFACTOR = 4;
	public static final int ERA_GEOFACTOR = 5;
	public static final int EROSION_GEOFACTOR = 6;
	public static final int TEMPERATURE_GEOFACTOR = 7;
	public static final int PRECIPITATION_GEOFACTOR = 8;
	
	public static final int MANTLE_LEVEL = 0;
	public static final int ROCK_LEVEL = 1;
	public static final int UPLIFT_LEVEL = 2;
	public static final int SURFACE_ACTUAL_LEVEL = 3;
	public static final int SURFACE_VIRTUAL_LEVEL = 4;
	public static final int SEA_LEVEL = 5;
	
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry);
}
