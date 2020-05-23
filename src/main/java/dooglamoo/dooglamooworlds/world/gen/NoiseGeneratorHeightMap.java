/*******************************************************************************
 * Copyright 2014-2020 Dooglamoo
 * 
 * Licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License, (the "License");
 * you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 * 
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 ******************************************************************************/
package dooglamoo.dooglamooworlds.world.gen;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import net.minecraft.util.math.Vec3d;

public class NoiseGeneratorHeightMap implements NoiseGenerator
{
	private byte[][] imgData;
	private double imgWidth;
	private double imgHeight;
	private double maxX;
	private double maxY;
	private double centerX;
	private double centerY;
	private double scale = 1.0;
	private NoiseGenerator noise;
	private double noiseFactor;
	private double noiseFactorInverse;

	public NoiseGeneratorHeightMap(Path file, NoiseGenerator noise, double noiseFactor)
    {
    	this.noise = noise;
    	if (noiseFactor < 0.0)
    	{
    		noiseFactor = 0.0;
    	}
    	else if (noiseFactor > 1.0)
    	{
    		noiseFactor = 1.0;
    	}
    	this.noiseFactor = noiseFactor;
    	this.noiseFactorInverse = 1.0 - noiseFactor;
    	
    	if (file != null && Files.exists(file))
    	{
        	try
        	{
        		BufferedImage img = ImageIO.read(file.toFile());
        		imgData = new byte[img.getWidth()][img.getHeight()];
        		for (int w = 0; w < img.getWidth(); w++)
        		{
        			for (int h = 0; h < img.getHeight(); h++)
        			{
        				imgData[w][h] = (byte)img.getRGB(w, h);
        			}
        		}
        		imgWidth = img.getWidth();
        		imgHeight = img.getHeight();
        		maxX = imgWidth - 1;
        		maxY = imgHeight - 1;
        		centerX = img.getWidth() / 2.0;
        		centerY = img.getHeight() / 2.0;
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        	}
    	}
    }
    
    public NoiseGeneratorHeightMap setScale(double scale)
    {
    	this.scale = scale;
    	this.imgWidth /= scale;
    	this.imgHeight /= scale;
    	this.maxX = this.imgWidth - 1;
    	this.maxY = this.imgHeight - 1;
    	return this;
    }
    
    public NoiseGeneratorHeightMap setCenter(Vec3d pos)
    {
    	if (pos != null)
    	{
	    	this.centerX = pos.x;
	    	this.centerY = pos.z;
    	}
    	return this;
    }

    @Override
    public double noise(double x, double y)
    {
    	x += (centerX / scale);
    	y += (centerY / scale);
    	if (imgData == null || x < 0.0 || x >= imgWidth || y < 0.0 || y >= imgHeight)
    	{
    		return 0.0;
    	}
    	
    	double n = noise != null ? noise.noise(x, y) * noiseFactor : 0.0;
    	
    	x *= scale;
    	y *= scale;
    	
    	double xfyf = imgData[(int)Math.floor(x)][(int)Math.floor(y)] & 0xFF;
    	double xcyf = imgData[(int)Math.min(Math.ceil(x), maxX)][(int)Math.floor(y)] & 0xFF;
    	double xcyc = imgData[(int)Math.min(Math.ceil(x), maxX)][(int)Math.min(Math.ceil(y), maxY)] & 0xFF;
    	double xfyc = imgData[(int)Math.floor(x)][(int)Math.min(Math.ceil(y), maxY)] & 0xFF;
    	
    	double xt = lerp(xfyf, xcyf, x - Math.floor(x));
    	double xb = lerp(xfyc, xcyc, x - Math.floor(x));
    	double ym = lerp(xt, xb, y - Math.floor(y));
    	
    	ym = ym / 127.0;
    	
    	return noise != null ? (ym - 1.0) * noiseFactorInverse + n : ym - 1.0;
    }
    
    private final double lerp(double v0, double v1, double t)
	{
		return (1.0 - t) * v0 + t * v1;
	}
}
