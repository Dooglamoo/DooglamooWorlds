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

public class GeofactorTerrianView implements TerrainView
{
	private int geofactor;
	
	public GeofactorTerrianView(int geofactor)
	{
		this.geofactor = geofactor;
	}
	
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		boolean water = levels[SURFACE_ACTUAL_LEVEL] < levels[SEA_LEVEL] - 1 || levels[SURFACE_ACTUAL_LEVEL] < levels[ROCK_LEVEL] - 1;
		Color color;
		if (geofactors[geofactor] < -0.5)
		{
			color = (water ? Color.BLUE : Color.LIGHT_GRAY).darker().darker().darker();
		}
		else if (geofactors[geofactor] < 0.0)
		{
			color = (water ? Color.BLUE : Color.LIGHT_GRAY).darker().darker();
		}
		else if (geofactors[geofactor] < 0.5)
		{
			color = (water ? Color.BLUE : Color.LIGHT_GRAY).darker();
		}
		else
		{
			color = (water ? Color.BLUE : Color.LIGHT_GRAY);
		}
		return color;
	}
}
