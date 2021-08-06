package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EmployeeTaskDetailActivity extends AppCompatActivity {

    TextView txtTDname,txtTDaddress,txtTDmobile,txtTDemail,txtTDcompanyname,txtTDleadtype,txtTDtasktype,txtTDdatetime,txtTDtaskdetail;
    ImageView imgTD,icTDdelete;

    SafalmAdmin se;
    private String TAG = EmployeeTaskDetailActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;

    String task_id,emp_info_id,date_time, date, time;
    String id, name, address, contact, email, compname,task_type,lead_type,lead_typ,task_detail;

    AsyncTask at;

    private static final String TAG_SLID = "self_lead_id";
    private static final String TAG_SLNAME = "self_lead_name";
    private static final String TAG_SLADDRESS = "self_lead_address";
    private static final String TAG_SLCONTACT = "self_lead_mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_task_detail_activity);

        txtTDname = findViewById(R.id.txtTDname);
        txtTDaddress = findViewById(R.id.txtTDaddress);
        txtTDmobile = findViewById(R.id.txtTDmobile);
        txtTDemail = findViewById(R.id.txtTDemail);
        txtTDcompanyname = findViewById(R.id.txtTDcompanyname);
        txtTDleadtype = findViewById(R.id.txtTDleadtype);
        txtTDtasktype = findViewById(R.id.txtTDtasktype);
        txtTDdatetime = findViewById(R.id.txtTDdatetime);
        txtTDtaskdetail = findViewById(R.id.txtTDtaskdetail);

        se = (SafalmAdmin) getApplicationContext();
        emp_info_id = se.getempId();

        imgTD = findViewById(R.id.imgTD);
        icTDdelete = findViewById(R.id.icTDdelete);


        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            task_id = (String) b.get("task_id");
            lead_typ = (String) b.get("lead_type");

        }
        try {
            lead_typ = java.net.URLEncoder.encode(lead_typ, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = "http://10.0.2.2/safalm_admin/employee_task_detail.php?task_id=" + task_id+"&lead_type="+lead_typ;
        at = new EmployeeTaskDetailActivity.Employee().execute();

        imgTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtTDtasktype.getText().toString().equals("Call")) {
                    dialContactPhone(contact);
                }
                if (txtTDtasktype.getText().toString().equals("SMS")) {
                    openSMS();
                }
                if (txtTDtasktype.getText().toString().equals("Mail")) {
                    openMail();
                }
                if (txtTDtasktype.getText().toString().equals("WhatsApp")) {
                    openWhatsApp();
                }

            }
        });
//
        icTDdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://10.0.2.2/safalm/delete_task.php?task_id=" + task_id;
                at = new EmployeeTaskDetailActivity.Employee().execute();
                //Intent i=new Intent(EmployeeTaskDetailActivity.this,EmployeeTaskListActivity.class);
                //startActivity(i);
                finish();

            }
        });

    }

    private void openSMS(){
        Uri sms_uri = Uri.parse("smsto:" + contact);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        //sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
        startActivity(sms_intent);
    }

    private void openMail(){
        Log.i("Send email", "");
        String[] TO = {email};
//                    String[] CC = {
//                            "ramdurai25@gmail.com"
//                    };
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        // emailIntent.putExtra(Intent.EXTRA_CC, CC);
        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            //  Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmployeeTaskDetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void openWhatsApp() {
        String smsNumber = "91:" + contact;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeTaskDetailActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Toast.makeText(SelfLeadDetailActivity.this, "date=" + date_time, Toast.LENGTH_SHORT).show();

            txtTDname.setText(name);
            txtTDaddress.setText(address);
            txtTDmobile.setText(contact);
            txtTDemail.setText(email);
            txtTDcompanyname.setText(compname);
            txtTDleadtype.setText(lead_type);
            txtTDtasktype.setText(task_type);
            txtTDdatetime.setText(date_time);
            txtTDtaskdetail.setText(task_detail);

            if (txtTDtasktype.getText().toString().equals("Call")){
                imgTD.setImageResource(R.drawable.call);
            }
            if (txtTDtasktype.getText().toString().equals("SMS")){
                imgTD.setImageResource(R.drawable.sms);
            }
            if (txtTDtasktype.getText().toString().equals("Mail")){
                imgTD.setImageResource(R.drawable.mail);
            }
            if (txtTDtasktype.getText().toString().equals("WhatsApp")){
                imgTD.setImageResource(R.drawable.whatsapp);
            }

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

                 //   StringTokenizer tokens = new StringTokenizer(date_time, " ");
                //    date = tokens.nextToken();// this will contain "Fruit"
                 //   time = tokens.nextToken();

                    id = c.getString(TAG_SLID);
                    name = c.getString(TAG_SLNAME);
                    address = c.getString(TAG_SLADDRESS);
                    contact = c.getString(TAG_SLCONTACT);
                    email = c.getString("self_lead_email");
                    compname = c.getString("self_lead_comp");
                    date_time = c.getString("task_date");
                    task_type = c.getString("task_type");
                    lead_type = c.getString("lead_type");
                    task_detail = c.getString("task_detail");


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
