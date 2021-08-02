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

public class EmployeeSelfVisitedLeadDetailActivity extends AppCompatActivity {

    String lead_id;
    SafalmAdmin sa;
    private String TAG = EmployeeSelfVisitedLeadDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    LinearLayout imgLeadLocator;

    private static String url = "";
    int flag = 0;
    String id, name, address, contact, email, compname, date_time, date, time,pname,pquantity,pprice,pamount,decision,pid;

    TextView txtSVLDname, txtSVLDaddress, txtSVLDmobile, txtSVLDemail, txtSVLDcompanyname,txtSVLDproductname,txtSVLDproductquantity,txtSVLDproductprice,txtSVLDproductamount,txtSVLDdatetime,txtSVLDdecision;

    AsyncTask at;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_self_visited_lead_detail_activity);

        txtSVLDname = findViewById(R.id.txtSVLDname);
        txtSVLDaddress = findViewById(R.id.txtSVLDaddress);
        txtSVLDmobile = findViewById(R.id.txtSVLDmobile);
        txtSVLDemail = findViewById(R.id.txtSVLDemail);
        txtSVLDcompanyname = findViewById(R.id.txtSVLDcompanyname);
        txtSVLDproductname = findViewById(R.id.txtSVLDproductname);
        txtSVLDproductquantity = findViewById(R.id.txtSVLDproductquantity);
        txtSVLDproductprice = findViewById(R.id.txtSVLDproductprice);
        txtSVLDproductamount = findViewById(R.id.txtSVLDproductamount);
        txtSVLDdatetime = findViewById(R.id.txtSVLDdatetime);
        txtSVLDdecision = findViewById(R.id.txtSVLDdecision);

        imgLeadLocator=findViewById(R.id.imgLeadLocator);

        imgLeadLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
        });

        ImageView icSVLDedit = findViewById(R.id.icSVLDedit);
        ImageView icSVLDaddsales = findViewById(R.id.icSVLDaddsales);
        ImageView icSVLDaddtask = findViewById(R.id.icSVLDaddtask);
        ImageView icSVLDdelete = findViewById(R.id.icSVLDdelete);


        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            //txtSLDname.setText(lead_id);
        }

        //Toast.makeText(EmployeeSelfVisitedLeadDetailActivity.this, "id"+lead_id, Toast.LENGTH_SHORT).show();

        url = "http://10.0.2.2/safalm_admin/employee_visited_lead_detail.php?id=" + lead_id;
        at = new Employee().execute();

        icSVLDedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(EmployeeSelfVisitedLeadDetailActivity.this, EmployeeEditVisitedLeadActivity.class);

                i.putExtra("id",id);
                i.putExtra("name",name);
                i.putExtra("pid",pid);
                i.putExtra("address",address);
                i.putExtra("mobile",contact);
                i.putExtra("pname",pname);
                i.putExtra("prate",pprice);
                i.putExtra("pamount",pamount);
                i.putExtra("pquantity",pquantity);
                i.putExtra("decision",decision);
                i.putExtra("date",date);
                i.putExtra("time",time);
                i.putExtra("cname",compname);
                i.putExtra("email",email);
                i.putExtra("cmp_edit","0");

                startActivity(i);
            }
        });

        icSVLDaddsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(EmployeeSelfVisitedLeadDetailActivity.this, EmployeeAddSelfVisitedLeadToSalesActivity.class);

                i.putExtra("vlid",lead_id);
                i.putExtra("pid",pid);
                i.putExtra("name",name);
                i.putExtra("address",address);
                i.putExtra("mobile",contact);
                i.putExtra("pname",pname);
                i.putExtra("prate",pprice);
                i.putExtra("pamount",pamount);
                i.putExtra("pquantity",pquantity);
                i.putExtra("decision",decision);
                i.putExtra("date",date);
                i.putExtra("time",time);
                i.putExtra("cname",compname);
                i.putExtra("email",email);

                startActivity(i);
            }
        });

        icSVLDaddtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(EmployeeSelfVisitedLeadDetailActivity.this, EmployeeLeadAddToTaskActivity.class);
                i.putExtra("vlid", id);
                i.putExtra("name", name);
                i.putExtra("address", address);
                i.putExtra("mobile", contact);
                i.putExtra("email", email);
                i.putExtra("cmpName", compname);
                startActivity(i);

            }
        });

        icSVLDdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://10.0.2.2/safalm_admin/employee_delete_self_visited_lead.php?id=" + lead_id;

                new EmployeeSelfVisitedLeadDetailActivity.Employee().execute();

                Intent i = new Intent(EmployeeSelfVisitedLeadDetailActivity.this, EmployeeSelfVisitedLeadListActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag > 0) {
            Intent ii = getIntent();
            Bundle b = ii.getExtras();
            if (b != null) {
                lead_id = (String) b.get("lead_id");
                //txtSLDname.setText(lead_id);
            }
            url = "http://10.0.2.2/safalm_admin/employee_visited_lead_detail.php?id=" + lead_id;
            at.cancel(true);
            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeSelfVisitedLeadDetailActivity.this);
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

            txtSVLDname.setText(name);
            txtSVLDproductname.setText(pname);
            txtSVLDaddress.setText(address);
            txtSVLDmobile.setText(contact);
            txtSVLDemail.setText(email);
            txtSVLDcompanyname.setText(compname);
            txtSVLDproductquantity.setText(pquantity);
            txtSVLDproductprice.setText(pprice);
            txtSVLDproductamount.setText(pamount);
            txtSVLDdecision.setText(decision);
            txtSVLDdatetime.setText(date_time);

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
                    pname = c.getString("pname");
                    pid = c.getString("pid");
                    pamount = c.getString("pamount");
                    pprice = c.getString("prate");
                    decision = c.getString("decision");
                    pquantity = c.getString("pquantity");

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

