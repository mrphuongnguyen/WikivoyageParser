package com.zmnx.wikiparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpotParser {

	private String docName;

	public SpotParser(String docName) {
		this.docName = docName;
	}

	public ArrayList<String> makeSpotList() throws IOException {

		File inputFile = new File("./html/" + docName + ".html");
		Document doc = Jsoup.parse(inputFile, "UTF-8",
				"https://en.wikivoyage.org/");

		System.out.println("makeSpotList for " + docName + "...");
		System.out.println("===========================================");

		ArrayList<String> spotList = new ArrayList<String>();

		Element seeElement = doc.select("[id^=See]").first();
		Elements seeSpots;

		if (seeElement != null) {
			seeSpots = seeElement.parent().parent()
					.select("[class^=fn org listing]");

			for (Element e : seeSpots) {
				spotList.add(spotExtract(e));
			}
		}

		Element doElement = doc.select("[id^=Do]").first();
		Elements doSpots;

		if (doElement != null) {
			doSpots = doElement.parent().parent()
					.select("[class^=fn org listing]");

			for (Element e : doSpots) {
				spotList.add(spotExtract(e));
			}
		}

		Element buyElement = doc.select("[id^=Buy]").first();
		Elements buySpots;

		if (buyElement != null) {
			buySpots = buyElement.parent().parent()
					.select("[class^=fn org listing]");

			for (Element e : buySpots) {
				spotList.add(spotExtract(e));
			}
		}

		return spotList;
	}

	public String spotExtract(Element e) {
		
		String spot;
		
		if(e.select("b").size() != 0)
			spot = e.select("b").first().ownText();
		else
			spot = e.ownText(); 
		
		//System.out.println(spot);
		return spot;
	}
}
