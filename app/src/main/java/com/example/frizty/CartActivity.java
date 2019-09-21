package com.example.frizty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.frizty.Model.Products;
import com.example.frizty.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    //private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView producrPrice, productDescription, productName;
    private String productId = "", state = "Normal";
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productId = getIntent().getStringExtra("pid");


        addToCartButton = (Button)findViewById(R.id.pdAddToCartButton);
        numberButton = (ElegantNumberButton)findViewById(R.id.numberButton);
        productImage = (ImageView)findViewById(R.id.productImageDetails);
        producrPrice = (TextView)findViewById(R.id.productPriceDetails);
        productDescription = (TextView)findViewById(R.id.productDescriptionDetails);
        productName = (TextView)findViewById(R.id.productNameDetails);


        getProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();

                if(state.equals("Order Placed") || state.equals("Order Shipped")){
                    Toast.makeText(CartActivity.this, "You can purchase more products, once your order is shipped or confirmed ", Toast.LENGTH_SHORT).show();
                } else {
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderStatus();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("pid", productId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", producrPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getUsername())
                .child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getUsername())
                                    .child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(CartActivity.this, "Added to the cart list", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    }
                });

    }

    private void getProductDetails(String productId) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        Products products = dataSnapshot.getValue(Products.class);
                        productName.setText(products.getProductName());
                        producrPrice.setText(products.getPrice());
                        productDescription.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkOrderStatus(){
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";
                    } else if(shippingState.equals("not shipped")){
                        if(shippingState.equals("shipped")) {
                            state = "Order Placed";
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
