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
import android.widget.TextView;

public class Modify extends Activity implements View.OnClickListener {
    //TODO Clear LOGS

    EditText new_password;
    TextView email_text, old_password;
    String prov, email, passwd;
    SharedPreferences sharedPreferences = null;
    private static final String PREFS_NAME = "lock";
    public static final String TAG = "MODIFY";
    public static final String EXTRA_DELETE = "DELETE";
    public static final String EXTRA_ID = "com.cyberviy.ViyP.EXTRA_ID";
    public static final String EXTRA_ENCRYPT = "com.cyberviy.ViyP.EXTRA_ENCRYPT";
    public static final String EXTRA_EMAIL = "com.cyberviy.ViyP.EXTRA_EMAIL";
    public static final String EXTRA_IV = "com.cyberviy.ViyP.EXTRA_IV";
    public static final String EXTRA_SALT = "com.cyberviy.ViyP.EXTRA_SALT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        email_text = findViewById(R.id.modify_email);
        old_password = findViewById(R.id.modify_old_password);
        new_password = findViewById(R.id.modify_new_password);
        CheckBox checkBox = findViewById(R.id.modify_show_password);
        Button updateBtn = findViewById(R.id.modify_update);
        Button deleteBtn = findViewById(R.id.modify_delete);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sha = sharedPreferences.getString("hash", "0");
        Intent intent = getIntent();
        email = intent.getStringExtra(EXTRA_EMAIL);
        email_text.setText(email);

        //DECRYPT
        passwd = intent.getStringExtra(EXTRA_ENCRYPT);
        try {
            String decPass = AESUtils.decrypt(passwd);
            old_password.setText(decPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new_password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                } else {
                    new_password.setInputType(129);
                }
            }
        });
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modify_update) {
            modify_data();
        } else if (v.getId() == R.id.modify_delete) {
            delete_data();
        }
    }

    private void delete_data() {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETE, true);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_ENCRYPT, passwd);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void modify_data() {
        String text_old_password, text_new_password;
        text_old_password = old_password.getText().toString();
        text_new_password = new_password.getText().toString();

        if (text_old_password.trim().isEmpty()) {
            old_password.setError("Required");
            old_password.requestFocus();
            return;
        }
        if (text_new_password.trim().isEmpty()) {
            new_password.setError("Required");
            new_password.requestFocus();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EMAIL, email);
        try {
            String encPass = AESUtils.encrypt(text_new_password);
            intent.putExtra(EXTRA_ENCRYPT, encPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}