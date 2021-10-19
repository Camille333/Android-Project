package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class MyAdapter extends ArrayAdapter {
    int recourceId;
    public MyAdapter(@NonNull Context context, int resource, @NonNull List<HashMap<String,String>> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }

    @NonNull
    @Override
    //重写getView方法——itemView
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HashMap<String,String> hashMap = (HashMap<String, String>) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(recourceId, parent, false);
        TextView name = view.findViewById(R.id.item_name);
        TextView country_rate = view.findViewById(R.id.item_detail);

/*
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(recourceId, parent, false);
        }

        Item item = (Item) getItem(position);
        TextView name = itemView.findViewById(R.id.item_name);
        TextView country_rate = itemView.findViewById(R.id.item_detail);


 */
        name.setText(hashMap.get("name"));
        country_rate.setText(hashMap.get("value"));

        return view;
    }

}