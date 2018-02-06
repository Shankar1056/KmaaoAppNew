package com.apextechies.kmaaoapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.activity.MainActivity;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.apextechies.kmaaoapp.model.UserDetails;
import com.apextechies.kmaaoapp.utilz.Download_web;
import com.apextechies.kmaaoapp.utilz.OnTaskCompleted;
import com.apextechies.kmaaoapp.utilz.WebService;
import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shankar on 1/25/2018.
 */

public class Login extends AppCompatActivity {
    public static final int RC_SIGN_IN = 111;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        holdScreenForSOmeTime();
    }
    private void holdScreenForSOmeTime() {
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {

                    sleep(1000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    checkSession();
                }
            }
        };
        timer.start();
    }
    private void checkSession() {
        AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
                new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                        if (u != null) {
                            callApi(u.getPhoneNumber());
                        } else {
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setProviders(
                                                    //  Arrays.asList(
                                                    Collections.singletonList(
                                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                                    )
                                            )
                                            .setLogo(R.mipmap.ic_launcher)
                                            .build(),
                                    RC_SIGN_IN);
                        }
                    }
                }).addOnFailureListener(this,
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                if (ClsGeneral.getPreferences(Login.this, "user_id").equalsIgnoreCase("")) {
                    startActivity(new Intent(Login.this, SignupActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                callApi(response.getPhoneNumber());
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User");
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                }
            }
            Log.e("Login", "Unknown sign in response");

        }
    }

    private void callApi(String number) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        progress_bar.setVisibility(View.VISIBLE);
        Download_web web = new Download_web(Login.this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String response) {
                progress_bar.setVisibility(View.GONE);
                if (response != null && response.length() > 0) {

                    Gson gson = new Gson();
                    UserDetails details = gson.fromJson(response,UserDetails.class);
                    if (details.getStatus().equalsIgnoreCase("true")){
                        if (details.getData().get(0).getUser_email().equalsIgnoreCase("")){
                            startActivity(new Intent(Login.this,SignupActivity.class));
                            finish();
                        }else if (details.getData().get(0).getDevice_unique_id().equalsIgnoreCase("")){
                            Toast.makeText(Login.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            startActivity(new Intent(Login.this,MainActivity.class));
                            finish();
                        }
                    }
                }
            }
        });
        nameValuePairs.add(new BasicNameValuePair("mobile", number));
        web.setData(nameValuePairs);
        web.setReqType(false);
        web.execute(WebService.LOGIN);

    }

}