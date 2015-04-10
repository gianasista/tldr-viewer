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
	public String parseLine(String line)
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
			{
				// line ends with ` so let's remove it
				trimmedLine = trimmedLine.substring(0, trimmedLine.length()-1);
				
				parsedLine = "<tt>"+trimmedLine+"</tt>";
			}
				
			}
		
		return parsedLine.concat("<br/>");
	}
}
