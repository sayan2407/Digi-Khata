package com.uit.digi_khata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AcountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String title,phone,address,userid,id,cname,cphone,date,time,accept,pay,currentDate,currentTime;
    DatabaseAccountHelper mydb ;
    FirebaseAuth fauth ;
    ListView listView ;
    Cursor result ;
    List<AcountModel>  modelList = new ArrayList<AcountModel>() ;

    LocalTime t;
    LocalDate d ;
    boolean b ;
    ListAdapterOne listAdapterOne ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        listView = findViewById(R.id.listview) ;

        toolbar = findViewById(R.id.mytoolbar) ;
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        title = bundle.getString("name");
        phone = bundle.getString("phone");
        address = bundle.getString("address") ;
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       mydb = new DatabaseAccountHelper(getApplicationContext()) ;

       fauth = FirebaseAuth.getInstance();

        userid = fauth.getCurrentUser().getUid() ;
       // result = mydb.getpaymentData();
        listAdapterOne = new ListAdapterOne(getApplicationContext(),R.layout.acountant_list) ;
        listView.setAdapter(listAdapterOne);


        getData();



    }


    public void calling(View view) {
        Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
    }

    public void create_reports(View view) {
        Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
    }

    public void sms_alert(View view) {
        Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show();
    }

    public void gave(View view)
    {
        alert_Dialog("You gave to "+title,"Enter amount",view.getContext(),0);

    }

    public void got(View view) {
        alert_Dialog("You got from "+title,"Enter amount",view.getContext(),1);
    }

    public void getData()
    {
        result = mydb.getpaymentData();
        listAdapterOne.clear();
        while (result.moveToNext())
        {
            id = result.getString(0) ;
            cname = result.getString(1);
            cphone = result.getString(2) ;


            if (id.equals(userid) && cname.equals(title) && cphone.equals(phone))
            {
                date = result.getString(3) ;
                time = result.getString(4) ;
                accept = result.getString(5) ;
                pay = result.getString(6) ;



                if (!TextUtils.isEmpty(accept))
                {
                    accept = "₹"+accept ;
                }
                if (!TextUtils.isEmpty(pay))
                {
                    pay = "₹"+pay ;
                }

                AcountModel acountModel = new AcountModel(date,time,pay,accept) ;
                listAdapterOne.add(acountModel);


            }

        }


    }


    public void alert_Dialog(String title1, String message, Context context,int flag)
    {
        EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder alert = new AlertDialog.Builder(context) ;
        alert.setTitle(title1) ;
        alert.setMessage(message) ;
        alert.setCancelable(false);
        alert.setView(editText) ;
        alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    t = LocalTime.now();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    d = LocalDate.now();
                }
                String amount = editText.getText().toString().trim();


                if (!TextUtils.isEmpty(amount))
                {
                    if (flag==1)
                    {
                       b = mydb.paymentInsert(userid,title,phone,d.toString(),t.toString(),amount,"");
                    }
                    else
                    {
                      b =  mydb.paymentInsert(userid,title,phone,d.toString(),t.toString(),"",amount);
                    }

                    if (b)
                    {
                        Toast.makeText(context, "Amount Saved", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                    else
                    {
                        Toast.makeText(context, "Amount Not Saved", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(context, "Please Enter amount", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.create().show();
    }

 /*   public class CustomAdapter extends BaseAdapter
    {
        List<AcountModel> acountModelList ;
        Context context ;

        public CustomAdapter(List<AcountModel> acountModelList, Context context) {
            this.acountModelList = acountModelList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return acountModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return acountModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = getLayoutInflater().inflate(R.layout.acountant_list,null) ;
            TextView date = view.findViewById(R.id.date) ;
            TextView time = view.findViewById(R.id.time) ;
            TextView accept = view.findViewById(R.id.got) ;
            TextView pay = view.findViewById(R.id.gave);

            String d = acountModelList.get(position).getDate();
            String t = acountModelList.get(position).getTime();
            String a = acountModelList.get(position).getGot();
            String p = acountModelList.get(position).getGave() ;

            date.setText("Date : "+d);
            time.setText("Time :"+t);
            accept.setText(a);
            pay.setText(p);

            return view;
        }
    } */
}