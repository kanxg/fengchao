package com.uumai.crawer.quartz.jipiao.chunqiu;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class ChunqiuTiejia extends QuartzLocalAppMaster {

	@Override
	public void dobusiness() throws Exception {
		sendtask("http://flights.ch.com/tejia");

	}

	private void sendtask(String url) throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		tasker.setUrl(url);
		for (int i = 1; i <= 14; i++) {
			for (int j = 1; j <= 5; j++) {
				tasker.addXpath("flight",
						"//table[@class='b0'][1]//tr[@class='flight'][" + i
								+ "]/td[" + j + "]/a/text()");
				tasker.addXpath("link",
						"//table[@class='b0'][1]//tr[@class='flight'][" + i
								+ "]/td[" + j + "]/a/@href");
				tasker.addXpath_newrow();
			}

		}
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 5; j++) {
				tasker.addXpath("flight",
						"//table[@class='b0'][2]//tr[@class='flight'][" + i
								+ "]/td[" + j + "]/a/text()");
				tasker.addXpath("link",
						"//table[@class='b0'][2]//tr[@class='flight'][" + i
								+ "]/td[" + j + "]/a/@href");
				tasker.addXpath_newrow();
			}

		}
		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		ChunqiuTiejia master = new ChunqiuTiejia();
		master.init();

		master.start();

	}

}
