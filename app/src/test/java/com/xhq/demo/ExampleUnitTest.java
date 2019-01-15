package com.xhq.demo;


import com.xhq.demo.tools.appTools.DeviceUtils;
import com.xhq.demo.tools.netTools.IPUtil;
import com.xhq.demo.tools.netTools.NetUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect(){
        assertEquals(4, 2 + 2);
    }


    @Test
    public void isValidateIp(){
//        boolean bool = IPUtils.isIPAddress("101.132.106.7");
        boolean bool = NetUtil.isValidUrl("www.baidu.com");
        System.out.printf("%b", bool);
    }

    @Test
    public void testIP(){
        boolean bool = IPUtil.ipCheck("47.110.200.202");
        System.out.printf("%b", bool);
    }


    @Test
    public void getLanguage(){
        String s1 = DeviceUtils.getLanguageDef();
        String s2 = DeviceUtils.getLocalName();
        String s3 = DeviceUtils.getLocaleCountry();
        System.out.printf("%s", s1);
        System.out.print("\n");
        System.out.printf("%s", s2);
        System.out.print("\n");
        System.out.printf("%s", s3);
    }

}