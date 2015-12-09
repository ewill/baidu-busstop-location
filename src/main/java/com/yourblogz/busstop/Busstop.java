package com.yourblogz.busstop;

public class Busstop {
    /**
     * 公交车所属公司
     */
    private String companyName;
    /**
     * 线路名称
     */
    private String buslineName;
    /**
     * 站点名称
     */
    private String busstopName;
    /**
     * 站点序号
     */
    private String busstopNum;
    /**
     * 途经公交车列表，逗号分隔
     */
    private String passBusses;
    /**
     * 站点经度
     */
    private String lng;
    /**
     * 站点纬度
     */
    private String lat;
    
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getBuslineName() {
        return buslineName;
    }
    public void setBuslineName(String buslineName) {
        this.buslineName = buslineName;
    }
    public String getBusstopName() {
        return busstopName;
    }
    public void setBusstopName(String busstopName) {
        this.busstopName = busstopName;
    }
    public String getBusstopNum() {
        return busstopNum;
    }
    public void setBusstopNum(String busstopNum) {
        this.busstopNum = busstopNum;
    }
    public String getPassBusses() {
        return passBusses;
    }
    public void setPassBusses(String passBusses) {
        this.passBusses = passBusses;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
}
