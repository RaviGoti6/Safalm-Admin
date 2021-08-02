package com.example.raju.safalmadmin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GiveProductActivity extends AppCompatActivity {

    EditText editGPleadquantity,editGPproductname,editGPproductdetail,editGPproductprice;

    Button editGPassign;
    String name,price,detail;

    AsyncTask at;
    int flag = 0;

    private String TAG = GiveProductActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;
    SafalmAdmin se;
    String emp_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_product_activity);

        editGPleadquantity=findViewById(R.id.editGPleadquantity);
        editGPproductname=findViewById(R.id.editGPproductname);
        editGPproductdetail=findViewById(R.id.editGPproductdetail);
        editGPproductprice=findViewById(R.id.editGPproductprice);

        editGPassign=findViewById(R.id.editGPassign);

        se = (SafalmAdmin) getApplicationContext();
        emp_id = se.getempId();

        editGPassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=editGPproductname.getText().toString().trim();
                price=editGPproductprice.getText().toString().trim();
                detail=editGPproductdetail.getText().toString().trim();

                if (!name.isEmpty() && !price.isEmpty() && !detail.isEmpty()){

                    url = "http://10.0.2.2/safalm_admin/give_product.php?p_name=" + name + "&p_price=" + price + "&p_detail=" + detail + "&emp_id=" + emp_id ;
                    new GiveProductActivity.Employee().execute();

                }else {
                    Toast.makeText(GiveProductActivity.this, "All Field Required", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GiveProductActivity.this);
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

            //if (contacts.get("success").toString().equals("1")) {
            // Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
            //  startActivity(i);
            finish();
            Toast.makeText(GiveProductActivity.this, "Product added successfully...", Toast.LENGTH_SHORT).show();
            // }

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
                    //JSONArray self_lead = jsonObj.getJSONArray("message");
//                    Log.e("SAFALM2=", contacts.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    Log.e("SAFALM3=", success);
                    Log.e("SAFALM4=", message);
                    //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");

                    // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                    // tmp hash map for single contact
                    contacts = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contacts.put("success", success);
                    contacts.put("message", message);
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
