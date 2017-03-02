package com.uumai.crawer.quartz.jobs.fiveonejob;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.core.cookies.CookieConstant;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class JobDetailTasker extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {

	}

	private void sendtask(String jobid, String url) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		// tasker.setCookies(cookie);
		tasker.setCookies(CookieConstant.fiveonejob_cookie);

		tasker.setUrl(url);
		tasker.setEncoding("gbk");
		// tasker.setDownloadType(DownloadType.selenium_download);
		tasker.addResultItem("_id", new Integer(jobid));

		tasker.addResultItem("url", url);

		tasker.addXpath("title", "//li[@class='tCompany_job_name']/allText()");
		tasker.addXpath("desc", "//div[@class='tCompany_text']/allText()");

		putDistributeTask(tasker);

	}

	public static void main(String[] args) throws Exception {

		new JobDetailTasker().init().start();

	}

}
