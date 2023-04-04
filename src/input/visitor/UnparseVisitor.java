package input.visitor;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Set;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
import utilities.io.StringUtilities;

//
// This file implements a Visitor (design pattern) with 
// the intent of building an unparsed, String representation
// of a geometry figure.
//
public class UnparseVisitor implements ComponentNodeVisitor
{
	@Override
	public Object visitFigureNode(FigureNode node, Object o)
	{
		//if(!(o instanceof SimpleEntry<StringBuilder, Integer>)) { return null; }
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();

		sb.append(StringUtilities.indent(level) + "Figure: \n" + StringUtilities.indent(level) + "{ \n");

		// adds the description to the string builder 
		sb.append(StringUtilities.indent(level + 1) + "Description: " + node.getDescription() + "\n");

		node.getSegments().accept(this, pair);

		node.getPointsDatabase().accept(this, pair);
		
		sb.append(StringUtilities.indent(level) + "}");

		return null;
	}

	@Override
	public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o)
	{
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> entry = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);

		StringBuilder sb = entry.getKey();

		int level = entry.getValue() + 1;

		sb.append(StringUtilities.indent(level) + "Segments: \n" + StringUtilities.indent(level) + "{");

		sb.append(StringUtilities.indent(level) + node.toString() + "\n");

		sb.append(StringUtilities.indent(level) + "}\n");

		// TODO

		return null;
	}

	/**
	 * This method should NOT be called since the segment database
	 * uses the Adjacency list representation
	 */
	@Override
	public Object visitSegmentNode(SegmentNode node, Object o)
	{

		return null;
	}

	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{

		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> entry = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);

		StringBuilder sb = entry.getKey();

		int level = entry.getValue() + 1;

		sb.append(StringUtilities.indent(level) + "Points: \n" + StringUtilities.indent(level) + "{ \n");

		for(PointNode point : node.getPoints()) {

			point.accept(this, entry);

		}
		
		sb.append(StringUtilities.indent(level) + "}\n");
		// TODO

		return null;
	}

	@Override
	public Object visitPointNode(PointNode node, Object o)
	{

		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> entry = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);

		StringBuilder sb = entry.getKey();

		int level = entry.getValue() + 1;

		sb.append(StringUtilities.indent(level + 1) + "Point(" + node.getName() + ")" +  " " + "(" + node.getX() + ", " +  node.getY() + ")" + "\n");
		// TODO

		return null;
	}
}