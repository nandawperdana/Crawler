package com.nandanu.crawler.model;

public class Barang {
	private String title, link, price, category, image, crawl;

	public Barang() {
	}

	public Barang(String title, String link, String price, String category,
			String image) {
		super();
		this.title = title;
		this.link = link;
		this.price = price;
		this.category = category;
		this.image = image;
		this.crawl = "Bukalapak";
	}

	public Barang(String title, String link, String price, String category,
			String image, String crawl) {
		super();
		this.title = title;
		this.link = link;
		this.price = price;
		this.category = category;
		this.image = image;
		this.crawl = crawl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCrawl() {
		return crawl;
	}

	public void setCrawl(String crawl) {
		this.crawl = crawl;
	}

}
