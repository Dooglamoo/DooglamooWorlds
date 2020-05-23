package dooglamoo.dooglamooworlds.world.gen;

import java.util.Random;

public class NoiseGeneratorImproved
{
    private int[] perm;
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final double[] field_152381_e = new double[] {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] field_152382_f = new double[] {1.0, 1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0};
    private static final double[] field_152383_g = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};
    private static final double[] field_152384_h = new double[] {1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, -1.0, 0.0};
    private static final double[] field_152385_i = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 0.0, 1.0, 0.0, -1.0};

    public NoiseGeneratorImproved()
    {
        this(new Random());
    }

    public NoiseGeneratorImproved(Random rand)
    {
        perm = new int[512];
        xCoord = rand.nextDouble() * 256.0;
        yCoord = rand.nextDouble() * 256.0;
        zCoord = rand.nextDouble() * 256.0;
        int i;

        for (i = 0; i < 256; perm[i] = i++)
        {
            ;
        }

        for (i = 0; i < 256; i++)
        {
            int j = rand.nextInt(256 - i) + i;
            int k = perm[i];
            perm[i] = perm[j];
            perm[j] = k;
            perm[i + 256] = perm[i];
        }
    }

    public final double lerp(double t, double a, double b)
    {
        return a + t * (b - a);
    }

    public final double grad(int hash, double x, double y)
    {
        int h = hash & 15;
        return field_152384_h[h] * x + field_152385_i[h] * y;
    }

    public final double grad(int hash, double x, double y, double z)
    {
        int h = hash & 15;
        return field_152381_e[h] * x + field_152382_f[h] * y + field_152383_g[h] * z;
    }

    /**
     * pars: noiseArray , xOffset , yOffset , zOffset , xSize , ySize , zSize , xScale, yScale , zScale , noiseScale.
     * noiseArray should be xSize*ySize*zSize in size
     */
    public void populateNoiseArray(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale)
    {
        int l;
        int i1;
        double d9;
        double d11;
        int zi;
        double x;
        int floorX;
        int X;
        double u;
        int BA;
        int index;

        // 2D
        if (ySize == 1)
        {
            double d21 = 0.0;
            double d22 = 0.0;
            BA = 0;
            double noiseScaleInverse = 1.0 / noiseScale;

            for (int xi = 0; xi < xSize; xi++)
            {
                d9 = xOffset + (double)xi * xScale + xCoord;
                int floorZ = (int)d9;

                if (d9 < (double)floorZ)
                {
                    --floorZ;
                }

                int k1 = floorZ & 255;
                d9 -= (double)floorZ;
                d11 = d9 * d9 * d9 * (d9 * (d9 * 6.0 - 15.0) + 10.0);

                for (zi = 0; zi < zSize; zi++)
                {
                    x = zOffset + (double)zi * zScale + zCoord;
                    floorX = (int)x;

                    if (x < (double)floorX)
                    {
                        --floorX;
                    }

                    X = floorX & 255;
                    x -= (double)floorX;
                    u = x * x * x * (x * (x * 6.0 - 15.0) + 10.0);
                    l = perm[k1] + 0;
                    int i4 = perm[l] + X;
                    int j4 = perm[k1 + 1] + 0;
                    i1 = perm[j4] + X;
                    d21 = lerp(d11, grad(perm[i4], d9, x), grad(perm[i1], d9 - 1.0, 0.0, x));
                    d22 = lerp(d11, grad(perm[i4 + 1], d9, 0.0, x - 1.0), grad(perm[i1 + 1], d9 - 1.0, 0.0, x - 1.0));
                    double d24 = lerp(u, d21, d22);
                    index = BA++;
                    noiseArray[index] += d24 * noiseScaleInverse;
                }
            }
        }
        // 3D
        else
        {
            l = 0;
            double noiseScaleInverse = 1.0 / noiseScale;
            i1 = -1;
            double d8 = 0.0;
            d9 = 0.0;
            double d10 = 0.0;
            d11 = 0.0;

            for (zi = 0; zi < xSize; zi++)
            {
                x = xOffset + (double)zi * xScale + xCoord;
                floorX = (int)x;

                if (x < (double)floorX)
                {
                    --floorX;
                }

                X = floorX & 255;
                x -= (double)floorX;
                u = x * x * x * (x * (x * 6.0 - 15.0) + 10.0);

                for (int xi = 0; xi < zSize; xi++)
                {
                    double z = zOffset + (double)xi * zScale + zCoord;
                    int floorZ = (int)z;

                    if (z < (double)floorZ)
                    {
                        --floorZ;
                    }

                    int Z = floorZ & 255;
                    z -= (double)floorZ;
                    double w = z * z * z * (z * (z * 6.0 - 15.0) + 10.0);

                    for (int yi = 0; yi < ySize; yi++)
                    {
                        double y = yOffset + (double)yi * yScale + yCoord;
                        int floorY = (int)y;

                        if (y < (double)floorY)
                        {
                            --floorY;
                        }

                        int Y = floorY & 255;
                        y -= (double)floorY;
                        double v = y * y * y * (y * (y * 6.0 - 15.0) + 10.0);

                        if (yi == 0 || Y != i1)
                        {
                            i1 = Y;
                            int A = perm[X] + Y;
                            int AA = perm[A] + Z;
                            int AB = perm[A + 1] + Z;
                            int B = perm[X + 1] + Y;
                            BA = perm[B] + Z;
                            int BB = perm[B + 1] + Z;
                            d8 = lerp(u, grad(perm[AA], x, y, z), grad(perm[BA], x - 1.0, y, z));
                            d9 = lerp(u, grad(perm[AB], x, y - 1.0, z), grad(perm[BB], x - 1.0, y - 1.0, z));
                            d10 = lerp(u, grad(perm[AA + 1], x, y, z - 1.0), grad(perm[BA + 1], x - 1.0, y, z - 1.0));
                            d11 = lerp(u, grad(perm[AB + 1], x, y - 1.0, z - 1.0), grad(perm[BB + 1], x - 1.0, y - 1.0, z - 1.0));
                        }

                        double d18 = lerp(v, d8, d9);
                        double d19 = lerp(v, d10, d11);
                        double d20 = lerp(w, d18, d19);
                        index = l++;
                        noiseArray[index] += d20 * noiseScaleInverse;
                    }
                }
            }
        }
    }
}
