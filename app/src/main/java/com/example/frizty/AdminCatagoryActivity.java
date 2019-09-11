package com.example.frizty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCatagoryActivity extends AppCompatActivity {

    private ImageView maleDress, femaleDress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catagory);


        maleDress = (ImageView)findViewById(R.id.maleDress);;
        femaleDress = (ImageView)findViewById(R.id.femaleDress);;

        maleDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatagoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("catagory", "Male Dress");
                startActivity(intent);
            }
        });

        femaleDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCatagoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("catagory", "Female Dress");
                startActivity(intent);
            }
        });
    }
}
