package io.agora.openvcall.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.agora.openvcall.R;

public class homePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

/*    private Button wallet;
    private Button referStudent;
    private Button referralStatus;
    private Button paymentStatus;*/
    LinearLayout videoClass,chatRoom,assignment,appointment,quiz;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbUser;
    String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        videoClass = findViewById(R.id.classroom);
        chatRoom = findViewById(R.id.chatRoom);
        assignment = findViewById(R.id.assignment);
        appointment = findViewById(R.id.appointment);
        quiz = findViewById(R.id.quiz);

        dept = getIntent().getStringExtra("dept");

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this,TestActivity.class);
                intent.putExtra("dept",dept);
                startActivity(intent);
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dept.equals("Tutor")) {
                    startActivity(new Intent(homePage.this, teacherAppointActivity.class));
                }else{
                    startActivity(new Intent(homePage.this, AppointmentTeachersList.class));
                }
            }
        });


        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(homePage.this,AssignmentCourseList.class));
                Intent intent = new Intent(homePage.this,AssignmentCourseList.class);
                intent.putExtra("dept",dept);
                startActivity(intent);
            }
        });

        chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homePage.this,ChooseCourseChat.class));
            }
        });

        videoClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(homePage.this,MainActivity.class));
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
/*        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){

                    new AlertDialog.Builder(homePage.this)
                            .setTitle("WELCOME!")
                            .setMessage("Please fill up the details On first login")
                            .setNegativeButton("Cancel", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    startActivity(new Intent(homePage.this,UpdateProfile.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(homePage.this,MobileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_update_profile) {
/*            Intent intent = new Intent(homePage.this,UpdateProfile.class);
            startActivity(intent);*/
        if(dept.equals("Tutor")){
            Intent intent = new Intent(homePage.this,BlockedStudents.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(homePage.this)
                    .setTitle("OOPS!")
                    .setMessage("Only Admin can Un Block Students. This is Admin only functionality.")
                    .setNegativeButton("OK", null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
        }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
