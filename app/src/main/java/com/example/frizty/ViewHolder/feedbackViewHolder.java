package com.example.frizty.ViewHolder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.R;

import java.text.CollationElementIterator;

public class feedbackViewHolder extends RecyclerView.ViewHolder {

    public TextView rname,remail,rcomment;
    View rview;


    public feedbackViewHolder(@NonNull View itemView) {
        super(itemView);

        rview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          mClickListner.onItemClick(view,getAdapterPosition());

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListner.onItemLongClick(view,getAdapterPosition());

                return true;
            }
        });


        rname = itemView.findViewById(R.id.rname);
        remail =itemView.findViewById(R.id.remail);
        rcomment =itemView.findViewById(R.id.rcomment);
    }


    private feedbackViewHolder.ClickListener mClickListner;

    public interface ClickListener{
        void onItemClick(View view ,int position);
        void onItemLongClick(View view ,int position);

    }

    public void setOnClickListener(feedbackViewHolder.ClickListener clickListener){

        mClickListner=clickListener;
    }
}
