package com.jianke.android.webengine.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.jianke.android.webengine.ResponseResult;
import com.jianke.android.webengine.WebEngineRequest;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = "Main";

    private RequestQueue mRequestQueue;

    private Button stringButton;

    private Button arrayButton;

    private Button mapButton;

    private Button signButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stringButton = (Button) findViewById(R.id.stringBtn);
        mapButton = (Button) findViewById(R.id.mapBtn);
        arrayButton = (Button) findViewById(R.id.arrayBtn);
        signButton = (Button) findViewById(R.id.signBtn);

        stringButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        arrayButton.setOnClickListener(this);
        signButton.setOnClickListener(this);

    }


    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.stringBtn:
                getRequestQueue().add(
                        new WebEngineRequest<String>(this, "http://172.17.30.10:8080/test/string",
                                new Response.Listener<ResponseResult<String>>() {
                                    @Override
                                    public void onResponse(ResponseResult<String> resu) {
                                        Log.d(TAG, "code: " + resu.getCode());
                                        Log.d(TAG, "msg: " + resu.getMsg());
                                        Log.d(TAG, "data: " + resu.getData());
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        Log.e(TAG, "error:" + volleyError.getMessage(), volleyError);
                                    }
                                }, new TypeToken<ResponseResult<String>>(){}));
                break;
            case R.id.mapBtn:
                getRequestQueue().add(
                        new WebEngineRequest<Map<String, Object>>(this, "http://172.17.30.10:8080/test/map",
                                new Response.Listener<ResponseResult<Map<String, Object>>>() {
                                    @Override
                                    public void onResponse(ResponseResult<Map<String, Object>> resu) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        Log.e(TAG, "error:" + volleyError.getMessage(), volleyError);
                                    }
                                }, new TypeToken<ResponseResult<Map<String, Object>>>(){}));
                break;
            case R.id.arrayBtn:
                getRequestQueue().add(
                        new WebEngineRequest<List<String>>(this, "http://172.17.30.10:8080/test/array",
                                new Response.Listener<ResponseResult<List<String>>>() {
                                    @Override
                                    public void onResponse(ResponseResult<List<String>> resu) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        volleyError.printStackTrace();
                                        Log.e(TAG, "error:" + volleyError.getMessage(), volleyError);
                                    }
                                }, new TypeToken<ResponseResult<List<String>>>(){}));
                break;

            case R.id.signBtn:

                new WebEngineRequest<List<String>>(this, "http://172.17.30.10:8080/test/array",
                        new Response.Listener<ResponseResult<List<String>>>() {
                            @Override
                            public void onResponse(ResponseResult<List<String>> resu) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                volleyError.printStackTrace();
                                Log.e(TAG, "error:" + volleyError.getMessage(), volleyError);
                            }
                        }, new TypeToken<ResponseResult<List<String>>>(){});
                break;
            default:
                ;
        }

    }
}
