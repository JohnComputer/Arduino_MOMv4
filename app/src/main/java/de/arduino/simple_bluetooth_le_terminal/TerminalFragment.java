package de.arduino.simple_bluetooth_le_terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener {
    private enum Connected { False, Pending, True }

    private String deviceAddress;
    private String newline = ",";

    private TextView receiveText;

    private SerialService service;
    private boolean initialStart = true;
    private Connected connected = Connected.False;

    TextView sendText;
    TextView sendText2;
    TextView sendText3;
    sourceDBHelpler dbHelpler = null;
    Button source_btn1;
    Button source_btn2;
    Button source_btn3;
    Button source_btn4;
    Button source_btn5;
    Button source_btn6;

    /*
     * Lifecycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        dbHelpler = new sourceDBHelpler(this.getActivity());
        deviceAddress = getArguments().getString("device");
    }

    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(service != null)
            service.attach(this);
        else
            getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
    }

    @Override
    public void onStop() {
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @SuppressWarnings("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try { getActivity().unbindService(this); } catch(Exception ignored) {}
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(initialStart && service !=null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }

    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    /*
     * UI
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        receiveText = view.findViewById(R.id.receive_text);                          // TextView performance decreases with number of spans
        receiveText.setTextColor(getResources().getColor(R.color.colorRecieveText)); // set as default color to reduce number of spans
        receiveText.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 소금
        sendText = view.findViewById(R.id.send_text1);
        // 간장
        sendText2 = view.findViewById(R.id.send_text2);
        // 참기름
        sendText3 = view.findViewById(R.id.send_text3);

        source_btn1 = (Button)view.findViewById(R.id.source_btn1);
        source_btn2 = (Button)view.findViewById(R.id.source_btn2);
        source_btn3 = (Button)view.findViewById(R.id.source_btn3);
        source_btn4 = (Button)view.findViewById(R.id.source_btn4);
        source_btn5 = (Button)view.findViewById(R.id.source_btn5);
        source_btn6 = (Button)view.findViewById(R.id.source_btn6);

        SQLiteDatabase db = dbHelpler.getReadableDatabase();
        Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
        while (cursor.moveToNext()) {
            int no = cursor.getInt(0);
            if (no == 1) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn1.setText(sourceName);
            }
            if (no == 2) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn2.setText(sourceName);
            }
            if (no == 3) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn3.setText(sourceName);
            }
            if (no == 4) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn4.setText(sourceName);
            }
            if (no == 5) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn5.setText(sourceName);
            }
            if (no == 6) {  // 소스버튼 인식
                String sourceName = cursor.getString(1);
                source_btn6.setText(sourceName);
            }
        }

        source_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(0)) {
                    int no = cursor.getInt(0);
                    if (no == 1) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 1);
                    startActivityForResult(intent, 1);
                }

            }
        });
        registerForContextMenu(source_btn1);

        source_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(1) == true) {
                    int no = cursor.getInt(0);
                    if (no == 2) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 2);
                    startActivityForResult(intent, 2);
                }
            }
        });
        registerForContextMenu(source_btn2);

        source_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(2)) {
                    int no = cursor.getInt(0);
                    if (no == 3) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 3);
                    startActivityForResult(intent, 3);
                }
            }
        });
        registerForContextMenu(source_btn3);

        source_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(3)) {
                    int no = cursor.getInt(0);
                    if (no == 4) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 4);
                    startActivityForResult(intent, 4);
                }
            }
        });
        registerForContextMenu(source_btn4);

        source_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(4)) {
                    int no = cursor.getInt(0);
                    if (no == 5) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 5);
                    startActivityForResult(intent, 5);
                }
            }
        });
        registerForContextMenu(source_btn5);

        source_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelpler.getReadableDatabase();
                Cursor cursor = db.rawQuery(sourceDB.SQL_SELECT, null);
                if (cursor.moveToPosition(5)) {
                    int no = cursor.getInt(0);
                    if (no == 6) {  // 소스버튼 인식
                        String source1 = cursor.getString(2);       // 소금
                        sendText.setText(source1);
                        String source2 = cursor.getString(3);       // 간장
                        sendText2.setText(source2);
                        String source3 = cursor.getString(4);       // 참기름
                        sendText3.setText(source3);
                    }
                }
                else {
                    Intent intent = new Intent(getActivity(), sourceSave.class);
                    intent.putExtra("no", 6);
                    startActivityForResult(intent, 6);
                }
            }
        });
        registerForContextMenu(source_btn6);


//        Intent getit = getActivity().getIntent();
//        final String s = getit.getStringExtra("salt");  // 타입 일치가 필요하다.
//        final String s1 = getit.getStringExtra("soysauce");
//        final String s2 = getit.getStringExtra("cham");


        View sendBtn = view.findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(v -> send(sendText.getText().toString(), sendText2.getText().toString(), sendText3.getText().toString()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terminal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear) {
            receiveText.setText("");
            return true;
        } else if (id ==R.id.newline) {
            String[] newlineNames = getResources().getStringArray(R.array.newline_names);
            String[] newlineValues = getResources().getStringArray(R.array.newline_values);
            int pos = java.util.Arrays.asList(newlineValues).indexOf(newline);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Newline");
            builder.setSingleChoiceItems(newlineNames, pos, (dialog, item1) -> {
                newline = newlineValues[item1];
                dialog.dismiss();
            });
            builder.create().show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Serial + UI
     */
    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("마미손 연결중....");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
    }

    private void send(String str, String str2, String str3) {
        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SpannableStringBuilder spn = new SpannableStringBuilder("소스제작에 들어갑니다.\n");    // 파란색 글
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            receiveText.append(spn);

//
//            byte[] data = (str + newline).getBytes();
//            service.write(data);
//            ------------ 수정 확인 중 ---------------
           byte[] data  = (str + newline +  str2 + newline + str3).getBytes();

            service.write(data);

            receiveText.append("소금: "+str+"\t간장: "+str2+"\t식초: "+str3+"\n");   //글 초록색 뜸
//           send_text1 -> salt / send_text2 = milk
//
//           straaa = "1,2,3"   str+,+str+

//           -------------------------------------------
        } catch (Exception e) {
            onSerialIoError(e);
        }

    }

    private void receive(byte[] data) {
//        receiveText.append(new String(data));
    }

    private void status(String str) {   //글자 연결 색상 노란색 뜨게 함.
        SpannableStringBuilder spn = new SpannableStringBuilder(str+'\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);
    }

    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {
        status("마미손과 연결되었습니다.");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
//        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.source_btn1:
                menu.add(0, 1, 100,"변경");
                menu.add(0, 2, 100, "삭제");
                menu.add(0, 3, 100, "취소");
                break;
            case R.id.source_btn2:
                menu.add(0, 4, 100,"변경");
                menu.add(0, 5, 100, "삭제");
                menu.add(0, 6, 100, "취소");
                break;
            case R.id.source_btn3:
                menu.add(0, 7, 100,"변경");
                menu.add(0, 8, 100, "삭제");
                menu.add(0, 9, 100, "취소");
                break;
            case R.id.source_btn4:
                menu.add(0, 10, 100,"변경");
                menu.add(0, 11, 100, "삭제");
                menu.add(0, 12, 100, "취소");
                break;
            case R.id.source_btn5:
                menu.add(0, 13, 100,"변경");
                menu.add(0, 14, 100, "삭제");
                menu.add(0, 15, 100, "취소");
                break;
            case R.id.source_btn6:
                menu.add(0, 16, 100,"변경");
                menu.add(0, 17, 100, "삭제");
                menu.add(0, 18, 100, "취소");
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), sourceSave.class);
        SQLiteDatabase db = dbHelpler.getWritableDatabase() ;
        String sqlDelete;
        switch (item.getItemId()) {
            case 1:
                intent.putExtra("no", 1);
                startActivityForResult(intent, 1);
                return true;
            case 2:
                sqlDelete = sourceDB.SQL_DELETE + "1";
                db.execSQL(sqlDelete);
                return true;
            case 3:
                return false;
            case 4:
                intent.putExtra("no", 2);
                startActivityForResult(intent, 2);
                return true;
            case 5:
                sqlDelete = sourceDB.SQL_DELETE + "2";
                db.execSQL(sqlDelete);
                return true;
            case 6:
                return false;
            case 7:
                intent.putExtra("no", 3);
                startActivityForResult(intent, 3);
                return true;
            case 8:
                sqlDelete = sourceDB.SQL_DELETE + "3";
                db.execSQL(sqlDelete);
                return true;
            case 9:
                return false;
            case 10:
                intent.putExtra("no", 4);
                startActivityForResult(intent, 4);
                return true;
            case 11:
                sqlDelete = sourceDB.SQL_DELETE + "4";
                db.execSQL(sqlDelete);
                return true;
            case 12:
                return false;
            case 13:
                intent.putExtra("no", 5);
                startActivityForResult(intent, 5);
                return true;
            case 14:
                sqlDelete = sourceDB.SQL_DELETE + "5";
                db.execSQL(sqlDelete);
                return true;
            case 15:
                return false;
            case 16:
                intent.putExtra("no", 6);
                startActivityForResult(intent, 6);
                return true;
            case 17:
                sqlDelete = sourceDB.SQL_DELETE + "6";
                db.execSQL(sqlDelete);
                return true;
            case 18:
                return false;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 1:
                source_btn1.setText(data.getStringExtra("result"));
                break;
            case 2:
                source_btn2.setText(data.getStringExtra("result"));
                break;
            case 3:
                source_btn3.setText(data.getStringExtra("result"));
                break;
            case 4:
                source_btn4.setText(data.getStringExtra("result"));
                break;
            case 5:
                source_btn5.setText(data.getStringExtra("result"));
                break;
            case 6:
                source_btn6.setText(data.getStringExtra("result"));
                break;
        }
    }
}
