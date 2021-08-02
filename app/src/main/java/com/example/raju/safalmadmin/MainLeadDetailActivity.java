package com.example.raju.safalmadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class MainLeadDetailActivity extends AppCompatActivity {

    TextView txtCLDname, txtCLDaddress, txtCLDmobile, txtCLDemail, txtCLDcompanyname;

    String id, name, address, contact, lead_id, email, compname, date_time, date, time;

    SafalmAdmin se;
    private String TAG = MainLeadDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;

    AsyncTask at;

    LinearLayout imgLeadLocator;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lead_detail_activity);

        txtCLDname = findViewById(R.id.txtCLDname);
        txtCLDaddress = findViewById(R.id.txtCLDaddress);
        txtCLDmobile = findViewById(R.id.txtCLDmobile);
        txtCLDemail = findViewById(R.id.txtCLDemail);
        txtCLDcompanyname = findViewById(R.id.txtCLDcompanyname);

//        ImageView icCLDaddvisit = findViewById(R.id.icCLDaddvisit);
//        ImageView icCLDaddsales = findViewById(R.id.icCLDaddsales);
//        ImageView icCLDaddtask = findViewById(R.id.icCLDaddtask);

//        imgLeadLocator=findViewById(R.id.imgLeadLocator);
//        imgLeadLocator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                startActivity(mapIntent);
//            }
//        });

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            //txtSLDname.setText(lead_id);
        }
        //Toast.makeText(MainLeadDetailActivity.this, "id"+lead_id, Toast.LENGTH_SHORT).show();
        url = "http://10.0.2.2/safalm/company_lead_detail.php?id=" + lead_id;
        at = new MainLeadDetailActivity.Employee().execute();

//        icCLDaddvisit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(CompanyLeadDetailActivity.this, AddCompanyLeadToVisitedAndSalesActivity.class);
//                i.putExtra("id", id);
//                i.putExtra("name", name);
//                i.putExtra("address", address);
//                i.putExtra("mobile", contact);
//                i.putExtra("email", email);
//                i.putExtra("cmpName", compname);
//                startActivity(i);
//
//            }
//        });
//
//        icCLDaddsales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(CompanyLeadDetailActivity.this, AddCompanyLeadToSalesActivity.class);
//                i.putExtra("id", id);
//                i.putExtra("name", name);
//                i.putExtra("address", address);
//                i.putExtra("mobile", contact);
//                i.putExtra("email", email);
//                i.putExtra("cmpName", compname);
//                startActivity(i);
//
//            }
//        });
//
//        icCLDaddtask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(CompanyLeadDetailActivity.this, LeadAddToTaskActivity.class);
//                i.putExtra("clid", id);
//                i.putExtra("name", name);
//                i.putExtra("address", address);
//                i.putExtra("mobile", contact);
//                i.putExtra("email", email);
//                i.putExtra("cmpName", compname);
//                i.putExtra("cmpTask", "1");
//                startActivity(i);
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag > 0) {
            url = "http://10.0.2.2/safalm/company_lead_detail.php?id=" + lead_id;
            at.cancel(true);
            at = new MainLeadDetailActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainLeadDetailActivity.this);
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

            //Log.e("SAFALM5=", contacts.get("success").toString() + contacts.get("message").toString());
            // Toast.makeText(getApplicationContext(), contacts.get("success").toString() + contacts.get("message").toString(), Toast.LENGTH_SHORT).show();
            //String empId=contacts.get("message").toString();
            //se.setempId(contacts.get("message").toString());
            //   String eid=se.getempId().toString();
            //    Log.e("Safalam 6:",eid);
            txtCLDname.setText(name);
            txtCLDaddress.setText(address);
            txtCLDmobile.setText(contact);
            txtCLDemail.setText(email);
            txtCLDcompanyname.setText(compname);

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
