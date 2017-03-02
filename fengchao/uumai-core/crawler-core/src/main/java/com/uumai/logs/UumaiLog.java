package com.uumai.logs;
/**
 * Created by rock on 8/15/15.
 */
public class UumaiLog {
    private String taskerName;
    private String taskerSeries;
    private String taskerOwner;
    private String runHost;

    private String url;

    private boolean result;
    private String errMessage;
    private String runtime;

    private String proxy;

    public UumaiLog(){
    }

    public String getTaskerOwner() {
        return taskerOwner;
    }

    public void setTaskerOwner(String taskerOwner) {
        this.taskerOwner = taskerOwner;
    }

    public String getRunHost() {
        return runHost;
    }

    public void setRunHost(String runHost) {
        this.runHost = runHost;
    }

    public String getTaskerName() {
        return taskerName;
    }

    public void setTaskerName(String taskerName) {
        this.taskerName = taskerName;
    }

    public String getTaskerSeries() {
        return taskerSeries;
    }

    public void setTaskerSeries(String taskerSeries) {
        this.taskerSeries = taskerSeries;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
}
