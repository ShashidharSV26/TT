package com.example.tt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Home_Act_adapter extends RecyclerView.Adapter<Home_Act_adapter.HomeViewHolder> {

    Context context;
    private Bitmap bitmap;

    ArrayList<HomeModel> homeItems;

    public Home_Act_adapter(Context context, ArrayList<HomeModel> homeItems) {
        this.context = context;
        this.homeItems = homeItems;
    }


    @NonNull
    @Override
    public Home_Act_adapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View listitem=layoutInflater.inflate(R.layout.homepage,parent,false);
        HomeViewHolder homeholder=new HomeViewHolder(listitem);
        return homeholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Home_Act_adapter.HomeViewHolder holder, int position) {
            HomeModel hmodel=(HomeModel) homeItems.get(position);
            holder.cardtitle.setText(hmodel.getCatagory().toUpperCase());
        Log.d("homeadapter","============="+hmodel.getCatagory());


        holder.cardimg.setImageResource(hmodel.getImage());

//        holder.cardimg.setImageBitmap(bitmap);
////        holder.cardimg.setImageResource(homeIcons[position]);
//
//        if(holder.cardimg!=null){
//            new ImageDownlaaderTask(holder.cardimg).execute(image);
//        }


            holder.cardcatagory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String item=hmodel.getCatagory();
                    Log.d("HomeActAdapter","onclick home item=="+item);
                    if(item.equals("About") || item.equals("Safety")){
                        Intent intent = new Intent(context, ProfilePage_Act.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, DetailPage_Act.class);
                        intent.putExtra("maintitle", item);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return homeItems.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout cardcatagory;
        TextView cardtitle;
        ImageView cardimg;


        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardcatagory=itemView.findViewById(R.id.catagorycard);
            this.cardtitle=itemView.findViewById(R.id.catagoryname);
            this.cardimg=itemView.findViewById(R.id.catagoryicon);
        }
    }
}
