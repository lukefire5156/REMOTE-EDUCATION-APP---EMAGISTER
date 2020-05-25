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

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

import io.agora.openvcall.R;

public class SubmissionRecyclerViewAdapter extends RecyclerView.Adapter<SubmissionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mReg = new ArrayList<>();
    private ArrayList<String> mUrl = new ArrayList<>();
    private Context mContext,context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    ArrayList<String> status = new ArrayList<String>(Arrays.asList("Not started","Started","Completed"));

    public SubmissionRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> reg, ArrayList<String> url) {

        mContext = context;
        mName = names;
        mReg = reg;
        mUrl = url;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stud_assign_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


            holder.nameTV.setText("Name: "+mName.get(position));
            holder.regTV.setText("Reg. No: "+mReg.get(position));

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mUrl.get(position)));
                    mContext.startActivity(intent);

                }
            });



    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV;
        TextView regTV;
        ImageView download;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nameTV = itemView.findViewById(R.id.nameTV);
            regTV = itemView.findViewById(R.id.dueDD);
            download = itemView.findViewById(R.id.downloadImg);
        }
    }
}
