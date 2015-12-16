package com.yourblogz.busstop;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yourblogz.busstop.consts.Consts;

/**
 * <p>根据公交站点的UID获取坐标</p>
 */
public class BusstopLocationGenerator {
    
    private final String uid;
    private int connectTimeout;
    private static final Logger log = LoggerFactory.getLogger(BusstopLocationGenerator.class);

    public BusstopLocationGenerator(String uid) {
        this.uid = uid;
    }
    
    public void generate(Busstop busstop) {
        try {
            Document doc = Jsoup.connect(String.format(Consts.BUSSTOP_LOCATION_REQUEST_URL, uid))
                                .ignoreContentType(true)
                                .timeout(connectTimeout)
                                .get();
            JSONObject result = (JSON.parseObject(doc.select("body").text().trim())).getJSONObject("content");
            if (result != null) {
                busstop.setPassBusses(result.getString("addr"));
                String[] xy = (result.getString("geo").split("\\|")[2]).split(",");
                busstop.setLng(xy[0]);
                busstop.setLat(xy[1].replaceAll(";", ""));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public BusstopLocationGenerator setConnectTimeout(int connectionTimeout) {
        this.connectTimeout = connectionTimeout;
        return this;
    }
    
}
