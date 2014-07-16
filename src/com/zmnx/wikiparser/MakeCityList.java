package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Make a CitiesList From Country Document of WikiVoyager
 * 
 * @author kangdongho
 *
 */

public class MakeCityList {

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String input = br.readLine();

			if (input.compareTo("s") == 0) {

				String[] nations = { "South_Korea", "France", "USA" };

				for (int i = 0; i < nations.length; i++) {
					Process process = Runtime
							.getRuntime()
							.exec("/usr/local/bin/phantomjs /Users/kangdongho/Dev/EclipseWorkspace/WikivoyageParser/src/HTMLParser.js "
									+ nations[i]);

					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br2 = new BufferedReader(isr);
					String line;
					
					System.out.println("Running phantomJS for " + nations[i] + "...");
					System.out.println("===========================================");
					
					while ((line = br2.readLine()) != null) {
						System.out.println(line);
					}
					
					// Wait for the process to complete and the images to be
					// generated
					process.waitFor();
					System.out.println("\nExit phantomJS for " + nations[i] + "...\n");
				}
			}
		} catch (IOException e) {
			System.out.println("error : " + e.toString());
		} catch (InterruptedException e) {
			System.out.println("errir : " + e.toString());
		}
	}

	public static void startParse() throws IOException {

		File inputFile = new File("input.html");
		Document doc = Jsoup.parse(inputFile, "UTF-8",
				"https://en.wikivoyage.org/");

		Element citiesElement = doc.select("[id^=Cities]").first();
		Elements citiesElements = citiesElement.parent().parent()
				.select("ul > li > a");

		for (Element e : citiesElements) {
			// System.out.println(e.attr("href"));
		}

	}
}
