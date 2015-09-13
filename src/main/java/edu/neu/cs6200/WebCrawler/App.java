package edu.neu.cs6200.WebCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	static ArrayList<Link> toVisit = new ArrayList<Link>();
	static ArrayList<Link> visited = new ArrayList<Link>();
	static int i;
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		i = 0; 
		Document doc;
		Elements links;
		Link start = new Link(
				"http://en.wikipedia.org/wiki/Hugh_of_Saint-Cher", 0);
		toVisit.add(start);
		while ((!toVisit.isEmpty()) && (visited.size() < 1000)) {
			Link node = toVisit.get(0);
			if (node.getDistance() < 5 && toVisit.size()<1000) {
				try {
					doc = Jsoup.connect(node.getUrl()).get();
					links = doc.select("a[href]");
					for (Element link : links) {
						filter(new Link(link.absUrl("href"),
								(node.getDistance() + 1)));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			visited.add(toVisit.get(0));
			
			System.out.println("toVisit: " + toVisit.size() + "  Visited: " + visited.size() + "  Distance: " + node.getDistance() + " " + toVisit.get(0).getUrl() );
			toVisit.remove(0);
		}
		
		PrintWriter writer = new PrintWriter("visited.txt", "UTF-8");
		System.out.println(visited.size());
		for(Link l : visited){
			writer.println(l.getUrl());
		}
		writer.close();
	}

	public static void filter(Link link) {
		boolean c1, c2, c3, c4;

		c1 = link.getUrl().contains("://en.wikipedia.org/wiki/");

		if ((link.getUrl().length() - link.getUrl().replaceAll(":", "")
				.length()) == 1)
			c2 = true;
		else
			c2 = false;

		if (link.getUrl().contains("://en.wikipedia.org/wiki/Main_Page"))
			c3 = false;
		else
			c3 = true;

		if ((!toVisit.contains(link)) && (!visited.contains(link)))
			c4 = true;
		else
			c4 = false;

		if (c1 && c2 && c3 && c4) {
			i++;
			//System.out.println(i + " " + c1 + " " + c2 + " " + c3 + " " + c4 + link.getUrl());
			toVisit.add(link);
		}
	}
}
