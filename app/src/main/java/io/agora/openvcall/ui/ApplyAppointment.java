package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.agora.openvcall.R;

public class ApplyAppointment extends AppCompatActivity {

    private ArrayList<String> mSubject = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mChannelName = new ArrayList<>();
    private ArrayList<String> mChannelPwd = new ArrayList<>();
    private ArrayList<String> mid = new ArrayList<>();



    FloatingActionButton addTask;
    String tid;

    TextView noitem,heading;
    RecyclerView pendingRecyclerview;
    ProgressBar PendingProgressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbUser,dbAdmin,Existing,dbPending,dbAccepted,dbRejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_appointment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Appointment Section");

        noitem = findViewById(R.id.noPendingRequest);
        PendingProgressBar = findViewById(R.id.PendingProgressBar);
        pendingRecyclerview = findViewById(R.id.pendingRequest);
        addTask = findViewById(R.id.addTask);
        tid = getIntent().getStringExtra("TeacherID");
        dbPending = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("Accepted").child(tid);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ApplyAppointment.this,addAppointment.class);
                intent.putExtra("TeacherID",tid);
                startActivity(intent);
            }
        });

        dbPending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PendingProgressBar.setVisibility(View.VISIBLE);
                mSubject.clear();
                mDate.clear();
                mTime.clear();
                mid.clear();
                mChannelName.clear();
                mChannelPwd.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mSubject.add(snapshot.child("Subject").getValue().toString());
                        mDate.add(snapshot.child("Date").getValue().toString());
                        mTime.add(snapshot.child("Time").getValue().toString());
                        mChannelName.add(snapshot.child("ChannelName").getValue().toString());
                        mChannelPwd.add(snapshot.child("pwd").getValue().toString());
                        mid.add(snapshot.getKey());

                    }
                    initPendingRequest();
                }
                else{
                    PendingProgressBar.setVisibility(View.GONE);
                    pendingRecyclerview.setVisibility(View.GONE);
                    noitem.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                PendingProgressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(ApplyAppointment.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });


    }

    private void initPendingRequest() {
        PendingProgressBar.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.pendingRequest);
        recyclerView.setLayoutManager(layoutManager);
        ApprovedAppointRecyclerViewAdapter adapter = new ApprovedAppointRecyclerViewAdapter(this,mSubject,mDate,mTime,mChannelName,mChannelPwd,tid,mid);
        recyclerView.setAdapter(adapter);
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
