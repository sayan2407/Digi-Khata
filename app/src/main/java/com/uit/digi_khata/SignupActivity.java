package com.uit.digi_khata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leo.simplearcloader.SimpleArcLoader;

public class SignupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText name,cname,email,phone,password,cpassword ;
    private UtilService utilService;
    private Button register ;
    private String owner,companyname,emailid,pass,cpass,mobile,userId ;
    private SimpleArcLoader loader ;
    FirebaseDatabase database ;
    FirebaseAuth fauth ;
    DatabaseReference myref ;
    DatabseHelper mydb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mapping();
        utilService = new UtilService();
        fauth = FirebaseAuth.getInstance();
        mydb = new DatabseHelper(getApplicationContext()) ;

     /*   database = FirebaseDatabase.getInstance();
        myref = database.getReference("profile") ; */

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.setVisibility(View.VISIBLE);
                utilService.hide_keyboard(v,SignupActivity.this);
                owner = name.getText().toString().trim();
                companyname = cname.getText().toString().trim();
                emailid = email.getText().toString().trim();
                pass = password.getText().toString().trim();
                mobile = phone.getText().toString().trim();
                cpass = cpassword.getText().toString().trim();

                if (validate(v))
                {
                   // Toast.makeText(getApplicationContext(),"name :"+owner+"\nemail : "+emailid,Toast.LENGTH_LONG).show();
                    fauth.createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                 userId = fauth.getCurrentUser().getUid();
                            /*     User u = new User(owner,companyname,mobile,emailid) ;
                                myref.child(userId).setValue(u) ; */
                                mydb.insertData(userId,emailid,mobile,owner,companyname) ;
                                Toast.makeText(getApplicationContext(),"Profile Created",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),LoginOrSignUp.class));
                                finish();
                                loader.setVisibility(View.INVISIBLE);
                            }
                            else{
                                loader.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Profile Not Created",Toast.LENGTH_LONG).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignupActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    loader.setVisibility(View.GONE);
                }



            }
        });
    }

    private boolean validate(View view)
    {
        boolean isValid = false ;
        if (!TextUtils.isEmpty(owner))
        {
            if(!TextUtils.isEmpty(companyname))
            {
                if (!TextUtils.isEmpty(emailid))
                {
                    if(!TextUtils.isEmpty(mobile))
                    {

                        if(!TextUtils.isEmpty(pass))
                        {
                            if (pass.length()>=6)
                            {
                                if (pass.equals(cpass))
                                {
                                    if (mobile.length()==10)
                                    {
                                        isValid = true ;
                                    }else{
                                        utilService.showSnackBar(view,"Enter 10 digit mobile number");
                                    }

                                }
                                else{
                                    utilService.showSnackBar(view,"Type correctly your password");
                                }

                            }
                            else
                            {
                                utilService.showSnackBar(view,"Please enter minimum six character");
                            }

                        }
                        else
                        {
                            utilService.showSnackBar(view,"Please create valid password");
                        }

                    }
                    else {
                        utilService.showSnackBar(view,"Please enter your ten digit phone number");
                    }

                }
                else
                {
                    utilService.showSnackBar(view,"Please enter your valid email");
                }

            }
            else
            {
                utilService.showSnackBar(view,"Please enter your company name");
            }

        }
        else
        {
            utilService.showSnackBar(view,"Enter your name");
        }

        return isValid ;
    }

    private  void mapping()
    {
        name = findViewById(R.id.name) ;
        cname = findViewById(R.id.companyname);
        email = findViewById(R.id.email) ;
        phone = findViewById(R.id.phone) ;
        password = findViewById(R.id.createpassword) ;
        cpassword = findViewById(R.id.confirmpassword) ;
        register = findViewById(R.id.register) ;
        loader = findViewById(R.id.loader) ;

    }
}