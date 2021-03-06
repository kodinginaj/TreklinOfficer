package com.example.treklinofficer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.treklinofficer.util.Session;

public class SplahPetugas extends Activity {

    Handler handler;
    Runnable runnable;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_petugas);

        img = findViewById(R.id.myimg);

        img.animate().alpha(4000).setDuration(0);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Session session = new Session(SplahPetugas.this);

                String nama = session.getNama();
                if (nama == null){
                    Intent i = new Intent(SplahPetugas.this, LoginPetugas.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplahPetugas.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 4000);
    }
}