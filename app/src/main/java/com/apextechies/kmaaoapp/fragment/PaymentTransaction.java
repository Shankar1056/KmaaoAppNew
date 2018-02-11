package com.apextechies.kmaaoapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.adapter.AmountTransactionAdapter;
import com.apextechies.kmaaoapp.allInterface.OnClickEvent;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.model.AmountTransactionModel;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shankar on 2/11/2018.
 */

@SuppressLint("ValidFragment")
public class PaymentTransaction extends Fragment {
    private RecyclerView rv_wallettransaction;
    private ArrayList<AmountTransactionModel> arrayList= new ArrayList<>();

    public PaymentTransaction(String s) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_wallettransaction = (RecyclerView)view.findViewById(R.id.rv_wallettransaction);
        rv_wallettransaction.setLayoutManager(new LinearLayoutManager(getActivity()));

        callApi();
    }

    private void callApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Utilz.showProgress(getActivity(), getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz.dismissProgressDialog();
                if (response!=null && response.length()>0){

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            JSONArray array = object.getJSONArray("data");
                            for (int i=0;i<array.length();i++){
                                JSONObject jo = array.getJSONObject(i);
                                arrayList.add(new AmountTransactionModel(jo.optString("wallet_transaction_id"),jo.optString("user_id")
                                ,jo.optString("transaction_amount"),jo.optString("transafered_mobile"),jo.optString("transaction_date"),
                                        jo.optString("transaction_time")));
                            }
                            rv_wallettransaction.setAdapter(new AmountTransactionAdapter(getActivity(), arrayList, R.layout.amounttrans_row, new OnClickEvent() {
                                @Override
                                public void onClick(int pos) {

                                }
                            }));
                        }else {
                            Toast.makeText(getActivity(), ""+object.optString("msg"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(getActivity(), PreferenceName.USER_ID)));
        web.setReqType(false);
        web.setData(nameValuePairs);
        web.execute(WebService.WALLEHTISTORY);
    }
}
