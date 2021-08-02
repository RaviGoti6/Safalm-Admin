package com.example.raju.safalmadmin;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MainSalesLeadListActivity extends AppCompatActivity {

    ListView list;

    String empid,date;
    ListAdapter adptr;


    AsyncTask at;
    int flag = 0;
    SafalmAdmin se;
    private String TAG = MainSalesLeadListActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_VLID = "id";
    private static final String TAG_VLNAME = "name";
    private static final String TAG_VLADDRESS = "address";
    private static final String TAG_VLCONTACT = "mobile";
    private static final String TAG_VLPRODUCT = "product";
    private static final String TAG_VLDATE = "date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_sales_lead_list_activity);
        list=findViewById(R.id.listCompanySalesLeadList);
        se = (SafalmAdmin) getApplicationContext();
        empid = se.getempId();

        leadList = new ArrayList<>();


        url = "http://10.0.2.2/safalm_admin/main_sales_lead_list.php";
        // list.setAdapter(null);
        at = new MainSalesLeadListActivity.Employee().execute();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String lead_id = (String) obj.get("id");

                //Toast.makeText(SelfLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainSalesLeadListActivity.this, MainSalesLeadDetailActivity.class);
                i.putExtra("lead_id", lead_id);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.listCompanySalesLeadList);
            url = "http://10.0.2.2/safalm_admin/main_sales_lead_list.php";
            leadList.clear();
            list.setAdapter(null);

            at = new MainSalesLeadListActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainSalesLeadListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            adptr = new SimpleAdapter(MainSalesLeadListActivity.this, leadList, R.layout.visited_lead_list_item, new String[]{TAG_VLID, TAG_VLNAME, TAG_VLADDRESS, TAG_VLCONTACT, TAG_VLDATE,"product"}, new int[]{R.id.txtVLLIid, R.id.txtVLLIname, R.id.txtVLLIaddress, R.id.txtVLLImobile, R.id.txtVLLIdate,R.id.txtVLLIproduct});
            list.setAdapter(adptr);

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
                        String id = c.getString(TAG_VLID);
                        String name = c.getString(TAG_VLNAME);
                        String address = c.getString(TAG_VLADDRESS);
                        String contact = c.getString(TAG_VLCONTACT);
                        date = c.getString(TAG_VLDATE);
                        String product = c.getString("product");

                        StringTokenizer tokens = new StringTokenizer(date, " ");
                        String d= tokens.nextToken();
                        // time = tokens.nextToken();

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_VLID, id);
                        map.put(TAG_VLNAME, name);
                        map.put(TAG_VLADDRESS, address);
                        map.put(TAG_VLCONTACT, contact);
                        map.put(TAG_VLDATE, d);
                        map.put("product", product);


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
