package input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import geometry_objects.Segment;
import input.builder.DefaultBuilder;
import input.builder.GeometryBuilder;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.segment.SegmentNode;
import input.parser.JSONParser;
import utilities.io.FileUtilities;

public class InputFacade
{
	/**
	 * A utility method to acquire a figure from the given JSON file:
	 *     Constructs a parser
	 *     Acquries an input file string.
	 *     Parses the file.
	 *
	 * @param filepath -- the path/name defining the input file
	 * @return a FigureNode object corresponding to the input file.
	 */
	public static FigureNode extractFigure(String filepath)
	{
        JSONParser parser = new JSONParser(new GeometryBuilder());
        String str = FileUtilities.readFileFilterComments(filepath);
        return (FigureNode) parser.parse(str);
	}

	/**
	 * 1) Convert the PointNode and SegmentNode objects to a Point and Segment objects 
	 *    (those classes have more meaningful, geometric functionality).
	 * 2) Return the points and segments as a pair.
	 *
	 * @param fig -- a populated FigureNode object corresponding to a geometry figure
	 * @return a point database and a set of segments
	 */
	public static Map.Entry<PointDatabase, Set<Segment>> toGeometryRepresentation(FigureNode fig)
	{
		PointDatabase pointDatabase = new PointDatabase();
		LinkedHashSet<Segment> segments = new LinkedHashSet<Segment>();
		
		for(PointNode pointNode : fig.getPointsDatabase().getPoints())
		{
			pointDatabase.put(pointNode.getName(), pointNode.getX(), pointNode.getY());
		}
		
		for(SegmentNode segmentNode : fig.getSegments().asUniqueSegmentList())
		{
			segments.add(toSegment(segmentNode));
		}
		
		return new AbstractMap.SimpleEntry<PointDatabase, Set<Segment>>(pointDatabase, segments);
	}
	
	//private static void createPointDatabase(PointDatabase db, FigureNode fn)
	//{
	//	for(pointNode pointNode : fn.getPointsDatabase()) 
	//	{
	//		Point point = toPoint(pointNode);
	//		
	//		db.put(point.getName(), point.getX(), point.getY());
	//	}
	//}
	//
	//private static void createSegmentSet(Set<Segment> segSet, FigureNode fn)
	//{
	//	for (SegmentNode segmentNode : fn.getSegments().asSegmentList()) 
	//	{
	//		Segment segment = toSegment(segmentNode);
	//		segSet.add(segment);
	//	}
	//}

	private static Point toPoint(PointNode input) 
	{
	    double x = input.getX();
	    double y = input.getY();
	    return new Point(x, y);
	}
	
	private static Segment toSegment(SegmentNode input) 
	{
	    Point p1 = new Point(input.getPoint1().getX(), input.getPoint1().getY());
	    Point p2 = new Point(input.getPoint2().getX(), input.getPoint2().getY());
	    
	    return new Segment(p1, p2);
	}
}
