package com.yourblogz.busstop.busline;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yourblogz.busstop.Busstop;
import com.yourblogz.busstop.BusstopLocationGenerator;
import com.yourblogz.busstop.consts.CityCode;
import com.yourblogz.busstop.consts.Consts;

/**
 * 根据城市代码和公交车名称获取线路列表
 * 如果输入的公交车名称不具体，则可能获取多条线路列表
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
                                Busstop busstop = new Busstop();
                                busstop.setBusName(busName);
                                busstop.setCityCode(cityCode);
                                busstop.setBusstopNum(String.valueOf(k + 1));
                                busstop.setBusstopName(((JSONObject)stations.get(k)).get("name").toString());
                                busstop.setCompanyName(busline.getString("company"));
                                busstop.setBuslineName(busline.getString("name").trim());
                                
                                List<String> searchList = new ArrayList<>();
                                searchList.add(String.format("%s-公交站", busstop.getBusstopName()));
                                searchList.add(String.format("%s %s-公交站", Consts.CITY_MAP.get(CityCode.C119), busstop.getBusstopName()));
                                searchList.add(String.format("%s %s %s-公交站", Consts.CITY_MAP.get(CityCode.C119), busName, busstop.getBusstopName()));
                                searchList.add(String.format("%s%s-公交站", busName, busstop.getBusstopName()));
                                
                                String searchKeyword = "";
                                while (searchList.size() != 0 && StringUtils.isNotBlank(searchKeyword = searchList.remove(0))) {
                                    new BusstopLocationGenerator(cityCode, busstop.getBusstopName(), searchKeyword).generate(busstop);
                                    if (StringUtils.isNotBlank(busstop.getLng())) {
                                        break;
                                    }
                                }
                                busstop.setSearchKeyword(searchKeyword);
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
