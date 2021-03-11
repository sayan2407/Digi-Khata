package com.uit.digi_khata.HomeFragmentsLayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.uit.digi_khata.DatabseHelper;
import com.uit.digi_khata.LoginOrSignUp;
import com.uit.digi_khata.R;


public class UserFragment extends Fragment {


   DatabseHelper databseHelper ;
   private Button logout ;
   private TextView companyname,owner,email,phone ;
   private Cursor result ;
   private FirebaseAuth fauth ;
   private String userid,id,emailid,phoneno,name,cname;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        logout = view.findViewById(R.id.logout) ;
        companyname = view.findViewById(R.id.companyname) ;
        owner = view.findViewById(R.id.owner) ;
        email = view.findViewById(R.id.email) ;
        phone = view.findViewById(R.id.phone) ;
        databseHelper = new DatabseHelper(getActivity()) ;
        fauth = FirebaseAuth.getInstance();
        userid = fauth.getCurrentUser().getUid() ;

        result = databseHelper.getOwnerData();
        while (result.moveToNext())
        {
            id = result.getString(0) ;
            if (id.equals(userid))
            {
              emailid = result.getString(1) ;
              phoneno = result.getString(2) ;
              name = result.getString(3) ;
              cname = result.getString(4) ;
              Toast.makeText(getActivity(), "Got it", Toast.LENGTH_SHORT).show();

            }
        }
        companyname.setText(cname);
        owner.setText("Owner : "+name);
        email.setText("Email : "+emailid);
        phone.setText("Phone : +91 "+phoneno);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
                builder.setTitle("Sign Out!!");
                builder.setMessage("Are you sure want to sign out?");
                builder.setCancelable(false) ;
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), LoginOrSignUp.class) ;
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
                        startActivity(i);
                        fauth.signOut();
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        return view ;
    }
}