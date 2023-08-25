package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    private Spinner typeCapture;
    private String[] typesCapture;

    private String name_activity;
    private Button doneBtn;
    private TextView resetBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setupActionbar();


        name_activity =  getIntent().getStringExtra("name_activity");
        typesCapture = new String[] {"Default", "Only pose", "Have original"};

        typeCapture = findViewById(R.id.typeCapture);
        doneBtn = findViewById(R.id.done_button);
        resetBtn = findViewById(R.id.reset_button);

        doneBtn.setOnClickListener(v-> {handelAfterSettting();});
        resetBtn.setOnClickListener(v->{});


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesCapture);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCapture.setAdapter(adapter);
    }


    private void handelAfterSettting(){
        this.finish();
        int typeCatureInt;
        switch (typeCapture.getSelectedItem().toString()){
            case "Default":
                typeCatureInt = 0;
                break;
            case "Only pose":
                typeCatureInt = 1;
                break;
            default:
                typeCatureInt = 2;
                break;
        }
        switch(name_activity){
            case "rt":
                Intent intent = new Intent(SettingActivity.this, RealtimeProcessingActivity.class);
                intent.putExtra("type_capture", typeCatureInt);
                startActivity(intent);
            default:

        }
    }

    private void setupActionbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handelAfterSettting();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}