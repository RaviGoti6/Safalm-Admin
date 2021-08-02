package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.EditEmployeeDetailActivity;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeDetailActivity extends AppCompatActivity {

    TextView txtEDname, txtEDaddress, txtEDemail, txtEDmobile, txtEDdate, txtEDpassword;

    ImageView icEDedit, icEDdelete;

    String emp_id, emp_name, emp_address, emp_email, emp_mobile, emp_password, emp_date;

    String empid;
    ListAdapter adptr;

    ListView list;
    AsyncTask at;
    int flag = 0;
    SafalmAdmin sa;
    private String TAG = EmployeeDetailActivity.class.getSimpleName();

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
        setContentView(R.layout.employee_detail_activity);

        txtEDname = findViewById(R.id.txtEDname);
        txtEDaddress = findViewById(R.id.txtEDaddress);
        txtEDmobile = findViewById(R.id.txtEDmobile);
        txtEDdate = findViewById(R.id.txtEDdate);
        txtEDemail = findViewById(R.id.txtEDemail);
        txtEDpassword = findViewById(R.id.txtEDpassword);

        icEDedit = findViewById(R.id.icEDedit);
        icEDdelete = findViewById(R.id.icEDdelete);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            emp_id = (String) b.get("emp_id");
            emp_name = (String) b.get("emp_name");
            emp_address = (String) b.get("emp_address");
            emp_mobile = (String) b.get("emp_mobile");
            emp_email = (String) b.get("emp_email");
            emp_date = (String) b.get("emp_date");
            emp_password = (String) b.get("emp_password");
            //txtSLDname.setText(lead_id);
        }

        sa = (SafalmAdmin) getApplicationContext();
        emp_id = sa.getempId();
        //Toast.makeText(EmployeeDetailActivity.this, "id:" + emp_id, Toast.LENGTH_SHORT).show();
        url = "http://10.0.2.2/safalm_admin/employee_detail.php?emp_id=" + emp_id;
        at = new EmployeeDetailActivity.Employee().execute();


        icEDedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeDetailActivity.this, EditEmployeeDetailActivity.class);
                i.putExtra("emp_id", emp_id);
                i.putExtra("emp_name", emp_name);
                i.putExtra("emp_email", emp_email);
                i.putExtra("emp_address", emp_address);
                //  i.putExtra("emp_date", emp_password);
                i.putExtra("emp_mobile", emp_mobile);
                i.putExtra("emp_password", emp_password);
                startActivity(i);

            }
        });

        icEDdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://10.0.2.2/safalm_admin/delete_employee.php?emp_id=" + emp_id;
                // list.setAdapter(null);
                new EmployeeDetailActivity.Employee().execute();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag > 0) {

            sa = (SafalmAdmin) getApplicationContext();
            emp_id = sa.getempId();
            url = "http://10.0.2.2/safalm_admin/employee_detail.php?emp_id=" + emp_id;
            at.cancel(true);
            at = new EmployeeDetailActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeDetailActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);


            txtEDname.setText(emp_name);
            txtEDpassword.setText(emp_password);
            txtEDdate.setText(emp_date);
            txtEDmobile.setText(emp_mobile);
            txtEDaddress.setText(emp_address);
            txtEDemail.setText(emp_email);

            //Toast.makeText(SelfLeadDetailActivity.this, "Name="+name, Toast.LENGTH_SHORT).show();
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
                    //  id = c.getString(TAG_SLID);
                    emp_name = c.getString(TAG_SLNAME);
                    emp_address = c.getString(TAG_SLADDRESS);
                    emp_mobile = c.getString(TAG_SLCONTACT);
                    emp_email = c.getString("email");
                    emp_password = c.getString("password");
                    emp_date = c.getString("date");

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
