package com.example.qrcards;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.qrcards.Database.RecordDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class QrListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    QRListAdapters qrListAdapters;
    List<Record> list_of_records =new ArrayList<>();
    RecordDatabase database;
    Record selected_record;

    SearchView search_view_home;
    FloatingActionButton floatingActionButton_arrival,floatingActionButton_departure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_list);


        init();
        UpdateRecycler(list_of_records);

        search_view_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });


        floatingActionButton_arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checking if camera permission is allowed
                Intent intent=new Intent(QrListActivity.this,ScannerActivity.class);
                intent.putExtra("state","Arrival");
                startActivityForResult(intent,101);




            }
        });

        floatingActionButton_departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(QrListActivity.this,ScannerActivity.class);
                intent.putExtra("state","Departure");
                startActivityForResult(intent,101);


            }
        });
    }

    private void filter(String newText) {
        List<Record>filteredRecords=new ArrayList<>();
        for(Record single_record:list_of_records){
            if(single_record.getName().toLowerCase().contains(newText.toLowerCase()) ||
            single_record.getState().toLowerCase().contains(newText.toLowerCase())){
                filteredRecords.add(single_record);


            }
        }
        qrListAdapters.FilteredList(filteredRecords);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.delete_all:
                database.recordDAO().deleteAll();
                list_of_records.clear();
                qrListAdapters.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        int permission= ActivityCompat.checkSelfPermission(QrListActivity.this,
                Manifest.permission.VIBRATE);
        if(permission== PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(QrListActivity.this,
                    new String[]{Manifest.permission.CAMERA},100);

        }

        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){
                Record record=(Record) data.getSerializableExtra("record_value");
                String qr_value=record.getQr_code_value().toUpperCase();
                String state=record.getState();
                int number_arrivals=database.recordDAO().get_arrivals(record.getName(),"Arrival");
                int number_departures=database.recordDAO().get_arrivals(record.getName(),"Departure");
                ArrayList<Record> temp=new ArrayList<>();
                temp.addAll(database.recordDAO().get_person_data(record.getName()));

                if(state.contains("Departure")){
                    if(number_arrivals!=0 && number_departures<number_arrivals){
                        Toast.makeText(this, "Impossible", Toast.LENGTH_SHORT).show();
                        database.recordDAO().insert(record);
                        list_of_records.clear();
                        list_of_records.addAll(database.recordDAO().getAll());
                        qrListAdapters.notifyDataSetChanged();


                    }
                    else{
                        vibe.vibrate(600);
                    }

                }
                else{
                    char single_double=qr_value.charAt(qr_value.length()-4);

                    //  check if single or double
                    switch (single_double){
                        case 'D':
                            if((number_arrivals<2 && number_arrivals!=0)|| (number_arrivals==number_departures+1)|| (number_arrivals==number_departures)){
                                database.recordDAO().insert(record);
                                list_of_records.clear();
                                list_of_records.addAll(database.recordDAO().getAll());
                                qrListAdapters.notifyDataSetChanged();
                                return;


                            }
                            else{
                                Toast.makeText(this, "repeated cards", Toast.LENGTH_SHORT).show();
                                vibe.vibrate(1000);

                                return;


                            }

                        case 'S':

                            if(number_arrivals==0 || (number_arrivals==number_departures && temp.get(temp.size()-2).getState().contains("Depar"))){

                                database.recordDAO().insert(record);
                                list_of_records.clear();
                                list_of_records.addAll(database.recordDAO().getAll());
                                qrListAdapters.notifyDataSetChanged();
                                return;


                            }

                            else{
                                Toast.makeText(this, "Repetition of Cards"+String.valueOf(number_arrivals)+" number of departures:"+String.valueOf(number_departures), Toast.LENGTH_SHORT).show();
                                vibe.vibrate(1000);

                                return;


                            }

                        default:
                            Toast.makeText(this, "I dont know", Toast.LENGTH_SHORT).show();



                    }

                }

//                if single compare with avilable list
//                if item not avilable add
//                if number of departure == to arrival add

            }
        }


    }

    public void init(){
        recyclerView=findViewById(R.id.recycler_list);
        search_view_home=findViewById(R.id.search_view_home);
        floatingActionButton_arrival =findViewById(R.id.scan_qr_arrival);
        floatingActionButton_departure=findViewById(R.id.scan_qr_departure);
        database=RecordDatabase.getInstance(this);
        list_of_records =database.recordDAO().getAll();

    }
    public void UpdateRecycler(List<Record>list_data){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        qrListAdapters=new QRListAdapters(QrListActivity.this,list_data,recordsClickListener);
        recyclerView.setAdapter(qrListAdapters);

    }

    private final RecordsClickListener recordsClickListener=new RecordsClickListener() {
        @Override
        public void onclick(Record record) {

        }

        @Override
        public void onLongClick(Record record, CardView container) {

            selected_record=new Record();
            selected_record=record;
            showPopup(container);
        }
    };

    private void showPopup(CardView container) {
        PopupMenu popupMenu=new PopupMenu(this,container);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       switch (item.getItemId()){
           case R.id.delete:
               database.recordDAO().delete(selected_record);
               list_of_records.remove(selected_record);
               qrListAdapters.notifyDataSetChanged();
               Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
               return true;

           default:
               return false;


       }

    }
}