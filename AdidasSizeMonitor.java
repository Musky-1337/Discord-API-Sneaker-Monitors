package com.bayanrasooly;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
//made by bayan rasooly!
public class AdidasSizeMonitor extends ListenerAdapter{
	static String PID;
	static String SITE;
	private static Listener lsn;
	final static String[] DEFAULT_USER_AGENTS = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/65.0.3325.181 Chrome/65.0.3325.181 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 7.0; Moto G (5) Build/NPPS25.137-93-8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11B554a Safari/9537.53",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0",
            "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0"};
	@SuppressWarnings("deprecation")
	public static void main(String [] args) throws IOException, LoginException{
		String half1 = "https://www.adidas.com/api/products/";
		String half2 = "/availability";
		Scanner console = new Scanner(System.in);
		lsn = new Listener();

		System.out.println("Enter the PID of what you like to monitor: \n");
		PID = console.next();
		SITE = half1 + PID + half2;
		Connection site1 = Jsoup.connect(SITE)
				.userAgent(DEFAULT_USER_AGENTS[(int)(Math.random()*7)])
				.ignoreContentType(true);

		final Document DOC = site1.get();
		//System.out.println(DOC);
		System.out.println("Checking item status of (SKU: " + PID + ")\n");

		System.out.println("Which sizes would you like to monitor? Enter \"ALL\" for all --- \"NO\" otherwise. Enter in sizes one at a time.");
		System.out.println();
		String ans = console.next();
		System.out.println();

		Deque<String> sizes = sizeBuilder(ans);
		
		System.out.println();
		System.out.println("Queue order: " + sizes);
		System.out.println();
		lsn.mod("MONITOR - STARTED");
		monitor(sizes);

	}
	public static Deque<String> sizeBuilder(String s){
		Scanner console1 = new Scanner(System.in);
		Deque<String> arr = new LinkedList();
		String size;

		if(s.equals("ALL")){
			System.out.println("Added sizes:");
			for(int numb = 8; numb <= 28; numb++){
				if(numb % 2 == 0)
					size = "" + numb / 2;
				else
					size = "" + numb / 2.0;
				System.out.println(" " + size + ", ");
				arr.addLast(size);
			}
		}
		else{
			System.out.println("Enter -1 when done.");
			int k = 1;
			while(true){
				System.out.print(k + ". Enter size : ");
				k++;
				size = console1.nextLine();
				if(size.equals("-1")){
					return arr;
				}
				else{
					arr.addLast(size);
				}
			}
		}
		lsn.mod(arr.toString());
		return arr;
	}
	public static void monitor(Deque<String> q) throws IOException{
		Connection site1 = Jsoup.connect(SITE)
								.userAgent(DEFAULT_USER_AGENTS[(int)(Math.random()*7)])
								.ignoreContentType(true);
		Scanner console = new Scanner(System.in);
		System.out.println("Enter your bot token.");
		String token = console.next();//"eg: E4ODkwMTIxNTI1ODU0MjE4.DuXVtQ.UhBKI_fldc9_n38HJzX8Wnc0AnM"

		try{
			JDA api = new JDABuilder(AccountType.BOT).setToken(token).buildAsync();
			api.addEventListener(lsn);
			
		}
		catch(Throwable t){}	
		Document DOC = site1.get();
		while(true){
			if(Math.random() < 0.20){
				site1 = Jsoup.connect(SITE)
						.userAgent(DEFAULT_USER_AGENTS[(int)(Math.random()*7)])
						.ignoreContentType(true);
				DOC = site1.get();
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(q.peek() == null || q.isEmpty()){
				lsn.mod("MONITOR ENDED. ALL SIZES HAVE BEEN RESTOCKED");
				return;
			}
			if(DOC.toString().indexOf("availability_status\":\"IN_STOCK\",\"size\":\"" + q.getFirst() + "\"") != -1){
				System.out.println("Size" + q.getFirst() + " is in stock. Size removed from search queue");
				lsn.mod("Size" + q.getFirst() + " is in stock. Size removed from search queue");
				q.remove();
			}
			else
				System.out.println(q.getFirst() + " - size OOS");
				q.addLast(q.poll());
		}
	}
	static class Listener extends ListenerAdapter{
		private int i = 0;
		private ArrayList<String> restock = new ArrayList();
		private MessageChannel chan;
		public void onMessageReceived(MessageReceivedEvent event){
			if(event.getAuthor().isBot())
				return;
			Message message = event.getMessage();
			String content = message.getContentRaw();
			MessageChannel channel = event.getChannel();
			chan = channel;
			if(message.getContentRaw().equals("!startbayan")){
				channel.sendMessage("Started Monitoring").queue();
				while(true){
					try {
						TimeUnit.MILLISECONDS.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!restock.isEmpty()){
						if(restock.get(0).indexOf("in stock") != -1){
							channel.sendMessage("@everyone" + restock.remove(0)).queue();
							channel.sendMessage("https://www.adidas.com/yeezy").queue();
						}
						else
							channel.sendMessage(restock.remove(0)).queue();
					}
				}
			}


		}
		public void mod(String s){
			restock.add(s);
		}
	}
}

