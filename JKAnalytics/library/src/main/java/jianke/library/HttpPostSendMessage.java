package jianke.library;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by zhangjiajing on 2016/8/22.
 * 发送数据
 */
public class HttpPostSendMessage extends Thread {
    private Map<String, String> params;
    private Context context;

    public HttpPostSendMessage(Map<String, String> params,Context context){
        this.params = params;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();
        submitPostData(params,context);
    }

    /**
     * 发送Post请求到服务器
     */
    public void submitPostData(final Map<String, String> params,Context context) {
        String result = null;
        try {
            URL url = new URL("http://172.17.30.123:8080/bigeater/appcontrail");
            final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.connect();


            //获得输出流，向服务器写入数据
            byte[] postData = params.toString().getBytes();
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(postData);
            dataOutputStream.flush();
            dataOutputStream.close();

            //获得服务器的响应码
            int response = httpURLConnection.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                result = "success";
            }else {
                result = "failure";
            }

            //删除已上传数据
            JKAnaliseDBOperate jkAnaliseDBOperate = new JKAnaliseDBOperate(context);
            int maxId = jkAnaliseDBOperate.getMaxId();
            Log.d("zjj","删除 maxid = "+maxId);
            if (result.equals("success")){
                jkAnaliseDBOperate.delete(maxId);
            }else if (result.equals("failure")){

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
