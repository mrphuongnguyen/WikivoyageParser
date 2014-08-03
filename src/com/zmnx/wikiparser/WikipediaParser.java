package com.zmnx.wikiparser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikipediaParser {

	private Document doc;
	
	public WikipediaParser(String file) {
		super();

		try {
			File inputFile = new File("./wikipediaHtml/" + file + ".html");
			doc = Jsoup.parse(inputFile, "UTF-8", "https://en.wikipedia.org/");

		} catch (IOException e) {
			System.out.println("IOException : " + e.toString());
		}
	}

	public String desWikiDoc() {

		String description = "";
		Elements pElements = doc.select("#mw-content-text > p");

		for (Element e : pElements) {

			if (e.ownText().isEmpty())
				continue;

			description = description + e.text();

			if (e.nextElementSibling() == null
					|| e.nextElementSibling().tagName().compareTo("p") != 0) {
				break;
			}
		}

		return description;
	}

	public String imgWikiDoc() {

		Elements imgElements = doc.select(
				"#mw-content-text > [class*=infobox] > a.image > img");
		
		if(imgElements.size() == 0) 
			return "";
		else {
			Element imgElement = imgElements.first();
			String imgURL = imgElement.attr("src");

			return imgURL;			
		}
	}
}
