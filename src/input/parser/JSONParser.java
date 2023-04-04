/**
* Creates a ComponentNode from parsing a json file.
*
* <p>Bugs: none known
*
* @author Sam Luck-Leonard, Mason Taylor, and Grace Houser
* @date 2/24/2023
*/

package input.parser;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import input.builder.DefaultBuilder;
import input.components.*;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
import input.exception.ParseException;

public class JSONParser
{
	protected ComponentNode  _astRoot;
	
	protected DefaultBuilder _builder;

	public JSONParser(DefaultBuilder builder)
	{
		_astRoot = null;
		
		_builder = builder;
		
	}

	private void error(String message)
	{
		throw new ParseException("Parse error: " + message);
	}

	public ComponentNode parse(String str) throws ParseException
	{
		// Parsing is accomplished via the JSONTokenizer class.
		JSONTokener tokenizer = new JSONTokener(str);
		JSONObject JSONroot = (JSONObject)tokenizer.nextValue();
		
		String description;
		PointNodeDatabase pnd;
		SegmentNodeDatabase snd;
		
		// parse elements from input JSONroot 
		try {
			JSONObject values = JSONroot.getJSONObject("Figure");
			description = values.getString("Description");
			pnd = getPointNodeDatabase(values);
			snd = getSegmentNodeDatabase(values, pnd);
		}
		
		catch(JSONException e) { throw new ParseException("File is incorrecly formatted."); }
		
		// return FigureNode with input data 
		return _builder.buildFigureNode(description, pnd, snd);
	}

	// makes and returns a PointNodeDatabase from the given JSON object 
	private PointNodeDatabase getPointNodeDatabase(JSONObject values) {

		JSONArray points = values.getJSONArray("Points");
		
		ArrayList<PointNode> pointList = new ArrayList<PointNode>();
		
		for (Object point : points) { pointList.add(makePointNode(point)); }

		return _builder.buildPointDatabaseNode(pointList);

	}

	// makes and returns a PointNode from a given dictionary 
	private PointNode makePointNode(Object point) {

		String name = "";
		String x = "";
		String y = "";

		String p_str = point.toString();
		int count = 0;

		// finds the name, x, and y value from the point's String
		for(int i = 0; i < p_str.length(); i++) {

			char character = p_str.charAt(i);

			if(character == ':') {

				// found name 
				if (count == 0) {
					name = p_str.substring(i + 2, i + 3);
					count = count + 1;
				}

				// found x
				else if(count == 1) {
					char sep = ',';
					int sepIndex = p_str.indexOf(sep, i + 1);
					x = p_str.substring(i + 1, sepIndex);
					count = count + 1;
				}

				// found y 
				else if(count == 2) {
					char sep = '}';
					int sepIndex = p_str.indexOf(sep, i + 1);
					y = p_str.substring(i + 1, sepIndex);
					count = count + 1;
				}
			}	
		}

		return _builder.buildPointNode(name, Double.parseDouble(x), Double.parseDouble(y));
	}

	// returns a SegmentNodeDatabase from the given JSON object 
	private SegmentNodeDatabase getSegmentNodeDatabase(JSONObject values, PointNodeDatabase pnd) {

		JSONArray segments = values.getJSONArray("Segments");
		SegmentNodeDatabase snd  = _builder.buildSegmentNodeDatabase();

		// adds all segments to the SegmentNodeDatabase 
		for(Object segment : segments) {
			makeSegmentNodeDatabase(segment, pnd, snd);
		}

		return snd;
	}

	// adds segments from a dictionary to a given SegmentNodeDatabase 
	private void makeSegmentNodeDatabase(Object segment, PointNodeDatabase points, SegmentNodeDatabase snd) {

		if(segment == null) return;
		
		if(points == null) return;
		
		if(snd == null) return;
		
		String s_str = segment.toString();

		// get the head PointNode
		String headName = s_str.substring(2, 3);
		PointNode head = points.getPoint(headName);

		// find all tail PointNodes and add the segments 
		for(int i = 5; i < s_str.length(); i++) {

			char character = s_str.charAt(i);

			if(character == '"') {

				// get the tail PointNode
				String tailName = s_str.substring(i + 1, i + 2);
				PointNode tail = points.getPoint(tailName);

				// add the head/tail pairing to the SegmentNodeDatabase
				_builder.addSegmentToDatabase(snd, head, tail);

				i += 2;
			}
		}
	}
	
	public ComponentNode parse(JSONObject JSONroot) throws ParseException
	{
		// Parsing is accomplished via the JSONTokenizer class.
		
		String description;
		PointNodeDatabase pnd;
		SegmentNodeDatabase snd;
		
		// parse elements from input JSONroot 
		try {
			JSONObject values = JSONroot.getJSONObject("Figure");
			description = values.getString("Description");
			pnd = getPointNodeDatabase(values);
			snd = getSegmentNodeDatabase(values, pnd);
		}
		
		catch(JSONException e) { throw new ParseException("File is incorrecly formatted."); }
		
		// return FigureNode with input data 
		return _builder.buildFigureNode(description, pnd, snd);
	}
	
	

}