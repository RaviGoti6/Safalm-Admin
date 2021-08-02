package com.example.raju.safalmadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raju.safalmadmin.EmployeeSide.EmployeeDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class EditEmployeeDetailActivity extends AppCompatActivity {

    EditText editEid,editEname,editEaddress,editEmobile,editEemail,editEcompanyname,editEdate,editEpassword;

    Button btnEedit;

    String lid,emp_name, emp_address, emp_mobile,emp_email,emp_date, id,emp_id,emp_password;
    // int mobile;
    SafalmAdmin sa;
    private String TAG = EditEmployeeDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_employee_detail_activity);
        editEname=findViewById(R.id.editEname);
        editEaddress=findViewById(R.id.editEaddress);
        editEmobile=findViewById(R.id.editEmobile);
        editEemail=findViewById(R.id.editEemail);
       // editEdate=findViewById(R.id.editEdate);
        editEpassword=findViewById(R.id.editEpassword);


        btnEedit=findViewById(R.id.btnEedit);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            emp_id = (String) b.get("emp_id");
            emp_name = (String) b.get("emp_name");
            emp_address = (String) b.get("emp_address");
            emp_mobile = (String) b.get("emp_mobile");
            emp_email = (String) b.get("emp_email");
          //  emp_date = (String) b.get("emp_date");
            emp_password = (String) b.get("emp_password");
            //txtSLDname.setText(lead_id);
        }
        editEaddress.setText(emp_address);
        editEemail.setText(emp_email);
        editEmobile.setText(emp_mobile);
        editEname.setText(emp_name);
        editEpassword.setText(emp_password);

        sa=(SafalmAdmin) getApplicationContext();
        emp_id=sa.getempId();


        btnEedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
                    //   lid = editEid.getText().toString().trim();
                  //  emp_date = java.net.URLEncoder.encode(date_time, "UTF-8");
                    emp_name = java.net.URLEncoder.encode(editEname.getText().toString().trim(), "UTF-8");
                    emp_address = java.net.URLEncoder.encode(editEaddress.getText().toString().trim(), "UTF-8");
                    emp_mobile = editEmobile.getText().toString().trim();
                    emp_email=editEemail.getText().toString().trim();
                    emp_password=editEpassword.getText().toString().trim();
                    // mobile=Integer.valueOf(lmobile);
                } catch (UnsupportedEncodingException e) {

                }

                if (!emp_name.equals("") && !emp_address.equals("") && !emp_mobile.equals("") && !emp_email.equals("") && !emp_password.equals("")) {

                    if (emp_mobile.length() == 10 ) {

                        if (emp_email.matches(emailPattern))
                        {

                           // Toast.makeText(EditEmployeeDetailActivity.this, "hello", Toast.LENGTH_SHORT).show();
                            url = "http://10.0.2.2/safalm_admin/edit_employee.php?emp_id="+emp_id+"&emp_name=" + emp_name + "&emp_address=" + emp_address + "&emp_mobile=" + emp_mobile + "&emp_password=" + emp_password + "&emp_email=" + emp_email ;
                            new EditEmployeeDetailActivity.Employee().execute();

                        }else {
                            Toast.makeText(EditEmployeeDetailActivity.this, "Enter Valid Email Address...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(EditEmployeeDetailActivity.this, "Enter valid Mobile Number..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(EditEmployeeDetailActivity.this, "All field are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditEmployeeDetailActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            if (contacts.get("success").toString().equals("1")) {

                Toast.makeText(EditEmployeeDetailActivity.this, "Data Updated successfully...", Toast.LENGTH_SHORT).show();
//                editEid.setText("");
//                editEemail.setText("");
//                editEname.setText("");
//                editEaddress.setText("");
//                editEmobile.setText("");
//                editEpassword.setText("");
                startActivity(new Intent(EditEmployeeDetailActivity.this,EmployeeDetailActivity.class));
                finish();
//                editEdate.setText("");

            }
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

                    String success = jsonObj.getString("success");
                    String message = jsonObj.getString("message");
                    Log.e("SAFALM3=", success);
                    Log.e("SAFALM4=", message);

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
