package geometry_objects.delegates;

import geometry_objects.Segment;
import utilities.math.MathUtilities;

/*
 * A processing, delegation class.
 * All functionality here treats any / all given segments AS LINES (infinite notions)
 * 
 */
public class LineDelegate
{
    /*
     * @param that -- another segment
     * @return true / false if the two lines are strictly collinear
     */
    public static boolean areCollinear(Segment thisS, Segment that)
    {
        // If the segments are vertical, just compare the X values of one point of each
        // Also ensure that the segments have some overlap
        if (thisS.isVertical() && that.isVertical())
        {
            return MathUtilities.doubleEquals(thisS.getPoint1().getX(), that.getPoint1().getX());
        }

        // If the segments are horizontal, just compare the Y values of one point of each; this is redundant
        if (thisS.isHorizontal() && that.isHorizontal())
        {
            return MathUtilities.doubleEquals(thisS.getPoint1().getY(), that.getPoint2().getY());
        }

        // Slopes equate
        return MathUtilities.doubleEquals(thisS.slope(), that.slope());
    }
}
