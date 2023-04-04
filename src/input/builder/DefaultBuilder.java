package input.builder;

import java.util.List;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

public class DefaultBuilder {

	public DefaultBuilder() { }

	public FigureNode buildFigureNode(String description,
			PointNodeDatabase points,
			SegmentNodeDatabase segments)
	{
		return null;
	}

	public SegmentNodeDatabase buildSegmentNodeDatabase()
	{
		return null;
	}

	public void addSegmentToDatabase(SegmentNodeDatabase segments, PointNode from, PointNode to)
	{
		if (segments != null) segments.addUndirectedEdge(from, to);
	}

	public SegmentNode buildSegmentNode(PointNode pt1, PointNode pt2)
	{
		return null;
	}

	public PointNodeDatabase buildPointDatabaseNode(List<PointNode> points)
	{
		return null;
	}

	public PointNode buildPointNode(String name, double x, double y)
	{
		return null;
	}
}
