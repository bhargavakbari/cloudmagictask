package com.example.bhargav.cloudmagicdemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bhargav.cloudmagicdemoapp.adapter.EmailRecyclerAdapter;
import com.example.bhargav.cloudmagicdemoapp.models.Message;
import com.example.bhargav.cloudmagicdemoapp.networkmanager.ApiClient;
import com.example.bhargav.cloudmagicdemoapp.networkmanager.ApiInterface;
import com.example.bhargav.cloudmagicdemoapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EmailRecyclerAdapter.recyclerClickListner,EmailRecyclerAdapter.recyclerLongClickListner{

    public static String TAG = "MainActivity";

    RecyclerView emailRecyclerView;
    RelativeLayout progressBarLayout;
    ProgressBar progressBar;

    boolean itemLongPressed;
    int selecteItemPosition = 0;
    private EmailRecyclerAdapter emailRecyclerAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailRecyclerView = (RecyclerView) findViewById(R.id.email_recycler_view);
        progressBarLayout = (RelativeLayout) findViewById(R.id.progress_bar_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor
                (this, R.color.blue), PorterDuff.Mode.MULTIPLY);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        emailRecyclerView.setLayoutManager(layoutManager);
        emailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if(isNetworkConnected())
            loadAllEmail();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(!itemLongPressed) {
            MenuItem deleteEmail = menu.findItem(R.id.delete_mail);
            deleteEmail.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_mail:
                deleteEmail();
                return true;
            case R.id.create_email:{
                Intent intent = new Intent(MainActivity.this, ComposeEmaiActivity.class);
                startActivity(intent);
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteEmail() {
        if(!isNetworkConnected()){
            return;
        }
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<String> deleteEMail= apiService.deleteMessageById(Integer.toString(selecteItemPosition));
        deleteEMail.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loadAllEmail();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadAllEmail();
            }
        });
    }

    private void loadAllEmail() {
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Message>> listOfAllMail= apiService.getAllEmail();
        listOfAllMail.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messageList = response.body();
                emailRecyclerAdapter = new EmailRecyclerAdapter(response.body(), MainActivity.this);
                emailRecyclerView.setAdapter(emailRecyclerAdapter);
                Toast.makeText(MainActivity.this,"Please long press to delete message",Toast.LENGTH_LONG).show();
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e(TAG, t.toString());
                hideProgressBar();
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        itemLongPressed = false;
        invalidateOptionsMenu();
        Intent intent = new Intent(MainActivity.this, DetailEmailActivity.class);
        intent.putExtra(Constants.MESSAGEID, messageList.get(position).getMessageId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClicked(int position) {
        selecteItemPosition = position;
        itemLongPressed = true;
        invalidateOptionsMenu();
    }

    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if(!itemLongPressed)
            super.onBackPressed();
        else {
            itemLongPressed = false;
            invalidateOptionsMenu();
            emailRecyclerAdapter.notifyItemChanged(selecteItemPosition);
        }
    }
}
