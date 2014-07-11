package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Stack;

import com.zmnx.wikiparser.SpotParser;

public class CityParser {

	public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input;

		while (true) {
			try {

				input = br.readLine();
				Stack<String> docStack = new Stack<String>();

				if (input.compareTo("q") == 0)
					break;

//				ArrayList<String> regionList = makeList("/wiki/" + input);
//
//				if (regionList.size() != 0) {
//					for (String URL : regionList) {
//
//						System.out.println("region : " + URL);
//						docStack.push(URL);
//					}
//				}
				
				docStack.push("/wiki/" + input);


				// TextFile
				File file = new File(input + ".txt");
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				// DFS : Finding All 'RegionDocs'
				while (!docStack.empty()) {

					String regionURL = docStack.pop();
					ArrayList<String> elements = makeList(regionURL);

					if (elements.size() != 0) {

						for (String URL : elements) {

							System.out.println("region : " + URL);
							docStack.push(URL);
						}
					}

					else {
						// RegionDocs (Lowest Doc)
						SpotParser spotParser = new SpotParser(
								"http://en.wikivoyage.org" + regionURL);

						System.out.println("spot Listing...");

						ArrayList<String> spotList = spotParser.makeSpotList();

						// Listing Spots and Make a Text File
						for (String spotTitle : spotList) {
							System.out.println("spot : " + spotTitle);

							bw.write(spotTitle);
							bw.newLine();
						}
					}
				}

				bw.close();

			} catch (IOException e) {
				System.out.println("error : " + e.toString());
			}
		}
	}

	public static ArrayList<String> makeList(String relURL) throws IOException {

		String url = "http://en.wikivoyage.org" + relURL;
		System.out.println("Fetching " + url + "...");

		Document doc = Jsoup.connect(url).get();

		Elements regionElements = doc.select("#region_list");
		ArrayList<String> regionList = new ArrayList<String>();

		if (regionElements.size() != 0) {
			
			for (Element e : regionElements) {

				Elements regions = e.getElementsByTag("b");

				for (Element e2 : regions) {
					if (e2.children().size() != 0) {
						String URL = e2.child(0).attr("href");
												
						if (URL.contains("/wiki/")) {
							System.out.println("region : " + URL);
							regionList.add(URL);
						}
					}
				}
			}
		}

		else {

			if (doc.select("#Districts").size() != 0) {
				Element districtElement = doc.select("#Districts").get(0);
				Elements siblingElements = districtElement.parent()
						.siblingElements();

				for (Element e : siblingElements) {

					Elements districtElements = e.getElementsByTag("b");

					for (Element e2 : districtElements) {
						Elements districts = e2.getElementsByAttribute("href");

						for (Element e3 : districts) {

							String district = e3.attr("href");

							if (district.contains("/wiki/")) {
								System.out.println("region : " + district);
								regionList.add(district);
							}
						}
					}

					if (e.getElementById("Understand") != null) {
						// System.out.println("Eat");
						break;
					}
				}
			}
		}

		return regionList;
	}
}
