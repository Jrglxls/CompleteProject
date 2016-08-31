package com.jianke.android.webengine;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.jianke.android.webengine.ResultCodeConstants.COMMON_ERROR_CODE;

/**
 * Created by season on 16/7/14.
 */
public class WebEngineRequest<T> extends Request<ResponseResult<T>> {

    private static final String DEFAULT_KEY_NAME = "key";

    private Context mContext;
    private Map<String, String> mParams;
    private Map<String, Object> mBody;
    private Response.Listener<ResponseResult<T>> mListener;
    TypeToken<ResponseResult<T>> mTypeToken;

    public WebEngineRequest(Context context,
                            String url,
                            Response.Listener<ResponseResult<T>> listener,
                            Response.ErrorListener errorListener,
                            TypeToken<ResponseResult<T>> typeToken) {
        this (context, url, null,null, listener, errorListener, typeToken);
    }

    /**
     * ApiRequest构造
     *
     * @param context
     * @param url           接口url地址
     * @param params        请求参数
     * @param listener      接口调用成功的监听器
     * @param errorListener 接口调用失败的监听器
     */
    public WebEngineRequest(Context context,
                            String url,
                            Map<String, String> params,
                            Map<String, Object> body,
                            Response.Listener<ResponseResult<T>> listener,
                            Response.ErrorListener errorListener,
                            TypeToken<ResponseResult<T>> typeToken) {
        super(Request.Method.POST, url, errorListener);
        this.mContext = context;
        this.mListener = listener;
        this.mParams = params;
        this.mBody = body;
        this.mTypeToken = typeToken;
    }


    @Override
    protected Response<ResponseResult<T>> parseNetworkResponse(NetworkResponse response) {


        ResponseResult<T> result = new ResponseResult<>();

        try {
            String responseBody = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ResultParser<T> parser = new ResultParser<>(responseBody);
            result = parser.parser(this.mTypeToken);
        } catch (UnsupportedEncodingException e) {
            result.setCode(COMMON_ERROR_CODE);
            result.setMsg("unsupported encoding");
        } catch (JsonSyntaxException e) {
            result.setCode(COMMON_ERROR_CODE);
            result.setMsg("json transform error");
        }

        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(ResponseResult<T> response) {
        mListener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> result = new HashMap<>();
        Map<String, String> defaultParams = getDefaultParams();
        if (defaultParams != null) {
            result.putAll(defaultParams);
        }

        if (this.mBody != null){
            this.putParam("body", new Gson().toJson(mBody));
        }

        if (this.mParams != null) {
            result.putAll(this.mParams);
        }

        if (!StringUtil.isEmpty(getKeyValue())) {
            result = signParams(result);
        }
        return result;
    }

    private Map<String, String> signParams(Map<String, String> params) {
        String sign = SignUtil.genSign(params, getKeyName(), getKeyValue());
        params.put("sign", sign);
        return params;
    }


    protected void putParam(String name, String value){
        if (this.mParams == null){
            this.mParams = new HashMap<>();
        }
        mParams.put(name, value);
    }

    protected void putBody(String name, Object value){
        if (this.mBody == null) {
            this.mBody = new HashMap<>();
        }
        this.mBody.put(name, value);
    }

    protected String getKeyName() {
        return DEFAULT_KEY_NAME;
    }


    protected String getKeyValue() {
        return null;
    }

    protected Context getContext() {
        return mContext;
    }

    private Map<String, String> getDefaultParams(){
        return ParamsHolder.getDefaultParams(mContext);
    }

}

