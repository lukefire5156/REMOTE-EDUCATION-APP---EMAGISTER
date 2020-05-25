package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.openvcall.R;

public class AcceptAppointment extends AppCompatActivity {

    TextView name,reg,subject,urgency;
    EditText date,time,channelName,channelPwd;
    Button approve;
    ProgressBar progressBar;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    String AssignID,stuID,Name,Reg,Sub,Urg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_appointment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Appointment Section");

        name = findViewById(R.id.stuname);
        reg = findViewById(R.id.regNo);
        subject = findViewById(R.id.subject);
        urgency = findViewById(R.id.urgency);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        channelName = findViewById(R.id.channelName);
        channelPwd = findViewById(R.id.channelPwd);

        approve = findViewById(R.id.approveBtn);
        progressBar = findViewById(R.id.approvePB);

        AssignID = getIntent().getStringExtra("AssignID");

        db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("pending").child(AssignID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Name = dataSnapshot.child("Name").getValue().toString();
                    Reg = dataSnapshot.child("RegNo").getValue().toString();
                    Sub = dataSnapshot.child("Subject").getValue().toString();
                    Urg = dataSnapshot.child("Urgency").getValue().toString();

                    name.setText(Name);
                    reg.setText(Reg);
                    subject.setText(Sub);
                    urgency.setText(Urg);
                    stuID = dataSnapshot.child("StuID").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new AlertDialog.Builder(AcceptAppointment.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });

    approve.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(date.getText().toString().length() < 1) {
                date.setError("Date is required");
                date.requestFocus();
                return;
            }else if(time.getText().toString().length() < 1) {
                time.setError("time Name is required");
                time.requestFocus();
                return;
            }else if(channelName.getText().toString().length() < 1) {
                channelName.setError("Channel Name is required");
                channelName.requestFocus();
                return;
            }else if(channelPwd.getText().toString().length() < 1) {
                channelPwd.setError("Channel pwd is required");
                channelPwd.requestFocus();
                return;
            }else{
                progressBar.setVisibility(View.VISIBLE);

            DatabaseReference dbAccepted = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("accepted").child(AssignID);
            DatabaseReference dbStu = FirebaseDatabase.getInstance().getReference().child("Users").child(stuID).child("Appointment").child("Accepted").child(user.getUid()).child(AssignID);

                dbAccepted.child("Name").setValue(Name);
                dbAccepted.child("RegNo").setValue(Reg);
                dbAccepted.child("Subject").setValue(Sub);
                dbAccepted.child("Urgency").setValue(Urg);

                dbAccepted.child("Date").setValue(date.getText().toString());
                dbAccepted.child("Time").setValue(time.getText().toString());
                dbAccepted.child("ChannelName").setValue(channelName.getText().toString());
                dbAccepted.child("pwd").setValue(channelPwd.getText().toString());

                dbStu.child("Name").setValue(Name);
                dbStu.child("RegNo").setValue(Reg);
                dbStu.child("Subject").setValue(Sub);
                dbStu.child("Urgency").setValue(Urg);

                dbStu.child("Date").setValue(date.getText().toString());
                dbStu.child("Time").setValue(time.getText().toString());
                dbStu.child("ChannelName").setValue(channelName.getText().toString());
                dbStu.child("pwd").setValue(channelPwd.getText().toString());

                progressBar.setVisibility(View.GONE);
                db.removeValue();
                Intent intent = new Intent(AcceptAppointment.this,teacherAppointActivity.class);
                //intent.putExtra("AssignID",mPayID.get(position));
                startActivity(intent);
                Toast.makeText(AcceptAppointment.this, "Appointment Approved!", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AcceptAppointment.this,teacherAppointActivity.class);
        //intent.putExtra("AssignID",mPayID.get(position));
        startActivity(intent);
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
