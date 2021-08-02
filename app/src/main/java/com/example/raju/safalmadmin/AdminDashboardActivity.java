package com.example.raju.safalmadmin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout emp_list, emp_work, lead_list, report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard_activity);

        emp_list = findViewById(R.id.ic1);
        emp_work = findViewById(R.id.ic2);
        lead_list = findViewById(R.id.ic3);
        report = findViewById(R.id.ic4);
        checkPermission();
        emp_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, EmployeeListMainActivity.class));
            }
        });

        emp_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, EmployeeListActivity.class));
            }
        });

        lead_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminDashboardActivity.this, LeadActivity.class));
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, AdminReportActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission();
    }

    private boolean checkPermission() {
        int i = 0;
        String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> reqPerm = new ArrayList<>();

        for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        if (i == 1)
            return true;
        else
            return requestPermission(reqPerm);
    }

    private boolean requestPermission(List<String> perm) {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] listReq = new String[perm.size()];
        listReq = perm.toArray(listReq);
        for (String permissions : listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions)) {
                Toast.makeText(this, "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }
        ActivityCompat.requestPermissions(this, listReq, 1);
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG);
                else
                    Toast.makeText(this, "You can't access Phone Storage", Toast.LENGTH_LONG);
                break;
        }
    }
}
