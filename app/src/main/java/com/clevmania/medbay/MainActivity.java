package com.clevmania.medbay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.clevmania.medbay.adapter.MedicationAdapter;
import com.clevmania.medbay.model.MedicationsModel;

public class MainActivity extends AppCompatActivity {
    private RecyclerView medicationList;
    private MedicationAdapter medicationAdapter;
    private MedicationsModel medicationsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        medicationList = findViewById(R.id.rv_medication_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medicationList.setLayoutManager(layoutManager);
        medicationList.setHasFixedSize(true);
    }

    private void retrieveMedications(){
        
    }
}
