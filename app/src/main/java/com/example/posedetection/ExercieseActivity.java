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

    private Button squartBtn;
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
//        pushUpBtn = findViewById(R.id.pushup);
//        pullUpBtn = findViewById(R.id.pullup);


        squartBtn.setOnClickListener(v->{btnOnclick(0);});
//        pushUpBtn.setOnClickListener(v->{btnOnclick(1);});
//        pullUpBtn.setOnClickListener(v->{btnOnclick(2);});

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

    }


    private void btnOnclick(int type){
        Intent intent;

        if(id == 0){
            intent = new Intent(ExercieseActivity.this, MovementCountingActivity.class);
        }else{
            intent = new Intent(ExercieseActivity.this, VideoProcessingActivity.class);
        }

        // 0 squart
        // 1 push up
        // 2 pull up

        switch (type){
            case 0:
                intent.putExtra("id_exercise", 0);
                break;
            case 1:
                intent.putExtra("id_exercise", 1);
                break;
            case 2:
                intent.putExtra("id_exercise", 2);
                break;
        }

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