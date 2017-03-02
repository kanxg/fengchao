package com.uumai.crawer.quartz;

import java.util.List;

import com.google.gson.JsonObject;
import com.uumai.crawer.quartz.download.selenium.QuartzSeleniumDownloader;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.MultiCrawlerWorker;
import com.uumai.crawer2.download.Download;
import com.uumai.crawer2.download.Download.DownloadType;
import com.uumai.dao.helper.Json2DBHelper;

/**
 * Created with IntelliJ IDEA. User: rock Date: 3/19/15 Time: 5:53 PM To change
 * this template use File | Settings | File Templates.
 */
public class QuartzCrawlerWorker extends MultiCrawlerWorker {

	// public static ExcelFileUtil excelFileUtil=new
	// ExcelFileUtil("/home/rock/quartz.xls");

	static {
		// excelFileUtil.writeLine("screenName","symbol","name","time","price","saleFlag","count");
	}

	public QuartzCrawlerWorker(CrawlerTasker tasker ) {

		super(tasker);
	}
	
	@Override
	protected void download() throws Exception {
        super.download();
//		QuartzCrawlerTasker quartztasker = (QuartzCrawlerTasker) tasker;
//		if(DownloadType.chrome_download.equals(quartztasker.getDownloadType())
//				||DownloadType.firefox_download.equals(quartztasker.getDownloadType())
//				||DownloadType.phantomjs_download.equals(quartztasker.getDownloadType())){
//			if(quartztasker.getSeleniumActionses()!=null){
//				this.result=new QuartzSeleniumDownloader().download(quartztasker);
//			}else{
//				 super.download();
//			}
//
//		}else{
//			 super.download();
//		}
	}

    @Override
    protected Download getDownloadInstantce() throws  Exception {
        QuartzCrawlerTasker quartztasker = (QuartzCrawlerTasker) tasker;
        if(quartztasker.getSeleniumActionses()!=null){
            return new QuartzSeleniumDownloader();
        }
        return super.getDownloadInstantce();
    }

     @Override
	protected void pipeline() throws Exception {

		QuartzCrawlerTasker quartztasker = (QuartzCrawlerTasker) tasker;
		
		if(quartztasker.getStoreTableName()==null||"".equals(quartztasker.getStoreTableName())){
//			System.out.println("not set store table");
			return;
		}
		
//		tasker.setRawText(Jsoup.clean(tasker.getRawText(), Whitelist.basic()));
		JsonParseHelper jsonParseHelper=new JsonParseHelper(quartztasker,result);
 
		Json2DBHelper helper = new Json2DBHelper();
		List<JsonObject> list=  jsonParseHelper.parse();

		for(JsonObject obj:list){
			helper.store(obj.toString(), quartztasker.getStoreTableName());
		}

		 

	}
 	


	

	 

}