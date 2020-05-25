package io.agora.openvcall.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mtask = new ArrayList<>();
    private ArrayList<String> missueDate = new ArrayList<>();
    private ArrayList<String> mstatus = new ArrayList<>();
    private ArrayList<String> mdueDate = new ArrayList<>();
    private ArrayList<String> mtid = new ArrayList<>();
    private ArrayList<String> mempuid = new ArrayList<>();
    private Context mContext,context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    ArrayList<String> status = new ArrayList<String>(Arrays.asList("Not started","Started","Completed"));

    public TaskRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> Phonenos, ArrayList<String> Remarks, ArrayList<String> stats, ArrayList<String> uid, ArrayList<String> empuid) {
        mtask = names;
        missueDate = Phonenos;
        mdueDate = Remarks;
        mContext = context;
        mstatus = stats;
        mtid = uid;
        mempuid = empuid;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignment_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");


            holder.task.setText(mtask.get(position));
            holder.idate.setText("ISSUE DATE: "+missueDate.get(position));
            holder.ddate.setText("DUE DATE: "+mdueDate.get(position));

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mstatus.get(position)));
                    mContext.startActivity(intent);

                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReviewSubmissions.class);
                    intent.putExtra("Course", mtid.get(position));
                    intent.putExtra("AssignID", mempuid.get(position));
                    context.startActivity(intent);
                }
            });


    }

    @Override
    public int getItemCount() {
        return mtask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView task;
        TextView idate;
        TextView ddate;
        ImageView download;
        Button delete;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            task = itemView.findViewById(R.id.assignedTask);
            idate = itemView.findViewById(R.id.dateIssue);
            ddate = itemView.findViewById(R.id.DueDate);
            download = itemView.findViewById(R.id.downloadImg);
            delete = itemView.findViewById(R.id.deleteButton);
        }
    }

}
