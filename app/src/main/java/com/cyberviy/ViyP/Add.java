package com.cyberviy.ViyP;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.sql.Blob;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add extends Activity implements View.OnClickListener {
    Blob blob = null;
    private static final String PREFS_NAME = "lock";
    public static final String EXTRA_PROVIDER = "com.cyberviy.ViyP.EXTRA_PROVIDER";
    public static final String EXTRA_ENCRYPT = "com.cyberviy.ViyP.EXTRA_ENCRYPT";
    public static final String EXTRA_EMAIL = "com.cyberviy.ViyP.EXTRA_EMAIL";
    public static final String EXTRA_IV = "com.cyberviy.ViyP.EXTRA_IV";
    public static final String EXTRA_SALT = "com.cyberviy.ViyP.EXTRA_SALT";
    private EditText email, password;
    private CheckBox checkBox;
    Button add_button;
    SharedPreferences sharedPreferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //ProgressBar progressBar = findViewById(R.id.progress_bar);
        email = findViewById(R.id.add_email);
        password = findViewById(R.id.add_password);
        checkBox = findViewById(R.id.add_show_password);
        add_button = findViewById(R.id.add_record);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String provider = getIntent().getStringExtra(EXTRA_PROVIDER);
        assert provider != null;
        switch (provider) {
            case "social":
                email.setHint("Username/Email");
                break;
            case "wifi":
                email.setHint("SSID");
                break;
            default:
                email.setHint("Email");
                break;
        }
        add_button.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                } else {
                    password.setInputType(129);
                }
            }
        });
    }

    private void save_data() {
        String text_email, text_password;
        text_email = email.getText().toString();
        text_password = password.getText().toString();
        String sha = sharedPreferences.getString("hash", "0");

        if (text_email.trim().isEmpty()) {
            email.setError("Required");
            email.requestFocus();
            return;
        }
        if (text_password.trim().isEmpty()) {
            password.setError("Required");
            password.requestFocus();
            return;
        }
        String regex_email = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(regex_email);
        Matcher matcher = pattern.matcher(text_email);
        //TODO Change if
        if (!matcher.matches()) {
            email.setError("Enter valid email");
            email.requestFocus();
            return;
        }
        //Encryption of password
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EMAIL, text_email);

        // AES UTILS ENC and DEC
        try {
            String encPass = AESUtils.encrypt(text_password);
            intent.putExtra(EXTRA_ENCRYPT, encPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_record) {
            save_data();
        }
    }
}