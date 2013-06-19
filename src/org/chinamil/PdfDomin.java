package org.chinamil;

public class PdfDomin extends Object{

	public String title;//标题
	public String cover;//缩略图地址
	public String link;//pdf连接地址
	public String channels;//说明
	public PdfDomin(String title, String cover, String link, String channels) {
		super();
		this.title = title;
		this.cover = cover;
		this.link = link;
		this.channels = channels;
	}
	
	public PdfDomin() {
		super();
	}
	
}
