/**
 * Creates a database for the SegmentNodes.
 *
 * <p>Bugs: none known
 *
 * @author Sam Luck-Leonard, Mason Taylor, and Josh Berger
 * @date 1/27/2023
 */
package input.components.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import input.components.ComponentNode;
import input.components.point.PointNode;
import input.visitor.ComponentNodeVisitor;
import utilities.io.StringUtilities;

public class SegmentNodeDatabase implements ComponentNode{

	private Map<PointNode, Set<PointNode>> _adjLists;

	public SegmentNodeDatabase() {

		_adjLists = new HashMap<PointNode, Set<PointNode>>();

	}

	public SegmentNodeDatabase(Map<PointNode, Set<PointNode>> adjLists) {

		if (adjLists == null) _adjLists = new HashMap<PointNode, Set<PointNode>>();
		else _adjLists = new HashMap<PointNode, Set<PointNode>>(adjLists);

	}

	// the adjacency map has each segment twice, so to get the actual number of segments, you should take half
	public int numUndirectedEdges() {

		int total = 0;

		for (Set<PointNode> adjacencies : _adjLists.values()) {

			total = total + adjacencies.size();

		}

		return total / 2;

	}

	/**
	 * 
	 * Adds a directed edge from node 'a' to node 'b' in the adjacency list.
	 * If the adjacency list does not contain node 'a', it will be added with node 'b' in its adjacency list.
	 * If node 'b' is not already in node 'a's adjacency list, it will be added.
	 * @param a the starting node of the directed edge
	 * @param b the ending node of the directed edge
	 */
	private void addDirectedEdge(PointNode a, PointNode b) {

		if(a == null || b == null) return;

		// if the map doesn't contain point a add it with b in its adjacency list

		if(!_adjLists.containsKey(a)) {

			_adjLists.put(a, new HashSet<PointNode>(Arrays.asList(b)));

		}

		// if b is not in a's adjacency list, add it

		if(!_adjLists.get(a).contains(b)) {

			_adjLists.get(a).add(b);

		}

	}

	/**
	 * Adds a directed edge both ways, resulting in an undirected edge
	 * @param a the starting node of one directed edge and the ending node of another
	 * @param b the starting node of one directed edge and the ending node of another
	 */

	public void addUndirectedEdge(PointNode a, PointNode b) {

		// call directed edge both ways

		this.addDirectedEdge(a, b);

		this.addDirectedEdge(b, a);

	}

	/**
	 * Allows the creation of a new node with a full adjacency list.
	 * If the node is already in the database, it adds the nodes from the inputed adjacency list to 
	 * the already existing adjacency list avoiding duplicates
	 * @param point the main point that the adjacency list is being made for
	 * @param adjLists the adjacency list for the main node
	 */

	public void addAdjacencyList(PointNode point, List<PointNode>adjLists) {

		if(point == null || adjLists == null) return;
		//isEmpty instead of size == 0
		if(adjLists.size()== 0) return;

		for(PointNode node : adjLists) {

			this.addUndirectedEdge(point, node);

		}
	}

	/**
	 * Returns a list of segments in the graph going both ways.
	 * It does this by going through the adjacency list for the graph and creating segments from each
	 * parent PointNode to its children PointNodes and adding them to a list
	 * @return a list of unique SegmentNodes in the graph.
	 */
	public List<SegmentNode> asUniqueSegmentList(){

		List<SegmentNode> segments = new ArrayList<SegmentNode>();

		for(Entry<PointNode, Set<PointNode>> entry : _adjLists.entrySet()) {

			for(PointNode node : entry.getValue()) {

				segments.add(new SegmentNode(entry.getKey(), node));
			}

		}

		return segments;
	}
	/**
	 * Returns a list of SegmentNodes representing the segments in this graph.
	 * This method iterates through the adjacency lists of the graph and creates a
	 * new SegmentNode for each unique pair of points. The segments are then added to a list and returned.
	 * @return a list of SegmentNodes objects representing the segments in this graph
	 */
	public List<SegmentNode> asSegmentList() {
		//could be a set so you dont have to check containment
		List<SegmentNode> segments = new ArrayList<SegmentNode>();
		List<PointNode> keys = new ArrayList<PointNode>();

		for (Entry<PointNode, Set<PointNode>> entry : _adjLists.entrySet()) { 

			keys.add(entry.getKey());

			for (PointNode p : entry.getValue()) {

				if (!keys.contains(p)) {

					segments.add(new SegmentNode(entry.getKey(),p));

				}

			}

		}

		return segments;

	}

	public String toString() {

		String str = "";

		for(PointNode head : _adjLists.keySet()) {

			str += "\n" + StringUtilities.indent(2) + head.getName() + " : " + "[" + _adjLists.get(head).toString() + "]";

		}

		return str;

	}

//	@Override
//	public void unparse(StringBuilder sb, int level) {
//
//		sb.append(StringUtilities.indent(level) + "Segments: \n" + StringUtilities.indent(level) + "{ \n");
//
//		sb.append(StringUtilities.indent(level) + this.toString());
//
//		sb.append(StringUtilities.indent(level - 1) + "}\n");
//
//	}

	@Override
	public boolean equals(Object o) {

		if(o == null)  return false; 

		if(!(o instanceof SegmentNodeDatabase))  return false; 
		
		SegmentNodeDatabase snd = (SegmentNodeDatabase) o;
		
		for(PointNode point : snd._adjLists.keySet()) {
			
			Set<PointNode> values = snd._adjLists.get(point);
			
			if(!_adjLists.get(point).equals(values)) return false;
			
		}
		
		return true;

	}
	
	public Map<PointNode, Set<PointNode>> getAdjLists(){ return _adjLists; }

	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o) { return visitor.visitSegmentDatabaseNode(this, o); }

}