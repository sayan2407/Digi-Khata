package com.uit.digi_khata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginOrSignUp extends AppCompatActivity {

    private Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);

        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
    }

    public void login_here(View view)
    {
        startActivity(new Intent(getApplicationContext(),AuthenticationActivity.class));
    }

    public void signup_here(View view)
    {
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }
}