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

public class CustomerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final int REQUEST_CODE = 1;
    private Button choose , insert ;
    private TextInputEditText name,phone,address ;
    private String customerName,customerPhone,customerAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mapping();

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
                    Toast.makeText(CustomerActivity.this, "phone:"+customerPhone+"\nName : "+customerName, Toast.LENGTH_SHORT).show();
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