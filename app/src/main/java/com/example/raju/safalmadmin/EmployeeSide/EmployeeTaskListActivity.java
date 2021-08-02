package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class EmployeeTaskListActivity extends AppCompatActivity {

    ListView list;

    String empid;
    ListAdapter adptr;

    AsyncTask at;
    int flag = 0;
    SafalmAdmin sa;
    private String TAG = EmployeeTaskListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_SLID = "task_id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_task_list_activity);

        list = findViewById(R.id.list_task);

        sa = (SafalmAdmin) getApplicationContext();
        empid = sa.getempId();

        leadList = new ArrayList<>();

        url = "http://10.0.2.2/safalm_admin/employee_task_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new EmployeeTaskListActivity.Employee().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String task_id = (String) obj.get("task_id");
                String lead_typ = (String) obj.get("lead_type");
                String t_typ = (String) obj.get("task_type");
               // Toast.makeText(EmployeeTaskListActivity.this, "task:"+t_typ, Toast.LENGTH_SHORT).show();

                //Toast.makeText(TaskListActivity.this, "Id=" + task_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EmployeeTaskListActivity.this, EmployeeTaskDetailActivity.class);
                i.putExtra("task_id", task_id);
                i.putExtra("lead_type", lead_typ);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.list_task);
            url = "http://10.0.2.2/safalm_admin/employee_task_list.php?emp_id=" + empid;
            leadList.clear();
            list.setAdapter(null);

           at = new EmployeeTaskListActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeTaskListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);


            adptr = new SimpleAdapter(EmployeeTaskListActivity.this, leadList, R.layout.employee_task_list_item, new String[]{TAG_SLID, TAG_SLNAME, TAG_SLADDRESS, TAG_SLCONTACT, "lead_type", "date", "time", "task_type"}, new int[]{R.id.txtTLIid, R.id.txtTLIname, R.id.txtTLIaddress, R.id.txtTLImobile, R.id.txtTLIleadtype, R.id.txtTLIdate, R.id.txtTLItime, R.id.txtTLItasktype}) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    TextView tasktype = v.findViewById(R.id.txtTLItasktype);
                    ImageView img = v.findViewById(R.id.imgTLIcall);

                    if (tasktype.getText().toString().equals("Call")) {
                        img.setImageResource(R.drawable.call);
                    }
                    if (tasktype.getText().toString().equals("SMS")) {
                        img.setImageResource(R.drawable.sms);
                    }

                    if (tasktype.getText().toString().equals("Mail")) {
                        img.setImageResource(R.drawable.mail);
                    }

                    if (tasktype.getText().toString().equals("WhatsApp")) {
                        img.setImageResource(R.drawable.whatsapp);
                    }
                    return v;
                }
            };
            list.setAdapter(adptr);
            pDialog.dismiss();
            if (flag == 0) {
                flag++;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.e("hardik", url);
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e("SAFALM1=", jsonObj.toString());

                    // Getting JSON Array node
                    JSONArray self_lead = jsonObj.getJSONArray("message");
//                    Log.e("SAFALM2=", contacts.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < self_lead.length(); i++) {
                        JSONObject c = self_lead.getJSONObject(i);

                        String success = jsonObj.getString("success");
                        String message = jsonObj.getString("message");
                        // Log.e("SAFALM3=", success);
                        //Log.e("SAFALM4=", message);
                        //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
                        String id = c.getString(TAG_SLID);
                        String name = c.getString(TAG_SLNAME);
                        String address = c.getString(TAG_SLADDRESS);
                        String contact = c.getString(TAG_SLCONTACT);
                        String lead_type = c.getString("lead_type");
                        String datetime = c.getString("task_date");
                        String tasktype = c.getString("task_type");

                        HashMap<String, String> map = new HashMap<>();

                        StringTokenizer tokens = new StringTokenizer(datetime, " ");
                        String date = tokens.nextToken();// this will contain "Fruit"
                        String time = tokens.nextToken();

                        map.put(TAG_SLID, id);
                        map.put(TAG_SLNAME, name);
                        map.put(TAG_SLADDRESS, address);
                        map.put(TAG_SLCONTACT, contact);
                        map.put("lead_type", lead_type);
                        map.put("date", date);
                        map.put("time", time);
                        map.put("task_type", tasktype);

                        leadList.add(map);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }
    }
}

