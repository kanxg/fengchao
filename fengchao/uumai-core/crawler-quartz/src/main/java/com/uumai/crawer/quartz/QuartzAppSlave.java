package com.uumai.crawer.quartz;

import com.uumai.crawer2.AbstractAppSlave;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.MultiCrawlerWorker;

/**
 * Created with IntelliJ IDEA.
 * User: rock
 * Date: 3/19/15
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuartzAppSlave extends AbstractAppSlave {

	@Override
    protected MultiCrawlerWorker createWoker(CrawlerTasker tasker) throws Exception{
         MultiCrawlerWorker worker = new QuartzCrawlerWorker(tasker);
         return    worker;
      
    }

    /**
     * Just for Debug............
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        QuartzAppSlave app=new QuartzAppSlave();
        app.setPoolsize(1);
        app.init();
        app.start();
    }
}
