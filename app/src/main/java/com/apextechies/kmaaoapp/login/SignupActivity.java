package com.apextechies.kmaaoapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.activity.MainActivity;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.common.PreferenceName;
import com.apextechies.kmaaoapp.model.UserDetails;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.Utilz;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shankar on 1/26/2018.
 */

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private String android_id = null;
    private Date currentTime;
    private String currentDate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        getDeviceUniqueId();
    }

    private void getDeviceUniqueId() {
         android_id = Settings.Secure.getString(SignupActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        currentDate = formatter.format(date);

    }

    @OnClick(R.id.button)
    void onButtonClick(){
        if (et_name.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(SignupActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (et_email.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(SignupActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utilz.isValidEmail1(et_email.getText().toString().trim()))
        {
            Toast.makeText(SignupActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        callSignupApi();
    }

    private void callSignupApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        progress_bar.setVisibility(View.VISIBLE);
        Download_web web = new Download_web(SignupActivity.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                progress_bar.setVisibility(View.GONE);
                if (response != null && response.length() > 0) {

                    Gson gson = new Gson();
                    UserDetails details = gson.fromJson(response,UserDetails.class);
                    setUserDetails(details);
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("user_id", getIntent().getStringExtra("id")));
        nameValuePairs.add(new BasicNameValuePair("device_unique_id", android_id));
        nameValuePairs.add(new BasicNameValuePair("user_name", et_name.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("user_email", et_email.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("device_token", ""));
        nameValuePairs.add(new BasicNameValuePair("user_created_date", currentDate));
        nameValuePairs.add(new BasicNameValuePair("user_status", ""+true));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.SIGNUP);
    }

    private void setUserDetails(UserDetails details) {
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.USER_ID, details.getData().get(0).getUser_id());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.USER_NAME, details.getData().get(0).getUser_name());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.USER_EMAIL, details.getData().get(0).getUser_email());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.USER_PHONE, details.getData().get(0).getUser_phone());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.DEVICE_UNIQUE_ID, details.getData().get(0).getDevice_unique_id());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.DEVICE_TOKEN, details.getData().get(0).getDevice_token());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.CREATED_DATE, details.getData().get(0).getUser_created_date());
        ClsGeneral.setPreferences(SignupActivity.this, PreferenceName.USER_STATUS, details.getData().get(0).getUser_status());

        startActivity(new Intent(SignupActivity.this,MainActivity.class));
        finish();
    }

}
