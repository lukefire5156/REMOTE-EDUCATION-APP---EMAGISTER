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

public class BlockedStudents extends AppCompatActivity {

    private ArrayList<String> muid = new ArrayList<>();

    TextView noitem;
    RecyclerView pendingRecyclerview;
    ProgressBar PendingProgressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbUser,dbAdmin,Existing,dbPending,dbAccepted,dbRejected;
    String empUID;
    FloatingActionButton addTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_students);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("UN BLOCK USERS");

        //empUID = getIntent().getStringExtra("UID");


        noitem = findViewById(R.id.noPendingRequest);
        PendingProgressBar = findViewById(R.id.PendingProgressBar);
        pendingRecyclerview = findViewById(R.id.pendingRequest);
        dbPending = FirebaseDatabase.getInstance().getReference().child("BlockedStudents");

        dbPending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PendingProgressBar.setVisibility(View.VISIBLE);
                muid.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        muid.add(snapshot.getKey());

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
                new AlertDialog.Builder(BlockedStudents.this)
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
        DeleteRecyclerViewAdapter adapter = new DeleteRecyclerViewAdapter(this,muid);
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
