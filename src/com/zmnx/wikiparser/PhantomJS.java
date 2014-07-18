package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhantomJS {

	public static void runPhantomJS(String input) throws InterruptedException,
			IOException {
		
		// edit path for phantomJS (command in terminal, "which phantomjs") 
		Process process = Runtime.getRuntime().exec(
				"/usr/local/bin/phantomjs ./src/WikivoyageParser.js " + input);

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br2 = new BufferedReader(isr);
		String line;

		System.out.println("===========================================");
		System.out.println("Running phantomJS for " + input + "...");

		while ((line = br2.readLine()) != null) {
			//System.out.println(line);
		}

		// Wait for the process to complete and the images to be
		// generated
		process.waitFor();
		System.out.println("Exit phantomJS for " + input + "...");
		System.out.println("===========================================");
	}
}
