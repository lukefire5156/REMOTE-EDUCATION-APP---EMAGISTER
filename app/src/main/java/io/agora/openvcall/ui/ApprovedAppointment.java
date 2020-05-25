package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.agora.openvcall.R;

public class ApprovedAppointment extends AppCompatActivity {

    TextView name,reg,subject,urgency,date,time,channelName,channelPwd;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    String AssignID,stuID,Name,Reg,Sub,Urg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_appointment);

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

        AssignID = getIntent().getStringExtra("AssignID");

        db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("accepted").child(AssignID);
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

                    date.setText(dataSnapshot.child("Date").getValue().toString());
                    time.setText(dataSnapshot.child("Time").getValue().toString());
                    channelName.setText(dataSnapshot.child("ChannelName").getValue().toString());
                    channelPwd.setText(dataSnapshot.child("pwd").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                new AlertDialog.Builder(ApprovedAppointment.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ApprovedAppointment.this,teacherAppointActivity.class);
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
