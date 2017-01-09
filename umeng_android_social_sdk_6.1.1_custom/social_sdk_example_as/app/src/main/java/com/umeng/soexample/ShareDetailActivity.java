package com.umeng.soexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.soexample.model.Defaultcontent;
import com.umeng.soexample.model.ShareTypeAdapter;
import com.umeng.soexample.model.StyleUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wangfei on 16/11/10.
 * 分享内容
 */
public class ShareDetailActivity extends Activity{
    private ListView listView;
    //分享内容类型适配器
    private ShareTypeAdapter shareAdapter;
    //类型
    public ArrayList<String> styles = new ArrayList<String>();
    //媒体
    private SHARE_MEDIA share_media;
    //图片链接，本地图片
    private UMImage imageurl,imagelocal;
    //视频
    private UMVideo video;
    //音乐
    private UMusic music;
    //表情
    private UMEmoji emoji;
    //文件
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));

        }
        setContentView(R.layout.share_detail);
        //媒体
        share_media = (SHARE_MEDIA) getIntent().getSerializableExtra("platform");
        listView = (ListView) findViewById(R.id.list);
        //设置分享类型
        initStyles(share_media);
        //初始化分享内容
        initMedia();
        //设置适配器
        shareAdapter  = new ShareTypeAdapter(this,styles);
        listView.setAdapter(shareAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //分享框
                if (styles.get(position).equals(StyleUtil.IMAGELOCAL)){
                    new ShareAction(ShareDetailActivity.this).withMedia(imagelocal )
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.IMAGEURL)){
                    new ShareAction(ShareDetailActivity.this).withMedia(imageurl )
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.TEXT)){
                    new ShareAction(ShareDetailActivity.this).withText(Defaultcontent.text)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.TEXTANDIMAGE)){
                    new ShareAction(ShareDetailActivity.this).withText(Defaultcontent.text)
                            .withMedia(imagelocal)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.WEB11)
                        ||styles.get(position).equals(StyleUtil.WEB00)
                        ||styles.get(position).equals(StyleUtil.WEB10)
                        ||styles.get(position).equals(StyleUtil.WEB01)){
                    new ShareAction(ShareDetailActivity.this)
                           .withText(Defaultcontent.text)
                            .withMedia(imagelocal)
                            .withTargetUrl(Defaultcontent.url)
                            .withTitle(Defaultcontent.title)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.MUSIC11)
                        ||styles.get(position).equals(StyleUtil.MUSIC00)
                        ||styles.get(position).equals(StyleUtil.MUSIC10)
                        ||styles.get(position).equals(StyleUtil.MUSIC01)){
                    new ShareAction(ShareDetailActivity.this).withMedia(music).withTargetUrl(Defaultcontent.url)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.VIDEO11)
                        ||styles.get(position).equals(StyleUtil.VIDEO00)
                ||styles.get(position).equals(StyleUtil.VIDEO01)
                ||styles.get(position).equals(StyleUtil.VIDEO10)){
                    new ShareAction(ShareDetailActivity.this).withMedia(video).withTargetUrl(Defaultcontent.url)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.EMOJI)){
                    new ShareAction(ShareDetailActivity.this)
                            .withMedia(emoji)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }else if (styles.get(position).equals(StyleUtil.FILE)){
                    new ShareAction(ShareDetailActivity.this)
                            .withFile(file)
                            .withText(Defaultcontent.text)
                            .withTitle(Defaultcontent.title)
                            .setPlatform(share_media)
                            .setCallback(shareListener).share();
                }
            }
        });
        ((TextView)findViewById(R.id.umeng_title)).setText(share_media.toString()+"选择分享类型");
        findViewById(R.id.umeng_back).setVisibility(View.VISIBLE);
        findViewById(R.id.umeng_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 分享监听器
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ShareDetailActivity.this,"成功了",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareDetailActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ShareDetailActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    /**
     * 设置分享类型
     * @param share_media
     */
    private void initStyles(SHARE_MEDIA share_media){
        styles.clear();
        if (share_media == SHARE_MEDIA.QQ){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.QZONE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.SINA){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.WEIXIN){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
            styles.add(StyleUtil.EMOJI);
        }
        else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }
        else if (share_media == SHARE_MEDIA.WEIXIN_FAVORITE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);

        }  else if (share_media == SHARE_MEDIA.TENCENT){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        } else if (share_media == SHARE_MEDIA.DOUBAN){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.RENREN){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.ALIPAY){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
        }else if (share_media == SHARE_MEDIA.FACEBOOK){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.FACEBOOK_MESSAGER){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.TWITTER){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.EMAIL){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.SMS){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.YIXIN){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.YIXIN_CIRCLE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.LAIWANG){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.LAIWANG_DYNAMIC){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.INSTAGRAM){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
        }else if (share_media == SHARE_MEDIA.PINTEREST){
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.TUMBLR){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.LINE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.WHATSAPP){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.KAKAO){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.GOOGLEPLUS){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.EVERNOTE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.YNOTE){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.FLICKR){

            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);

        }else if (share_media == SHARE_MEDIA.LINKEDIN){
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }else if (share_media == SHARE_MEDIA.POCKET) {
            styles.add(StyleUtil.WEB00);
        }else if (share_media == SHARE_MEDIA.FOURSQUARE) {
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
        }else if (share_media == SHARE_MEDIA.MORE) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.TEXTANDIMAGE);
        }else if (share_media == SHARE_MEDIA.DINGTALK) {
            styles.add(StyleUtil.TEXT);
            styles.add(StyleUtil.IMAGELOCAL);
            styles.add(StyleUtil.IMAGEURL);
            styles.add(StyleUtil.WEB11);
            styles.add(StyleUtil.MUSIC11);
            styles.add(StyleUtil.VIDEO11);
        }

    }

    /**
     * 初始化分享内容
     */
    private void initMedia(){
        //分享链接
        imageurl = new UMImage(this,Defaultcontent.imageurl);
        imageurl.setThumb(new UMImage(this, R.drawable.thumb));
        //分享图片
        imagelocal = new UMImage(this,R.drawable.logo);
        imagelocal.setThumb(new UMImage(this, R.drawable.thumb));

        music = new UMusic(Defaultcontent.musicurl);
        video = new UMVideo(Defaultcontent.videourl);
        music.setTitle("This is music title");
        music.setThumb(new UMImage(this, R.drawable.thumb));
        music.setDescription("my description");
        //init video
        video.setThumb(new UMImage(this,R.drawable.thumb));
        video.setTitle("This is video title");
        video.setDescription("my description");
        emoji = new UMEmoji(this,"http://img5.imgtn.bdimg.com/it/u=2749190246,3857616763&fm=21&gp=0.jpg");
        emoji.setThumb(imagelocal);
        file = new File(this.getFilesDir()+"test.txt");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (SocializeUtils.File2byte(file).length<=0){
            String content = "U-share分享";
            byte[] contentInBytes = content.getBytes();
            try {
                FileOutputStream fop = new FileOutputStream(file);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
