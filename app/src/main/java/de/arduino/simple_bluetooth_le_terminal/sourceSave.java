package de.arduino.simple_bluetooth_le_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class sourceSave extends AppCompatActivity {

//----------------DB 도우미, 입력창 선언-------------------

    sourceDBHelpler dbHelpler = null;
    Intent intent;
    EditText editTextSourceName;
    EditText editTextSource1;
    EditText editTextSource2;
    EditText editTextSource3;

//---------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_save);

//-----------------DB 도우미, 입력창 인스턴스화----------------

        dbHelpler = new sourceDBHelpler(this);
        intent = getIntent();
        editTextSourceName = (EditText)findViewById(R.id.source_name);
        editTextSource1 = (EditText)findViewById(R.id.save_text);
        editTextSource2 = (EditText)findViewById(R.id.save_text2);
        editTextSource3 = (EditText)findViewById(R.id.save_text3);

//----------------------------------------------------------

//-------------------DB SELECT(버튼에 저장된 DB 정보가 있을 시 변경)-------------------

        // SQLiteDatabase 객체를 참조하기 위한 dbHelpler.getReadableDatabase()
        SQLiteDatabase db = dbHelpler.getReadableDatabase();
        // Cursor 객체 : 테이블을 한 행씩 분리해 타겟팅
        Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
        // DB 검색 후 클릭된 소스버튼에 저장된 값 확인
        while (cursor.moveToNext()) {
            int no = cursor.getInt(0);
            // DB에 저장된 no 속성의 값과 인텐트로 전달 받은 no인 값과 일치하는지 확인
            if (no == intent.getExtras().getInt("no")) {
                // 일치하는 값이 있을시 저장된 값 set
                String sourceName = cursor.getString(1);
                editTextSourceName.setText(sourceName);
                String source1 = cursor.getString(2);       // 소금
                editTextSource1.setText(source1);
                String source2 = cursor.getString(3);       // 간장
                editTextSource2.setText(source2);
                String source3 = cursor.getString(4);       // 식초
                editTextSource3.setText(source3);
            }
        }

//-------------------------------------------------------------------------------------

    }

//---------------------저장, 취소 버튼 클릭 메소드-------------------

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                save_values();
                // 소스이름을 리턴하기 위한 Intent 객체 정의
                Intent returnIntent = new Intent();
                // 소스이름 Intent 객체에 put
                returnIntent.putExtra("result", editTextSourceName.getText().toString());
                // 결과 코드와 Intent 객체 리턴
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

//-------------------------------------------------------------------

    private void save_values() {
//------------------DB INSERT 및 DB UPDATE --------------------------

        // DB UPDATE를 하기 위해 Cursor 객체에 셋팅
        SQLiteDatabase db_select = dbHelpler.getReadableDatabase();
        Cursor cursor = db_select.rawQuery(sourceDB.SQL_SELECT, null);

        // DB INSERT를 하기 위해 dbHelpler.getWritableDatabase()를 이용해 SQLiteDatabase 객체 참조
        SQLiteDatabase db = dbHelpler.getWritableDatabase();
        // DB에 값을 저장하기 위해 변수에 저장
        int no = intent.getExtras().getInt("no");
        String sourceName = editTextSourceName.getText().toString();
        String source1 = editTextSource1.getText().toString();
        String source2 = editTextSource2.getText().toString();
        String source3 = editTextSource3.getText().toString();

        // Cursor객체에 타겟팅 된 행 이동 및 저장된 값이 있으면 UPDATE 없으면 INSERT
        if (cursor.moveToPosition(no-1)) {
            // 쿼리문 작성
            // UPDATE SOURCE_T SET / NO = x, NAME = x, SOURCE1 = x, SOURCE2 = x, SOURCE3 = x WHERE NO = x
            // sourceDB.SQL_UPDATE + ~~
            String sqlUpdate = sourceDB.SQL_UPDATE +
                    "NAME = '" + sourceName + "', " +
                    "SOURCE1 = '" + source1 + "', " +
                    "SOURCE2 = '" + source2 + "', " +
                    "SOURCE3 = '" + source3 + "'" +
                    " WHERE NO = " +  Integer.toString(no);
            // 쿼리문을 DB에서 실행
            db.execSQL(sqlUpdate);
        }
        else {
            // 쿼리문 작성
            // INSERT OR REPLACE INTO SOURCE_T (NO, NAME, SOURCE1, SOURCE2, SOURCE3) VALUES / (x, x, x, x)
            //                               sourceDB.SQL_INSERT                            / ~~
            String sqlInsert = sourceDB.SQL_INSERT +
                    " (" +
                    Integer.toString(no) + ", " +
                    "'" + sourceName + "', " +
                    "'" + source1 + "', " +
                    "'" + source2 + "', " +
                    "'" + source3 + "' " +
                    ")";
            // 쿼리문을 DB에서 실행
            db.execSQL(sqlInsert);
        }
    }

//-----------------------------------------------------------------------
}
