package com.xhq.demo;


import com.xhq.demo.tools.netTools.NetUtils;

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
        boolean bool = NetUtils.isValidUrl("www.baidu.com");
        System.out.printf("%b", bool);
    }
}