package com.glitter.spring.boot.util;

public class AspectInfo {

    private Integer order;

    private String aspectName;

    private String before;

    private String around;

    private String after;

    private String afterReturning;

    private String afterThrowing;


    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getAspectName() {
        return aspectName;
    }

    public void setAspectName(String aspectName) {
        this.aspectName = aspectName;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAround() {
        return around;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getAfterReturning() {
        return afterReturning;
    }

    public void setAfterReturning(String afterReturning) {
        this.afterReturning = afterReturning;
    }

    public String getAfterThrowing() {
        return afterThrowing;
    }

    public void setAfterThrowing(String afterThrowing) {
        this.afterThrowing = afterThrowing;
    }

}