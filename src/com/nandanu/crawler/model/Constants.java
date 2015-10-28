package com.nandanu.crawler.model;

public class Constants {
	public static final String TAG_TITLE = "title";
	public static final String TAG_PRICE = "price";
	public static final String TAG_CAT = "category";
	public static final String TAG_LINK = "link";
	public static final String TAG_IMG = "image";
	public static final String TAG_SITE = "site";
	public static final String[] kategori = { "Semua Kategori", "Elektronik",
			"Fashion", "Handphone dan Gadget", "Hobi dan Olahraga",
			"Kantor dan Industri", "Komputer", "Otomotif dan Sepeda",
			"Rumah Tangga" };
	public static final String[] sort = { "Terbaru", "Termurah", "Termahal" };

	/**
	 * mengembalikan kategori search
	 * 
	 * @param kategori
	 * @return array, idx 0 = bukalapak, idx 1 = olx
	 */
	public static String[] getKategori(String kategori) {
		String[] kat = new String[2];
		String kategori_bukalapak = "https://www.bukalapak.com/", kategori_olx = "http://olx.co.id/";
		if (!kategori.equalsIgnoreCase("semua kategori")) {
			if (kategori.equalsIgnoreCase("elektronik")) {
				kategori_bukalapak += "c/elektronik";
				kategori_olx += "elektronik-gadget";
			} else if (kategori.equalsIgnoreCase("Fashion")) {
				kategori_bukalapak += "c/fashion";
				kategori_olx += "keperluan-pribadi";
			} else if (kategori.equalsIgnoreCase("Handphone dan Gadget")) {
				kategori_bukalapak += "c/handphone";
				kategori_olx += "elektronik-gadget/handphone";
			} else if (kategori.equalsIgnoreCase("Hobi dan Olahraga")) {
				kategori_bukalapak += "c/hobi";
				kategori_olx += "hobi-olahraga";
			} else if (kategori.equalsIgnoreCase("Kantor dan Industri")) {
				kategori_bukalapak += "c/perlengkapan-kantor";
				kategori_olx += "kantor-industri/peralatan-kantor";
			} else if (kategori.equalsIgnoreCase("Komputer")) {
				kategori_bukalapak += "c/komputer";
				kategori_olx += "elektronik-gadget/komputer";
			} else if (kategori.equalsIgnoreCase("Otomotif dan Sepeda")) {
				kategori_bukalapak += "c/sepeda";
				kategori_olx += "hobi-olahraga/sepeda-aksesoris";
			} else if (kategori.equalsIgnoreCase("Rumah Tangga")) {
				kategori_bukalapak += "c/rumah-tangga";
				kategori_olx += "rumah-tangga";
			}
		} else {
			kategori_bukalapak += "products";
			kategori_olx += "all-results";
		}
		kat[0] = kategori_bukalapak;
		kat[1] = kategori_olx;
		return kat;
	}

	/**
	 * mengembalikan sort method
	 * 
	 * @param sort
	 * @return idx 0 = bukalapak, idx 1 = olx
	 */
	public static String[] getSortResult(String sort) {
		String[] sortResult = new String[2];
		String bukalapak_sort = "", olx_sort = "";
		if (sort.equalsIgnoreCase("terbaru")) {
			bukalapak_sort = "last_relist_at%3Adesc";
			olx_sort = "created_at%3Adesc";
		} else if (sort.equalsIgnoreCase("termurah")) {
			bukalapak_sort = "price%3Aasc";
			olx_sort = "filter_float_price%3Aasc";
		} else {
			bukalapak_sort = "price%3Adesc";
			olx_sort = "filter_float_price%3Aesc";
		}
		sortResult[0] = bukalapak_sort;
		sortResult[1] = olx_sort;
		return sortResult;
	}
	
}
