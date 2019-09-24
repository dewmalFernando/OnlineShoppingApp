package com.example.frizty.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.R;

public class ViewHolderDilivery extends RecyclerView.ViewHolder {


    public TextView streetname,cityname,statename,codename;
    public View rview;

    public ViewHolderDilivery(@NonNull View itemView) {
        super(itemView);

        rview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rClickListner.onItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                rClickListner.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

        streetname=itemView.findViewById(R.id.streetname);
        cityname=itemView.findViewById(R.id.cityname);
        statename=itemView.findViewById(R.id.statename);
        codename=itemView.findViewById(R.id.codename);



    }

    private ViewHolderDilivery.ClickListener rClickListner;


    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolderDilivery.ClickListener clickListener) {


        rClickListner=clickListener;
    }

}


