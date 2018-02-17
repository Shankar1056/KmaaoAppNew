package com.apextechies.kmaaoapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.AppListAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.model.CategoryDateModel;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.crashlytics.android.Crashlytics;
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
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_applist)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.walletamount)
    TextView walletamount;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    private ArrayList<CategoryDateModel> categoryDateModels = new ArrayList<>();

    private AdView mAdView;
    private long hour;
    private long minute;
    private long second;


    public static final String TAG = MyServiceAnoter.class.getSimpleName();
    private TimerServiceMainActivity timerService;
    private boolean serviceBound;
    private TextView timerTextView;
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);
    // Message type for the handler
    public final static int MSG_UPDATE_TIME = 0;
    private int totalwallet;
    private  boolean load = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        initWidgit();
        navigationMappin();
        callCategoryApi();
        initAds();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //checksession();
                if (serviceBound && !timerService.isTimerRunning()) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Starting timer");
                    }
                    timerService.startTimer();
                    updateUIStartRun();
                }

            }
        }, 3000);


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

    private void navigationMappin() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.userName);
        TextView userEmail = (TextView) hView.findViewById(R.id.userEmail);
        nav_user.setText(ClsGeneral.getPreferences(MainActivity.this, PreferenceName.USER_NAME));
        userEmail.setText(ClsGeneral.getPreferences(MainActivity.this, PreferenceName.USER_EMAIL));
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void callCategoryApi() {
        Utilz.showProgress(MainActivity.this, getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(MainActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (response != null && response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            JSONArray array = object.getJSONArray("data");
                            for (int i=0; i<array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                categoryDateModels.add(new CategoryDateModel(jo.optString("application_id"),jo.optString("application_name"),
                                        jo.optString("application_play_store_link"),jo.optString("application_price"), jo.optString("application_status"),
                                        jo.optString("created_date"),jo.optString("created_time"),jo.optString("expiry_date"),
                                        jo.optString("expiry_time") ));
                            }
                            final AppListAdapter adapter = new AppListAdapter(MainActivity.this, categoryDateModels, R.layout.applist_row, new OnClickEvent() {
                                @Override
                                public void onClick(int pos) {
                                    startActivity(new Intent(MainActivity.this, DetailsActivity.class).
                                            putExtra("id", categoryDateModels.get(pos).getApplication_id()).
                                            putExtra("link", categoryDateModels.get(pos).getApplication_play_store_link()).
                                            putExtra("name", categoryDateModels.get(pos).getApplication_name())
                                    );
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        web.setReqType(true);
        web.execute(WebService.CATEGORY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @OnClick(R.id.walletamount)
    void OnAmountClick() {
        startActivity(new Intent(MainActivity.this, WalletActivity.class));
    }

    private void initWidgit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        timerTextView = (TextView) findViewById(R.id.timer_text_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setwalletAmount( ClsGeneral.getPreferences(MainActivity.this, PreferenceName.TOTALAMOUNT));
      //  walletamount.setText("₹" + ClsGeneral.getPreferences(MainActivity.this, PreferenceName.TOTALAMOUNT));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        if (item.getItemId() == R.id.dialytask) {
            startActivity(new Intent(MainActivity.this, DialyTask.class));

        }
        return false;
    }


    public void updateUITimer() {
        if (serviceBound) {

           // timerTextView.setText(timerService.elapsedTime() + " seconds");
            try {
                long longVal = timerService.elapsedTime();
                int hours = (int) longVal / 3600;
                int remainder = (int) longVal - hours * 3600;
                int mins = remainder / 60;
                remainder = remainder - mins * 60;
                int secs = remainder;

               // int[] ints = {hours, mins, secs};

                timerTextView.setText(""+hours +":"+mins+":"+secs+ " seconds");
            }catch (Exception e){
                timerTextView.setText(timerService.elapsedTime() + " seconds");
            }
            if (timerService.elapsedTime()>=9 && load){
                load = false;
                try {
                    String date = Utilz.getCurrentDateInDigit(MainActivity.this);

                    if (ClsGeneral.getPreferences(MainActivity.this, PreferenceName.TODAYDATE).equalsIgnoreCase(date)) {
                        return;
                    }
                    updateWalletApi();
                    totalwallet = Integer.parseInt(ClsGeneral.getPreferences(MainActivity.this, PreferenceName.TOTALAMOUNT));
                } catch (NumberFormatException e) {
                    totalwallet = 0;
                } catch (Exception e) {

                }
            }

            // Toast.makeText(timerService, "" + timerService.elapsedTime(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerServiceMainActivity.class);
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
                stopService(new Intent(this, TimerServiceMainActivity.class));
            }
            // Unbind the service
            unbindService(mConnection);
            serviceBound = false;
        }
    }



    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound");
            }
            TimerServiceMainActivity.RunServiceBinder binder = (TimerServiceMainActivity.RunServiceBinder) service;
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
        private final WeakReference<MainActivity> activity;

        UIUpdateHandler(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MyServiceAnoter.MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(MainActivity.TAG, Log.VERBOSE)) {
                    Log.v(MainActivity.TAG, "updating time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MyServiceAnoter.MSG_UPDATE_TIME, UPDATE_RATE_MS);

            }
        }
    }

    private void updateWalletApi() {
        String date = Utilz.getCurrentDateInDigit(MainActivity.this);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList();
        Download_web web = new Download_web(MainActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {

                if (response != null && response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            ClsGeneral.setPreferences(MainActivity.this, PreferenceName.TODAYDATE, object.optString("data") );
                            load = true;
                            Toast.makeText(MainActivity.this, "Daily Bonus Credited", Toast.LENGTH_SHORT).show();
                            ClsGeneral.setPreferences(MainActivity.this, PreferenceName.TOTALAMOUNT, "" + (totalwallet+5));
                            setwalletAmount(ClsGeneral.getPreferences(MainActivity.this, PreferenceName.TOTALAMOUNT));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(MainActivity.this, PreferenceName.USER_ID)));
        nameValuePairs.add(new BasicNameValuePair("amount", "" + (totalwallet + 5)));
        nameValuePairs.add(new BasicNameValuePair("todaydate", date));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.SETWALLETAMOUNTDIALY);
    }

    private void setwalletAmount(String preferences) {
        walletamount.setText(" Wallet ₹" +preferences);
    }
}
