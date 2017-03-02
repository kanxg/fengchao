package com.uumai.crawer2.local;

import com.google.gson.JsonObject;
import com.uumai.crawer.util.filesystem.UumaiFileUtil;
import com.uumai.crawer2.CrawlerResult;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.CrawlerWorker;
import com.uumai.crawer2.download.Download;
import com.uumai.crawer2.download.DownloadFactory;

import java.util.List;

/**
 * Created by rock on 12/9/15.
 */
public class LocalCrawlerWorker extends CrawlerWorker {
    public List<JsonObject> pipelineresult;

    public LocalCrawlerWorker(CrawlerTasker tasker) {
        super(tasker);
    }



    @Override
    protected void download() throws Exception {
        //try get from cache
        if(tasker.getSavefilename()!=null&&tasker.isUseingcache()){
            UumaiFileUtil uumaiFileUtil=new UumaiFileUtil();
            String text=uumaiFileUtil.readfromcache(tasker.getSavefilename());
            if(text!=null) {
                this.result=new CrawlerResult();
                result.setRawText(text);
                result.setUrl(tasker.getUrl());
                System.out.println("read cache from file:" + tasker.getSavefilename());
                return;
            }
        }

        Download download=getDownloadInstantce();
        this.result=download.download(tasker);
        this.result.setUrl(tasker.getUrl());
//            System.out.println("download html:"+tasker.getRawText());



        if(tasker.getSavefilename()!=null&&tasker.isUseingcache()){
            UumaiFileUtil uumaiFileUtil=new UumaiFileUtil();
            uumaiFileUtil.save2file(tasker.getSavefilename(),result.getRawText());
            System.out.println("save html to cache file:" + tasker.getSavefilename());
        }

    }

    protected Download getDownloadInstantce() throws  Exception {
        return DownloadFactory.getnewDownload(tasker.getDownloadType());
    }
    @Override
    protected void pipeline() throws Exception {

    }
}
