package dooglamoo.dooglamooworlds.world.gen;

import java.util.Random;

public class NoiseGeneratorOctaves
{
    /** Collection of noise generation functions.  Output is combined to produce different octaves of noise. */
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;

    public NoiseGeneratorOctaves(Random rand, int octaves)
    {
        this.octaves = octaves;
        generatorCollection = new NoiseGeneratorImproved[octaves];

        for (int i = 0; i < octaves; i++)
        {
            generatorCollection[i] = new NoiseGeneratorImproved(rand);
        }
    }

    /**
     * pars:(par2,3,4=noiseOffset ; so that adjacent noise segments connect) (pars5,6,7=x,y,zArraySize),(pars8,10,12 =
     * x,y,z noiseScale)
     */
    public double[] generateNoiseOctaves(double[] noiseArray, int noiseOffsetX, int noiseOffsetY, int noiseOffsetZ, int xSize, int ySize, int zSize, double noiseScaleX, double noiseScaleY, double noiseScaleZ)
    {
        if (noiseArray == null)
        {
            noiseArray = new double[xSize * ySize * zSize];
        }
        else
        {
            for (int k = 0; k < noiseArray.length; k++)
            {
                noiseArray[k] = 0.0;
            }
        }

        double frequency = 1.0;

        for (int i = 0; i < octaves; i++)
        {
            double xOffset = (double)noiseOffsetX * frequency * noiseScaleX;
            double yOffset = (double)noiseOffsetY * frequency * noiseScaleY;
            double zOffset = (double)noiseOffsetZ * frequency * noiseScaleZ;
            long i2 = floor_double_long(xOffset);
            long j2 = floor_double_long(zOffset);
            xOffset -= (double)i2;
            zOffset -= (double)j2;
            i2 %= 16777216L;
            j2 %= 16777216L;
            xOffset += (double)i2;
            zOffset += (double)j2;
            generatorCollection[i].populateNoiseArray(noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, noiseScaleX * frequency, noiseScaleY * frequency, noiseScaleZ * frequency, frequency);
            frequency /= 2.0;
        }

        return noiseArray;
    }

    /**
     * Bouncer function to the main one with some default arguments.
     * noiseScale: 1 - 2000, default 200
     */
    public double[] generateNoiseOctaves(double[] noiseArray, int noiseOffsetX, int noiseOffsetZ, int xSize, int zSize, double noiseScaleX, double noiseScaleZ, double p_76305_10_)
    {
        return generateNoiseOctaves(noiseArray, noiseOffsetX, 10, noiseOffsetZ, xSize, 1, zSize, noiseScaleX, 1.0, noiseScaleZ);
    }
    
    /**
     * Long version of floor_double
     */
    private static long floor_double_long(double a)
    {
        long i = (long)a;
        return a < (double)i ? i - 1L : i;
    }
}
