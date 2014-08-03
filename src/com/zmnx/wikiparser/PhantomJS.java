package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PhantomJS {

	public static void runPhantomJS(String doc, String jsName)
			throws InterruptedException, IOException {

		Process process = Runtime.getRuntime().exec(
				"/usr/local/bin/phantomjs ./src/" + jsName + ".js" + " " + doc);

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br2 = new BufferedReader(isr);
		String line;

		System.out.println("===========================================");
		System.out.println("Running phantomJS for " + doc + "...");

		while ((line = br2.readLine()) != null) {
			// System.out.println(line);
		}

		// Wait for the process to complete and the images to be
		// generated
		process.waitFor();
		System.out.println("Exit phantomJS for " + doc + "...");
		System.out.println("===========================================");

	}

	public static void runPhantomJS(String doc, String jsName, String file)
			throws InterruptedException, IOException {

		// edit path for phantomJS (command in terminal, "which phantomjs")
		Process process = Runtime.getRuntime().exec(
				"/usr/local/bin/phantomjs ./src/" + jsName + ".js" + " " + doc
						+ " " + file);

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br2 = new BufferedReader(isr);
		String line;

		System.out.println("===========================================");
		System.out.println("Running phantomJS for " + doc + "...");

		while ((line = br2.readLine()) != null) {
			// System.out.println(line);
		}

		// Wait for the process to complete and the images to be
		// generated
		process.waitFor();
		System.out.println("Exit phantomJS for " + doc + "...");
		System.out.println("===========================================");
	}
}
