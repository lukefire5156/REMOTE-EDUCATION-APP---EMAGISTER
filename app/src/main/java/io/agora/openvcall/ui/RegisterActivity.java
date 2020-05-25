package io.agora.openvcall.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.agora.openvcall.R;

public class RegisterActivity extends AppCompatActivity {

    EditText name,phone;
    Spinner department;
    ProgressBar progressBar;
    Button register;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;
    private StorageReference mStorageRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRegister,dbUser;
    ArrayList<String> dept = new ArrayList<String>(Arrays.asList("Select an Option","Tutor","Student"));
    int dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register Mobile");


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        progressBar = findViewById(R.id.progressBar);
        department = findViewById(R.id.department);
        register = findViewById(R.id.register);


        department.setAdapter(new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, dept));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        if(name.getText().toString().length() < 1) {
            name.setError("Student's Name is required");
            name.requestFocus();
            return;
        }
        else if(phone.getText().toString().length() < 1) {
            phone.setError("Student's Mobile number must be 10 digit long");
            phone.requestFocus();
            return;
        }
        else if(department.getSelectedItem().equals("Select an Option")) {
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Oops!")
                    .setMessage("Department is required")
                    .setNegativeButton("OK", null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

            dbUser.child("Name").setValue(name.getText().toString());
            dbUser.child("RegNo").setValue(phone.getText().toString());
            dbUser.child("dept").setValue(department.getSelectedItem().toString());
            String dep = department.getSelectedItem().toString();

            //String dept = dataSnapshot.child("dept").getValue().toString();
            Intent intent = new Intent(RegisterActivity.this,homePage.class);
            intent.putExtra("dept",dep);
            startActivity(intent);
            //startActivity(new Intent(RegisterActivity.this,homePage.class));

        }

            }
        });

    }

/*    @Override
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
    }*/


    @Override
    public void onBackPressed(){
        finishAffinity();
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
