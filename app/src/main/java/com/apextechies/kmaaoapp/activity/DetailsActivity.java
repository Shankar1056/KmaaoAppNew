package com.apextechies.kmaaoapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.DetailsAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.model.DetailsModelData;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsActivity extends AppCompatActivity implements MyService.Callbacks {

    @BindView(R.id.rv_details)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.install_appname)
    TextView install_appname;
    @BindView(R.id.wallet)
    TextView wallet;
    private  int totalwallet;

    //Service
    Intent serviceIntent;
    int seconds;
    int minutes;
    int hours;
    MyService myService;
    private int count = 0;


    private ArrayList<DetailsModelData> detailsModelData = new ArrayList<>();
    private DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initWidgit();
        if (Utilz.isInternetConnected(DetailsActivity.this)) {
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
                if (response != null && response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            JSONArray array = object.getJSONArray("data");
                            for (int i=0; i<array.length(); i++){
                                JSONObject jo = array.getJSONObject(i);
                                detailsModelData.add(new DetailsModelData(jo.optString("application_rules_id"),jo.optString("application_id"),
                                        jo.optString("application_rules"),jo.optString("stpes"), jo.optString("rules_image"),
                                        jo.optString("application_status")));
                            }
                            recyclerView.setAdapter(new DetailsAdapter(DetailsActivity.this, detailsModelData, R.layout.appdetails_row, new OnClickEvent() {
                                @Override
                                public void onClick(int pos) {

                                }
                            }));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

        serviceIntent = new Intent(DetailsActivity.this, MyService.class);

        install_appname.setText("Install " + getIntent().getStringExtra("name"));
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
                    startService(serviceIntent); //Starting the service
                    bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                    //   myService.startCounter();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            totalwallet = Integer.parseInt(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
        }catch (NumberFormatException e)
        {
            totalwallet = 0;
        }
        catch (Exception e)
        {

        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Toast.makeText(DetailsActivity.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
            // We've binded to LocalService, cast the IBinder and get LocalService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getServiceInstance(); //Get instance of your service!
            myService.registerClient(DetailsActivity.this); //Activity register in the service as client for callabcks!

            Toast.makeText(myService, "Connected to service...", Toast.LENGTH_SHORT).show();

            myService.startCounter();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Toast.makeText(DetailsActivity.this, "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
            Toast.makeText(myService, "Service disconnected", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void updateClient(long millis) {

        seconds = (int) (millis / 1000) % 60;
        minutes = (int) ((millis / (1000 * 60)) % 60);
        hours = (int) ((millis / (1000 * 60 * 60)) % 24);

        //   tvServiceOutput.setText((hours>0 ? String.format("%d:", hours) : "") + ((this.minutes<10 && this.hours > 0)? "0" + String.format("%d:", minutes) :  String.format("%d:", minutes)) + (this.seconds<10 ? "0" + this.seconds: this.seconds));
        Log.e("TTTTTTTTTTTTT", "" + millis);
        count++;

    }

    @Override
    protected void onResume() {
        super.onResume();
        int requestedamount = Integer.parseInt("10");

        if (count > 3) {
            unbindService(mConnection);
            stopService(serviceIntent);
            myService.stopCounter();
            stopService(new Intent(getBaseContext(), MyService.class));
        }
        if (count >= 30) {
            Toast.makeText(myService, "You stayed" + count + "Seconds", Toast.LENGTH_SHORT).show();
            if (totalwallet>=requestedamount) {
                if (Utilz.isInternetConnected(DetailsActivity.this)) {
                    updateWalletApi(totalwallet, requestedamount);
                }
            }
        }

            setwalletAmount(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
    }
    public void setwalletAmount(String amount) {
        wallet.setText("Wallet ₹"+amount);
    }

    private void updateWalletApi(final int wallet, final int requestedamount) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList();
        Download_web web = new Download_web(DetailsActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                if (response != null && response.length() > 0) {
                    ClsGeneral.setPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT, ""+(wallet+requestedamount));
                    setwalletAmount(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.USER_ID)));
        nameValuePairs.add(new BasicNameValuePair("amount", ""+(totalwallet+10)));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.SETWALLETAMOUNT);
    }
}
