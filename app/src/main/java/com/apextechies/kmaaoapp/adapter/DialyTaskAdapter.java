package com.apextechies.kmaaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 2/18/2018.
 */

public class DialyTaskAdapter extends RecyclerView.Adapter<DialyTaskAdapter.MyViewHolder> {

    private List<String> subCatListModels;
    private Context context;
    private int categorylist_row;
    private OnClickEvent onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_taskname)
        TextView tv_taskname;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public DialyTaskAdapter(Context context, List<String> subCatListModels, int categorylist_row, OnClickEvent onClickListener) {
        this.context = context;
        this.subCatListModels = subCatListModels;
        this.categorylist_row = categorylist_row;
        this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(categorylist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_taskname.setText(subCatListModels.get(position));
        //Glide.with(context).load(pl.getImage()).into(holder.app_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return subCatListModels.size();
    }
}

