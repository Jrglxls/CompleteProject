package jianke.library;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
 * Created by zhangjiajing on 2016/8/12.
 * extras参数设置
 */
public class JKSystemParamsHelper {
    private Context context;

    public String uuid;//设备唯一标识符
    public String udid;//设备唯一标识符
    public String adid;//广告唯一标识符
    public String brand;//设备品牌
    public String model;//设备型号
    public String screen;//设备屏幕尺寸
    public String os;//设备系统类型
    public String osVersion;//设备系统版本号
    public String clientVersion;//应用版本
    public String clientBuild;//客户端的编译版本号
    public String geo;//地理位置
    public String networkType;//网络类型
    public String channel;//下载渠道
    private EasyDeviceMod deviceMod;

    private EasyIdMod idMod;
    private EasyAppMod appMod;
    private EasyDisplayMod displayMod;
    private EasyNetworkMod networkMod;

    private static JKSystemParamsHelper instance;

    public synchronized static JKSystemParamsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new JKSystemParamsHelper(context);
        }
        return instance;
    }

    public JKSystemParamsHelper(Context context) {
        this.context = context;
        EasyIdMod idMod = new EasyIdMod(context);
        idMod.getAndroidAdId(new EasyIdMod.AdIdentifierCallback() {
            @Override
            public void onSuccess(String adIdentifier, boolean adDonotTrack) {
                JKSystemParamsHelper.this.adid = adIdentifier;
            }
        });
    }

    public static Map<String, String> getDefaultParams(Context context) {
        Map<String, String> result = new HashMap<>();
        JKSystemParamsHelper jkSystemParamsHelper = JKSystemParamsHelper.getInstance(context);

        // 使用反射机制将JKSystemParamsHelper所有String类型的属性写入到params中
        java.lang.reflect.Method[] methods = JKSystemParamsHelper.class.getDeclaredMethods();
        Pattern pattern = Pattern.compile("^get(\\w+)");
        for (java.lang.reflect.Method method : methods) {
            if (method.getReturnType().equals(String.class)) {
                Matcher matcher = pattern.matcher(method.getName());
                if (matcher.find()) {
                    String key = matcher.group(1);
                    key = key.substring(0, 1).toLowerCase() + key.substring(1);
                    try {
                        String value = (String) method.invoke(jkSystemParamsHelper);
                        if (value != null)
                            result.put(key, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w("PARAMS", "can't get param: " + key, e);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 设备唯一标识符
     */
    public String getUuid() {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        uuid = deviceUuid.toString();
        return uuid;
    }

    /**
     * 设备唯一标识符
     */
    public String getUdid() {
        if (isEmpty(this.udid)) {
            this.udid = getDeviceMod().getIMEI();
        }
        return udid;
    }

    /**
     * 广告唯一标识符
     */
    public String getAdid() {
        this.adid = "";
        return adid;
    }

    /**
     * 设备品牌
     */
    public String getBrand() {
        if (isEmpty(this.brand)) {
            brand = getDeviceMod().getBuildBrand();
        }
        return brand;
    }

    /**
     * 设备型号
     */
    public String getModel() {
        if (isEmpty(model)) {
            model = getDeviceMod().getModel();
        }
        return model;
    }

    /**
     * 设备屏幕尺寸
     */
    public String getScreen() {
        if (isEmpty(screen)) {
            screen = getDisplayMod().getResolution();
        }
        return screen;
    }

    /**
     * 设备系统类型
     */
    public String getOs() {
        os = "Android";
        return os;
    }

    /**
     * 设备系统版本号
     */
    public String getOsVersion() {
        if (isEmpty(osVersion)) {
            osVersion = getDeviceMod().getOSVersion();
        }
        return osVersion;
    }

    /**
     * 应用版本
     */
    public String getClientVersion() {
        if (isEmpty(clientVersion)) {
            clientVersion = getAppMod().getAppVersion();
        }
        return clientVersion;
    }

    /**
     * 客户端的编译版本号
     */
    public String getClientBuild() {
        if (isEmpty(clientBuild)) {
            clientBuild = getAppMod().getAppVersionCode();
        }
        return clientBuild;
    }

    /**
     * 获取地理位置
     */
    public String getLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = "";
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        JSONObject address = new JSONObject();
        try {
            if(location != null){
                address.put("lng", location.getLongitude());
                address.put("lat", location.getLatitude());
            }else {
                address.put("lng", "0000000000");
                address.put("lat", "0000000000");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        geo = address.toString();
        return geo;
    }

    /**
     * 网络类型
     */
    public String getNetworkType() {
        int type = getNetworkMod().getNetworkType();
        switch (type) {
            case WIFI_WIFIMAX:
                networkType = "WIFI";
                break;
            case CELLULAR_2G:
            case CELLULAR_3G:
            case CELLULAR_4G:
                networkType = "Mobile";
                break;
            default:
                networkType = "unknown";
        }
        return networkType;
    }

    /**
     * 下载渠道
     * @return
     */
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
