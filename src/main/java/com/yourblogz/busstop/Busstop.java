package com.yourblogz.busstop;

public class Busstop {
    /**
     * 城市代号
     */
    private int cityCode;
    /**
     * 公交车名称
     */
    private String busName;
    /**
     * 班次开车时间
     */
    private String startTime;
    /**
     * 班次落班时间
     */
    private String endTime;
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
    
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getCityCode() {
        return cityCode;
    }
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
    public String getBusName() {
        return busName;
    }
    public void setBusName(String busName) {
        this.busName = busName;
    }
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
