package com.apextechies.kmaaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.model.DetailsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/26/2018.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private List<DetailsModel> subCatListModels;
    private Context context;
    private int categorylist_row;
    private OnClickEvent onClickListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step)
        TextView tv_step;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public DetailsAdapter(Context context, List<DetailsModel> subCatListModels, int categorylist_row, OnClickEvent onClickListener) {
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
        final DetailsModel pl = subCatListModels.get(position);
        holder.tv_step.setText(pl.getId());


    }



    @Override
    public int getItemCount() {
        return subCatListModels.size();
    }

}
