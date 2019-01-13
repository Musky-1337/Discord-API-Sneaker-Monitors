package com.bayanrasooly;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
import java.util.*;
//made by Bayan Rasooly
public class NikeBackendAPIMonitor {
    static JSONObject keeper;
    final static String[] proxies = {
        "35.190.137.55:3128:rz8smjts:m7NiZm0FGW",
        "35.190.137.55:3128:rz8smjts:m7NiZm0FGW"//ip:host you can add more
    };
    final static String[] DEFAULT_USER_AGENTS = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/65.0.3325.181 Chrome/65.0.3325.181 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 7.0; Moto G (5) Build/NPPS25.137-93-8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B554a Safari/9537.53",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0"};

    static String ENDPOINT_USA0 = "https://api.nike.com/snkrs/content/v1/?&country=US&language=en&offset=0&orderBy=published";
    static String ENDPOINT_USA1 = "https://api.nike.com/snkrs/content/v1/?&country=US&language=en&offset=100&orderBy=published";
    static String ENDPOINT_USA2 = "https://api.nike.com/snkrs/content/v1/?&country=US&language=en&offset=200&orderBy=published";
    static String ENDPOINT_USA3 = "https://api.nike.com/snkrs/content/v1/?&country=US&language=en&offset=300&orderBy=published";

    public static void main(String [] args) throws IOException{
        Connection SITE;
        Connection SITE1;
        while(true)
            try{
            SITE = Jsoup.connect(ENDPOINT_USA0)
                    .userAgent(DEFAULT_USER_AGENTS[(int) (Math.random() * 7)])
                    .ignoreContentType(true);
            Document raw = SITE.get();
            String rawJSON = raw.toString();
            rawJSON = rawJSON.substring(rawJSON.indexOf("{"));
            //System.out.println(rawJSON);
            JSONObject jsonObj = new JSONObject(rawJSON);
            //System.out.println(jsonObj.toString(1));

            //JSONObject cells = (JSONObject) jsonObj.get("threads");
            //System.out.println(cells.toString());
            JSONArray hold = (JSONArray) jsonObj.get("threads");
            keeper = hold.getJSONObject(0);
            Scanner x = new Scanner(System.in);
            System.out.println("Do you want to debug last 25 nike items? (Yes/No)");
            String wxy = x.next();
            try{
            if(wxy.equalsIgnoreCase("yes")){
                for(int i = 0; i < 25; i++){
                    connect(proxies[(int)(Math.random()*proxies.length)]);
                    postNike(hold.getJSONObject(i));}}}
            catch(Throwable t){}

            while(true){try {
                connect(proxies[(int)(Math.random()*proxies.length)]);
                SITE = Jsoup.connect(ENDPOINT_USA0)
                        .userAgent(DEFAULT_USER_AGENTS[(int) (Math.random() * 7)])
                        .ignoreContentType(true);
                raw = SITE.get();
                rawJSON = raw.toString();
                rawJSON = rawJSON.substring(rawJSON.indexOf("{"));
                //System.out.println(rawJSON);
                jsonObj = new JSONObject(rawJSON);
                //System.out.println(jsonObj.toString(1));
                hold = (JSONArray) jsonObj.get("threads");
                //JSONObject cells = (JSONObject) jsonObj.get("threads");
                //System.out.println(cells.toString());
                //System.out.println(hold);
                //System.out.println(hold.getJSONObject(0));
                System.out.println("not equal");
                if(!keeper.toString().equals(hold.getJSONObject(0).toString())){
                    postNike(hold.getJSONObject(0));
                    keeper = hold.getJSONObject(0);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            catch(Throwable t){
                System.out.println("are we stuck... smfh");
            }
        }
    }
    catch(Throwable t){}}
        //Iterator<Object> iterator = cells.iterator();

    public static String getTime(){
        String timeStamp = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
        String newTime = "["+timeStamp.substring(0,8)+"]";
        return newTime;
    }
    public static void postNike(JSONObject object) throws IOException {
        //System.out.println(object.toString(1));
        JSONObject pic = new JSONObject();
        JSONObject obj;
        JSONObject embed;
        JSONArray arr = new JSONArray();
        JSONObject author =  new JSONObject();
        int publishtype = object.toString().indexOf("\"publishType\":");
        String halfpub = object.toString().substring(publishtype+8);
        author.put("text","Snkrs! " + " " + halfpub.substring(0,halfpub.indexOf("\",")).replaceAll("ug","").replaceAll("\"", "") + " ");
        author.put("url", "https://www.nike.com/launch");
        author.put("icon_url", "https://is5-ssl.mzstatic.com/image/thumb/Purple128/v4/61/e4/d3/61e4d341-9f5f-7db2-0826-53b557f4979c/SNKRS-AppIcon-0-1x_U007emarketing-0-0-GLES2_U002c0-512MB-sRGB-0-0-0-85-220-0-0-0-10.png/246x0w.jpg");
        String DiscordWebHook = "";

        String s = object.toString();
        String urlHold = s.substring(s.indexOf("\"url\":\"h")+7);
        String mainURL = urlHold.substring(0, urlHold.indexOf("\""));
        String s1 = new String(s);
        String imageURL = s1.substring(s1.indexOf("imageUrl")+11, s.length());
        String mainImageURL = imageURL.substring(0, imageURL.indexOf("\""));
        int i = 0;
        while(mainImageURL.equalsIgnoreCase("https://secure-images.nike.com/is/image/DotCom/999999_999")){
            try {
                s1 = s1.substring(s1.indexOf("imageUrl") + 11, s1.length());
                imageURL = s1.substring(s1.indexOf("imageUrl") + 11, s1.length());
                mainImageURL = imageURL.substring(0, imageURL.indexOf("\""));
            }
            catch(Throwable t){i=1;}
            if(i == 1)
                break;
        }
        pic.put("url", mainImageURL);
        int date = 10;
        if(object.toString().indexOf("startSellDate")!= -1)
            date = object.toString().indexOf("startSellDate")+16;
        else if(object.toString().indexOf("estimatedLaunchDate") != -1)
            date = object.toString().indexOf("estimatedLaunchDate")+22;
        else date = 0;
        //System.out.println(date);
        JSONObject a = new JSONObject();
        //System.out.println(object.toString().substring(date,date+19));
        JSONArray extend = new JSONArray("[\n" +
                "        {\n" +
                "          \"name\": \"Drop Date\",\n" +
                "          \"value\": \"" + object.toString().substring(date,date+11).replaceAll("T", " ") + "\",\n" +
                "          \"inline\": true\n" +
                "        }]");
        a.put("image", "https://c.static-nike.com/a/images/w_750,c_limit/cd7th0wwtejcceb4jzd1/image.jpg");
        embed =  new JSONObject();

        embed.put("title", "LINK: " + "https://www.nike.com/launch/t/" + object.getString("seoSlug"));
        //embed.put("header", "https://media.discordapp.net/attachments/514338083827875890/523573434006110209/cooo.jpg?width=300&height=300");
        embed.put("description", object.getString("seoSlug").replaceAll("-", " ").toUpperCase());
        embed.put("color","16754488");
        embed.put("footer", author);
        embed.put("icon_url", a);
        embed.put("image", pic);
        embed.put("fields", extend);
        arr.put(embed);
        obj = new JSONObject();
        obj.put("embeds", arr);
        obj.put("username", "SNKRS USA");
        obj.put("avatar_url", "");//add your picture here
        //System.out.println(obj);
        try{
        Document webhook = Jsoup.connect(DiscordWebHook)
                .requestBody(obj.toString())
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36")
                .post();}
        catch(Throwable t){postNike(object);}
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
}
