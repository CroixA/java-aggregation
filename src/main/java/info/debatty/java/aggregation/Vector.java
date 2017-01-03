package info.debatty.java.aggregation;

/**
 *
 * @author Thibault Debatty
 */
class Vector {

    private final double[] values;

    Vector(int size) {
        values = new double[size + 1];
    }

    public Vector(double[] values) {
        this.values = new double[values.length + 1];

        // values[0..] are copied to this.values[1..] :-(
        for (int i = 1; i <= values.length; i++) {
            this.values[i] = values[i-1];
        }
    }

    Vector(Vector a) {
        values = new double[a.values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = a.values[i];
        }
    }

    public double get(int position) {
        return values[position];
    }

    public void set(int position, double value) {
        values[position] = value;
    }


    public wwLdf setQ(int num_values) {
        // de vLp se'n necessita un punt de mes perque es va a llo+1
        Point[] vLp = new Point[num_values + 2];
        wwLdf df = new wwLdf(num_values + 1); /* es lo que se devuelve */

        int i;
        double tempx, llo;

        llo = num_values;
        vLp[1] = new Point(0.0, 0.0);
        for (i = 2; i <= llo + 1; i++) {
            tempx = i - 1;
            vLp[i] = new Point(tempx / llo, values[i - 1] + vLp[i - 1].y);
        }
        // df.initLdf(num_values);
        df.ferQ(vLp, num_values);
        return (df);
    }

    /**
     * Order the vector in place. Make a copy if you wish to keep the original
     * vector.
     * @param num_values
     */
    public void sort() {

        for (int i = 1; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] > values[i]) {
                    double temp = values[j];
                    values[j] = values[i];
                    values[i] = temp;
                }
            }
        }
    }



    /**
     * Sort both vectors according to the values in this vector.
     * @param other
     */
    public void sort(final Vector other) {


        for (int i = 1; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] > values[i]) {

                    double temp = other.values[j];
                    other.values[j] = other.values[i];
                    other.values[i] = temp;

                    double temp2 = values[j];
                    values[j] = values[i];
                    values[i] = temp2;
                }
            }
        }
    }



    /**
     * Compute dot product of vectors.
     * @param other
     * @return
     */
    public double dotProduct(final Vector other) {
        double agg = 0;

        for (int i = 0; i < values.length; i++) {
            agg += other.values[i] * values[i];

        }
        return agg;
    }
}
