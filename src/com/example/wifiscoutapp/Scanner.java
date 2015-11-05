package com.example.wifiscoutapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Scanner {

	public static void ping() {

		String ipAdd = null;
		new ArrayList<User>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp")); // Parses
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
			String line; // the host ip address
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("[  ]+");
				if (splitted != null && splitted.length >= 4) {

					if (!splitted[3].equals("00:00:00:00:00:00")
							&& !splitted[3].equals("type")) {
						ipAdd = splitted[0];
						break;
					}

				}

			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String splitter[] = ipAdd.split("\\."); // Take the Host Address and
												// manipulate it to become the
												// broadcast address
		String broadcast = splitter[0] + "." + splitter[1] + "." + splitter[2]
				+ "." + "255";
		String pingComm = "ping -b " + broadcast; // Ping the broadcast address
													// using a UNIX command so
													// every device responds
		try { // and sends it's MAC Address to the ARP Table
			Runtime.getRuntime().exec(pingComm);
		} catch (IOException e) {
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
