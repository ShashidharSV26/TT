package com.example.tt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DetailsAdapter extends ArrayAdapter<DetailsModel> {

    private List<DetailsModel> languages;
    private Context context;

    public DetailsAdapter(Context context,List<DetailsModel> languages) {
        super(context, R.layout.lang_list,languages);
        this.languages = languages;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView=inflater.inflate(R.layout.lang_list,null,true);
        TextView langu=convertView.findViewById(R.id.lang);

        DetailsModel model=languages.get(position);
        String name=model.getLanguage();
        langu.setText(name.toUpperCase());

         return convertView;
    }



}
