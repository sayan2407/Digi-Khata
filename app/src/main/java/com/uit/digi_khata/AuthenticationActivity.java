package com.uit.digi_khata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private FirebaseAuth fauth ;
    private TextInputEditText email,pass ;
    private String emailid,password ;
    private Button login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mapping();
        fauth = FirebaseAuth.getInstance() ;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailid = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                fauth.signInWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Login successfull",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                // set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
                                startActivity(i);
                            }

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error :"+e.getMessage(),Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });

    }

    private void mapping()
    {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password) ;
        login = findViewById(R.id.login) ;
    }

    public void login_here(View view)
    {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}