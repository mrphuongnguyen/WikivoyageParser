package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikivoyageParser {

	public static ArrayList<String> cityList = new ArrayList<String>();

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String input = br.readLine();

			if (input.compareTo("s") == 0) {
				startParse();
			}

		} catch (IOException e) {
			System.out.println("error : " + e.toString());
		}
	}

	public static void startParse() {

		Stack<String> docStack = new Stack<String>();

		// push Continental Doc
		//docStack.push("/wiki/South_Korea");
		//docStack.push("/wiki/Japan");
		//docStack.push("/wiki/China");
		//docStack.push("/wiki/United_Kingdom");
		docStack.push("/wiki/United_States_of_America");
		//docStack.push("/wiki/France");

		try {
			while (!docStack.empty()) {

				String regionURL = docStack.pop();
				ArrayList<String> elements = makeList(regionURL);

				if (elements.size() != 0) {

					for (String URL : elements) {

						Document doc = Jsoup.connect(
								"http://en.wikivoyage.org" + URL).get();

						if (doc.select("[id^=Cities]").size() != 0) {
							System.out.println("region : " + URL);
							docStack.push(URL);
						} else {
							System.out.println("city : " + URL);
							cityList.add(URL);
						}
					}
				}
				else {
					// RegionDocs (Lowest Doc)
					Document doc = Jsoup.connect(
							"http://en.wikivoyage.org" + regionURL).get();

					if (doc.select("[id^=Cities]").size() != 0) {
						Element seeElement = doc.select("[id^=Cities]").get(0);
						Element e = seeElement.parent();
						
						while (true) {

							e = e.nextElementSibling();
							
							// System.out.println(e.html());
							// System.out.println();

//							if (e.getElementById("Other_Destinations") != null) {
//								break;
//							} else if (e.getElementById("Understand") != null) {
//								break;
//							} else if (e.getElementById("Islands") != null) {
//								break;
//							} else if (e.getElementById("Other_attractions") != null) {
//								break;
//							} else if (e.getElementById("Other_destinations") != null) {
//								break;
//							} else if (e.getElementById("Get_in") != null) {
//								break;
//							}
							
							if (e.getElementById("Other_Destinations") != null) {
								break;
							}

							Elements cities = e.getElementsByTag("li");

							for (Element e2 : cities) {

								if (e2.getElementsByTag("a").size() != 0) {
									Element city = e2.getElementsByTag("a")
											.first();

									if (city.attr("href").startsWith("/wiki/")) {
										String cityURL = city.attr("href");

										System.out.println("city : " + cityURL);
										cityList.add(cityURL);
									}
								}
							}
						}
					}
				}
			}
			
			makeTextFile();
			
		} catch (IOException e) {
			System.out.println("error : " + e.toString());
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

		return regionList;
	}

	public static void makeTextFile() throws IOException {
		// TextFile
		File file = new File("cityList.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for (String city : cityList) {
			bw.write(city);
			bw.newLine();
		}
		
		bw.close();
	}
}
