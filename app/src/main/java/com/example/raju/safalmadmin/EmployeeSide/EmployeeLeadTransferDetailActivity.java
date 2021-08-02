package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class EmployeeLeadTransferDetailActivity extends AppCompatActivity {

    LinearLayout icTransfer;
    SafalmAdmin se;
    private String TAG = EmployeeLeadTransferDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;
    String id, name, address, contact, lead_id, email, compname, date_time, date, time;
    String new_emp_id,emp_id,emp_name,emp_address,emp_mobile,emp_email;

    TextView txtSLDname, txtSLDaddress, txtSLDmobile, txtSLDemail, txtSLDcompanyname;

    AsyncTask at;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    TextView txtEmpname,txtEmpaddress,txtEmpmobile,txtEmpemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_lead_transfer_detail_activity);

        txtEmpname=findViewById(R.id.txtEmpname);
        txtEmpaddress=findViewById(R.id.txtEmpaddress);
        txtEmpemail=findViewById(R.id.txtEmpemail);
        txtEmpmobile=findViewById(R.id.txtEmpmobile);

        icTransfer=findViewById(R.id.icTransfer);

        txtSLDname = findViewById(R.id.txtSLDname);
        txtSLDaddress = findViewById(R.id.txtSLDaddress);
        txtSLDmobile = findViewById(R.id.txtSLDmobile);
        txtSLDemail = findViewById(R.id.txtSLDemail);
        txtSLDcompanyname = findViewById(R.id.txtSLDcompanyname);

        se = (SafalmAdmin) getApplicationContext();
        emp_id = se.getempId();


        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            new_emp_id = (String) b.get("emp_id");
            emp_name = (String) b.get("emp_name");
            emp_address = (String) b.get("emp_address");
            emp_mobile = (String) b.get("emp_mobile");
            emp_email = (String) b.get("emp_email");
            //txtSLDname.setText(lead_id);
        }

        txtEmpname.setText(emp_name);
        txtEmpmobile.setText(emp_mobile);
        txtEmpemail.setText(emp_email);
        txtEmpaddress.setText(emp_address);

        url = "http://10.0.2.2/safalm_admin/employee_self_lead_detail.php?id=" + lead_id;
        at = new EmployeeLeadTransferDetailActivity.Employee().execute();

        icTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://10.0.2.2/safalm_admin/employee_lead_transfer.php?lead_id=" + lead_id+"&new_emp_id="+new_emp_id;
                at = new EmployeeLeadTransferDetailActivity.Employee().execute();
                startActivity(new Intent(EmployeeLeadTransferDetailActivity.this,EmployeeDashboardActivity.class));
                finish();

            }
        });


    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeLeadTransferDetailActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Toast.makeText(SelfLeadDetailActivity.this, "date=" + date_time, Toast.LENGTH_SHORT).show();

            StringTokenizer tokens = new StringTokenizer(date_time, " ");
            date = tokens.nextToken();// this will contain "Fruit"
            time = tokens.nextToken();


            txtSLDname.setText(name);
            txtSLDaddress.setText(address);
            txtSLDmobile.setText(contact);
            txtSLDemail.setText(email);
            txtSLDcompanyname.setText(compname);


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
                    Log.e("SAFALM2=", self_lead.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    // for (int i = 0; i < self_lead.length(); i++) {
                    JSONObject c = self_lead.getJSONObject(0);

                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    // Log.e("SAFALM3=", success);
                    //Log.e("SAFALM4=", message);
                    //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
                    id = c.getString(TAG_SLID);
                    name = c.getString(TAG_SLNAME);
                    address = c.getString(TAG_SLADDRESS);
                    contact = c.getString(TAG_SLCONTACT);
                    email = c.getString("email");
                    compname = c.getString("cname");
                    date_time = c.getString("date");

                    // HashMap<String, String> map = new HashMap<>();

//                        map.put(TAG_SLID, id);
//                        map.put(TAG_SLNAME, name);
//                        map.put(TAG_SLADDRESS, address);
//                        map.put(TAG_SLCONTACT, contact);
//
//                        leadList.add(map);

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
