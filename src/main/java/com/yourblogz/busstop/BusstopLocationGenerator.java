package com.yourblogz.busstop;

import java.net.URLEncoder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yourblogz.busstop.consts.Consts;
import com.yourblogz.busstop.kit.ToolKit;

public class BusstopLocationGenerator {
    
    private final int cityCode;
    private final String keyword;
    private final String stationName;
    private int connectionTimeout;
    private static final Logger log = LoggerFactory.getLogger(BusstopLocationGenerator.class);

    public BusstopLocationGenerator(int cityCode, String stationName, String keyword) {
        this.cityCode = cityCode;
        this.keyword = keyword;
        this.stationName = stationName;
    }
    
    public void generate(Busstop busstop) {
        try {
            Document doc = Jsoup.connect(String.format(Consts.BUSSTOP_LOCATION_REQUEST_URL, cityCode, URLEncoder.encode(keyword, "UTF-8")))
                                .ignoreContentType(true)
                                .timeout(connectionTimeout)
                                .get();
            JSONArray result = (JSON.parseObject(doc.select("body").text().trim())).getJSONArray("content");
            if (CollectionUtils.isNotEmpty(result)) {
                for (int i = 0; i < result.size(); i++) {
                    JSONObject row = result.getJSONObject(i);
                    String addr = row.getString("addr");
                    
                    if ((stationName.equals(row.getString("name") + "") && row.getIntValue("catalogID") == Consts.CATALOG_BUSSTOP)
                        || (row.getIntValue("catalogID") == Consts.CATALOG_BUSSTOP && StringUtils.isNotBlank(addr) && addr.contains(busstop.getBusName()))) {
                        busstop.setPassBusses(addr);
                        busstop.setLng(ToolKit.longToLocation(row.getLongValue("x"), 2));
                        busstop.setLat(ToolKit.longToLocation(row.getLongValue("y"), 2));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public BusstopLocationGenerator setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getCityCode() {
        return cityCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStationName() {
        return stationName;
    }
    
}
