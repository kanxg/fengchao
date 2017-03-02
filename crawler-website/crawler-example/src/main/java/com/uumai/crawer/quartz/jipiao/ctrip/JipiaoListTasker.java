package com.uumai.crawer.quartz.jipiao.ctrip;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class JipiaoListTasker extends QuartzLocalAppMaster { // QuartzLocalAppMaster{

	@Override
	public void dobusiness() throws Exception {
		createonetask("http://flights.ctrip.com/schedule", "ROOT");

	}

	public void createonetask(String url, String from) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();

		// tasker.setDownloadType(DownloadType.firefox_download);
		tasker.setUrl(url);
		// tasker.addRequestHeader("Referer",
		// "http://flights.ctrip.com/booking/SHA-BJS1-day-1.html");

		tasker.addResultItem("from", from);
		tasker.addXpath_all("flight_name", "//div[@class='m']/a/text()");
		tasker.addXpath_all("flight", "//div[@class='m']/a/@href");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		JipiaoListTasker master = new JipiaoListTasker();
		master.init();

		master.start();

	}

}
