package input.parser;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;

public class JSON_Constants
{
	public static final String JSON_FIGURE = "Figure";
	public static final String JSON_FIGURE_S = "Figures";

	public static final String JSON_DESCRIPTION = "Description";
	
	public static final String JSON_POINT_S = "Points";

	public static final String JSON_NAME = "name";
	public static final String JSON_X = "x";
	public static final String JSON_Y = "y";
	
	public static final String JSON_SEGMENTS = "Segments";

	public static final List<String> TOP_LEVEL_STRINGS = Arrays.asList(JSON_FIGURE_S, JSON_FIGURE);
	public static final JSONArray TOP_LEVEL_JSON_ARRAY = new JSONArray(TOP_LEVEL_STRINGS);
}