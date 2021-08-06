package com.example.raju.safalmadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raju.safalmadmin.EmployeeSide.EmployeeAddSelfLeadToSalesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class AdminReportActivity extends AppCompatActivity {

    ProgressBar prgCSales, prgSSales, prgCLead, prgSLead, prgCProduct, prgSProduct;
    Button btnToDate, btnFromDate;
    int mDate, mMonth, mYear, mMinute, mHour;
    String date_time, dt, tm, date_from, date_to, self_p_qn, comp_p_qn, self_visited, comp_visited, self_sales, comp_sales;

    SafalmAdmin sa;

    String empid, name;
    TextView txtSelfPS,txtCompPS,txtSelfSS,txtSelfVS,txtCompVS,txtCompSS,txtToDate,txtFromDate;
    ListAdapter adptr;

    AsyncTask at;
    int flag = 0;

    private String TAG = AdminReportActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_report_activity);

        prgCLead = findViewById(R.id.prgCLead);
        prgCProduct = findViewById(R.id.prgCProduct);
        prgCSales = findViewById(R.id.prgCSales);
        prgSLead = findViewById(R.id.prgSLead);
        prgSSales = findViewById(R.id.prgSSales);
        prgSProduct = findViewById(R.id.prgSProduct);
        btnFromDate = findViewById(R.id.btnFromDate);
        btnToDate = findViewById(R.id.btnToDate);
        txtCompPS = findViewById(R.id.txtCompPS);
        txtSelfPS = findViewById(R.id.txtSelfPS);
        txtSelfVS = findViewById(R.id.txtSelfVS);
        txtCompVS = findViewById(R.id.txtCompVS);
        txtSelfSS = findViewById(R.id.txtSelfSS);
        txtCompSS = findViewById(R.id.txtCompSS);
        txtToDate = findViewById(R.id.txtToDate);
        txtFromDate = findViewById(R.id.txtFromDate);

        date_from = "2019-01-01";
        date_to = "2020-01-01";

        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        date_from = ((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
                        txtFromDate.setText(date_from);
                        Toast.makeText(AdminReportActivity.this, date_from, Toast.LENGTH_SHORT).show();
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDate = c.get(Calendar.DATE);
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        date_to = ((year + "-" + (((month + 1) < 10) ? "0" + (month + 1) : (month + 1)) + "-" + ((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth)));
                        txtToDate.setText(date_to);
                        Toast.makeText(AdminReportActivity.this, date_to, Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                }, mYear, mMonth, mDate);
                datePickerDialog.show();
            }
        });
        url = "http://10.0.2.2/safalm_admin/self_report_product.php?f_date=" + date_from + "&t_date=" + date_to;
        // list.setAdapter(null);
        at = new AdminReportActivity.Employee().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        url = "http://10.0.2.2/safalm_admin/self_report_product.php?f_date=" + date_from + "&t_date=" + date_to;
        // list.setAdapter(null);
        at = new AdminReportActivity.Employee().execute();
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminReportActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
         //   pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            pDialog.dismiss();
//            prgSProduct.setProgress(Integer.parseInt(self_p_qn));
//            prgCProduct.setProgress(Integer.parseInt(comp_p_qn));
//            prgCLead.setProgress(Integer.parseInt(comp_visited));
//            prgSLead.setProgress(Integer.parseInt(self_visited));
//            prgSSales.setProgress(Integer.parseInt(self_sales));
//            prgCSales.setProgress(Integer.parseInt(comp_sales));

            txtSelfPS.setText(self_p_qn);
            txtCompPS.setText(comp_p_qn);
            txtSelfVS.setText(self_visited);
            txtCompVS.setText(comp_visited);
            txtSelfSS.setText(self_sales);
            txtCompSS.setText(comp_sales);
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
                    JSONObject o = self_lead.getJSONObject(0);
                    Log.e("Ravi", String.valueOf(self_lead));
                  //  for (int i = 0; i < self_lead.length(); i++) {

                        comp_p_qn = o.getString("comp_product_saled");
                        self_p_qn = o.getString("self_product_saled");
                        self_visited = o.getString("self_visited_quantity");
                        comp_visited = o.getString("comp_visited_quantity");
                        comp_sales = o.getString("comp_sales_quantity");
                        self_sales = o.getString("self_sales_quantity");
//                        Log.e("SAFALM2=", contacts.toString());
                        // JSONArray contacts = new JSONArray(jsonStr);

                        // looping through All Contacts
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
