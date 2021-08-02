package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeOpportunityListActivity extends AppCompatActivity {
    ListView listOpportunity;

    String empid;
    ListAdapter adptr;

    AsyncTask at;
    int flag = 0;
    SafalmAdmin se;
    private String TAG = EmployeeOpportunityListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;
    Button giveOpp;
    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_OID = "oppo_id";
    private static final String TAG_OTYPE = "oppo_type";
    private static final String TAG_OQUANTITY = "oppo_quantity";
    private static final String TAG_DATE_TO = "oppo_to_date";
    private static final String TAG_DATE_FROM = "oppo_from_date";
    private static final String TAG_DETAIL = "extra_detail";
    //  private static final String TAG_DATE_TO = "mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_opportunity_list_activity);

        listOpportunity=findViewById(R.id.listOpportunity);
        giveOpp = findViewById(R.id.btnOppoTarget);

        se = (SafalmAdmin) getApplicationContext();
        empid = se.getempId();

        leadList = new ArrayList<>();

        url = "http://10.0.2.2/safalm_admin/opportunity_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new Employee().execute();

        giveOpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeOpportunityListActivity.this,EmployeeGiveOpportunityActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            listOpportunity=findViewById(R.id.listOpportunity);
            url = "http://10.0.2.2/safalm_admin/opportunity_list.php?emp_id=" + empid;
            leadList.clear();
            listOpportunity.setAdapter(null);

            at = new Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeOpportunityListActivity.this);
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

            adptr = new SimpleAdapter(EmployeeOpportunityListActivity.this, leadList, R.layout.opportunity_list_item, new String[]{TAG_OID, TAG_OTYPE, TAG_OQUANTITY, TAG_DATE_TO, TAG_DATE_FROM, TAG_DETAIL}, new int[]{R.id.txtOLIid, R.id.txtOLIoppotype, R.id.txtOLItotalquantity,  R.id.txtOLIuptodate,R.id.txtOLIfromdate, R.id.txtOLIdetail});
            listOpportunity.setAdapter(adptr);
            //{

//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//
//                    View v = super.getView(position, convertView, parent);
//
//                    TextView target_type = v.findViewById(R.id.txtTLItargettype);
//                    TextView product_name = v.findViewById(R.id.txtPLIproductname);
//                    TextView product = v.findViewById(R.id.txtTLIproductname);
//                    TextView quantity = v.findViewById(R.id.quantity);
//                    TextView tquantity = v.findViewById(R.id.txtTLItotalquantity);
//                    ProgressBar pb = v.findViewById(R.id.TLIProgressbar);
//
//                    if (target_type.getText().toString().equals("Product")) {
//                        product_name.setVisibility(v.VISIBLE);
//                        product.setVisibility(v.VISIBLE);
//                    }
//
//                    String q = quantity.getText().toString();
//                    String tq = tquantity.getText().toString();
//                    int q1 =0,tq1=0;
//                    if (q.equals("null")) {
//                        q = "0";
//                    }
//
//                    q1 = Integer.parseInt(q);
//                    tq1 = Integer.parseInt(tq);
//                    float at=0;
//                    at = (q1 * 100) / tq1;
//
//                    pb.setProgress((int) at);
//
//                    return v;
//                }
//            };



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
                        String tid = c.getString(TAG_OID);
                        String ttype = c.getString(TAG_OTYPE);
                        String tquantity = c.getString(TAG_OQUANTITY);
                        String tdateto = c.getString(TAG_DATE_TO);
                        String tdatefrom = c.getString(TAG_DATE_FROM);
                        String detail = c.getString(TAG_DETAIL);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_OID, tid);
                        map.put(TAG_OTYPE, ttype);
                        map.put(TAG_OQUANTITY, tquantity);
                        map.put(TAG_DATE_TO, tdateto);
                        map.put(TAG_DATE_FROM, tdatefrom);
                        map.put(TAG_DETAIL, detail);


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
