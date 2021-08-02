package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class EmployeeSelfLeadDetailActivity extends AppCompatActivity {

    SafalmAdmin sa;
    LinearLayout imgLeadLocator;
    private String TAG = EmployeeSelfLeadDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;
    String id, name, address, contact, lead_id, email, compname, date_time, date, time;

    TextView txtSLDname, txtSLDaddress, txtSLDmobile, txtSLDemail, txtSLDcompanyname;

    AsyncTask at;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_self_lead_detail_activity);

        txtSLDname = findViewById(R.id.txtSLDname);
        txtSLDaddress = findViewById(R.id.txtSLDaddress);
        txtSLDmobile = findViewById(R.id.txtSLDmobile);
        txtSLDemail = findViewById(R.id.txtSLDemail);
        txtSLDcompanyname = findViewById(R.id.txtSLDcompanyname);

        ImageView icSLDedit = findViewById(R.id.icSLDedit);
        ImageView icSLDaddvisit = findViewById(R.id.icSLDaddvisit);
        ImageView icSLDaddsales = findViewById(R.id.icSLDaddsales);
        ImageView icSLDaddtask = findViewById(R.id.icSLDaddtask);
        ImageView icSLDdelete = findViewById(R.id.icSLDdelete);

        imgLeadLocator=findViewById(R.id.imgLeadLocator);


        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            //txtSLDname.setText(lead_id);
        }

        url = "http://10.0.2.2/safalm_admin/employee_self_lead_detail.php?id=" + lead_id;
        at = new Employee().execute();

        imgLeadLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        icSLDedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(EmployeeSelfLeadDetailActivity.this, EmployeeEditSelfLeadActivity.class);
                i.putExtra("id", lead_id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("mobile", contact);
                i.putExtra("email", email);
                i.putExtra("cmpName", compname);
                i.putExtra("date", date);
                i.putExtra("time", time);
                startActivity(i);


            }
        });

        icSLDaddvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeSelfLeadDetailActivity.this, EmployeeAddSelfLeadToVisitedAndSalesActivity.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("mobile", contact);
                i.putExtra("email", email);
                i.putExtra("cmpName", compname);
                startActivity(i);

            }
        });

        icSLDaddsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeSelfLeadDetailActivity.this, EmployeeAddSelfLeadToSalesActivity.class);
                i.putExtra("id", id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("mobile", contact);
                i.putExtra("email", email);
                i.putExtra("cmpName", compname);
                startActivity(i);

            }
        });

        icSLDaddtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeSelfLeadDetailActivity.this, EmployeeLeadAddToTaskActivity.class);
                i.putExtra("slid", id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("mobile", contact);
                i.putExtra("email", email);
                i.putExtra("cmpName", compname);
                startActivity(i);


            }
        });

        icSLDdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://10.0.2.2/safalm_admin/employee_delete_self_lead.php?id=" + lead_id;

                new Employee().execute();

                Intent i = new Intent(EmployeeSelfLeadDetailActivity.this, EmployeeSelfLeadListActivity.class);
                startActivity(i);
                finish();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag > 0) {
            url = "http://10.0.2.2/safalm/self_lead_detail.php?id=" + lead_id;
            at.cancel(true);
            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeSelfLeadDetailActivity.this);
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
