package preprocessor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import preprocessor.delegates.ImplicitPointPreprocessor;
import utilities.math.MathUtilities;
import geometry_objects.Segment;

public class Preprocessor
{
	// The explicit points provided to us by the user.
	// This database will also be modified to include the implicit
	// points (i.e., all points in the figure).
	protected PointDatabase _pointDatabase;

	// Minimal ('Base') segments provided by the user
	protected Set<Segment> _givenSegments;

	// The set of implicitly defined points caused by segments
	// at implicit points.
	protected Set<Point> _implicitPoints;

	// The set of implicitly defined segments resulting from implicit points.
	protected Set<Segment> _implicitSegments;

	// Given all explicit and implicit points, we have a set of
	// segments that contain no other subsegments; these are minimal ('base') segments
	// That is, minimal segments uniquely define the figure.
	protected Set<Segment> _allMinimalSegments;

	// A collection of non-basic segments
	protected Set<Segment> _nonMinimalSegments;

	// A collection of all possible segments: maximal, minimal, and everything in between
	// For lookup capability, we use a map; each <key, value> has the same segment object
	// That is, key == value. 
	protected Map<Segment, Segment> _segmentDatabase;
	public Map<Segment, Segment> getAllSegments() { return _segmentDatabase; }

	public Preprocessor(PointDatabase points, Set<Segment> segments)
	{
		_pointDatabase  = points;
		_givenSegments = segments;
		
		_segmentDatabase = new HashMap<Segment, Segment>();
		
		_implicitPoints = new LinkedHashSet<Point>();
		_implicitSegments = new LinkedHashSet<Segment>();
		_allMinimalSegments = new LinkedHashSet<Segment>();
		_nonMinimalSegments = new LinkedHashSet<Segment>();
		
		analyze();
	}

	/**
	 * Invoke the precomputation procedure.
	 */
	public void analyze()
	{
		// Implicit Points
		_implicitPoints = ImplicitPointPreprocessor.compute(_pointDatabase, _givenSegments.stream().toList());

		// Implicit Segments attributed to implicit points
		_implicitSegments = computeImplicitBaseSegments(_implicitPoints);

		// Combine the given minimal segments and implicit segments into a true set of minimal segments
		//     *givenSegments may not be minimal
		//     * implicitSegmen
		_allMinimalSegments = identifyAllMinimalSegments(_implicitPoints, _givenSegments, _implicitSegments);

		// Construct all segments inductively from the base segments
		_nonMinimalSegments = constructAllNonMinimalSegments(_allMinimalSegments);

		// Combine minimal and non-minimal into one package: our database
		_allMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
		_nonMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
	}

	/**
	 * gets all the point from a set that are intersection points for a specified segment
	 * @param segment
	 * @param implicitPoints
	 * @return the midpoints
	 */
	private Set<Point> getIntersectionPoints(Segment segment, Set<Point> implicitPoints) {
		Set<Point> midPoints = new LinkedHashSet<Point>();
		for(Point point : implicitPoints) {
			if(segment.pointLiesBetweenEndpoints(point)) {
				midPoints.add(point);
			}
		}
		return midPoints;
	}

	/**
	 * splits a specified segment on specified points
	 * @param segment
	 * @param midPoints
	 * @return set of broken down segments
	 */
	private Set<Segment> breakSegmentOnPoints(Segment segment, Set<Point> midPoints) {
		Set<Segment> implicitSegments = new LinkedHashSet<Segment>();
		List<Point> points = new ArrayList<Point>(midPoints);
		points.add(segment.getPoint1());
		points.add(segment.getPoint2());
		points.sort(Comparator.naturalOrder());
		for(int i=0; i<points.size()-1; i++) {
			implicitSegments.add(new Segment(points.get(i), points.get(i+1)));
		}
		return implicitSegments;
	}

	/**
	 * finds and splits a given segment on an overlapping point if one exists
	 * @param segment
	 * @param implicitPoints
	 * @return set from broken down segment
	 */
	private Set<Segment> computeImplicitSegmentBreaksIfExists(Segment segment, Set<Point> implicitPoints) {
		Set<Segment> implicitSegments = new LinkedHashSet<Segment>();
		Set<Point> midPoints = getIntersectionPoints(segment, implicitPoints);
		if(midPoints.size() != 0) {
			implicitSegments.addAll(breakSegmentOnPoints(segment, midPoints));
		}
		return implicitSegments;
	}

	/**
	 * computes the set of all implicit segments
	 * @param implicitPoints
	 * @return the set of implicit segments
	 */
	protected Set<Segment> computeImplicitBaseSegments(Set<Point> implicitPoints) {
		Set<Segment> implicitSegments = new LinkedHashSet<Segment>();
		for(Segment segment: _givenSegments) {
			implicitSegments.addAll(computeImplicitSegmentBreaksIfExists(segment, implicitPoints));
		}
		return implicitSegments;
	}

	/**
	 * determines whether is segment has a point that lies between its end points
	 * @param segment
	 * @param implicitPoints
	 * @return whether the segment is minimal
	 */
	private boolean isMinimal(Segment segment, Set<Point> implicitPoints) {
		for (Point point : implicitPoints) {
			if (segment.pointLiesBetweenEndpoints(point)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * finds the set of all minimal segments
	 * @param implicitPoints
	 * @param givenSegments
	 * @param implicitSegments
	 * @return the set of all minimal segments
	 */
	protected Set<Segment> identifyAllMinimalSegments(Set<Point> implicitPoints, Set<Segment> givenSegments, Set<Segment> implicitSegments) {
		Set<Segment> allMinimalSegments = new LinkedHashSet<Segment>();
		for (Segment segment : givenSegments) {
			if (isMinimal(segment, implicitPoints)) {
				allMinimalSegments.add(segment);
			}
		}
		allMinimalSegments.addAll(implicitSegments);
		return allMinimalSegments;
	}
	
	/**
	 * gets the midpoint for a segment
	 * @param segment
	 * @return the midpoint
	 */
	private Point getMidpoint(Segment segment) {
		Point point1 = segment.getPoint1();
		Point point2 = segment.getPoint2();
		double avrX = (point1.getX() + point2.getX()) / 2;
		double avrY = (point1.getY() + point2.getY()) / 2;
		return new Point(avrX, avrY);
	}
	
	/**
	 * gets the midpoints for a of the segments associated with the segment
	 * @param allMinimalSegments
	 * @return a mapping from midpoint to segment
	 */
	private Map<Point, Segment> getMidPointsAssociatedWithSegments(Set<Segment> allMinimalSegments) {
		Map<Point, Segment> SegmentMidPoints = new HashMap<Point, Segment>();
		for(Segment segment: allMinimalSegments) {
			SegmentMidPoints.put(getMidpoint(segment), segment);
		}
		return SegmentMidPoints;
	}
	
	/**
	 * Sorts the segments based on their midpoints
	 * @param allMinimalSegments
	 * @return list of sorted segments
	 */
	private List<Segment> sortSegments(Set<Segment> allMinimalSegments) {
		List<Segment> sortedSegment = new ArrayList<Segment>();
		Map<Point, Segment> SegmentMidPoints = getMidPointsAssociatedWithSegments(allMinimalSegments);
		List<Point> midPoints = new ArrayList<Point>(SegmentMidPoints.keySet());
		
		midPoints.sort(Comparator.naturalOrder());
		
		for(Point midPoint: midPoints) {
			sortedSegment.add(SegmentMidPoints.get(midPoint));
		}
		return sortedSegment;
	}

	/**
	 * gets the segment group that the segment belongs in.
	 * A segment belongs in a group if it has the same slope as the rest of the group
	 * and shares a vertex with at least one group member.
	 * @param segment
	 * @param groupedSegments
	 * @return the proper group or -1
	 */
	private int getBelongingGroup(Segment segment, ArrayList<ArrayList<Segment>> groupedSegments) {
		for(int i=0; i<groupedSegments.size(); i++) {
			ArrayList<Segment> group = groupedSegments.get(i);
			if(MathUtilities.doubleEquals(group.get(0).slope(), segment.slope())) {
				for(Segment other: group) {
					if(segment.sharedVertex(other) != null)
						return i;
				}
			}
		}
		return -1;
	}

	/**
	 * groups segments in to bins such that each group has the same slope and is contiguous
	 * @param sortedSegments
	 * @return grouped segments
	 */
	private ArrayList<ArrayList<Segment>> contructGroupedSegments(List<Segment> sortedSegments) {
		ArrayList<ArrayList<Segment>> groupedSegments = new ArrayList<ArrayList<Segment>>();
		for(Segment segment: sortedSegments) {
			int belongsTo = getBelongingGroup(segment, groupedSegments);
			if(belongsTo >= 0) {
				groupedSegments.get(belongsTo).add(segment);
			}
			else {
				ArrayList<Segment> newGroup = new ArrayList<Segment>();
				newGroup.add(segment);
				groupedSegments.add(newGroup);
			}
		}
		return groupedSegments;
	}

	/**
	 * merges specified segments together.
	 * @param segment1
	 * @param segment2
	 * @return merged segment
	 */
	private Segment mergeSegments(Segment segment1, Segment segment2) {
		List<Point> points = new ArrayList<Point>();
		points.add(segment1.getPoint1());
		points.add(segment1.getPoint2());
		points.add(segment2.getPoint1());
		points.add(segment2.getPoint2());
		points.sort(Comparator.naturalOrder());
		return new Segment(points.get(0), points.get(points.size()-1));
	}

	/**
	 * merges all pairs of the segments in the group
	 * @param group
	 * @return the merge group
	 */
	private Set<Segment> mergeGroup(ArrayList<Segment> group) {
		Set<Segment> mergeGroup = new LinkedHashSet<Segment>();
		for(int i=0; i<group.size()-1; i++) {
			for(int j=i+1; j<group.size(); j++) {
				mergeGroup.add(mergeSegments(group.get(i), group.get(j)));
			}
		}
		return mergeGroup;
	}

	/**
	 * Constructs the set of non-minimal segments
	 * @param allMinimalSegments
	 * @return set of non-minimal segments
	 */
	protected Set<Segment> constructAllNonMinimalSegments(Set<Segment> allMinimalSegments) {
		List<Segment> sortedSegments = sortSegments(allMinimalSegments);
		Set<Segment> nonMinimalSegments = new LinkedHashSet<Segment>();
		ArrayList<ArrayList<Segment>> groupedSegments = contructGroupedSegments(sortedSegments);
		for(ArrayList<Segment> group: groupedSegments) {
			nonMinimalSegments.addAll(mergeGroup(group));
		}
		return nonMinimalSegments;
	}
}
