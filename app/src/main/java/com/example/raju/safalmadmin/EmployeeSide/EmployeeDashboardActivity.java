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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class EmployeeDashboardActivity extends AppCompatActivity {

    SafalmAdmin se;

    private String TAG = EmployeeDashboardActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";

    TextView txtEmpUname;
    String eid;
    HashMap<String, String> contacts;

    ListView lstTask;
    String currentDate, emp_name, email = "rajesh3233@gmai.com";

    ListView list;

    String empid, addr, num, tid;
    ListAdapter adptr;

    AsyncTask at;
    int flag = 0;

    ArrayList<HashMap<String, String>> leadList;

    //private static String TAG_SLID = "task_id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_dashboard_activity);

        LinearLayout ic1 = findViewById(R.id.ic1);
        LinearLayout ic2 = findViewById(R.id.ic2);
        LinearLayout ic3 = findViewById(R.id.ic3);
        LinearLayout ic4 = findViewById(R.id.ic4);
        LinearLayout ic5 = findViewById(R.id.ic5);
        LinearLayout ic6 = findViewById(R.id.ic6);
        txtEmpUname = findViewById(R.id.txtEmpUname);

        se = (SafalmAdmin) getApplicationContext();
        eid = se.getempId();
        //Toast.makeText(getApplicationContext(), "Id="+eid, Toast.LENGTH_SHORT).show();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(c);

        lstTask = findViewById(R.id.lstTask);
        lstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) lstTask.getAdapter().getItem(position);
                String task_id = (String) obj.get("task_id");
                String lead_typ = (String) obj.get("lead_type");
                addr = (String) obj.get(TAG_SLADDRESS);
                num = (String) obj.get(TAG_SLCONTACT);
                //   email = (String) obj.get("email");
                String t_typ = (String) obj.get("task_type");
                // Toast.makeText(EmployeeDashboardActivity.this, "task:"+t_typ, Toast.LENGTH_SHORT).show();

                //Toast.makeText(TaskListActivity.this, "Id=" + task_id, Toast.LENGTH_SHORT).show();
                if (t_typ.equals("Call")) {
                    dialContactPhone(num);
                }
                if (t_typ.equals("SMS")) {
                    openSMS();
                }
                if (t_typ.equals("Mail")) {
                    openMail();
                }
                if (t_typ.equals("WhatsApp")) {
                    openWhatsApp();
                }

            }
        });

        leadList = new ArrayList<>();

        url = "http://10.0.2.2/safalm/get_emp_from_id.php?id=" + eid + "&cur_date=" + currentDate;

        new Employee().execute();

        ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeDashboardActivity.this, EmployeeLeadActivity.class);
                startActivity(i);
            }
        });

        ic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeDashboardActivity.this, EmployeeTargetListActivity.class);
                startActivity(i);
            }
        });

        ic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeDashboardActivity.this, EmployeeTaskListActivity.class));

            }
        });

        ic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeDashboardActivity.this, EmployeeProductListMainActvity.class));
            }
        });

        ic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeDashboardActivity.this, EmployeeLeadTransferActivity.class));

            }
        });

        ic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeDashboardActivity.this, EmployeeOpportunityListActivity.class));
            }
        });


    }

    private void openSMS() {
        Uri sms_uri = Uri.parse("smsto:" + num);
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        //sms_intent.putExtra("sms_body", "Good Morning ! how r U ?");
        startActivity(sms_intent);
    }

    private void openMail() {
//        Log.i("Send email", "");
//        String[] TO = {email};
////                    String[] CC = {
////                            "ramdurai25@gmail.com"
////                    };
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        // emailIntent.putExtra(Intent.EXTRA_CC, CC);
//        //emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//        //emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
//            //  Log.i("Finished sending email...", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void openWhatsApp() {
        String smsNumber = "91:" + num;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        } else {
            //  Uri uri = Uri.parse("market://details?id=com.whatsapp");
            //  Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            //  startActivity(goToMarket);
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
            pDialog = new ProgressDialog(EmployeeDashboardActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            for (int i = 0; i < leadList.size(); i++) {
                HashMap<String, String> hashmap = leadList.get(0);
                emp_name = hashmap.get("emp_name");
            }

            txtEmpUname.setText("Welcome " + emp_name);
            if (tid == null) {
                Toast.makeText(EmployeeDashboardActivity.this, "Task id: " + tid, Toast.LENGTH_SHORT).show();
            } else {
                adptr = new SimpleAdapter(EmployeeDashboardActivity.this, leadList, R.layout.task_list_item_dashboard, new String[]{"task_id", TAG_SLNAME, TAG_SLCONTACT, "task_type"}, new int[]{R.id.txtTLIid, R.id.txtTLIname, R.id.txtTLImobile, R.id.txtTLItasktype}) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);

                        TextView tasktype = v.findViewById(R.id.txtTLItasktype);
                        ImageView img = v.findViewById(R.id.imgTLIcall);

                        if (tasktype.getText().toString().equals("Call")) {
                            img.setImageResource(R.drawable.call);
                        }
                        if (tasktype.getText().toString().equals("SMS")) {
                            img.setImageResource(R.drawable.sms);
                        }

                        if (tasktype.getText().toString().equals("Mail")) {
                            img.setImageResource(R.drawable.mail);
                        }

                        if (tasktype.getText().toString().equals("WhatsApp")) {
                            img.setImageResource(R.drawable.whatsapp);
                        }
                        return v;
                    }
                };
                lstTask.setAdapter(adptr);
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
//                    Log.e("SAFALM2=", contacts.toString());
                    // JSONArray contacts = new JSONArray(jsonStr);

                    if (jsonObj.getString("success").equals("1")) {
                        for (int i = 0; i < self_lead.length(); i++) {
                            JSONObject c = self_lead.getJSONObject(i);
                            //TAG_SLID = "0";
                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            Log.e("SAFALM33=", success);
                            Log.e("SAFALM44=", message);

                            String emp_name = c.getString("emp_name");

                            HashMap<String, String> map = new HashMap<>();
                            map.put("emp_name", emp_name);
                            leadList.add(map);
                        }
                    } else {
                        // looping through All Contacts
                        for (int i = 0; i < self_lead.length(); i++) {
                            JSONObject c = self_lead.getJSONObject(i);

                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            // Log.e("SAFALM3=", success);
                            //Log.e("SAFALM4=", message);
                            //Toast.makeText(getApplicationContext(), success + message, Toast.LENGTH_SHORT).show();
                            tid = c.getString("task_id");
                            String emp_name = c.getString("emp_name");
                            String name = c.getString(TAG_SLNAME);
                            String address = c.getString(TAG_SLADDRESS);
                            String contact = c.getString(TAG_SLCONTACT);
                            String lead_type = c.getString("lead_type");
                            String datetime = c.getString("task_date");
                            String tasktype = c.getString("task_type");

                            HashMap<String, String> map = new HashMap<>();

                            StringTokenizer tokens = new StringTokenizer(datetime, " ");
                            String date = tokens.nextToken();// this will contain "Fruit"
                            String time = tokens.nextToken();

                            map.put("task_id", tid);
                            map.put(TAG_SLNAME, name);
                            map.put(TAG_SLADDRESS, address);
                            map.put(TAG_SLCONTACT, contact);
                            map.put("lead_type", lead_type);
                            map.put("emp_name", emp_name);
                            map.put("date", date);
                            map.put("time", time);
                            map.put("task_type", tasktype);

                            leadList.add(map);
                        }
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

