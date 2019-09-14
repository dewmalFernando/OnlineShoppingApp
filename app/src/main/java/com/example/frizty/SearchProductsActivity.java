package com.example.frizty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.frizty.Model.Products;
import com.example.frizty.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchProductsActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);


        inputText = (EditText)findViewById(R.id.searchProductText);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchList = findViewById(R.id.searchList);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = inputText.getText().toString();

                onStart();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions
                .Builder<Products>()
                .setQuery(reference.orderByChild("productName").startAt(searchInput), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Products model) {
                holder.txtProductName.setText(model.getProductName());
                holder.textProductDescription.setText(model.getDescription());
                holder.textProductPrice.setText("Price = "+ "Rs." + model.getPrice() );
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchProductsActivity.this, CartActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
