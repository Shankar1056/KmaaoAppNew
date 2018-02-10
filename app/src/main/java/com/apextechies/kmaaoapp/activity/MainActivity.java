package com.apextechies.kmaaoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.AppListAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.model.CategoryModel;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_applist)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initWidgit();
        callCategoryApi();
    }

    private void callCategoryApi() {
        Utilz.showProgress(MainActivity.this, getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(MainActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (response!=null && response.length()>0){
                    Gson gson = new Gson();
                    final CategoryModel details = gson.fromJson(response,CategoryModel.class);
                    final AppListAdapter adapter = new AppListAdapter(MainActivity.this, details.getData(), R.layout.applist_row, new OnClickEvent() {
                        @Override
                        public void onClick(int pos) {
                            startActivity(new Intent(MainActivity.this, DetailsActivity.class).
                                    putExtra("id", details.getData().get(pos).getApplication_id()).
                                    putExtra("link", details.getData().get(pos).getApplication_play_store_link()).
                                    putExtra("name", details.getData().get(pos).getApplication_name())
                            );
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        web.setReqType(true);
        web.execute(WebService.CATEGORY);
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu){

            getMenuInflater().inflate(R.menu.menu_main, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){

            switch(item.getItemId()){
                case R.id.menu_item:   //this item has your app icon
                    return true;



                default: return super.onOptionsItemSelected(item);
            }
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu){
            menu.findItem(R.id.menu_item).setEnabled(false);

            return super.onPrepareOptionsMenu(menu);
        }

    private void initWidgit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }



}
