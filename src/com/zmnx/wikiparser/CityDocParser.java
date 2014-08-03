package com.zmnx.wikiparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Stack;

import com.zmnx.wikiparser.PhantomJS;
import com.zmnx.wikiparser.SpotParser;

public class CityDocParser {

	public static void parseCityDoc(String input) {

		try {

			Stack<String> docStack = new Stack<String>();

			docStack.push(input);

			// TextFile
			File file = new File("./spots/" + input + ".txt");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			// DFS : Finding All 'RegionDocs'
			while (!docStack.empty()) {

				String docPath = docStack.pop();
				PhantomJS.runPhantomJS(docPath, "WikivoyageParser");

				ArrayList<String> districtElements = makeDistrictList(docPath);

				if (districtElements.size() != 0) {
					// found district elements

					for (String district : districtElements) {

						System.out.println("found district : " + district);
						docStack.push(district);
					}

					System.out
							.println("===========================================");
				}

				else {

					// not found district elements (Lowest Doc)
					SpotParser spotParser = new SpotParser(docPath);
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
			System.out.println("IOException : " + e.toString());
		} catch (InterruptedException e) {
			System.out.println("InterruptedException : " + e.toString());
		}

	}

	/**
	 * method which parse a city documentation, and returns regionList of city.
	 * 
	 * @param city
	 * @return
	 * @throws IOException
	 */

	public static ArrayList<String> makeDistrictList(String docName)
			throws IOException, InterruptedException {

		File inputFile = new File("./html/" + docName + ".html");
		Document doc = Jsoup.parse(inputFile, "UTF-8",
				"https://en.wikivoyage.org/");

		Elements regionElements = doc.select("#region_list");
		ArrayList<String> regionList = new ArrayList<String>();

		if (regionElements.size() != 0) {
			// id region_list exists

			for (Element e : regionElements) {

				// 수정요
				Elements regions = e.select("table > tbody > tr > td > b > a");

				for (Element e2 : regions) {

					String URL = e2.attr("href");

					if (URL.contains("/wiki/")) {
						String district = URL.replaceFirst("/wiki/", "");

						//System.out.println("district : " + district);
						regionList.add(district);
					}
				}
			}
		}

		else {
			// id region_list doesn't exit

			if (doc.select("#Districts").size() != 0) {
				Element districtElement = doc.select("#Districts").get(0);
				Elements districElements = districtElement.parent().parent()
						.select("ul > li > b > a");

				for (Element e : districElements) {
					String URL = e.attr("href");
					String district = URL.replaceFirst("/wiki/", "");

					//System.out.println("district : " + district);
					regionList.add(district);
				}
			}
		}

		return regionList;
	}
}
