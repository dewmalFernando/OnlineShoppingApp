package com.example.frizty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.productList);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, adminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, adminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull adminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhone.setText("Phone: " + model.getPhone());
                holder.userTotPrice.setText("Total Amount: " + model.getTotalAmount());
                holder.userDateTime.setText("Order at: " + model.getDate() + "" +model.getTime());
                holder.userPostalCode.setText("Postal Code: " + model.getPostalCode());
                holder.userShippingAddress.setText("Address: " + model.getAddress() + "," + model.getCity());

                holder.showOrdersButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProducts.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public adminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new adminOrdersViewHolder(view);
            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class adminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhone, userTotPrice, userDateTime, userPostalCode, userShippingAddress;
        public Button showOrdersButton;

        public adminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.orderUserName);
            userPhone = itemView.findViewById(R.id.orderPhoneNumber);
            userDateTime = itemView.findViewById(R.id.orderDateAndTime);
            userTotPrice = itemView.findViewById(R.id.orderTotalPrice);
            userPostalCode = itemView.findViewById(R.id.orderPostalCode);
            userShippingAddress = itemView.findViewById(R.id.orderAddressAndCity);
            showOrdersButton = itemView.findViewById(R.id.showAllProductsButton);
        }
    }
}
