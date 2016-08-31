package com.jianke.android.webengine;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by season on 16/7/16.
 */

public class RegexTest {
    @Test
    public void testRegexWord(){
        String regex = "^\\b(is|get)[\\w_\\d]+$";
        assertTrue("isBoolean".matches(regex));
        assertTrue("getProp".matches(regex));
        assertFalse("get ".matches(regex));
        assertTrue("get1213".matches(regex));
        assertTrue("geta_b".matches(regex));
    }
}
