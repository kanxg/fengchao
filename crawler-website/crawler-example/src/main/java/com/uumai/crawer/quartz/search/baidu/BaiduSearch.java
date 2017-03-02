package com.uumai.crawer.quartz.search.baidu;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class BaiduSearch extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		tasker.setUrl("http://www.baidu.com/s?wd=爬虫");
		tasker.addXpath("result", "//div[@class='nums']/text()");
		putDistributeTask(tasker);

	}

	public static void main(String[] args) throws Exception {
		new BaiduSearch().init().start();
	}
}