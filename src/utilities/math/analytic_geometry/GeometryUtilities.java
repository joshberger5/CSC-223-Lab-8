package utilities.math.analytic_geometry;

import geometry_objects.points.Point;
import utilities.math.MathUtilities;

public class GeometryUtilities
{
    /**
     * Calculates the distance between 2 points
     * @param p1 Point 1
     * @param p2 Point 2
     * @return The distance between points
     */
    public static double distance(Point p1, Point p2)
    {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) +
                         Math.pow(p2.getY() - p1.getY(), 2));
    }
    
	/*
	 * @param M -- a point
	 * @param A -- a point
	 * @param B -- a point
	 * @return true if the three points are (1) collinear and (2) M is between A and B
	 *                                     A-------------M---------B
	 * Note: returns true if M is one of the endpoints
	 */
	public static boolean between(Point M, Point A, Point B)
	{
		return MathUtilities.doubleEquals(GeometryUtilities.distance(A, M) +
                                          GeometryUtilities.distance(M, B),
                                          GeometryUtilities.distance(A, B));
	}
    
    /*
     * @param A -- a point
     * @param B -- a point
     * @return the slope between these two points
     *           IF:
     *             * points are same throw exception
     *             * points are vertical throw exception
     *           ELSE: slope
     */
    public static double slope(Point A, Point B) throws ArithmeticException
    {
    	if (MathUtilities.doubleEquals(A.getX(), B.getX())) throw new ArithmeticException("Vertical line");
    	
    	return (A.getY() - B.getY()) / (A.getX() - B.getX());
    }
}