package com.example.frizty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frizty.Model.Cart;
import com.example.frizty.Prevalent.Prevalent;
import com.example.frizty.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Integer.valueOf;

public class ViewCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextButton;
    private TextView totalPriceTxt, messageTextOne;
    private int totalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        recyclerView = findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextButton = (Button)findViewById(R.id.orderNextButton);
        totalPriceTxt = (TextView)findViewById(R.id.totalPrice);
        messageTextOne = (TextView)findViewById(R.id.messageOne);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(ViewCartActivity.this, ComfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalPrice));
                finish();
                startActivity(intent);
            }
        });
        totalPriceTxt.setText("Total price = Rs." + String.valueOf(totalPrice));
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getUsername()).child("Products"), Cart.class)//Something is wrong in this line
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.textProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.textProductPrice.setText("Price "+ "Rs." + model.getPrice() );
                holder.textProductName.setText(model.getPname());

//                int oneTypeProductTotPrice = (((Integer.valueOf(model.getPrice()))) * Integer.valueOf((model.getQuantity())));
//                totalPrice = totalPrice + oneTypeProductTotPrice;
                try{
                    int oneTypeProductTotPrice = (Integer.valueOf(model.getPrice()))*Integer.valueOf(model.getQuantity());
                    totalPrice += oneTypeProductTotPrice;

                } catch (Exception e){
                    e.printStackTrace();
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                            "Edit",
                            "Remove"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Intent intent = new Intent(ViewCartActivity.this, CartActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } if(which == 1){
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getUsername())
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(ViewCartActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ViewCartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void checkOrderStatus(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped")){
                        totalPriceTxt.setText("Dear " + userName + "\n your order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);

                        messageTextOne.setVisibility(View.VISIBLE);
                        messageTextOne.setText("Congratulations, your final order has been shipped. You will receive your product soon");

                        nextButton.setVisibility(View.GONE);

                        Toast.makeText(ViewCartActivity.this, "You can purchase more product once you receive your first order", Toast.LENGTH_SHORT).show();

                    } else if(shippingState.equals("not shipped")){
                        if(shippingState.equals("shipped")) {
                            totalPriceTxt.setText("Dear " + userName + "\n Shipping state  = Not shipped");
                            recyclerView.setVisibility(View.GONE);

                            messageTextOne.setVisibility(View.VISIBLE);
                            nextButton.setVisibility(View.GONE);

                            Toast.makeText(ViewCartActivity.this, "You can purchase more product once you receive your first order", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
