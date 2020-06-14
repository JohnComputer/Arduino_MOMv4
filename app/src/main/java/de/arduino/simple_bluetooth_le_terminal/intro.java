package de.arduino.simple_bluetooth_le_terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class intro extends AppCompatActivity {
    // 첫화면 제작 !! Intent로 인한 전송
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        ImageView introImage = (ImageView)findViewById(R.id.introImage);

        introImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIt = new Intent(intro.this,MainActivity.class);
                startActivity(mainIt);
                finish();

            }
        });
    }
}