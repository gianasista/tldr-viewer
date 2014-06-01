package de.gianasista.tldr_viewer;

import java.io.IOException;

import android.app.ListActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommandListActivity extends ListActivity {

	static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
		"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
		"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		Log.d(CommandListActivity.class.getName(), "onCreate");
		String[] fileCommandList = FRUITS;
		AssetManager assetManager = getAssets();
		try {
			fileCommandList = assetManager.list("common");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(CommandListActivity.class.getName(), e.getMessage());
		}
 
		setListAdapter(new ArrayAdapter<String>(this, R.layout.command_list,fileCommandList));
 
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
 
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
		
	}
}
