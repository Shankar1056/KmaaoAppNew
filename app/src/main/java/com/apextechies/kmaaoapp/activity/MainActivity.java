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
import com.apextechies.kmaaoapp.model.AppListModel;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

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
        recyclerView.setAdapter(new AppListAdapter(this, getList(), R.layout.applist_row, new OnClickEvent() {
            @Override
            public void onClick(int pos) {

                startActivity(new Intent(MainActivity.this,DetailsActivity.class).putExtra("pos",pos));
            }
        }));
    }

    private List<AppListModel> getList() {
        ArrayList<AppListModel> list = new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            list.add(new AppListModel(""+i,"Application "+i,"http://shirehallmonmouth.org.uk/wp-content/uploads/2017/02/cropped-fb-logo.png"));
        }
        return list;
    }


}
