import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import geometry_objects.points.Point;
import geometry_objects.Segment;

class SegmentTest {

	@Test
	void hasSubSegmentWithSubSegmentTest() {
		// tests hasSubSegment(Segment candidate)
		// on Segment:       A----B--------D
		// with Segment:     A----B
		// obviously, it should return true
		Point A = new Point("A", 0, 0);
		Point D = new Point("D", 10, 0);
		Segment AD = new Segment(A, D);
		Point B = new Point("B", 2, 0);
		Segment AB = new Segment(A, B);
		
		assertTrue(AD.HasSubSegment(AB));
	}
	
	@Test
	void hasSubSegmentWithNonOverlappingCollinearSegmentTest() {
		// tests hasSubSegment(Segment candidate)
		// on Segment:    A-------------B
		// with Segment:                           C----D
		// obviously, it should return false
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		Point C = new Point("C", 20, 0);
		Point D = new Point("D", 25, 0);
		Segment CD = new Segment(C, D);
		
		assertFalse(AB.HasSubSegment(CD));
	}
	
	@Test
	void hasSubSegmentWithOverlappingCollinearSegmentTest() {
		// tests hasSubSegment(Segment candidate)
		// on Segment:    A----------C--B
		// with Segment:             C-----D
		// obviously, it should return false
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		Point C = new Point("C", 7, 0);
		Point D = new Point("D", 15, 0);
		Segment CD = new Segment(C, D);
		
		assertFalse(AB.HasSubSegment(CD));
	}
	
	@Test
	void hasSubSegmentNullTest() {
		// tests hasSubSegment(null)
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		
		assertFalse(AB.HasSubSegment(null));
	}
	
	@Test
	void coincideWithoutOverlapNullTest() {
		// tests coincideWithoutOverlap(Segment that)
		// on Segment:    A-------------B
		// with Segment:                           C----D
		// obviously, it should return true
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
				
		assertFalse(AB.coincideWithoutOverlap(null));
	}

	@Test
	void coincideWithoutOverlapNoOverlapCollinearTest() {
		// tests coincideWithoutOverlap(Segment that)
		// on Segment:    A-------------B
		// with Segment:                           C----D
		// obviously, it should return true
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		Point C = new Point("C", 20, 0);
		Point D = new Point("D", 25, 0);
		Segment CD = new Segment(C, D);
				
		assertTrue(AB.coincideWithoutOverlap(CD));
	}
	
	@Test
	void coincideWithoutOverlapEndpointOverlapCollinearTest() {
		// tests coincideWithoutOverlap(Segment that)
		// on Segment:    A-------------B
		// with Segment:                B---------------D
		// obviously, it should return true
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		Point D = new Point("D", 25, 0);
		Segment BD = new Segment(B, D);
				
		assertTrue(AB.coincideWithoutOverlap(BD));
	}
	
	@Test
	void coincideWithoutOverlapOverlappingCollinearSegmentTest() {
		// tests coincideWithoutOverlap(Segment that)
		// on Segment:    A----------C--B
		// with Segment:             C-----D
		// obviously, it should return false
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		Point C = new Point("C", 7, 0);
		Point D = new Point("D", 15, 0);
		Segment CD = new Segment(C, D);
		
		assertFalse(AB.coincideWithoutOverlap(CD));
	}
	
	@Test
	void collectOrderedPointsOnSegmentNullTest() {
		// tests collectOrderedPointsOnSegment(null)
		// should return null
		Point A = new Point("A", 0, 0);
		Point B = new Point("B", 10, 0);
		Segment AB = new Segment(A, B);
		
		assertNull(AB.collectOrderedPointsOnSegment(null));
	}
	
	@Test
	void collectOrderedPointsOnSegmentTest() {
		// tests collectOrderedPointsOnSegment(Set<Point> points)
		//			E					F
		// 			    A--B--C--D	I
		//			G		H	
		// on Segment AD
		// with A,B,C,D,E,F,G,H,I
		Set<Point> points = new HashSet<Point>();
		Point A = new Point("A", 0, 0);
		points.add(A);
		Point D = new Point("D", 10, 0);
		points.add(D);
		Segment AD = new Segment(A, D);
		
		Point B = new Point("B", 2, 0);
		points.add(B);
		Point C = new Point("C", 4, 0);
		points.add(C);
		Point E = new Point("E", -6, 6);
		points.add(E);
		Point F = new Point("F", 14, 6);
		points.add(F);
		Point G = new Point("G", -6, -6);
		points.add(G);
		Point H = new Point("H", 2, -6);
		points.add(H);
		Point I = new Point("I", 12, 0);
		points.add(I);
		
		Set<Point> RP = AD.collectOrderedPointsOnSegment(points);
		assertTrue(RP.contains(A));
		assertTrue(RP.contains(B));
		assertTrue(RP.contains(C));
		assertTrue(RP.contains(D));
		assertFalse(RP.contains(E));
		assertFalse(RP.contains(F));
		assertFalse(RP.contains(G));
		assertFalse(RP.contains(H));
		assertFalse(RP.contains(I));
	}
}
