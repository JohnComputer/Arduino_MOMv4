package de.arduino.simple_bluetooth_le_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class sourceSave extends AppCompatActivity {
    sourceDBHelpler dbHelpler = null;
    Intent intent;
    EditText editTextSourceName;
    EditText editTextSource1;
    EditText editTextSource2;
    EditText editTextSource3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_save);
        dbHelpler = new sourceDBHelpler(this);
        intent = getIntent();
        editTextSourceName = (EditText)findViewById(R.id.source_name);
        editTextSource1 = (EditText)findViewById(R.id.save_text);
        editTextSource2 = (EditText)findViewById(R.id.save_text2);
        editTextSource3 = (EditText)findViewById(R.id.save_text3);

        // DB 검색
        SQLiteDatabase db = dbHelpler.getReadableDatabase();
        Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
            while (cursor.moveToNext()) {
                // DB 소스 정보
                int no = cursor.getInt(0);
                if (no == intent.getExtras().getInt("no")) {  // 소스버튼 인식
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
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                save_values();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", editTextSourceName.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    private void save_values() {
        SQLiteDatabase db_select = dbHelpler.getReadableDatabase();
        Cursor cursor = db_select.rawQuery(sourceDB.SQL_SELECT, null);
        SQLiteDatabase db = dbHelpler.getWritableDatabase();
        int no = intent.getExtras().getInt("no");
        String sourceName = editTextSourceName.getText().toString();

        String source1 = editTextSource1.getText().toString();

        String source2 = editTextSource2.getText().toString();

        String source3 = editTextSource3.getText().toString();

        if (cursor.moveToPosition(no-1)) {
            String sqlUpdate = sourceDB.SQL_UPDATE +
                    "NAME = '" + sourceName + "', " +
                    "SOURCE1 = '" + source1 + "', " +
                    "SOURCE2 = '" + source2 + "', " +
                    "SOURCE3 = '" + source3 + "'" +
                    " WHERE NO = " +  Integer.toString(no);
            db.execSQL(sqlUpdate);
        }
        else {
            String sqlInsert = sourceDB.SQL_INSERT +
                    " (" +
                    Integer.toString(no) + ", " +
                    "'" + sourceName + "', " +
                    "'" + source1 + "', " +
                    "'" + source2 + "', " +
                    "'" + source3 + "' " +
                    ")";

            db.execSQL(sqlInsert);
        }

    }
}
