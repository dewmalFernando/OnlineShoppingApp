package com.example.frizty.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Interface.ItemClickListner;
import com.example.frizty.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName, textProductPrice, textProductQuantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        textProductName = itemView.findViewById(R.id.cartProductName);
        textProductPrice = itemView.findViewById(R.id.cartProductPrice);
        textProductQuantity = itemView.findViewById(R.id.cartProductQty);
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
