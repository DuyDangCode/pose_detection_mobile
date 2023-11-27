package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class ExercieseActivity extends AppCompatActivity {

    private Button squartBtn, seatedDumbbellBtn;
//    private Button  pushUpBtn, pullUpBtn;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        squartBtn = findViewById(R.id.squat);
        seatedDumbbellBtn = findViewById(R.id.seated_dumbbell);



        //navigate base on type exercise
        squartBtn.setOnClickListener(v->{navigate(0);});
        seatedDumbbellBtn.setOnClickListener(v->{navigate(1);});


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

    }


    private void navigate(int type){
        Intent intent;

        if(id == 0){
            intent = new Intent(ExercieseActivity.this, MovementCountingActivity.class);
        }else{
            intent = new Intent(ExercieseActivity.this, VideoProcessingActivity.class);
        }

        // 0 squart
        // 1 seated dumbbell
        // 2 pull up

        intent.putExtra("id_exercise", type);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}