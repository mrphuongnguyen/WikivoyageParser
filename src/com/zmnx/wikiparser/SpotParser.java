package com.zmnx.wikiparser;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpotParser {

	public String docURL;

	public SpotParser(String docURL) {
		this.docURL = docURL;
	}

	public ArrayList<String> makeSpotList() throws IOException {

		String url = docURL;
		// System.out.println("Fetching " + url + "...");

		Document doc = Jsoup.connect(url).get();

		ArrayList<String> spotList = new ArrayList<String>();

		if (doc.select("[id^=See]").size() != 0) {
			Element seeElement = doc.select("[id^=See]").get(0);
			Element e = seeElement.parent();

			while (true) {

				e = e.nextElementSibling();
				// System.out.println(e.html());
				// System.out.println();

				if (e.getElementById("Eat") != null) {
					// System.out.println("Eat");
					break;
				} else if (e.getElementById("Drink") != null) {
					// System.out.println("Drink");
					break;
				} else if (e.getElementById("Sleep") != null) {
					// System.out.println("Sleep");
					break;
				}

				Elements spots = e.select("[class=fn org listing-name]");

				for (Element e2 : spots) {

					String spotTitle;

					if (e2.children().size() == 0)
						spotTitle = e2.ownText();
					else
						spotTitle = e2.child(0).ownText();

					// System.out.println("spot : " + spotTitle);
					spotList.add(spotTitle);
				}
			}
		}

		return spotList;
	}
}
