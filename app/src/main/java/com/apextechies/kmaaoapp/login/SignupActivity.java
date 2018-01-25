package com.apextechies.kmaaoapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apextechies.kmaaoapp.R;
import com.apextechies.kmaaoapp.activity.MainActivity;
import com.apextechies.kmaaoapp.common.ClsGeneral;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                else
                {
                    ClsGeneral.setPreferences(SignupActivity.this,"user_id","1");
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }
}
