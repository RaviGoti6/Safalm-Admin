package com.example.raju.safalmadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.raju.safalmadmin.EmployeeSide.EmployeeProductListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GiveTargetActivity extends AppCompatActivity {

    EditText editGTleadquantity,editGTproductname,editGTfromdate,editGTtodate;
    Button editGTadd;
    Spinner editGTtargettype;
    SafalmAdmin se;
    ImageView imgcalender1,imgcalender2,imgproductlist;
    String quantity,target_type,date_from,date_to,p_id,emp_id,pname;

    AsyncTask at;
    int flag = 0;

    private String TAG = GiveTargetActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;
    LinearLayout ic1;

    ArrayList<HashMap<String, String>> leadList;

    String[] target =new String[] {"Product","Lead"};

    private static final String TAG_PID = "pid";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_PPRICE = "pprice";
    private static final String TAG_PDETAIL = "pdetail";

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_target_activity);

        editGTleadquantity=findViewById(R.id.editGTleadquantity);
        editGTproductname=findViewById(R.id.editGTproductname);
        editGTfromdate=findViewById(R.id.editGTfromdate);
        editGTtodate=findViewById(R.id.editGTtodate);
        imgcalender1=findViewById(R.id.imgcalender1);
        imgcalender2=findViewById(R.id.imgcalender2);
        imgproductlist=findViewById(R.id.imgproductlist);
        editGTadd=findViewById(R.id.editGTadd);
        ic1=findViewById(R.id.ic1);

        editGTtargettype=findViewById(R.id.editGTtargettype);

        se = (SafalmAdmin) getApplicationContext();
        emp_id = se.getempId();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,target);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGTtargettype.setAdapter(adapter);

        editGTtargettype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                target_type =editGTtargettype.getSelectedItem().toString();
                //Toast.makeText(GiveTargetActivity.this, "target:"+target_type, Toast.LENGTH_SHORT).show();

                if(target_type.equals("Product"))
                {
                    ic1.setVisibility(View.VISIBLE);
                }
                if(target_type.equals("Lead"))
                {
                    ic1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editGTadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity = editGTleadquantity.getText().toString().trim();
                date_from = editGTfromdate.getText().toString().trim();
                date_to = editGTtodate.getText().toString().trim();

                if(target_type.equals("Product")) {

                    url = "http://10.0.2.2/safalm_admin/add_target.php?t_quantity=" + quantity + "&t_type=" + target_type + "&f_date=" + date_from + "&p_id=" + p_id + "&t_date=" + date_to + "&emp_id=" + emp_id;
                    new GiveTargetActivity.Employee().execute();
                }
                if(target_type.equals("Lead")) {

                    p_id = "0";
                    url = "http://10.0.2.2/safalm_admin/add_target.php?t_quantity=" + quantity + "&t_type=" + target_type + "&f_date=" + date_from + "&p_id=" + p_id + "&t_date=" + date_to + "&emp_id=" + emp_id;
                    new GiveTargetActivity.Employee().execute();
                }



            }
        });

        imgproductlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (imgproductlist.hasFocus()) {
                Intent i = new Intent(GiveTargetActivity.this, EmployeeProductListActivity.class);
                startActivityForResult(i, 1);
                //  }
                //Toast.makeText(getApplicationContext(), "HEllo", Toast.LENGTH_SHORT).show();
            }
        });

        imgcalender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(GiveTargetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mnth, day;

                        editGTfromdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        imgcalender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(GiveTargetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mnth, day;

                        editGTtodate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data.getExtras().containsKey(TAG_PID)) {

            p_id = data.getExtras().getString(TAG_PID);
            pname = data.getExtras().getString(TAG_PNAME);

            editGTproductname.setText(pname);

        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GiveTargetActivity.this);
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
                Toast.makeText(GiveTargetActivity.this, "Target added successfully...", Toast.LENGTH_SHORT).show();
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
