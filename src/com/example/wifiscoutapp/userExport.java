package com.example.wifiscoutapp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class userExport {
	public static void exporter(ArrayList<User> users, String fName, String dir) {

		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(dir + "/" + fName));
			// output user.toString to create lines that are strings with
			// formatting markers
			// to be stored on a file until they need to be accessed again.

			for (int i = 0; i < users.size(); i++) {
				out.write(users.get(i).toString() + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
