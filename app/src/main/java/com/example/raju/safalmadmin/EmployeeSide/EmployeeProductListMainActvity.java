package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.GiveProductActivity;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeProductListMainActvity extends AppCompatActivity {

    TextView txtPLIid,txtPLIname,txtPLIprice,txtPLIdetail;

    ListView list_main_product;

    String empid;

    Button btnGiveProduct;

    SafalmAdmin se;
    private String TAG = EmployeeProductListMainActvity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> product_list;

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_product_list_main_actvity);

        list_main_product=findViewById(R.id.list_main_product);
        btnGiveProduct=findViewById(R.id.btnGiveProduct);

        se = (SafalmAdmin) getApplicationContext();
        empid = se.getempId();

        product_list = new ArrayList<>();


        url = "http://10.0.2.2/safalm_admin/employee_product_list_by_employee.php?emp_id=" + empid;
        new EmployeeProductListMainActvity.Employee().execute();

        list_main_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list_main_product.getAdapter().getItem(position);
                String p_id = (String) obj.get(TAG_PID);
                String p_name = (String) obj.get(TAG_PNAME);
                String p_price = (String) obj.get(TAG_PPRICE);
                String p_detail = (String) obj.get(TAG_PDETAIL);

                Intent i = getIntent();

                i.putExtra(TAG_PID, p_id);
                i.putExtra(TAG_PNAME, p_name);
                i.putExtra(TAG_PPRICE, p_price);
                i.putExtra(TAG_PDETAIL, p_detail);

                //setResult(RESULT_OK, i);
                //finish();
            }
        });
        btnGiveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeProductListMainActvity.this,GiveProductActivity.class));
            }
        });

    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeProductListMainActvity.this);
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

            ListAdapter adapter = new SimpleAdapter(EmployeeProductListMainActvity.this, product_list, R.layout.product_list_item, new String[]{TAG_PID, TAG_PNAME,TAG_PPRICE,TAG_PDETAIL}, new int[]{R.id.txtPLIid, R.id.txtPLIname,R.id.txtPLIprice,R.id.txtPLIdetail});
            list_main_product.setAdapter(adapter);


            //  if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            //       Toast.makeText(SelfLeadListActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
            //   }

            pDialog.dismiss();
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
                        String ppid = c.getString(TAG_PID);
                        String ppname = c.getString(TAG_PNAME);
                        String ppprice = c.getString(TAG_PPRICE);
                        String ppdetail = c.getString(TAG_PDETAIL);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_PID, ppid);
                        map.put(TAG_PNAME, ppname);
                        map.put(TAG_PPRICE, ppprice);
                        map.put(TAG_PDETAIL, ppdetail);

                        product_list.add(map);
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
