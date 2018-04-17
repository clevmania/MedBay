package com.clevmania.medbay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clevmania.medbay.adapter.MedicationAdapter;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.model.MedicationsModel;
import com.clevmania.medbay.ui.MedicationActivity;
import com.clevmania.medbay.ui.auth.SignInActivity;
import com.clevmania.medbay.ui.profile.ProfileManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView medicationList;
    private MedicationAdapter medicationAdapter;
    private List<MedicationsModel> medicationsModel;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAuthenticationState();
    }

    private void setUpFAB(){
        FabSpeedDial fabMenu = findViewById(R.id.fabSpeedDial);
        fabMenu.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_add:
                        // do stuff
                        startActivity(new Intent(MainActivity.this, MedicationActivity.class));
                        break;
                    case R.id.action_history:
                        // do stuff
                        Toast.makeText(MainActivity.this,
                                "You clicked"+menuItem.getTitle().toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_log_out:
                        FirebaseUtils.getAuthenticationReference().signOut();
                        new ProfileManager(MainActivity.this).wipeUserDetails();

                        Intent logOutIntent = new Intent(MainActivity.this, SignInActivity.class);
                        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logOutIntent);
                        break;
                    case R.id.action_view:
                        // do stuff
                        Toast.makeText(MainActivity.this,
                                "You clicked"+menuItem.getTitle().toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }

            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }
        });
    }

    private void setupRecyclerView(){
        medicationsModel = new ArrayList<>();
        medicationList = findViewById(R.id.rv_medication_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        medicationList.setLayoutManager(layoutManager);
        medicationList.setHasFixedSize(true);
    }

    private void retrieveMedications(){
        FirebaseUtils.getMedicReference(new ProfileManager(MainActivity.this).getUid()
                ,getMonth()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null){
                    MedicationsModel mm = dataSnapshot.getValue(MedicationsModel.class);
                    medicationsModel.add(new MedicationsModel(mm.getTitle(),mm.getDesc(),mm.getInterval(),
                            mm.getStart_date(),mm.getEnd_date(),mm.getDosage()));
                    medicationAdapter = new MedicationAdapter(medicationsModel);
                    medicationList.setAdapter(medicationAdapter);
                }
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

    private String getMonth(){
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        changeSearchViewColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(android.R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconfiedByDefault()){
                    searchView.setIconified(true);
                    item.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<MedicationsModel> filteredResult = filterList(medicationsModel,newText);
                medicationAdapter.setFilter(filteredResult);
                return false;
            }
        });
        return true;
    }

    private List<MedicationsModel> filterList (List<MedicationsModel> mm, String query){
        query = query.toLowerCase();
        final List<MedicationsModel> filteredModelList = new ArrayList<>();
        for(MedicationsModel selectedMedications : mm){
            String name = selectedMedications.getTitle().toLowerCase();
            if(name.contains(query)){
                filteredModelList.add(selectedMedications);
            }
        }

        return filteredModelList;
    }

    private void changeSearchViewColor(View view){
        if(view != null){
            if (view instanceof TextView){
                ((TextView)view).setTextColor(Color.WHITE);
                return;
            }else if(view instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) view;
                for(int i = 0; i<viewGroup.getChildCount();i++){
                    changeSearchViewColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void userAuthenticationState(){
        if(FirebaseUtils.getAuthenticationReference().getCurrentUser() != null){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_main);
                    setUpFAB();
                    setupRecyclerView();
                    retrieveMedications();
                }
            });
        }else{
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
            });
        }

    }
}
