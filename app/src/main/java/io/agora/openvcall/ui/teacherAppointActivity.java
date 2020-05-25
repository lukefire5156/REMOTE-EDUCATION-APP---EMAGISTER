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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.agora.openvcall.R;

public class teacherAppointActivity extends AppCompatActivity {
    private ArrayList<String> pAmount = new ArrayList<>();
    private ArrayList<String> pCoins = new ArrayList<>();
    private ArrayList<String> pUID = new ArrayList<>();
    private ArrayList<String> pPayID = new ArrayList<>();

    private ArrayList<String> aAmount = new ArrayList<>();
    private ArrayList<String> aCoins = new ArrayList<>();
    private ArrayList<String> aUID = new ArrayList<>();
    private ArrayList<String> aPayID = new ArrayList<>();

    TextView noitem;
    RecyclerView pendingRecyclerview;
    ProgressBar PendingProgressBar;

    TextView noAcceptedItem;
    RecyclerView acceptedRecyclerview;
    ProgressBar AcceptedProgressBar;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbUser,dbPending,dbAccepted,dbRejected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_appoint);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Appointment Section");

        noitem = findViewById(R.id.noBonus);
        PendingProgressBar = findViewById(R.id.PendingProgressBar);
        pendingRecyclerview = findViewById(R.id.pendingRequest);

        noAcceptedItem = findViewById(R.id.noAcceptedRequest);
        AcceptedProgressBar = findViewById(R.id.AcceptedProgressBar);
        acceptedRecyclerview = findViewById(R.id.acceptedRequest);

        dbPending = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("pending");
        dbAccepted = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("accepted");

        dbPending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PendingProgressBar.setVisibility(View.VISIBLE);
                pAmount.clear();
                pCoins.clear();
                pUID.clear();
                pPayID.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pAmount.add(snapshot.child("RegNo").getValue().toString());
                        pCoins.add(snapshot.child("Subject").getValue().toString());
                        pUID.add(snapshot.child("Urgency").getValue().toString());
                        pPayID.add(snapshot.getKey());

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
                new AlertDialog.Builder(teacherAppointActivity.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });

        dbAccepted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AcceptedProgressBar.setVisibility(View.VISIBLE);
                aAmount.clear();
                aCoins.clear();
                aUID.clear();
                aPayID.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        aAmount.add(snapshot.child("RegNo").getValue().toString());
                        aCoins.add(snapshot.child("Subject").getValue().toString());
                        aUID.add(snapshot.child("Date").getValue().toString());
                        aPayID.add(snapshot.getKey());
                    }
                    initAcceptedRequest();
                }
                else{
                    AcceptedProgressBar.setVisibility(View.GONE);
                    acceptedRecyclerview.setVisibility(View.GONE);
                    noAcceptedItem.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AcceptedProgressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(teacherAppointActivity.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });



    }

    private void initAcceptedRequest() {
        AcceptedProgressBar.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.acceptedRequest);
        recyclerView.setLayoutManager(layoutManager);
        AppointmentRecyclerViewAdapter adapter = new AppointmentRecyclerViewAdapter(this, aAmount,aCoins,aUID,aPayID,"a");
        recyclerView.setAdapter(adapter);
    }

    private void initPendingRequest() {
        PendingProgressBar.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.pendingRequest);
        recyclerView.setLayoutManager(layoutManager);
        AppointmentRecyclerViewAdapter adapter = new AppointmentRecyclerViewAdapter(this, pAmount,pCoins,pUID,pPayID,"p");
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
