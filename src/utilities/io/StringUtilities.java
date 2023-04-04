package utilities.io;

public class StringUtilities
{
	/**
	 * @param level -- levels to which we indent
	 * @return a string containg the corresponding number of spaces requested
	 */
	public static String indent(int level)
	{
		return "    ".repeat(level);
	}
}