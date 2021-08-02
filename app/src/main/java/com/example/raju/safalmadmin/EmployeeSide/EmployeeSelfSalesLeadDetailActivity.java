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

public class EmployeeSelfSalesLeadDetailActivity extends AppCompatActivity {

    TextView txtSSLDname, txtSSLDaddress, txtSSLDmobile, txtSSLDemail, txtSSLDcompanyname, txtSSLDproductname, txtSSLDproductquantity, txtSSLDproductprice, txtSSLDproductamount, txtSSLDdatetime;

    String lead_id;

    LinearLayout imgLeadLocator;
    SafalmAdmin sa;
    private String TAG = EmployeeSelfSalesLeadDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;
    String id, slid, name, address, contact, email, compname, date_time, date, time, pname, pquantity, pprice, pamount, decision, pid;

    AsyncTask at;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_self_sales_lead_detail_activity);

        txtSSLDname = findViewById(R.id.txtSSLDname);
        txtSSLDaddress = findViewById(R.id.txtSSLDaddress);
        txtSSLDmobile = findViewById(R.id.txtSSLDmobile);
        txtSSLDemail = findViewById(R.id.txtSSLDemail);
        txtSSLDcompanyname = findViewById(R.id.txtSSLDcompanyname);
        txtSSLDproductname = findViewById(R.id.txtSSLDproductname);
        txtSSLDproductquantity = findViewById(R.id.txtSSLDproductquantity);
        txtSSLDproductprice = findViewById(R.id.txtSSLDproductprice);
        txtSSLDproductamount = findViewById(R.id.txtSSLDproductamount);
        txtSSLDdatetime = findViewById(R.id.txtSSLDdatetime);
        // txtSSLDdetail = findViewById(R.id.txtSSLDdetail);


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

        ImageView icSSLDedit = findViewById(R.id.icSSLDedit);
      //  ImageView icSSLDaddtask = findViewById(R.id.icSSLDaddtask);
        ImageView icSSLDdelete = findViewById(R.id.icSSLDdelete);



        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            lead_id = (String) b.get("lead_id");
            //txtSLDname.setText(lead_id);
        }
        url = "http://10.0.2.2/safalm_admin/employee_self_sales_lead_detail.php?id=" + lead_id;
        at = new Employee().execute();

        icSSLDedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EmployeeSelfSalesLeadDetailActivity.this,EmployeeEditSelfSalesLeadActivity.class);

                i.putExtra("id",id);
                i.putExtra("slid",slid);
                i.putExtra("name",name);
                i.putExtra("address",address);
                i.putExtra("mobile",contact);
                i.putExtra("pname",pname);
                i.putExtra("prate",pprice);
                i.putExtra("pid",pid);
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

//        icSSLDaddtask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(EmployeeSelfSalesLeadDetailActivity.this, EmployeeLeadAddToTaskActivity.class);
//                i.putExtra("vlid", id);
//                i.putExtra("name", name);
//                i.putExtra("address", address);
//                i.putExtra("mobile", contact);
//                i.putExtra("email", email);
//                i.putExtra("cmpName", compname);
//                startActivity(i);
//
//            }
//        });

        icSSLDdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://10.0.2.2/safalm_admin/employee_delete_self_sales_lead.php?id=" + lead_id;

                new EmployeeSelfSalesLeadDetailActivity.Employee().execute();

                Intent i = new Intent(EmployeeSelfSalesLeadDetailActivity.this, EmployeeSelfSalesLeadListActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag > 0) {
            url = "http://10.0.2.2/safalm_admin/employee_self_sales_lead_detail.php?id=" + lead_id;
            at.cancel(true);
            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeSelfSalesLeadDetailActivity.this);
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


            //Toast.makeText(EmployeeSelfSalesLeadDetailActivity.this, "p_name=" + pname, Toast.LENGTH_SHORT).show();
            txtSSLDname.setText(name);
            txtSSLDproductname.setText(pname);
            txtSSLDaddress.setText(address);
            txtSSLDmobile.setText(contact);
            txtSSLDemail.setText(email);
            txtSSLDcompanyname.setText(compname);
            txtSSLDproductquantity.setText(pquantity);
            txtSSLDproductprice.setText(pprice);
            txtSSLDproductamount.setText(pamount);
            //  txtSSLDdetail.setText(decision);
            txtSSLDdatetime.setText(date_time);

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
                    slid = c.getString("slid");
                    pamount = c.getString("pamount");
                    pprice = c.getString("prate");
                    //        decision = c.getString("decision");
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
