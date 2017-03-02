package com.uumai.crawer.quartz.jipiao.chunqiu;

import com.uumai.crawer.quartz.QuartzCrawlerTasker;
import com.uumai.crawer.quartz.local.QuartzLocalAppMaster;

public class ChunqiuFlight extends QuartzLocalAppMaster {
	@Override
	public void dobusiness() throws Exception {

		String[] flightStr = new String[] { "大连", "上海" };
		String OriCity = "&OriCity=" + flightStr[0];
		String DestCity = "&DestCity=" + flightStr[1];

		String fdate = "2016-09-09";
		sendtask("http://flights.ch.com/default/SearchByTime",
				"SType=0&IfRet=false&MType=0&ANum=1&CNum=0&INum=0&PostType=0"
						+ OriCity + DestCity + "&FDate=" + fdate, fdate);

	}

	private void sendtask(String url, String postdata, String fdate)
			throws Exception {
		QuartzCrawlerTasker tasker = new QuartzCrawlerTasker();
		// tasker.setCookies(cookie);
		tasker.setUrl(url);
		// tasker.setDownloadType(DownloadType.selenium_download);
		tasker.setRequestmethod("POST");
		tasker.setPostdata(postdata);
		tasker.addResultItem("fdate", fdate);
		for (int i = 0; i <= 10; i++) {
			tasker.addJsonpath("number", "$.Packages[" + i + "][0].No");
			tasker.addJsonpath("Departure", "$.Packages[" + i
					+ "][0].Departure");
			tasker.addJsonpath("Arrival", "$.Packages[" + i + "][0].Arrival");

			tasker.addJsonpath("DepartureStation", "$.Packages[" + i
					+ "][0].DepartureStation");
			tasker.addJsonpath("ArrivalStation", "$.Packages[" + i
					+ "][0].ArrivalStation");

			tasker.addJsonpath("DepartureCode", "$.Packages[" + i
					+ "][0].DepartureCode");
			tasker.addJsonpath("ArrivalCode", "$.Packages[" + i
					+ "][0].ArrivalCode");

			tasker.addJsonpath("DepartureTime", "$.Packages[" + i
					+ "][0].DepartureTime");
			tasker.addJsonpath("ArrivalTime", "$.Packages[" + i
					+ "][0].ArrivalTime");
			tasker.addJsonpath("price1", "$.Packages[" + i
					+ "][0].CabinInfos[0].Cabins[0].CabinPrice");
			tasker.addJsonpath("price2", "$.Packages[" + i
					+ "][0].CabinInfos[1].Cabins[0].CabinPrice");
			tasker.addJsonpath("price3", "$.Packages[" + i
					+ "][0].CabinInfos[2].Cabins[0].CabinPrice");

			tasker.addXpath_newrow();

		}

		putDistributeTask(tasker);
	}

	public static void main(String[] args) throws Exception {

		ChunqiuFlight master = new ChunqiuFlight();
		master.init();
		master.start();

	}
}