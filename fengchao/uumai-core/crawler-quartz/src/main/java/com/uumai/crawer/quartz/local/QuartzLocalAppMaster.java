package com.uumai.crawer.quartz.local;

import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.local.LocalAppMaster;

public class QuartzLocalAppMaster extends LocalAppMaster {
	
	
    public void putDistributeTask(CrawlerTasker crawlerTasker)  throws Exception {
        this.crawlerTasker=crawlerTasker;
        QuartzLocalCrawlerWorker quartzLocalCrawlerWorker=    new QuartzLocalCrawlerWorker(crawlerTasker);
        localCrawlerWorker=quartzLocalCrawlerWorker;
        quartzLocalCrawlerWorker.download();
        quartzLocalCrawlerWorker.pipeline();
    }

//	@Override
//	public void dobusiness() throws Exception {
//		super.dobusiness();
//	}

}
