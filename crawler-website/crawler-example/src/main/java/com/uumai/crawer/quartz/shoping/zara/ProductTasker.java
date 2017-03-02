package com.uumai.crawer.quartz.shoping.zara;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
import com.uumai.crawer.quartz.util.QuartzQueryItem;
import com.uumai.crawer.util.UumaiTime;
import com.uumai.crawer2.download.CrawlerProxy;
import com.uumai.crawer2.download.Download.DownloadType;

public class ProductTasker extends QuartzLocalAppMaster { //QuartzLocalAppMaster { // AbstractAppMaster { 
 
	@Override
	public void dobusiness() throws Exception {		
 
 		createonetask("http://www.zara.com/us/en/man/t-shirts/view-all/dark-t-shirt-c733866p3648259.html");
	 
		
	}
 	
 
	
 	public void createonetask(String url) throws Exception{
 

		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		tasker.setUrl(url);
 		tasker.setStoreTableName("/home/rock/uumai/upwork/upwork_zara_product_temp.txt");
//		tasker.setDownloadType(DownloadType.firefox_download);
//		tasker.setCookies(cookies);
  		tasker.addResultItem("url", url);
		tasker.setUseingcache(true);
		tasker.setSavefilename("/tmp/1.html");
		
 //		tasker.setProxy(new CrawlerProxy("cn-proxy.sg.oracle.com", 80));
 	
		
 		tasker.addXpath("name", "//h1[@class='product-name']/text()");
  		tasker.addXpath_all("colorcode", "//label[@data-colorcode]/@data-colorcode");
 		tasker.addXpath_all("color", "//label[@data-colorcode]/span/text()");
 		
 		tasker.addXpath_all("size", "//tr[@class='product-size _product-size ']/td[1]/text()");

 		tasker.addXpath_all("images", "//a[@class='_seoImg']/@href");
 		
 		
 		
 		tasker.addXpath("description", "//div[@id='description']/allText()");
 		tasker.addXpath("composition", "//ul[@class='list-composition']/allText()");
 		
 		tasker.addRegexExpress("json", "window.zara.dataLayer = (.*);window.zara.viewPayload").asSource("json").setNotOutput();;
	  	tasker.addJsonpath("productid","$..product.id").fromsource("json");
	  	tasker.addJsonpath("price","$..product.price").fromsource("json");

//		tasker.addXpath("html", "*");
// 		putDistributeTask("rock-oracle",tasker);
 		putDistributeTask(tasker);
	}
 	
 	public static void main(String[] args) {
		ProductTasker master=new ProductTasker();
		master.init();
		master.start();
		
	}

}
