package com.uumai.crawer2.queues;

import com.uumai.crawer.util.Java8Time;
import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer.util.UumaiTime;
import com.uumai.crawer.util.io.SerializeUtil;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.redis.RedisDao;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rock on 3/16/16.
 */
public class RedisBackupQueuePool extends  QueuePool{
    private final  String host= UumaiProperties.getHostName();
    private final String runningAndMissingListKey_PRE="runlist";

    private RedisDao dao;
    private Jedis redis;
    private Set<String> keySets=new HashSet<String>();
    private Set<String> runningKeySets=new HashSet<String>();

    private String runningAndMissingListKey;

    public final int max_mins_waitingfor_tasker_finish=5;

    public  RedisBackupQueuePool(String rediskey){
        super();
        this.rediskey=rediskey;
        this.runningAndMissingListKey=host+"_"+runningAndMissingListKey_PRE ;
        this.dao=new RedisDao();
    }

    @Override
    public void connect(){
//        dao.init();
        redis=dao.getRedis();
//        checkreloadkeys();
    }

    protected void reloadKeys(){
        this.keySets=redis.keys(rediskey+"*");
        this.runningKeySets=redis.keys(runningAndMissingListKey+"*");
    }



    //    public List<String> blpop() {
//        for(int i=1;i<=maxlevel;i++){
//            List<String> result= redis.blpop(1, this.rediskey+i);
//            if(result!=null&&result.size()!=0)
//                return result;
//        }
//       return null;
//    }
//
//    public List<String> brpop() {
//        for(int i=1;i<=maxlevel;i++){
//            List<String> result= redis.brpop(1, this.rediskey + i);
//            if(result!=null&&result.size()!=0)
//                return result;
//        }
//        return null;
//    }
//    @Override
//    public Long llen(int level){
//        level=checklevel(level);
//        return  redis.llen(rediskey+level);
//    }
    @Override
    public List<CrawlerTasker> getDistributeTask(){
        checkreloadkeys();
        runningTakserlist.clear();

        for(String key:this.runningKeySets){
            String org_runningtime=key.substring(key.length()-19,key.length());
            float dura=new Java8Time().getTimeDuration(org_runningtime,new UumaiTime().getNowString());
            if(dura<max_mins_waitingfor_tasker_finish)
                continue;
            Integer retrytime= new Integer(key.substring(key.length()-21,key.length()-20))+1;
            if(retrytime>=4){
                System.out.println("get not-finish tasker which retry more than 4 times,delete it:" + key );
                cleantasks(key);
                continue;
            }
            String origkey= key.substring(0,key.length()-22);
            String RedisBackupKey=origkey +"_"+retrytime+"_"+new UumaiTime().getNowString();
            while(true){
                String result =redis.rpoplpush(key,RedisBackupKey);
                if(result!=null) {
                    System.out.println("get not finish tasker,recover it and put into backup key:" + RedisBackupKey );
                    CrawlerTasker crawlerTasker=(CrawlerTasker) SerializeUtil.unserialize(result);
                    crawlerTasker.setRedisOrigMessage(result);
                    crawlerTasker.setRedisBackupKey(RedisBackupKey);
                    runningTakserlist.add(crawlerTasker);
                }else{
                    break;
                }
            }
            //end for

        }

        for(String key:this.keySets){
//              String result =redis.rpop(key);

            String RedisBackupKey=runningAndMissingListKey+"_"+key+"_0_"+new UumaiTime().getNowString();
            String result =redis.rpoplpush(key,RedisBackupKey);
            System.out.println("get new tasker, put into backup key:" + RedisBackupKey );
            if(result!=null) {
                CrawlerTasker crawlerTasker=(CrawlerTasker) SerializeUtil.unserialize(result);
                crawlerTasker.setRedisOrigMessage(result);
                crawlerTasker.setRedisBackupKey(RedisBackupKey);
                runningTakserlist.add(crawlerTasker);
            }

        }

        return runningTakserlist;
    }



    @Override
    public  void putDistributeTask(CrawlerTasker tasker) {
        try {
            String key=this.rediskey +"_" +tasker.getTaskerOwner()+ "_" + tasker.getTaskerName() +"_"+ tasker.getTaskerSeries();
//            if(putfirst){
//                redis.rpush(rediskey + level, tasker);
//            }else {
            redis.lpush(key, SerializeUtil.serialize(tasker));
//                redis.lpush(rediskey+level, tasker);
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will called by all the crawlerworker ,so need synchronized .and need to get redis connection by itselef.
     * @param tasker
     */
    @Override
    public synchronized void removeRunningList(CrawlerTasker tasker){
        Jedis  removeredis=null;
        try {
            removeredis=dao.getRedis();
            if(removeredis.exists(tasker.getRedisBackupKey())) {
                System.out.println("tasker finish,remove tasker:" + tasker.getRedisBackupKey());
                long i=redis.lrem(tasker.getRedisBackupKey(), -1, tasker.getRedisOrigMessage());
                System.out.println("remove count :" + i);
//                removeredis.del(tasker.getRedisBackupKey());
            }else{
                System.out.println("tasker finish,didn't detect in  running list:"+tasker.getRedisBackupKey());
            }
//            redis.srem(rediskey+__runingKey,tasker);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(removeredis!=null)
                dao.returnResource(removeredis);
        }

    }

    //    @Override
//    public int getNotFinishedTasker(int level){
//        level=checklevel(level);
//        int returnkey=-1;
//        try {
//            returnkey=redis.llen(rediskey+level).intValue();
////            dao.returnResource(redis);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return returnkey;
//    }
    @Override
    public void close(){
        dao.returnResource(redis);
//        dao.destroy();
    }
    @Override
    public void cleantasks() {
        this.reloadKeys();
        for(String key:keySets){
            this.cleantasks(key);
        }
//        this.cleantasks(rediskey+__runingKey);
    }


    private void cleantasks(String key){
        try {
            redis.del(key);
//            dao.returnResource(redis);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void checkRunningPool() {
//        while (true){
//            String msg= redis.rpop(rediskey+__runingKey);
////            String msg=redis.spop(rediskey+__runingKey);
//            if(msg!=null){
//                System.out.println("found not finish running tasker, send to pool back.");
//                this.putDistributeTask(msg);
//            }else{
//                break;
//            }
//        }
    }

    @Override
    public Long  getQueueSize(String key) {
        return redis.llen(key);
    }

    public static void  main(String[] a){
        String key="fafda.quartz_2016-03-19 12:36:29_1_2016-03-09 11:36:29";
        key=key.replaceAll(" ","T");
        String origkey= key.substring(0,key.length()-22);
        String org_runningtime=key.substring(key.length()-19,key.length()-9)+" "+key.substring(key.length()-8,key.length());
        Integer retrytime= new Integer(key.substring(key.length()-21,key.length()-20))+1;


        System.out.println(origkey);
        System.out.println(org_runningtime);

        System.out.println(retrytime);

    }

}
