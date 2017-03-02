package com.uumai.crawer.quartz.shoping.jd;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;
import com.uumai.crawer2.download.CrawlerProxy;
import com.uumai.crawer2.download.Download.DownloadType;

public class JdProductTasker extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

		createonetask("http://item.jd.com/1246836.html");

	}

	public void createonetask(String url) throws Exception {

		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		tasker.setUrl(url);
//		 tasker.setDownloadType(DownloadType.jbrowser_download);
			tasker.setDownloadType(DownloadType.phantomjs_download);

		 tasker.setProxy(new CrawlerProxy("cn-proxy.sg.oracle.com", 80));

		tasker.addXpath("price", "//strong[@id='jd-price']/text()");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) {
		new JdProductTasker().init().start();

	}
}