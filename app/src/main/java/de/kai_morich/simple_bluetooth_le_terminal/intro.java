package de.kai_morich.simple_bluetooth_le_terminal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        ImageView introImage = (ImageView)findViewById(R.id.introImage);

        introImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIt = new Intent(intro.this,Main2Activity.class);
                startActivity(mainIt);  // 첫화면 아무곳이나 클릭시 넘김
                finish();

            }
        });
    }
}