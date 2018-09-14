package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class myWallet extends AppCompatActivity {

    Context context;
    TextView currency,available,eth,ccb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        context = getApplicationContext().getApplicationContext();
        new Thread(getaccount).start();
        new Thread(setup).start();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        title.setText(R.string.woqian);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myWallet.this,MyActivity.class);
                startActivity(intent);
            }
        });
        currency = (TextView) findViewById(R.id.totalFunds);
        available = (TextView) findViewById(R.id.AvailFunds);
        eth = (TextView) findViewById(R.id.totaleth);
        ccb = (TextView) findViewById(R.id.totalbi);
        Button exBtn = (Button) findViewById(R.id.button6);
        exBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myWallet.this,exchange.class);
                startActivity(intent);
            }
        });

    }

    Handler setupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:currency.setText(R.string.tasset + msg.obj.toString());break;
                case 2:available.setText(R.string.afunds+msg.obj.toString());break;
                case 3:ccb.setText(R.string.tasset + msg.obj.toString());break;
                case 4:eth.setText(R.string.tasset + msg.obj.toString());break;
            }
        }
    };

    Runnable getaccount = new Runnable() {
        @Override
        public void run() {
            AppHelper helper = new AppHelper();
            DynamoDBMapper mapper = helper.getMapper(context);
            String user = helper.getCurrentUserName(context);
            SharedPreferences preferences = getSharedPreferences("ipaddress",0);
            helper.getaccount(preferences,mapper,user);

        }
    };

    Runnable setup = new Runnable() {
        @Override
        public void run() {
            AppHelper helper = new AppHelper();
            DynamoDBMapper mapper = helper.getMapper(context);
            String user = helper.getCurrentUserName(context);
            UserPoolDO userPoolDO = mapper.load(UserPoolDO.class,user);
            Message msg = new Message();
            msg.what=1;
            if(userPoolDO.getCandyCurrency()!=null){
                msg.obj=userPoolDO.getCandyCurrency().intValue();
            }
            else {
                msg.obj=0;
            }
            setupHandler.sendMessage(msg);
            Message msg1 = new Message();
            msg1.what=2;
            if(userPoolDO.getAvailableWallet()!=null){
                msg1.obj=userPoolDO.getAvailableWallet().intValue();
            }
            else {
                msg1.obj=0;
            }
            setupHandler.sendMessage(msg1);

            Message msg2 = new Message();
            msg2.what=3;
            msg2.obj = userPoolDO.getChancecoin();
            setupHandler.sendMessage(msg2);
            Message msg3 = new Message();
            msg3.what = 4;
            msg3.obj = userPoolDO.getEtherum();
            setupHandler.sendMessage(msg3);

        }
    };

}
