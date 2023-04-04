package utilities.math;

/**
 * Utilities focused on math-based functionality
 * 
 * @author Chris Alvin
 *
 */
public class MathUtilities
{
    public static final double EPSILON_POWER = 6;
    public static final double EPSILON = Math.pow(10, -EPSILON_POWER); // 0.000001

    public static boolean doubleEquals(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }
    
    /**
     * Given a value like 0.123456789
     * we return a double with only the epsilon precision we want:
     * 
     * 0.123456789 within epsilon = 0.000001 is 0.12345 
     * 
     * @param x
     */
    public static double removeLessEpsilon(double x)
    {
    	int value = (int)(x * Math.pow(10, EPSILON_POWER - 1));
    	return value / Math.pow(10, EPSILON_POWER - 1);
    }
}