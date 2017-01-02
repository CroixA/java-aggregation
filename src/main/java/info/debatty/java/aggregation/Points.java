
package info.debatty.java.aggregation;

/**
 * Represents an array of points.
 * @author Thibault Debatty
 */
class Points {
  Point[] punti;
  int  lon;

  Points(int LARRAY) {
    lon = LARRAY;
    punti = new Point[LARRAY + 1];
    for (int i = 0; i <= LARRAY; i++) {
        punti[i] = new Point(0.0, 0.0);
    }
  }

}
