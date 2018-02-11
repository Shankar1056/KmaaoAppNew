package com.apextechies.kmaaoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.model.AmountTransactionModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 2/11/2018.
 */

public class AmountTransactionAdapter extends RecyclerView.Adapter<AmountTransactionAdapter.MyViewHolder> {

    private List<AmountTransactionModel> subCatListModels;
    private Context context;
    private int categorylist_row;
    private OnClickEvent onClickListener ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mobilenumber)
        TextView mobilenumber;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.datetime)
        TextView datetime;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public AmountTransactionAdapter(Context context, List<AmountTransactionModel> subCatListModels, int categorylist_row, OnClickEvent onClickListener) {
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
        final AmountTransactionModel pl = subCatListModels.get(position);
        holder.mobilenumber.setText(pl.getTransafered_mobile());
        holder.amount.setText("₹"+pl.getTransaction_amount());
      holder.datetime.setText(pl.getTransaction_date()+" , "+pl.getTransaction_time());


    }



    @Override
    public int getItemCount() {
        return subCatListModels.size();
    }

}
