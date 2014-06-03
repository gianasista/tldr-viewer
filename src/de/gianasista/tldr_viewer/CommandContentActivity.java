/**
 * 
 */
package de.gianasista.tldr_viewer;

import android.app.Activity;
import android.os.Bundle;
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
		textView.setText(getIntent().getStringExtra("COMMAND_NAME"));
		
	}

}
