package de.gianasista.tldr_viewer;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import de.gianasista.tldr_viewer.util.MdFileContentParser;
import de.gianasista.tldr_viewer.util.TldrContentProvider;

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
		
		/*
		List<CharSequence> lines = MdFileContentParser.parseContentFromAsset(getAssets(), commandName);
		for(CharSequence line : lines)
			textView.append(line);
			*/
		textView.setText(new TldrContentProvider(getAssets()).getHtmlContentStringForCommand(commandName));
		//textView.append(Html.fromHtml("<font color='#0000FF'>Blue test</font>"));
		textView.setMovementMethod(new ScrollingMovementMethod());
	}

}
