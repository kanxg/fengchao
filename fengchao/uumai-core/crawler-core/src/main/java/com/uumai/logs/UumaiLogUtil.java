package com.uumai.logs;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.uumai.crawer.util.MongoUtil;
import com.uumai.crawer.util.UumaiProperties;
import com.uumai.crawer.util.UumaiTime;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.dao.helper.Json2DBHelper;

/**
 * Created by rock on 8/30/15.
 */
public class UumaiLogUtil {
    MongoUtil mongoUtil=null;

    public void connect(){
        mongoUtil =new MongoUtil();
    }

    public UumaiLog createLogInstanceFromTasker(CrawlerTasker tasker){
        UumaiLog uumaiLog=new UumaiLog();
        uumaiLog.setTaskerName(tasker.getTaskerName());
        uumaiLog.setTaskerSeries(tasker.getTaskerSeries());
        uumaiLog.setTaskerOwner(tasker.getTaskerOwner());
        uumaiLog.setRunHost(UumaiProperties.getHostName());
        uumaiLog.setUrl(tasker.getUrl());
        uumaiLog.setRuntime(new UumaiTime().getNowString());
        uumaiLog.setResult(true);
        return uumaiLog;
    }
    public void createLogInstanceFromException(UumaiLog uumaiLog ,CrawlerTasker tasker,Exception e){
        uumaiLog.setResult(false);
        StringBuffer sb=new StringBuffer();
        sb.append(e.getMessage());
        StackTraceElement [] messages=e.getStackTrace();
        if(messages!=null){
            for(int i=0;i<messages.length;i++){
                sb.append("ClassName:" + messages[i].getClassName());
                sb.append("getFileName:"+messages[i].getFileName());
                sb.append("getLineNumber:" + messages[i].getLineNumber());
                sb.append("getMethodName:" + messages[i].getMethodName());
                sb.append("toString:"+messages[i].toString());
            }
        }

        uumaiLog.setErrMessage(sb.toString());
        uumaiLog.setProxy(tasker.getProxy().toString());
    }
    public void createLog(UumaiLog uumaiLog) {
        try {
//                    if(tasker.isSavingLogs()||!uumaiLog.isResult()){
            Json2DBHelper helper = new Json2DBHelper();
            helper.store(new Gson().toJson(uumaiLog),"uumai_system_logs");
//                    }
            //remove from back redis pool list
         } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use
            // File | Settings | File Templates.
        }
    }

    public int getLogs(String taskerOwner,String taskerName,String taskerSeries){

          return this.getLogs(taskerOwner,taskerName,taskerSeries,null);

    }
    public int getLogs(String taskerOwner,String taskerName,String taskerSeries,String result) {
         BasicDBObject query = new BasicDBObject();
        query.append("taskerName",taskerName);
        query.append("taskerOwner",taskerOwner);
        query.append("taskerSeries",taskerSeries);
        if(result!=null){
            if("true".equals(result)){
                query.append("result",true);
            }else{
                query.append("result",false);
            }
        }

        DB db = mongoUtil.getDB();
        DBCollection collection = db.getCollection("uumai_system_logs");
        return collection.find(query).count();
    }

    public void close(){
        if(mongoUtil!=null)
        mongoUtil.close();
    }


    }
