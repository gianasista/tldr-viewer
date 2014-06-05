/**
 * 
 */
package de.gianasista.tldr_viewer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.text.Html;
import android.text.Spanned;

/**
 * @author vera
 *
 */
public class MdFileContentParser 
{
	private static final MdFileContentParser parser = new MdFileContentParser();
	
	public static String parseContentFromAsset(AssetManager manager, String assetFileName)
	{
		StringBuilder result = new StringBuilder();
		
		try {
			InputStream inputStream = manager.open("common/"+assetFileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while( (line=bufferedReader.readLine()) != null)
			{
				result.append(parser.parseLine(line));
			}
		} catch (IOException e) {
			// TODO Close Stream
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
	private String parseLine(String line)
	{
		String parsedLine = "";
		if(line.length()<1)
			parsedLine = line;
		else 
			{
			String trimmedLine = line.substring(1).trim();
			
			if(line.startsWith("#"))
				parsedLine = "<b><font color='#0000FF'>"+trimmedLine+"</font></b>";
			else if(line.startsWith(">"))
				parsedLine = "<i>"+trimmedLine+"</i>";
			else if(line.startsWith("-"))
				parsedLine = trimmedLine;
			else if(line.startsWith("`"))
				parsedLine = "<tt>"+trimmedLine+"</tt>";
			}
		
		return parsedLine.concat("<br/>");
	}
}
