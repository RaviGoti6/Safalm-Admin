package com.example.raju.safalmadmin.EmployeeSide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.raju.safalmadmin.R;
import com.example.raju.safalmadmin.SafalmAdmin;

public class EmployeeLeadActivity extends AppCompatActivity {

    SafalmAdmin sa;
    String emp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_lead_activity);

        LinearLayout lead_ic1 = findViewById(R.id.lead_ic1);
        LinearLayout lead_ic2 = findViewById(R.id.lead_ic2);
        LinearLayout lead_ic3 = findViewById(R.id.lead_ic3);
        LinearLayout lead_ic4 = findViewById(R.id.lead_ic4);
        LinearLayout lead_ic5 = findViewById(R.id.lead_ic5);
        LinearLayout lead_ic6 = findViewById(R.id.lead_ic6);

      //  sa=(SafalmAdmin) getApplicationContext();
       // emp_id=sa.getempId();

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            emp_id = (String) b.get("emp_id");
           // name = (String) b.get("emp_name");
        }


        lead_ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(EmployeeLeadActivity.this,EmployeeSelfLeadListActivity.class);
                i.putExtra("emp_id",emp_id);
                startActivity(i);

            }
        });

        lead_ic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeLeadActivity.this,EmployeeCompanyLeadListActivity.class));

            }
        });

        lead_ic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeLeadActivity.this,EmployeeSelfVisitedLeadListActivity.class));

            }
        });

        lead_ic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeLeadActivity.this,EmployeeCompanyVisitedLeadListActivity.class));

            }
        });

        lead_ic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EmployeeLeadActivity.this,EmployeeSelfSalesLeadListActivity.class));
            }
        });

        lead_ic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(EmployeeLeadActivity.this,EmployeeCompanySalesLeadListActivity.class));

            }
        });
    }
}
