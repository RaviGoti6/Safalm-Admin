package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.GiveTargetActivity;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class EmployeeTargetListActivity extends AppCompatActivity {

    Button btnGiveTarget;

    ListView list_target;

    String empid;
    ListAdapter adptr;

    AsyncTask at;
    int flag = 0;
    SafalmAdmin se;
    private String TAG = EmployeeTargetListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    int q1 = 0, tq1 = 0;
    String q = "0", tq = "0";

    TextView q_complete, q_remaining;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_TID = "target_id";
    private static final String TAG_TTYPE = "target_type";
    private static final String TAG_TQUANTITY = "target_quantity";
    private static final String TAG_DATE_TO = "target_date_to";
    private static final String TAG_DATE_FROM = "target_date_from";
    private static final String TAG_PRODUCT_NAME = "product_name";
    //  private static final String TAG_DATE_TO = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_target_list_activity);

        list_target = findViewById(R.id.list_target);
        btnGiveTarget = findViewById(R.id.btnGiveTarget);


        btnGiveTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeTargetListActivity.this, GiveTargetActivity.class));
            }
        });

        se = (SafalmAdmin) getApplicationContext();
        empid = se.getempId();

        leadList = new ArrayList<>();

        list_target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list_target.getAdapter().getItem(position);
                String lead_id = (String) obj.get("target_id");

               // Toast.makeText(EmployeeTargetListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                //  Intent i = new Intent(EmployeeTargetListActivity.this, SelfLeadDetailActivity.class);
                //  i.putExtra("lead_id", lead_id);
                // startActivity(i);
            }
        });

        url = "http://10.0.2.2/safalm_admin/employee_target_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new EmployeeTargetListActivity.Employee().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list_target = findViewById(R.id.list_target);
            url = "http://10.0.2.2/safalm_admin/employee_target_list.php?emp_id=" + empid;
            leadList.clear();
            list_target.setAdapter(null);

            at = new EmployeeTargetListActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeTargetListActivity.this);
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

            adptr = new SimpleAdapter(EmployeeTargetListActivity.this, leadList, R.layout.employee_target_list_item, new String[]{TAG_TID, TAG_TTYPE, TAG_TQUANTITY, TAG_DATE_TO, TAG_DATE_FROM, TAG_PRODUCT_NAME, "quantity"}, new int[]{R.id.txtTLIid, R.id.txtTLItargettype, R.id.txtTLItotalquantity, R.id.txtTLIuptodate, R.id.txtTLIgivendate, R.id.txtPLIproductname, R.id.quantity}) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    TextView target_type = v.findViewById(R.id.txtTLItargettype);
                    TextView product_name = v.findViewById(R.id.txtPLIproductname);
                    TextView product = v.findViewById(R.id.txtTLIproductname);
                    TextView quantity = v.findViewById(R.id.quantity);
                    TextView tquantity = v.findViewById(R.id.txtTLItotalquantity);
                    ProgressBar pb = v.findViewById(R.id.TLIProgressbar);
                    q_complete = v.findViewById(R.id.q_complete);
                    q_remaining = v.findViewById(R.id.q_remaining);

                    if (target_type.getText().toString().equals("Product")) {
                        product_name.setVisibility(v.VISIBLE);
                        product.setVisibility(v.VISIBLE);
                    }
                    if (target_type.getText().toString().equals("Lead")) {
                        product_name.setVisibility(v.GONE);
                        product.setVisibility(v.GONE);
                    }

                    q = quantity.getText().toString();
                    tq = tquantity.getText().toString();

                    if (q.equals("null")) {
                        q = "0";
                    }

                    q1 = Integer.parseInt(q);
                    tq1 = Integer.parseInt(tq);


                    float at = (q1 * 100) / tq1;

                    q_complete.setText(String.valueOf(q1));
                    q_remaining.setText(String.valueOf(tq1 - q1));


                    pb.setProgress((int) at);

                    return v;
                }
            };
            list_target.setAdapter(adptr);


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
                        String tid = c.getString(TAG_TID);
                        String ttype = c.getString(TAG_TTYPE);
                        String tquantity = c.getString(TAG_TQUANTITY);
                        String tdateto = c.getString(TAG_DATE_TO);
                        String tdatefrom = c.getString(TAG_DATE_FROM);
                        String product_name = c.getString(TAG_PRODUCT_NAME);
                        String quantity = c.getString("quantity");

                        StringTokenizer s = new StringTokenizer(tdatefrom," ");
                        String dt_from = s.nextToken();

                        StringTokenizer s1 = new StringTokenizer(tdateto," ");
                        String dt_to = s1.nextToken();

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_TID, tid);
                        map.put(TAG_TTYPE, ttype);
                        map.put(TAG_TQUANTITY, tquantity);
                        map.put(TAG_DATE_TO, dt_to);
                        map.put(TAG_DATE_FROM, dt_from);
                        map.put(TAG_PRODUCT_NAME, product_name);
                        map.put("quantity", quantity);

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

