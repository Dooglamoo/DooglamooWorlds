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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dooglamoo.dooglamooworlds.world.biome.provider.DooglamooBiomeProvider;
import dooglamoo.dooglamooworlds.world.gen.NoiseGenerator;
import dooglamoo.dooglamooworlds.world.gen.NoiseGeneratorPerlin;
import dooglamoo.dooglamooworlds.world.gen.RiverRegion;

import net.minecraft.util.math.MathHelper;

public class TerrainViewer
{
	private static Image img;
	private static int imgWidth = 1024;
	private static int imgHeight = 1024;
	private static Random rand = new Random();
	private static long seed = rand.nextLong();
	
	// scale: 1.0 to 3.0
	private static final double elevationScale = 2.0;
	private static final double densityScale = 2.0;
	private static final double volcanismScale = 2.0;
	private static final double upliftScale = 2.0;
	private static final double eraScale = 2.0;
	private static final double erosionScale = 2.0;
	private static final double temperatureScale = 2.0;
	private static final double precipitationScale = 2.0;
	
	// amplifier: 0.0 to 2.0
	public static final double elevationAmplitude = 1.0;
	public static final double densityAmplitude = 1.0;
	public static final double upliftAmplitude = 1.0;
	public static final double volcanismAmplitude = 1.0;
	public static final double eraAmplitude = 1.0;
	public static final double erosionAmplitude = 1.0;
	public static final double temperatureAmplitude = 1.0;
	public static final double precipitationAmplitude = 1.0;
	
	// weight: -0.6 to 0.6
	public static final double elevationWeight = 0.0;
	public static final double densityWeight = 0.0;
	public static final double upliftWeight = 0.0;
	public static final double volcanismWeight = 0.0;
	public static final double eraWeight = 0.0;
	public static final double erosionWeight = 0.0;
	public static final double temperatureWeight = 0.0;
	public static final double precipitationWeight = 0.0;
	
	private static int imgScale = 16; /* 1, 8, 16, 32, 64 */
	private static int seaLevel = 63;
	private static double riverWiggleHi = 400.0 * elevationScale;
	private static double riverWiggleLo = riverWiggleHi * 1.3;
	private static final double riverWidthSquared = Math.pow(256, 2) * elevationScale;
	private static final double riverBank = riverWidthSquared * 0.2;
	private static final double riverShore = riverWidthSquared - riverBank;
	private static final double riverChannel = riverBank * 0.2;
	private static final double riverShallow = riverBank - riverChannel;
	
	private static double[] geofactors = new double[9];
	private static int[] levels = new int[6];

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Terrain Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel canvas = new JPanel() {
			@Override
			public void paint(Graphics g)
			{
				g.drawImage(img,  0,  0,  null);
			}
		};
		canvas.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent event)
			{
				
			}

			@Override
			public void mouseMoved(MouseEvent event)
			{
				int x1 = (-(imgWidth / 2) + event.getX()) * imgScale;
				int z1 = (-(imgHeight / 2) + event.getY()) * imgScale;
				frame.setTitle("Terrain Viewer seed:" + seed + " (" + x1 + "," + z1 + ")");
			}
		});
		JScrollPane pane = new JScrollPane(canvas);
		canvas.setPreferredSize(new Dimension(imgWidth, imgHeight));
		frame.getContentPane().add(pane);
		frame.pack();
		frame.setVisible(true);
		img = canvas.createImage(imgWidth, imgHeight);
		System.out.println("running ...");
		frame.setTitle("Terrain Viewer calculating ...");
		run(canvas);
		System.out.println("finished");
		frame.setTitle("Terrain Viewer seed:" + seed);
		canvas.repaint();
	}
	
	private static void run(JPanel canvas)
	{
		if (img == null)
		{
			System.out.println("null image");
			return;
		}
		
		Graphics g = img.getGraphics();
		rand.setSeed(seed);
		
		NoiseGenerator surfaceGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.05 * 4.0);
		NoiseGenerator elevationGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.00016 * (-4.0 * elevationScale + 13.0));
		NoiseGenerator densityGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.0006 * (-4.0 * densityScale + 13.0));
		NoiseGenerator upliftGen = new NoiseGeneratorPerlin(rand, 6).setScale(0.004 * (-4.0 * upliftScale + 13.0));
		NoiseGenerator volcanismGen = new NoiseGeneratorPerlin(rand, 3).setScale(0.0001 * (-4.0 * volcanismScale + 13.0));
		NoiseGenerator eraGen = new NoiseGeneratorPerlin(rand, 4).setScale(0.0005 * (-4.0 * eraScale + 13.0));
		NoiseGenerator erosionGen = new NoiseGeneratorPerlin(rand, 5).setScale(0.006 * (-4.0 * erosionScale + 13.0));
		NoiseGenerator temperatureGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.00016 * (-4.0 * temperatureScale + 13.0));
		NoiseGenerator precipitationGen = new NoiseGeneratorPerlin(rand, 2).setScale(0.00016 * (-4.0 * precipitationScale + 13.0));
		
		TerrainView view = new ElevationTerrainView();
//		TerrainView view = new ClimateTerrainView();
//		TerrainView view = new GeofactorTerrianView(TerrainView.TEMPERATURE_GEOFACTOR);
//		TerrainView view = new HeightMapTerrainView();
//		TerrainView view = new BiomeTerrainView();
//		TerrainView view = new JsonTerrainView("{\"type\": \"biome\", \"temperature\":0, \"precipitation\":0}");
		
		Map<Long, RiverRegion> riverRegionCache = new ConcurrentHashMap<Long, RiverRegion>();
		int riverRegionHalfWidth = (int)(8192 * elevationScale) / 2;
		int riverRegionWidth = riverRegionHalfWidth * 2;
		
		levels[TerrainView.SEA_LEVEL] = seaLevel;
		
		for (int x = 0; x < imgWidth; x++)
		{
			int x1 = (-(imgWidth / 2) + x) * imgScale;
			
			for (int z = 0; z < imgHeight; z++)
			{
				int z1 = (-(imgHeight / 2) + z) * imgScale;
				
				// rivers
				long riverKey = (long)(Math.floorDiv(x1 + riverRegionHalfWidth, riverRegionWidth)) & 4294967295L | ((long)(Math.floorDiv(z1 + riverRegionHalfWidth, riverRegionWidth)) & 4294967295L) << 32;
				RiverRegion riverRegion = riverRegionCache.get(riverKey);
			    if (riverRegion == null)
			    {
			    	int startX = Math.floorDiv(x1 + riverRegionHalfWidth, riverRegionWidth) * riverRegionWidth - riverRegionHalfWidth;
			    	int startZ = Math.floorDiv(z1 + riverRegionHalfWidth, riverRegionWidth) * riverRegionWidth - riverRegionHalfWidth;
			    	riverRegion = new RiverRegion(seed & riverKey, startX, startZ, riverRegionWidth, riverRegionWidth, 40, elevationGen, precipitationGen);
			    	riverRegionCache.put(riverKey, riverRegion);
			    }
				
				double surface = surfaceGen.noise(x1, z1);
				double elevation = elevationGen.noise(x1, z1) * elevationAmplitude + elevationWeight;
				double density = densityGen.noise(x1, z1) * densityAmplitude + densityWeight;
				double uplift = upliftGen.noise(x1, z1) * upliftAmplitude + upliftWeight;
				double volcanism = volcanismGen.noise(x1, z1) * volcanismAmplitude + volcanismWeight;
				double era = eraGen.noise(x1, z1) * eraAmplitude + eraWeight;
				double erosion = erosionGen.noise(x1, z1) * erosionAmplitude + erosionWeight;
				double temperature = temperatureGen.noise(x1, z1) * temperatureAmplitude + MathHelper.clamp(temperatureWeight + z1 * 0.00001875, -0.6, 0.6);
				double precipitation = precipitationGen.noise(x1, z1) * precipitationAmplitude + MathHelper.clamp(precipitationWeight + x1 * 0.00001875, -0.6, 0.6);
				
				geofactors[TerrainView.SURFACE_GEOFACTOR] = surface;
				geofactors[TerrainView.ELEVATION_GEOFACTOR] = elevation;
				geofactors[TerrainView.DENSITY_GEOFACTOR] = density;
				geofactors[TerrainView.UPLIFT_GEOFACTOR] = uplift;
				geofactors[TerrainView.VOLCANISM_GEOFACTOR] = volcanism;
				geofactors[TerrainView.ERA_GEOFACTOR] = era;
				geofactors[TerrainView.EROSION_GEOFACTOR] = erosion;
				geofactors[TerrainView.TEMPERATURE_GEOFACTOR] = temperature;
				geofactors[TerrainView.PRECIPITATION_GEOFACTOR] = precipitation;
				
				boolean dryRiver = false;
				
				// rivers
				if (elevation > -0.2)
				{
					double riverUplift = uplift;
					for (int t = 0; t < riverRegion.rivers.size(); t++)
					{
						RiverRegion.River river = riverRegion.rivers.get(t);
						double d = RiverRegion.distToSegmentSquared(x1, z1,
								river.vHi.x + riverWiggleHi * precipitation,
								river.vHi.z + riverWiggleHi * uplift,
								river.vLo.x + riverWiggleLo * precipitation,
								river.vLo.z + riverWiggleLo * uplift
								) + surface * 6400.0;
						if (d < riverChannel)
						{
							riverUplift = -0.8;
							if (river.dry)
							{
								dryRiver = true;
							}
						}
						else if (d < riverBank)
						{
							riverUplift = Math.min(((d - riverChannel) / riverShallow) * 0.8 - 0.8, riverUplift);
							if (river.dry)
							{
								dryRiver = true;
							}
						}
						else if (d < riverWidthSquared)
						{
							riverUplift = Math.min((d - riverBank) / riverShore, riverUplift);
							if (river.dry)
							{
								dryRiver = true;
							}
						}
					}
					if (elevation < 0.0)
					{
						riverUplift = MathHelper.lerp(-elevation * 5.0, riverUplift, uplift);
					}
					uplift = riverUplift;
				}
				
				double r = surface * 0.1 + (rand.nextDouble() * 0.1 - 0.05);
				
				int code = 0;
				
				if (elevation + surface * 0.008 < -0.5)
				{
					code |= DooglamooBiomeProvider.PLATES_OCEAN;
				}
				else if (elevation + surface * 0.008 < 0.0)
				{
					code |= DooglamooBiomeProvider.PLATES_SHALLOWS;
				}
				else if (elevation + surface * 0.008 < 0.5)
				{
					code |= DooglamooBiomeProvider.PLATES_PLAINS;
				}
				else
				{
					code |= DooglamooBiomeProvider.PLATES_PLATEAU;
				}
				
				if (density + r < -0.5)
				{
					code |= DooglamooBiomeProvider.ROCK_SEDIMENTARY;
				}
				else if (density + r < 0.0)
				{
					code |= DooglamooBiomeProvider.ROCK_SOFT;
				}
				else if (density + r < 0.5)
				{
					code |= DooglamooBiomeProvider.ROCK_HARD;
				}
				else
				{
					code |= DooglamooBiomeProvider.ROCK_IGNEOUS;
				}
				
				if (uplift + r < -0.5)
				{
					code |= DooglamooBiomeProvider.LIFT_SUNK;
				}
				else if (uplift + r < 0.0)
				{
					code |= DooglamooBiomeProvider.LIFT_LEVEL;
				}
				else if (uplift + r < 0.5)
				{
					code |= DooglamooBiomeProvider.LIFT_HILLS;
				}
				else
				{
					code |= DooglamooBiomeProvider.LIFT_MOUNTAINS;
				}
				
				if (volcanism + r < -0.5)
				{
					code |= DooglamooBiomeProvider.THERMAL_NONE;
				}
				else if (volcanism + r < 0.0)
				{
					code |= DooglamooBiomeProvider.THERMAL_SOME;
				}
				else if (volcanism + r < 0.5)
				{
					code |= DooglamooBiomeProvider.THERMAL_MOST;
				}
				else
				{
					code |= DooglamooBiomeProvider.THERMAL_FULL;
				}
				
				if (era + r < -0.5)
				{
					code |= DooglamooBiomeProvider.AGE_YOUNGEST;
				}
				else if (era + r < 0.0)
				{
					code |= DooglamooBiomeProvider.AGE_YOUNG;
				}
				else if (era + r < 0.5)
				{
					code |= DooglamooBiomeProvider.AGE_OLD;
				}
				else
				{
					code |= DooglamooBiomeProvider.AGE_OLDEST;
				}
				
				if (erosion + r < -0.5)
				{
					code |= DooglamooBiomeProvider.EROSION_NONE;
				}
				else if (erosion + r < 0.0)
				{
					code |= DooglamooBiomeProvider.EROSION_SOME;
				}
				else if (erosion + r < 0.5)
				{
					code |= DooglamooBiomeProvider.EROSION_MOST;
				}
				else
				{
					code |= DooglamooBiomeProvider.EROSION_FULL;
				}
				
				if (temperature + r < -0.5)
				{
					code |= DooglamooBiomeProvider.TEMP_COLD;
				}
				else if (temperature + r < 0.0)
				{
					code |= DooglamooBiomeProvider.TEMP_COOL;
				}
				else if (temperature + r < 0.5)
				{
					code |= DooglamooBiomeProvider.TEMP_WARM;
				}
				else
				{
					code |= DooglamooBiomeProvider.TEMP_HOT;
				}
				
				if (precipitation + r < -0.5)
				{
					code |= DooglamooBiomeProvider.PRECIP_DRY;
				}
				else if (precipitation + r < 0.0)
				{
					code |= DooglamooBiomeProvider.PRECIP_MOIST;
				}
				else if (precipitation + r < 0.5)
				{
					code |= DooglamooBiomeProvider.PRECIP_HUMID;
				}
				else
				{
					code |= DooglamooBiomeProvider.PRECIP_WET;
				}
				
				// calculate levels
				double surfaceDepthPlateFactor = Math.max(Math.min((elevation + 0.02) * 25.0, 1.0), 0.08);
				int surfaceDepth = (int)((((surface + 1.0) * (uplift > -0.5 ? uplift + 0.5 : 0.0)) * 6.0 + (era + 0.5) * 12.0) * surfaceDepthPlateFactor);
				if (surfaceDepth < 1)
				{
					surfaceDepth = 1;
				}
				double erosionFactor = surface * 2.0; // to get "tabletop" effect
				if (erosionFactor > 0.0)
				{
					erosionFactor = -erosionFactor;
				}
				if (erosionFactor > -0.1)
				{
					erosionFactor = -0.1;
				}
				erosionFactor = 1.0 + erosionFactor;
				int erosionDepth;
				if (density < -0.5)
				{
					erosionDepth = erosion > 0.0 ? (int)(erosion * 50.0) : 0;
				}
				else if (density < 0.0)
				{
					erosionDepth = erosion > 0.0 ? (int)(erosion * 40.0) : 0;
				}
				else if (density < 0.5)
				{
					erosionDepth = erosion > 0.0 ? (int)(erosion * 32.0) : 0;
				}
				else
				{
					erosionDepth = erosion > 0.0 ? (int)(erosion * 26.0) : 0;
				}
				levels[TerrainView.ROCK_LEVEL] = (int)(((((elevation) + 1.0) / 2.0) * 128.0) + surface * 0.3);
				double upliftFactor = (erosion < 0.0 && uplift > 0.1 && uplift <= 0.6 ? Math.min(Math.max((uplift - 0.1) * (-erosion * 6.0), uplift), 0.6) : uplift) * 128.0;
				levels[TerrainView.UPLIFT_LEVEL] = levels[TerrainView.ROCK_LEVEL] + (int)((uplift > 0.0 ? upliftFactor * Math.min(surfaceDepthPlateFactor + (volcanism > 0.45 ? (volcanism - 0.45) * 4.5 : 0.0), 1.0) : 0.0));
				levels[TerrainView.SURFACE_VIRTUAL_LEVEL] = (levels[TerrainView.UPLIFT_LEVEL] + surfaceDepth);
				levels[TerrainView.SURFACE_ACTUAL_LEVEL] = levels[TerrainView.SURFACE_VIRTUAL_LEVEL];
				if (erosionFactor > 0.0)
				{
					levels[TerrainView.SURFACE_ACTUAL_LEVEL] -= erosionFactor * erosionDepth;
				}
				if (uplift < -0.5)
				{
					levels[TerrainView.SURFACE_ACTUAL_LEVEL] -= (-uplift - 0.5) * 50.0;
				}
				
				levels[TerrainView.MANTLE_LEVEL] = levels[TerrainView.UPLIFT_LEVEL] - (int)(200.0 - (volcanism > 0.0 ? volcanism * 256.0 : 0.0));
				if (levels[TerrainView.MANTLE_LEVEL] > levels[TerrainView.UPLIFT_LEVEL])
				{
					levels[TerrainView.MANTLE_LEVEL] = levels[TerrainView.UPLIFT_LEVEL];
				}
				if (levels[TerrainView.UPLIFT_LEVEL] >= levels[TerrainView.SURFACE_ACTUAL_LEVEL] || uplift >= 0.5)
				{
					levels[TerrainView.UPLIFT_LEVEL] = levels[TerrainView.SURFACE_ACTUAL_LEVEL] - 1;
				}
				// end calculation
				
				g.setColor(view.getColor(x, z, geofactors, levels, code, dryRiver));
				g.fillRect(x, z, 1, 1);
			}
		}
		
		// river lines
//		for (RiverRegion riverRegion : riverRegionCache.values())
//		{
//			for (RiverRegion.River river : riverRegion.rivers)
//			{
//				int xHi = (int)((river.vHi.x + (imgWidth * scale / 2)) / scale);
//				int zHi = (int)((river.vHi.z + (imgHeight * scale / 2)) / scale);
//				int xLo = (int)((river.vLo.x + (imgWidth * scale / 2)) / scale);
//				int zLo = (int)((river.vLo.z + (imgHeight * scale / 2)) / scale);
//				g.setColor(Color.BLACK);
//				g.drawLine(xHi, zHi, xLo, zLo);
//				g.setColor(Color.LIGHT_GRAY);
//				g.fillOval(xHi, zHi, 8, 8);
//				g.setColor(Color.DARK_GRAY);
//				g.fillOval(xLo, zLo, 8, 8);
//			}
//		}
		
		// grid lines
		g.setColor(Color.BLACK);
		int space = 2048 / imgScale;
		for (int x = 0; x < imgWidth; x++)
		{
			if (((-(imgWidth / 2) + x)) % space == 0)
			{
				g.drawLine(x, 0, x, imgHeight - 1);
			}
		}
		for (int z = 0; z < imgHeight; z++)
		{
			if (((-(imgHeight / 2) + z)) % space == 0)
			{
				g.drawLine(0, z, imgWidth - 1, z);
			}
		}
		
		// center dot
		g.setColor(new Color(1.0f, 0.0f, 1.0f));
		g.fillRect((imgWidth / 2) - 4, (imgHeight / 2) - 4, 8, 8);
		
		canvas.repaint();
	}
}
