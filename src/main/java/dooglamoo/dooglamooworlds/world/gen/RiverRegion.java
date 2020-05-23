/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.Vec3d;

public class RiverRegion
{
	private Random rand = new Random();
	public List<River> rivers = new ArrayList<River>();
	private static final HeightComparator HIGHT_COMPARE = new HeightComparator();
	private static final double MIN_LENGTH_SQUARED = Math.pow(256, 2);
	
	public RiverRegion(long seed, int regionStartX, int regionStartZ, int regionWidth, int regionHeight, int samples, NoiseGenerator elevationGen, NoiseGenerator precipitationGen)
	{
		rand.setSeed(seed);
		samples = samples / 2 * 2;
		
		List<Vec3d> points = new ArrayList<Vec3d>();
		
		for (int i = 0; i < samples; i++)
		{
			int x1 = regionStartX + rand.nextInt(regionWidth);
			int z1 = regionStartZ + rand.nextInt(regionHeight);
			double y = elevationGen.noise(x1, z1);
			Vec3d v = new Vec3d(x1, y, z1);
			points.add(v);
		}
		
		Collections.sort(points, HIGHT_COMPARE);
		
		for (int n = 0; n < points.size() / 2; n++)
		{
			Vec3d v1 = points.get(points.size() - 1 - n);
			Vec3d v2 = points.get(n);
			if (v1.y > 0.15)
			{
				rivers.add(new River(v1, v2, precipitationGen.noise(v1.x, v1.z) < -0.5));
			}
		}
		
		for (int n = 1; n < rivers.size(); n ++)
		{
			River r = rivers.get(n);
			for (int m = 0; m < n; m++)
			{
				River c = rivers.get(m);
				// if r crosses c, cut r
				Vec3d p = getLineIntersection(r.vHi.x, r.vHi.z, r.vLo.x, r.vLo.z, c.vHi.x, c.vHi.z, c.vLo.x, c.vLo.z);
				if (p != null)
				{
					r.vLo = elevationGen.noise(p.x, p.z) >= r.vHi.y ? new Vec3d(p.x, 1.0, p.y) : p;
					r.lengthSquared = dist2(r.vHi.x, r.vHi.z, r.vLo.x, r.vLo.z);
					r.dry = c.dry;
				}
			}
		}
		
		// remove rivers that don't end at ocean or another river, and rivers that are too flat or too short.
		for (Iterator<River> it = rivers.iterator(); it.hasNext();)
		{
			River river = it.next();
			if (river.vLo.y > 0.0 || river.vHi.y - river.vLo.y < 0.15 || river.lengthSquared < MIN_LENGTH_SQUARED)
			{
				it.remove();
			}
			else if ((Math.abs(river.vHi.z - regionStartZ) < 512 && Math.abs(river.vLo.z - regionStartZ) < 512)
					|| (Math.abs(river.vHi.z - (regionStartZ + regionHeight)) < 512 && Math.abs(river.vLo.z - (regionStartZ + regionHeight)) < 512)
					|| (Math.abs(river.vHi.x - regionStartX) < 512 && Math.abs(river.vLo.x - regionStartX) < 512)
					|| (Math.abs(river.vHi.x - (regionStartX + regionWidth)) < 512 && Math.abs(river.vLo.x - (regionStartX + regionWidth)) < 512)
					)
			{
				it.remove();
			}
		}
	}
	
	private static final Vec3d getLineIntersection(double p0_x, double p0_y, double p1_x, double p1_y, double p2_x, double p2_y, double p3_x, double p3_y)
	{
		double s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;
		s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;
		s2_y = p3_y - p2_y;

		double s, t;
		s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
		t = (s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

		if (s >= 0.0 && s <= 1.0 && t >= 0.0 && t <= 1.0)
		{
			return new Vec3d(p0_x + (t * s1_x), 0.0, p0_y + (t * s1_y));
		}

		return null;
	}
	
	private static final double sqr(double x)
	{
		return x * x; 
	}
	
	private static final double dist2(double vx, double vz, double wx, double wz)
	{
		return sqr(vx - wx) + sqr(vz - wz);
	}
	
	public static final double distToSegmentSquared(double px, double pz, double vx, double vz, double wx, double wz)
	{
		double l2 = dist2(vx, vz, wx, wz);
		if (l2 == 0.0)
		{
			return dist2(px, pz, vx, vz);
		}
		double t = ((px - vx) * (wx - vx) + (pz - vz) * (wz - vz)) / l2;
		t = Math.max(0, Math.min(1, t));
		return dist2(px, pz, vx + t * (wx - vx), vz + t * (wz - vz));
	}
	
	public static class River
	{
		public Vec3d vHi;
		public Vec3d vLo;
		public double lengthSquared;
		public boolean dry;
		
		public River(Vec3d vHi, Vec3d vLo, boolean dry)
		{
			this.vHi = vHi;
			this.vLo = vLo;
			this.dry = dry;
			lengthSquared = dist2(vHi.x, vHi.z, vLo.x, vLo.z);
		}
		
		@Override
		public String toString()
		{
			return "pHi:" + vHi + " pLo:" + vLo + " length squared:" + lengthSquared;
		}
	}
	
	private static class HeightComparator implements Comparator<Vec3d>
	{
		@Override
		public int compare(Vec3d o1, Vec3d o2)
		{
			if (o1.y < o2.y)
			{
				return -1;
			}
			else if (o1.y > o2.y)
			{
				return 1;
			}
			return 0;
		}
	}
}
