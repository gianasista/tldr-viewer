package de.gianasista.tldr_viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gianasista.tldr_viewer.backend.Command;
import de.gianasista.tldr_viewer.backend.TldrApiClient;

public class CommandListActivity extends ActionBarActivity {

    private static final String TAG = CommandListActivity.class.getName();

	public static final String COMMAND_NAME = "COMMAND_NAME";
    public static final String PLATFORMS = "PLATFORMS";

	private String userChoice;
	
	private ListView commandListView;

    private List<Command> tldrCommands = new ArrayList<>(0);

	private MenuItem reloadMenuItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.command_list); 
		Log.d(CommandListActivity.class.getName(), "onCreate");
		
		commandListView = (ListView) findViewById(R.id.id_command_list_view);
		commandListView.setTextFilterEnabled(true);
		
		commandListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Command command = (Command) commandListView.getAdapter().getItem(position);
				commandSelected(command);
			}
		});	
		
		EditText searchField = (EditText) findViewById(R.id.id_search_field);
		searchField.addTextChangedListener(new TextWatcher() {
			@SuppressWarnings("unchecked")
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				((ArrayAdapter<String>)commandListView.getAdapter()).getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		// set focus to listView (on some devices the textfield has inital
		// focus which does not look very nice)
		commandListView.requestFocus();
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		reloadMenuItem = menu.add(getApplicationContext().getString(R.string.list_reload));
		reloadMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		reloadItemList();

		return true;
	}

	private void updateReloadButton() {
		reloadMenuItem.setVisible(tldrCommands.isEmpty());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getTitle().equals(getApplicationContext().getString(R.string.list_reload)))
			reloadItemList();

		return super.onOptionsItemSelected(item);
	}

	private void reloadItemList()
	{
		setProgressBarIndeterminateVisibility(true);
		TldrApiClient.getPages(new TldrApiClient.PagesListCallback() {
			@Override
			public void receivePages(List<Command> list, boolean hasError, String errorMessage) {
				tldrCommands = list;
				Collections.sort(tldrCommands);
				updateListData();
				setProgressBarIndeterminateVisibility(false);

				if(hasError)
					Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void updateListData()
	{
        ListAdapter adapter = new CommandListAdapter(this, tldrCommands.toArray(new Command[0]));
		commandListView.setAdapter(adapter);
		updateReloadButton();
	}
	
	private void commandSelected(Command command)
	{
		Toast.makeText(getApplicationContext(), command.getName(), Toast.LENGTH_SHORT).show();
		Intent detailIntent = new Intent(this, CommandContentActivity.class);
		detailIntent.putExtra(COMMAND_NAME, command.getName());
        detailIntent.putExtra(PLATFORMS, command.getPlatforms().toArray(new String[0]));
		startActivity(detailIntent);
	}

    private class CommandListAdapter extends ArrayAdapter<Command> {

        public CommandListAdapter(Context context, Command[] objects) {
            super(context, R.layout.command_list_item, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.command_list_item, parent, false);
            } else {
                view = convertView;
            }

            TextView textCommand = (TextView) view.findViewById(R.id.command_item);
            Command item = getItem(position);
            textCommand.setText(item.getName());
            TextView textPlatform = (TextView) view.findViewById(R.id.command_platform);
            textPlatform.setText(item.getPlatformString());

            return view;
        }
    }
}
