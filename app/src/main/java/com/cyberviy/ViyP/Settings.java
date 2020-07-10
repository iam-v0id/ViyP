package com.cyberviy.ViyP;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cyberviy.ViyP.ui.password.PasswordViewModel;

public class Settings extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    String PREF_NAME = "Settings";
    String PREF_KEY = "MASTER_PASSWORD";
    String PREF_KEY_SECURE_CORE_MODE = "SECURE_CORE";
    String NO_DATA = "NO DATA";
    String TYPE_PASS_1 = "PIN";
    String TYPE_PASS_2 = "PASSWORD";
    TextView change_password, export_data, delete_data, about_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        change_password = findViewById(R.id.change_master_password);
        export_data = findViewById(R.id.export_data);
        delete_data = findViewById(R.id.delete_all_data);
        about_app = findViewById(R.id.about_app);
        final Switch askPasswordLaunchSwitch = findViewById(R.id.ask_password_launch);
        final Switch secureCoreModeSwitch = findViewById(R.id.secure_core_mode);
        final boolean secureCodeModeState = sharedPreferences.getBoolean(PREF_KEY_SECURE_CORE_MODE, false);
        final boolean askPasswordLaunchState = sharedPreferences.getBoolean(PREF_KEY, true);
        final boolean status = sharedPreferences.getBoolean(NO_DATA, false);
        secureCoreModeSwitch.setChecked(secureCodeModeState);
        askPasswordLaunchSwitch.setChecked(askPasswordLaunchState);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        askPasswordLaunchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // ask for password
                    askPassword(true);
                    editor.putBoolean(PREF_KEY, true).apply();
                } else {
                    // remove password
                    askPassword(false);
                    editor.putBoolean(PREF_KEY, false).apply();
                }
            }
        });
        secureCoreModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Removing
                    secureCodeMode(true);
                    editor.putBoolean(PREF_KEY_SECURE_CORE_MODE, true).apply();
                } else {
                    secureCodeMode(false);
                    editor.putBoolean(PREF_KEY_SECURE_CORE_MODE, false).apply();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void secureCodeMode(boolean state) {
        if (state) {
            //to do False
            //remove copy to clipboard and screenshot ability
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            Toast.makeText(getApplicationContext(), "Secure code mode: ON", Toast.LENGTH_LONG).show();
        } else {
            //to do true
            //set copy to clipboard and screenshot ability
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            Toast.makeText(getApplicationContext(), "Secure code mode: OFF", Toast.LENGTH_LONG).show();
        }
    }

    private void askPassword(boolean state) {
        if (state) {
            //to do False
            //remove copy to clipboard and screenshot ability
            Toast.makeText(getApplicationContext(), "Password: ON", Toast.LENGTH_LONG).show();
        } else {
            //to do true
            //set copy to clipboard and screenshot ability
            Toast.makeText(getApplicationContext(), "Password: OFF", Toast.LENGTH_LONG).show();
        }
    }


    public void changePassword(View view) {
        TextView PIN = findViewById(R.id.change_master_password_option_1);
        TextView Password = findViewById(R.id.change_master_password_option_2);
        PIN.setVisibility(View.VISIBLE);
        Password.setVisibility(View.VISIBLE);
    }

    public void changePasswordToPIN(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangePassActivity.class);
        intent.putExtra(ChangePassActivity.EXTRA_TYPE_PASS, TYPE_PASS_1);
        startActivity(intent);
    }

    public void changePasswordToPassword(View view) {
        Intent intent = new Intent(getApplicationContext(), ChangePassActivity.class);
        intent.putExtra(ChangePassActivity.EXTRA_TYPE_PASS, TYPE_PASS_2);
        startActivity(intent);
    }

    public void exportData(View view) {
        Toast.makeText(getApplicationContext(), "Export successful", Toast.LENGTH_SHORT).show();
    }

    public void deleteData(View view) {
        //AlertDialog START
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Delete Everything");
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Are you sure??? You want to delete everything?");
        alertDialogBuilder.setCancelable(false);
        //Positive button
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                PasswordViewModel passwordViewModel = new PasswordViewModel(getApplication());
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                passwordViewModel.deleteAllNotes();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(NO_DATA, false).apply();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        //Negative button
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //AlertDialog END

    }

    public void aboutApp(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}