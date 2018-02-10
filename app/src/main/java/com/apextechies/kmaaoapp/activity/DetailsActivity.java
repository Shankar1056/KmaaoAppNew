package com.apextechies.kmaaoapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.DetailsAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.model.DetailsModel;
import com.apextechies.kmaaoapp.model.DetailsModelData;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.rv_details)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.install_appname)
    TextView install_appname;

    private ArrayList<DetailsModelData> detailsModelData = new ArrayList<>();
    private DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initWidgit();
        if (Utilz.isInternetConnected(DetailsActivity.this)){
            callRelesApi();
        }
    }

    private void callRelesApi() {
        Utilz.showProgress(DetailsActivity.this, getResources().getString(R.string.pleasewait));
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Download_web web = new Download_web(DetailsActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (response!=null && response.length()>0){
                    Gson gson = new Gson();
                    final DetailsModel details = gson.fromJson(response,DetailsModel.class);
                    detailsModelData = details.getData();
                    recyclerView.setAdapter(new DetailsAdapter(DetailsActivity.this, detailsModelData , R.layout.appdetails_row, new OnClickEvent() {
                        @Override
                        public void onClick(int pos) {

                        }
                    }));
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("application_id", getIntent().getStringExtra("id")));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.APPRULES);
    }

    private void initWidgit() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getIntent().getStringExtra("name"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        install_appname.setText("Install "+getIntent().getStringExtra("name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        install_appname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("link")));
                    startActivity(intent);
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }



}
