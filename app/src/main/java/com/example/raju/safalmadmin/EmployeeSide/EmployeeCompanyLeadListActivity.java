package com.example.raju.safalmadmin.EmployeeSide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import com.example.raju.safalmadmin.FileChooser;
import com.example.raju.safalmadmin.HttpHandler;
import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class EmployeeCompanyLeadListActivity extends AppCompatActivity {

    String empid;
    ListAdapter adptr;
    String TAG1 = "main";
    private TextView textView;
    private ProgressDialog progressDialog;
    ListView list;
    AsyncTask at;
    int flag = 0;
    SafalmAdmin sa;
    private String TAG = EmployeeCompanyLeadListActivity.class.getSimpleName();

    ImageView imgUploadExcell;

    private ProgressDialog pDialog;
    private static String url = "";
    HashMap<String, String> contacts;

    ArrayList<HashMap<String, String>> leadList;

    private static final String TAG_SLID = "id";
    private static final String TAG_SLNAME = "name";
    private static final String TAG_SLADDRESS = "address";
    private static final String TAG_SLCONTACT = "mobile";

    LinearLayout icAddLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_company_lead_list_activity);

        list = findViewById(R.id.listCompanyLeadList);
        icAddLead = findViewById(R.id.icAddLead);

        imgUploadExcell = findViewById(R.id.imgUploadExcell);

        imgUploadExcell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FileChooser(EmployeeCompanyLeadListActivity.this, new String[]{"xls", "xlsx"})
                        .setFileListener(new FileChooser.FileSelectedListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void fileSelected(File file) {
                                progressDialog = new ProgressDialog(EmployeeCompanyLeadListActivity.this);
                                progressDialog.setTitle("Lead Inserting...");
                                progressDialog.setMessage("Please Wait...");
                                progressDialog.setIndeterminate(true);
                                progressDialog.setCanceledOnTouchOutside(false);

                                Toast.makeText(EmployeeCompanyLeadListActivity.this, file.getName(), Toast.LENGTH_SHORT).show();
                                String filePath = file.getAbsolutePath();

                                progressDialog.show();
                                if (readExcelFileFromAssets(filePath)) {
                                    progressDialog.dismiss();
                                    finish();
                                }
                            }
                        })
                        .showDialog();
            }
        });

        icAddLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeCompanyLeadListActivity.this, EmployeeAddCompanyLeadActivity.class));
            }
        });

        sa = (SafalmAdmin) getApplicationContext();
        empid = sa.getempId();

        leadList = new ArrayList<>();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, Object> obj = (HashMap<String, Object>) list.getAdapter().getItem(position);
                String lead_id = (String) obj.get("id");

                //Toast.makeText(EmployeeCompanyLeadListActivity.this, "Id=" + lead_id, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(EmployeeCompanyLeadListActivity.this, EmployeeCompanyLeadDetailActivity.class);
                i.putExtra("lead_id", lead_id);
                startActivity(i);
            }
        });

        url = "http://10.0.2.2/safalm_admin/employee_company_lead_list.php?emp_id=" + empid;
        // list.setAdapter(null);
        at = new Employee().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag > 0) {
            list = findViewById(R.id.listCompanyLeadList);
            url = "http://10.0.2.2/safalm_admin/employee_company_lead_list.php?emp_id=" + empid;
            leadList.clear();
            list.setAdapter(null);

            at = new Employee().execute();
        }
    }

    public boolean readExcelFileFromAssets(String filePath) {
        try {
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel sheet
//            String[] fname=this.getAssets().list("");
//            for (int i=0;i<fname.length;i++){
//            Toast.makeText(this, "hello:"+fname[i], Toast.LENGTH_LONG).show();
//            Log.e("Excell",fname[i]);}

            File file = new File(filePath);
            InputStream is = new FileInputStream(file);


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            // myInput = assetManager.open("Book1.xls");

            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(is);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;
//            textView.append("\n");
//            textView.append(  "name  --  mobile   --  address -- \n");
            while (rowIter.hasNext()) {
                Log.e(TAG1, " row no " + rowno);
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String name = "", mobile = "", address = "", c_name = "", email = "", date = "";
                    long lonVal = 0;
                    Double d = null;
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            name = myCell.toString();
                        } else if (colno == 1) {
                            mobile = myCell.toString();

                            Double e1Val = myCell.getNumericCellValue();
                            BigDecimal bd = new BigDecimal(e1Val.toString());
                            lonVal = bd.longValue();
                            mobile = String.valueOf(lonVal);
                            // d=Double.parseDouble(mobile);
                        } else if (colno == 2) {
                            address = myCell.toString();
                        } else if (colno == 3) {
                            c_name = myCell.toString();
                        } else if (colno == 4) {
                            email = myCell.toString();
                        }
//                        else if (colno==5){
//                            date = myCell.toString();
//                        }
                        colno++;
                        Log.e(TAG, " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());
                        Log.e("Name:", name);
                        //  Log.e("Mobile:",d.toString());
                        Log.e("Address:", address);
                        Log.e("Comp_name:", c_name);
                        Log.e("Email:", email);
                    }
                    url = "http://10.0.2.2/safalm_admin/add_comp_lead.php?lname=" + name + "&laddress=" + address + "&lmobile=" + mobile + "&emp_id=" + empid + "&lemail=" + email + "&lcompany_name=" + c_name + "&ldate=" + currentDateandTime;
                    new EmployeeCompanyLeadListActivity.Employee().execute();
                    //  textView.append( name + " -- "+ mobile+ "  -- "+ address+"   --  "+ c_name+"  --   "+ email+"\n");
                }
                rowno++;
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "error " + e.toString());
            return false;
        }
    }

    private class Employee extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EmployeeCompanyLeadListActivity.this);
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

            adptr = new SimpleAdapter(EmployeeCompanyLeadListActivity.this, leadList, R.layout.self_lead_list_item, new String[]{TAG_SLID, TAG_SLNAME, TAG_SLADDRESS, TAG_SLCONTACT, "s_task", "s_visited", "s_sales"}, new int[]{R.id.txtSLLIid, R.id.txtSLLIname, R.id.txtSLLIaddress, R.id.txtSLLImobile, R.id.s_task, R.id.s_visited, R.id.s_sales}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);

                    LinearLayout l_task = v.findViewById(R.id.icStatusTask);
                    LinearLayout l_visited = v.findViewById(R.id.icStatusVisit);
                    LinearLayout l_sales = v.findViewById(R.id.icStatusSales);
                    TextView t_task = v.findViewById(R.id.s_task);
                    TextView t_visited = v.findViewById(R.id.s_visited);
                    TextView t_sales = v.findViewById(R.id.s_sales);
                    // l_task.setBackgroundColor(Color.parseColor("#000000"));
                    if (t_task.getText().toString().equals("T")) {
                        l_task.setBackground(getResources().getDrawable(R.drawable.task_status));
                    } else {
                        l_task.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }
                    if (t_visited.getText().toString().equals("V")) {
                        l_visited.setBackground(getResources().getDrawable(R.drawable.visited_status));
                    } else {
                        l_visited.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }
                    if (t_sales.getText().toString().equals("S")) {
                        l_sales.setBackground(getResources().getDrawable(R.drawable.sales_status));
                    } else {
                        l_sales.setBackground(getResources().getDrawable(R.drawable.status_none));
                    }

                    return v;
                }
            };
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
                        String s_task = c.getString("lead_task_status");
                        String s_visit = c.getString("lead_visited_status");
                        String s_sales = c.getString("lead_sales_status");

                        HashMap<String, String> map = new HashMap<>();

                        map.put(TAG_SLID, id);
                        map.put(TAG_SLNAME, name);
                        map.put(TAG_SLADDRESS, address);
                        map.put(TAG_SLCONTACT, contact);
                        map.put("s_task", s_task);
                        map.put("s_visited", s_visit);
                        map.put("s_sales", s_sales);

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
