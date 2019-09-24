package com.example.frizty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesButton, deletProductButton;
    private EditText name, price, description;
    private ImageView imageView;

    private String productId = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        applyChangesButton = findViewById(R.id.applyChangeButton);
        name = findViewById(R.id.productName);
        price = findViewById(R.id.productPrice);
        description = findViewById(R.id.productDescription);
        imageView = findViewById(R.id.productImage);
        deletProductButton = findViewById(R.id.deleteProductButton);

        displaySpecificProductInfo();


        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deletProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

//////////////////////////////////////////
    private void deleteProduct() {

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
//        databaseReference.child().removeValue();
//
//
//        Toast.makeText(this, "Product Deleted Successfully", Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
//        startActivity(intent);
    }

    private void applyChanges() {
        String productName = name.getText().toString();
        String productPrice = price.getText().toString();
        String productDescription = description.getText().toString();

        if(productName.equals("")){
            Toast.makeText(this, "Enter the product name", Toast.LENGTH_SHORT).show();
        } else if(productPrice.equals("")){
            Toast.makeText(this, "Enter the product price", Toast.LENGTH_SHORT).show();
        } else if(productDescription.equals("")){
            Toast.makeText(this, "Enter the product description", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", productDescription);
            productMap.put("price", productPrice);
            productMap.put("productName", productName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String productName = dataSnapshot.child("productName").getValue().toString();
                    String productPrice = dataSnapshot.child("price").getValue().toString();
                    String productDescription = dataSnapshot.child("description").getValue().toString();
                    String productImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(productName);
                    price.setText(productPrice);
                    description.setText(productDescription);
                    Picasso.get().load(productImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
