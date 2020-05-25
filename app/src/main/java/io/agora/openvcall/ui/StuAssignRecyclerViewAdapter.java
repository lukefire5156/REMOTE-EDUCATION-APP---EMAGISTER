package io.agora.openvcall.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.agora.openvcall.R;

public class StuAssignRecyclerViewAdapter extends RecyclerView.Adapter<StuAssignRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDD = new ArrayList<>();
    private ArrayList<String> mAssignID = new ArrayList<>();
    private String mflag,mdept,Course;
    private Context mContext,context;


    public StuAssignRecyclerViewAdapter(Context context, ArrayList<String> names,ArrayList<String> dd,ArrayList<String> assignID, String course,String flag, String dept) {

        mNames = names;
        mContext = context;
        Course = course;
        mflag = flag;
        mDD = dd;
        mdept = dept;
        mAssignID = assignID;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_assignment_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(mNames.get(position));
        holder.duedd.setText("Due Date: "+mDD.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mflag=="s") {
                    Intent intent = new Intent(context,AssignmentSubmission.class);
                    intent.putExtra("Course",Course);
                    intent.putExtra("AssignID",mAssignID.get(position));
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,duedd;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.nameTV);
            duedd = itemView.findViewById(R.id.dueDD);

        }
    }
}
