package com.uumai.crawer.quartz.jipiao.ctrip;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class JipiaoInternListTasker extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

		String link = "http://flights.ctrip.com/international/Schedule/#schedule_a";
		String from = "A";
		createonetask(link, from);

	}

	public void createonetask(String url, String from) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		// tasker.setDownloadType(DownloadType.firefox_download);
		tasker.setUrl(url);
		// tasker.addRequestHeader("Referer",
		// "http://flights.ctrip.com/booking/SHA-BJS1-day-1.html");

		tasker.addResultItem("from", from);

		tasker.addXpath_all("flight_name",
				"//ul[@class='schedule_detail_list clearfix']//a/text()");
		tasker.addXpath_all("flight",
				"//ul[@class='schedule_detail_list clearfix']//a/@href");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		JipiaoInternListTasker master = new JipiaoInternListTasker();
		master.init();

		master.start();

	}

}
