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

public class MainLeadListActivity extends AppCompatActivity {

    String empid;
    ListAdapter adptr;

    ListView list;
    AsyncTask at;
    int flag = 0;
    SafalmAdmin se;
    private String TAG = MainLeadListActivity.class.getSimpleName();

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
        setContentView(R.layout.main_lead_list_activity);

        list = findViewById(R.id.listCompanyLeadList);

        se = (SafalmAdmin) getApplicationContext();
        empid = se.getempId();

        leadList = new ArrayList<>();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String lead_id = (String) obj.get("id");

                //Toast.makeText(MainLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainLeadListActivity.this, MainLeadDetailActivity.class);
                i.putExtra("lead_id", lead_id);
                startActivity(i);
            }
        });

        url = "http://10.0.2.2/safalm_admin/main_lead_list.php";
        // list.setAdapter(null);
        at = new MainLeadListActivity.Employee().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.listCompanyLeadList);
            url = "http://10.0.2.2/safalm_admin/main_lead_list.php";
            leadList.clear();
            list.setAdapter(null);

            at = new MainLeadListActivity.Employee().execute();
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainLeadListActivity.this);
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

            adptr = new SimpleAdapter(MainLeadListActivity.this, leadList, R.layout.self_lead_list_item, new String[]{TAG_SLID, TAG_SLNAME, TAG_SLADDRESS, TAG_SLCONTACT}, new int[]{R.id.txtSLLIid, R.id.txtSLLIname, R.id.txtSLLIaddress, R.id.txtSLLImobile});
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
                        String id = c.getString(TAG_SLID);
                        String name = c.getString(TAG_SLNAME);
                        String address = c.getString(TAG_SLADDRESS);
                        String contact = c.getString(TAG_SLCONTACT);

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_SLID, id);
                        map.put(TAG_SLNAME, name);
                        map.put(TAG_SLADDRESS, address);
                        map.put(TAG_SLCONTACT, contact);

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
