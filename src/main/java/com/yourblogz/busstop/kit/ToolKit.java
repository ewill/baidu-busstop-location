package com.yourblogz.busstop.kit;

import java.text.DecimalFormat;

public class ToolKit {
    
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static final String doubleToStr(double d) {
        return DECIMAL_FORMAT.format(d);
    }
    
    public static final String longToLocation(long l, int size) {
        String strLong = String.valueOf(l);
        int tmpLength = strLong.length() - Math.abs(size);
        if (tmpLength > 0 && tmpLength < strLong.length()) {
            return String.format("%s.%s", strLong.substring(0, tmpLength), strLong.substring(tmpLength));
        } else {
            return strLong;
        }
    }
}
