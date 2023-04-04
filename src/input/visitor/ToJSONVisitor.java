package input.visitor;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

public class ToJSONVisitor implements ComponentNodeVisitor {
	
	public Object visitFigureNode(FigureNode node, Object o)
	{
		JSONObject Shape = new JSONObject();
		JSONObject figure = new JSONObject();
		Shape.put("Description", node.getDescription());
		
		Shape.put("Points", node.getPointsDatabase().accept(this, null));
		
		Shape.put("Segments", node.getSegments().accept(this, null));
		
		figure.put("Figure", Shape);
		return figure;
	}

	@Override
	public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o) {
		
		JSONObject segmentDatabase = new JSONObject();
		Map<PointNode, Set<PointNode>> list = node.getAdjLists();
		
		for(PointNode head : list.keySet()) {
			JSONArray segments = new JSONArray();
			
			for(PointNode tail : list.get(head)) {
				segments.put(tail.getName());
			}		
			segmentDatabase.put(head.getName(), segments);
		}
		
		return segmentDatabase;
	}

	@Override
	public Object visitSegmentNode(SegmentNode node, Object o) {
		
		return null;
	}

	@Override
	public Object visitPointNode(PointNode node, Object o) {
		
		JSONObject point = new JSONObject();
		String name = node.getName();
		Double x = node.getX();
		Double y = node.getY();
		point.put("name", name);
		point.put("x", x);
		point.put("y", y);
		return point;
	}

	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o) {
		
		JSONArray points = new JSONArray();
		for(PointNode point : node.getPoints()) {
			points.put(point.accept(this, null));
		}
		return points;
	}
}
