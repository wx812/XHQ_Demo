package com.xhq.common;

import com.xhq.common.constant.apiconfig.ApiEnum;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void EnumTest() throws Exception {
        ApiEnum.EducationBackground[] values = ApiEnum.EducationBackground.values();
        ApiEnum.EducationBackground preschool = ApiEnum.EducationBackground.valueOf("PRESCHOOL");
        System.out.println(preschool);
        for (ApiEnum.EducationBackground educationBackground : values) {
            System.out.println(educationBackground.name);
        }
        System.out.println(Arrays.toString(values));
    }


}