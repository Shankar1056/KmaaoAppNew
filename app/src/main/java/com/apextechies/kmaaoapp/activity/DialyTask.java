package com.apextechies.kmaaoapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.DialyTaskAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 2/18/2018.
 */

public class DialyTask extends AppCompatActivity {

    private InterstitialAd mInterstitialAd1, mInterstitialAd2, mInterstitialAd3, mInterstitialAd4, mInterstitialAd5, mInterstitialAd6,
            mInterstitialAd7, mInterstitialAd8, mInterstitialAd9, mInterstitialAd10;
    @BindView(R.id.rv_dialytask)
    RecyclerView rv_dialytask;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String task1, task2, task3, task4, task5, task6, task7, task8, task9, task10;
    private boolean taskClicked = false;
    private ArrayList<String> taskcompletedList = new ArrayList<>();
    private DialyTaskAdapter adapter;
    private int count =0;
    private int totalwallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialytask);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        MobileAds.initialize(this);

        // Defined in res/values/strings.xml

        initAds();
        initInterstitialAd();
        startGame();
        initWidgit();

        mInterstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame1();
            }
        });
        mInterstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame2();
            }
        });
        mInterstitialAd3.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame3();
            }
        });
        mInterstitialAd4.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame4();
            }
        });
        mInterstitialAd5.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame5();
            }
        });
        mInterstitialAd6.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame6();
            }
        });
        mInterstitialAd7.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame7();
            }
        });
        mInterstitialAd8.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame8();
            }
        });
        mInterstitialAd9.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame9();
            }
        });
        mInterstitialAd10.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame10();
            }
        });

        callTaskApi();

    }

    private void callTaskApi() {
        Utilz.showProgress(DialyTask.this, getResources().getString(R.string.pleasewait));
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        String date = Utilz.getCurrentDateInDigit(DialyTask.this);
        Download_web web = new Download_web(DialyTask.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (response.length() > 0) {

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")) {
                            JSONArray array = object.getJSONArray("data");
                            JSONObject jo = array.getJSONObject(0);
                            setData(jo);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("todaydate", date));
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(DialyTask.this, PreferenceName.USER_ID)));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.DIALYTASKREPORT);
    }

    private void setData(JSONObject response) {
        String todaydata = response.optString("today_date");
        task1 = response.optString("dialy_task_1");
        task2 = response.optString("dialy_task_2");
        task3 = response.optString("dialy_task_3");
        task4 = response.optString("dialy_task_4");
        task5 = response.optString("dialy_task_5");
        task6 = response.optString("dialy_task_6");
        task7 = response.optString("dialy_task_7");
        task8 = response.optString("dialy_task_8");
        task9 = response.optString("dialy_task_9");
        task10 = response.optString("dialy_task_10");

        String completely_today = response.optString("completely_today");

        setButtonText(completely_today);
    }

    private void setButtonText(String completely_today) {
        taskcompletedList.add(task1);
        taskcompletedList.add(task2);
        taskcompletedList.add(task3);
        taskcompletedList.add(task4);
        taskcompletedList.add(task5);
        taskcompletedList.add(task6);
        taskcompletedList.add(task7);
        taskcompletedList.add(task8);
        taskcompletedList.add(task9);
        taskcompletedList.add(task10);

        adapter.notifyDataSetChanged();
        countCompleted(completely_today);
    }

    private void countCompleted(String completely_today) {
        for (int i=0; i<taskcompletedList.size(); i++){
            if (taskcompletedList.get(i).equalsIgnoreCase("completed")){
                count = count+1;
            }
        }
        if (count==10){
            if (completely_today.equalsIgnoreCase("yes")){
                return;
            }
            updateAmount();
        }
    }

    private void updateAmount() {
        String date = Utilz.getCurrentDateInDigit(DialyTask.this);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList();
        Download_web web = new Download_web(DialyTask.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                finish();
                startActivity(getIntent());
                if (response != null && response.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            ClsGeneral.setPreferences(DialyTask.this, PreferenceName.TODAYDATE, object.optString("data") );
                            Toast.makeText(DialyTask.this, "Daily won Bonus", Toast.LENGTH_SHORT).show();
                            ClsGeneral.setPreferences(DialyTask.this, PreferenceName.TOTALAMOUNT, "" + (totalwallet+10  ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(DialyTask.this, PreferenceName.USER_ID)));
        nameValuePairs.add(new BasicNameValuePair("amount", "" + (totalwallet + 10)));
        nameValuePairs.add(new BasicNameValuePair("todaydate", date));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.SETWALLETAMOUNTDIALY);
    }

    private void startGame() {
        startGame1();
        startGame2();
        startGame3();
        startGame4();
        startGame5();
        startGame6();
        startGame7();
        startGame8();
        startGame9();
        startGame10();
    }

    private void initInterstitialAd() {
        mInterstitialAd1.setAdUnitId(getString(R.string.ad_unit_idZeor));
        mInterstitialAd2.setAdUnitId(getString(R.string.ad_unit_idOne));
        mInterstitialAd3.setAdUnitId(getString(R.string.ad_unit_idTwo));
        mInterstitialAd4.setAdUnitId(getString(R.string.ad_unit_idtree));
        mInterstitialAd5.setAdUnitId(getString(R.string.ad_unit_idFour));
        mInterstitialAd6.setAdUnitId(getString(R.string.ad_unit_idFive));
        mInterstitialAd7.setAdUnitId(getString(R.string.ad_unit_idSix));
        mInterstitialAd8.setAdUnitId(getString(R.string.ad_unit_idSeven));
        mInterstitialAd9.setAdUnitId(getString(R.string.ad_unit_idEit));
        mInterstitialAd10.setAdUnitId(getString(R.string.ad_unit_idNine));
    }

    private void initAds() {
        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd4 = new InterstitialAd(this);
        mInterstitialAd5 = new InterstitialAd(this);
        mInterstitialAd6 = new InterstitialAd(this);
        mInterstitialAd7 = new InterstitialAd(this);
        mInterstitialAd8 = new InterstitialAd(this);
        mInterstitialAd9 = new InterstitialAd(this);
        mInterstitialAd10 = new InterstitialAd(this);
    }

    private void initWidgit() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Dialy Task");
        rv_dialytask.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = (new DialyTaskAdapter(DialyTask.this, getTaskList(), taskcompletedList, R.layout.dialytask_row, new OnClickEvent() {
            @Override
            public void onClick(int pos) {

                switch (pos) {
                    case 0:
                        task1 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 1:
                        task2 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 2:
                        task3 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 3:
                        task4 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 4:
                        task5 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 5:
                        task6 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 6:
                        task7 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 7:
                        task8 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 8:
                        task9 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                    case 9:
                        task10 = "completed";
                        taskClicked = true;
                        showAllInterstitial();
                        break;
                }
            }
        }));
        rv_dialytask.setAdapter(adapter);

        try {
            totalwallet = Integer.parseInt(ClsGeneral.getPreferences(DialyTask.this, PreferenceName.TOTALAMOUNT));

        } catch (NumberFormatException e) {
            totalwallet = 0;
        } catch (Exception e) {

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showAllInterstitial() {
        showInterstitial();
        showInterstitia2();
        showInterstitia3();
        showInterstitia4();
        showInterstitia5();
        showInterstitia6();
        showInterstitia7();
        showInterstitia8();
        showInterstitia9();
        showInterstitial0();
    }

    private List<String> getTaskList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add("Task " + i);
        }
        return list;
    }


    private void showInterstitial() {
        if (mInterstitialAd1 != null && mInterstitialAd1.isLoaded()) {
            mInterstitialAd1.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame1();
        }
    }

    private void showInterstitia2() {
        if (mInterstitialAd2 != null && mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.show();
        } else {
            //Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame2();
        }
    }

    private void showInterstitia3() {
        if (mInterstitialAd3 != null && mInterstitialAd3.isLoaded()) {
            mInterstitialAd3.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame3();
        }
    }

    private void showInterstitia4() {
        if (mInterstitialAd4 != null && mInterstitialAd4.isLoaded()) {
            mInterstitialAd4.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame4();
        }
    }

    private void showInterstitia5() {
        if (mInterstitialAd5 != null && mInterstitialAd5.isLoaded()) {
            mInterstitialAd5.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame5();
        }
    }

    private void showInterstitia6() {
        if (mInterstitialAd6 != null && mInterstitialAd6.isLoaded()) {
            mInterstitialAd6.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame6();
        }
    }

    private void showInterstitia7() {
        if (mInterstitialAd7 != null && mInterstitialAd7.isLoaded()) {
            mInterstitialAd7.show();
        } else {
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame7();
        }
    }

    private void showInterstitia8() {
        if (mInterstitialAd8 != null && mInterstitialAd8.isLoaded()) {
            mInterstitialAd8.show();
        } else {
            // Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame8();
        }
    }

    private void showInterstitia9() {
        if (mInterstitialAd9 != null && mInterstitialAd9.isLoaded()) {
            mInterstitialAd9.show();
        } else {
            //Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame9();
        }
    }

    private void showInterstitial0() {
        if (mInterstitialAd10 != null && mInterstitialAd10.isLoaded()) {
            mInterstitialAd10.show();
        } else {
            //  Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            startGame10();
        }
    }

    private void startGame1() {
        if (!mInterstitialAd1.isLoading() && !mInterstitialAd1.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd1.loadAd(adRequest);
        }
    }

    private void startGame2() {
        if (!mInterstitialAd2.isLoading() && !mInterstitialAd2.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd2.loadAd(adRequest);
        }
    }

    private void startGame3() {
        if (!mInterstitialAd3.isLoading() && !mInterstitialAd3.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd3.loadAd(adRequest);
        }
    }

    private void startGame4() {
        if (!mInterstitialAd4.isLoading() && !mInterstitialAd4.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd4.loadAd(adRequest);
        }
    }

    private void startGame5() {
        if (!mInterstitialAd5.isLoading() && !mInterstitialAd5.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd5.loadAd(adRequest);
        }
    }

    private void startGame6() {
        if (!mInterstitialAd6.isLoading() && !mInterstitialAd6.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd6.loadAd(adRequest);
        }
    }

    private void startGame7() {
        if (!mInterstitialAd7.isLoading() && !mInterstitialAd7.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd7.loadAd(adRequest);
        }
    }

    private void startGame8() {
        if (!mInterstitialAd8.isLoading() && !mInterstitialAd8.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd8.loadAd(adRequest);
        }
    }

    private void startGame9() {
        if (!mInterstitialAd9.isLoading() && !mInterstitialAd9.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd9.loadAd(adRequest);
        }
    }

    private void startGame10() {
        if (!mInterstitialAd10.isLoading() && !mInterstitialAd10.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd10.loadAd(adRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (taskClicked) {
            if (Utilz.isInternetConnected(DialyTask.this))
                saveUpdatetaskCompleted();
        }
    }

    private void saveUpdatetaskCompleted() {
        if (count==9)
        Utilz.showProgress(DialyTask.this, getResources().getString(R.string.pleasewait));
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        String date = Utilz.getCurrentDateInDigit(DialyTask.this);
        Download_web web = new Download_web(DialyTask.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (count==9)
                {
                    updateAmount();
                }
                else {
                    finish();
                    startActivity(getIntent());
                }

            }
        });
        nameValuePairs.add(new BasicNameValuePair("task1", task1));
        nameValuePairs.add(new BasicNameValuePair("task2", task2));
        nameValuePairs.add(new BasicNameValuePair("task3", task3));
        nameValuePairs.add(new BasicNameValuePair("task4", task4));
        nameValuePairs.add(new BasicNameValuePair("task5", task5));
        nameValuePairs.add(new BasicNameValuePair("task6", task6));
        nameValuePairs.add(new BasicNameValuePair("task7", task7));
        nameValuePairs.add(new BasicNameValuePair("task8", task8));
        nameValuePairs.add(new BasicNameValuePair("task9", task9));
        nameValuePairs.add(new BasicNameValuePair("task10", task10));
        nameValuePairs.add(new BasicNameValuePair("date", date));
        nameValuePairs.add(new BasicNameValuePair("completely_today", "yes"));
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(DialyTask.this, PreferenceName.USER_ID)));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.INSERTDIALYTASK);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        taskClicked = false;
    }


}