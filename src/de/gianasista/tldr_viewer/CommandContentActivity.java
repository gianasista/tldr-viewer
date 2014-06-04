/**
 * 
 */
package de.gianasista.tldr_viewer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * @author vera
 *
 */
public class CommandContentActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_command_content);
		TextView textView = (TextView)findViewById(R.id.detail_content);
		String commandName = getIntent().getStringExtra(CommandListActivity.COMMAND_NAME);
		
		AssetManager manager = getAssets();
		StringBuilder fileContent = new StringBuilder();
		try {
			InputStream inputStream = manager.open("common/"+commandName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while( (line=bufferedReader.readLine()) != null)
				fileContent.append(line).append("\n");
		} catch (IOException e) {
			// TODO Close Stream
			e.printStackTrace();
		}
		
		textView.setText(fileContent);
		textView.setMovementMethod(new ScrollingMovementMethod());
	}

}
