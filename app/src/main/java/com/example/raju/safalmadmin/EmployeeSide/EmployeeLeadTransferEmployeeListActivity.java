package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.EmployeeListActivity;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeLeadTransferEmployeeListActivity extends AppCompatActivity {

    String empid,lead_id;
    ListAdapter adptr;

    // ListView list;
    AsyncTask at;
    int flag = 0;

    //   SafalmAdmin se;
    private String TAG = EmployeeListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_ELID = "id";
    private static final String TAG_ELNAME = "name";
    private static final String TAG_ELEMAIL = "email";
    private static final String TAG_ELADDRESS = "address";
    private static final String TAG_ELCONTACT = "mobile";

    ListView list_item;

    TextView txtELIid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list_activity);

        setTitle("Select Employee");


        list_item=findViewById(R.id.list_item);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            //txtSLDname.setText(lead_id);
        }

//        se = (SafalmAdmin) getApplicationContext();
//        empid = se.getempId();

        leadList = new ArrayList<>();

        url = "http://10.0.2.2/safalm/employee_list.php";
        // list.setAdapter(null);
        at = new EmployeeLeadTransferEmployeeListActivity.Employee().execute();

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) list_item.getAdapter().getItem(position);
                String emp_id = (String) obj.get(TAG_ELID);
                String emp_name = (String) obj.get(TAG_ELNAME);
                String emp_mobile = (String) obj.get(TAG_ELCONTACT);
                String emp_address = (String) obj.get(TAG_ELADDRESS);
                String emp_email = (String) obj.get(TAG_ELEMAIL);

                //Toast.makeText(SelfLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EmployeeLeadTransferEmployeeListActivity.this, EmployeeLeadTransferDetailActivity.class);
                i.putExtra("emp_id", emp_id);
                i.putExtra("lead_id", lead_id);
                i.putExtra("emp_name", emp_name);
                i.putExtra("emp_address", emp_address);
                i.putExtra("emp_mobile", emp_mobile);
                i.putExtra("emp_email", emp_email);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list_item = findViewById(R.id.list_item);
            url = "http://10.0.2.2/safalm/employee_list.php";
            leadList.clear();
            list_item.setAdapter(null);

            at = new EmployeeLeadTransferEmployeeListActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeLeadTransferEmployeeListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Log.e("SAFALM5=", contacts.get("success").toString() + contacts.get("message").toString());
            // Toast.makeText(getApplicationContext(), contacts.get("success").toString() + contacts.get("message").toString(), Toast.LENGTH_SHORT).show();
            //String empId=contacts.get("message").toString();
            //se.setempId(contacts.get("message").toString());
            //   String eid=se.getempId().toString();
            //    Log.e("Safalam 6:",eid);

            adptr = new SimpleAdapter(EmployeeLeadTransferEmployeeListActivity.this, leadList, R.layout.employee_list_item_activity, new String[]{TAG_ELID, TAG_ELNAME,TAG_ELEMAIL, TAG_ELADDRESS, TAG_ELCONTACT}, new int[]{R.id.txtELIid, R.id.txtELIname,R.id.txtELIemail, R.id.txtELIaddress,R.id.txtELImobile});
            list_item.setAdapter(adptr);


            //  if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            //       Toast.makeText(EmployeeSelfLeadListActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
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
                        String id = c.getString(TAG_ELID);
                        String name = c.getString(TAG_ELNAME);
                        String email = c.getString(TAG_ELEMAIL);
                        String address = c.getString(TAG_ELADDRESS);
                        String contact = c.getString(TAG_ELCONTACT);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_ELID, id);
                        map.put(TAG_ELNAME, name);
                        map.put(TAG_ELEMAIL, email);
                        map.put(TAG_ELADDRESS, address);
                        map.put(TAG_ELCONTACT, contact);

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
