package com.apextechies.kmaaoapp.model;

/**
 * Created by Shankar on 2/11/2018.
 */

public class AmountTransactionModel {
    public String getWallet_transaction_id() {
        return wallet_transaction_id;
    }

    public void setWallet_transaction_id(String wallet_transaction_id) {
        this.wallet_transaction_id = wallet_transaction_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(String transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getTransafered_mobile() {
        return transafered_mobile;
    }

    public void setTransafered_mobile(String transafered_mobile) {
        this.transafered_mobile = transafered_mobile;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    private String wallet_transaction_id,user_id,transaction_amount,transafered_mobile,transaction_date,transaction_time;

    public AmountTransactionModel(String wallet_transaction_id, String user_id, String transaction_amount, String transafered_mobile,
                                  String transaction_date, String transaction_time){
        this.wallet_transaction_id = wallet_transaction_id;
        this.user_id = user_id;
        this.transaction_amount = transaction_amount;
        this.transafered_mobile = transafered_mobile;
        this.transaction_date = transaction_date;
        this.transaction_time = transaction_time;

    }
}
