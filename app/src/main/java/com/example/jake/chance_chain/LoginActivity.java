package com.example.jake.chance_chain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements AWSLoginHandler {

    private Button loginBtn;
    private TextView signupText,forgotText;
    AWSLoginModel awsLoginModel;
    Boolean Show = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView toggleShow = (ImageView) findViewById(R.id.imageView7);
        loginBtn = (Button) findViewById(R.id.buttonLogIn);
        signupText = (TextView) findViewById(R.id.textViewUserSignUp);
        forgotText = (TextView) findViewById(R.id.textViewUserForgotPassword);
        awsLoginModel = new AWSLoginModel(this, this);
        EditText password = (EditText) findViewById(R.id.editTextUserPassword);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAction();
            }
        });
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,signUpActivity.class);
                startActivity(intent);
            }
        });
        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,forgotPassword.class);
                startActivity(intent);
            }
        });
        toggleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Show == false){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    Show = true;
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.length());
                    Show = false;
                }

            }
        });

    }

    private void LoginAction(){
        EditText login = (EditText) findViewById(R.id.editTextUserId);
        EditText password = (EditText) findViewById(R.id.editTextUserPassword);

        awsLoginModel.signInUser(login.getText().toString(), password.getText().toString());


    }




    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

    }

    @Override
    public void onRegisterConfirmed() {

    }

    @Override
    public void onSignInSuccess() {
        LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    @Override
    public void onFailure(int process, Exception exception) {

        exception.printStackTrace();

        Toast.makeText(LoginActivity.this, "Login fail, Please enter agin", Toast.LENGTH_LONG).show();

    }
}
