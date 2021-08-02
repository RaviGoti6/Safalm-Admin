package com.example.raju.safalmadmin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class LeadListActivity extends AppCompatActivity {

    ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lead_list_activity);

        list_item=findViewById(R.id.list_item);
    }
}
