package geometry_objects.delegates.intersections;

import geometry_objects.points.Point;
import geometry_objects.Segment;
import utilities.OutTriple;

/*
 * This class treats all lines as infinite (in both directions)
 */
public class LineIntersectionDelegate
{
    /*
     * <Line, Line> intersection
     * @param thisS -- (this Segment)
     * @param that -- a Segment to intersect with
     * @return the point at which these lines (infinite) intersection; null for coinciding lines
     */
    public static Point intersection(Segment thisS, Segment that)
    {
        // Special Case: Collinear, but non-overlapping.
        if (thisS.coincideWithoutOverlap(that)) return null;

        // Special Case: Intersect at an endpoint
        Point shared = thisS.sharedVertex(that);
        if (shared != null) return shared;

        double a, b, c, d, e, f;
        final String name = "";

        if (thisS.isVertical() && that.isHorizontal()) return new Point(name, thisS.getPoint1().getX(), that.getPoint1().getY());

        if (that.isVertical() && thisS.isHorizontal()) return new Point(name, that.getPoint1().getX(), thisS.getPoint1().getY());

        OutTriple<Double, Double, Double> out = new OutTriple<Double, Double, Double>(); 
        if (thisS.isVertical())
        {
            makeLine(that.getPoint1().getX(), that.getPoint1().getY(), that.getPoint2().getX(), that.getPoint2().getY(), out);// a, out b, out e);
            a = out.getFirst();
            b = out.getSecond();
            e = out.getThird();
            return new Point(name, thisS.getPoint1().getX(), evaluateYGivenX(a, b, e, thisS.getPoint1().getX()));
        }
        if (that.isVertical())
        {
            makeLine(thisS.getPoint1().getX(), thisS.getPoint1().getY(), thisS.getPoint2().getX(), thisS.getPoint2().getY(), out);
            a = out.getFirst();
            b = out.getSecond();
            e = out.getThird();
            return new Point(name, that.getPoint1().getX(), evaluateYGivenX(a, b, e, that.getPoint1().getX()));
        }
        if (thisS.isHorizontal())
        {
            makeLine(that.getPoint1().getX(), that.getPoint1().getY(), that.getPoint2().getX(), that.getPoint2().getY(), out);
            a = out.getFirst();
            b = out.getSecond();
            e = out.getThird();

            return new Point(name, evaluateXGivenY(a, b, e, thisS.getPoint1().getY()), thisS.getPoint1().getY());
        }
        if (that.isHorizontal())
        {
            makeLine(thisS.getPoint1().getX(), thisS.getPoint1().getY(), thisS.getPoint2().getX(), thisS.getPoint2().getY(), out);
            a = out.getFirst();
            b = out.getSecond();
            e = out.getThird();

            return new Point(name, evaluateXGivenY(a, b, e, that.getPoint1().getY()), that.getPoint1().getY());
        }

        //
        // ax + by = e
        // cx + dy = f
        // 

        makeLine(thisS.getPoint1().getX(), thisS.getPoint1().getY(), thisS.getPoint2().getX(), thisS.getPoint2().getY(), out);
        a = out.getFirst();
        b = out.getSecond();
        e = out.getThird();

        makeLine(that.getPoint1().getX(), that.getPoint1().getY(), that.getPoint2().getX(), that.getPoint2().getY(), out);
        c = out.getFirst();
        d = out.getSecond();
        f = out.getThird();

        
        double overallDeterminant = a * d - b * c;
        double x = determinant(e, b, f, d) / overallDeterminant;
        double y = determinant(a, e, c, f) / overallDeterminant;

        return new Point(name, x, y);
    }
    
    //
    // Determine the intersection point of the two segments
    //
    //
    // | a b |
    // | c d |
    //
    private static double determinant(double a, double b, double c, double d)
    {
        return a * d - b * c;
    }

    private static void makeLine(double x_1, double y_1, double x_2, double y_2, OutTriple<Double, Double, Double> out)
    {
        double slope = (y_2 - y_1) / (x_2 - x_1);
        double a = - slope;
        double b = 1;
        double c = y_2 - slope * x_2;

        out.set(a, b, c);
    }

    private static double evaluateYGivenX(double a, double b, double e, double x)
    {
        // ax + by = e
        return (e - a * x) / b;
    }

    private static double evaluateXGivenY(double a, double b, double e, double y)
    {
        // ax + by = e
        return (e - b * y) / a;
    }
}
