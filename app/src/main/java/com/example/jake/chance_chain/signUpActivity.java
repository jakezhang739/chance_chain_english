package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class signUpActivity extends AppCompatActivity implements AWSLoginHandler {
    private Button finishBtn;
    AWSLoginModel awsLoginModel;
    EditText username,Email;
    private String email,uid;
    private AppHelper helper = new AppHelper();
    private Context context;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = getApplicationContext().getApplicationContext();
        awsLoginModel = new AWSLoginModel(this, this);
        finishBtn = (Button) findViewById(R.id.signUp);
        username = (EditText) findViewById(R.id.editTextRegUserId);
        pass = (EditText) findViewById(R.id.editTextRegUserPassword);
        EditText rePass = (EditText) findViewById(R.id.editTextRegGivenName);
        Email = (EditText) findViewById(R.id.editTextRegEmail);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wtf","1 "+pass.getText().toString()+" 2 "+rePass.getText().toString());
                if(username.length() == 0 || pass.length()==0 || rePass.length() == 0 || Email.length() == 0){
                    Toast.makeText(signUpActivity.this,R.string.enterall,Toast.LENGTH_LONG).show();
                }
                else if(username.length()>20){
                    Toast.makeText(signUpActivity.this,R.string.userToolong,Toast.LENGTH_LONG).show();
                }

               else if(!pass.getText().toString().equals(rePass.getText().toString())){
                    Toast.makeText(signUpActivity.this,R.string.passnotmatch,Toast.LENGTH_LONG).show();
                }
                else if(pass.getText().toString().length()<8){
                    Toast.makeText(signUpActivity.this,R.string.passnotlong,Toast.LENGTH_LONG).show();
                }
                else {
                    finishBtn.setVisibility(View.INVISIBLE);
                    email = Email.getText().toString();
                    new Thread(getEmail).start();
                    //registerAction();
                }
            }
        });
    }

    @Override
    public void onRegisterSuccess(boolean mustConfirmToComplete) {

        Intent intent = new Intent(signUpActivity.this, confirmUser.class);
        intent.putExtra("username",username.getText().toString());
        intent.putExtra("email",Email.getText().toString());
        intent.putExtra("pass",pass.getText().toString());
        startActivity(intent);

    }

    @Override
    public void onRegisterConfirmed() {

    }

    @Override
    public void onSignInSuccess() {

    }

    Runnable getEmail = new Runnable() {
        @Override
        public void run() {
            DynamoDBMapper mapper = helper.getMapper(context);
            UserPoolDO user = new UserPoolDO();
            user.setMyEmail(email);
            UserPoolDO user2 = new UserPoolDO();
            user2.setUserId(username.getText().toString());
            DynamoDBQueryExpression expression = new DynamoDBQueryExpression().withIndexName("GetStuff").withHashKeyValues(user).withConsistentRead(false);
            DynamoDBQueryExpression expression1 = new DynamoDBQueryExpression().withHashKeyValues(user2).withConsistentRead(false);
            if(mapper.query(UserPoolDO.class,expression1).size()+mapper.query(UserPoolDO.class,expression).size()>0){
                if(mapper.query(UserPoolDO.class,expression1).size()>0){
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
                else {
                     Message msg = new Message();
                     msg.what=2;
                     handler.sendMessage(msg);
                }
            }
            else{
                registerAction();
            }

        }
    };



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:Toast.makeText(context,R.string.usernameexit,Toast.LENGTH_LONG).show();finishBtn.setVisibility(View.VISIBLE);break;
                case 2:Toast.makeText(context,R.string.emailused,Toast.LENGTH_LONG).show();finishBtn.setVisibility(View.VISIBLE);break;

            }
        }
    };

    @Override
    public void onFailure(int process, Exception exception) {

        finishBtn.setVisibility(View.INVISIBLE);
        Toast.makeText(signUpActivity.this, "Something is wrong，please fill again", Toast.LENGTH_LONG).show();

    }
    private void registerAction() {
        EditText username = (EditText) findViewById(R.id.editTextRegUserId);
        EditText pass = (EditText) findViewById(R.id.editTextRegUserPassword);
        EditText Email = (EditText) findViewById(R.id.editTextRegEmail);



        // do sign in and handles on interface
        awsLoginModel.registerUser(username.getText().toString(), Email.getText().toString(), pass.getText().toString());
    }
}
