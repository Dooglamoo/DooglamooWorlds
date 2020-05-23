package dooglamoo.dooglamooworlds.world.gen;

import java.util.Random;

public class NoiseGeneratorSimplex implements NoiseGenerator
{
    private static int[][] grad3 = new int[][] {{1, 1, 0}, { -1, 1, 0}, {1, -1, 0}, { -1, -1, 0}, {1, 0, 1}, { -1, 0, 1}, {1, 0, -1}, { -1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    public static final double SQRT3 = Math.sqrt(3.0);
    private int[] perm;
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final double F2 = 0.5 * (SQRT3 - 1.0);
    private static final double G2 = (3.0 - SQRT3) / 6.0;

    public NoiseGeneratorSimplex()
    {
        this(new Random());
    }

    public NoiseGeneratorSimplex(Random rand)
    {
        perm = new int[512];
        xCoord = 0.0;
        yCoord = 0.0;
        zCoord = 0.0;
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

    private static int fastFloor(double x)
    {
        return x > 0.0 ? (int)x : (int)x - 1;
    }

    private static double dot(int[] grad, double x, double y)
    {
        return (double)grad[0] * x + (double)grad[1] * y;
    }

    // 2D simplex noise
    @Override
    public double noise(double x, double y)
    {
        double _F2 = 0.5 * (SQRT3 - 1.0);
        double s = (x + y) * _F2;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        double _G2 = (3.0 - SQRT3) / 6.0;
        double t = (double)(i + j) * _G2;
        double X0 = (double)i - t;
        double Y0 = (double)j - t;
        double x0 = x - X0;
        double y0 = y - Y0;
        byte i1;
        byte j1;

        if (x0 > y0)
        {
            i1 = 1;
            j1 = 0;
        }
        else
        {
            i1 = 0;
            j1 = 1;
        }

        double x1 = x0 - (double)i1 + _G2;
        double y1 = y0 - (double)j1 + _G2;
        double x2 = x0 - 1.0 + 2.0 * _G2;
        double y2 = y0 - 1.0 + 2.0 * _G2;
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = perm[ii + perm[jj]] % 12;
        int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
        int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        double n0;

        if (t0 < 0.0)
        {
            n0 = 0.0;
        }
        else
        {
            t0 *= t0;
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
        }

        double t1 = 0.5 - x1 * x1 - y1 * y1;
        double n1;

        if (t1 < 0.0)
        {
            n1 = 0.0;
        }
        else
        {
            t1 *= t1;
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
        }

        double t2 = 0.5 - x2 * x2 - y2 * y2;
        double n2;

        if (t2 < 0.0)
        {
            n2 = 0.0;
        }
        else
        {
            t2 *= t2;
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
        }

        return 70.0 * (n0 + n1 + n2);
    }

    // 2D simplex noise
    public void noise(double[] noiseArray, double xIn, double yIn, int xSize, int ySize, double xScale, double yScale, double noiseScale)
    {
        int k = 0;

        for (int iy = 0; iy < ySize; iy++)
        {
            double y = (yIn + (double)iy) * yScale + yCoord;

            for (int ix = 0; ix < xSize; ix++)
            {
                double x = (xIn + (double)ix) * xScale + xCoord;
                double s = (x + y) * F2;
                int i = fastFloor(x + s);
                int j = fastFloor(y + s);
                double t = (double)(i + j) * G2;
                double X0 = (double)i - t;
                double Y0 = (double)j - t;
                double x0 = x - X0;
                double y0 = y - Y0;
                byte i1;
                byte j1;

                if (x0 > y0)
                {
                    i1 = 1;
                    j1 = 0;
                }
                else
                {
                    i1 = 0;
                    j1 = 1;
                }

                double x1 = x0 - (double)i1 + G2;
                double y1 = y0 - (double)j1 + G2;
                double x2 = x0 - 1.0 + 2.0 * G2;
                double y2 = y0 - 1.0 + 2.0 * G2;
                int ii = i & 255;
                int jj = j & 255;
                int gi0 = perm[ii + perm[jj]] % 12;
                int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
                int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
                double t0 = 0.5 - x0 * x0 - y0 * y0;
                double n0;

                if (t0 < 0.0)
                {
                    n0 = 0.0;
                }
                else
                {
                    t0 *= t0;
                    n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
                }

                double t1 = 0.5 - x1 * x1 - y1 * y1;
                double n1;

                if (t1 < 0.0)
                {
                    n1 = 0.0;
                }
                else
                {
                    t1 *= t1;
                    n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
                }

                double t2 = 0.5 - x2 * x2 - y2 * y2;
                double n2;

                if (t2 < 0.0)
                {
                    n2 = 0.0;
                }
                else
                {
                    t2 *= t2;
                    n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
                }

                int i3 = k++;
                noiseArray[i3] += 70.0 * (n0 + n1 + n2) * noiseScale;
            }
        }
    }
}
