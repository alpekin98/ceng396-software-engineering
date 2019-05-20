package com.example.alp.softwareproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import com.example.alp.softwareproject.ProjectContract.*;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;

public class AttendanceList extends AppCompatActivity {

    private Spinner spinner;
    private SQLiteDatabase mDatabase;
    private ProjectAdapter mAdapter;
    private Button btnRefresh;

    private String spinnerItem;
    private String Filter[] = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        spinner = findViewById(R.id.spinnerAttList);
        btnRefresh = findViewById(R.id.btnRefresh);

        spinnerItem = String.valueOf(spinner.getSelectedItem());

        Filter[0] = spinnerItem;

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerItem = String.valueOf(spinner.getSelectedItem());
                Filter[0] = spinnerItem;
                getAllItems(Filter);
                mAdapter.swapCursor(getAllItems(Filter));
            }
        });

        RecyclerViewDBHelper dbHelper = new RecyclerViewDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();

        RecyclerView recyclerView = findViewById(R.id.RecViewAttendanceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        mAdapter = new ProjectAdapter(this,getAllItems(Filter), ProjectEntry2.COLUMN_ATT_STUDENT_INFO, ProjectEntry2.COLUMN_ATTENDANCE_ID , ProjectEntry2.COLUMN_DATE);
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long)viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void removeItem(long id){
        mDatabase.delete(ProjectContract.ProjectEntry2.TABLE_NAME_2,
                ProjectContract.ProjectEntry2.COLUMN_ATTENDANCE_ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems(Filter));
    }

    private Cursor getAllItems(String[] Filter){

        return mDatabase.rawQuery("SELECT * FROM " + ProjectEntry2.TABLE_NAME_2 + " WHERE " + ProjectEntry2.COLUMN_ATT_LECTURE_CODE + "=?",Filter);

    }
}
