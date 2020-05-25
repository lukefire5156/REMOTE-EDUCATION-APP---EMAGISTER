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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.agora.openvcall.R;

public class addAssignment extends AppCompatActivity {

    String course;
    EditText dueDate,assignName;
    ImageView uploadQuestion;
    Button uploadBtn;

    TextView notification;
    Uri pdfUri;
    ProgressDialog progressDialog;

    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference db;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Assignment Section");

        date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        course = getIntent().getStringExtra("Course");

        dueDate = findViewById(R.id.dueDate);
        assignName = findViewById(R.id.assignName);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadQuestion = findViewById(R.id.uploadImageView);
        notification = findViewById(R.id.fileLocation);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        uploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(addAssignment.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else{
                    ActivityCompat.requestPermissions(addAssignment.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

                }

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(assignName.getText().toString().length() < 1) {
                    assignName.setError("Assignment Name is required");
                    assignName.requestFocus();
                    return;
                }else if(dueDate.getText().toString().length() < 1) {
                    dueDate.setError("Due Date is required");
                    dueDate.requestFocus();
                    return;
                }else if(pdfUri==null){
                    new AlertDialog.Builder(addAssignment.this)
                            .setTitle("Oops!")
                            .setMessage("Assignment Question FIle is required")
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                }else{
                    db = FirebaseDatabase.getInstance().getReference().child("Assignment").child(course);
                    DatabaseReference pushedPostRef = db.push();
                    String paymentUniqueID = pushedPostRef.getKey();

                    progressDialog = new ProgressDialog(addAssignment.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Uploading Question...");
                    progressDialog.setProgress(0);
                    progressDialog.show();

                    String fileName = "Question";
                    StorageReference storageReference = storage.getReference();

                    storageReference.child("Assignment").child(course).child(paymentUniqueID).child(fileName).putFile(pdfUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.child("Assignment").child(course).child(paymentUniqueID).child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pushedPostRef.child("AssignName").setValue(assignName.getText().toString());
                                            pushedPostRef.child("IssueDate").setValue(date);
                                            pushedPostRef.child("DueDate").setValue(dueDate.getText().toString());
                                            pushedPostRef.child("QuestionURL").setValue(uri.toString());
                                        }
                                    });
                                    progressDialog.dismiss();
                                    new AlertDialog.Builder(addAssignment.this)
                                            .setTitle("UPLOADED SUCCESSFULLY!")
                                            .setMessage("Assignment question file uploaded successfully!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(addAssignment.this,TeacherAssignmentList.class).putExtra("Course",course));
                                                    finish();
                                                }
                                            })
                                            .show();
                                    notification.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addAssignment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(addAssignment.this, "Please provide permission..", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(addAssignment.this, "Please select the file", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(addAssignment.this,TeacherAssignmentList.class);
        intent.putExtra("Course",course);
        startActivity(intent);
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

