package com.example.bhargav.cloudmagicdemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhargav.cloudmagicdemoapp.models.DetailMessage;
import com.example.bhargav.cloudmagicdemoapp.networkmanager.ApiClient;
import com.example.bhargav.cloudmagicdemoapp.networkmanager.ApiInterface;
import com.example.bhargav.cloudmagicdemoapp.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bhargav on 7/31/2016.
 */
public class DetailEmailActivity extends AppCompatActivity {

    String messageId;
    TextView emailSubject, senderName, receiverName, emailBody;
    ImageView importantMail;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_email);

        readIntentData();
        emailSubject = (TextView) findViewById(R.id.tv_email_subject);
        importantMail = (ImageView) findViewById(R.id.iv_important);
        senderName = (TextView) findViewById(R.id.tv_email_sender_name);
        receiverName = (TextView) findViewById(R.id.tv_email_receiver);
        emailBody = (TextView) findViewById(R.id.tv_email_body_message);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor
                (this, R.color.blue), PorterDuff.Mode.MULTIPLY);

        if(isNetworkConnected())
            loadDetailEmail(messageId);
    }

    private void readIntentData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.get(Constants.MESSAGEID)!= null){
            messageId = bundle.get(Constants.MESSAGEID).toString();
        } else{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadDetailEmail(String id) {
       showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<DetailMessage> detailEMail= apiService.getMessageById(id);
        detailEMail.enqueue(new Callback<DetailMessage>() {
            @Override
            public void onResponse(Call<DetailMessage> call, Response<DetailMessage> response) {
                setUpUi(response);
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<DetailMessage> call, Throwable t) {
                Toast.makeText(DetailEmailActivity.this,"Somthing went wrong",Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });

    }

    private void setUpUi(Response<DetailMessage> response) {
        DetailMessage message = response.body();
        emailSubject.setText(message.getSubject());
        senderName.setText(message.getParticipants().get(0).getName());
        emailBody.setText(message.getBodyMessage());
        if(message.getParticipants().size() > 2)
            receiverName.setText(message.getParticipants().get(1).getName());
        if(message.isStarred())
            importantMail.setImageResource(R.drawable.ic_imp);
        else
            importantMail.setImageResource(R.drawable.ic_star);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Intent intent = new Intent(this, NoInternetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return false;
        }
    }

    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }
}
