package io.agora.openvcall.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.agora.openvcall.R;

public class DeleteRecyclerViewAdapter extends RecyclerView.Adapter<DeleteRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> muid = new ArrayList<>();
    private Context mContext,context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference dbRegistered;

    public DeleteRecyclerViewAdapter(Context context, ArrayList<String> uid) {
        muid = uid;
        mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(muid.get(position));
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        holder.name.setText("NAME: "+dataSnapshot.child("Name").getValue().toString());
                        holder.mob.setText("REG NO: "+dataSnapshot.child("RegNo").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    new AlertDialog.Builder(context)
                            .setTitle("Oops!")
                            .setMessage("Something went wrong!")
                            .setNegativeButton("Cancel", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dbRegistered = FirebaseDatabase.getInstance().getReference().child("BlockedStudents").child(muid.get(position));
                    dbRegistered.removeValue();

                    muid.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, muid.size());
                    Toast.makeText(context, "User Un Blocked successfully", Toast.LENGTH_SHORT).show();
                }
            });


    }

    @Override
    public int getItemCount() {
        return muid.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView mob;
        Button delete;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.nameTV);
            mob = itemView.findViewById(R.id.mobTV);
            delete = itemView.findViewById(R.id.deleteUser);
        }
    }
}
