package com.jianke.android.webengine;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by season on 16/7/15.
 */
public class ResultParserTest {

    @Test
    public void testParseList(){
        String json = "{code: 0, msg: 'none', data: ['hello', 'world']}";
        ResultParser<List<String>> parser = new ResultParser(json);
        ResponseResult<List<String>> result = parser.parser(new TypeToken<ResponseResult<List<String>>>(){});
        assertEquals(0, result.getCode());
        assertEquals("none", result.getMsg());
        assertEquals(2, result.getData().size());
        assertEquals("hello", result.getData().get(0));
        assertEquals("world", result.getData().get(1));
    }

    @Test
    public void testParserObject(){
        String json = "{code: 0, msg: '', data: {'id': 'id str', 'value': 'value str'}}";
        ResultParser<TestObject> parser = new ResultParser<>(json);
        ResponseResult<TestObject> result = parser.parser(new TypeToken<ResponseResult<TestObject>>(){});
        assertEquals(0, result.getCode());
        assertEquals("", result.getMsg());
    }

//    @Test
//    public void testGetParameterizedType(){
//        Map<String, Object> map = new HashMap<>();
//        map.getClass()
//    }


}
