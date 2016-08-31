package com.jianke.android.webengine;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by season on 16/7/19.
 */
public class SignTest {

    SignService service = new SignService();

    @Test
    public void testSign(){

        Map<String, String> params = new HashMap<>();
        String sign = service.sign(params);
        assertEquals("wanted", sign);
    }
}
