package com.example.frizty;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.model;
import com.example.frizty.ViewHolder.feedbackViewHolder;

import java.util.List;

public class customAdapter extends RecyclerView.Adapter<feedbackViewHolder> {

    private feedbackList listActivity;
    private  List<model>modelList;
    private  Context context;

    public customAdapter(feedbackList listActivity, List<model> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;


    }



    @NonNull
    @Override
    public feedbackViewHolder  onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout,viewGroup,false);

        feedbackViewHolder viewHolder= new feedbackViewHolder((itemView));

        viewHolder.setOnClickListener(new feedbackViewHolder.ClickListener() {



            @Override
            public void onItemClick(View view, int position) {


                String name = modelList.get(position).getName();
                String email = modelList.get(position).getEmail();
                String comment = modelList.get(position).getComment();
                feedbackList feedbackList = new feedbackList();

                Toast.makeText(feedbackList,name+"\n"+email+"\n"+comment,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull feedbackViewHolder viewHolder, int i) {

        viewHolder.rname.setText(modelList.get(i).getName());
        viewHolder.remail.setText(modelList.get(i).getEmail());
        viewHolder.rcomment.setText(modelList.get(i).getComment());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
