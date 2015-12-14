package com.yourblogz.busstop.busline;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yourblogz.busstop.Busstop;
import com.yourblogz.busstop.BusstopLocationGenerator;
import com.yourblogz.busstop.consts.Consts;

/**
 * <p>根据城市代码和公交车名称获取线路列表</p>
 * <p>如果输入的公交车名称不具体，则可能获取多条线路列表</p>
 */
public abstract class BuslineGenerator {
    
    private final int cityCode;
    private final String[] busNames;
    private int connectTimeout;
    private static final Logger log = LoggerFactory.getLogger(BuslineGenerator.class);
    
    public BuslineGenerator(int cityCode, String busName) {
        this(cityCode, new String[] { busName });
    }

    public BuslineGenerator(int cityCode, String[] busNames) {
        this.cityCode = cityCode;
        this.busNames = busNames;
    }
    
    public int getConnectTimeout() {
        return connectTimeout;
    }

    public BuslineGenerator setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getCityCode() {
        return cityCode;
    }

    public String[] getBusNames() {
        return busNames;
    }
    
    public void start() {
        if (busNames == null || busNames.length == 0) return;
        for (String busName : busNames) {
            try {
                Document doc = Jsoup.connect(String.format(Consts.BUSSTOP_UID_REQUEST_URL, cityCode, busName))
                                    .ignoreContentType(true)
                                    .timeout(connectTimeout)
                                    .get();
                
                JSONArray result = JSON.parseObject(doc.select("body").text().trim()).getJSONArray("content");
                if (CollectionUtils.isNotEmpty(result)) {
                    for (int i = 0; i < result.size(); i++) {
                        String uid = ((JSONObject)result.get(i)).get("uid") + "";
                        
                        Document buslineDoc = Jsoup.connect(String.format(Consts.BUSLINE_REQUEST_URL, cityCode, uid))
                                                   .ignoreContentType(true)
                                                   .timeout(connectTimeout)
                                                   .get();
                        
                        JSONArray buslines = JSON.parseObject(buslineDoc.select("body").text().trim()).getJSONArray("content");
                        if (CollectionUtils.isNotEmpty(buslines)) {
                            JSONObject busline = buslines.getJSONObject(0);
                            JSONArray stations = busline.getJSONArray("stations");
                            for (int k = 0; k < stations.size(); k++) {
                                JSONObject station = stations.getJSONObject(k);
                                Busstop busstop = new Busstop();
                                busstop.setBusName(busName);
                                busstop.setCityCode(cityCode);
                                busstop.setStartTime(busline.getString("startTime"));
                                busstop.setEndTime(busline.getString("endTime"));
                                busstop.setBusstopNum(String.valueOf(k + 1));
                                busstop.setBusstopName(station.get("name").toString());
                                busstop.setCompanyName(busline.getString("company"));
                                busstop.setBuslineName(busline.getString("name").trim());
                                new BusstopLocationGenerator(station.get("uid").toString()).setConnectTimeout(connectTimeout).generate(busstop);
                                output(busstop);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected abstract void output(Busstop busstop);
}
