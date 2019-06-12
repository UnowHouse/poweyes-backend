package top.poweyes.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  @项目名：  poweyes
 *  @包名：    top.poweyes.utils
 *  @文件名:   DataUtils
 *  @创建者:   ouyangxiong
 *  @创建时间:  2019-04-23 23:51
 *  @描述：    数据解析工具
 */
public class DataUtils {
    /**
     * 驼峰法转下划线
     *
     * @param line
     *            源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase()
                .concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static Integer getTimeClass(Long timeStamp){
        Date date = new Date(timeStamp);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int i = instance.get(Calendar.HOUR_OF_DAY);
        if( i >= 6 && i < 8){
            return 0;
        }else if( i >= 8 && i < 11){
            return 1;
        }else if (i >= 11 && i < 14){
            return 2;
        }else if (i >= 14 && i < 18){
            return 3;
        }else if (i >= 18 && i < 20){
            return 4;
        }else {
            return 5;
        }

    }

    public static Integer getTimeHour(Long startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isOtherLocationType(String locationType){
        switch (locationType){
            case "学校食堂":
                return false;
            case "大型商超":
                return false;
            case "政府大门":
                return false;
            case "校园大门":
                return false;
            case "交通枢纽":
                return false;
            default:
                return true;
        }
    }

    public static void demo(){
        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int day = instance.get(Calendar.DATE);
        System.out.println(instance.toString());
        System.out.println(instance.get(Calendar.DAY_OF_MONTH));
        System.out.println(instance.getTimeInMillis());
        System.out.println(date.getTime());
        System.out.println(new Date(instance.getTimeInMillis()));

    }
    public static Long surplusDayTime(Long current){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        Date dayEnd = calendar.getTime();
        Long duration = dayEnd.getTime()-current;
        return duration;
    }

    public static void main(String [] args) throws ParseException {
//        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        Long time = new Long("1558592440214");
//        String d = format.format(time);
//        Date date=format.parse(d);
//        Calendar instance = Calendar.getInstance();
//        instance.setTime(date);
//        int i = instance.get(Calendar.HOUR_OF_DAY);
//        System.out.println(i);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.set(Calendar.HOUR_OF_DAY, 24);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
//        Date start = calendar.getTime();

    }
}
