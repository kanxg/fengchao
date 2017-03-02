package com.uumai.crawer.quartz.medical.guahao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
import com.uumai.crawer.quartz.util.QuartzQueryItem;
import com.uumai.crawer.util.UumaiTime;
 import com.uumai.crawer2.CookieManager.CookieHelper;
import com.uumai.crawer2.CookieManager.CrawlerCookie;
import com.uumai.crawer2.download.CrawlerProxy;

public class Yuyue  extends QuartzLocalAppMaster {  //QuartzLocalAppMaster{ //AbstractAppMaster {
 	CookieHelper cookieHelper=new CookieHelper();
	List<CrawlerCookie> cookies;
 	@Override
	public void dobusiness() throws Exception {
 		String cook=" _sh_ssid_=1465036662472; _e_m=1465036662477; _sid_=1465036662467201780671528; __rf__=ifPguCPi+XEd2m7kLsL/TJHpqT80IwNaPpEIFni1/tlz8WSmkd/jRhZoxaeKh/LQIlDj8g/XKxauWuk0LpzhazFj0kxj5NoDn1yR3CEvTz9q9/nHn7AmSe57Advitpqj; Hm_lvt_66fcb71a7f1d7ae4ae082580ac03c957=1465036663; Hm_lpvt_66fcb71a7f1d7ae4ae082580ac03c957=1465037252; JSESSIONID=kiasbb4sixng1lvvsgcu1zetm; _ci_=seKbXdbD2qfPYFxkbpmwHwbkxABZeoSn9v5W7xVAdNmNSQWJcpuO2E9039uUKnp6; __i__=3SbFlByu/+jR2Mx2DdrrZJrGAB3PhUfIcOG2nBNxjWM=; __usx__=Ow0jWslaqQqTDgz8FTibXlHc3/ryWmOaBuZWeacUjsA=; __up__=ETKfDPdAhWvuAJi1vHyv3IFDBfTNZyS03IXVE+mSxgM=; _exp_=CNVezA4mmKUR2ixtOYRw3aYsR8RXE0YiEHVrERNvqN8=; __ut__=PV6J5FZv00CBPlCeOZKyNZHzco6GOgSD1wOk9ldLOaI=; __atet__=73bjyQcMLBr9fivq3EmbQP8ydwr+3OG7vjmxtyhjZn8=; __p__=pBDdqyQU3DjtJYSuVpPKuEzbK2Ljp7scQTxJ21ihvnODxiIVf1dU4Q==; __un__=nJixEOSlTf5eqEwobB8/BP6oSeWOflqQcph2a/UaldeigPvqQhHVLvRqzHKIO0EdRBRcAMAzjMA=";
		cookies=cookieHelper.parseCookieFromCrul(cook);
 		get("叶玮","http://www.guahao.com/expert/new/shiftcase/?expertId=138181403231050000&hospDeptId=138181401578623000&hospId=0ba4a4af-6a09-47ef-8bc6-6ecd1a7d3bb4000");
		
		get("冯希平","http://www.guahao.com/expert/new/shiftcase/?expertId=138181402578508000&hospDeptId=138181401578623000&hospId=0ba4a4af-6a09-47ef-8bc6-6ecd1a7d3bb4000");
//		get("口腔预防","http://www.guahao.com/expert/new/shiftcase/?expertId=138181404112472000&hospDeptId=138181401578623000&hospId=0ba4a4af-6a09-47ef-8bc6-6ecd1a7d3bb4000",taskerserie);
 //		get("冯希平","",taskerserie);
		
		
 	 

	}
 

	private void get(String dockername,String url) throws Exception{
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
  		tasker.setUrl(url);
 
 		tasker.setCookies(cookies);
//		 tasker.setDownloadType(DownloadType.java_download);
// 		tasker.setStoreTableName("guahao_yuyue");
		tasker.addResultItem("dockername", dockername);
 		tasker.addJsonpath("shiftSchedule", "$..shiftSchedule");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		Yuyue master = new Yuyue();
 
			master.init();
 	 
			master.start();
 		 
	}
}
