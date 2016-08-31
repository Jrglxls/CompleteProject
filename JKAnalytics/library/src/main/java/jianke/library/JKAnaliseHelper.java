package jianke.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangjiajing on 2016/8/12.
 * 创建数据库
 */
public class JKAnaliseHelper extends SQLiteOpenHelper {
    private static JKAnaliseHelper instance;

    /**
     * 构造方法
     */
    public JKAnaliseHelper(Context context) {
        super(context, "JKAnalise", null, 1);
    }

    /**
     * 单例
     */
    public static JKAnaliseHelper Instance(Context context) {
        if (instance == null) {
            instance = new JKAnaliseHelper(context);
        }
        return instance;
    }

    /**
     * 创建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists JKAnaliseTable(id integer PRIMARY KEY AUTOINCREMENT,Appkey text,UserId text,UserFlag text,PageId text,Referrer text,Timestamp text,EventId text,Duration text,Extras text,Param text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
