package com.zmnx.wikiparser;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.select.Evaluator.Class;

import java.io.IOException;

import java.util.List;

public class HtmlExtractor {

	public static void main(String[] args) throws IOException {

		// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		// String url = args[0];
		String url = "http://en.wikivoyage.org/wiki/Seoul/Jongno#See";
		System.out.println("Fetching " + url + "...");

		Document doc = Jsoup.connect(url).get();
		
		System.out.println(doc.html());
	}
}