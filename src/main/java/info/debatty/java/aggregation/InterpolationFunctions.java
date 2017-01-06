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

/**
 * Represents a set of points with interpolation functions between these points.
 * @author Thibault Debatty
 */
class InterpolationFunctions {

    private static final double INFINITY = Double.MAX_VALUE;

    private final Point[] points;
    private final InterpolationFunction[] functions;

    InterpolationFunctions(final Point[] points) {

        int size = points.length;

        // Make a copy of points
        // this.points starts at 1... :(
        this.points = new Point[size + 1];
        this.points[0] = new Point();
        for (int i = 1; i < size; i++) {
            this.points[i] = new Point(points[i - 1]);
        }
        this.points[size] = new Point(points[size - 1]);


        // Compute the tangent line corresponding to each point
        // compute the angular coeficient between successive points
        double[] point_coefs = new double[points.length + 1];
        for (int i = 2; i <= size; i++) {
            point_coefs[i] = Point.computeCoef(points[i - 2], points[i - 1]);
        }

        // compute the angular coef of each tangent line
        double[] line_coefs = new double[points.length + 1];
        for (int i = 2; i <= size - 1; i++) {
            line_coefs[i] = Point.computeLineCoef(
                    point_coefs[i],
                    point_coefs[i + 1],
                    points[i - 1],
                    points[i - 2],
                    points[i]);
        }

        // Modify the first and last value in line_coefs
        if ((line_coefs[2] == 0.0) && (point_coefs[2] == 0.0)) {
            line_coefs[1] = 0.0;
        } else if (line_coefs[2] == 0.0) {
            line_coefs[1] = INFINITY;
        } else {
            line_coefs[1] = point_coefs[2] * point_coefs[2] / line_coefs[2];
        }

        if ((line_coefs[size - 1] == 0.0) && (point_coefs[size] == 0.0)) {
            line_coefs[size] = 0.0;
        } else if (line_coefs[size - 1] == 0.0) {
            line_coefs[size] = INFINITY;
        } else {
            line_coefs[size] = point_coefs[size] * point_coefs[size] / line_coefs[size - 1];
        }

        // Create the actual lines
        Line[] lines = new Line[points.length];
        for (int i = 1; i <= size; i++) {
            lines[i - 1] = new Line(
                    line_coefs[i],
                    points[i - 1].y - line_coefs[i] * points[i - 1].x);
        }

        // Compute the interpolation functions between each pair of points,
        // using the tangent lines computed above.
        // this.functions is 1 based :(
        this.functions = new InterpolationFunction[size + 1];
        this.functions[0] = new InterpolationFunction();
        for (int i = 1; i < size; i++) {

            this.functions[i] = new InterpolationFunction(
                    lines[i - 1],
                    lines[i],
                    points[i - 1],
                    points[i]);
        }
        this.functions[size] = new InterpolationFunction();
    }

    public double eval(final double value, final int num_values) {

        for (int i = 1; i <= num_values; i++) {
            if ((points[i].x <= value) && (value <= points[i + 1].x)) {
                return functions[i].eval(value);
            }
        }

        if (value <= points[1].x) {
            return 0.0;
        }

        if (points[num_values + 1].x <= value) {
            return 1.0;
        }

        throw  new ArithmeticException("Value not found!");
    }
}
