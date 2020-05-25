package io.agora.openvcall.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;

import io.agora.openvcall.R;
import io.agora.openvcall.model.User;

public class ChatRoom extends AppCompatActivity {

    EditText msg;
    TextView title;
    ImageButton send;
    ProgressBar chatPB;
    RecyclerView chatRV;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbChat;
    String course;
    String flag = "t";
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mChat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Chat Room");

        chatRV = findViewById(R.id.chatRV);
        chatPB = findViewById(R.id.chatPB);
        send = findViewById(R.id.sendBtn);
        msg = findViewById(R.id.message);
        title = findViewById(R.id.textView2);


        course = getIntent().getStringExtra("Course");
        dbChat = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(course);
        title.setText(course+" Chat Room");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg.getText().toString().length() < 1) {
                    msg.setError("type your message here");
                    msg.requestFocus();
                    return;
                }
                else{


                    DatabaseReference profile = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    profile.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name;
                            String chatmsg;
                            name = dataSnapshot.child("Name").getValue().toString();
                            chatmsg = msg.getText().toString();

                            DatabaseReference dbch = FirebaseDatabase.getInstance().getReference().child("ChatRoom").child(course);
                            DatabaseReference pushedPostRef = dbch.push();
                            String paymentUniqueID = pushedPostRef.getKey();
                            flag = "f";
                            pushedPostRef.child("Name").setValue(name);
                            pushedPostRef.child("chat").setValue(chatmsg);
                            flag = "t";
                            /*mName.add(name);
                            mChat.add(chatmsg);
                            initPointRequest();*/
                            msg.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            new AlertDialog.Builder(ChatRoom.this)
                                    .setTitle("Oops!")
                                    .setMessage("Something went wrong!")
                                    .setNegativeButton("Cancel", null)
                                    .setIcon(R.drawable.ic_alert)
                                    .show();
                        }
                    });
                }
            }
        });

        dbChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Toast.makeText(ChatRoom.this, flag, Toast.LENGTH_SHORT).show();
                if(flag.equals("t")) {
                    if (dataSnapshot.exists()) {
                        mName.clear();
                        mChat.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child("chat").exists()) {
                                mName.add(snapshot.child("Name").getValue().toString());
                                mChat.add(snapshot.child("chat").getValue().toString());
                            }
                        }
                        initPointRequest();
                    } else {
                        chatPB.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                chatPB.setVisibility(View.GONE);
                new AlertDialog.Builder(ChatRoom.this)
                        .setTitle("Oops!")
                        .setMessage("Something went wrong!")
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });

    }

    private void initPointRequest() {

        chatPB.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.chatRV);
        recyclerView.setLayoutManager(layoutManager);
        ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(this, mName, mChat,"a");
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
