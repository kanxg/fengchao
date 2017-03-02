package com.uumai.crawer.quartz.gupiao.qq;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class SockIntro extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

		String symbol = "SZ20006";
		geteastmoney("http://stock.finance.qq.com/corp1/profile.php?zqdm="
				+ symbol.substring(2), symbol);

	}

	private void geteastmoney(String url, String sockname) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		// tasker.setCookies(cookie);
		tasker.setUrl(url);
		tasker.setEncoding("gbk");
		// tasker.setDownloadType(DownloadType.selenium_download);
		tasker.addResultItem("name", sockname);
		// tasker.addXpath("html", "*");
		tasker.addXpath_all("shuxing", "//table[@class='list']//td/allText()");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		SockIntro master = new SockIntro();

		master.init();

		master.start();

	}
}