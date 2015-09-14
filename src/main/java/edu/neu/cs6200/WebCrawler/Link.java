package edu.neu.cs6200.WebCrawler;

public class Link {
	String url;
	int distance;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Link other = (Link) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.toLowerCase().equals(other.url.toLowerCase()))
			return false;
		return true;
	}
	public Link(String url, int distance) {
		super();
		this.url = url;
		this.distance = distance;
	}
	public Link() {
		super();
	}
	
	public static void main(String[] args){
		Link l1 = new Link ("hi", 0);
		Link l2 = new Link ("Hi", 2);
		
		System.out.println(l1.equals(l2));	
	}
}
