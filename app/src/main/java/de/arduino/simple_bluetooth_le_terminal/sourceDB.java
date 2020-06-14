package de.arduino.simple_bluetooth_le_terminal;

public class sourceDB {
    private sourceDB() {} ;
    // DB 테이블의 테이블명과 속성명 정의
    public static final String TBL_CONTACT = "SOURCE_T" ;       // 테이블명
    public static final String COL_NO = "NO" ;                  // 양념장 넘버
    public static final String COL_NAME = "NAME";               // 양념장 이름
    public static final String COL_SOURCE1 = "SOURCE1" ;        // 소금
    public static final String COL_SOURCE2 = "SOURCE2" ;        // 간장
    public static final String COL_SOURCE3 = "SOURCE3" ;        // 식초


    // 테이블 생성 쿼리 정의
    // CREATE TABLE IF NOT EXISTS SOURCE_T (NO INTEGER NOT NULL, NAME TEXT, SOURCE1 TEXT, SOURCE2 TEXT, SOURCE3 TEXT,)
    // *IF NOT EXISTS : 테이블명이 동일한 값이 없을때 실행
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT + " " +
            "(" +
            COL_NO +          " INTEGER NOT NULL" +   ", " +
            COL_NAME +     " TEXT"          +   ", " +
            COL_SOURCE1 +     " TEXT"          +   ", " +
            COL_SOURCE2 +     " TEXT"          +   ", " +
            COL_SOURCE3 +     " TEXT"          +
            ")" ;

    // sourceDBHelper dbHelper = null;    xxxDBHelper : 사용자가 정의한 Helper class (SQLiteOpenHelper 상속)
    // SQLiteDatabase db = dbHelper.getReadableDatabase();
    // db : SQLiteDatabase 객체 (execSQL() 함수 사용 가능)
    // dbHelper : SQLiteOpenHelper가 상속된 sourceDBHelper 객체 (getReadableDatabase() 함수 사용 가능)
    // getReadableDatabase(), getWritableDatabase() : SQLiteDatabase 객체를 참조하기 위한 함수
    // 사용법 : String Query = SQL_INSER +
    //         " (" +
    //         Integer.toString(no) + ", " +
    //         "'" + sourceName + "', " +
    //         "'" + source1 + "', " +
    //         "'" + source2 + "', " +
    //         "'" + source3 + "' " +
    //         ")";
    // db.execSQL(Query);
    //

    // 테이블 삭제 쿼리 정의
    // DROP TABLE IF EXISTS SOURCE_T
    // *IF EXISTS : 테이블명이 동일한 값이 있을때 실행
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_CONTACT ;

    // 테이블 검색 쿼리 정의
    // SELECT * FROM SOURCE_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_CONTACT;

    // 행 입력 쿼리 정의
    // INSERT OR REPLACE INTO SOURCE_T (NO, NAME, SOURCE1, SOURCE2, SOURCE3) VALUES (x, x, x, x)
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TBL_CONTACT + " " +
            "(" + COL_NO + ", " + COL_NAME + ", " + COL_SOURCE1 + ", " + COL_SOURCE2 + ", " + COL_SOURCE3 + ") VALUES " ;

    // 행 변경 쿼리 정의
    // UPDATE SOURCE_T SET NO = x, NAME = x, SOURCE1 = x, SOURCE2 = x, SOURCE3 = x WHERE NO = x
    public static final String SQL_UPDATE = "UPDATE " + TBL_CONTACT + " SET ";

    // 행 삭제 쿼리 정의
    // DELETE FROM SOURCE_T WHERE NO = x
    public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT + " WHERE NO = " ;
}
