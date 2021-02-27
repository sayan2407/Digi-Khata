package com.uit.digi_khata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterOne extends ArrayAdapter {
    List list=new ArrayList();
    public ListAdapterOne(@NonNull Context context, int resource) {
        super(context, resource);
    }
    static class LayoutHandelerone
    {
        TextView DATE,TIME,ACCEPT,CREDIT;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object) ;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutHandelerone layoutHandelerone;
        if (row==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.acountant_list,parent,false);

            layoutHandelerone=new LayoutHandelerone();
            layoutHandelerone.DATE= (TextView)row.findViewById(R.id.date);
            layoutHandelerone.TIME=(TextView)row.findViewById(R.id.time);
            layoutHandelerone.ACCEPT=(TextView)row.findViewById(R.id.got);
            layoutHandelerone.CREDIT=(TextView)row.findViewById(R.id.gave);
            row.setTag(layoutHandelerone);

        }else {
            layoutHandelerone=(LayoutHandelerone)row.getTag();
        }
        AcountModel dataProviderOne=(AcountModel) this.getItem(position);

        layoutHandelerone.DATE.setText(dataProviderOne.getDate());
        layoutHandelerone.TIME.setText(dataProviderOne.getTime());
        layoutHandelerone.ACCEPT.setText(dataProviderOne.getGot());
        layoutHandelerone.CREDIT.setText(dataProviderOne.getGave());

        return row;
    }
}
