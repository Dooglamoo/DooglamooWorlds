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

import static dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProvider.*;

public class ClimateTerrainView implements TerrainView
{
	private static final Color JUNGLE = new Color(31, 167, 99);
	private static final Color DARK_FOREST = new Color(0, 128, 0);
	private static final Color SAVANNA = new Color(157, 157, 0);
	private static final Color DESERT = new Color(233, 203, 112);
	private static final Color SWAMP = new Color(66, 104, 68);
	private static final Color FOREST = new Color(0, 213, 0);
	private static final Color PLAINS = new Color(181, 230, 29);
	private static final Color BADLANDS = new Color(217, 173, 43);
	private static final Color GIANT_TAIGA = new Color(128, 174, 130);
	private static final Color TAIGA = new Color(106, 255, 106);
	private static final Color BIRCH_FOREST = new Color(208, 239, 114);
	private static final Color STONE_SHORE = new Color(167, 141, 92);
	private static final Color FROZEN_RIVER = new Color(195, 195, 195);
	private static final Color SNOWY_TAIGA = new Color(228, 228, 228);
	private static final Color TUNDRA = new Color(255, 255, 255);
	private static final Color VOLCANO = new Color(50, 43, 51);
	private static final Color FROZEN_OCEAN = new Color(154, 79, 255);
	private static final Color DEEP_FROZEN_OCEAN = new Color(121, 21, 255);
	private static final Color COLD_OCEAN = new Color(114, 79, 255);
	private static final Color DEEP_COLD_OCEAN = new Color(68, 21, 255);
	private static final Color OCEAN = new Color(79, 79, 255);
	private static final Color DEEP_OCEAN = new Color(21, 21, 255);
	private static final Color LUKEWARM_OCEAN = new Color(79, 118, 255);
	private static final Color DEEP_LUKEWARM_OCEAN = new Color(21, 74, 255);
	private static final Color WARM_OCEAN = new Color(79, 167, 255);
	private static final Color DEEP_WARM_OCEAN = new Color(21, 138, 255);
	private static final Color RIVER = new Color(0, 53, 132);
	private static final Color BEACH = new Color(105, 78, 14);
	private static final Color SNOWY_BEACH = new Color(251, 247, 230);
	
	
	@Override
	public Color getColor(int x, int z, double[] geofactors, int[] levels, int code, boolean dry)
	{
		Color color = Color.MAGENTA;
		
		if (geofactors[ELEVATION_GEOFACTOR] > 0.0)
		{
			switch (code & TEMP_MASK)
			{
			case TEMP_HOT:
				switch (code & PRECIP_MASK)
				{
				case PRECIP_WET: color = JUNGLE; break;
				case PRECIP_HUMID: color = DARK_FOREST; break;
				case PRECIP_MOIST: color = SAVANNA; break;
				case PRECIP_DRY: color = DESERT; break;
				}
				break;
			case TEMP_WARM:
				switch (code & PRECIP_MASK)
				{
				case PRECIP_WET: color = SWAMP; break;
				case PRECIP_HUMID: color = FOREST; break;
				case PRECIP_MOIST: color = PLAINS; break;
				case PRECIP_DRY: color = BADLANDS; break;
				}
				break;
			case TEMP_COOL:
				switch (code & PRECIP_MASK)
				{
				case PRECIP_WET: color = GIANT_TAIGA; break;
				case PRECIP_HUMID: color = TAIGA; break;
				case PRECIP_MOIST: color = BIRCH_FOREST; break;
				case PRECIP_DRY: color = STONE_SHORE; break;
				}
				break;
			case TEMP_COLD:
				switch (code & PRECIP_MASK)
				{
				case PRECIP_WET: color = FROZEN_RIVER; break;
				case PRECIP_HUMID: color = SNOWY_TAIGA; break;
				case PRECIP_MOIST: color = TUNDRA; break;
				case PRECIP_DRY: color = TUNDRA; break;
				}
				break;
			}
		}
		else if (geofactors[ELEVATION_GEOFACTOR] > -0.5)
		{
			switch (code & TEMP_MASK)
			{
			case TEMP_HOT: color = WARM_OCEAN; break;
			case TEMP_WARM: color = LUKEWARM_OCEAN; break;
			case TEMP_COOL: color = COLD_OCEAN; break;
			case TEMP_COLD: color = FROZEN_OCEAN; break;
			}
			
			if ((code & PRECIP_MASK) == PRECIP_DRY || (code & PRECIP_MASK) == PRECIP_MOIST)
			{
				switch (code & TEMP_MASK)
				{
				case TEMP_WARM:
				case TEMP_COOL: color = OCEAN; break;
				}
			}
		}
		else
		{
			switch (code & TEMP_MASK)
			{
			case TEMP_HOT: color = DEEP_WARM_OCEAN; break;
			case TEMP_WARM: color = DEEP_LUKEWARM_OCEAN; break;
			case TEMP_COOL: color = DEEP_COLD_OCEAN; break;
			case TEMP_COLD: color = DEEP_FROZEN_OCEAN; break;
			}
			
			if ((code & PRECIP_MASK) == PRECIP_DRY || (code & PRECIP_MASK) == PRECIP_MOIST)
			{
				switch (code & TEMP_MASK)
				{
				case TEMP_WARM:
				case TEMP_COOL: color = DEEP_OCEAN; break;
				}
			}
		}
		
		if ((code & THERMAL_MASK) == THERMAL_FULL && ((code & AGE_MASK) == AGE_YOUNGEST || (code & AGE_MASK) == AGE_YOUNG))
		{
			color = VOLCANO;
		}
		
		if (geofactors[ELEVATION_GEOFACTOR] > 0.0 && (levels[SURFACE_ACTUAL_LEVEL] < 63 || levels[SURFACE_ACTUAL_LEVEL] < levels[ROCK_LEVEL] - 1))
		{
			color = RIVER;
		}
		else if (geofactors[ELEVATION_GEOFACTOR] <= 0.0 && levels[SURFACE_ACTUAL_LEVEL] >= 63 - 1)
		{
			if ((code & TEMP_MASK) == TEMP_COLD)
			{
				color = SNOWY_BEACH;
			}
			else
			{
				color = BEACH;
			}
		}
		
		return color;
	}
}
