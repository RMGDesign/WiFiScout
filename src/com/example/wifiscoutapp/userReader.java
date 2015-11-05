package com.example.wifiscoutapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class userReader {

	public static ArrayList<User> reader(String fName, String dir) {
		ArrayList<User> users = new ArrayList<User>();
		File used;

		used = new File(dir, fName);
		// Read in lines of strings and use the formatting to break down the
		// lines
		// into User components, create the Users and then add them to an
		// ArrayList to be imported.
		try {
			BufferedReader br = new BufferedReader(new FileReader(used));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("~");
				users.add(new User(splitted[3], splitted[1], splitted[2],
						splitted[4], splitted[0]));
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}
}
