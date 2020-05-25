package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.agora.openvcall.R;

public class AssignmentSubmission extends AppCompatActivity {

    TextView assignedTask,issueDD,dueDD;
    TextView notification;
    ImageView download,upload;
    LinearLayout linearLayout;
    ProgressBar assignPB;
    String course,assignID,downloadURL;
    Button submit;

    Uri pdfUri;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_submission);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Assignment Section");


        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        assignedTask = findViewById(R.id.assignedTask);
        issueDD = findViewById(R.id.dateIssue);
        submit = findViewById(R.id.submitBtn);
        dueDD = findViewById(R.id.DueDate);
        download = findViewById(R.id.downloadImg);
        upload = findViewById(R.id.uploadImg);
        linearLayout = findViewById(R.id.linearLayout);
        assignPB = findViewById(R.id.assignPB);
        notification = findViewById(R.id.notification);

        course = getIntent().getStringExtra("Course");
        assignID = getIntent().getStringExtra("AssignID");

        db = FirebaseDatabase.getInstance().getReference().child("Assignment").child(course).child(assignID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    assignedTask.setText(dataSnapshot.child("AssignName").getValue().toString());
                    issueDD.setText("Issue Date: "+dataSnapshot.child("IssueDate").getValue().toString());
                    dueDD.setText("Due Date: "+dataSnapshot.child("DueDate").getValue().toString());
                    downloadURL = dataSnapshot.child("QuestionURL").getValue().toString();
                    assignPB.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                assignPB.setVisibility(View.GONE);
                new AlertDialog.Builder(AssignmentSubmission.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(downloadURL));
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(AssignmentSubmission.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else{
                    ActivityCompat.requestPermissions(AssignmentSubmission.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(pdfUri==null){
                    new AlertDialog.Builder(AssignmentSubmission.this)
                            .setTitle("Oops!")
                            .setMessage("Submission FIle is required")
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                }else {

                    progressDialog = new ProgressDialog(AssignmentSubmission.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Uploading Question...");
                    progressDialog.setProgress(0);
                    progressDialog.show();

                    String fileName = user.getUid();
                    StorageReference storageReference = storage.getReference();

                    storageReference.child("Assignment").child(course).child(assignID).child("Submissions").child(fileName).putFile(pdfUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.child("Assignment").child(course).child(assignID).child("Submissions").child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //////////////
                                            DatabaseReference dbAssign = FirebaseDatabase.getInstance().getReference().child("Assignment").child(course).child(assignID).child("Submissions");
                                            DatabaseReference userdetails = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                                            userdetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        dbAssign.child(user.getUid()).child("Name").setValue(dataSnapshot.child("Name").getValue().toString());
                                                        dbAssign.child(user.getUid()).child("RegNo").setValue(dataSnapshot.child("RegNo").getValue().toString());
                                                        dbAssign.child(user.getUid()).child("Solution").setValue(uri.toString());

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    new AlertDialog.Builder(AssignmentSubmission.this)
                                                            .setTitle("Oops!")
                                                            .setMessage("Something went wrong!")
                                                            .setNegativeButton("Cancel", null)
                                                            .setIcon(R.drawable.ic_alert)
                                                            .show();
                                                }
                                            });
                                            //////////////
                                        }
                                    });
                                    progressDialog.dismiss();
                                    new AlertDialog.Builder(AssignmentSubmission.this)
                                            .setTitle("UPLOADED SUCCESSFULLY!")
                                            .setMessage("Assignment file uploaded successfully!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(AssignmentSubmission.this,StudentAssignmentList.class).putExtra("Course",course));
                                                    finish();
                                                }
                                            })
                                            .show();
                                    notification.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AssignmentSubmission.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(currentProgress);
                        }
                    });






                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else{
            Toast.makeText(AssignmentSubmission.this, "Please provide permission..", Toast.LENGTH_SHORT).show();

        }
    }

    private void selectPdf() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            notification.setText(data.getData().getLastPathSegment());
        } else {
            Toast.makeText(AssignmentSubmission.this, "Please select the file", Toast.LENGTH_SHORT).show();
        }

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
