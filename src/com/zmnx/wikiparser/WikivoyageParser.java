package com.zmnx.wikiparser;

import java.io.*;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zmnx.wikiparser.PhantomJS;
import com.zmnx.wikiparser.CityDocParser;

/**
 * Make a CitiesList From Country Document of WikiVoyage
 * 
 * @author kangdongho
 *
 */

public class WikivoyageParser {

	public static String[] nations = { "USA" };
	public static ArrayList<String> citiesList = new ArrayList<String>();

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String input = br.readLine();

			// nation Doc Parsing
			if (input.compareTo("s") == 0) {
				for (int i = 0; i < nations.length; i++) {
					PhantomJS.runPhantomJS(nations[i], "WikivoyageParser");
				}

				parseNationDoc();
			}

			// city Doc Parsing
			for (String city : citiesList) {
				CityDocParser.parseCityDoc(city);
			}
		} catch (IOException e) {
			System.out.println("IOException : " + e.toString());
		} catch (InterruptedException e) {
			System.out.println("InterruptedException : " + e.toString());
		}
	}

	public static void parseNationDoc() throws IOException {

		for (String nation : nations) {

			File inputFile = new File("./html/" + nation + ".html");
			Document doc = Jsoup.parse(inputFile, "UTF-8",
					"https://en.wikivoyage.org/");

			Element citiesElement = doc.select("[id^=Cities]").first();
			Elements citiesElements = citiesElement.parent().parent()
					.select("ul > li > a");

			System.out.println("\nCities of " + nation + "...");
			System.out.println("===========================================");

			for (Element e : citiesElements) {

				String relURL = e.attr("href");
				String city = relURL.split("/")[2];

				System.out.println(city);
				citiesList.add(city);
			}

			System.out.println();
		}
	}
}
