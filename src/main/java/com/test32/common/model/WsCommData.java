package com.test32.common.model;

public class WsCommData {
    private String g;   // gameId
    private String t;   // message type
    private Integer s;   // state
    private Object d;   // message data

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Object getD() {
        return d;
    }

    public void setD(Object d) {
        this.d = d;
    }

    public String getG()
    {
        return g;
    }

    public void setG(String g)
    {
        this.g = g;
    }

    @Override
    public String toString()
    {
        return "WsCommData{" +
                "g='" + g + '\'' +
                ", t='" + t + '\'' +
                ", s=" + s +
                ", d=" + d +
                '}';
    }
}