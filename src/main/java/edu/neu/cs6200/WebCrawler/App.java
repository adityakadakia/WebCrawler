package edu.neu.cs6200.WebCrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {
	static ArrayList<Link> toVisit = new ArrayList<Link>();
	static ArrayList<Link> visited = new ArrayList<Link>();
	public static void main(String[] args) {
		Document doc;
		Elements links;
		Link start = new Link("http://en.wikipedia.org/wiki/Hugh_of_Saint-Cher", 0);
		try {
			doc = Jsoup.connect(start.getUrl()).get();
			links = doc.select("a[href]");
			for (Element link : links) {
				filter(new Link(link.absUrl("href"), 1));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Link url : toVisit)
			System.out.println(url.getUrl());
		
		System.out.println(toVisit.size());
	}
	
	public static void filter(Link link){
		boolean c1, c2, c3, c4;
		
		c1 = link.getUrl().contains("://en.wikipedia.org/wiki/");
		
		if ((link.getUrl().length() - link.getUrl().replaceAll(":", "").length()) == 1 )
			c2 = true;
		else
			c2 = false;
		
		if (link.getUrl().contains("://en.wikipedia.org/wiki/Main_Page"))
			c3 = false;
		else 
			c3 = true;
		
		if((!toVisit.contains(link)) && (!visited.contains(link)))
			c4 = true;
		else
			c4 = false;
		
		if (c1 && c2 && c3 && c4){
//			System.out.println(c1 + " " + c2 + " " + c3 + " " + c4 + link);
			toVisit.add(link);
		}
	}
}
