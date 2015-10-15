package com.farmers.underground.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by samson
 * on 14.10.15.
 */
public class DateFormaterUtil {

    public static String convertDate(Calendar date){
        return new SimpleDateFormat("ccc dd.M.yy").format(date.getTime());
    }

    public static String convertDate(Date date){
        return new SimpleDateFormat("ccc dd.M.yy").format(date);
    }
}