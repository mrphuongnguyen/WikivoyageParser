package com.zmnx.wikiparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.*;

public class QueryTest {

	public static void main(String[] args) {

		while (true) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));

				String input = br.readLine();

				// nation Doc Parsing
				if (input.compareTo("q") == 0) {
					break;
				}

				String searchQuery = "https://www.googleapis.com/customsearch/v1?q=site:+en.wikipedia.org+"
						+ input
						+ "&ie=UTF-8&oe=UTF-8&cx=009090225724931508849:o2ygj2-ocyi&key=AIzaSyD5jwBCx7L_COq12N7z2PHFJ6ZtVWsiNk0";

				getHttps(searchQuery);

			} catch (IOException e) {
				System.out.println("IOException : " + e.toString());
			} catch (NoSuchAlgorithmException e) {
				System.out.println("NoSuchAlgorithmException : " + e.toString());
			} catch (KeyManagementException e) {
				System.out.println("KeyManagementException : " + e.toString());
			}
		}
	}

	/**
	 * 
	 * @param urlString
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void getHttps(String urlString) throws IOException,
			NoSuchAlgorithmException, KeyManagementException {

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
		while ((line = reader.readLine()) != null) {
			System.out.printf("%s\n", line);
		}

		reader.close();
	}
}
