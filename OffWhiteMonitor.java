package com.bayanrasooly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.Reader;
import java.text.SimpleDateFormat;
//made by Bayan Rasooly
public class OffWhiteMonitor {
	final static String WEBSITEADDRESS = "https://www.off---white.com/en/GB/search?utf8=%E2%9C%93&q=nike";
	final static String WEBSITEADDRESS2 = "https://www.off---white.com/en/GB/search?utf8=%E2%9C%93&q=chuck";
	final static String WEBSITEADDRESS3 = "https://www.off---white.com/en/GB/search?utf8=%E2%9C%93&q=rimowa";
	final static String[] proxies =
			{"35.180.252.11:3128:m6fe4lga:t0m2pTI85G",
                    "35.180.32.62:3128:m6fe4lga:t0m2pTI85G",
                    "52.47.150.244:3128:m6fe4lga:t0m2pTI85G",
                    "35.180.234.251:3128:m6fe4lga:t0m2pTI85G",
                    "52.47.110.52:3128:m6fe4lga:t0m2pTI85G",
                    "35.180.113.108:3128:m6fe4lga:t0m2pTI85G",
                    "35.180.117.212:3128:m6fe4lga:t0m2pTI85G",
                    "35.180.186.14:3128:m6fe4lga:t0m2pTI85G"};//you need really good unbanned proxies for off-white

	final static String[] DEFAULT_USER_AGENTS = {
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/65.0.3325.181 Chrome/65.0.3325.181 Safari/537.36",
			"Mozilla/5.0 (Linux; Android 7.0; Moto G (5) Build/NPPS25.137-93-8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Mobile Safari/537.36",
			"Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B554a Safari/9537.53",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0",
			"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0"};
	static Queue<String> products = new LinkedList<String>();
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, LoginException {

        connect(proxies[(int) (Math.random() * proxies.length)]);
		String json = Jsoup.connect("https://www.off---white.com/en/GB/men/products/omng001f18d431409810.json").ignoreContentType(true).execute().body();
		//System.out.println(json);
		// Connect to the URL using java's native library

		Connection SITE = Jsoup.connect(WEBSITEADDRESS)
				.userAgent(DEFAULT_USER_AGENTS[(int) (Math.random() * 7)])
				.ignoreContentType(true);
		Connection SITE2 = Jsoup.connect(WEBSITEADDRESS2)
				.userAgent(DEFAULT_USER_AGENTS[(int) (Math.random() * 7)])
				.ignoreContentType(true);
		Connection SITE3 = Jsoup.connect(WEBSITEADDRESS3)
				.userAgent(DEFAULT_USER_AGENTS[(int) (Math.random() * 7)])
				.ignoreContentType(true);
		Scanner console = new Scanner(System.in);
		System.out.println("What would you like to monitor?");
		System.out.println("Enter \"all\" for all hyped items. " + "Enter \"nike\" for all nike collab items. " + "Enter \"rimowa\" for all suitcases. ");

		String tf = console.next();
        connect(proxies[(int) (Math.random() * proxies.length)]);
        try {
            TimeUnit.MILLISECONDS.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		if (tf.equalsIgnoreCase("all")) {
			linkScraper(SITE3);
			linkScraper(SITE2);
			linkScraper(SITE);
		}
		else if (tf.equalsIgnoreCase("rimowa")) {
			linkScraper(SITE3);
		}
		else{
			linkScraper(SITE);
			linkScraper(SITE2);
		}
		while(true) {
			connect(proxies[(int) (Math.random() * proxies.length)]);
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			if(!(products.isEmpty()) && !(sizeFinder() == null)) {
				try {
					sizeFinder();
				}
				catch(Throwable t){System.out.println("bumped into an error. retrying");}
			}
		}
	}
	public static void linkScraper(Connection connection) throws IOException{
        connect(proxies[(int) (Math.random() * proxies.length)]);
	    Document DOC = connection.get();
		String rawDoc = DOC.toString();

		while (rawDoc.indexOf("data-json-url") != -1) {
            //connect(proxies[(int) (Math.random() * proxies.length)]);
			rawDoc = rawDoc.substring(rawDoc.indexOf("data-json-url") + 7);
			products.add("https://www.off---white.com" + rawDoc.substring(rawDoc.indexOf("=") + 2, rawDoc.indexOf(".json")+5));
		}
		System.out.println(products);
	}
	public static void post(String url, String size) throws IOException {
		JSONObject obj;
		JSONObject embed;
		JSONArray arr = new JSONArray();
		String discordWebHook = "";//add yours here!!!
		embed =  new JSONObject();
		embed.put("title", url);
		embed.put("description", getTime()+" Restocked!! Sizes: [" + size);
		embed.put("color","16754488");
		arr.put(embed);
		obj = new JSONObject();
		obj.put("embeds", arr);
		Document webhook = Jsoup.connect(discordWebHook)
				.data("content",getTime())
				.requestBody(obj.toString())//https://discordapp.com/api/webhooks/523187769216860181/i37CP5jRNWQZaCA-VP_av83-9-n5AvqKmkUkAauqGZwgHeS5azmROvlz5QRWX6Of_Ngb
				.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36")
				.post();
	}
	public static String sizeFinder () throws IOException{

		try {
			TimeUnit.MILLISECONDS.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String address = products.peek();//product peek
        System.out.println(address);
		String json;
		try {
			json = Jsoup.connect(address).ignoreContentType(true).execute().body();
		}
		catch(HttpStatusException | SocketTimeoutException r){System.out.println("ran into an error with this link" +address); products.poll(); return "Proxy error encountered";}
		String out = findStock(json);
		if (out.length() > 1){
			post(address.substring(0,address.length()-5), out);
			System.out.println(address.substring(0,address.length()-5) + " - RESTOCK! [" + out);
			products.poll();
			return "Sizes in stock: [" + out;
		}
		else
			products.add(products.poll());
			System.out.println("still alive");
			return null;
	}
	public static String findStock(String s){
		if(s.indexOf("name\":\"") == -1) {
			return "]";
		}
		else {
			String s1 = s.substring(s.indexOf("name\":\"")+7);
			String size = s1.substring(0,s1.indexOf("\""));
			if(s1.indexOf("name\":\"") == -1)
				return size + findStock(s1);
			return size + " " + findStock(s1);
		}
	}
	public static void connect(String proxyd) {
		String ip = proxyd.substring(0,proxyd.indexOf(":"));
		proxyd = proxyd.substring(proxyd.indexOf(":")+1);
		String port = proxyd.substring(0,proxyd.indexOf(":"));
		proxyd = proxyd.substring(proxyd.indexOf(":")+1);
		String user = proxyd.substring(0,proxyd.indexOf(":"));
		proxyd = proxyd.substring(proxyd.indexOf(":")+1);
		System.out.println(ip);
		System.out.println(port);
		System.out.println(user);
		System.out.println(proxyd);
		System.getProperties().put("http.proxyHost", ip);
		System.getProperties().put("http.proxyPort", port);
		final String u = user;
		final String u1 = proxyd;
		Authenticator.setDefault(
			new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							u, u1.toCharArray());
				}
			}
		);
		System.getProperties().put("http.proxyUser", user);
		System.getProperties().put("http.proxyPassword", proxyd);
		System.getProperties().put("https.proxyHost", ip);
		System.getProperties().put("https.proxyPort", port);
		System.getProperties().put("https.proxyUser", user);
		System.getProperties().put("https.proxyPassword", proxyd);
	}
	public static String getTime(){
		String timeStamp = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
		String newTime = "["+timeStamp+"]";
		return newTime;
	}
}
