package com.crashreport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.crashreport.database.DBConstant;

import java.util.ArrayList;

public class CrashActivity extends AppCompatActivity {
    public static final String TAG = CrashActivity.class.getSimpleName();
    private Context context;
    private ListView list_crash;
    private CrashAdapter crashAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        context = CrashActivity.this;
        ((Button) findViewById(R.id.btn_crash)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = null;
                s.length();
            }
        });

        loadList();
        viewCrashes();
    }


    private void loadList() {
        list_crash = (ListView) findViewById(R.id.list_crash);
        crashAdapter = new CrashAdapter(context, new ArrayList<CrashDetails>());
        list_crash.setAdapter(crashAdapter);
    }


    private class CrashAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<CrashDetails> listReport;
        private LayoutInflater inflater;

        public CrashAdapter(Context context, ArrayList<CrashDetails> list) {
            mContext = context;
            inflater = LayoutInflater.from(mContext);
            listReport = list;
        }

        public void refresh(ArrayList<CrashDetails> list) {
            listReport.clear();
            listReport.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (listReport == null) ? 0 : listReport.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.inflate_listrow, null);
                viewHolder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
                viewHolder.txt_details = (TextView) convertView.findViewById(R.id.txt_details);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CrashDetails crashDetails = listReport.get(i);
            viewHolder.txt_date.setText(crashDetails.getDate());
            viewHolder.txt_details.setText(crashDetails.getCrashdetail());

            return convertView;
        }

        public class ViewHolder {
            TextView txt_date;
            TextView txt_details;
        }

    }


    private void viewCrashes() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            private ArrayList<CrashDetails> list = null;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Cursor cursor = context.getContentResolver().query(DBConstant.CRASHREPORT_CONTENT_URI, DBConstant.PROJECTTION, null, null, null);
                    if (cursor != null && cursor.getCount() >= 1) {
                        list = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            CrashDetails crashDetails = new CrashDetails();
                            crashDetails.setDate(cursor.getString(cursor.getColumnIndex(DBConstant.CRASHREPORT_FIELD_DATE)));
                            crashDetails.setCrashdetail(cursor.getString(cursor.getColumnIndex(DBConstant.CRASHREPORT_FIELD_DETAIL)));
                            list.add(crashDetails);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                crashAdapter.refresh((list == null) ? new ArrayList<CrashDetails>() : list);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
