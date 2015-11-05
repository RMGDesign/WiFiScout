package com.example.wifiscoutapp;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UsersActivity extends ListActivity {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Allows movement back to scanning screen
		int id = item.getItemId();
		if (id == R.id.action_Main) {
			Intent intent = new Intent(this, MainActivity.class);

			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.users, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // Imports in the user list and
											// parses it for display
		ArrayList<User> users = userReader.reader("users.txt", getFilesDir()
				.toString());
		String[] t = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			String result = "Name: " + users.get(i).getName() + "\n"
					+ "MacAdd: " + users.get(i).getMac();
			t[i] = result;
		}

		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, t));

		ListView lv = getListView();

		// listening to single list item on click
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String profiler = ((TextView) view).getText().toString();
				String[] profiled = profiler.split("\n");
				String profile = profiled[1];
				Intent i = new Intent(getApplicationContext(),
						ProfileActivity.class);
				i.putExtra("profile", profile);
				startActivity(i);
				// Parses the selected profile so ProfileActivity can Receive
				// and interact properly with it.
			}
		});
	}
}
