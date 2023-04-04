package geometry_objects.delegates;

import geometry_objects.points.Point;
import geometry_objects.Segment;
import utilities.math.analytic_geometry.GeometryUtilities;

/*
 * A processing, delegation class.
 * All functionality here treats any / all given segments as finite segments
 * 
 * @author C. Alvin
 */
public class SegmentDelegate extends FigureDelegate
{
	/*
	 * @param thisS -- a segment
	 * @param that -- a segment
	 * Do these lines cross in the middle of each segment?
	 */
	public static boolean middleCrosses(Segment thisS, Segment that)
	{
		Point intersection = thisS.segmentIntersection(that);
		
		if (intersection == null) return false;

		return thisS.pointLiesBetweenEndpoints(intersection) &&
			   that.pointLiesBetweenEndpoints(intersection);
	}

	/*
	 *                     \  /
	 *     |                \/
	 *     |        or       \
	 *   ---------
	 *   1 angle 
     *
     * The segment-based intersection point will:
     *     * fall on an endpoint of one segment
     *     * fall in the middle of the other segment
     *     
	 * @param thisS -- a segment
	 * @param that -- a segment
	 */
	public static boolean standingOn(Segment thisS, Segment that)
	{
		Point intersection = thisS.segmentIntersection(that);
		
		if (intersection == null) return false;
		
		if (thisS.has(intersection) && that.pointLiesBetweenEndpoints(intersection)) return true;
		if (that.has(intersection) && thisS.pointLiesBetweenEndpoints(intersection)) return true;

		return false;
	}

	/*
	 * @param pt -- a point
	 * @return true / false if this segment (finite) contains the point
	 */
	public static boolean pointLiesOnSegment(Segment segment, Point pt)
	{
		if (pt == null) return false;

		return GeometryUtilities.between(pt, segment.getPoint1(), segment.getPoint2());
	}

	/*
	 * @param pt -- a point
	 * @return true if the point is on the segment (EXcluding endpoints); finite examination only
	 */
	public static boolean pointLiesBetweenEndpoints(Segment segment, Point pt)
	{
		if (pt == null || segment.has(pt)) return false;

		return GeometryUtilities.between(pt, segment.getPoint1(), segment.getPoint2());
	}
}
