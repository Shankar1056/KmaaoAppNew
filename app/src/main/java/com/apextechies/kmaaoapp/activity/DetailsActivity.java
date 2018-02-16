package com.apextechies.kmaaoapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private int totalwallet;

    public static final String TAG = MyServiceAnoter.class.getSimpleName();
    private TimerService timerService;
    private boolean serviceBound;
    private TextView timerTextView;
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);
    // Message type for the handler
    public final static int MSG_UPDATE_TIME = 0;
    private int count = 0;
    private boolean isOnresume = false;

    private AdView mAdView;



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
        initAds();
    }


    private void initAds() {
        MobileAds.initialize(this);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getResources().getString(R.string.ADUNIT_ID));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @OnClick(R.id.wallet)
    void OnAmountClick() {
        startActivity(new Intent(DetailsActivity.this, WalletActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning()) {
                timerService.foreground();
            } else {
                stopService(new Intent(this, TimerService.class));
            }
            // Unbind the service
            unbindService(mConnection);
            serviceBound = false;
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
                        if (object.optString("status").equalsIgnoreCase("true")) {
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                detailsModelData.add(new DetailsModelData(jo.optString("application_rules_id"), jo.optString("application_id"),
                                        jo.optString("application_rules"), jo.optString("stpes"), jo.optString("rules_image"),
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
        timerTextView = (TextView) findViewById(R.id.timer_text_view);

        install_appname.setText("Install " + getIntent().getStringExtra("name"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceBound && timerService.isTimerRunning()) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Stopping timer");
                    }
                    timerService.stopTimer();
                    updateUIStopRun();
                }
                finish();
            }
        });


        install_appname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("link")));
                    startActivity(intent);
                    if (serviceBound && timerService.isTimerRunning()) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "Stopping timer");
                        }
                        timerService.stopTimer();
                        updateUIStopRun();
                    }
                    if (serviceBound && !timerService.isTimerRunning()) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "Starting timer");
                        }
                        timerService.startTimer();
                        updateUIStartRun();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isOnresume = true;
            }
        });
        try {
            totalwallet = Integer.parseInt(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
        } catch (NumberFormatException e) {
            totalwallet = 0;
        } catch (Exception e) {

        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    @Override
    public void updateClient(long millis) {


    }

    @Override
    protected void onResume() {
        super.onResume();


        setwalletAmount(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
    }

    public void setwalletAmount(String amount) {
        wallet.setText("Wallet ₹" + amount);
    }

    private void updateWalletApi(final int wallet, final int requestedamount) {
        String date = Utilz.getCurrentDateInDigit(DetailsActivity.this);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList();
        Download_web web = new Download_web(DetailsActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                if (response != null && response.length() > 0) {
                    ClsGeneral.setPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT, "" + (wallet + requestedamount));
                    setwalletAmount(ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.TOTALAMOUNT));
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(DetailsActivity.this, PreferenceName.USER_ID)));
        nameValuePairs.add(new BasicNameValuePair("amount", "" + (totalwallet + requestedamount)));
        nameValuePairs.add(new BasicNameValuePair("todaydate", date));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.SETWALLETAMOUNT);
    }

    public void updateUITimer() {
        int requestedamount = Integer.parseInt("10");
        if (serviceBound) {
            timerTextView.setText(timerService.elapsedTime() + " seconds");
            count = (int) timerService.elapsedTime();
           // Toast.makeText(timerService, "" + timerService.elapsedTime(), Toast.LENGTH_SHORT).show();
            if (count>=30){

                if (Utilz.isInternetConnected(DetailsActivity.this)) {
                    updateWalletApi(totalwallet, requestedamount);
                }

            }
            if (count>100){
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Stopping timer");
                    }
                    timerService.stopTimer();
                    updateUIStopRun();
            }
        }
    }

    private void updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    public class UIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<DetailsActivity> activity;

        UIUpdateHandler(DetailsActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MyServiceAnoter.MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(MyServiceAnoter.TAG, Log.VERBOSE)) {
                    Log.v(MyServiceAnoter.TAG, "updating time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MyServiceAnoter.MSG_UPDATE_TIME, UPDATE_RATE_MS);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (serviceBound && timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Stopping timer");
            }
            timerService.stopTimer();
            updateUIStopRun();
        }
    }
}
