package com.uumai.crawer2.download.groovydownload;

import org.apache.commons.lang.StringEscapeUtils;

public class StringUtli {

    public static String HtmlEncode(String theString) {
        theString= StringEscapeUtils.escapeHtml(theString);
        // theString = theString.replaceAll(">", "&gt;");
        // theString = theString.replaceAll("<", "&lt;");
        // theString = theString.replaceAll(" ", " &nbsp;");
        // theString = theString.replaceAll(" ", " &nbsp;");
        // theString = theString.replaceAll("\"", "&quot;");
        // theString = theString.replaceAll("\'", "&#39;");
        theString = theString.replaceAll("\r", "&#13;");
        theString = theString.replaceAll("\n", "&#10;");
        //
        return theString;
    }

    public static String HtmlDiscode(String theString) {
        return StringEscapeUtils.unescapeHtml(theString);

        // theString = theString.replaceAll("&gt;", ">");
        // theString = theString.replaceAll("&lt;", "<");
        // theString = theString.replaceAll("&nbsp;", " ");
        // theString = theString.replaceAll(" &nbsp;", " ");
        // theString = theString.replaceAll("&quot;", "\"");
        // theString = theString.replaceAll("&#39;", "\'");
        // theString = theString.replaceAll("&#13;", "\r");
        // theString = theString.replaceAll("&#10;", "\n");
        // return theString;
    }
}