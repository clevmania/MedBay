package com.clevmania.medbay.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clevmania.medbay.R;
import com.clevmania.medbay.model.MedicationsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by grandilo-lawrence on 4/8/18.
 */

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>{
    private List<MedicationsModel> medicationsModels;

    public MedicationAdapter(List<MedicationsModel> medicationsModels) {
        this.medicationsModels = medicationsModels;
    }

    @Override
    public MedicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_list,parent,false);
        return new MedicationViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(MedicationViewHolder holder, int position) {
        MedicationsModel medicModel = medicationsModels.get(position);
        holder.medicName.setText(medicModel.getTitle());
        holder.medicDescription.setText(medicModel.getDesc());
        holder.medicDosage.setText(String.format("%s daily",medicModel.getDosage()));
        holder.medicInterval.setText(medicModel.getInterval());
        holder.startDate.setText(medicModel.getStart_date());
        holder.endDate.setText(medicModel.getEnd_date());
        holder.duration.setText(durationOfMedication(medicModel.getEnd_date()));
    }

    @Override
    public int getItemCount() {
        return medicationsModels.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView medicName, medicDescription, medicDosage, medicInterval, startDate, endDate, duration;

        private MedicationViewHolder(View itemView) {
            super(itemView);
            medicName = itemView.findViewById(R.id.tv_name_of_medication);
            medicDescription = itemView.findViewById(R.id.tv_description_of_medication);
            medicDosage = itemView.findViewById(R.id.tv_dosage);
            medicInterval = itemView.findViewById(R.id.et_interval);
            startDate = itemView.findViewById(R.id.et_start_date);
            endDate = itemView.findViewById(R.id.et_end_date);
            duration = itemView.findViewById(R.id.tv_duration);
        }
    }

    private String durationOfMedication(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date finishDate , currentDate;
        String noOfDays = "";

        try {
            finishDate=formatter.parse(date);
            currentDate = formatter.parse(formatter.format(System.currentTimeMillis()));
            long daysInMills = Math.abs(finishDate.getTime() - currentDate.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(daysInMills);
            noOfDays = String.format("%s \n days left",String.valueOf(days));

            if(finishDate.after(currentDate)){
                noOfDays = "done";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return noOfDays;
    }


}
