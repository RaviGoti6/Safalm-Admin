package com.example.raju.safalmadmin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class LeadActivity extends AppCompatActivity {

    LinearLayout lead_ic1,visited_ic2,sales_ic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lead_activity);

        lead_ic1=findViewById(R.id.ic1);
        visited_ic2=findViewById(R.id.ic2);
        sales_ic3=findViewById(R.id.ic3);

        lead_ic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeadActivity.this,MainLeadListActivity.class));
            }
        });
        visited_ic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeadActivity.this,MainVisitedLeadListActivity.class));
            }
        });

        sales_ic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LeadActivity.this,MainSalesLeadListActivity.class));
            }
        });
    }
}
