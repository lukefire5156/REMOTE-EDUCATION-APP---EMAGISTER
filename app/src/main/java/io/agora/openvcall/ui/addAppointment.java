package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import io.agora.openvcall.R;

public class addAppointment extends AppCompatActivity {

    EditText name,phone;
    Spinner department;
    ProgressBar progressBar;
    Button register;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRegister,dbUser;
    ArrayList<String> dept = new ArrayList<String>(Arrays.asList("Select an Option","HIGH","MEDIUM","LOW"));
    int dd;
    String tid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Appointment Section");

        name = findViewById(R.id.name);
        progressBar = findViewById(R.id.progressBar);
        department = findViewById(R.id.department);
        register = findViewById(R.id.register);
        tid = getIntent().getStringExtra("TeacherID");


        department.setAdapter(new ArrayAdapter<String>(addAppointment.this, android.R.layout.simple_spinner_dropdown_item, dept));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() < 1) {
                    name.setError("Subject is required");
                    name.requestFocus();
                    return;
                }
                else if(department.getSelectedItem().equals("Select an Option")) {
                    new AlertDialog.Builder(addAppointment.this)
                            .setTitle("Oops!")
                            .setMessage("Urgency Value is required")
                            .setNegativeButton("OK", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(tid).child("Appointment").child("pending");
                    DatabaseReference pushedPostRef = dbUser.push();
                    String paymentUniqueID = pushedPostRef.getKey();

                    DatabaseReference userdetails = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    userdetails.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                pushedPostRef.child("Name").setValue(dataSnapshot.child("Name").getValue().toString());
                                pushedPostRef.child("RegNo").setValue(dataSnapshot.child("RegNo").getValue().toString());
                                pushedPostRef.child("StuID").setValue(user.getUid());
                                pushedPostRef.child("Subject").setValue(name.getText().toString());
                                pushedPostRef.child("Urgency").setValue(department.getSelectedItem().toString());
                                name.setText("");
                                progressBar.setVisibility(View.GONE);
                                new AlertDialog.Builder(addAppointment.this)
                                        .setTitle("Congratulations!")
                                        .setMessage("Appointment placed successfully!")
                                        .setNegativeButton("OK", null)
                                        .show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(addAppointment.this)
                                    .setTitle("Oops!")
                                    .setMessage("Something went wrong!")
                                    .setNegativeButton("Cancel", null)
                                    .setIcon(R.drawable.ic_alert)
                                    .show();
                        }
                    });


                }

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // back button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
