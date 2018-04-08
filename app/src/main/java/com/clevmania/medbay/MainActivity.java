package com.clevmania.medbay;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.clevmania.medbay.adapter.MedicationAdapter;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.model.MedicationsModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView medicationList;
    private MedicationAdapter medicationAdapter;
    private List<MedicationsModel> medicationsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        medicationsModel = new ArrayList<>();
        medicationList = findViewById(R.id.rv_medication_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medicationList.setLayoutManager(layoutManager);
        medicationList.setHasFixedSize(true);
    }

    private void retrieveMedications(){
        FirebaseUtils.getMedicationsReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MedicationsModel mm = dataSnapshot.getValue(MedicationsModel.class);
                medicationsModel.add(new MedicationsModel(mm.getName(),mm.getDesc(),mm.getInterval(),
                        mm.getStart_date(),mm.getEnd_date(),mm.getDosage()));
                medicationAdapter = new MedicationAdapter(medicationsModel);
                medicationList.setAdapter(medicationAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                MedicationsModel medication = dataSnapshot.getValue(MedicationsModel.class);
                if (medication != null) {
                    int indexOfTopic = medicationsModel.indexOf(medication);
                    if (indexOfTopic != -1) {
                        medicationsModel.remove(medication);
                        medicationAdapter.notifyItemRemoved(indexOfTopic);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                medicationAdapter.notifyDataSetChanged();
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
