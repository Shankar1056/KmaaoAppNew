package com.apextechies.kmaaoapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.activity.MainActivity;
import com.apextechies.kmaaoapp.common.ClsGeneral;
import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

/**
 * Created by Shankar on 1/25/2018.
 */

public class Login extends AppCompatActivity {
    public static final int RC_SIGN_IN = 111;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkSession();
    }
    private void checkSession() {
        AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
                new OnSuccessListener() {

                    @Override
                    public void onSuccess(Object o) {
                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                        if (u != null) {

                            if (ClsGeneral.getPreferences(Login.this,"user_id").equalsIgnoreCase(""))
                            {
                                startActivity(new Intent(Login.this, SignupActivity.class));
                                finish();
                            }
                            else
                            {
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }

                            // callApi(Utilz.getNumber(u.getPhoneNumber()));

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
                if (ClsGeneral.getPreferences(Login.this,"user_id").equalsIgnoreCase(""))
                {
                    startActivity(new Intent(Login.this, SignupActivity.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                // callApi(Utilz.getNumber(response.getPhoneNumber()));
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

}