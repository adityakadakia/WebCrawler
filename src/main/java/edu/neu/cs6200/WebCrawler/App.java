package edu.neu.cs6200.WebCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	
	static final int MAX_DEPTH = 5;
	static final long POLITE = 1000;
	static final Link SEED = new Link(
			"https://en.wikipedia.org/wiki/Hugh_of_Saint-Cher", 1);
	
	static LinkedList<Link> toVisit = new LinkedList<Link>();
	static LinkedList<Link> visited = new LinkedList<Link>();
	static LinkedList<Link> keylinks = new LinkedList<Link>();	
	static int i;
	static long startTime, endTime, execTime;
	static Document doc;
	static Elements links;
	static String keyphrase;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		 
		keyphrase = "concordance";
		toVisit.add(SEED);
		if (keyphrase == null)
			unfocused();
		else {
			Link node = toVisit.get(0);
			while (true) {
				if (node.getDistance() < 5)
					try {
						doc = Jsoup.connect(node.getUrl()).get();
						links = doc.select("a[href]");
						for (Element link : links) {
							filter(new Link(link.absUrl("href"),
									(node.getDistance() + 1)));
						}
						visited.add(node);
						if (checkkeyphrase(doc)) {
							keylinks.add(node);
						}
						System.out.println("toVisit: " + toVisit.size()
								+ "  Visited: " + visited.size()
								+ "  Distance: " + node.getDistance() + " "
								+ "  keylinks: " + keylinks.size() + " "
								+ toVisit.get(0).getUrl());
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			toVisit.remove(0);
			focused();
		}
	}

	public static void focused() throws FileNotFoundException, UnsupportedEncodingException{

		while ((!toVisit.isEmpty()) && (keylinks.size() < 1000)) {
			
//			startTime = new Date().getTime();
			Link node = toVisit.get(0);
			
			while (true) {
				if (node.getDistance() <= 5)
					try {
						doc = Jsoup.connect(node.getUrl()).get();
						if (checkkeyphrase(doc)) {
							keylinks.add(node);
							if (node.getDistance() < 5) {
								links = doc.select("a[href]");
								for (Element link : links) {
									filter(new Link(link.absUrl("href"),
											(node.getDistance() + 1)));
								}
							}
							System.out.println("toVisit: " + toVisit.size()
									+ "  Visited: " + visited.size()
									+ "  Distance: " + node.getDistance() + " "
									+ "  keylinks: " + keylinks.size() + " "
									+ toVisit.get(0).getUrl());
						}
						visited.add(node);

						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			toVisit.remove(0);
			
//			endTime = new Date().getTime();
//			execTime = endTime - startTime;
//			if ( execTime < POLITE ) Thread.sleep( POLITE - execTime );
				
		}
		PrintWriter writer = new PrintWriter("keylinks.txt", "UTF-8");
		System.out.println("Visted: " + visited.size());
		System.out.println("toVisit: " + toVisit.size());
		System.out.println("keylinks: " + keylinks.size());
		for(Link l : keylinks){
			writer.println(l.getUrl());
		}
		writer.close();
	}
	
	public static boolean checkkeyphrase(Document doc){
		if (doc.getElementsContainingText(keyphrase).size() > 0)
			return true;
		else 
			return false;
	}
	
	public static void unfocused() throws FileNotFoundException, UnsupportedEncodingException{
		while ((!toVisit.isEmpty()) && (visited.size() < 1000)) {
			
			//startTime = new Date().getTime();
			Link node = toVisit.get(0);
			
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
			
			//endTime = new Date().getTime();
			//execTime = endTime - startTime;
			//if ( execTime < POLITE ) Thread.sleep( POLITE - execTime );
			
		}
		
		PrintWriter writer = new PrintWriter("visited.txt", "UTF-8");
		System.out.println("Visted: " + visited.size());
		System.out.println("toVisit: " + toVisit.size());
		for(Link l : visited){
			writer.println(l.getUrl());
		}
		writer.close();
	}
	
	public static void filter(Link link) {
		//boolean c1, c2, c3, c4, c5;
		
		link.setUrl(link.getUrl().split("#")[0]);

		if( link.getUrl().toLowerCase().contains("https://en.wikipedia.org/wiki/".toLowerCase()))	
			if ((link.getUrl().length() - link.getUrl().replaceAll(":", "").length()) == 1)
				if (!(link.getUrl().toLowerCase().contains("https://en.wikipedia.org/wiki/Main_Page".toLowerCase())))
					if (link.getDistance() <= MAX_DEPTH )
						if ((!toVisit.contains(link)) && (!visited.contains(link)))
							toVisit.add(link);
		}
	}
