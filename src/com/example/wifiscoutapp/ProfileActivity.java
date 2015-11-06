package com.example.wifiscoutapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ProfileActivity extends Activity {
	String userType;
	EditText nameDisp, notesDisp, macAdd, ipAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String message = intent.getStringExtra("profile");
		ArrayList<User> all = new ArrayList<User>();
		try {
			all = userReader.reader("users.txt", getFilesDir().toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		ArrayList<User> scanned = userReader.reader("scan.txt", getFilesDir()
				.toString());
		String[] fix = message.split(" "); // Manipulates data so only MAC
											// Address is available
		String profile = null;
		int stop = 0;
		if (!(all.isEmpty())) {
			for (int i = 0; i < all.size(); i++) { // Checks through the
													// ArrayLists
													// to find a match(the data
													// associated with the
													// profile)

				if (all.get(i).getMac().equals(fix[1])) {
					profile = all.get(i).toString();
					stop++;
				}
			}
		}
		for (int c = 0; c < scanned.size(); c++) {
			if (scanned.get(c).getMac().equals(fix[1]) && stop == 0) {
				profile = scanned.get(c).toString();
			}
		}
		String[] add = profile.split("~"); // Once data is associated from
											// storage it is converted into user
											// objects so it can
		String mac, ip, name, notes; // be displayed by the profile
		if (!add[0].equals("User")) {
			mac = add[1];
			ip = add[2];
			name = add[3];
			notes = add[4];
		} else {
			mac = add[1];
			ip = add[2];
			name = "Add a Nickname";
			notes = "Write Notes Here";
		}
		LinearLayout p = new LinearLayout(this);
		nameDisp = new EditText(this);
		notesDisp = new EditText(this);
		macAdd = new EditText(this);
		ipAdd = new EditText(this);
		Button save = new Button(this);
		final Button delete = new Button(this);
		p.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		Spinner auth = new Spinner(this);
		String[] items = { "User", "Authorized", "Unauthorized" };
		auth.setLayoutParams(textParams);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		auth.setAdapter(adapter);

		String current = add[0];

		for (int ind = 0; ind < items.length; ind++) {
			if (items[ind].equals(current)) {
				auth.setSelection(ind, false);
			}
		}

		nameDisp.setTextSize(24);
		notesDisp.setTextSize(24);
		macAdd.setTextSize(24);
		ipAdd.setTextSize(24);
		macAdd.setText(mac);
		ipAdd.setText(ip);
		macAdd.setKeyListener(null);
		ipAdd.setKeyListener(null);
		nameDisp.setText(name);
		notesDisp.setText(notes);
		save.setTextSize(24);
		save.setText("Save");
		delete.setTextSize(24);
		delete.setText("Delete Profile");

		p.addView(nameDisp, textParams);
		p.addView(macAdd, textParams);
		p.addView(ipAdd, textParams);
		p.addView(notesDisp, textParams);
		p.addView(auth);
		p.addView(save, buttonParams);
		p.addView(delete, buttonParams); // Created the view Programmatically

		save.setOnClickListener(new View.OnClickListener() { // When the save
			// button is
			// clicked the
			// current state
			// is taken and
			@Override
			// converted into an object to be stored in the users.txt file
			public void onClick(View view) {
				ArrayList<User> users = userReader.reader("users.txt",
						getFilesDir().toString());
				ArrayList<User> temp = userReader.reader("scan.txt",
						getFilesDir().toString());
				int check = 0;
				for (int i = 0; i < users.size(); i++) {
					if ((users.get(i).getMac()).equals(macAdd.getText()
							.toString())) {
						users.set(i, new User(nameDisp.getText().toString(),
								macAdd // Filter
										// out
										// any
										// signal
										// symbols
										// the
										// user
										// may
										// have
										// typed
										// in
								.getText().toString(), ipAdd.getText()
										.toString(), notesDisp.getText()
										.toString(), userType));
						System.out.println(users.get(i).toString() + "r");
						check++;
					}
				}
				if (check == 0) {
					users.add(new User(nameDisp.getText().toString(), macAdd
							.getText().toString(), ipAdd.getText().toString(),
							notesDisp.getText().toString(), userType));
				}
				userExport.exporter(users, "users.txt", getFilesDir() // Export
																		// the
																		// users
						.toString());
				userExport.exporter(temp, "scan.txt", getFilesDir().toString()); // Export
																					// the
																					// scan
																					// temp
																					// file
				Intent intent = new Intent(getApplicationContext(),
						UsersActivity.class);

				startActivity(intent); // Moved to Users List Activity
			}
		});
		delete.setOnClickListener(new View.OnClickListener() { // Deletes the
			// Profile with
			// a dialog to
			// make sure
			// removing the
			// match
			@Override
			// from the ArrayList to be exported
			public void onClick(View view) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						ProfileActivity.this).create();
				alertDialog.setTitle("Delete?");
				alertDialog
						.setMessage("Are you sure you want to delete this profile?");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								ArrayList<User> users = userReader.reader(
										"users.txt", getFilesDir().toString());
								ArrayList<User> temp = userReader.reader(
										"scan.txt", getFilesDir().toString());
								for (int i = 0; i < users.size(); i++) {
									if ((users.get(i).getMac()).equals(macAdd
											.getText().toString())) {
										users.remove(i);
										break;

									}
								}

								userExport.exporter(users, "users.txt",
										getFilesDir().toString());
								userExport.exporter(temp, "scan.txt",
										getFilesDir().toString());
								Intent intent = new Intent(
										getApplicationContext(),
										UsersActivity.class);

								startActivity(intent);
							}

						});
				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				alertDialog.show();

			}
		});
		auth.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				userType = (parent.getItemAtPosition(position)).toString();
				System.out.println(userType + "d");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		setContentView(p);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_main) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}