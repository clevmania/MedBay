package com.clevmania.medbay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clevmania.medbay.R;
import com.clevmania.medbay.model.MedicationsModel;

import java.util.List;

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
        holder.medicName.setText(medicModel.getName());
        holder.medicDescription.setText(medicModel.getDesc());
        holder.medicDosage.setText(medicModel.getDosage());
        holder.medicInterval.setText(medicModel.getInterval());
        holder.startDate.setText(medicModel.getStart_date());
        holder.endDate.setText(medicModel.getEnd_date());
    }

    @Override
    public int getItemCount() {
        return medicationsModels.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView medicName, medicDescription, medicDosage, medicInterval, startDate, endDate;

        public MedicationViewHolder(View itemView) {
            super(itemView);
            medicName = itemView.findViewById(R.id.tv_name_of_medication);
            medicDescription = itemView.findViewById(R.id.tv_description_of_medication);
            medicDosage = itemView.findViewById(R.id.tv_dosage);
            medicInterval = itemView.findViewById(R.id.tv_interval);
            startDate = itemView.findViewById(R.id.tv_start_date);
            endDate = itemView.findViewById(R.id.tv_end_date);
        }
    }

}