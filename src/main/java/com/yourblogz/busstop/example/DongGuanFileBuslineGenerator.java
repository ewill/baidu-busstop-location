package com.yourblogz.busstop.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yourblogz.busstop.Busstop;
import com.yourblogz.busstop.busline.FileBuslineGenerator;
import com.yourblogz.busstop.consts.CityCode;

public class DongGuanFileBuslineGenerator extends FileBuslineGenerator {
    
    private static final Logger log = LoggerFactory.getLogger(DongGuanFileBuslineGenerator.class);

    public DongGuanFileBuslineGenerator(String filePath) {
        super(CityCode.C119, filePath);
    }

    @Override
    protected void output(Busstop busstop) {
        log.info(String.format("\n站序：%s\n搜索关键字：%s\n城市代号：%d\n公交车名称：%s\n所属公司：%s\n线路编号：%s\n公交车站名称：%s\n途经公交车：%s\n墨卡x：%s,墨卡y：%s\n%s\n",
                busstop.getBusstopNum(),
                busstop.getSearchKeyword(),
                busstop.getCityCode(),
                busstop.getBusName(),
                busstop.getCompanyName(),
                busstop.getBuslineName(),
                busstop.getBusstopName(),
                busstop.getPassBusses(),
                busstop.getLng(),
                busstop.getLat(),
                "--------------------------------------------------------------------------------"
        ));
    }

}
