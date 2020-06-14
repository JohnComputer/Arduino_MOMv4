package de.arduino.simple_bluetooth_le_terminal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// DB를 생성해주고 DB의 버전을 관리해주는 Class(테이블 관리 X)
// 현재 코드에서는 생성과 업그레이드만 구현
public class sourceDBHelpler extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1 ;
    public static final String DBFILE_CONTACT = "contact.db" ;

    public sourceDBHelpler(Context context) {
        super(context, DBFILE_CONTACT, null, DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sourceDB.SQL_CREATE_TBL) ;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(ContactDBCtrct.SQL_DROP_TBL) ;
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade(db, oldVersion, newVersion);
    }
}
