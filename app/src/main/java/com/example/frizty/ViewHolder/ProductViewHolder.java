package com.example.frizty.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.frizty.Interface.ItemClickListner;
import com.example.frizty.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, textProductDescription, textProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView)itemView.findViewById(R.id.productImage);
        txtProductName = (TextView)itemView.findViewById(R.id.productName);
        textProductDescription = (TextView)itemView.findViewById(R.id.productDescription);
        textProductPrice = (TextView)itemView.findViewById(R.id.productPrice);
    }

    public void setItemClickLliner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
