package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class sharingActivity extends AppCompatActivity {
    Button shareBtn;
    TextView cancelText;
    chanceClass chC;
    TextView sUser,sNei;
    ImageView sUimg;
    EditText shuru;
    AppHelper helper = new AppHelper();
    DynamoDBMapper dynamoDBMapper;
    String userId,fengTxt;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        chC = (chanceClass) getIntent().getParcelableExtra("link");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.sharingtopbar);
        cancelText = (TextView) actionBar.getCustomView().findViewById(R.id.Cancel);
        shareBtn = (Button) actionBar.getCustomView().findViewById(R.id.sharingFa);
        context=getApplication().getApplicationContext();
        dynamoDBMapper=helper.getMapper(context);
        userId = helper.getCurrentUserName(context);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sharingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        sUser = (TextView) findViewById(R.id.UserNameAt);
        sNei = (TextView) findViewById(R.id.titleTxt);
        sUimg = (ImageView) findViewById(R.id.fenTou);
        shuru = (EditText) findViewById(R.id.fenXiangShare);
        RelativeLayout sharRel = (RelativeLayout) findViewById(R.id.shareLink);
        if(!chC.touUri.isEmpty()){
            Picasso.get().load(chC.touUri).into(sUimg);
        }
        sNei.setText(chC.txtTitle);
        sUser.setText("@"+chC.userid);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fengTxt=shuru.getText().toString();
                new Thread(sharingRunnable).start();
                Intent intent = new Intent(sharingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    Handler shareHandler = new Handler(){
      @Override
      public void handleMessage(Message msg){
          if(msg.what==1){
              Toast.makeText(context,"First share，you have gotten 100 Candy",Toast.LENGTH_LONG).show();

          }
          else if(msg.what==2){
              Toast.makeText(context,"First share today，you have gotten 10 Candy",Toast.LENGTH_LONG).show();

          }
      }
    };

    Runnable sharingRunnable = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO lastChance = dynamoDBMapper.load(ChanceWithValueDO.class,chC.cId);
            if(lastChance.getShared()!=null){
                lastChance.setShared(lastChance.getShared()+1);
            }
            else {
                lastChance.setShared(1.0);
            }
            dynamoDBMapper.save(lastChance);
            int cSize = helper.returnChanceeSize(dynamoDBMapper)+1;
            final ChanceWithValueDO chanceWithValueDO = new ChanceWithValueDO();
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userId);
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            List<String> temp = new ArrayList<>();
            Message msg = new Message();
            if(userPoolDO.getLastZhuan()==null){
                userPoolDO.setLastZhuan(dateString);
                msg.what=1;
                shareHandler.sendMessage(msg);
                userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 100);
                userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 100);
            }
            else{
                if(sameDay(userPoolDO.getLastZhuan())==1){
                    msg.what=2;
                    shareHandler.sendMessage(msg);
                    userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 10);
                    userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 10);
                }
                userPoolDO.setLastZhuan(dateString);

            }
            chanceWithValueDO.setUsername(userId);
            chanceWithValueDO.setId(String.valueOf(cSize));
            List<String> shareLink = new ArrayList<>();
            shareLink.add(chC.cId);
            shareLink.add("@"+chC.userid);
            shareLink.add(chC.txtTitle);
            if(!chC.touUri.isEmpty()){
            shareLink.add(chC.touUri);}
            chanceWithValueDO.setSharedFrom(shareLink);
            chanceWithValueDO.setShouFei(0.0);
            chanceWithValueDO.setShouFeiType("cc");
            chanceWithValueDO.setFuFei(0.0);
            chanceWithValueDO.setFuFeiType("cc");
            chanceWithValueDO.setTag(1.0);
            chanceWithValueDO.setText(fengTxt);
            chanceWithValueDO.setRenShu(1.0);
            chanceWithValueDO.setTime(Double.parseDouble(dateString));
            chanceWithValueDO.setTitle(fengTxt);
            if(userPoolDO.getChanceIdList()!=null){
                temp.add(String.valueOf(cSize));
                userPoolDO.setChanceIdList(temp);
            }
            else {
                temp=userPoolDO.getChanceIdList();
                userPoolDO.setChanceIdList(temp);
            }
            if(userPoolDO.getProfilePic()!=null){
                chanceWithValueDO.setProfilePicture(userPoolDO.getProfilePic());
            }
            dynamoDBMapper.save(chanceWithValueDO);
            dynamoDBMapper.save(userPoolDO);
        }
    };
    private int sameDay(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        int dayDif = Integer.parseInt(sameday1)-Integer.parseInt(sameday2);
        return  dayDif;

    }
}
