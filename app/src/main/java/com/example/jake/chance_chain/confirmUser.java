package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class confirmUser extends AppCompatActivity implements AWSLoginHandler{
    AWSLoginModel awsLoginModel;
    String userid,email,password;
    Context context;
    Button confUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user);
        awsLoginModel = new AWSLoginModel(this, this);
        confUser = (Button) findViewById(R.id.button2);
        userid=getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("pass");
        context = getApplicationContext().getApplicationContext();
        confUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confUser.setVisibility(View.INVISIBLE);
                confirmAction();
            }
        });
    }

    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

    }

    @Override
    public void onRegisterConfirmed() {
        Toast.makeText(confirmUser.this, R.string.succonf, Toast.LENGTH_LONG).show();
        new Thread(CreateNewUser).start();
        LoginAction();

    }

    Runnable CreateNewUser = new Runnable() {
        @Override
        public void run() {
            AppHelper helper = new AppHelper();
            DynamoDBMapper mapper = helper.getMapper(context);
            final UserPoolDO userPoolDO = new UserPoolDO();
            userPoolDO.setUserId(userid);
            userPoolDO.setMyEmail(email);
            mapper.save(userPoolDO);
        }
    };

    @Override
    public void onSignInSuccess() {
        confirmUser.this.startActivity(new Intent(confirmUser.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void onFailure(int process, Exception exception) {
        confUser.setVisibility(View.VISIBLE);
        Toast.makeText(confirmUser.this, R.string.conf + exception.getMessage(), Toast.LENGTH_LONG).show();


    }

    private void confirmAction() {
        EditText confirmationCode = (EditText) findViewById(R.id.editText2);
        Log.d("wobushieshili","1"+confirmationCode.getText().toString());

        // do confirmation and handles on interface
        awsLoginModel.confirmRegistration(confirmationCode.getText().toString(),userid);
    }

    private void LoginAction(){

        awsLoginModel.signInUser(userid, password);


    }

}
