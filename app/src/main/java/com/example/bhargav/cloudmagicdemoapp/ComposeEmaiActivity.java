package com.example.bhargav.cloudmagicdemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bhargav on 7/31/2016.
 */
public class ComposeEmaiActivity extends AppCompatActivity{

    EditText from, to, subject, body;
    Button sendEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        from = (EditText) findViewById(R.id.et_from);
        to = (EditText) findViewById(R.id.et_to);
        subject = (EditText) findViewById(R.id.et_subject);
        body = (EditText) findViewById(R.id.et_body);
        sendEmail = (Button) findViewById(R.id.button_send_email);
        sendEmail.setOnClickListener(sendEmailListner);
    }

    View.OnClickListener sendEmailListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!validateAllField())
                return;
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to.getText().toString()});
            i.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
            i.putExtra(Intent.EXTRA_TEXT   , body.getText().toString());
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ComposeEmaiActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean validateAllField() {
        if(TextUtils.isEmpty(from.getText().toString()) || !validateEmail(from.getText().toString())) {
            from.setError("enter valid email");
            return false;
        } else if(TextUtils.isEmpty(to.getText().toString()) || !validateEmail(to.getText().toString())) {
            to.setError("enter valid email");
            return false;
        }
        return true;
    }

    public boolean validateEmail(String emailStr) {

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
