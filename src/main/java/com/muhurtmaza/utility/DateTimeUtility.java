package com.muhurtmaza.utility;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sandeep on 11/22/15.
 */

public class DateTimeUtility {


    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String VISIT_TIME = "hh:mm a";
    private static final String VISIT_DATE = "dd/MM";

    /**
     * Convert date from utc to local
     *
     * @param pDate
     * @return
     */
    public static Date convertToLocal(Date pDate) {
        TimeZone lTimeZone = TimeZone.getDefault();
        // Log.d("TAG", "UTC Date = " + pDate);
        // Date lDate = new Date(pDate.getTime() + lTimeZone.getRawOffset());

        // Log.d("TAG", "Local lDate2 = " + lDate);
        return new Date(pDate.getTime()
                + lTimeZone.getOffset(pDate.getTime()));
    }

    /**
     * Comvert date in universal time
     *
     * @param pDate
     * @return
     */
    public static Date convertToUTC(Date pDate) {
        TimeZone lTimeZone = TimeZone.getDefault();

        return new Date(pDate.getTime()
                - lTimeZone.getOffset(pDate.getTime()));
    }


}
