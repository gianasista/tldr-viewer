package de.gianasista.tldr_viewer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import de.gianasista.tldr_viewer.util.TldrContentProvider;

/**
 * @author vera
 *
 */
public class CommandContentActivity extends ActionBarActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_command_content);
		TextView textView = (TextView)findViewById(R.id.detail_content);
		String commandName = getIntent().getStringExtra(CommandListActivity.COMMAND_NAME);
		getSupportActionBar().setTitle(commandName);
		
		textView.setText(new TldrContentProvider(getAssets()).getHtmlContentStringForCommand(commandName));
		textView.setMovementMethod(new ScrollingMovementMethod());
	}

}
