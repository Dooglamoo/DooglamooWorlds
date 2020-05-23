/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen;

public class GeoData
{
	public double[][] surface = new double[16][16];
	public double[][] elevation = new double[16][16];
	public double[][] density = new double[16][16];
	public double[][] uplift = new double[16][16];
	public double[][] volcanism = new double[16][16];
	public double[][] era = new double[16][16];
	public double[][] erosion = new double[16][16];
	public double[][] temperature = new double[16][16];
	public double[][] precipitation = new double[16][16];
	
	public int[][] code = new int[16][16];
	
	public int[][] mantleLevel = new int[16][16];
	public int[][] rockLevel = new int[16][16];
	public int[][] upliftLevel = new int[16][16];
	public int[][] surfaceActualLevel = new int[16][16];
	public int[][] surfaceVirtualLevel = new int[16][16];
}
