package com.example.administrator.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SipAnswerActivity extends Activity {

    private EditText ed_username,ed_password,ed_domain;
    private Button btn_sip_login;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sip_answer_activity);
        ed_username = (EditText)findViewById(R.id.sip_ed_username);
        ed_password = (EditText)findViewById(R.id.sip_ed_password);
        ed_domain   = (EditText)findViewById(R.id.sip_ed_domain);

        SharedPreferences shared = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
        if(shared.getString("namePref","")!=null){
            ed_username.setText(shared.getString("namePref",""));
            ed_password.setText(shared.getString("passPref",""));
            ed_domain.setText(shared.getString("domainPref",""));
        }
        btn_sip_login = (Button)findViewById(R.id.sip_btnn_login);
        btn_sip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                String  domain  =  ed_domain .getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("namePref",username);
                editor.putString("passPref",password);
                editor.putString("domainPref",domain);
                editor.commit();
                finish();
            }
        });
    }

}
