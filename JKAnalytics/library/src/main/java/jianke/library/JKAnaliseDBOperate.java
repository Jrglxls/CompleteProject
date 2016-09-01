package jianke.library;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiajing on 2016/8/17.
 * 操作数据库
 */
public class JKAnaliseDBOperate {
    private Context context;
    private SQLiteDatabase db;
    private JKAnaliseHelper jkAnaliseHelper;

    public JKAnaliseDBOperate(Context context) {
        this.context = context;
        jkAnaliseHelper = JKAnaliseHelper.Instance(context);
    }

    /**
     * 添加数据
     */
    public void insert(JKAnalyticsInfo jkAnalyticsInfo) {
        db = jkAnaliseHelper.getWritableDatabase();
        db.execSQL("insert into JKAnaliseTable values (NULL,?,?,?,?,?,?,?,?,?,?)",
                new Object[]{jkAnalyticsInfo.getAppkey(),
                             jkAnalyticsInfo.getUserId(),
                             jkAnalyticsInfo.getUserFlag(),
                             jkAnalyticsInfo.getPageId(),
                             jkAnalyticsInfo.getReferrer(),
                             jkAnalyticsInfo.getTimestamp(),
                             jkAnalyticsInfo.getEventId(),
                             jkAnalyticsInfo.getDuration(),
                             jkAnalyticsInfo.getExtras(),
                             jkAnalyticsInfo.getParam()});
        db.close();
    }

    /**
     * 删除数据
     */
    public void delete(int maxId){
        db = jkAnaliseHelper.getWritableDatabase();
        db.execSQL("delete from JKAnaliseTable where id <= ?",new Object[]{maxId});
        db.close();
    }

    /**
     * 获取MaxId
     * @return maxId
     */
    public int getMaxId(){
        int maxId = 0;
        db = jkAnaliseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from JKAnaliseTable",null) ;
            while (cursor.moveToNext()){
                maxId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        cursor.close();
        db.close();
        return maxId;
    }

    /**
     * 获取所有数据
     * @return jkAnalyticsInfoList
     */
    public List<JKAnalyticsInfo> getALLInfos(){
        int maxId = getMaxId();
        List<JKAnalyticsInfo> jkAnalyticsInfoList = new ArrayList<JKAnalyticsInfo>();
        db = jkAnaliseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from JKAnaliseTable where id <= ?",new String[]{maxId+""});
        if (cursor!=null){
            while (cursor.moveToNext()){
                JKAnalyticsInfo jkAnalyticsInfo = new JKAnalyticsInfo();

                jkAnalyticsInfo.setAppkey(cursor.getString(cursor.getColumnIndex("Appkey")));
                jkAnalyticsInfo.setUserId(cursor.getString(cursor.getColumnIndex("UserId")));
                jkAnalyticsInfo.setUserFlag(cursor.getString(cursor.getColumnIndex("UserFlag")));
                jkAnalyticsInfo.setPageId(cursor.getString(cursor.getColumnIndex("PageId")));
                jkAnalyticsInfo.setReferrer(cursor.getString(cursor.getColumnIndex("Referrer")));
                jkAnalyticsInfo.setTimestamp(cursor.getString(cursor.getColumnIndex("Timestamp")));
                jkAnalyticsInfo.setEventId(cursor.getString(cursor.getColumnIndex("EventId")));
                jkAnalyticsInfo.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                jkAnalyticsInfo.setExtras(cursor.getString(cursor.getColumnIndex("Extras")));
                jkAnalyticsInfo.setParam(cursor.getString(cursor.getColumnIndex("Param")));

                jkAnalyticsInfoList.add(jkAnalyticsInfo);
            }
            cursor.close();
        }
        db.close();
        return jkAnalyticsInfoList;
    }
}
