/*
 * The MIT License
 *
 * Copyright 2017 Thibault Debatty.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.debatty.java.aggregation;

class Lines {

    private StraightLine[] lines;

    static boolean McAllister = false;
    // false = Modificacio meva C & O
    // true  = Alternativa de McAllister i Roulier segons Iqbal

    private static final double INFINITY = Double.MAX_VALUE;

    Lines(final Point[] points) {

        lines = new StraightLine[points.length];

        for (int i = 0; i < points.length; i++) {
            lines[i] = new StraightLine(0, 0);
        }

        // will store the angular coeficient between successive points
        double[] coefs = new double[points.length + 1];
        double[] m = new double[points.length + 1];

        int i = 0;
        int size = points.length;

        for (i = 2; i <= size; i++) {
            coefs[i] = Point.computeCoef(points[i - 2], points[i - 1]);
        }

        for (i = 2; i <= size - 1; i++) {
            m[i] = Point.calculaMi(
                    coefs[i],
                    coefs[i + 1],
                    points[i - 1],
                    points[i - 2],
                    points[i]);
        }

        if (McAllister) {
            computeMcAllister(coefs, m);
        } else {
            compute(coefs, m);
        }

        for (i = 1; i <= size; i++) {
            this.lines[i - 1].a = m[i];
            this.lines[i - 1].b = points[i - 1].y - m[i] * points[i - 1].x;
        }
    }

    public static void setMcAllister() {
        McAllister = true;
    }

    /**
     * Get the line at given position.
     *
     * @param position
     * @return
     */
    public StraightLine get(final int position) {
        return lines[position];
    }

    /**
     * Modify the first and last value in m using McAllister.
     * @param coefs
     * @param m
     */
    private void computeMcAllister(final double[] coefs, final double[] m) {
        if ((coefs[2] * (2 * coefs[2] - m[2])) > 0.0) {
            m[1] = 2 * coefs[2] - m[2];
        } else {
            m[1] = 0.0;
        }

        int size = coefs.length - 1;

        if ((coefs[size] * (2 * coefs[size] - m[size - 1])) > 0.0) {
            m[size] = 2 * coefs[size] - m[size - 1];
        } else {
            m[size] = 0.0;
        }

    }

    /**
     * Modify the first and last value in m using classical method.
     * @param coefs
     * @param m
     */
    private void compute(final double[] coefs, final double[] m) {
        if ((m[2] == 0.0) && (coefs[2] == 0.0)) {
            m[1] = 0.0;
        } else if (m[2] == 0.0) {
            m[1] = INFINITY;
        } else {
            m[1] = coefs[2] * coefs[2] / m[2];
        }

        int size = coefs.length - 1;

        if ((m[size - 1] == 0.0) && (coefs[size] == 0.0)) {
            m[size] = 0.0;
        } else if (m[size - 1] == 0.0) {
            m[size] = INFINITY;
        } else {
            m[size] = coefs[size] * coefs[size] / m[size - 1];
        }
    }
}
