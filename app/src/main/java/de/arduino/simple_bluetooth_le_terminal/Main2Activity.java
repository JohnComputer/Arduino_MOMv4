package de.arduino.simple_bluetooth_le_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button makeSauce = (Button) findViewById(R.id.makeSauce);
        Button sampleSauce1 = (Button) findViewById(R.id.sampleSauce1);

//         두개의 버튼 다 같은 화면으로 보내고 두개의 화면의 경우
//         하나는 자동소스로 데이터값 보내기 / 하나는 0 값으로 보내어 초기화 한 후 사용자가 직접 데이터 값 입력.
        makeSauce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIt = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(mainIt);  // 첫화면 아무곳이나 클릭시 넘김
                finish();
            }
        });

        sampleSauce1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIt = new Intent(Main2Activity.this, MainActivity.class);
                mainIt.putExtra("salt",2);
                mainIt.putExtra("soysauce",2);
                mainIt.putExtra("cham",2);
                startActivity(mainIt);  // 첫화면 아무곳이나 클릭시 넘김
            }
        });
    }
}
