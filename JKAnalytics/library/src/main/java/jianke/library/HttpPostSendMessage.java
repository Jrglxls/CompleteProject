package jianke.library;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by zhangjiajing on 2016/8/22.
 * 发送数据
 */
public class HttpPostSendMessage extends Thread {
    private Map<String, String> params;
    private String encode;
    private Context context;
    private Handler handler = new Handler();

    public HttpPostSendMessage(Map<String, String> params, String encode,Context context){
        this.params = params;
        this.encode = encode;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        submitPostData(params,encode,context);
    }

    /**
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public void submitPostData(Map<String, String> params, String encode,Context context) {
        final byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        String result = null;
        try {
            URL url = new URL("http://172.17.250.104/bigeater/appcontrail");
            final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);        //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");   //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = null;
                    try {
                        outputStream = httpURLConnection.getOutputStream();
                        outputStream.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            //获得服务器的响应码
            int response = httpURLConnection.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                result = "success";
            }else {
                result = "failure";
            }

            JKAnaliseDBOperate jkAnaliseDBOperate = new JKAnaliseDBOperate(context);
            int maxId = jkAnaliseDBOperate.getMaxId();
            if (result.equals("success")){
                jkAnaliseDBOperate.delete(maxId);
            }else if (result.equals("failure")){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer(); //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);  //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
}
