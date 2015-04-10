package de.gianasista.tldr_viewer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.gianasista.tldr_viewer.util.TldrContentProvider;
import de.gianasista.tldr_viewer.util.TldrPreferences;

public class CommandListActivity extends ActionBarActivity {

	public static final String COMMAND_NAME = "COMMAND_NAME";
	private String userChoice;
	
	private ListView commandListView;
	
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
			    CharSequence selectedCommand = ((TextView) view).getText();
				commandSelected(selectedCommand);
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
	
	private void updateListData()
	{
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.command_list_item, new TldrContentProvider(this).getCommandList());
		commandListView.setAdapter(listAdapter);
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
	
	private void commandSelected(CharSequence commandName)
	{
		Toast.makeText(getApplicationContext(), commandName, Toast.LENGTH_SHORT).show();
		Intent detailIntent = new Intent(this, CommandContentActivity.class);
		detailIntent.putExtra(COMMAND_NAME, commandName);
		startActivity(detailIntent);
	}
}
