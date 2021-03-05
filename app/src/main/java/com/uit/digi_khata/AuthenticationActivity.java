package com.uit.digi_khata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.leo.simplearcloader.SimpleArcLoader;

public class AuthenticationActivity extends AppCompatActivity {

    private Toolbar toolbar ;
    private FirebaseAuth fauth ;
    private TextInputEditText email,pass ;
    private String emailid,password ;
    private Button login ;
    private String yourEmail ;
    private SimpleArcLoader loader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
        loader = findViewById(R.id.loader) ;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mapping();
        fauth = FirebaseAuth.getInstance() ;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                emailid = email.getText().toString().trim();
                password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(emailid) || TextUtils.isEmpty(password))
                {
                    loader.setVisibility(View.INVISIBLE);
                    Toast.makeText(AuthenticationActivity.this, "Please enter credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
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
                                    loader.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    loader.setVisibility(View.INVISIBLE);
                                    Toast.makeText(AuthenticationActivity.this, "Please enter correct credentials", Toast.LENGTH_SHORT).show();
                                }

                            }catch (Exception e){
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Error :"+e.getMessage(),Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }



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

    public void forget_password(View view)
    {
        EditText editText = new EditText(view.getContext());
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext()) ;
        alert.setTitle("Forgot your password?") ;
        alert.setMessage("Enter your email Id") ;
        alert.setCancelable(false);
        alert.setView(editText);
        alert.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourEmail = editText.getText().toString().trim() ;
                if (!TextUtils.isEmpty(yourEmail))
                {
                    fauth.sendPasswordResetEmail(yourEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AuthenticationActivity.this, "Password reset link sent to your email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error!! Reset link not sent because :"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(AuthenticationActivity.this, "Please Enter your registered email id", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();
            }
        });

        alert.create().show();

    }
}