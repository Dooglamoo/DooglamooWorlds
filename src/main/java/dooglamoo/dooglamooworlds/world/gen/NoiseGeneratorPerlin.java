package dooglamoo.dooglamooworlds.world.gen;

import java.util.Random;

public class NoiseGeneratorPerlin implements NoiseGenerator
{
    private NoiseGeneratorSimplex[] generatorCollection;
    private int octaves;
    private double scale = 1.0;
    private double factor = 1.0;

    public NoiseGeneratorPerlin(Random rand, int octaves)
    {
        this.octaves = octaves;
        this.factor = Math.pow(2.0, octaves) - 1.0;
        generatorCollection = new NoiseGeneratorSimplex[octaves];

        for (int i = 0; i < octaves; i++)
        {
            generatorCollection[i] = new NoiseGeneratorSimplex(rand);
        }
    }
    
    public NoiseGenerator setScale(double scale)
    {
    	this.scale = scale;
    	return this;
    }

    @Override
    public double noise(double x, double y)
    {
        double total = 0.0;
        double frequency = 1.0;
        
        x *= scale;
        y *= scale;

        for (int i = 0; i < octaves; i++)
        {
            total += generatorCollection[i].noise(x * frequency, y * frequency) / frequency;
            frequency /= 2.0;
        }

        return total / factor;
    }

    public double[] noise(double[] noiseArray, double x, double y, int xSize, int ySize, double xScale, double yScale, double coordScale)
    {
        return noise(noiseArray, x, y, xSize, ySize, xScale, yScale, coordScale, 0.5);
    }

    public double[] noise(double[] noiseArray, double x, double y, int xSize, int ySize, double xScale, double yScale, double coordScale, double noiseScale)
    {
        if (noiseArray != null && noiseArray.length >= xSize * ySize)
        {
            for (int k = 0; k < noiseArray.length; k++)
            {
                noiseArray[k] = 0.0;
            }
        }
        else
        {
            noiseArray = new double[xSize * ySize];
        }

        double d7 = 1.0;
        double d6 = 1.0;

        for (int i = 0; i < octaves; i++)
        {
            generatorCollection[i].noise(noiseArray, x, y, xSize, ySize, xScale * d6 * d7, yScale * d6 * d7, 0.55 / d7);
            d6 *= coordScale;
            d7 *= noiseScale;
        }

        return noiseArray;
    }
}
