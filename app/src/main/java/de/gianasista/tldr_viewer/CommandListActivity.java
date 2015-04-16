package de.gianasista.tldr_viewer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import de.gianasista.tldr_viewer.util.TldrContentProvider;
import de.gianasista.tldr_viewer.util.TldrPreferences;

public class CommandListActivity extends ActionBarActivity {

    private static final String TAG = CommandListActivity.class.getName();

	public static final String COMMAND_NAME = "COMMAND_NAME";
    public static final String PLATFORMS = "PLATFORMS";

	private String userChoice;
	
	private ListView commandListView;

    private List<Command> tldrCommands = new ArrayList<>(0);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.command_list); 
		Log.d(CommandListActivity.class.getName(), "onCreate");
		
		commandListView = (ListView) findViewById(R.id.id_command_list_view);
		commandListView.setTextFilterEnabled(true);
		
		updateListData();
		
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

        TldrApiClient.getPages(new TldrApiClient.PagesListCallback() {
            @Override
            public void receivePages(List<Command> list) {
                tldrCommands = list;
                Collections.sort(tldrCommands);
                updateListData();

                /*
                for(Command command : tldrCommands) {
                    if(command.getPlatforms().size() > 1)
                        Log.i(TAG, "Wichtiger Command: "+command.getName()+ "- "+command.getPlatforms());
                }
                */
            }
        });
	}
	
	private void updateListData()
	{
		//final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.command_list_item, new TldrContentProvider(this).getCommandList());
        ListAdapter adapter = new CommandListAdapter(this, tldrCommands.toArray(new Command[0]));
		commandListView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.command_list_actions, menu);
	    return super.onCreateOptionsMenu(menu);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
			case (R.id.action_settings): actionSettings(); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	private void actionSettings()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.settings_title);
		builder.setPositiveButton(R.string.settings_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				TldrPreferences.setCurrentPlatform(userChoice);
				updateListData();
			}
		});
		builder.setNegativeButton(R.string.settings_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// Nothing to do, just dismiss
			}
		});
		
		//getSharedPreferences(TldrPreferences.TLDR_PREFS_NAME, Context.MODE_PRIVATE).getString(TldrPreferences.KEY_PLATFORM, "common.txt");
		builder.setSingleChoiceItems(TldrPreferences.possibleChoices, TldrPreferences.getIndexOfCurrentPlaformChoice(), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				userChoice = TldrPreferences.possibleChoices[which];
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
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
            return super.getView(position, convertView, parent);
        }
    }
}
