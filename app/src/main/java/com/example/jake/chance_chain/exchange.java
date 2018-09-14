package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import org.w3c.dom.Text;

public class exchange extends AppCompatActivity {
    Double candy,ccb;
    Context context;
    DynamoDBMapper mapper;
    SharedPreferences preferences;
    AppHelper helper = new AppHelper();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        context = getApplicationContext().getApplicationContext();
        preferences = getSharedPreferences("ipaddress",0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        title.setText(R.string.ex);
        mapper = helper.getMapper(context);
        userID = helper.getCurrentUserName(context);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout mainRel = (RelativeLayout) findViewById(R.id.exhange);
        EditText candyText = (EditText) findViewById(R.id.editText4);
        EditText ccbText = (EditText) findViewById(R.id.editText5);
        TextView ccbView = (TextView) findViewById(R.id.textView11);
        TextView ethView = (TextView) findViewById(R.id.textView12);
        ImageView candyImg = (ImageView) findViewById(R.id.imageView14);
        ImageView ccbImg = (ImageView) findViewById(R.id.imageView18);
        ImageView ccbNext = (ImageView) findViewById(R.id.imageView19);
        ImageView ethImg = (ImageView) findViewById(R.id.imageView21);
        Button excBtn = (Button) findViewById(R.id.button8);
        Button topBtn = (Button) findViewById(R.id.button9);
        candyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candyImg.setVisibility(View.INVISIBLE);
                candyText.setVisibility(View.VISIBLE);
            }
        });
        ccbNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccbImg.setVisibility(View.INVISIBLE);
                ccbText.setVisibility(View.VISIBLE);
            }
        });
        mainRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candyImg.setVisibility(View.VISIBLE);
                candyText.setVisibility(View.INVISIBLE);
                ccbText.setVisibility(View.INVISIBLE);
                ccbImg.setVisibility(View.VISIBLE);
                ccbView.setVisibility(View.INVISIBLE);
                ccbNext.setVisibility(View.VISIBLE);
                ethView.setVisibility(View.INVISIBLE);
                ethImg.setVisibility(View.VISIBLE);
            }
        });
        candyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ccbView.setVisibility(View.VISIBLE);
                Double amount = Double.parseDouble(s.toString());
                ccbView.setText(String.valueOf(amount/100));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ccbText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ethView.setVisibility(View.VISIBLE);
                ethView.setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        excBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                candy = Double.parseDouble(candyText.getText().toString())/100;
                candyImg.setVisibility(View.VISIBLE);
                candyText.setVisibility(View.INVISIBLE);
                ccbImg.setVisibility(View.VISIBLE);
                ccbView.setVisibility(View.INVISIBLE);
                new Thread(exchange).start();

            }
        });
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ccb = Double.parseDouble(ccbText.getText().toString());
                ccbNext.setVisibility(View.VISIBLE);
                ethView.setVisibility(View.INVISIBLE);
                ethImg.setVisibility(View.VISIBLE);
                ccbText.setVisibility(View.INVISIBLE);
                new Thread(topup).start();

            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1: Toast.makeText(context,"Transfer CC successful, the hash is "+msg.obj.toString(),Toast.LENGTH_LONG).show();break;
                case 2: Toast.makeText(context,"Transfer CC failed",Toast.LENGTH_LONG).show();break;
                case 3: Toast.makeText(context,"Transfer eth successful, the hash is "+msg.obj.toString(),Toast.LENGTH_LONG).show();break;
                case 4: Toast.makeText(context,"Transfer eth failed",Toast.LENGTH_LONG).show();break;
            }

        }
    };

    Runnable exchange = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            String hascode;
            UserPoolDO userPoolDO = mapper.load(UserPoolDO.class,userID);
            if(userPoolDO.getWalletAddress()!=null) {
                hascode = helper.transfercc(preferences, mapper, userID,candy,userPoolDO.getWalletAddress());
                if(hascode.equals("error")){
                    msg.what=2;
                    handler.sendMessage(msg);
                }
                else {
                    msg.what = 1;
                    msg.obj = hascode;
                    handler.sendMessage(msg);
                }
            }
            else{
                msg.what=2;
                handler.sendMessage(msg);
            }
            mapper.save(userPoolDO);

        }
    };

    Runnable topup = new Runnable() {
        @Override
        public void run() {
            String hascode;
            Message msg = new Message();
            UserPoolDO userPoolDO = mapper.load(UserPoolDO.class,userID);
            if(userPoolDO.getWalletAddress()!=null) {
                hascode = helper.transfereth(preferences, mapper, userID,candy,userPoolDO.getWalletAddress());
                if(hascode.equals("error")){
                    msg.what=4;
                    handler.sendMessage(msg);
                }
                else {
                    msg.what = 3;
                    msg.obj = hascode;
                    handler.sendMessage(msg);
                }
            }
            else{
                msg.what=4;
                handler.sendMessage(msg);
            }
            mapper.save(userPoolDO);
        }
    };
}
