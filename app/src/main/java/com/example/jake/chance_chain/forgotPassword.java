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

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

public class forgotPassword extends AppCompatActivity implements AWSLoginHandler{
    AWSLoginModel awsLoginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        awsLoginModel = new AWSLoginModel(this, this);

        Button getVerBtn =(Button) findViewById(R.id.button3);
        Button confirmBtn = (Button) findViewById(R.id.ForgotPassword_button);
        EditText userName = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        EditText verCode = (EditText) findViewById(R.id.confirmcode);
        EditText newPass = (EditText) findViewById(R.id.newPass);


        getVerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.length() == 0) {
                    Toast.makeText(forgotPassword.this, R.string.empyou, Toast.LENGTH_LONG).show();

                } else {

                    resetPass();

                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verCode.length() == 0 || newPass.length() == 0) {
                    Toast.makeText(forgotPassword.this, R.string.empyan, Toast.LENGTH_LONG).show();

                } else {
                    confirmPass();

                }
            }
        });
    }


    public void resetPass(){
        EditText userName = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        awsLoginModel.paswordReset(userName.getText().toString());

    }

    public void confirmPass(){
        EditText userName = (EditText) findViewById(R.id.editTextForgotPasswordPass);
        EditText verCode = (EditText) findViewById(R.id.confirmcode);
        EditText newPass = (EditText) findViewById(R.id.newPass);
        awsLoginModel.confirmPass(userName.getText().toString(),newPass.getText().toString(),verCode.getText().toString());
    }

    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

    }

    @Override
    public void onRegisterConfirmed() {
        //Toast.makeText(forgotPassword.this, "已发送验证码" , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSignInSuccess() {
        Toast.makeText(forgotPassword.this, R.string.compchange , Toast.LENGTH_LONG).show();
        Intent intent = new Intent(forgotPassword.this,LoginActivity.class);
        startActivity(intent);


    }

    @Override
    public void onFailure(int process, Exception exception) {

    if(process == 4){
            Toast.makeText(forgotPassword.this, R.string.changefail , Toast.LENGTH_LONG).show();
        }

    }
}
