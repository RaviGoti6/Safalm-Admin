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
import java.util.StringTokenizer;

public class EmployeeSelfVisitedLeadListActivity extends AppCompatActivity {

    ListView listVisitedLeadList;

    String empid,date;
    ListAdapter adptr;

    ListView list;
    AsyncTask at;
    int flag = 0;
    SafalmAdmin sa;
    private String TAG = EmployeeSelfVisitedLeadListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_VLID = "id";
    private static final String TAG_VLNAME = "name";
    private static final String TAG_VLADDRESS = "address";
    private static final String TAG_VLCONTACT = "mobile";
    private static final String TAG_VLPRODUCT = "product";
    private static final String TAG_VLDATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_self_visited_lead_list_activity);

        list=findViewById(R.id.listVisitedLeadList);

        sa = (SafalmAdmin) getApplicationContext();
        empid = sa.getempId();

        leadList = new ArrayList<>();


        url = "http://10.0.2.2/safalm_admin/employee_visited_lead_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new Employee().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String lead_id = (String) obj.get("id");

                //Toast.makeText(SelfLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EmployeeSelfVisitedLeadListActivity.this, EmployeeSelfVisitedLeadDetailActivity.class);
                i.putExtra("lead_id", lead_id);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.listVisitedLeadList);
            url = "http://10.0.2.2/safalm_admin/employee_visited_lead_list.php?emp_id=" + empid;
            leadList.clear();
            list.setAdapter(null);

            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeSelfVisitedLeadListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            adptr = new SimpleAdapter(EmployeeSelfVisitedLeadListActivity.this, leadList, R.layout.visited_lead_list_item, new String[]{TAG_VLID, TAG_VLNAME, TAG_VLADDRESS, TAG_VLCONTACT, TAG_VLDATE,"product","s_task","s_sales"}, new int[]{R.id.txtVLLIid, R.id.txtVLLIname, R.id.txtVLLIaddress, R.id.txtVLLImobile, R.id.txtVLLIdate,R.id.txtVLLIproduct,R.id.s_task,R.id.s_sales})
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    LinearLayout l_task=v.findViewById(R.id.icStatusTask);
                    LinearLayout l_sales=v.findViewById(R.id.icStatusSales);
                    TextView t_task=v.findViewById(R.id.s_task);
                    TextView t_sales=v.findViewById(R.id.s_sales);
                    // l_task.setBackgroundColor(Color.parseColor("#000000"));
                    if (t_task.getText().toString().equals("T")){
                        l_task.setBackground(getResources().getDrawable(R.drawable.task_status));
                    }else {
                        l_task.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }
                    if (t_sales.getText().toString().equals("S")){
                        l_sales.setBackground(getResources().getDrawable(R.drawable.sales_status));
                    }else {
                        l_sales.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }

                    return v;
                }
            };
            list.setAdapter(adptr);

            //  if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            //       Toast.makeText(SelfLeadListActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
            //   }

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
                        String id = c.getString(TAG_VLID);
                        String name = c.getString(TAG_VLNAME);
                        String address = c.getString(TAG_VLADDRESS);
                        String contact = c.getString(TAG_VLCONTACT);
                        date = c.getString(TAG_VLDATE);
                        String product = c.getString("product");
                        String s_task = c.getString("lead_task_status");
                        String s_sales = c.getString("lead_sales_status");

                        StringTokenizer tokens = new StringTokenizer(date, " ");
                        String d= tokens.nextToken();
                        // time = tokens.nextToken();

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_VLID, id);
                        map.put(TAG_VLNAME, name);
                        map.put(TAG_VLADDRESS, address);
                        map.put(TAG_VLCONTACT, contact);
                        map.put(TAG_VLDATE, d);
                        map.put("product", product);
                        map.put("s_task", s_task);
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
