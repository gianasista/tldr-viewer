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
    public String parseMdFileContent(String mdFileContent)
    {
        StringBuilder result = new StringBuilder();
        String[] lines = mdFileContent.split("\\n");
        for(String line : lines) {
            result.append(parseLine(line));
        }

        return result.toString();
    }

	public String parseLine(String line)
	{
		String parsedLine = "";
		if(line.length()<1)
			parsedLine = line;
		else 
			{
			String trimmedLine = line.substring(1).trim();
			
			if(line.startsWith("#"))
				//parsedLine = "<b><font color='#0000FF'>"+trimmedLine+"</font></b>";
				parsedLine = "";
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

       // return parsedLine.length() > 0 ? parsedLine.concat("<br/>") : parsedLine;
        return parsedLine.concat("<br/>");
	}
}
