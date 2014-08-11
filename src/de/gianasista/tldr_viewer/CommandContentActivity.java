package de.gianasista.tldr_viewer;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class CommandContentActivity extends ActionBarActivity implements CommandContentDelegate, Handler.Callback 
{
	private TextView textView;
	private TldrContentProvider contentProvider;
	
	private Timer loadingTimer = new Timer();
	
	private static final String[] loadingText = { "Loading", "Loading .", "Loading ..", "Loading ..."};
	private int loadingPos = 0;
	
	private Handler loadingUpdateHandler;
	
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
		
		loadingUpdateHandler = new Handler(this);
		
		TimerTask loadingTask = new TimerTask() {
			
			@Override
			public void run() 
			{
				loadingUpdateHandler.sendEmptyMessage(0);
			}
			
		};
		
		loadingTimer.schedule(loadingTask, 0, 500);
		
	}

	@Override
	public void receiveCommandContent(String content) 
	{
		loadingTimer.cancel();
		textView.setText(Html.fromHtml(content));
	}

	@Override
	public boolean handleMessage(Message msg) {
		textView.setText(loadingText[loadingPos]);
		loadingPos = (loadingPos+1) % 4;
		return true;
	}

}
