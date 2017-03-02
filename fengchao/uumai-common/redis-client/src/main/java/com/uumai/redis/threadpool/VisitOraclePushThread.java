package com.uumai.redis.threadpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kanxg on 14-11-26.
 */
public class VisitOraclePushThread extends PopThread{

    public VisitOraclePushThread(String s){
        super(s);
    }

    public void dobusiness(String msg){
        System.out.println(" processing "+msg);

        try {
            URL url = new URL("http://rock-oracle:8080/uumai_rest");
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT)");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(is));

            String s;
            StringBuilder result = new StringBuilder();
            while (((s = reader.readLine()) != null)) {
            result.append(s);
            }

            System.out.println("result= " + result.toString());

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
