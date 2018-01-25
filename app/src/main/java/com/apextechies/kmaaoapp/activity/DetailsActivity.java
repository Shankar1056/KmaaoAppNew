package com.apextechies.kmaaoapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.DetailsAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.model.DetailsModel;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initWidgit();
    }

    private void initWidgit() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        int pos = getIntent().getIntExtra("pos",0);
        toolbar.setTitle("Application"+pos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DetailsAdapter(this, getList(), R.layout.appdetails_row, new OnClickEvent() {
            @Override
            public void onClick(int pos) {

            }
        }));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<DetailsModel> getList() {
        ArrayList<DetailsModel> list = new ArrayList<>();
        for (int i=0;i<10;i++)
        {
            list.add(new DetailsModel("Step-"+i));
        }
        return list;
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
}
