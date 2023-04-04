package preprocessor.delegates;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;



import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessorTest {

	@Test
	void compute_empty_test() {
		PointDatabase points = new PointDatabase();
		ArrayList<Segment> segments = new ArrayList<Segment>();
		Set<Point> implicitPoints = ImplicitPointPreprocessor.compute(points, segments);
		
		assertTrue(implicitPoints.isEmpty());
	}
	
	@Test
	void compute_nonIntersecting_test() {
		Point a = new Point("A", 0, 0);
		Point b = new Point("B", 1, 0);
		Point c = new Point("C", 0, 1);
		Point d = new Point("D", 1, 1);
		List<Point> pointInput = Arrays.asList(new Point[]{a, b, c, d});
		PointDatabase points = new PointDatabase(pointInput);
		
		Segment ab = new Segment(a, b);
		Segment cd = new Segment(c, d);
		List<Segment> segments = Arrays.asList(new Segment[] {ab, cd});
		Set<Point> implicitPoints = ImplicitPointPreprocessor.compute(points, segments);
		
		assertTrue(implicitPoints.isEmpty());
	}
	
	@Test
	void compute_midIntersecting_test() {
		Point a = new Point("A", 0, 0);
		Point b = new Point("B", 1, 0);
		Point c = new Point("C", 0, 1);
		Point d = new Point("D", 1, 1);
		List<Point> pointInput = Arrays.asList(new Point[]{a, b, c, d});
		PointDatabase points = new PointDatabase(pointInput);
		
		Segment ad = new Segment(a, d);
		Segment bc = new Segment(b, c);
		List<Segment> segments = Arrays.asList(new Segment[] {ad, bc});
		Set<Point> implicitPoints = ImplicitPointPreprocessor.compute(points, segments);
		
		assertEquals("[*_A(0.5, 0.5)]", implicitPoints.toString());
	}
	
	@Test
	void compute_endIntersecting_test() {
		Point a = new Point("A", 0, 0);
		Point b = new Point("B", 1, 0);
		Point c = new Point("C", 0, 1);
		List<Point> pointInput = Arrays.asList(new Point[]{a, b, c});
		PointDatabase points = new PointDatabase(pointInput);
		
		Segment ab = new Segment(a, b);
		Segment bc = new Segment(b, c);
		List<Segment> segments = Arrays.asList(new Segment[] {ab, bc});
		Set<Point> implicitPoints = ImplicitPointPreprocessor.compute(points, segments);
		
		assertTrue(implicitPoints.isEmpty());
	}
}
