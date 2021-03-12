package com.uit.digi_khata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AcountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String title,phone,address,userid,id,cname,cphone,date,time,accept,pay,currentDate,currentTime;
    private DatabaseAccountHelper mydb ;
    private  DatabseHelper profileDb ;
    FirebaseAuth fauth ;
    ListView listView ;
    Cursor result ;
    List<AcountModel>  modelList = new ArrayList<AcountModel>() ;
    final int SEND_SMS_PERMISSION=1;
    LocalTime t;
    LocalDate d ;
    boolean b ;
    ListAdapterOne listAdapterOne ;
    LinearLayout sms ;
    int sumA = 0 , sumP = 0 ;
    TextView msg,value ;
    PdfDocument.PageInfo mypageinfo1;
    PdfDocument mypdfDocument ;
    PdfDocument.Page mypage1;
    private Cursor res ;
    private String ownerName , companyName , ownerPhone ;

 /*   String[] dates = {"01 jan 2021","04 jan 2021","21 jan 2021","13 Feb 2021","01 March 2021","02 Mar 2021","04 Mar 2021"} ;
    String[] debit = {"100","","200","","","150",""} ;
    String[] credit = {"","50","","200","50","","100"} ; */

    private ArrayList<String> datess = new ArrayList<String>() ;
    private ArrayList<String> times = new ArrayList<String>() ;
    private ArrayList<String> moneyCome = new ArrayList<String>() ;
    private ArrayList<String> moneyOut = new ArrayList<String>() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        listView = findViewById(R.id.listview) ;
        sms = findViewById(R.id.sms) ;
        msg = findViewById(R.id.msg) ;
        value = findViewById(R.id.value) ;

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
       profileDb = new DatabseHelper(getApplicationContext()) ;

       fauth = FirebaseAuth.getInstance();

        userid = fauth.getCurrentUser().getUid() ;
       // result = mydb.getpaymentData();
        listAdapterOne = new ListAdapterOne(getApplicationContext(),R.layout.acountant_list) ;
        listView.setAdapter(listAdapterOne);


        getData();

        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        if (checkpermission(Manifest.permission.SEND_SMS)){
            sms.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION);
        }

        res = profileDb.getOwnerData() ;
        while (res.moveToNext())
        {
            String id1 = res.getString(0) ;
            if (id1.equals(userid))
            {
                ownerPhone = res.getString(2) ;
                ownerName = res.getString(3) ;
                companyName = res.getString(4) ;
            }
        }





    }


    public void calling(View view) {
     Intent i = new Intent(Intent.ACTION_DIAL) ;
     i.setData(Uri.parse("tel:"+phone)) ;
     startActivity(i); ;
    }

    public void create_reports(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            t = LocalTime.now() ;
            d = LocalDate.now() ;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mypdfDocument = new PdfDocument() ;
            Paint mypaint = new Paint() ;

            mypageinfo1 = new PdfDocument.PageInfo.Builder(350,700,1).create();
            mypage1 = mypdfDocument.startPage(mypageinfo1) ;
            Canvas canvas = mypage1.getCanvas() ;

            mypaint.setColor(Color.BLUE);
            canvas.drawRect(0,0,mypageinfo1.getPageWidth(),40,mypaint) ;
            canvas.drawRect(0,mypageinfo1.getPageHeight()-40,mypageinfo1.getPageWidth(),mypageinfo1.getPageHeight(),mypaint);
            mypaint.setColor(Color.WHITE);
            mypaint.setTextAlign(Paint.Align.LEFT);
            mypaint.setTextSize(9.0f);
            canvas.drawText(companyName,20,20,mypaint);
            canvas.drawText("start using Digi Khata",20,mypageinfo1.getPageHeight()-20,mypaint);

            mypaint.setTextAlign(Paint.Align.RIGHT);
            mypaint.setTextSize(9.0f);
            canvas.drawText("DIGI KHATA",mypageinfo1.getPageWidth()-20,20,mypaint);
            canvas.drawText("Help :+91 "+ownerPhone,mypageinfo1.getPageWidth()-20,mypageinfo1.getPageHeight()-20,mypaint);

            mypaint.setTextAlign(Paint.Align.CENTER);
            mypaint.setColor(Color.BLACK);
            mypaint.setTextSize(10.0f);
            canvas.drawText(title+" Statement",mypageinfo1.getPageWidth()/2,60,mypaint);
            mypaint.setTextSize(7.0f);
            mypaint.setColor(Color.GRAY);
            canvas.drawText("phone number : "+phone,mypageinfo1.getPageWidth()/2,70,mypaint);
            canvas.drawText("Address : "+address,mypageinfo1.getPageWidth()/2,80,mypaint);

            canvas.drawRect(20,100,mypageinfo1.getPageWidth()-20,122,mypaint);
            mypaint.setColor(Color.BLACK);
            mypaint.setTextSize(8.0f);
            canvas.drawText("Date",40,115,mypaint);
            canvas.drawText("Debit(-)",120,115,mypaint);
            canvas.drawText("Credit(+)",215,115,mypaint);
            canvas.drawText("Balance",mypageinfo1.getPageWidth()-50,115,mypaint);

           float h = 130 ;
            int sum = 0,sumD=0,sumC=0 ;
            for(int i=0;i<datess.size();i++)
            {
                mypaint.setStyle(Paint.Style.STROKE);
                mypaint.setColor(Color.GRAY);
                mypaint.setStrokeWidth(2.0f);
                canvas.drawRect(20,h,mypageinfo1.getPageWidth()-20,h+20,mypaint);
                mypaint.setTextSize(7.0f);
                mypaint.setStyle(Paint.Style.FILL);
                canvas.drawText(datess.get(i),50,h+13,mypaint);
                if (moneyOut.get(i).equals(""))
                {
                    mypaint.setColor(Color.GREEN);
                    canvas.drawText(moneyCome.get(i),215,h+13,mypaint);
                    sum+=Integer.parseInt(moneyCome.get(i)) ;
                    sumC+=Integer.parseInt(moneyCome.get(i));
                }
                else
                {
                    mypaint.setColor(Color.RED);
                    canvas.drawText(moneyOut.get(i),120,h+13,mypaint);
                    sum-=Integer.parseInt(moneyOut.get(i)) ;
                    sumD+=Integer.parseInt(moneyOut.get(i));
                }
                if (sum<0)
                {
                    mypaint.setColor(Color.RED);
                    canvas.drawText((int)(Math.abs(sum))+"Dr",mypageinfo1.getPageWidth()-50,h+13,mypaint);
                }
                else{
                    mypaint.setColor(Color.GREEN);
                    canvas.drawText(sum+"Cr",mypageinfo1.getPageWidth()-50,h+13,mypaint);
                }
                h+=30 ;
            }

            mypaint.setColor(Color.GRAY);
            canvas.drawRect(20,h,mypageinfo1.getPageWidth()-20,h+22,mypaint);
            mypaint.setColor(Color.BLACK);
            canvas.drawText("Grand Total",50,h+13,mypaint);
            mypaint.setColor(Color.RED);
            canvas.drawText(sumD+"Rs.",120,h+13,mypaint);
            mypaint.setColor(Color.GREEN);
            canvas.drawText(sumC+"Rs.",215,h+13,mypaint);
            if (sumC<sumD)
            {
                mypaint.setColor(Color.RED);
                canvas.drawText((sumD-sumC)+"Dr.",mypageinfo1.getPageWidth()-50,h+13,mypaint);
            }
            else
            {
                mypaint.setColor(Color.GREEN);
                canvas.drawText((sumC-sumD)+"Cr.",mypageinfo1.getPageWidth()-50,h+13,mypaint);
            }
            mypaint.setTextSize(8.0f);
            mypaint.setColor(Color.GRAY);
            canvas.drawText("Report Generated At"+t.toString().substring(0,5)+" | "+d.toString(),100,h+40,mypaint);

            mypaint.setColor(Color.BLACK);
            mypaint.setTextSize(10.0f);
            canvas.drawText("Signature Of Owner",mypageinfo1.getPageWidth()-60,mypageinfo1.getPageHeight()-60,mypaint);
            canvas.drawLine(mypageinfo1.getPageWidth()-120,mypageinfo1.getPageHeight()-70,mypageinfo1.getPageWidth()-10,mypageinfo1.getPageHeight()-70,mypaint);
            mypaint.setColor(Color.GRAY);
            canvas.drawText(ownerName.toUpperCase(),mypageinfo1.getPageWidth()-60,mypageinfo1.getPageHeight()-80,mypaint);



            mypdfDocument.finishPage(mypage1);

            File file = new File(Environment.getExternalStorageDirectory(),"/digikhata1.pdf") ;


            try {
                mypdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mypdfDocument.close();

            Toast.makeText(this, "PDF created", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "PDF Not Created", Toast.LENGTH_SHORT).show();
        }

    }

    public void sms_alert(View view) {
        final EditText m=new EditText(view.getContext());
        AlertDialog.Builder alert=new AlertDialog.Builder(view.getContext());
        alert.setTitle("Request For Payment");
        alert.setMessage("Enter message");
        alert.setCancelable(false);
        alert.setView(m);
        alert.setPositiveButton("send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String message=m.getText().toString().trim();
                if (checkpermission(Manifest.permission.SEND_SMS))
                {
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phone,null,message,null,null);
                    Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Message not sent",Toast.LENGTH_LONG).show();
                }

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.create().show();
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

                datess.add(date) ;
                times.add(time) ;
                moneyCome.add(accept) ;
                moneyOut.add(pay) ;



                if (!TextUtils.isEmpty(accept))
                {
                    sumA+=Integer.parseInt(accept) ;
                    accept = "₹"+accept ;
                }
                if (!TextUtils.isEmpty(pay))
                {
                    sumP+=Integer.parseInt(pay) ;
                    pay = "₹"+pay ;
                }

                AcountModel acountModel = new AcountModel(date,time,pay,accept) ;
                listAdapterOne.add(acountModel);


            }

        }
        if (sumA>sumP)
        {
            msg.setText("You will give");
            msg.setTextColor(Color.RED);
            value.setText("₹"+(sumA-sumP));
            value.setTextColor(Color.RED);
        }
        else if(sumP>sumA)
        {
            msg.setText("You will got");
            msg.setTextColor(Color.RED);
            value.setText("₹"+(sumP-sumA));
            value.setTextColor(Color.RED);
        }
        else {
            msg.setText("All transaction done!!");
            msg.setTextColor(Color.GREEN);
            value.setText("₹"+(sumP-sumA));
            value.setTextColor(Color.GREEN);
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
                      //  getData();
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
    public boolean checkpermission(String Permission)
    {
        int check= ContextCompat.checkSelfPermission(this,Permission);
        return (check== PackageManager.PERMISSION_GRANTED);
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