package com.example.frizty.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.frizty.AdminNewOrderActivity;
import com.example.frizty.HomeActivity;
import com.example.frizty.R;

public class AdminCatagoryActivity extends AppCompatActivity {

    private ImageView maleDress, femaleDress;
    private Button adminLogoutButton, chechkOredersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catagory);


        maleDress = (ImageView)findViewById(R.id.maleDress);;
        femaleDress = (ImageView)findViewById(R.id.femaleDress);;

        adminLogoutButton = (Button)findViewById(R.id.adminLogOutButton);
        chechkOredersButton = (Button)findViewById(R.id.checkOrdersButton);



        adminLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatagoryActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });


        chechkOredersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCatagoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

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
