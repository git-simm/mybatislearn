package simm.learning.biztest;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {
    @Test
    public void ByteTest(){
        Byte b = Byte.parseByte("1");
        //b = 0x0F;
        System.out.println(b.intValue());
    }
    @Test
    public void formatTest(){
       //Date d1 = parse(null);
       Date d2 = parse("2018-08-16 18:03:35");
       Date d3 = parse("2018-08-16 18:03:45");
       //boolean b1 = d1.before(d2);
       boolean b2 = d3.before(d2);
       d2 = dateAddDay(d2,10);
       d2 = dateAddDay(d2,-12);
       Assert.assertTrue("结果相当",true);
    }
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String formatDate(Date date) {
        return sdf.format(date);
    }
    /**
     * 日期加几天
     */
    public Date dateAddDay(Date dt,Integer days){
        Calendar specialDate = Calendar.getInstance();
        specialDate.setTime(dt); //注意在此处将 specialDate 的值改为特定日期
        specialDate.add(Calendar.HOUR, days); //特定时间的1年后
        return specialDate.getTime();
    }
    public Date parse(String strDate) {
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
