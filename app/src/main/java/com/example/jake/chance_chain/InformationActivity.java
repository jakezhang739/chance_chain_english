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
import android.widget.ImageView;
import android.widget.TextView;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;


public class InformationActivity extends AppCompatActivity {
    DynamoDBMapper dynamoDBMapper;
    private TextView nickView,nameView,chanceView,walletView,genderView, careerView, resumeView;
    private ImageView toImage;
    Context context;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Button changeBtn = (Button) findViewById(R.id.changeBtn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=getApplication().getApplicationContext();
        uId = AppHelper.getCurrentUserName(context);
        nickView = (TextView) findViewById(R.id.usridInp);
        nameView = (TextView) findViewById(R.id.nameInp);
        chanceView = (TextView) findViewById(R.id.chanceInp);
        walletView = (TextView) findViewById(R.id.addressInp);
        genderView = (TextView) findViewById(R.id.sexInp);
        careerView = (TextView) findViewById(R.id.careerInp);
        resumeView = (TextView) findViewById(R.id.resumeInp);
        toImage = (ImageView) findViewById(R.id.touxiang);

        dynamoDBMapper=AppHelper.getMapper(context);


        Log.d("wtf","uu"+dynamoDBMapper.toString());
        getAtr();
        changeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, changeInformationActivity.class);
                startActivity(intent);
            }
        });

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String str;
            if(msg.obj!=null){
                str=msg.obj.toString();
            }
            else{
                str=null;
            }
            int index = msg.what;
            switch (index){
                case 1:nickView.setText(str);
                break;
                case 2:nameView.setText(str);
                break;
                case 3:chanceView.setText(str);
                break;
                case 4:walletView.setText(str);
                break;
                case 5:genderView.setText(str);
                break;
                case 6:careerView.setText(str);
                break;
                case 7:resumeView.setText(str);
                break;
                case 8: Log.d("try8 ",str);Picasso.get().load(str).into(toImage);
                break;
            }
        }
    };





    public void getAtr(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
                Message msgNick = Message.obtain();
                Message msgName = Message.obtain();
                Message msgChance = Message.obtain();
                Message msgWallet = Message.obtain();
                Message msgGender = Message.obtain();
                Message msgCareer = Message.obtain();
                Message msgResume = Message.obtain();
                Message msgPro = Message.obtain();
                msgNick.what= 1;
                msgName.what = 2;
                msgChance.what = 3;
                msgWallet.what = 4;
                msgGender.what = 5;
                msgCareer.what = 6;
                msgResume.what = 7;
                msgPro.what=8;



                if(userPoolDO != null) {
                    Log.d("wtf", "sdf3" + userPoolDO.toString());
                    Log.d("wtf", "career" + userPoolDO.getChanceId());
                    msgNick.obj = userPoolDO.getNickName();
                    msgName.obj=userPoolDO.getName();
                    msgChance.obj=userPoolDO.getChanceId();
                    msgWallet.obj=userPoolDO.getWalletAddress();
                    msgGender.obj = userPoolDO.getGender();
                    msgCareer.obj=userPoolDO.getCareer();
                    msgResume.obj=userPoolDO.getResume();
                    handler.sendMessage(msgNick);
                    handler.sendMessage(msgName);
                    handler.sendMessage(msgChance);
                    handler.sendMessage(msgWallet);
                    handler.sendMessage(msgGender);
                    handler.sendMessage(msgCareer);
                    handler.sendMessage(msgResume);
                }
                if(userPoolDO.getProfilePic()!=null){
                    msgPro.obj=userPoolDO.getProfilePic();
                    handler.sendMessage(msgPro);

                }
            }
        }).start();

    }




}
