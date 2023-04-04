package geometry_objects.delegates.intersections;

import geometry_objects.delegates.FigureDelegate;
import geometry_objects.points.Point;
import geometry_objects.Segment;


public class IntersectionDelegate extends FigureDelegate
{
    /*
     * <Segment, Segment> Intersection (finite treatment of segments)
     */
    public static Point segmentIntersection(Segment thisS, Segment that)
    {
        Point p = SegmentIntersectionDelegate.findIntersection(thisS, that);

        if(p != null) return p;

        return null;
    }
}
