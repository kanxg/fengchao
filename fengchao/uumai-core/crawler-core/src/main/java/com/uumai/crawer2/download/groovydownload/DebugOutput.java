package com.uumai.crawer2.download.groovydownload;

/**
 * Created by rock on 11/19/16.
 */
public class DebugOutput {
    StringBuilder  output = new StringBuilder();

    public void append(Object s){
        if(s!=null)
            output.append("groovy> ").append(s.toString()).append('\n');
    }
    public String mergeOutput(){
//      return  StringUtli.HtmlEncode(output.toString());
        return output.toString();
    }
}
