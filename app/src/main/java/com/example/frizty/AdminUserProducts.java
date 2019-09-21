package com.example.frizty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.Cart;
import com.example.frizty.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProducts extends AppCompatActivity {


    private RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String userId = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userId = getIntent().getStringExtra("uid");
        productList = (RecyclerView) findViewById(R.id.productList);
        //productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        //productList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(userId).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart>options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int i, @NonNull Cart model) {
                holder.textProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.textProductPrice.setText("Price "+ "Rs." + model.getPrice() );
                holder.textProductName.setText(model.getPname());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

//          productList.setAdapter(adapter);
//          adapter.startListening();
    }
}
