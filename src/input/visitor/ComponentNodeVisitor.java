package input.visitor;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

//
// All of the following are ComponentNode Classes
//    * FigureNode
//    * PointNode
//    * PointNodeDatabase
//    * SegmentNodeDatabase
//
// Depending on implementation, SegmentNode may be a ComponentNode.
//
// For each ComponentNode, we need a visit method as established in this interface.
//
public interface ComponentNodeVisitor
{
	Object visitFigureNode(FigureNode node, Object o);

	Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o);
	
	Object visitSegmentNode(SegmentNode node, Object o);
	
	Object visitPointNode(PointNode node, Object o);

	Object visitPointNodeDatabase(PointNodeDatabase node, Object o);
}