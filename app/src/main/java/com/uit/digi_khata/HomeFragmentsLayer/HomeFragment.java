package com.uit.digi_khata.HomeFragmentsLayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.uit.digi_khata.AcountActivity;
import com.uit.digi_khata.CustomerActivity;
import com.uit.digi_khata.DatabaseAccountHelper;
import com.uit.digi_khata.DatabaseHelperCustomer;
import com.uit.digi_khata.ItemModel;
import com.uit.digi_khata.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    EditText search ;
    ListView listView ;
    FloatingActionButton contact ;
    TextView paid,due ;
    FirebaseAuth fauth ;
    String userid ;
    Cursor result,resultMoney ;
    DatabaseHelperCustomer mydb ;
    ItemModel itemModel;
    List<ItemModel> modelList=new ArrayList<>();
    private double sumC = 0.0 , sumD = 0.0 ;
    private DatabaseAccountHelper databaseAccountHelper ;


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
        paid = view.findViewById(R.id.t2) ;
        due = view.findViewById(R.id.t4) ;
       mydb = new DatabaseHelperCustomer(getActivity());
       databaseAccountHelper = new DatabaseAccountHelper(getActivity()) ;
        fauth = FirebaseAuth.getInstance();
        userid = fauth.getCurrentUser().getUid();
       result = mydb.customer_data() ;

       while (result.moveToNext())
        {
            String id = result.getString(3) ;
           // Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            if (id.equals(userid))
            {
                itemModel = new ItemModel(result.getString(1),result.getString(0),result.getString(2));
            }

            modelList.add(itemModel) ;

        }
       CustomAdapter customAdapter = new CustomAdapter(modelList,getActivity()) ;
       listView.setAdapter(customAdapter);

       resultMoney = databaseAccountHelper.getpaymentData() ;
       if (resultMoney.getCount() == 0)
       {
           sumC = 0.00 ;
           sumD = 0.00;
       }
       else
       {
           while (resultMoney.moveToNext())
           {
               String id = resultMoney.getString(0) ;
               if (id.equals(userid))
               {
                   if (resultMoney.getString(5).equals(""))
                   {
                       sumD+=Double.parseDouble(resultMoney.getString(6)) ;
                   }
                   else
                   {
                       sumC+=Double.parseDouble(resultMoney.getString(5)) ;
                   }
               }
           }
           paid.setText("₹"+sumC);
           due.setText("₹"+sumD);
       }

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               String n = modelList.get(position).getCustomerName();
               String p = modelList.get(position).getCustomerPhone();
               String a = modelList.get(position).getCustomerAddress();

               Bundle bundle = new Bundle() ;
               bundle.putString("name",n);
               bundle.putString("phone",p);
               bundle.putString("address",a);
               Intent intent = new Intent(getActivity(), AcountActivity.class) ;
               intent.putExtras(bundle) ;
               startActivity(intent);
           }
       });

       search.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               customAdapter.getFilter().filter(s);
               customAdapter.notifyDataSetChanged();
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));
            }
        });
        return view ;
    }
   public class CustomAdapter extends BaseAdapter implements Filterable
   {
       List<ItemModel> itemModelList ;
       List<ItemModel> itemModelListFilter ;
       Context context ;

       public CustomAdapter(List<ItemModel> itemModelList,Context context)
       {
           this.itemModelList = itemModelList ;
           this.itemModelListFilter = itemModelList ;
           this.context = context ;
       }

       @Override
       public int getCount() {
           return itemModelListFilter.size();
       }

       @Override
       public Object getItem(int position) {
           return itemModelListFilter.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent)
       {
           View view=getLayoutInflater().inflate(R.layout.customer_list,null);
           TextView name = view.findViewById(R.id.cname) ;
           TextView phone = view.findViewById(R.id.cphone);
           TextView address = view.findViewById(R.id.cadd);

           name.setText("Name : "+itemModelListFilter.get(position).getCustomerName());
           phone.setText("Phone : "+itemModelListFilter.get(position).getCustomerPhone());
           address.setText("Address : "+itemModelListFilter.get(position).getCustomerAddress());
           return view;
       }

       @Override
       public Filter getFilter() {

           Filter filter = new Filter() {
               @Override
               protected FilterResults performFiltering(CharSequence constraint) {
                   FilterResults filterResults = new FilterResults() ;
                   if (constraint == null || constraint.length() == 0)
                   {
                       filterResults.count = itemModelList.size() ;
                       filterResults.values = itemModelList ;
                   }
                   else
                   {
                       List<ItemModel> resultModel = new ArrayList<ItemModel>();
                       String searchStr = constraint.toString().toLowerCase();
                       for (ItemModel itemModel : itemModelList)
                       {
                           if (itemModel.getCustomerName().toLowerCase().contains(searchStr))
                           {
                               resultModel.add(itemModel);
                           }
                           filterResults.count=resultModel.size();
                           filterResults.values=resultModel;
                       }
                   }
                   return filterResults;
               }

               @Override
               protected void publishResults(CharSequence constraint, FilterResults results) {
                   itemModelListFilter = (List<ItemModel>)results.values ;
                   modelList =(List<ItemModel>)results.values ;
                   notifyDataSetChanged();

               }
           };
           return filter;
       }
   }

}