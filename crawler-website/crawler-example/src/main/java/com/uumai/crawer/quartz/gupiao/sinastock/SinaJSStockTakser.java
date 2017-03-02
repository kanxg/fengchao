package com.uumai.crawer.quartz.gupiao.sinastock;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class SinaJSStockTakser extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

		String symbol = "SZ20001";
		dotask("http://hq.sinajs.cn/list=" + symbol, symbol);

	}

	private void dotask(String url, String sockname) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		// tasker.setCookies(cookie);
		tasker.setUrl(url);
		// tasker.setDownloadType(DownloadType.selenium_download);
		tasker.addResultItem("stock", sockname);

		tasker.addXpath("all", "*");

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		SinaJSStockTakser master = new SinaJSStockTakser();
		master.init();

		master.start();

	}

}
