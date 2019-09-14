package com.example.frizty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frizty.PaypalConfig.PaypalClientCode;
import com.example.frizty.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ComfirmFinalOrderActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalClientCode.PAYPAL_CLIENT_ID);

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText, postalCodeText;
    private Button confirmOrderBtn;
    private String totalAmount = "";




    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total price is = Rs." + totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderBtn = (Button)findViewById(R.id.confirmButton);
        nameEditText = (EditText)findViewById(R.id.shipmentName);
        phoneEditText = (EditText)findViewById(R.id.shipmentPhone);
        addressEditText = (EditText)findViewById(R.id.shipmentAddress);
        cityEditText = (EditText)findViewById(R.id.shipmentCity);
        postalCodeText = (EditText)findViewById(R.id.shipmentPostalCode);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });
    }

    private void check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else  if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(this, "Please enter your city", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(postalCodeText.getText().toString())){
            Toast.makeText(this, "Please enter your postal code", Toast.LENGTH_SHORT).show();
        }else{
            confirmOrder();
        }
    }

    private void confirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calendar.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getUsername());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("postalCode", postalCodeText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference().child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getUsername())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(totalAmount)),"Rs.", "Donate for EDMTDev", PayPalPayment.PAYMENT_INTENT_SALE);
                                    Toast.makeText(ComfirmFinalOrderActivity.this, "Final order has been pleased successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ComfirmFinalOrderActivity.this, PaymentActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null) {
                    try {
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", totalAmount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

}
