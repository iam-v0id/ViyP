package com.cyberviy.ViyP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    String PREF_NAME = "Settings";
    String PREF_KEY = "MASTER_PASSWORD";
    SharedPreferences sharedPreferences;


    // Gradient on statusbar
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            //window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                final boolean askPasswordLaunchState = sharedPreferences.getBoolean(PREF_KEY, true);
                if (!askPasswordLaunchState) {
                    startActivity(new Intent(SplashActivity.this, Home.class));
                    Toast.makeText(getApplicationContext(), "Consider using Master lock for maximum privacy", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MLock.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);


    }
}