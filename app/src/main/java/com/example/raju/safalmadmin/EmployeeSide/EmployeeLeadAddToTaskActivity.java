package com.example.raju.safalmadmin.EmployeeSide;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.MyBroadcast;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class EmployeeLeadAddToTaskActivity extends AppCompatActivity {

    EditText editLATTdate, editLATTtime, editLATTdetail, editTaskType;
    ImageView imgTaskType;

    TextView txtLATTname, txtLATTaddress, txtLATTmobile, txtLATTemail, txtLATTcompanyname;

    String emp_id, slid = "0", vlid = "0", clid = "0", cvlid = "0", lname, laddress, lmobile, lemail, lcname, task_type, detail, cmpTask = "0";
    HashMap<String, String> contacts;

    SafalmAdmin se;
    private String TAG = EmployeeLeadAddToTaskActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    int flag = 0;

    AsyncTask at;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm;

    String[] spinnerData = new String[]{"WhatsApp to lead", "Call to Lead", "SMS to Lead", "Mail to Lead"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_lead_add_to_task_activity);

        txtLATTname = findViewById(R.id.txtLATTname);
        txtLATTaddress = findViewById(R.id.txtLATTaddress);
        txtLATTmobile = findViewById(R.id.txtLATTmobile);
        txtLATTemail = findViewById(R.id.txtLATTemail);
        txtLATTcompanyname = findViewById(R.id.txtLATTcompanyname);

        editLATTdate = findViewById(R.id.editLATTdate);
        editLATTtime = findViewById(R.id.editLATTtime);
        editLATTdetail = findViewById(R.id.editLATTdetail);
//        editTaskType=findViewById(R.id.editTaskType);

//        imgTaskType=findViewById(R.id.imgTaskType);
//
//        imgTaskType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LeadAddToTaskActivity.this, LeadTaskTypeListActivity.class);
//                startActivityForResult(i, 1);
//
//            }
//        });

        final Spinner spinnerLATTtasktype = findViewById(R.id.spinnerLATTtasktype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLATTtasktype.setAdapter(adapter);

        spinnerLATTtasktype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task_type = spinnerLATTtasktype.getSelectedItem().toString();
               // Toast.makeText(EmployeeLeadAddToTaskActivity.this, "task:" + task_type, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button btnLATTadd = findViewById(R.id.btnLATTadd);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            slid = (String) b.get("slid");
            vlid = (String) b.get("vlid");
            lname = (String) b.get("name");
            laddress = (String) b.get("address");
            lmobile = (String) b.get("mobile");
            lemail = (String) b.get("email");
            lcname = (String) b.get("cmpName");
            cmpTask = (String) b.get("cmpTask");
            clid = (String) b.get("clid");
            cvlid = (String) b.get("cvlid");

            //txtSLDname.setText(lead_id);
        }


        txtLATTname.setText(lname);
        txtLATTaddress.setText(laddress);
        txtLATTmobile.setText(lmobile);
        txtLATTemail.setText(lemail);
        txtLATTcompanyname.setText(lcname);

        se = (SafalmAdmin) getApplicationContext();
        //emp_id = "1";
        emp_id = se.getempId();

        editLATTdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeLeadAddToTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editLATTdate.setText((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));

                        // editASLTVSdatetime.setText(date_time);
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        editLATTtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c1 = Calendar.getInstance();
                mHour = c1.get(Calendar.HOUR);
                mMinute = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EmployeeLeadAddToTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // editASLTVSdatetime.setText(date_time + (" "+hourOfDay + ":" + minute));
                        editLATTtime.setText((((hourOfDay < 10) ? "0" + hourOfDay : hourOfDay) + ":" + ((minute < 10) ? "0" + minute : minute) + ":00"));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnLATTadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = editLATTdate.getText().toString();
                tm = editLATTtime.getText().toString();
                date_time = dt + " " + tm;

                StringTokenizer tokens = new StringTokenizer(task_type, " ");
                task_type = tokens.nextToken();

                try {
                    //startActivity(new Intent(AddSelfLeadActivity.this, SelfLeadDetailActivity.class));
                    date_time = java.net.URLEncoder.encode(date_time, "UTF-8");
                    detail = java.net.URLEncoder.encode(editLATTdetail.getText().toString(), "UTF-8");

                    // mobile=Integer.valueOf(lmobile);
                } catch (UnsupportedEncodingException e) {

                }

                if (!date_time.equals("") && !detail.equals("")) {

                    StringTokenizer s1 = new StringTokenizer(dt, "-");
                    int year = Integer.parseInt(s1.nextToken());
                    int month = Integer.parseInt(s1.nextToken());
                    int day = Integer.parseInt(s1.nextToken());


                    StringTokenizer s2 = new StringTokenizer(tm, ":");
                    int hour = Integer.parseInt(s2.nextToken());
                    int min = Integer.parseInt(s2.nextToken());


                    Calendar current = Calendar.getInstance();

                    Calendar cal = Calendar.getInstance();
                    cal.set(year,
                            month - 1,
                            day,
                            hour,
                            min, 00);

                    if (cal.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getApplicationContext(),
                                "Invalid Date/Time",
                                Toast.LENGTH_LONG).show();
                    } else {
                        url = "http://10.0.2.2/safalm_admin/employee_lead_add_to_task.php?sl_id=" + slid + "&date=" + date_time + "&emp_id=" + emp_id + "&task_type=" + task_type + "&task_detail=" + detail + "&cl_id=" + clid + "&svl_id=" + vlid + "&cvl_id=" + cvlid + "&task_cmp_task=" + cmpTask;
                        new EmployeeLeadAddToTaskActivity.Employee().execute();
                        setReminder(cal, current);
                    }

                } else {
                    Toast.makeText(EmployeeLeadAddToTaskActivity.this, "All Fields are required...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setReminder(Calendar time,Calendar current) {
        // info.setText("***\n" + "Alarm is set@ " + targetCal.getTime() + "\n" + "***\n");
        Log.d("Ravi", String.valueOf(time));
        Log.d("Sonu Ravi", String.valueOf(current));

        Intent intent = new Intent(EmployeeLeadAddToTaskActivity.this, MyBroadcast.class);
        intent.putExtra("Lead_name",lname);
        intent.putExtra("task_type",task_type);
        intent.putExtra("number",lmobile);
        intent.putExtra("email",lemail);
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EmployeeLeadAddToTaskActivity.this, id, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && data.getExtras().containsKey("task_type")) {
//
//
//            task_type = data.getExtras().getString("task_type");
//
//            editTaskType.setText(task_type);
//            //Toast.makeText(AddSelfLeadToVisitedAndSalesActivity.this, "id=" + pid, Toast.LENGTH_SHORT).show();
//
//            //Toast.makeText(AddSelfLeadToVisitedAndSalesActivity.this, "p_id=" + p_id, Toast.LENGTH_SHORT).show();
//        }
//    }
    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeLeadAddToTaskActivity.this);
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


            if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
                //startActivity(i);
//                finish();
                Toast.makeText(EmployeeLeadAddToTaskActivity.this, "Task added successfully...", Toast.LENGTH_SHORT).show();
                editLATTdate.setText("");
                editLATTdetail.setText("");
                editLATTtime.setText("");
                //    editTaskType.setText("");

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

                    // Getting JSON Array node
//                    JSONArray contacts = jsonObj.getJSONArray();
//                    Log.e("SAFALM2=", contacts.toString());
                    //JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    //for (int i = 0; i < contacts.length(); i++) {
//                        JSONObject c = contacts.getJSONObject(i);

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
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);

                    // adding contact to contact list
                    //contactList.add(contact);
                    // }
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
