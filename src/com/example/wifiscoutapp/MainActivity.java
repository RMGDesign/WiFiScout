package com.example.wifiscoutapp;

import java.io.File;
import java.io.IOException;
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

public class MainActivity extends ListActivity {
	ArrayAdapter<String> adt1;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handles the movement between the activities of the application
		int id = item.getItemId();
		if (id == R.id.action_Scan) { // Scans the network using a refresh of
										// the activity to evoke a new
										// onCreate() check
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
		if (id == R.id.action_Users) { // Moves to the User Screen
			Intent intent = new Intent(this, UsersActivity.class);

			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<User> sc = Scanner.getScanData(); // Pings the network and
													// Parses the arp table.
													// Creates generic users for
													// each found device and
													// creates an ArrayList of
													// Users
		ArrayList<User> use = new ArrayList<User>();
		File usrs = new File(getFilesDir().toString() + "/users.txt");
		if (usrs.exists()) {

		} else {
			try {
				usrs.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		userExport.exporter(sc, "scan.txt", getFilesDir().toString()); // Stores
																		// the
																		// most
																		// recent
																		// scan
																		// to be
																		// processed
																		// into
																		// Profiles
		try {
			use = userReader.reader("users.txt", getFilesDir().toString()); // Imports
																			// the
																			// users
																			// file
																			// if
																			// it
																			// exists
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		String[] t = new String[sc.size()]; // Sets up the array to display the
											// users in a list
		if (use != null && use.size() != 0) {
			int i, c;
			for (i = 0; i < sc.size(); i++) {
				int used = 0;
				for (c = 0; c < use.size(); c++) {
					if (sc.get(i).getMac().equals(use.get(c).getMac())) {
						// Checks to see if there is a match between a user and
						// Someone currently on the network
						// by checking each user against the list and adding
						// them if the next element is available or a match is
						// made.
						t[i] = "Name: " + use.get(c).getName() + "\n"
								+ "MacAdd: " + use.get(c).getMac();
						used++;
					}
				}
				if (used == 0) // If there is no match then the generic user is
								// added to
					t[i] = "New User on Network" + "\n" + "MacAdd: " // the list
																		// for
																		// editing
							+ sc.get(i).getMac();

			}

		} else {
			for (int i = 0; i < sc.size(); i++) { // If there are no stored
													// profiles then the generic
													// is added to the list
				t[i] = "New User on Network" + "\n" + "MacAdd: "
						+ sc.get(i).getMac();
			}
		}

		// The correctly scanned items are added to the ListView
		adt1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, t);
		setListAdapter(adt1);

		ListView lv = getListView();

		// Handles the screen change and information transfer when a profile is
		// selected
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String profiler = ((TextView) view).getText().toString();
				// Parses the elements display and manipulates it so
				// The MAC address is sent (with its label) to the
				// ProfileActivity
				String[] profiled = profiler.split("\n");
				String profile = profiled[1];
				Intent i = new Intent(getApplicationContext(),
						ProfileActivity.class);
				i.putExtra("profile", profile);
				startActivity(i);

			}
		});
	}

}