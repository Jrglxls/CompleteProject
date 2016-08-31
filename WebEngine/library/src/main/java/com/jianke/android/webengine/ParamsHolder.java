package com.jianke.android.webengine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import github.nisrulz.easydeviceinfo.EasyAppMod;
import github.nisrulz.easydeviceinfo.EasyDeviceMod;
import github.nisrulz.easydeviceinfo.EasyDisplayMod;
import github.nisrulz.easydeviceinfo.EasyIdMod;
import github.nisrulz.easydeviceinfo.EasyNetworkMod;

import static github.nisrulz.easydeviceinfo.EasyNetworkMod.CELLULAR_2G;
import static github.nisrulz.easydeviceinfo.EasyNetworkMod.CELLULAR_3G;
import static github.nisrulz.easydeviceinfo.EasyNetworkMod.CELLULAR_4G;
import static github.nisrulz.easydeviceinfo.EasyNetworkMod.WIFI_WIFIMAX;

/**
 * Created by season on 16/7/14.
 */
public class ParamsHolder {

    public final static String TAG = "PARAMS";

    private static ParamsHolder instance;

    private Context context;

    private String udid;
    private String adid;
    private String brand;
    private String model;
    private String screen;
    //    private String os;
    private String osVersion;
    private String clientVersion;
    private String clientBuild;
    private String channel;

    private EasyDeviceMod deviceMod;
    private EasyIdMod idMod;
    private EasyAppMod appMod;
    private EasyDisplayMod displayMod;
    private EasyNetworkMod networkMod;


    public synchronized static ParamsHolder getInstance(Context context) {
        if (instance == null) {
            instance = new ParamsHolder(context);
        }
        return instance;
    }

    private ParamsHolder(Context context) {
        this.context = context;
        EasyIdMod idMod = new EasyIdMod(context);
        idMod.getAndroidAdId(new EasyIdMod.AdIdentifierCallback() {
            @Override
            public void onSuccess(String adIdentifier, boolean adDonotTrack) {
                ParamsHolder.this.adid = adIdentifier;
            }
        });
    }

    public static Map<String, String> getDefaultParams(Context context){
        Map<String, String> result= new HashMap<>();
        ParamsHolder paramsHolder = ParamsHolder.getInstance(context);

        // 使用反射机制将ParamHolder所有String类型的属性写入到params中
        java.lang.reflect.Method[] methods = ParamsHolder.class.getDeclaredMethods();
        Pattern pattern = Pattern.compile("^get(\\w+)");
        for (java.lang.reflect.Method method : methods) {
            if (method.getReturnType().equals(String.class)) {
                Matcher matcher = pattern.matcher(method.getName());
                if (matcher.find()) {

                    String key = matcher.group(1);
                    key = key.substring(0,1).toLowerCase() + key.substring(1);
                    try {
                        String value = (String) method.invoke(paramsHolder);
                        if (value != null)
                            result.put(key, value);
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.w(TAG, "can't get param: " + key, e);
                    }
                }

            }
        }

        return result;
    }

    public String getUdid() {
        if (isEmpty(this.udid)) {
            this.udid = getDeviceMod().getIMEI();
        }
        return udid;
    }

    public String getAdid() {
        return adid;
    }

    public String getBrand() {
        if (isEmpty(this.brand)) {
            brand = getDeviceMod().getBuildBrand();
        }
        return brand;
    }

    public String getModel() {
        if (isEmpty(model)) {
            model = getDeviceMod().getModel();
        }
        return model;
    }

    public String getScreen() {
        if (isEmpty(screen)) {
            screen = getDisplayMod().getResolution();
        }
        return screen;
    }

    public String getOs() {
        return "Android";
    }

    public String getOsVersion() {
        if (isEmpty(osVersion)) {
            osVersion = getDeviceMod().getOSVersion();
        }
        return osVersion;
    }

    public String getClientVersion() {
        if (isEmpty(clientVersion)) {
            clientVersion = getAppMod().getAppVersion();
        }
        return clientVersion;
    }

    public String getClientBuild() {
        if (isEmpty(clientBuild)) {
            clientBuild = getAppMod().getAppVersionCode();
        }
        return clientBuild;
    }

    public String getNetworkType() {

        String result;

        int type = getNetworkMod().getNetworkType();

        switch (type) {
            case WIFI_WIFIMAX:
                result = "wifi";
                break;
            case CELLULAR_2G:
            case CELLULAR_3G:
            case CELLULAR_4G:
                result = "mobile";
                break;
            default:
                result = "unknown";
        }

        return result;
    }

    public String getChannel() {
        if (channel == null) {
            try {
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                channel = info.metaData.getString("UMENG_CHANNEL");
            } catch (PackageManager.NameNotFoundException e) {
                channel = "";
            }
        }
        return channel;
    }

    private boolean isEmpty(String str) {
        return "".equals(str) || str == null;
    }

    public EasyDeviceMod getDeviceMod() {
        if (deviceMod == null) {
            deviceMod = new EasyDeviceMod(context);
        }
        return deviceMod;
    }

    public EasyIdMod getIdMod() {
        if (idMod == null) {
            idMod = new EasyIdMod(context);
        }
        return idMod;
    }

    public EasyAppMod getAppMod() {
        if (appMod == null) {
            appMod = new EasyAppMod(context);
        }
        return appMod;
    }

    public EasyDisplayMod getDisplayMod() {
        if (displayMod == null) {
            displayMod = new EasyDisplayMod(context);
        }
        return displayMod;
    }

    public EasyNetworkMod getNetworkMod() {
        if (networkMod == null) {
            networkMod = new EasyNetworkMod(context);
        }
        return networkMod;
    }
}
