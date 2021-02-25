package com.uit.digi_khata.HomeFragmentsLayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uit.digi_khata.CustomerActivity;
import com.uit.digi_khata.R;

public class HomeFragment extends Fragment {

    EditText search ;
    ListView listView ;
    FloatingActionButton contact ;
    TextView paid,due ;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        search = view.findViewById(R.id.search);
        listView = view.findViewById(R.id.listview);
        contact = view.findViewById(R.id.contacts) ;
        paid = view.findViewById(R.id.t3) ;
        due = view.findViewById(R.id.t4) ;

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));
            }
        });
        return view ;
    }
}