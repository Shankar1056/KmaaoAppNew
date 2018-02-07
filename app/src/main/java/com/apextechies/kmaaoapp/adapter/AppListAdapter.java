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
import com.apextechies.kmaaoapp.model.CategoryDateModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/25/2018.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.MyViewHolder> {

    private List<CategoryDateModel> subCatListModels;
    private Context context;
    private int categorylist_row;
    private OnClickEvent onClickListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_list_item)
        ImageView app_image;
        @BindView(R.id.tv_list_item)
        TextView text_AppName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public AppListAdapter(Context context, List<CategoryDateModel> subCatListModels, int categorylist_row, OnClickEvent onClickListener) {
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
        final CategoryDateModel pl = subCatListModels.get(position);
        holder.text_AppName.setText(pl.getApplication_name());
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