package com.example.alp.softwareproject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ArrayAdapter;

import com.example.alp.softwareproject.ProjectContract.*;

import static android.graphics.drawable.ClipDrawable.HORIZONTAL;
import static android.graphics.drawable.ClipDrawable.VERTICAL;


/**
 * Activity for reading data from an NDEF Tag.
 *
 * @author Ralf Wondratschek
 *
 */
public class MainActivity extends Activity{

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    String TeacherName;

    private Spinner spinner;

    String[] Attendance = new String[60];
    int AttendanceCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.SpinnerLecture);

        Bundle extras = getIntent().getExtras();
        TeacherName = extras.getString("TeacherName");

        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */
        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */
        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */

        RecyclerViewDBHelper dbHelper = new RecyclerViewDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();

        RecyclerView recyclerView = findViewById(R.id.RecViewAttendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        mAdapter = new ProjectAdapter(this,getAllItems(), ProjectEntry.COLUMN_DEMO_STUDENT_INFO, ProjectEntry.COLUMN_DEMO_ID , ProjectEntry.COLUMN_DEMO_DATE);
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

        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */
        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */
        /** ATTENDANCE RECYCLER VIEW ŞEYSİ */

        mTextView = findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());

        Button FinishAttendance = findViewById(R.id.btnFinishAttendance);
        FinishAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnMainMenu();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /*
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1
             *
             * http://www.nfc-forum.org/specs/
             *
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                String Lecture = String.valueOf(spinner.getSelectedItem());

                /** AddItem fonksiyonu */
                /** AddItem fonksiyonu */

                ContentValues cv = new ContentValues();
                cv.put(ProjectContract.ProjectEntry.COLUMN_DEMO_STUDENT_INFO, result);
                cv.put(ProjectContract.ProjectEntry.COLUMN_DEMO_LECTURE_CODE, Lecture);

                mDatabase.insert(ProjectContract.ProjectEntry.TABLE_NAME,null,cv);
                mAdapter.swapCursor(getAllItems());

                /** AddItem fonksiyonu */
                /** AddItem fonksiyonu */

                Attendance[AttendanceCounter] = result;
                AttendanceCounter++;
                int i;
                for(i=0 ; i<AttendanceCounter ; i++){
                    if(Attendance[i]==null)
                        break;
                }
            }
        }
    }

    private ProjectAdapter mAdapter;

    private SQLiteDatabase mDatabase;

    private void removeItem(long id){
        mDatabase.delete(ProjectContract.ProjectEntry.TABLE_NAME,
                ProjectContract.ProjectEntry.COLUMN_DEMO_ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());
    }

    private Cursor getAllItems(){
        return mDatabase.query(
                ProjectContract.ProjectEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProjectContract.ProjectEntry.COLUMN_DEMO_DATE + " DESC"
        );
    }

    private void returnMainMenu(){
        Log.e("SELAMLARSELAMLARSELAMLAR" , ProjectEntry2.COLUMN_ATTENDANCE_ID );

        mDatabase.execSQL("INSERT INTO " + ProjectContract.ProjectEntry2.TABLE_NAME_2 + "(" +
                ProjectContract.ProjectEntry2.COLUMN_ATTENDANCE_ID + "," +
                ProjectContract.ProjectEntry2.COLUMN_ATT_STUDENT_INFO + "," +
                ProjectContract.ProjectEntry2.COLUMN_DATE + "," +
                ProjectContract.ProjectEntry2.COLUMN_ATT_LECTURE_CODE + ") SELECT "+
                ProjectContract.ProjectEntry.COLUMN_DEMO_ID + ", " +
                ProjectContract.ProjectEntry.COLUMN_DEMO_STUDENT_INFO + ", " +
                "datetime(" + ProjectEntry.COLUMN_DEMO_DATE + ", 'localtime'),  " +
                ProjectContract.ProjectEntry.COLUMN_DEMO_LECTURE_CODE + " FROM " +
                ProjectContract.ProjectEntry.TABLE_NAME + ";"
        );



        mDatabase.execSQL("DELETE FROM " + ProjectContract.ProjectEntry.TABLE_NAME);

        Intent intent = new Intent(this,MainMenu.class);
        intent.putExtra("TeacherName2",TeacherName);
        startActivity(intent);
    }
}