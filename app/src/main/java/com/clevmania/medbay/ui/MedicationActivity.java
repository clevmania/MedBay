package com.clevmania.medbay.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.clevmania.medbay.R;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.model.MedicationsModel;
import com.clevmania.medbay.ui.profile.ProfileManager;
import com.clevmania.medbay.utils.UiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MedicationActivity extends AppCompatActivity {
    private EditText title, desc, interval, dosage, startDate, endDate;
    private Button addMedication;
    private DatePickerDialog.OnDateSetListener startDateListener,endDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewMedication();
            }
        });
        pickStartDate();
        pickEndDate();
    }

    private void initViews(){
        title = findViewById(R.id.et_med_title);
        desc = findViewById(R.id.et_description);
        interval = findViewById(R.id.et_interval);
        dosage = findViewById(R.id.et_dosage);
        startDate = findViewById(R.id.et_start_date);
        endDate = findViewById(R.id.et_end_date);
        addMedication = findViewById(R.id.btn_add_med);
    }

    private void saveNewMedication(){
        MedicationsModel model = new MedicationsModel(title.getText().toString(),
                desc.getText().toString(),interval.getText().toString(),
                startDate.getText().toString(),endDate.getText().toString(),
                dosage.getText().toString());

        FirebaseUtils.getMedicReference(new ProfileManager(MedicationActivity.this)
                .getUid(),getMonth()).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UiUtils.showLongSnackBar(addMedication,
                        String.format("%s, has been successfully added",title.getText().toString()));
                clear();
            }
        });

    }

    private void clear(){
        title.getText().clear();
        desc.getText().clear();
        interval.getText().clear();
        dosage.getText().clear();
        startDate.getText().clear();
        endDate.getText().clear();
    }

    private void pickEndDate(){
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog pickerDialog = new DatePickerDialog(MedicationActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,endDateListener,
                        cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                pickerDialog.setTitle("Medication End DATE");
                pickerDialog.show();
                pickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        });
        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yr, int m, int d) {
                endDate.setText(String.format("%d/%d/%d",d,++m,yr));
            }
        };
    }

    private void pickStartDate(){
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                DatePickerDialog pickerDialog = new DatePickerDialog(MedicationActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,startDateListener,
                        cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                pickerDialog.setTitle("Medication Start DATE");
                pickerDialog.show();
                pickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        });
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yr, int m, int d) {
                startDate.setText(String.format("%d/%d/%d",d,++m,yr));
            }
        };
    }

    private String getMonth(){
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.getTime());
    }

}
