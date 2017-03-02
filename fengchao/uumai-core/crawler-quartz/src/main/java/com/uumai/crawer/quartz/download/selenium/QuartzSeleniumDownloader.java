package com.uumai.crawer.quartz.download.selenium;


import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer2.CrawlerResult;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.CookieManager.CookieHelper;
import com.uumai.crawer2.CookieManager.CrawlerCookie;
import com.uumai.crawer2.download.CrawlerProxy;
import com.uumai.crawer2.download.Download;
import com.uumai.crawer2.download.selenium.SeleniumDownloader;
import com.uumai.crawer2.download.selenium.WebDriverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class QuartzSeleniumDownloader extends SeleniumDownloader {

	public QuartzSeleniumDownloader() {

	}


//	@Override
//    public CrawlerResult download(CrawlerTasker tasker) throws Exception

    protected void superClassdone() {
        QuartzCrawlerTasker quartzCrawlerTasker=(QuartzCrawlerTasker)tasker;

        //do actions
        if(quartzCrawlerTasker.getSeleniumActionses()!=null){
            new SeleniumActionBot(webDriver,quartzCrawlerTasker.getSeleniumActionses()).doactions();
        }
    }

    public static void main(String[] args) throws  Exception{
//        UumaiProperties.init("/home/rock/kanxg/Dropbox/mysourcecode/uumai/bitbucket/shop_indexer/crawler-example/deploy/resources/uumai.properties");

        QuartzSeleniumDownloader downloader=new QuartzSeleniumDownloader();
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();

        tasker.setDownloadType(DownloadType.firefox_download);
        tasker.setUrl("http://bbs.skykiwi.com/forum.php?mod=viewthread&tid=3327087");
        tasker.setProxy(new CrawlerProxy("cn-proxy.jp.oracle.com", 80));
        tasker.addSeleniumAction("sleep", null, "60000");
        tasker.addSeleniumAction("click", "id=btn_flight_search", null);

//    	tasker.addSeleniumAction("sendKeys", "id=0", "北京首都国际机场");
//		tasker.addSeleniumAction("sendKeys", "id=1", "上海浦东机场");
//		tasker.addSeleniumAction("sendKeys", "id=deptDateShowGo", "2015-12-25");
//		tasker.addSeleniumAction("click", "id=portalBtn", null);
//		
//        tasker.setCookies(CookieUtil.loadCookie("amazon"));
        String html=downloader.download(tasker).getRawText();
            System.out.println("html:"+ html);


    }
}