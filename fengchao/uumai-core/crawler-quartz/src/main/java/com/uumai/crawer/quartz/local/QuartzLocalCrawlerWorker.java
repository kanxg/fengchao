package com.uumai.crawer.quartz.local;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.uumai.crawer.quartz.download.selenium.QuartzSeleniumDownloader;
import com.uumai.crawer2.download.Download;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uumai.crawer.quartz.JsonParseHelper;
import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.util.filesystem.ExcelFileUtil;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.local.LocalCrawlerWorker;
import com.uumai.dao.helper.Json2DBHelper;

public class QuartzLocalCrawlerWorker extends LocalCrawlerWorker {
	
	public QuartzLocalCrawlerWorker(CrawlerTasker tasker) {
		super(tasker);
 	}

	@Override
    protected void download() throws Exception {
        super.download();
//         QuartzCrawlerTasker quartztasker = (QuartzCrawlerTasker) tasker;
//        if(Download.DownloadType.chrome_download.equals(quartztasker.getDownloadType())
//                || Download.DownloadType.firefox_download.equals(quartztasker.getDownloadType())
//                || Download.DownloadType.jbrowser_download.equals(quartztasker.getDownloadType())
//                || Download.DownloadType.phantomjs_download.equals(quartztasker.getDownloadType())){
//            if(quartztasker.getSeleniumActionses()!=null){
//                this.result=new QuartzSeleniumDownloader().download(quartztasker);
//            }else{
//                super.download();
//            }
//
//        }else{
//            super.download();
//        }
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


            Json2DBHelper helper =null;
			
			JsonParseHelper jsonParseHelper=new JsonParseHelper(quartztasker,result);

			List<JsonObject> list=  jsonParseHelper.parse();
            this.pipelineresult=list;
 			
 			if(quartztasker.getStoreTableName()!=null){
                if(quartztasker.getStoreTableName().endsWith(".xls")){
                    ExcelFileUtil util=new ExcelFileUtil(quartztasker.getStoreTableName());
                    for(JsonObject obj:list){
                        List<String> columnvalues=new ArrayList<String>();
                        Iterator i$ = obj.entrySet().iterator();
                        while(i$.hasNext()) {
                            Map.Entry entry = (Map.Entry)i$.next();
                            String value= ((JsonElement)entry.getValue()).toString();
                            if(value!=null){
                                value=value.substring(1, value.length()-1);
                                columnvalues.add(value);
                            }

                        }
                        util.writeLine(columnvalues);

                    }
                    util.createWorkBook();
                } else if(quartztasker.getStoreTableName().endsWith(".txt")){
                    BufferedWriter  out=new BufferedWriter(new FileWriter(quartztasker.getStoreTableName(),false));
                    for(JsonObject obj:list){
                        out.write(obj.toString());
                        out.newLine();
                    }
                    out.close();
                }else{
                    if(helper==null)
                        helper = new Json2DBHelper();
                    for(JsonObject obj:list){
                        helper.store(obj.toString(), quartztasker.getStoreTableName());
                    }
                }

 			}
            for(JsonObject obj:list){
                System.out.println(obj.toString());
            }
 
    }

}
