package com.uumai.resourcepool;

import com.uumai.crawer.util.Java8Time;
import com.uumai.crawer.util.UumaiProperties;
import com.uumai.resourcepool.semaphore.SemaphoreResourcePool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rock on 7/29/15.
 */
public class ResourcePoolFactory {

    public static Map<String,String>  readConfigTimeMap= Collections.synchronizedMap(new HashMap<String, String>());
    public static Map<String,ResourcePool>  resourcePoolMap= Collections.synchronizedMap(new HashMap<String, ResourcePool>());
    private static String resourcepoolsize_default=UumaiProperties.readconfig("resourcepoolsize/default", "-1");
    private static String resourcepoolwaittime_default=UumaiProperties.readconfig("resourcepoolwaittime/default" , "-1");

    private static int checkpoolsizechangeInterval=5;

    public static synchronized void readconfig(String domain){
        int poolsize= -1;
        int resourcepoolwaittime=-1;
        try {
            String pstr=UumaiProperties.readconfig("resourcepoolsize/"+ domain , resourcepoolsize_default,false);
            String wtstr=UumaiProperties.readconfig("resourcepoolwaittime/" + domain , resourcepoolwaittime_default,false);
            poolsize= new Integer(pstr);
            resourcepoolwaittime= new Integer(wtstr);
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println("error get config:resourcepoolsize."+domain );
            return;
        }

        ResourcePool old_resourcePool= resourcePoolMap.get(domain);

        if(poolsize!=-1){
            if(old_resourcePool!=null){
                if(old_resourcePool.getPoolsize()!=poolsize){
                    old_resourcePool.resize(poolsize,resourcepoolwaittime);
                }
            }else{
//                ResourcePool resourcePool=new DefaultResoucePool(domain,poolsize,resourcepoolwaittime);
                ResourcePool resourcePool=new SemaphoreResourcePool(domain,poolsize,resourcepoolwaittime);
                resourcePoolMap.put(domain,resourcePool);
                System.out.println("start a resource pool:"+domain + " with size:"+poolsize +", waittime:"+resourcepoolwaittime);
            }

        }else{
            resourcePoolMap.put(domain,null);
        }
        readConfigTimeMap.put(domain,new Java8Time().getNowString());
    }

    public static synchronized ResourcePool getNewResourcePool(String domain){
//        if(domain.startsWith("http://")){
//            domain=domain.substring(7);
//        }
//        if(domain.startsWith("https://")){
//            domain=domain.substring(8);
//        }
//        Boolean readconfig=readConfigMap.get(domain);
//        if(readconfig==null){

//        }
        String lastruntime=readConfigTimeMap.get(domain);
        if(lastruntime!=null){
            float duration=new Java8Time().getTimeDuration(lastruntime);
            if(duration>checkpoolsizechangeInterval){
                readconfig(domain);
            }
        }else{
            readconfig(domain);
        }
        ResourcePool resourcePool= resourcePoolMap.get(domain);
        return resourcePool;
    }

    public static void main(String[] aa){
//        UumaiProperties.init("/home/rock/kanxg/Dropbox/mysourcecode/uumai/bitbucket/shop_indexer/crawler-example/deploy/resources/uumai.properties");
        ResourcePool resourcePool=ResourcePoolFactory.getNewResourcePool("http://www.amazon.com");
        System.out.println("ResourcePool:"+resourcePool);
    }
}
