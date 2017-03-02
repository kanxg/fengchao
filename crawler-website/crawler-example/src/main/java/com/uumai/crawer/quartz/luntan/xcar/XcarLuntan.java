package com.uumai.crawer.quartz.luntan.xcar;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
import com.uumai.crawer.util.UumaiTime;

public class XcarLuntan extends QuartzLocalAppMaster {
 
	@Override
    public void dobusiness()  throws Exception{
 		
  		sendtask("http://www.xcar.com.cn/bbs/header/bbsnav.htm?v=20151131515");
     }

	private void sendtask(String url) throws Exception{
		   QuartzCrawlerTasker tasker=new QuartzCrawlerTasker();
//		   tasker.setCookies(cookie);
//        tasker.setUrl("http://data.eastmoney.com/zjlx/600307.html");
        tasker.setUrl(url);
        tasker.setEncoding("gbk");
//        tasker.setDownloadType(Download.DownloadType.selenium_download);
 //        tasker.setDownloadType(DownloadType.selenium_download);
//         tasker.setStoreTableName("xar_luntan");
//        tasker.setCookies(cookies);
         tasker.addXpath_all("link", "//a/@href");
         tasker.addXpath_all("name", "//a/text()");
//        tasker.addXpath("html", "*");

        putDistributeTask(tasker);
	}
	
	public static void main(String[] args) throws Exception {
		
		
		
 		XcarLuntan master=new XcarLuntan();
     	master.init();
   
    		master.start();
  	 
	}

}