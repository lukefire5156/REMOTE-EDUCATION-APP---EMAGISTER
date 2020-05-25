package io.agora.openvcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.openvcall.R;

public class MobileActivity extends AppCompatActivity {

    private EditText number;
    private Button requestOTP;
    String No;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRegister,dbUser;
    ProgressBar requestPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);


        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Emagister");

        number = findViewById(R.id.mobileNo);
        requestOTP = findViewById(R.id.requestOTP);
        requestPB = findViewById(R.id.RequestProgressBar);

        requestOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(number.getText().toString())){
                    Toast.makeText(MobileActivity.this, "Enter No ....", Toast.LENGTH_SHORT).show();
                }
                else if(number.getText().toString().replace(" ","").length()!=10){
                    Toast.makeText(MobileActivity.this, "Enter Correct No ...", Toast.LENGTH_SHORT).show();
                }
                else {
                    requestPB.setVisibility(View.VISIBLE);
                   /* Intent intent = new Intent(MainActivity.this,OtpVerification.class);
                    intent.putExtra("number","+91"+number.getText().toString());
                    startActivity(intent);
                    finish();*/
                    No = number.getText().toString();
                    dbUser = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(No);
                    dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                requestPB.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(MobileActivity.this,OtpVerification.class);
                                intent.putExtra("number","+91"+No);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                requestPB.setVisibility(View.INVISIBLE);
                                new AlertDialog.Builder(MobileActivity.this)
                                        .setTitle("Oops!")
                                        .setMessage("Access Denied!... Ask Administration to give access.")
                                        .setNegativeButton("OK", null)
                                        .setIcon(R.drawable.ic_alert)
                                        .show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            requestPB.setVisibility(View.INVISIBLE);
                            new AlertDialog.Builder(MobileActivity.this)
                                    .setTitle("Oops!")
                                    .setMessage("Something went wrong!")
                                    .setNegativeButton("CANCEL", null)
                                    .setIcon(R.drawable.ic_alert)
                                    .show();
                        }
                    });
                }
            }
        });


    }


    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }


}
