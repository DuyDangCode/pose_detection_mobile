package com.example.posedetection.custom_dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.posedetection.R;

public class SettingsRealtime extends Dialog implements  android.view.View.OnClickListener {
    public Activity c;
    public Dialog d;

    public SettingsRealtime(@NonNull Activity a) {
        super(a);
        this.c = a;
    }

    public SettingsRealtime(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SettingsRealtime(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings_realtime_processing);
        
    }

    @Override
    public void onClick(View view) {

    }
}
