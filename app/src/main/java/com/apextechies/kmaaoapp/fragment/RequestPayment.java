package com.apextechies.kmaaoapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.activity.WalletActivity;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shankar on 2/11/2018.
 */

@SuppressLint("ValidFragment")
public class RequestPayment extends Fragment {
   private EditText paytmNumber;
    private  EditText amount;
    private Button request;
    private int wallet;

    public RequestPayment(String s) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_payment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       wallet =  Integer.parseInt(ClsGeneral.getPreferences(getActivity(), PreferenceName.TOTALAMOUNT));
        paytmNumber = (EditText)view.findViewById(R.id.paytmNumber);
        amount = (EditText)view.findViewById(R.id.amount);
        request = (Button)view.findViewById(R.id.request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestedamount = Integer.parseInt(amount.getText().toString());
                if (paytmNumber.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Enter your Paytm Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (amount.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (requestedamount>=wallet){
                    Toast.makeText(getActivity(), "You are requesting an invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    
                    int walletAmount = wallet-requestedamount;
                    callRequestApi(walletAmount);
                }
            }
        });

    }



    private void callRequestApi(final int walletAmount) {
        String time = Utilz.getCurrentTime(getActivity());
        String date = Utilz.getCurrentDateInDigit(getActivity());
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        Utilz.showProgress(getActivity(), getResources().getString(R.string.pleasewait));
        Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                Utilz. dismissProgressDialog();
                if (response!=null && response.length()>0){

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("status").equalsIgnoreCase("true")){
                            paytmNumber.setText("");
                            amount.setText("");
                            Toast.makeText(getActivity(), ""+object.optString("data"), Toast.LENGTH_SHORT).show();
                            ClsGeneral.setPreferences(getActivity(), PreferenceName.TOTALAMOUNT, ""+walletAmount);
                            ((WalletActivity) getActivity()).setwalletAmount(""+walletAmount);
                        }else {
                            Toast.makeText(getActivity(), ""+object.optString("msg"), Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("wallet_id",""));
        nameValuePairs.add(new BasicNameValuePair("number",paytmNumber.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(getActivity(), PreferenceName.USER_ID)));
        nameValuePairs.add(new BasicNameValuePair("requested_amount", amount.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("wallet_amount", ""+walletAmount));
        nameValuePairs.add(new BasicNameValuePair("requested_date", date));
        nameValuePairs.add(new BasicNameValuePair("requested_time", time));
        web.setReqType(false);
        web.setData(nameValuePairs);
        web.execute(WebService.REQUESTAMOUNT);
    }
}
