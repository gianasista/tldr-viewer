package de.gianasista.tldr_viewer.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import de.gianasista.tldr_viewer.CommandListActivity;

/**
 * @author vera
 *
 */
public class TldrContentProvider extends AsyncTask<URL, Void, String>
{
	private AssetManager assetManager;
	private CommandContentDelegate contentDelegate;
	
	public TldrContentProvider(AssetManager assetManager)
	{
		this.assetManager = assetManager;
	}
	
	public TldrContentProvider(AssetManager assetManager, CommandContentDelegate delegate)
	{
		this(assetManager);
		
		this.contentDelegate = delegate;
	}
	
	public String[] getCommandList()
	{
		List<String> resultList = new ArrayList<String>();
		
		try
		{
			InputStream inputStream = assetManager.open("common.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while( (line=bufferedReader.readLine()) != null)
			{
				resultList.add(line);
			}
		}
		catch (IOException e) 
		{
			Log.e(this.getClass().getName(), e.getMessage());
		}
		
		return stripFileExtensionFromList(resultList.toArray(new String[] {}));
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
	
	public void loadHtmlContentStringForCommand(String commandName)
	{
		// https://raw.githubusercontent.com/tldr-pages/tldr/master/pages/common/ssh.md
		try {
			this.execute(new URL("https://raw.githubusercontent.com/tldr-pages/tldr/master/pages/common/"+commandName+".md"));
		} catch (MalformedURLException e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	protected String doInBackground(URL... params) 
	{
		if(params.length == 0) return null;
		
		StringBuilder result = new StringBuilder();
		MdFileContentParser parser = new MdFileContentParser();
		DataInputStream urlDataStream = null;
		BufferedReader reader = null;
		
		try {
			urlDataStream = new DataInputStream(params[0].openStream());
			reader = new BufferedReader(new InputStreamReader(urlDataStream));
			String line;
			
			while( (line=reader.readLine()) != null)
			{
				result.append(parser.parseLine(line));
			}
		} catch (IOException e) {
			try 
			{
				if (reader != null) 
					reader.close();
			} catch(IOException exc) {
				Log.e(this.getClass().getName(), exc.getMessage());
			}
			
			try 
			{
				if (urlDataStream != null) 
					urlDataStream.close();
			} catch(IOException exc) {
				Log.e(this.getClass().getName(), exc.getMessage());
			}
			
			Log.e(this.getClass().getName(), e.getMessage());
		}
		
		return result.toString();
	}
	
	@Override
	protected void onPostExecute(String result) 
	{
		super.onPostExecute(result);
		contentDelegate.receiveCommandContent(result);
	}
}
