package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private Spinner typeCapture;
    private String[] typesCapture;

    private String name_activity;
    private Button doneBtn;
    private TextView resetBtn;

    private SeekBar accuracySb;
    private SeekBar matchScoreSb;

    private ArrayAdapter<String> adapter;

    private TextView accuracyTv;
    private TextView matchScoreTv;

    private LinearLayout deviationAngleLayout;
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
        accuracySb = findViewById(R.id.accuracy_seekbar);
        matchScoreSb = findViewById(R.id.match_score_seekbar);
        accuracyTv = findViewById(R.id.accuracy_textView);
        matchScoreTv = findViewById(R.id.match_score_textView);
        deviationAngleLayout = findViewById(R.id.deviation_angle_layout);

        accuracySb.setProgress(getIntent().getIntExtra("accuracy", 45));
        accuracyTv.setText(String.valueOf(accuracySb.getProgress()));



        doneBtn.setOnClickListener(v-> {handelAfterSetting();});
        resetBtn.setOnClickListener(v->{resetBtnClick();});

//        Log.i("Show match score", String.valueOf(name_activity == "ip"));
//        Log.i("Show match score", name_activity);
//        if(name_activity != "rt"){
//
//        }

        switch (name_activity){
            case "ip":
                deviationAngleLayout.setVisibility(View.VISIBLE);
                matchScoreSb.setProgress(getIntent().getIntExtra("match_score", 15));
                matchScoreTv.setText(String.valueOf(matchScoreSb.getProgress()));

                matchScoreSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        matchScoreTv.setText(String.valueOf(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            default:
        }

        accuracySb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                accuracyTv.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesCapture);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeCapture.setAdapter(adapter);
    }


    private void handelAfterSetting(){
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
                intent.putExtra("accuracy", accuracySb.getProgress());
                startActivity(intent);
                break;
            case "ip":
                Intent intent2 = new Intent(SettingActivity.this, ImageProcessingActivity.class);
                intent2.putExtra("type_capture", typeCatureInt);
                intent2.putExtra("accuracy", accuracySb.getProgress());
                intent2.putExtra("match_score", matchScoreSb.getProgress());
                startActivity(intent2);
                break;
            default:

        }
    }

    private void resetBtnClick(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("Are you sure to reset")

                .setMessage("All settings will be reset to default ")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        accuracySb.setProgress(45);
                        matchScoreSb.setProgress(15);
                        typeCapture.setSelection(adapter.getPosition("Default"));
                        Toast.makeText(getApplicationContext(),"All settings have been returned to default",Toast.LENGTH_LONG).show();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getApplicationContext(),"Cancle",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
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
                handelAfterSetting();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}