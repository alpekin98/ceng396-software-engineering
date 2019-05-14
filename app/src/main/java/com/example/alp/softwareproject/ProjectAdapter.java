package com.example.alp.softwareproject;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    private Cursor mCursor;
    private Context mContext;
    private String Stu_Info;
    private String Stu_ID;
    private String Stu_Date;

    public ProjectAdapter(Context context , Cursor cursor , String Info , String ID , String Date){
        mContext = context;
        mCursor = cursor;
        Stu_Info = Info;
        Stu_ID = ID;
        Stu_Date = Date;
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder{
        public TextView infoText;
        public TextView dateText;

        public ProjectViewHolder(@NonNull View itemView){
            super(itemView);
            infoText = itemView.findViewById(R.id.tvRecView);
            dateText = itemView.findViewById(R.id.tvRecViewDate);
        }
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.attendance_recycler_view, viewGroup , false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {
        if(!mCursor.moveToPosition(i)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(Stu_Info));
        long id = mCursor.getLong(mCursor.getColumnIndex(Stu_ID));
        String date = mCursor.getString(mCursor.getColumnIndex(Stu_Date));

        projectViewHolder.infoText.setText(name);
        projectViewHolder.itemView.setTag(id);
        projectViewHolder.dateText.setText(date);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if(newCursor != null)
            notifyDataSetChanged();
    }
}
