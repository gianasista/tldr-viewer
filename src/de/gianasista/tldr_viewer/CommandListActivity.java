package de.gianasista.tldr_viewer;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.gianasista.tldr_viewer.util.TldrContentProvider;

public class CommandListActivity extends ActionBarActivity {

	public static final String COMMAND_NAME = "COMMAND_NAME";
	
	private ListView commandListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.command_list); 
		Log.d(CommandListActivity.class.getName(), "onCreate");
		
		commandListView = (ListView) findViewById(R.id.id_command_list_view);
		commandListView.setTextFilterEnabled(true);
		
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.command_list_item,new TldrContentProvider(getAssets()).getCommandList());
		commandListView.setAdapter(listAdapter);
		
		commandListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    CharSequence selectedCommand = ((TextView) view).getText();
				commandSelected(selectedCommand);
			}
		});	
		
		EditText searchField = (EditText) findViewById(R.id.id_search_field);
		searchField.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				listAdapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
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
			case (R.id.action_settings): return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	private void actionSettings()
	{
		
	}
	
	private void commandSelected(CharSequence commandName)
	{
		Toast.makeText(getApplicationContext(), commandName, Toast.LENGTH_SHORT).show();
		Intent detailIntent = new Intent(this, CommandContentActivity.class);
		detailIntent.putExtra(COMMAND_NAME, commandName);
		startActivity(detailIntent);
	}
}
