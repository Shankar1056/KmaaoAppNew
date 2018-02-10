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
import com.apextechies.kmaaoapp.model.DetailsModelData;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private List<DetailsModelData> subCatListModels;
    private Context context;
    private int categorylist_row;
    private OnClickEvent onClickListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step)
        TextView tv_step;
        @BindView(R.id.rules)
        TextView rules;
        @BindView(R.id.rulespic)
        ImageView rulespic;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public DetailsAdapter(Context context, List<DetailsModelData> subCatListModels, int categorylist_row, OnClickEvent onClickListener) {
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
        final DetailsModelData pl = subCatListModels.get(position);
        holder.tv_step.setText(pl.getStpes());
        holder.rules.setText(pl.getApplication_rules());
        if (pl.getRules_image()!=null && pl.getRules_image().length()>0) {
            Glide.with(context).load(pl.getRules_image()).into(holder.rulespic);
        }
        else {
            holder.rulespic.setVisibility(View.GONE);
        }


    }



    @Override
    public int getItemCount() {
        return subCatListModels.size();
    }

}
