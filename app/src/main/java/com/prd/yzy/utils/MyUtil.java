package com.prd.yzy.utils;


import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyUtil {
    public MyUtil() {
    }

    public static String getShowNote(String str) {
        if ("1".equals(str)) {
            return "√";
            //return "<img src='question/tick.png'>";
        }
        if ("0".equals(str)) {
            return "";
            //return "<img src='question/cancel.png'>";
        }
        return "未收到";
    }

    public final static String GB2Uni(String pStr) {
        String retVal = null;
        try {
            retVal = new String(pStr.getBytes("GBK"), "ISO8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public final static String Uni2GB(String pStr) {
        String retVal = null;
        try {
            if (pStr != null)
                retVal = new String(pStr.getBytes("ISO8859-1"), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public final static String replace(String pStr) {
        String retVal = null;
        try {
            retVal = pStr;// .replaceAll("'","''");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;

    }

    public final static String encrypt(String pStr) throws Exception {
        String strRet = "";
        try {
            Md5 md5 = new Md5(pStr.getBytes());
            byte b[] = md5.getDigest();
            strRet = new String(Base64.encode(b));
        } catch (Exception e) {
            throw e;
        }
        return strRet;
    }

    public final static String encode64(String pStr) throws Exception {
        String strRet = "";
        try {
            strRet = new String(Base64.encode(pStr.getBytes()));
        } catch (Exception e) {
            throw e;
        }
        return strRet;
    }

    /**
     * 将ISO8859-1编码的字符串转成UTF-8编码的字符串
     *
     * @param src
     * @return
     */
    public static String iso2Utf8(String src) {
        String ret = src;
        if (ret != null && !src.equals("")) {
            try {
                ret = new String(src.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO 自动生成 catch 块
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 将单引号换成双单引号
     *
     * @param src
     * @return
     */
    public static String getDoubleQuotes(String src) {
        String ret = src;
        if (ret != null) {
            ret = ret.replaceAll("'", "''");
        }
        return ret;
    }

    /**
     * 将字符串转成HTML不会转意的内容
     *
     * @param src
     * @return
     */
    public static String getHtmlOutput(String src) {
        String ret = src;
        if (ret == null || ret.equals("")) {
            ret = "";
        } else {
            ret = ret.replaceAll("&", "&amp;");
            ret = ret.replaceAll("<", "&lt;");
            ret = ret.replaceAll(">", "&gt;");
            ret = ret.replaceAll("\"", "&quot;");
        }
        return ret;
    }

    /**
     * 将字符串转成HTML不会转意的内容，并将空字符串转成空格
     *
     * @param src
     * @return
     */
    public static String getHtmlOutput2(String src) {
        String ret = src;
        if (ret == null || ret.equals("")) {
            ret = "&nbsp;";
        } else {
            ret = ret.replaceAll("&", "&amp;");
            ret = ret.replaceAll("<", "&lt;");
            ret = ret.replaceAll(">", "&gt;");
            ret = ret.replaceAll("\"", "&quot;");
        }
        return ret;
    }

    /**
     * 日期格式的字符串转换成UTC时间
     *
     * @param dt
     * @return
     */
    public static long getUtcFromDateString(String dt) {
        long utc = 0;
        String dts = dt + " 上午 0:0";

        Date date = null;
        DateFormat df = DateFormat.getInstance();
        try {
            date = df.parse(dts);
            utc = (date.getTime() / 1000);
        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }

        return utc;
    }

    /**
     * 日期格式的字符串转换成UTC时间，但时间是一天的起点
     *
     * @param dt
     * @return
     */
    public static long getUtcFromDateStringBeginpoint(String dt) {
        long utc = 0;
        String dts = dt + " 上午 0:0";

        Date date = null;
        DateFormat df = DateFormat.getInstance();
        try {
            date = df.parse(dts);
            utc = (date.getTime() / 1000);
        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }

        return utc;
    }

    /**
     * 日期格式的字符串转换成UTC时间，时间为一天的末端，23点59分59秒，下一天回退一秒
     *
     * @param dt
     * @return
     */
    public static long getUtcFromDateStringEndpoint(String dt) {
        long utc = 0;
        String dts = dt + " 上午 0:0";

        Date date = null;
        DateFormat df = DateFormat.getInstance();
        try {
            date = df.parse(dts);
            date.setDate(date.getDate() + 1);
            utc = (date.getTime() / 1000);
        } catch (ParseException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }

        return utc - 1;
    }

    /**
     * 从UTC时间取得时间字符串
     *
     * @param utc
     * @return
     */
    public static String getDateStringFromUtc(long utc) {
        String result = null;
        long lutc = utc * 1000L;
        Calendar cal = Calendar.getInstance();
        Date date = new Date(lutc);
        cal.setTime(date);
        result = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1)
                + "-" + cal.get(Calendar.DAY_OF_MONTH) + " "
                + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
        return result;
    }

    // //毫秒转化成度
    public static String tranMsToFMLati(int d) {
        String formatStr = "";
        if (d < 0) {
            d = -d;
            formatStr = "-";
        }
        int miao = d / 1000;
        int du = miao / 3600;
        int fe = miao / 60 - du * 60;
        int ma = miao - fe * 60 - du * 60 * 60;
        formatStr += (du < 10 ? "0" + du : "" + du) + "°"
                + (fe < 10 ? "0" + fe : "" + fe) + "′"
                + (ma < 10 ? "0" + ma : "" + ma) + "″";

        return formatStr;
    }

    /**
     * 字符串转成UTC
     */
    public static long strFomatUtc(String time) {
        long utc = 0;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        if (time != null || time.length() > 0) {
            try {
                Date d = sf.parse(time);
                utc = d.getTime() / 1000;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return utc;
    }

    public static long getDay() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        long utc = 0;
        try {
            d = sf.parse(sf.format(new Date()));
            utc = d.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return utc;
    }

    // 返回日期格式：ex 1999－01－22 ，张维波添加2011－5－24。
    public static String getCurDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public static String strFormateDate(String str) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(str);
        return formatter.format(date);
    }

    public static void main(String[] args) throws Exception {
        // String a="asdfsfd'a'sdfs'";
        // String b="asdf";
        // System.out.println(replace(a));
        // System.out.println(replace(b));
        // String str="mcc";
        // System.out.println(encrypt(str));

        // System.out.println("MyUtil.main():" +
        // getUtcFromDateString("2007-03-22"));
        // System.out.println( getDay());
        System.out.println(encrypt("123"));
        System.out.println(MyUtil.getUtcFromDateString("2012-12-12"));

    }

}
