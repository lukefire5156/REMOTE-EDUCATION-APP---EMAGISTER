package io.agora.openvcall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
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


public class SplashScreenActivity extends AppCompatActivity {

    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
            dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String dept = dataSnapshot.child("dept").getValue().toString();
                        Intent intent = new Intent(SplashScreenActivity.this,homePage.class);
                        intent.putExtra("dept",dept);
                        startActivity(intent);
                        /*Intent intent = new Intent(SplashScreenActivity.this, homePage.class);
                        startActivity(intent);*/
                        SplashScreenActivity.this.finish();
                    }
                    else{
                        Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        SplashScreenActivity.this.finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    new AlertDialog.Builder(SplashScreenActivity.this)
                            .setTitle("Oops!")
                            .setMessage("Something went wrong!")
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                }
            });
        }
        else{
            Intent intent = new Intent(SplashScreenActivity.this, MobileActivity.class);
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }
    }


}
