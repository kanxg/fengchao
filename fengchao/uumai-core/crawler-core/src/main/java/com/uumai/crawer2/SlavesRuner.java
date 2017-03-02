package com.uumai.crawer2;

import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer.util.license.LicenseValidateHelper;
import com.uumai.crawer2.rpc.RmiImpl;
import com.uumai.crawer2.rpc.RmiServer;
import com.uumai.zookeeperclient.UUmaiZkClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rock on 4/22/15.
 */
public class SlavesRuner {

    private List<AbstractAppSlave> threadList=new ArrayList<AbstractAppSlave>();
    private List<RmiServer> rmpserverlist=new ArrayList<RmiServer>();

    public SlavesRuner(){
        //register shutdown hook
        new ShutdownHook(threadList,rmpserverlist);

    }

    private boolean checkLicense() {
        try {
            LicenseValidateHelper licenseValidateHelper=new LicenseValidateHelper();
            licenseValidateHelper.validate();
            System.out.println("license check passed!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("license check failed!");
        return false;
    }

    public void startserver(){
        if(!checkLicense()) return;

        UUmaiZkClient client=new UUmaiZkClient();
        List<String> children=client.getChildren("/uumai/config/uumai.crawler.worker.appslave.mainclass");
        if(children==null) {
            System.out.println("didn't detect any appslave class in conifg");
            System.out.println("stop slave...");
            return;
        }
        for(String child:children){
            String mainclass=UumaiProperties.readconfig("uumai.crawler.worker.appslave.mainclass/"+child,null);
            if(mainclass==null)
                continue;
            String poolsize=UumaiProperties.readconfig("uumai.crawler.worker.appslave.poolsize/"+child,null);
//            String redisKey=UumaiProperties.readconfig("uumai.crawler.worker"+i+".appslave.redisKey",null);
            String rmiPORT=UumaiProperties.readconfig("uumai.crawler.worker.appslave.rmiport/"+child,null);
            try {
                AbstractAppSlave app=(AbstractAppSlave)Class.forName(mainclass).newInstance();
                if(poolsize!=null){
                    app.setPoolsize(Integer.parseInt(poolsize));
                }

//                if(redisKey!=null){
//                    app.setRedisKey(redisKey);
//                }
                app.init();
                app.start();
                //app.join();

                threadList.add(app);

                if(rmiPORT!=null){

                    //create RMI server
                    RmiImpl rmiimple=new RmiImpl();
                    rmiimple.setAbstractAppSlave(app);
                    RmiServer rmiServer=new RmiServer();
                    rmiServer.setPORT(Integer.parseInt(rmiPORT));
                    rmiServer.setRmiImpl(rmiimple);
//                rmiServer.setDaemon(true);
                    rmiServer.start();

                    rmpserverlist.add(rmiServer);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        client.close();
    }

    public static void main(String[] args) throws Exception {
        SlavesRuner runer=new SlavesRuner();

        runer.startserver();
    }

    class  ShutdownHook implements Runnable {
//        private AbstractAppSlave  slave;
        private    List<AbstractAppSlave> threadList;
        private List<RmiServer> rmiserverlist;
        public ShutdownHook(List<AbstractAppSlave> threadList,List<RmiServer> rmiserverlist) {
            // register a shutdown hook for this class.
            // a shutdown hook is an initialzed but not started thread, which will get up and run
            // when the JVM is about to exit. this is used for short clean up tasks. ;
            this.threadList=threadList  ;
            this.rmiserverlist=rmiserverlist;
            Runtime.getRuntime().addShutdownHook(new Thread(this));
            System.out.println("slave shutdown hook registered");
        }

        // this method will be executed of course, since it's a Runnable.
        // tasks should not be light and short, accessing database is alright though.
        public void run() {
            // System.out.println("/n>>> About to execute: " + ShutdownHook.class.getName() + ".run() to clean up before JVM exits.");
            this.cleanUp();
            // System.out.println(">>> Finished execution: " + ShutdownHook.class.getName() + ".run()");
        }

        private void cleanUp() {
            for(AbstractAppSlave slave:threadList){
                slave.stoppool();
                try {
                    slave.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(RmiServer slave:rmiserverlist){
                slave.stopserver();
                try {
                    slave.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //stop threadpool
        }
    }

}