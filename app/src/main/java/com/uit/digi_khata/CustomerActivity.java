package com.uit.digi_khata;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int REQUEST_CODE = 1;
    private Button choose , insert ;
    private TextInputEditText name,phone,address ;
    private String customerName,customerPhone,customerAdd,userid;
    DatabaseHelperCustomer mydb ;
    FirebaseAuth fauth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mapping();
        mydb = new DatabaseHelperCustomer(getApplicationContext()) ;
        fauth = FirebaseAuth.getInstance();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 customerName = name.getText().toString().trim();
                customerPhone = phone.getText().toString().trim();
                customerAdd = address.getText().toString().trim();

                if (validate(v))
                {
                    userid = fauth.getCurrentUser().getUid() ;
                    boolean check = mydb.insert_customer(customerPhone,customerName,customerAdd,userid) ;
                    if (check)
                        Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Data Not Inserted",Toast.LENGTH_LONG).show();

                    name.setText("");
                    phone.setText("");
                    address.setText("");
                }
            }
        });
    }
    private boolean validate(View v)
    {
        boolean isValid = false ;
        if (!TextUtils.isEmpty(customerName))
        {
            if (!TextUtils.isEmpty(customerPhone))
            {
                if (!TextUtils.isEmpty(customerAdd))
                {
                    isValid = true ;
                }else{
                    Toast.makeText(this, "please enter address", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "please enter phone", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(this, "please enter name", Toast.LENGTH_SHORT).show();
        }
        return isValid ;
    }
    private void mapping()
    {
        choose = findViewById(R.id.addcustomer);
        insert = findViewById(R.id.insert);
        name = findViewById(R.id.customername);
        phone = findViewById(R.id.customerphone);
        address = findViewById(R.id.customeraddress);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String cname = cursor.getString(nameColumnIndex);

                name.setText(cname);
                phone.setText(number);
            }
            }
    }
}