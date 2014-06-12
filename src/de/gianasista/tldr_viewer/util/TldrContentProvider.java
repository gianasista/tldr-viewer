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
import android.util.Log;
import de.gianasista.tldr_viewer.CommandListActivity;

/**
 * @author vera
 *
 */
public class TldrContentProvider 
{
	private AssetManager assetManager;
	
	public TldrContentProvider(AssetManager assetManager)
	{
		this.assetManager = assetManager;
	}
	
	public String[] getCommandList()
	{
		String[] fileCommandList = {};
		
		try {
			fileCommandList = assetManager.list("common");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(CommandListActivity.class.getName(), e.getMessage());
		}
		
		return stripFileExtensionFromList(fileCommandList);
	}
	
	private String[] stripFileExtensionFromList(String[] listWithFileExtension)
	{
		List<String> resultList = new ArrayList<String>();
		for(String element : listWithFileExtension)
		{
			resultList.add(element.substring(0, element.length()-3));
		}
		
		return resultList.toArray(new String[] {});
	}
	
	public Spanned getHtmlContentStringForCommand(String commandName)
	{
		StringBuilder result = new StringBuilder();
		MdFileContentParser parser = new MdFileContentParser();
		
		try {
			InputStream inputStream = assetManager.open("common/"+commandName+".md");
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
		
		return Html.fromHtml(result.toString());
	}
}
