package io.agora.openvcall.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import io.agora.openvcall.R;

public class ApprovedAppointRecyclerViewAdapter extends RecyclerView.Adapter<ApprovedAppointRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mSubject = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();
    private ArrayList<String> mChannelName = new ArrayList<>();
    private ArrayList<String> mChannelPwd = new ArrayList<>();
    private ArrayList<String> mid = new ArrayList<>();
    private Context mContext,context;
    String mtid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    ArrayList<String> status = new ArrayList<String>(Arrays.asList("Not started","Started","Completed"));

    public ApprovedAppointRecyclerViewAdapter(Context context, ArrayList<String> subject, ArrayList<String> date, ArrayList<String> time, ArrayList<String> channelName, ArrayList<String> channelPwd,String tid, ArrayList<String> id) {

        mSubject = subject;
        mDate=date;
        mTime=time;
        mtid=tid;
        mid=id;
        mChannelName=channelName;
        mChannelPwd=channelPwd;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_appointment_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


            holder.subTv.setText("Subject:"+mSubject.get(position));
            holder.ddTV.setText("Date:"+mDate.get(position));
            holder.timeTV.setText("Time:"+mTime.get(position));
            holder.chNameTV.setText("Channel Name:"+mChannelName.get(position));
            holder.chPwdTV.setText("Channel pwd:"+mChannelPwd.get(position));

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dbPending = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Appointment").child("Accepted").child(mtid).child(mid.get(position));
                    dbPending.removeValue();
                    mSubject.remove(position);
                    mDate.remove(position);
                    mTime.remove(position);
                    mChannelName.remove(position);
                    mChannelPwd.remove(position);
                    mid.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mSubject.size());
                    Toast.makeText(context, "Entry deleted successfully", Toast.LENGTH_SHORT).show();
                }
            });


    }

    @Override
    public int getItemCount() {
        return mSubject.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView subTv,ddTV,timeTV,chNameTV,chPwdTV;
        Button delete;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            subTv = itemView.findViewById(R.id.subject);
            ddTV = itemView.findViewById(R.id.date);
            timeTV = itemView.findViewById(R.id.time);
            chNameTV = itemView.findViewById(R.id.channelName);
            chPwdTV = itemView.findViewById(R.id.channelPwd);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
