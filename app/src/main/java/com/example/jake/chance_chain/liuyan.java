package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class liuyan extends AppCompatActivity {

    LinearLayout stuff;
    String myUsr;
    String TextValue;
    Context context;
    AppHelper helper = new AppHelper();
    DynamoDBMapper mapper;
    HashMap<String,Double> mapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liuyan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        stuff = (LinearLayout) findViewById(R.id.stuff);
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        mapper = helper.getMapper(context);
        TextView titlebar = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        titlebar.setText("Message");
        ImageView backImg = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        new Thread(setup).start();
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(liuyan.this,MessageActivity.class);
                startActivity(intent);
            }
        });
    }

    Handler setupHandle = new Handler(){
      @Override
      public void handleMessage(Message msg){
          if(msg.what==1){
              Log.d("wait","whattt");
              ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
              progressBar.setVisibility(View.INVISIBLE);
              ArrayList<String> putString = (ArrayList<String>) msg.getData().getStringArrayList("key");
              onAddField(putString.get(0),putString.get(1),putString.get(2),putString.get(3));
          }
          else{
              ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
              progressBar.setVisibility(View.INVISIBLE);
          }
      }
    };

    public void onAddField( String username, String time, String txt, String num) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.liuitem, stuff, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.ltvalue);
        myMsg.setText(txt);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.ltou);
        TextView myname = (TextView) layout1.findViewById(R.id.name);
        myname.setText(username);
        TextView unread = (TextView) layout1.findViewById(R.id.alert);
        if(Integer.parseInt(num)!=0) {
            unread.setText(num);
            unread.setVisibility(View.VISIBLE);
        }
        else {
            unread.setVisibility(View.INVISIBLE);
        }
        TextView utime = (TextView) layout1.findViewById(R.id.ltime);
        utime.setText(helper.displayTime(time));
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + username + ".png").placeholder(R.drawable.splash).into(wotou);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(liuyan.this,chattingActivity.class);
                unread.setText("0");
                unread.setVisibility(View.INVISIBLE);
                intent.putExtra("title",username);
                startActivity(intent);
            }
        });
        stuff.addView(layout1);

    }

    Runnable setup = new Runnable() {
        @Override
        public void run() {
            try{
                Log.d("heyhey","whattt");
                UserChatDO userChatDO = mapper.load(UserChatDO.class,myUsr);
                List<String> chatList = userChatDO.getChattingList();
                Log.d("heyhey",chatList.toString());
                int size = chatList.size()-1;
                Log.d("he11yhey",String.valueOf(size));
                for(int i= size;i>=0;i--){
                    Log.d("hwt",String.valueOf(i));
                    Message msg = new Message();
                    ArrayList<String> setup = new ArrayList<>();
                    setup.add(chatList.get(i));
                    setup.add(userChatDO.getLastTime().get(chatList.get(i)));
                    setup.add(userChatDO.getLastSentence().get(chatList.get(i)));
                    if(userChatDO.getUnRead().get(chatList.get(i))!=null) {
                        setup.add(userChatDO.getUnRead().get(chatList.get(i)));
                    }
                    else {
                        setup.add("0");
                    }
                    msg.what=1;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("key",setup);
                    msg.setData(bundle);
                    Log.d("heyhey",chatList.get(i));
                    setupHandle.sendMessage(msg);
                }
                }catch (Exception e){

                Message msg = new Message();
                msg.what=0;
                setupHandle.sendMessage(msg);
                Log.d("errr1",e.toString());

            }

        }

    };


}