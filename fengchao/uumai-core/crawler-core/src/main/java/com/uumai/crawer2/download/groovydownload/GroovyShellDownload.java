package com.uumai.crawer2.download.groovydownload;

import com.uumai.crawer2.CrawlerResult;
import com.uumai.crawer2.CrawlerTasker;
import com.uumai.crawer2.download.Download;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rock on 11/18/16.
 */
public class GroovyShellDownload implements Download {

    private static String printtouumai_result="_uumai_println_kxgGroovyDebug_output";
    @Override
    public CrawlerResult download(CrawlerTasker tasker) throws Exception {
        CrawlerResult result=new CrawlerResult();

        try {
            //		println code
            String code=StringUtli.HtmlDiscode(tasker.getUrl());
            //		println code
            code=code.replaceAll("println", printtouumai_result+".append  ");
            //		code=code+"; return _kxgGroovyDebug_output";

            DebugOutput output = new DebugOutput();
            output.append("result is:");

            Binding binding = new Binding();
            binding.setVariable(printtouumai_result,output);
//            binding.setVariable("searchTask",SearchTaskFactory.getInstance());
            GroovyShell shell = new GroovyShell(binding);
            Object value =shell.evaluate(code);

            result.setReturncode(200);
            result.setRawText(output.mergeOutput());

        } catch (Exception e1) {
            result.setRawText(e1.getMessage());
            result.setReturncode(-1);
         }
        return result;
    }

    public static void main(String[] args) throws  Exception{
        GroovyShellDownload download=new GroovyShellDownload();
        CrawlerTasker tasker=new CrawlerTasker();
        tasker.setUrl("println 3*5");
        String out=download.download(tasker).getRawText().toString();
        System.out.println(out);
    }
}
