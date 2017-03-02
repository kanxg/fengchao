package com.uumai.crawer.quartz.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.uumai.crawer.quartz.result.QuartzResult;
import com.uumai.crawer.quartz.result.QuartzResultItem;
import com.uumai.crawer.util.MongoUtil;
import com.mongodb.*;
import com.mongodb.util.JSON;

public class UumaiResultUtil {

	MongoUtil mongoUtil=new MongoUtil();
//	private DB db = null;
	
	public UumaiResultUtil(){
//		db = mongoUtil.getDB();
	}
	public void close(){
		mongoUtil.close();
	}

	public void createIndex(String tablename,String column){
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		BasicDBObject query = new BasicDBObject();
		query.append(column, 1);
		collection.createIndex(query);
 	}
	public void removeColumnFromTable(String tablename,List<QuartzQueryItem> paramslist, String column) {
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		
		BasicDBObject query = null;
		query = new BasicDBObject();
		if (paramslist != null) {
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}
		
		query.append(column, 1);
		
		DBObject update = new BasicDBObject();
		update.put("$unset", new BasicDBObject(column, 1));

		collection.update(query, update,true,false);
 	}

	public void droptable(String tablename) {
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		collection.drop();
 	}

	public List<QuartzResult> getDiscountResult(String tablename,
			String columnname) {
		return this.getDiscountResult(tablename, columnname, null);
	}

	public List<QuartzResult> getDiscountResult(String tablename,
			String columnname, DBObject query) {

		List<QuartzResult> result = new ArrayList<QuartzResult>();
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		List ret = null;

		if (query == null) {
			ret = collection.distinct(columnname);
		} else {
			ret = collection.distinct(columnname, query);
		}
		for (int i = 0; i < ret.size(); i++) {
			if (ret.get(i) == null)
				continue;
//			System.out.println(ret.get(i));
			QuartzResult QuartzResultItem = new QuartzResult();
			QuartzResultItem.getItemlist().add(
					new QuartzResultItem(columnname, ret.get(i).toString()));
		}
 		return result;
	}

	public void deletereNullsult(String tablename, String... columnname) {
		BasicDBObject query = new BasicDBObject();
		for (String column : columnname) {
			query.append(column, null);
		}

		this.deleteresult(tablename, query);
	}

	public void deleteresult(String tablename) {
		BasicDBObject query = null;
		this.deleteresult(tablename, query);
	}
	public void deleteById(String tablename,Object id) {
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		this.deleteresult(tablename, query);
 
	}
	
	public void deleteresult(String tablename, List<QuartzQueryItem> paramslist) {

		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}

		this.deleteresult(tablename, query);
	}

	public void deleteresult(String tablename, BasicDBObject query) {
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		DBCursor ret = null;
		if (query == null) {
			ret = collection.find();
		} else {
			ret = collection.find(query);
		}

		while (ret.hasNext()) {

			BasicDBObject bdbObj = (BasicDBObject) ret.next();
			collection.remove(bdbObj);

		}
		;
	}
	public DBCursor getresultCursor(String tablename){
		return this.getresultCursor(tablename,null);
	}
			
	public DBCursor getresultCursor(String tablename,
			List<QuartzQueryItem> paramslist) {
		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		DBCursor ret = null;
		if (query == null) {
			ret = collection.find();
		} else {
			ret = collection.find(query);
		}
		ret.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		return ret;
		
		
	}
	
	public String  getPK(String tablename,
			List<QuartzQueryItem> paramslist) {
		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}
		}
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		BasicDBObject bdbObj = null;
		if (query == null) {
			bdbObj =  (BasicDBObject)collection.findOne();
		} else {
			bdbObj =  (BasicDBObject)collection.findOne(query);
		}
		;
		
		if(bdbObj!=null){
			return bdbObj.get("_id").toString();
		}
 		
		return null;
	}
	
	public boolean exist(String tablename,
			List<QuartzQueryItem> paramslist) {
		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}
		}
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		BasicDBObject bdbObj = null;
		if (query == null) {
			bdbObj =  (BasicDBObject)collection.findOne();
		} else {
			bdbObj =  (BasicDBObject)collection.findOne(query);
		}
		;
		
		if(bdbObj!=null){
			return true;
		}
 		
		return false;
	}
 	public QuartzResult getOneresult(String tablename,
			List<QuartzQueryItem> paramslist) {
		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		BasicDBObject bdbObj = null;
		if (query == null) {
			bdbObj =  (BasicDBObject)collection.findOne();
		} else {
			bdbObj =  (BasicDBObject)collection.findOne(query);
		}

		if(bdbObj!=null){
			QuartzResult QuartzResultItem = new QuartzResult();

			java.util.Iterator<Entry<String, Object>> iterator = bdbObj
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> mapEntry = iterator.next();
				String columnname = mapEntry.getKey();
				if (columnname.equals("_id"))
					continue;
				String value = mapEntry.getValue() == null ? null : mapEntry
						.getValue().toString();
				// System.out.println(" key:"+mapEntry.getKey());
				// System.out.println(" value:"+mapEntry.getValue());
				QuartzResultItem.getItemlist().add(
						new QuartzResultItem(columnname, value));
			}
			return QuartzResultItem;
		}
 		
		return null;
	}
	public List<QuartzResult> getresult(String tablename) {
		return this.getresult(tablename, null);
	}

	public List<QuartzResult> getresult(String tablename,
			List<QuartzQueryItem> paramslist) {

		List<QuartzResult> result = new ArrayList<QuartzResult>();

		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		DBCursor ret = null;
		if (query == null) {
			ret = collection.find();
		} else {
			ret = collection.find(query);
		}

		while (ret.hasNext()) {
			QuartzResult QuartzResultItem = new QuartzResult();

			BasicDBObject bdbObj = (BasicDBObject) ret.next();
			java.util.Iterator<Entry<String, Object>> iterator = bdbObj
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> mapEntry = iterator.next();
				String columnname = mapEntry.getKey();
				if (columnname.equals("_id"))
					continue;
				String value = mapEntry.getValue() == null ? null : mapEntry
						.getValue().toString();
				// System.out.println(" key:"+mapEntry.getKey());
				// System.out.println(" value:"+mapEntry.getValue());
				QuartzResultItem.getItemlist().add(
						new QuartzResultItem(columnname, value));
			}
			result.add(QuartzResultItem);

			// DBObject DBresult=(DBObject)bdbObj.get("result");
			// if(DBresult==null) continue;
			//
			// JsonParser parser = new JsonParser();
			// JsonArray obj=(JsonArray)parser.parse(DBresult.toString());
			// if(obj!=null&&obj.size()!=0){
			// for (int j = 0; j < obj.size(); j++){
			// JsonObject objjson = obj.get(j).getAsJsonObject();
			// java.util.Iterator<Entry<String, JsonElement>> iterator=
			// objjson.entrySet().iterator();
			// while(iterator.hasNext()){
			// Entry<String, JsonElement> mapEntry= iterator.next();
			// // System.out.println("objjson key:"+mapEntry.getKey());
			// // System.out.println("objjson value:"+mapEntry.getValue());
			// QuartzResultItem quartzResultItem=new QuartzResultItem();
			// quartzResultItem.setName(mapEntry.getKey());
			// quartzResultItem.setValue(objjson.get(mapEntry.getKey()).getAsString());
			// result.add(quartzResultItem);
			// }
			//
			// // for(String objjson.)
			// //
			// }
			//
			// }
			//

		}
 		
		return result;
	}
	
	public long getresultCount(String tablename) {
		return this.getresultCount(tablename, null);
	}
	
	
	public long getresultCount(String tablename,
			List<QuartzQueryItem> paramslist) {
		BasicDBObject query = null;
		if (paramslist != null) {
			query = new BasicDBObject();
			for (QuartzQueryItem item : paramslist) {
				query.append(item.getName(), item.getValue());
			}

		}

		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
 		if (query == null) {
			return  collection.getCount();
		} else {
			return  collection.getCount(query);
		}
   
	}

	public void copyresult(String fromtablename, String totablename,
			String pkname) {
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(fromtablename);
		DBCollection tocollection = db.getCollection(totablename);
		DBCursor ret = collection.find();

		List<QuartzQueryItem> paramslist = new ArrayList<QuartzQueryItem>();

		while (ret.hasNext()) {
			BasicDBObject bdbObj = (BasicDBObject) ret.next();
			Object pk = bdbObj.get(pkname);
			paramslist.clear();
			paramslist.add(new QuartzQueryItem(pkname, pk));
 			if (!exist(totablename, paramslist)) {
				tocollection.save(bdbObj);
			}
		}
 	}
	
	public BasicDBList group(String tablename,String key,DBObject cond){
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
	    BasicDBObject keyOjb = new BasicDBObject(key,true);  //分组KEY    
//        BasicDBObject cond = new BasicDBObject("id",new BasicDBObject(QueryOperators.GT,0));     
        BasicDBObject initial = new BasicDBObject("count",0); //统计字段    
        String reduce = "function (doc,prev){  prev.count++ }";  //计算函数
        BasicDBList group =  (BasicDBList) collection.group(keyOjb,cond,initial,reduce);      
        return group;
	}
	public void save2db(String tablename, List<QuartzQueryItem> list){
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		BasicDBObject obj=new BasicDBObject();
		for(QuartzQueryItem item:list){
			obj.append(item.getName(), item.getValue());
		}
		collection.save(obj);
	}
	
	public void saveone2db(String tablename, String json){
		DB db = mongoUtil.getDB();
		DBCollection collection = db.getCollection(tablename);
		 DBObject object = (DBObject)JSON.parse(json);
         collection.insert(new DBObject[] { object });
	}

	public static void main(String[] args) {
	 		UumaiResultUtil util = new UumaiResultUtil();
		// List<QuartzResult> results= util.getresult("xueqiuallsockets", null);
		// List<QuartzQueryItem> paramslist = new ArrayList<QuartzQueryItem>();
		// paramslist.add(new QuartzQueryItem("分类", null));
		// util.deleteresult("AmazonBestseller", paramslist);

		// util.getDiscountResult("AmazonBestsellerProduct", "ASIN", null);

		//util.copyresult("AmazonProduct_0824", "AmazonProduct", "ASIN");
		//util.removeColumnFromTable("eastmoney", null, "currentcrawlerdeepth");
		
//		for(int i=0;i<100;i++){
//			util.createIndex("jd_cate_price", "id"+i);
//		}
	 		BasicDBList group=util.group("linkedin_jianli_failed", "proxy", null);
	 		for(int i=0;i<group.size();i++){
	 			BasicDBObject obj=(BasicDBObject)group.get(i);
	 			System.out.println(obj);
	 			
	 		}
		
	}
}
