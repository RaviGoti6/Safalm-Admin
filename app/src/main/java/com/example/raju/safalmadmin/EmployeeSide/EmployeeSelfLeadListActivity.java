package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

public class EmployeeSelfLeadListActivity extends AppCompatActivity {


    LinearLayout imgAddSelfLead;

    String empid;
    ListAdapter adptr;

    ListView list;
    AsyncTask at;
    int flag = 0;
    SafalmAdmin sa;
    private String TAG = EmployeeSelfLeadListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_self_lead_list_activity);

        imgAddSelfLead = findViewById(R.id.imgAddSelfLead);


        list = findViewById(R.id.listSelfLeadList);
        sa = (SafalmAdmin) getApplicationContext();
        empid = sa.getempId();

        leadList = new ArrayList<>();

        imgAddSelfLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeSelfLeadListActivity.this, EmployeeAddSelfLeadActivity.class);
                startActivity(i);
                finish();

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String lead_id = (String) obj.get("id");

                //Toast.makeText(SelfLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EmployeeSelfLeadListActivity.this, EmployeeSelfLeadDetailActivity.class);
                i.putExtra("lead_id", lead_id);
                startActivity(i);
            }
        });

        url = "http://10.0.2.2/safalm_admin/employee_self_lead_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new Employee().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.listSelfLeadList);
            url = "http://10.0.2.2/safalm_admin/employee_self_lead_list.php?emp_id=" + empid;
            leadList.clear();
            list.setAdapter(null);

            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeSelfLeadListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);


            adptr = new SimpleAdapter(EmployeeSelfLeadListActivity.this, leadList, R.layout.self_lead_list_item, new String[]{TAG_SLID, TAG_SLNAME, TAG_SLADDRESS, TAG_SLCONTACT, "s_task", "s_visited", "s_sales"}, new int[]{R.id.txtSLLIid, R.id.txtSLLIname, R.id.txtSLLIaddress, R.id.txtSLLImobile, R.id.s_task, R.id.s_visited, R.id.s_sales}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    LinearLayout l_task = v.findViewById(R.id.icStatusTask);
                    LinearLayout l_visited = v.findViewById(R.id.icStatusVisit);
                    LinearLayout l_sales = v.findViewById(R.id.icStatusSales);
                    TextView t_task = v.findViewById(R.id.s_task);
                    TextView t_visited = v.findViewById(R.id.s_visited);
                    TextView t_sales = v.findViewById(R.id.s_sales);
                    // l_task.setBackgroundColor(Color.parseColor("#000000"));
                    if (t_task.getText().toString().equals("T")) {
                        l_task.setBackground(getResources().getDrawable(R.drawable.task_status));
                    } else {
                        l_task.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }
                    if (t_visited.getText().toString().equals("V")) {
                        l_visited.setBackground(getResources().getDrawable(R.drawable.visited_status));
                    } else {
                        l_visited.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }
                    if (t_sales.getText().toString().equals("S")) {
                        l_sales.setBackground(getResources().getDrawable(R.drawable.sales_status));
                    } else {
                        l_sales.setBackground(getResources().getDrawable(R.drawable.status_none));
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
                        String s_task = c.getString("lead_task_status");
                        String s_visit = c.getString("lead_visited_status");
                        String s_sales = c.getString("lead_sales_status");

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_SLID, id);
                        map.put(TAG_SLNAME, name);
                        map.put(TAG_SLADDRESS, address);
                        map.put(TAG_SLCONTACT, contact);
                        map.put("s_task", s_task);
                        map.put("s_visited", s_visit);
                        map.put("s_sales", s_sales);

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
