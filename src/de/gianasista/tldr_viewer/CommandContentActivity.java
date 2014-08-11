package de.gianasista.tldr_viewer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.ProgressBarICS;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.gianasista.tldr_viewer.util.CommandContentDelegate;
import de.gianasista.tldr_viewer.util.TldrContentProvider;

/**
 * @author vera
 *
 */
public class CommandContentActivity extends ActionBarActivity implements CommandContentDelegate
{
	private TextView textView;
	private TldrContentProvider contentProvider;
	
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_command_content);
		textView = (TextView)findViewById(R.id.detail_content);
		String commandName = getIntent().getStringExtra(CommandListActivity.COMMAND_NAME);
		getSupportActionBar().setTitle(commandName);
		
		contentProvider = new TldrContentProvider(this, this);
		contentProvider.loadHtmlContentStringForCommand(commandName);
		textView.setMovementMethod(new ScrollingMovementMethod());
		
		if(progressBar == null)
		{
			progressBar = new ProgressBar(getApplicationContext());
			addContentView(progressBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		progressBar.setVisibility(ProgressBar.VISIBLE);
	}

	@Override
	public void receiveCommandContent(String content) 
	{
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		textView.setText(Html.fromHtml(content));
	}

}
