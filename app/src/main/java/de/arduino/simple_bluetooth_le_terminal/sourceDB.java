package de.arduino.simple_bluetooth_le_terminal;

public class sourceDB {
    private sourceDB() {} ;

    public static final String TBL_CONTACT = "SOURCE_T" ;
    public static final String COL_NO = "NO" ;                  // 양념장 넘버
    public static final String COL_NAME = "NAME";               // 양념장 이름
    public static final String COL_SOURCE1 = "SOURCE1" ;        // 소금
    public static final String COL_SOURCE2 = "SOURCE2" ;        // 간장
    public static final String COL_SOURCE3 = "SOURCE3" ;        // 식초


    // CREATE TABLE IF NOT EXISTS CONTACT_T (NO INTEGER NOT NULL, NAME TEXT, PHONE TEXT, OVER20 INTEGER)
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_CONTACT + " " +
            "(" +
            COL_NO +          " INTEGER NOT NULL" +   ", " +
            COL_NAME +     " TEXT"          +   ", " +
            COL_SOURCE1 +     " TEXT"          +   ", " +
            COL_SOURCE2 +     " TEXT"          +   ", " +
            COL_SOURCE3 +     " TEXT"          +
            ")" ;

    // DROP TABLE IF EXISTS CONTACT_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_CONTACT ;

    // SELECT * FROM CONTACT_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_CONTACT;

    // INSERT OR REPLACE INTO CONTACT_T (NO, NAME, PHONE, OVER20) VALUES (x, x, x, x)
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + TBL_CONTACT + " " +
            "(" + COL_NO + ", " + COL_NAME + ", " + COL_SOURCE1 + ", " + COL_SOURCE2 + ", " + COL_SOURCE3 + ") VALUES " ;

    public static final String SQL_UPDATE = "UPDATE " + TBL_CONTACT + " SET ";

    // DELETE FROM CONTACT_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT + " WHERE NO = " ;
}
