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
    String userid,email;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user);
        awsLoginModel = new AWSLoginModel(this, this);
        Button confUser = (Button) findViewById(R.id.button2);
        userid=getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        context = getApplicationContext().getApplicationContext();
        confUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Intent intent = new Intent(confirmUser.this, LoginActivity.class);
        new Thread(CreateNewUser).start();
        startActivity(intent);

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

    }

    @Override
    public void onFailure(int process, Exception exception) {

        Toast.makeText(confirmUser.this, R.string.conf + exception.getMessage(), Toast.LENGTH_LONG).show();


    }

    private void confirmAction() {
        EditText confirmationCode = (EditText) findViewById(R.id.editText2);
        EditText userId = (EditText) findViewById(R.id.editText);
        Log.d("wobushieshili","1"+confirmationCode.getText().toString());

        // do confirmation and handles on interface
        awsLoginModel.confirmRegistration(confirmationCode.getText().toString(),userId.getText().toString());
    }
}
