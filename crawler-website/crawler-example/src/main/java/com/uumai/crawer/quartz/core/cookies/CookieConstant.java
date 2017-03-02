package com.uumai.crawer.quartz.core.cookies;

import java.io.File;
import java.util.List;

import com.uumai.crawer2.CookieManager.CookieHelper;
import com.uumai.crawer2.CookieManager.CrawlerCookie;

public class CookieConstant {
	static CookieHelper cookieHelper =new  CookieHelper();

	public static List<CrawlerCookie> xueqiu_cookie = cookieHelper.readcookiefromfile(new File(System.getProperty("user.home")+"/cookies/xueqiu_cookies.txt"));

	public static List<CrawlerCookie> amazon_cookie =  cookieHelper.readcookiefromfile(new File( System.getProperty("user.home")+"/cookies/jd_cookies.txt"));
			
 
	public static List<CrawlerCookie> jd_cookie=cookieHelper.readcookiefromfile(new File(  System.getProperty("user.home")+"/cookies/jd_cookies.txt"));

 	
	
	public static List<CrawlerCookie> fiveonejob_cookie=cookieHelper.readcookiefromString("");

	public static  List<CrawlerCookie> linkedin_cookie=cookieHelper.readcookiefromfile(new File(System.getProperty("user.home")+"/resources/linkedin_cookies.txt"));

 
}
