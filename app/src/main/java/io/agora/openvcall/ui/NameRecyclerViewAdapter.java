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

public class NameRecyclerViewAdapter extends RecyclerView.Adapter<NameRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private String mflag,mdept;
    private Context mContext,context;


    public NameRecyclerViewAdapter(Context context, ArrayList<String> names, String flag,String dept) {

        mNames = names;
        mContext = context;
        mflag = flag;
        mdept = dept;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.name.setText(mNames.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mflag=="c") {
                    Intent intent = new Intent(context,ChatRoom.class);
                    intent.putExtra("Course",mNames.get(position));
                    context.startActivity(intent);
                }else if(mflag=="a") {
                    if(mdept.equals("Tutor")) {
                        Intent intent = new Intent(context, TeacherAssignmentList.class);
                        intent.putExtra("Course", mNames.get(position));
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, StudentAssignmentList.class);
                        intent.putExtra("Course", mNames.get(position));
                        context.startActivity(intent);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;


        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.nameTV);

        }
    }
}
