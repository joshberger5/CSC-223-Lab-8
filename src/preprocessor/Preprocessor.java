package preprocessor;

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
		
		_implicitPoints = new LinkedHashSet<Point>();
		_implicitSegments = new LinkedHashSet<Segment>();
		_allMinimalSegments = new LinkedHashSet<Segment>();
		_nonMinimalSegments = new LinkedHashSet<Segment>();
		
		_segmentDatabase = new HashMap<Segment, Segment>();
		
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
	 * finds and splits a given segment on an overlapping point if one exists
	 * @param segment
	 * @param implicitPoints
	 * @return set from broken down segment
	 */
	private Set<Segment> computeImplicitSegmentBreaksIfExists(Segment segment, Set<Point> implicitPoints) {
		Set<Segment> implicitSegments = new LinkedHashSet<Segment>();
		Set<Point> midPoints = new LinkedHashSet<Point>();
		for(Point point : implicitPoints) {
			if(segment.pointLiesBetweenEndpoints(point)) {
				midPoints.add(point);
			}
		}
		implicitSegments.addAll(breakSegmentOnPoints(segment, midPoints));
		return implicitSegments;
	}

	/**
	 * splits a specified segment on a specified point and adds to the implicit segments
	 * @param segment
	 * @param midPoints
	 * @return set of two broken down segments
	 */
	private Set<Segment> breakSegmentOnPoints(Segment segment, Set<Point> midPoints) {
		//<-->   <--(-[-)--]->  ==> /--/-/-/--/- 
		//
		//         +  (  [             +  (  [
		//<-->   <-|--|--|->    ==>  /-/--/--/-/
		//         ?  )  ]             ?  )  ]
//		Set<Segment> implicitSegments = new LinkedHashSet<Segment>();
//		implicitSegments.add(new Segment(segment.getPoint1(), midPoints));
//		implicitSegments.add(new Segment(midPoints, segment.getPoint2()));
//		return implicitSegments;
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
		allMinimalSegments.addAll(_implicitSegments);
		return allMinimalSegments;
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
	 * Constructs the set of non-minimal segments
	 * @param allMinimalSegments
	 * @return set of non-minimal segments
	 */
	protected Set<Segment> constructAllNonMinimalSegments(Set<Segment> allMinimalSegments) {
		Set<Segment> nonMinimalSegments = new LinkedHashSet<Segment>();
		for (Segment segment: _givenSegments) {
			if (!allMinimalSegments.contains(segment)) {
				nonMinimalSegments.add(segment);
			}
		}
		return nonMinimalSegments;
	}
}
