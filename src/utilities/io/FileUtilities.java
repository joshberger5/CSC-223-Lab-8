package utilities.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtilities
{
	/**
	 * @param filepath -- path to a specific file
	 * @return string corresponding to the complete contents of the file
	 */
	public static String readFile(String filepath)
	{
		Path filePath = Path.of(filepath);
		String content = "";

		try { content = Files.readString(filePath); }
		catch (IOException e)
		{
			System.err.println("Unexpected file I/O problem with " + filePath);
		}
		
		return content;
	}
	
	/**
	 * Filter all comments using    // ...
	 * Must read the file 
	 * 
	 * @param filepath -- path to a specific file
	 * @return string corresponding to the complete contents of the file (minus comments)
	 */
	public static String readFileFilterComments(String filepath)
	{
		StringBuilder builder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) 
		{
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	// Filter out '//'-style comments
		    	int commentIndex = line.indexOf("//");
		    	if (commentIndex != -1) line = line.substring(0, commentIndex);
		    	
		    	if (line.length() >= 1) builder.append(line).append("\n");
		    }
		} 
		catch (IOException e)
		{
            System.err.println("Error reading file: " + filepath);
		}

		return builder.toString();
	}
}