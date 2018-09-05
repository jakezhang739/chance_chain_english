package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HisActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private DynamoDBMapper dynamoDBMapper;
    private AppHelper helper = new AppHelper();
    private Context context;
    private String userName,myUsr;
    private HisFragment fragment = new HisFragment();
    private TextView usernameText,userResume,guanzhu,beiguanzhu,fabu,shenText;
    private ImageView userPic;
    TextView followTxt,unfollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his);
        context = getApplication().getApplicationContext();
        dynamoDBMapper = helper.getMapper(context);
        myUsr = helper.getCurrentUserName(context);
        userName = getIntent().getStringExtra("userName");
        usernameText = (TextView) findViewById(R.id.wodeUser);
        userResume = (TextView) findViewById(R.id.wodeJian);
        guanzhu = (TextView) findViewById(R.id.guanzhuNum);
        beiguanzhu = (TextView) findViewById(R.id.beiGuanNum);
        fabu = (TextView) findViewById(R.id.woFabuNum);
        shenText = (TextView) findViewById(R.id.woshengwang);
        usernameText.setText(userName);
        userPic = (ImageView) findViewById(R.id.wodetouxiang);
        new Thread(setUp).start();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hisThread hisT = new hisThread(this,dynamoDBMapper,fragmentTransaction,fragment,userName);
        hisT.start();
        RelativeLayout sendMsg = (RelativeLayout) findViewById(R.id.secondBar);
        RelativeLayout follow  = (RelativeLayout) findViewById(R.id.firstBar);
        RelativeLayout hisFollow = (RelativeLayout) findViewById(R.id.thirdBar);
        followTxt = (TextView) findViewById(R.id.zhuanBar);
        unfollow = (TextView) findViewById(R.id.followed);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(followRun).start();
            }
        });
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HisActivity.this,chattingActivity.class);
                intent.putExtra("title",userName);
                startActivity(intent);
            }
        });
        hisFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HisActivity.this,hisfollow.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
            }
        });
    }

    Handler pHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    Picasso.get().load(msg.obj.toString()).resize(60,60).centerCrop().into(userPic);break;
                case 2:userResume.setText(msg.obj.toString());break;
                case 3:shenText.setText(msg.obj.toString());break;
                case 4:guanzhu.setText(msg.obj.toString());break;
                case 5:beiguanzhu.setText(msg.obj.toString());break;
                case 6:fabu.setText(msg.obj.toString());break;
                case 7:followTxt.setVisibility(View.INVISIBLE);unfollow.setVisibility(View.VISIBLE);break;
                case 8:followTxt.setVisibility(View.VISIBLE);unfollow.setVisibility(View.INVISIBLE);break;
            }
        }

    };

    Runnable followRun = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,myUsr);
            UserPoolDO hisUser = dynamoDBMapper.load(UserPoolDO.class,userName);
            List<String> temp = new ArrayList<>();
            Message message = new Message();
            if(userPoolDO.getGuanZhu()==null){
                temp.add(userName);
                userPoolDO.setGuanZhu(temp);
                message.what=7;
                pHandler.sendMessage(message);
                Log.d("wtffff","1");
                List<String> hisTemp = new ArrayList<>();
                if(hisUser.getBeiGuanZhu()==null){
                    hisTemp.add(myUsr);
                    hisUser.setBeiGuanZhu(hisTemp);
                }
                else {
                    hisTemp = hisUser.getBeiGuanZhu();
                    hisTemp.add(myUsr);
                    hisUser.setBeiGuanZhu(hisTemp);
                }
            }
            else if(userPoolDO.getGuanZhu().contains(userName)){
                temp = userPoolDO.getGuanZhu();
                temp.remove(userName);
                userPoolDO.setGuanZhu(temp);
                message.what=8;
                pHandler.sendMessage(message);
                Log.d("wtffff","2");
                List<String> hisTemp = new ArrayList<>();
                hisTemp = hisUser.getBeiGuanZhu();
                hisTemp.remove(myUsr);
                hisUser.setBeiGuanZhu(hisTemp);
            }
            else {
                temp = userPoolDO.getGuanZhu();
                temp.add(userName);
                userPoolDO.setGuanZhu(temp);
                message.what=7;
                pHandler.sendMessage(message);
                List<String> hisTemp = new ArrayList<>();
                if(hisUser.getBeiGuanZhu()==null){
                    hisTemp.add(myUsr);
                    hisUser.setBeiGuanZhu(hisTemp);
                }
                else {
                    hisTemp = hisUser.getBeiGuanZhu();
                    hisTemp.add(myUsr);
                    hisUser.setBeiGuanZhu(hisTemp);
                }
                Log.d("wtffff","3");
            }
            dynamoDBMapper.save(userPoolDO);
            dynamoDBMapper.save(hisUser);
        }
    };

    Runnable setUp = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userName);
            UserPoolDO myUserPool = dynamoDBMapper.load(UserPoolDO.class,myUsr);
            if(userPoolDO.getProfilePic()==null){
                Message msg =new Message();
                msg.what=0;
                pHandler.sendMessage(msg);

            }
            else{
                Message msg =new Message();
                msg.what = 1;
                msg.obj = userPoolDO.getProfilePic();
                pHandler.sendMessage(msg);
            }
            if(userPoolDO.getResume()==null){
                Message msg =new Message();
                msg.what = 2;
                msg.obj = 0;
                pHandler.sendMessage(msg);
            }
            else{
                Message msg =new Message();
                msg.what=2;
                msg.obj=userPoolDO.getResume();
                pHandler.sendMessage(msg);
            }
            if(userPoolDO.getShengWang()==null){
                Message msg =new Message();
                msg.what=3;
                msg.obj="Reputation：0";
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=3;
                String str = "Reputation： ";
                str+=userPoolDO.getShengWang();
                msg.obj=str;
                pHandler.sendMessage(msg);
            }
            if(userPoolDO.getGuanZhu()==null){
                Message msg =new Message();
                msg.what=4;
                msg.obj="0";
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=4;
                msg.obj=String.valueOf(userPoolDO.getGuanZhu().size());
                pHandler.sendMessage(msg);
            }
            if(userPoolDO.getBeiGuanZhu()==null){
                Message msg =new Message();
                msg.what = 5;
                msg.obj = "0";
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=5;
                msg.obj=String.valueOf(userPoolDO.getBeiGuanZhu().size());
                pHandler.sendMessage(msg);
            }
            if(userPoolDO.getChanceIdList()==null){
                Message msg =new Message();
                msg.what=6;
                msg.obj=0;
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=6;
                msg.obj=userPoolDO.getChanceIdList().size();
                pHandler.sendMessage(msg);
            }

            if(myUserPool.getGuanZhu()!=null){
                if(myUserPool.getGuanZhu().contains(userName)){
                    Message msg =new Message();
                    msg.what=7;
                    pHandler.sendMessage(msg);

                }
                else {
                    Message msg =new Message();
                    msg.what=8;
                    pHandler.sendMessage(msg);
                }

            }
            else {
                Message msg =new Message();
                msg.what=8;
                pHandler.sendMessage(msg);
            }


        }

        };

    public void setHistFragment(List<chanceClass> cc, FragmentTransaction ft){

        fragment.setClass(cc,userName);
        fragmentTransaction.replace(R.id.fragmentHis,fragment);
        ft.commitAllowingStateLoss();
    }


}
