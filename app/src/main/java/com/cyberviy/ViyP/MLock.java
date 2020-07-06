package com.cyberviy.ViyP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.himanshurawat.hasher.HashType;
import com.himanshurawat.hasher.Hasher;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
//MLock Master Lock

public class MLock extends AppCompatActivity {
    TextView mlock_tv_greet, mlock_tv_pp;
    SharedPreferences sharedPreferences = null;
    final String PREFS_NAME = "lock";
    final String PREF_KEY = "firstrun";
    final String TAG = "lock";
    final int DOESNT_EXIST = -1;
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_mlock);

        mlock_tv_greet = findViewById(R.id.mlock_l_tv_greet);
        /*mlock_et_mp = findViewById(R.id.mlock_l_et_mpass);
        mlock_b_mp = findViewById(R.id.mlock_l_b_setmp);*/
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView = findViewById(R.id.pin_lock_view);


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //First time then change Text
        if (sharedPreferences.getBoolean(PREF_KEY, true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            mlock_tv_greet.setText(R.string.mlock_st_create_password);
        }
        PinLockListener mPinLockListener = new PinLockListener() {
            @Override
            public void onComplete(String pin) {

                if (sharedPreferences.getBoolean(PREF_KEY, true)) {
                    String Hash = Hasher.Companion.hash(pin, HashType.SHA_512);
                    Log.d(TAG, Hash);
                    String HASH = new String(Hex.encodeHex(DigestUtils.sha512(pin)));
                    String string = "z3r0" + HASH + "423viyP";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("hash", string).apply();
                    //Log.d(TAG, string);
                    sharedPreferences.edit().putBoolean(PREF_KEY, false).apply();
                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                } else {
                    String sp = sharedPreferences.getString("hash", "0");
                    String check = sp.substring(4);
                    String HASH = new String(Hex.encodeHex(DigestUtils.sha512(pin)));
                    if (check.equals(HASH + "423viyP")) {
                        Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_LONG).show();
                    }
                }
                //Log.d(TAG, "Pin complete: " + pin);

            }

            @Override
            public void onEmpty() {
                Log.d(TAG, "Pin empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                //Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
            }
        };
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.side_nav_bar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
}