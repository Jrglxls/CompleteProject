package com.jianke.android.webengine;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by season on 16/7/15.
 */
public class ResultParser<T> {

    private String json;


    public ResultParser(String json) {
        this.json = json;
    }

    public ResponseResult<T> parser(TypeToken<ResponseResult<T>> typeToken) throws JsonSyntaxException {
        ResponseResult<T> result = new ResponseResult<>();
//        JSON.parse(json, new TypeReference<ResponseResult<T>>(){});
        Gson gson = new Gson();
//        JSONObject jsonObject = new JSONObject(json);
//        result.setCode(jsonObject.getInt("code"));
//        result.setMsg(jsonObject.getString("msg"));
//
//        if (data.getClass().isArray()){
//            JSONArray jsonArray = new JSONArray()
//        } else  {
//
//        }
//
//        gson.fromJson(json, data.getClass());

        return gson.fromJson(json, typeToken.getType());

    }


}
