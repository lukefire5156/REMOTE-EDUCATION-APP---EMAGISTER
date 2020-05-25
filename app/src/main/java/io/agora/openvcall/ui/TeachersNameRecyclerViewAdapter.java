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

public class TeachersNameRecyclerViewAdapter extends RecyclerView.Adapter<TeachersNameRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mEmpId = new ArrayList<>();
    private ArrayList<String> mTeacherUID = new ArrayList<>();
    private String mflag,mdept;
    private Context mContext,context;


    public TeachersNameRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> empid, ArrayList<String> TeacherUID, String flag) {

        mNames = names;
        mEmpId = empid;
        mTeacherUID = TeacherUID;
        mContext = context;
        mflag = flag;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.nameTV.setText("Name: "+ mNames.get(position));
        holder.empIDTV.setText("Emp. ID: "+mEmpId.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mflag=="t") {
                    Intent intent = new Intent(context,ApplyAppointment.class);
                    intent.putExtra("TeacherID",mTeacherUID.get(position));
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

        TextView nameTV;
        TextView empIDTV;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nameTV = itemView.findViewById(R.id.nameTV);
            empIDTV = itemView.findViewById(R.id.empID);

        }
    }


}
