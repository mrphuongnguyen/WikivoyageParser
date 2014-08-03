package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.*;

import javax.net.ssl.*;

import com.eclipsesource.json.*;

public class SearchQuery {

	public static String spot;
	public static FileWriter fileWriter;
	public static BufferedWriter writer;

	// constant values.
	public static final String language = "en";
	public static final String key = "AIzaSyD5jwBCx7L_COq12N7z2PHFJ6ZtVWsiNk0";
	public static final String city = "NewYorkCity";
	public static final String readFileName = "New_York_City";

	public static void main(String[] args) {

		while (true) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));

				String input = br.readLine();

				// stop
				if (input.compareTo("q") == 0) {
					break;
				}

				// start
				else if (input.compareTo("s") == 0) {

					// text File Read (spot list)
					File readFile = new File("./spots/" + readFileName + ".txt");
					FileReader fileReader = new FileReader(readFile);
					BufferedReader reader = new BufferedReader(fileReader);
					String line = null;

					// parse and output
					File writeFile = new File("./" + city + ".txt");
					fileWriter = new FileWriter(writeFile);
					writer = new BufferedWriter(fileWriter);

					while ((line = reader.readLine()) != null) {

						writer.write(line);
						writer.newLine();
						writer.newLine();

						String searchJsonString = getHttps(searchQuery(line)); // Google
																				// SearchQuery
						parseSearch(searchJsonString);

						String placeJsonString = getHttps(placeQuery(line)); // Google
																				// PlaceQuery
						parsePlace(placeJsonString);
					}

					reader.close();
					writer.close();
				}

			} catch (IOException e) {
				System.out.println("IOException : " + e.toString());
			} catch (NoSuchAlgorithmException e) {
				System.out
						.println("NoSuchAlgorithmException : " + e.toString());
			} catch (KeyManagementException e) {
				System.out.println("KeyManagementException : " + e.toString());
			} catch (InterruptedException e) {
				System.out.println("InterruptedException : " + e.toString());
			}
		}
	}

	/**
	 * generate a searchQuery
	 * 
	 * @param line
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws InterruptedException
	 */
	public static String searchQuery(String line) throws IOException,
			NoSuchAlgorithmException, KeyManagementException,
			InterruptedException {

		// System.out.println("Wikipedia Parsing for " + line + "...");

		line = line.replaceAll(" ", "+");
		spot = line;

		String searchQuery = "https://www.googleapis.com/customsearch/v1?q=site:+en.wikipedia.org+"
				+ line
				+ city
				+ "&ie=UTF-8&oe=UTF-8&cx=009090225724931508849:o2ygj2-ocyi&key="
				+ key;

		return searchQuery;
	}

	/**
	 * generate a placeQuery
	 * 
	 * @param line
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws InterruptedException
	 */

	public static String placeQuery(String line) throws IOException,
			NoSuchAlgorithmException, KeyManagementException,
			InterruptedException {

		// System.out.println("GoogleMap API Json Parsing for " + line + "...");

		line = line.replaceAll(" ", "+");

		String placeQuery = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
				+ line + city + "&key=" + key + "&language=" + language;

		return placeQuery;
	}

	/**
	 * 
	 * @param urlString
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static String getHttps(String urlString) throws IOException,
			NoSuchAlgorithmException, KeyManagementException,
			InterruptedException {

		// Get HTTPS URL connection
		URL url = new URL(urlString);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// Set Hostname verification
		conn.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// Ignore host name verification. It always returns true.
				return true;
			}
		});

		// SSL setting
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, null, null); // No validation for now
		conn.setSSLSocketFactory(context.getSocketFactory());

		// Connect to host
		conn.connect();
		conn.setInstanceFollowRedirects(true);

		// Print response from host
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;

		String jsonToString = "";

		while ((line = reader.readLine()) != null) {

			jsonToString = jsonToString + line;
			// System.out.printf("%s\n", line);
		}

		reader.close();

		return jsonToString;
	}

	public static void parseSearch(String jsonToString) throws IOException,
			InterruptedException {
		JsonObject jsonObject = JsonObject.readFrom(jsonToString);
		JsonArray items = jsonObject.get("items").asArray();

		if (items.size() != 0) {

			JsonObject firstItem = items.get(0).asObject();
			JsonValue link = firstItem.get("link");

			String docUrl = link.asString();

			if (docUrl.contains("wikipedia")) {
				System.out
						.println("===========================================");
				System.out.println("parse wikipedia with " + docUrl + "...");
				PhantomJS.runPhantomJS(docUrl, "WikipediaParser", spot);

				WikipediaParser wikiParser = new WikipediaParser(spot);

				String description = wikiParser.desWikiDoc();
				String imgURL = wikiParser.imgWikiDoc();

				System.out.println("description : " + description);
				System.out.println("img : " + imgURL);

				writer.write(description);
				writer.newLine();
				writer.newLine();
				writer.write("imgURL : " + imgURL);
			}
		}
	}

	public static void parsePlace(String jsonToString) throws IOException,
			InterruptedException {

		JsonObject jsonObject = JsonObject.readFrom(jsonToString).asObject();

		if (jsonObject.get("results").asArray().size() != 0) {

			JsonObject resultObject = jsonObject.get("results").asArray()
					.get(0).asObject();

			JsonValue nameValue = resultObject.get("name");
			JsonValue addressValue = resultObject.get("formatted_address");
			JsonValue lonValue = resultObject.get("geometry").asObject()
					.get("location").asObject().get("lng");
			JsonValue latValue = resultObject.get("geometry").asObject()
					.get("location").asObject().get("lat");

			JsonArray typeArray = resultObject.get("types").asArray();

			String nameString = nameValue.asString();
			String addressString = addressValue.asString();
			String typeString = "";
			Double lonDouble = lonValue.asDouble();
			Double latDouble = latValue.asDouble();

			for (JsonValue o : typeArray) {
				if (spot.contains("station")) {
					// 역인 경우
					if (o.asString().contains("station"))
						typeString = o.asString();
				} else {
					// 역이 아닌 경우
					if (o.asString().contains("station"))
						continue;

					typeString = o.asString();
				}
			}

			System.out.println("loc_name : " + nameString);
			System.out.println("addressValue : " + addressString);
			System.out.println("typeValue : " + typeString);
			System.out.println("lonValue : " + lonDouble);
			System.out.println("latValue : " + latDouble);

			writer.write("loc_name : " + nameString);
			writer.newLine();
			writer.write("addressValue : " + addressString);
			writer.newLine();
			writer.write("typeValue : " + typeString);
			writer.newLine();
			writer.write("lonValue : " + lonDouble);
			writer.newLine();
			writer.write("latValue : " + latDouble);
			writer.newLine();
			writer.newLine();
		}
	}
}
