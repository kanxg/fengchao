package com.uumai.crawer.quartz.luntan.xcar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
import com.uumai.crawer.quartz.result.QuartzResult;
import com.uumai.crawer.quartz.result.QuartzResultItem;
import com.uumai.crawer.quartz.util.QuartzQueryItem;
 import com.uumai.crawer.util.CookieUtil;
import com.uumai.crawer.util.UumaiTime;
 import com.uumai.crawer2.download.Download;

public class XcarApp extends QuartzLocalAppMaster {
 
	@Override
    public void dobusiness()  throws Exception{
 // 		DBCursor cursor=util.getresultCursor("xar_luntan");
// 		while(cursor.hasNext()){
//			BasicDBObject bdbObj = (BasicDBObject) cursor.next();
//			for(int i=0;i<=1300;i++){
//				String link=bdbObj.getString("link"+i);
//				if(link==null) break;
//				if(link.startsWith("http://www.xcar.com.cn/bbs/forumdisplay.php?fid=")){
//					link=link.replace("http://www.xcar.com.cn/bbs/forumdisplay.php?fid=", "");
//					sendalltask(link , taskerserie);
//				}
//			}
// 		}
		
		sendalltask("44");
		
 
    }
	
	private void sendalltask(String fid) throws Exception{
		for(int i=1;i<=1000;i++ ){
			sendtask("http://www.xcar.com.cn/bbs/forumdisplay.php?fid="+fid+"&orderby=dateline&page="+i,fid,i );
		}
	}
 
 
	
	private void sendtask(String url,String fid,int page) throws Exception{
		   QuartzCrawlerTasker tasker=new QuartzCrawlerTasker();
        tasker.setUrl(url);
        tasker.setEncoding("gbk");
//        tasker.setDownloadType(Download.DownloadType.selenium_download);
  //        tasker.setDownloadType(DownloadType.selenium_download);
//         tasker.setStoreTableName("xar");
        tasker.addResultItem("fid", fid);
        tasker.addResultItem("page", page+"");
        tasker.addXpath_all("link", "//td[@class='line34']/a[@class='open_view']/@href");
        tasker.addXpath_all("title", "//td[@class='line34']/a[@class='open_view']/text()");
        tasker.addXpath_all("view", "//td[@class='line34 c4']/allText()");


        putDistributeTask(tasker);
	}
	
	public static void main(String[] args) throws Exception {
		
		
		
		XcarApp master=new XcarApp();
     	master.init();
   
    		master.start();
  	 
	}

}
