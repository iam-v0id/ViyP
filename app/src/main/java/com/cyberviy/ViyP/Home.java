package com.cyberviy.ViyP;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    /*private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-";*/
    private static final String COLLECTION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*_=+-";
    //TODO Generate password from add activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        ImageButton imageButton = view.findViewById(R.id.refresh);
        final TextView textView1 = view.findViewById(R.id.generate_password);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_password,
                R.id.nav_social,
                R.id.nav_wifi)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //Initial random password gen
        String Password = generatePassword();
        textView1.setText(Password);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav_refresh();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                return true;
            case R.id.action_help:
                startActivity(new Intent(getApplicationContext(), Help.class));
                return true;
//            case R.id.action_delete_all:
//                passwordViewModel.deleteAllNotes();
//                Toast.makeText(getApplicationContext(), "Deleted everything",Toast.LENGTH_SHORT).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void copy(View view) {
        TextView textView = findViewById(R.id.generate_password);
        String gn_password = textView.getText().toString().trim();
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", gn_password);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copied!", Toast.LENGTH_SHORT).show();
        }
    }

    private String generatePassword() {
        //Creating Random object
        Random random = new Random();
        //Limiting the length of the generated password between 8 to 14
        int limit = (int) (Math.random() * 14 + 8);
        StringBuilder password = new StringBuilder();
        for (int itr = 0; itr < limit; itr++) {
            password.append(COLLECTION.charAt(random.nextInt(COLLECTION.length())));
        }
        return password.toString();
    }

    public void nav_refresh() {
        TextView textView = findViewById(R.id.generate_password);
        String Password = generatePassword();
        textView.setText(Password);
    }
}
