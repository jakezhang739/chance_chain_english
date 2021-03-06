package com.example.jake.chance_chain;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.sangcomz.fishbun.define.Define;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class chattingActivity extends AppCompatActivity {
    LinearLayout beijing;
    EditText getInput;
    String myUsr;
    String TextValue;
    Context context;
    AppHelper helper = new AppHelper();
    DynamoDBMapper mapper;
    String userId;
    AmazonDynamoDBClient dbClient;
    TextView addText;
    RelativeLayout liaotiankuang;
    TextView tianconglan;
    ProgressBar progressBar;
    Boolean exit = true;
    ScrollView s;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ActionBar actionBar = getSupportActionBar();
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        userId = getIntent().getStringExtra("title").toString();
        mapper = helper.getMapper(context);
        titlteText.setText(userId);
        beijing = (LinearLayout) findViewById(R.id.liaobeijing);
        getInput = (EditText) findViewById(R.id.inputMsg);
        dbClient = helper.getClient(context);
        progressBar = (ProgressBar) findViewById(R.id.progressBarchat);
        liaotiankuang = (RelativeLayout) findViewById(R.id.liaotian);
        tianconglan = (TextView) findViewById(R.id.tianc);
        ImageView sPic = (ImageView) findViewById(R.id.imageView23);
        sPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requstStoragePermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        Thread receiveThread = new Thread(ReceiverListener);
        receiveThread.start();
        s = (ScrollView) findViewById(R.id.liaozhuti);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit=false;
                finish();

            }
        });

        addText = (TextView) findViewById(R.id.fasongxiao);
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue = getInput.getText().toString();
                if (TextValue.length() == 0) {

                }
                else if (TextValue.trim().length() == 0){

                }
                else {
                    addText.setVisibility(View.INVISIBLE);
                    getInput.setText("");
                    new Thread(sendMsg).start();
                }
            }
        });

    }

    Handler addHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:progressBar.setVisibility(View.INVISIBLE); liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);onAddTime(msg.obj.toString());break;
                case 2:progressBar.setVisibility(View.INVISIBLE); liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);onAddMyField(msg.obj.toString());s.fullScroll(ScrollView.FOCUS_DOWN);break;
                case 3:progressBar.setVisibility(View.INVISIBLE); liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);onAddHisField(msg.obj.toString());s.fullScroll(ScrollView.FOCUS_DOWN);break;
                case 4:progressBar.setVisibility(View.INVISIBLE); liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);break;
                case 5:progressBar.setVisibility(View.INVISIBLE); liaotiankuang.setVisibility(View.VISIBLE);tianconglan.setVisibility(View.VISIBLE);addText.setVisibility(View.VISIBLE);break;

            }
        }
    };


    public void onAddMyField(String getMsg) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.wodeliaotian, beijing, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.woshuo);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.wotou);
        ImageView picture = (ImageView) layout1.findViewById(R.id.imageView24);
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + myUsr + ".png").placeholder(R.drawable.splash).into(wotou);
        if (getMsg.length()>20) {
            Log.d("niubi",getMsg.substring(20, getMsg.length()));
            if (getMsg.substring(0, 20).equals("jake_is_super_niu_bi")) {
                myMsg.setVisibility(View.INVISIBLE);
                picture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+getMsg.substring(20, getMsg.length())).resize(500,500).into(picture);
            } else {
                myMsg.setText(getMsg);
            }
        }
        else {
            myMsg.setText(getMsg);
        }
        beijing.addView(layout1);
        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    public void onAddTime(String time) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.timetag, beijing, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.timetag);
            myMsg.setText(helper.displayTime(time));
            beijing.addView(layout1);

        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    public void onAddHisField(String getMsg) {
        View layout1 = LayoutInflater.from(this).inflate(R.layout.tadeliaotian, beijing, false);
        TextView myMsg = (TextView) layout1.findViewById(R.id.tashuo);
        ImageView wotou = (ImageView) layout1.findViewById(R.id.tadetou);
        ImageView picture = (ImageView) layout1.findViewById(R.id.imageView24);
        Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + userId + ".png").placeholder(R.drawable.splash).into(wotou);
        if (getMsg.length()>20) {
            Log.d("niubi",getMsg.substring(0,20));
            if (getMsg.substring(0, 20).equals("jake_is_super_niu_bi")) {
                myMsg.setVisibility(View.INVISIBLE);
                picture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+getMsg.substring(20, getMsg.length())).resize(500,500).into(picture);
                Log.d("jakeis",getMsg.substring(20, getMsg.length()));
            } else {
                myMsg.setText(getMsg);
            }
        }
        else {
            myMsg.setText(getMsg);
        }
        beijing.addView(layout1);
        Log.d("beijing", String.valueOf(beijing.getChildCount()) + Picasso.get().load("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + "sd" + ".png"));
    }

    Runnable sendMsg = new Runnable() {
        @Override
        public void run() {
            int flag=0;
            ChattingListDO chatList;
            UserChatDO usrChat1,usrChat2;
            Log.d("me usr",myUsr+userId);
            try{
                usrChat1 = mapper.load(UserChatDO.class,myUsr);
                if(usrChat1.getChattingList().contains(userId)){
                    usrChat1.removeUsr(userId);
                    }
                usrChat1.addChatting(userId);
                usrChat1.addSentence(userId,TextValue);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                usrChat1.addTime(userId,dateString);
                mapper.save(usrChat1);
            }catch (Exception e){
                Log.d("chat1error",e.toString());
                final UserChatDO userChatDO1 = new UserChatDO();
                userChatDO1.addChatting(userId);
                userChatDO1.setUserId(myUsr);
                userChatDO1.addSentence(userId,TextValue);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                userChatDO1.addTime(userId,dateString);
                mapper.save(userChatDO1);
            }
            try{
                usrChat2 = mapper.load(UserChatDO.class,userId);
                if(usrChat2.getChattingList().contains(myUsr)){
                    usrChat2.removeUsr(myUsr);
                }
                usrChat2.addChatting(myUsr);
                usrChat2.addSentence(myUsr,TextValue);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                usrChat2.addUnread(myUsr);
                usrChat2.addTotal();
                usrChat2.addTime(myUsr,dateString);
                mapper.save(usrChat2);
            }catch (Exception e){
                Log.d("chat2error",e.toString());
                final UserChatDO userChatDO = new UserChatDO();
                userChatDO.addChatting(myUsr);
                userChatDO.setUserId(userId);
                userChatDO.addSentence(myUsr,TextValue);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                userChatDO.addTime(myUsr,dateString);
                userChatDO.addTotal();
                userChatDO.addUnread(myUsr);
                mapper.save(userChatDO);
            }
            try{
                chatList = mapper.load(ChattingListDO.class,myUsr,userId);
                chatList.addText(TextValue);
                chatList.addSr("user1");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                chatList.addTime(dateString);

                mapper.save(chatList);
            }catch (Exception e){
                flag=1;
                try{
                    chatList = mapper.load(ChattingListDO.class,userId,myUsr);
                    chatList.addText(TextValue);
                    chatList.addSr("user2");
                    Date currentTime = Calendar.getInstance().getTime();
                    String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                    chatList.addTime(dateString);
                    mapper.save(chatList);
                }catch (Exception e1){
                    flag=2;
                }
            }
            if(flag==2){
                final ChattingListDO chattingListDO = new ChattingListDO();
                chattingListDO.setUser1(myUsr);
                chattingListDO.setUser2(userId);
                chattingListDO.addText(TextValue);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                chattingListDO.addTime(dateString);
                chattingListDO.addSr("user1");
                mapper.save(chattingListDO);
            }
            Message mesg = new Message();
            mesg.what=5;
            addHandler.sendMessage(mesg);

        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("get code","reque" + requestCode + " resu " + resultCode);

        if(requestCode == 1){
            if(resultCode == 0){

            }
            else {
                uri=data.getData();
                Log.d("givemeshit",uri.toString());
                new Thread(sendPic).start();
            }
        }
    }


    public void uploadPic(Uri uri, String key){
        try {
            String path = helper.getPath(uri, context);
            File file = new File(path);
            TransferUtility sTransferUtility = helper.getTransferUtility(context);
            TransferObserver observer;
            observer =
                    sTransferUtility.upload(helper.BUCKET_NAME, key, file);
        }catch (Exception e){

        }
    }
    Runnable sendPic = new Runnable() {
        @Override
        public void run() {
            int flag=0;
            ChattingListDO chatList;
            UserChatDO usrChat1,usrChat2;
            Log.d("me usr",myUsr+userId);
            String key;
            try{
                chatList = mapper.load(ChattingListDO.class,myUsr,userId);
                key=myUsr+userId+String.valueOf(chatList.getChattingText().size())+".png";
                chatList.addText("jake_is_super_niu_bi"+key);
                uploadPic(uri,key);
                chatList.addSr("user1");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                chatList.addTime(dateString);

                mapper.save(chatList);
            }catch (Exception e){
                flag=1;
                try{
                    chatList = mapper.load(ChattingListDO.class,userId,myUsr);
                    key=myUsr+userId+String.valueOf(chatList.getChattingText().size())+".png";
                    chatList.addText("jake_is_super_niu_bi"+key);
                    uploadPic(uri,key);
                    chatList.addSr("user2");
                    Date currentTime = Calendar.getInstance().getTime();
                    String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                    chatList.addTime(dateString);
                    mapper.save(chatList);
                }catch (Exception e1){
                    flag=2;
                }
            }
            if(flag==2){
                final ChattingListDO chattingListDO = new ChattingListDO();
                chattingListDO.setUser1(myUsr);
                chattingListDO.setUser2(userId);
                key=myUsr+userId+String.valueOf(chattingListDO.getChattingText().size())+".png";
                chattingListDO.addText("jake_is_super_niub"+key);
                uploadPic(uri,key);
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                chattingListDO.addTime(dateString);
                chattingListDO.addSr("user1");
                mapper.save(chattingListDO);
            }
            Message mesg = new Message();
            mesg.what=5;
            addHandler.sendMessage(mesg);
            try{
                usrChat1 = mapper.load(UserChatDO.class,myUsr);
                if(usrChat1.getChattingList().contains(userId)){
                    usrChat1.removeUsr(userId);
                }
                usrChat1.addChatting(userId);
                usrChat1.addSentence(userId,"[picture]");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                usrChat1.addTime(userId,dateString);
                mapper.save(usrChat1);
            }catch (Exception e){
                Log.d("chat1error",e.toString());
                final UserChatDO userChatDO1 = new UserChatDO();
                userChatDO1.addChatting(userId);
                userChatDO1.setUserId(myUsr);
                userChatDO1.addSentence(userId,"[picture]");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                userChatDO1.addTime(userId,dateString);
                mapper.save(userChatDO1);
            }
            try{
                usrChat2 = mapper.load(UserChatDO.class,userId);
                if(usrChat2.getChattingList().contains(myUsr)){
                    usrChat2.removeUsr(myUsr);
                }
                usrChat2.addChatting(myUsr);
                usrChat2.addSentence(myUsr,"[picture]");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                usrChat2.addUnread(myUsr);
                usrChat2.addTotal();
                usrChat2.addTime(myUsr,dateString);
                mapper.save(usrChat2);
            }catch (Exception e){
                Log.d("chat2error",e.toString());
                final UserChatDO userChatDO = new UserChatDO();
                userChatDO.addChatting(myUsr);
                userChatDO.setUserId(userId);
                userChatDO.addSentence(myUsr,"[picture]");
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                userChatDO.addTime(myUsr,dateString);
                userChatDO.addTotal();
                userChatDO.addUnread(myUsr);
                mapper.save(userChatDO);
            }

        }
    };

    Runnable ReceiverListener = new Runnable() {
        @Override
        public void run() {
                    int flag=0;
                    AppHelper helper = new AppHelper();

                    int size=0;
                    ChattingListDO chattingListDO;
                    try{
                        chattingListDO = mapper.load(ChattingListDO.class,myUsr,userId);
                        List<String> chatString = chattingListDO.getChattingText();
                        for (int i = 0; i < chatString.size(); i++) {
                                if (i == 0) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = chattingListDO.getChattingTime().get(i);
                                    addHandler.sendMessage(msg);
                                }
                                else if(Double.parseDouble(chattingListDO.getChattingTime().get(i)) - Double.parseDouble(chattingListDO.getChattingTime().get(i-1)) >1000){
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = chattingListDO.getChattingTime().get(i);
                                    Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                                    Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                                    addHandler.sendMessage(msg);
                                }
                                Log.d("srlist ",chattingListDO.getSrList().get(i));
                                Log.d("srlist1 ",String.valueOf(chatString.size()));
                                if (chattingListDO.getSrList().get(i).equals("user1")) {
                                    Message msg = new Message();
                                    msg.what = 2;
                                    msg.obj = chatString.get(i);
                                    addHandler.sendMessage(msg);
                                }
                                if (chattingListDO.getSrList().get(i).equals("user2")) {
                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = chatString.get(i);
                                    addHandler.sendMessage(msg);
                                }

                            }
                            flag=1;
                            size = chatString.size();

                    }catch (Exception e){
                        Log.d("exception1",e.toString());
                    }
            try {
                chattingListDO = mapper.load(ChattingListDO.class, userId, myUsr);
                List<String> chatString = chattingListDO.getChattingText();
                int i = 0;
                while (i < chatString.size()) {
                    if (i == 0) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = chattingListDO.getChattingTime().get(i);
                        addHandler.sendMessage(msg);
                    }
                    else if(Double.parseDouble(chattingListDO.getChattingTime().get(i)) - Double.parseDouble(chattingListDO.getChattingTime().get(i-1)) >1000){
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = chattingListDO.getChattingTime().get(i);
                        Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                        Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                        addHandler.sendMessage(msg);
                    }
                    Log.d("sr11list ", chattingListDO.getSrList().get(i));
                    Log.d("sr2list1 ", String.valueOf(chatString.size()));
                    if (chattingListDO.getSrList().get(i).equals("user2")) {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = chatString.get(i);
                        addHandler.sendMessage(msg);
                    }
                    if (chattingListDO.getSrList().get(i).equals("user1")) {
                        Log.d("y2oyowtf ", String.valueOf(i));
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = chatString.get(i);
                        Log.d("y4oyowtf ", String.valueOf(i));
                        addHandler.sendMessage(msg);
//                                    userChatDO.removeUnread(myUsr);
                        Log.d("y3oyowtf ", String.valueOf(i));
//                                }
                        Log.d("yoyowtf ", String.valueOf(i));
                        Log.d("whatisgoingon ", String.valueOf(i));
                    }
                    i++;
                }
                size = chatString.size();
                flag=2;
            }catch (Exception e1){
                Log.d("exception2",e1.toString());

            }
            if(flag==0){
                        Message mesg = new Message();
                        mesg.what=4;
                        addHandler.sendMessage(mesg);

            }
            else {
                        UserChatDO userChatDO = mapper.load(UserChatDO.class,myUsr);
                        if(userChatDO.getUnRead().get(userId)!=null){
                            int num = Integer.parseInt(userChatDO.getUnRead().get(userId));
                            userChatDO.getUnRead().put(userId,"0");
                            double tot =  userChatDO.getTotalUnread().intValue()-num;
                            userChatDO.setTotalUnread(tot);
                            mapper.save(userChatDO);
                        }
            }
            s.fullScroll(ScrollView.FOCUS_DOWN);
            while (exit==true){
                Log.d("showwhile",exit.toString());
                int tempflag=0;
             try{
                 Log.d("chattingsize",String.valueOf(size));
                 Thread.sleep(500);
             }catch (Exception e){

             }
             try{
                 chattingListDO = mapper.load(ChattingListDO.class,myUsr,userId);
                 List<String> chatString = chattingListDO.getChattingText();
                 Log.d("chattingsize11",String.valueOf(chatString.size())+String.valueOf(size));
                 for (int i = size; i < chatString.size(); i++) {
                     if(i==0){
                         Message msg = new Message();
                         msg.what = 1;
                         msg.obj = chattingListDO.getChattingTime().get(i);
                         addHandler.sendMessage(msg);
                         Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                         Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                     }
                     else if(Double.parseDouble(chattingListDO.getChattingTime().get(i)) - Double.parseDouble(chattingListDO.getChattingTime().get(i-1)) >1000){
                         Message msg = new Message();
                             msg.what = 1;
                             msg.obj = chattingListDO.getChattingTime().get(i);
                             addHandler.sendMessage(msg);
                             Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                             Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                         }
                         Log.d("srlist ",chattingListDO.getSrList().get(i));
                         Log.d("srlist1 ",String.valueOf(chatString.size()));
                         if (chattingListDO.getSrList().get(i).equals("user1")) {
                             Message msg = new Message();
                             msg.what = 2;
                             msg.obj = chatString.get(i);
                             addHandler.sendMessage(msg);
                         }
                         if (chattingListDO.getSrList().get(i).equals("user2")) {
                             Message msg = new Message();
                             msg.what = 3;
                             msg.obj = chatString.get(i);
                             addHandler.sendMessage(msg);
                         }

                     }
                     tempflag=1;
                     size = chatString.size();

                 }catch (Exception e){
                     Log.d("wexception1",e.toString());
                 }
                 try {
                     chattingListDO = mapper.load(ChattingListDO.class, userId, myUsr);
                     List<String> chatString = chattingListDO.getChattingText();
                     int i = size;
                     Log.d("chattingsize11",String.valueOf(chatString.size()));
                     while (i < chatString.size()) {
                         if(i==0){
                             Message msg = new Message();
                             msg.what = 1;
                             msg.obj = chattingListDO.getChattingTime().get(i);
                             addHandler.sendMessage(msg);
                             Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                             Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                         }
                        else if(Double.parseDouble(chattingListDO.getChattingTime().get(i)) - Double.parseDouble(chattingListDO.getChattingTime().get(i-1)) >1000){
                             Message msg = new Message();
                             msg.what = 1;
                             msg.obj = chattingListDO.getChattingTime().get(i);
                            Log.d("gettime ", helper.displayTime(chattingListDO.getChattingTime().get(i)));
                            Log.d("gettime1 ", chattingListDO.getChattingTime().get(i));
                             addHandler.sendMessage(msg);
                         }
                         Log.d("sr11list ", chattingListDO.getSrList().get(i));
                         Log.d("sr2list1 ", String.valueOf(chatString.size()));
                         if (chattingListDO.getSrList().get(i).equals("user2")) {
                             Message msg = new Message();
                             msg.what = 2;
                             msg.obj = chatString.get(i);
                             addHandler.sendMessage(msg);
                         }
                         if (chattingListDO.getSrList().get(i).equals("user1")) {
                             Log.d("y2oyowtf ", String.valueOf(i));
                             Message msg = new Message();
                             msg.what = 3;
                             msg.obj = chatString.get(i);
                             Log.d("y4oyowtf ", String.valueOf(i));
                             addHandler.sendMessage(msg);
//                                    userChatDO.removeUnread(myUsr);
                             Log.d("y3oyowtf ", String.valueOf(i));
//                                }
                             Log.d("yoyowtf ", String.valueOf(i));
                             Log.d("whatisgoingon ", String.valueOf(i));
                         }
                         i++;
                     }
                     size = chatString.size();
                     tempflag=2;
                 }catch (Exception e1){
                     Log.d("wexception2",e1.toString());

                 }
                 if(tempflag==0){
                     Message mesg = new Message();
                     mesg.what=4;
                     addHandler.sendMessage(mesg);

                 }
                 else {
                     UserChatDO userChatDO = mapper.load(UserChatDO.class,myUsr);
                     if(userChatDO.getUnRead().get(userId)!=null){
                         int num = Integer.parseInt(userChatDO.getUnRead().get(userId));
                         userChatDO.getUnRead().put(userId,"0");
                         double tot =  userChatDO.getTotalUnread().intValue()-num;
                         userChatDO.setTotalUnread(tot);
                         mapper.save(userChatDO);
                     }
                 }


            }


        }
    };


    private void requstStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(chattingActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }
    }




}
