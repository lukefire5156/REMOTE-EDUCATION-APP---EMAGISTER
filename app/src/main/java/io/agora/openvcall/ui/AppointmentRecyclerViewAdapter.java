package io.agora.openvcall.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mAmount = new ArrayList<>();
    private ArrayList<String> mCoins = new ArrayList<>();
    private ArrayList<String> mUID = new ArrayList<>();
    private ArrayList<String> mPayID = new ArrayList<>();
    private Context mContext,context;
    private String mflag;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;

    public AppointmentRecyclerViewAdapter(Context context, ArrayList<String> Amount, ArrayList<String> Coins, ArrayList<String> UID, ArrayList<String> PayID, String flag) {
        mAmount = Amount;
        mCoins = Coins;
        mUID = UID;
        mPayID = PayID;
        mContext = context;
        mflag = flag;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_appoint_area_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");




            holder.nameTV.setText("REG. NO.: "+mAmount.get(position));
            holder.amountTV.setText("SUBJECT: "+mCoins.get(position));

        if(mflag == "p"){
            holder.payTV.setText("URGENCY: "+mUID.get(position));
        }else if(mflag == "a"){
            holder.payTV.setText("MEETING DATE: "+mUID.get(position));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mflag=="p") {
                    Intent intent = new Intent(context,AcceptAppointment.class);
                    intent.putExtra("AssignID",mPayID.get(position));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
                else if(mflag=="a") {
                    Intent intent = new Intent(context,ApprovedAppointment.class);
                    intent.putExtra("AssignID",mPayID.get(position));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mAmount.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV;
        TextView amountTV;
        TextView payTV;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nameTV = itemView.findViewById(R.id.EmpName);
            amountTV = itemView.findViewById(R.id.Amount);
            payTV = itemView.findViewById(R.id.PaymentID);
        }
    }
}
