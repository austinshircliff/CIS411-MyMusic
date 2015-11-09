package com.wesleyreisz.mymusic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wesleyreisz on 11/8/15.
 */
public class StringUtil {
    public static String buildTopTenListUrl(String url, String param){
        return String.format(url,param);
    }

    public static Date getDate(String date2convert) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH");

        try {
            return formatter.parse(date2convert);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
