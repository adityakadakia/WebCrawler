package edu.neu.cs6200.WebCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	
	static final int MAX_DEPTH = 5;
	
	static ArrayList<Link> toVisit = new ArrayList<Link>();
	static ArrayList<Link> visited = new ArrayList<Link>();
	static int i;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		 
		final long POLITE = 1000;
		
		final Link SEED = new Link(
				"https://en.wikipedia.org/wiki/Hugh_of_Saint-Cher", 0);
		long startTime, endTime, execTime;
		Document doc;
		Elements links;
		
		i = 0;
		toVisit.add(SEED);
		while ((!toVisit.isEmpty()) && (visited.size() < 1000)) {
		
			startTime = new Date().getTime();
			Link node = toVisit.get(0);
			
			if (!visited.contains(node))
			while (true) {
				try {
					doc = Jsoup.connect(node.getUrl()).get();
					links = doc.select("a[href]");
					for (Element link : links) {
						filter(new Link(link.absUrl("href"),
								(node.getDistance() + 1)));
					}
					visited.add(node);
					System.out.println("toVisit: " + toVisit.size() + "  Visited: " + visited.size() + "  Distance: " + node.getDistance() + " " + toVisit.get(0).getUrl() );
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			toVisit.remove(0);
			
			endTime = new Date().getTime();
			execTime = endTime - startTime;
			//if ( execTime < POLITE ) Thread.sleep( POLITE - execTime );
				
		}
		
		PrintWriter writer = new PrintWriter("visited.txt", "UTF-8");
		System.out.println("Visted: " + visited.size());
		System.out.println("toVist: " + toVisit.size());
		for(Link l : visited){
			writer.println(l.getUrl());
		}
		writer.close();
	}

	public static void filter(Link link) {
		boolean c1, c2, c3, c4, c5;
		
		link.setUrl(link.getUrl().split("#")[0]);

		c1 = link.getUrl().toLowerCase().contains("https://en.wikipedia.org/wiki/".toLowerCase());

		if ((link.getUrl().length() - link.getUrl().replaceAll(":", "")
				.length()) == 1)
			c2 = true;
		else
			c2 = false;

		if (link.getUrl().toLowerCase().contains("https://en.wikipedia.org/wiki/Main_Page".toLowerCase()))
			c3 = false;
		else
			c3 = true;

//		if ((!toVisit.contains(link)) && (!visited.contains(link)))
//			c4 = true;
//		else
//			c4 = false;
		c4 = true;
		
		if (link.getDistance() <= MAX_DEPTH )
			c5 = true;
		else
			c5 = false;

		if (c1 && c2 && c3 && c4) {
			i++;
//			System.out.println(i + " " + c1 + " " + c2 + " " + c3 + " " + c4 + link.getUrl());
			toVisit.add(link);
		}
	}
}
