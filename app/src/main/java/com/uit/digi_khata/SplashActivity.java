package com.uit.digi_khata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth fauth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        fauth = FirebaseAuth.getInstance();
        Handler handler = new Handler() ;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fauth.getCurrentUser() != null)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),LoginOrSignUp.class));
                }

                finish();
            }
        },2000) ;
    }
}