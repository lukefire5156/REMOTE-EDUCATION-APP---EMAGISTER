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

public class TeacherAssignmentList extends AppCompatActivity {


    private ArrayList<String> mtask = new ArrayList<>();
    private ArrayList<String> missueDate = new ArrayList<>();
    private ArrayList<String> mstatus = new ArrayList<>();
    private ArrayList<String> mdueDate = new ArrayList<>();
    private ArrayList<String> mtid = new ArrayList<>();
    private ArrayList<String> muid = new ArrayList<>();

    TextView noitem,heading;
    RecyclerView pendingRecyclerview;
    ProgressBar PendingProgressBar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbUser,dbAdmin,Existing,dbPending,dbAccepted,dbRejected;

    FloatingActionButton addTask;
    String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignment_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Assignment Section");


        course = getIntent().getStringExtra("Course");
        heading = findViewById(R.id.PendingRequestTextView);
        heading.setText(course+" ASSIGNMENTS");


        noitem = findViewById(R.id.noPendingRequest);
        PendingProgressBar = findViewById(R.id.PendingProgressBar);
        pendingRecyclerview = findViewById(R.id.pendingRequest);
        dbPending = FirebaseDatabase.getInstance().getReference().child("Assignment").child(course);
        addTask = findViewById(R.id.addTask);


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TeacherAssignmentList.this,addAssignment.class);
                intent.putExtra("Course",course);
                startActivity(intent);
                finish();

            }
        });

        dbPending.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PendingProgressBar.setVisibility(View.VISIBLE);
                mtask.clear();
                missueDate.clear();
                mstatus.clear();
                mdueDate.clear();
                mtid.clear();
                muid.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mtask.add(snapshot.child("AssignName").getValue().toString());
                        missueDate.add(snapshot.child("IssueDate").getValue().toString());
                        mstatus.add(snapshot.child("QuestionURL").getValue().toString());
                        mdueDate.add(snapshot.child("DueDate").getValue().toString());
                        mtid.add(course);
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
                new AlertDialog.Builder(TeacherAssignmentList.this)
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
        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(this, mtask, missueDate,mdueDate,mstatus,mtid,muid);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(TeacherAssignmentList.this,AssignmentCourseList.class).putExtra("dept","Tutor"));
        finish();
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
