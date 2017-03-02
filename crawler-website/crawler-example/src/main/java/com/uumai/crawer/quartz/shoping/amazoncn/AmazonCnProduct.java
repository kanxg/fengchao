package com.uumai.crawer.quartz.shoping.amazoncn;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
 
public class AmazonCnProduct extends QuartzLocalAppMaster {
  
	@Override
	public void dobusiness() throws Exception {
 
		getall("B00F4TTDX2");
		 
	}
 

	private void getall( String prdid) throws Exception{
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
 		tasker.setUrl("http://www.amazon.cn/dp/"+prdid);
 		// tasker.setDownloadType(DownloadType.selenium_download);
 		
 		tasker.addResultItem("_id", prdid);
		tasker.addXpath("title", "//span[@id='productTitle']/text()");
		tasker.addXpath("listprice", "//td[@class='a-span12 a-color-secondary a-size-base a-text-strike']/text()");
		tasker.addXpath("listprice2", "//span[@id='listPriceValue']/text()");
		tasker.addXpath("storeID", "//input[@id='storeID']/@value");
		tasker.addXpath("merchantID", "//input[@id='merchantID']/@value");
		tasker.addXpath("brand", "//a[@id='brand']/text()");
		tasker.addXpath("imgsrc", "//div[@id='altImages']//*//img/@src");
		tasker.addXpath("bigimgsrc", "//div[@id='main-image-container']//*//img/@src");
		tasker.addXpath("imgsrc2", "//img[@class='border thumb0 selected']/@src");
		tasker.addXpath("bigimgsrc2", "//img[@id='main-image']/@src");
		tasker.addXpath("customerreview", "//span[@id='acrCustomerReviewText']/text()");
		tasker.addXpath("model", "//td[@class='bucket']/div[2]/ul/li[5]/text()");
		
//		tasker.addXpath("ziying", "//span[@class='service_sub']/text()");
//		tasker.addXpath("imgsrc", "//img[@id='xgalleryImg']/@src");
//		tasker.addXpath("listpirce", "//span[@itemprop='highPrice' or @itemprop='price']/del/text()");
//		tasker.addXpath("bigimgsrc", "//img[@class='img-hover']/@src");
		
		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {
		new AmazonCnProduct().init().start();;
  	}

}
