package com.example.wifiscoutapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Scanner {

	public static void ping() {

		String ipAdd = null;
		new ArrayList<User>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line; // the host ip address
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("[  ]+");
				if (splitted != null && splitted.length >= 4) {
					System.out.println(line);
					if (!splitted[3].equals("00:00:00:00:00:00")
							&& !splitted[3].equals("type")) {
						ipAdd = splitted[0];
						break;
					}

				}

			}
		} catch (Exception e1) {

			e1.printStackTrace();
		} // Parses
			// the
			// first
			// IP
			// address
			// on
			// the
			// arp
			// table
			// to
			// get

		String splitter[] = ipAdd.split("\\."); // Take the Host Address and
												// manipulate it to become the
												// broadcast address
		String broadcast = splitter[0] + "." + splitter[1] + "." + "135" + "."
				+ "255";
		String pingComm = "ping -c 1 -b " + broadcast; // Ping the
														// broadcast
														// address
		// using a UNIX command so
		// every device responds
		System.out.println(pingComm);

		Runtime r = Runtime.getRuntime();
		try {
			Process process = r.exec(pingComm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<User> getScanData() { // After the ARP table is
													// filled by the broadcast
													// ping, parse the table and
		String mac = null; // create generic users to be shown on the main scan
							// activity
		String ipAdd = null;
		ArrayList<User> scanned = new ArrayList<User>();
		BufferedReader br = null;
		ping();
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;

			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("[  ]+");
				if (splitted != null && splitted.length >= 4) {

					if (!splitted[3].equals("00:00:00:00:00:00")
							&& !splitted[3].equals("type")) {
						System.out.println(line);
						ipAdd = splitted[0];
						mac = splitted[3];
						scanned.add(new User(mac, ipAdd));
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return scanned;
	}
}
