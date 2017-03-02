package com.uumai.crawer2.download.selenium;

import com.uumai.crawer2.CookieManager.CookieHelper;
import com.uumai.crawer2.CookieManager.CrawlerCookie;
import com.uumai.crawer2.CrawlerResult;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.download.CrawlerProxy;
import com.uumai.crawer2.download.Download;
import org.openqa.selenium.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class SeleniumDownloader implements Download {
    protected WebDriver webDriver=null;
    protected  CrawlerTasker tasker;
	public SeleniumDownloader() {

	}


	@Override
    public CrawlerResult download(CrawlerTasker tasker) throws Exception {
        this.tasker=tasker;
        URL url = new URL(tasker.getUrl());

        try {
            CrawlerProxy proxy=tasker.getProxy();
            if(proxy==null){
                webDriver = WebDriverFactory.getDriver(tasker.getDownloadType(), null);
            }else{
                webDriver = WebDriverFactory.getDriver(tasker.getDownloadType(), proxy.getProxyIpAndPortString());
            }

            WebDriver.Options manage = webDriver.manage();


            webDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);

            if(tasker.getCookies()!=null){

                if(tasker.getDownloadType().equals(DownloadType.jbrowser_download)){
                    webDriver.get(url.getProtocol() + "://" + url.getHost());
                    manage.deleteAllCookies();
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                   for(CrawlerCookie crawlerCookie:tasker.getCookies()){
                            js.executeScript("document.cookie = \""+crawlerCookie.getName()+"="+crawlerCookie.getValue()+";path=/;domain="+url.getHost()+"\"");
                    }

                }else if(tasker.getDownloadType().equals(DownloadType.phantomjs_download)){
                    webDriver.get(url.getProtocol() + "://" + url.getHost());
                    manage.deleteAllCookies();
                    JavascriptExecutor js = (JavascriptExecutor) webDriver;
                    for(CrawlerCookie crawlerCookie:tasker.getCookies()){
                        js.executeScript("document.cookie = \""+crawlerCookie.getName()+"="+crawlerCookie.getValue()+";path=/;domain="+url.getHost()+"\"");
                    }

                }else{
                    webDriver.get(url.getProtocol() + "://" + url.getHost());
                    manage.deleteAllCookies();
                    for(CrawlerCookie crawlerCookie:tasker.getCookies()){
                        String domain=crawlerCookie.getDomain();
                        if(domain==null||"".equals(domain)){
                            domain=url.getHost();
                        }
                        Cookie cookie = new Cookie(crawlerCookie.getName(),crawlerCookie.getValue(),domain,
                                crawlerCookie.getPath(),crawlerCookie.getExpiry(),crawlerCookie.isSecure(),crawlerCookie.isHttpOnly() );
                        manage.addCookie(cookie);
                    }
                }


            }

            webDriver.get(tasker.getUrl());
//            System.out.println(webDriver.getPageSource());

            superClassdone();

            return getResult();

        } catch(Exception ex){
           ex.printStackTrace();
            throw  ex;
        }  finally {
            try {
//                webDriver.close();
                webDriver.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    protected void superClassdone() {
    }

        protected CrawlerResult getResult(){
        CrawlerResult crawlerResult=new CrawlerResult();

        CookieHelper cookieHelper=new CookieHelper();
        crawlerResult.setCookies(cookieHelper.parseCookies(webDriver));

        if(tasker.getDownloadType().equals(DownloadType.jbrowser_download)){
            crawlerResult.setRawText(webDriver.getPageSource());
        }else{
            WebElement webElement = webDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("innerHTML");
            crawlerResult.setRawText(content);
        }
        crawlerResult.setReturncode(200);
        return crawlerResult;
    }

    public static void main(String[] args) throws  Exception{

        SeleniumDownloader downloader=new SeleniumDownloader();
        CrawlerTasker tasker=new CrawlerTasker();
        tasker.setDownloadType(DownloadType.firefox_download);
        tasker.setUrl("http://bbs.skykiwi.com/forum.php?mod=viewthread&tid=3280918");
//        tasker.setUrl("http://www.oracle.com");
        tasker.setProxy(new CrawlerProxy("cn-proxy.jp.oracle.com", 80));
        CookieHelper cookieHelper=new CookieHelper();

        List<CrawlerCookie> cookies = null ;//cookieHelper.readcookiefromfile(new File("/home/rock/uumai/cookies/skykiwi_cookies.txt"));
//
        tasker.setCookies(cookies);
        String html=downloader.download(tasker).getRawText();
//            System.out.println("html:"+ html);

    }
}
