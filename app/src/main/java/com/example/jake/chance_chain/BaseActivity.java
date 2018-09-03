package com.example.jake.chance_chain;

import android.Manifest;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.ArraySet;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.JsonObject;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;



import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import javax.net.ssl.HttpsURLConnection;


public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    DynamoDBMapper dynamoDBMapper;
    protected BottomNavigationView navigationView;
    String us;
    private int STORAGE_PERMISSION_CODE = 10;
    private static final int GALLERY_REQUEST= 5;
    private Context context;
    TransferObserver observer;
    private TransferUtility sTransferUtility;
    AppHelper helper= new AppHelper();;
    private String uId;
    int number=0;
    ImageView myimageView,tImage;
    TextView myTextView,jianText,shenText,guanText,beiGuanText,faText;
    String ChanceId="asd";
    String totId="totalID";
    String vStr;
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<String> mDatasText;
    private List<String> mDatasImage;
    private String username,textTilte,textValue,txtShoufei,txtShoufeiType,txtFuFei,txtFuFeiType;
    private List<String> picList;
    String TestChance;
    public String trynum = "ui";
    public List<String> touUri;
    private List<String> uid;
    private List<Uri> uriList;
    private HomeFragment fragment = new HomeFragment();
    private FragmentTransaction fragmentTransaction;
    private int clickFlag =0;
    private int fufeiInt,shoufeiInt;
    private int unreadnum = 0;
    private String unread = "0";
    private double renshu;
    private int viewpage;
    TextView alert1,alert2;
    //private HashMap<String, Double> mapping = new HashMap<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        context=getApplication().getApplicationContext();
        sTransferUtility = helper.getTransferUtility(context);
        uId = helper.getCurrentUserName(context);
        Log.d("like wtf",uId);
        dynamoDBMapper=AppHelper.getMapper(context);
        mDatasImage = new ArrayList<String>(Arrays.asList());
        mDatasText = new ArrayList<String>(Arrays.asList());
        touUri = new ArrayList<String>(Arrays.asList());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        uriList = new ArrayList<Uri>();
        username=helper.getCurrentUserName(context);
        new Thread(LoginRunnable).start();
        //new Thread(httpRun).start();









        Log.d("uid","f"+uId);



        if(getContentViewId()==R.layout.activity_my) {
            new Thread(getChatting1).start();

            tImage = (ImageView) findViewById(R.id.wodetouxiang);
            RelativeLayout infButton = (RelativeLayout) findViewById(R.id.shezhi);

            infButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intentInf = new Intent(BaseActivity.this, settingActivity.class);
                    startActivity(intentInf);
                }
            });

            TextView userTxt = (TextView) findViewById(R.id.wodeUser);
            userTxt.setText(AppHelper.getCurrentUserName(context));
            us = AppHelper.getCurrentUserName(context);
            jianText = (TextView) findViewById(R.id.wodeJian);
            shenText = (TextView) findViewById(R.id.woshengwang);
            guanText = (TextView) findViewById(R.id.guanzhuNum);
            beiGuanText = (TextView) findViewById(R.id.beiGuanNum);
            faText = (TextView) findViewById(R.id.woFabuNum);
            alert1 = (TextView) findViewById(R.id.alert1);
            ImageView wodeFabu = (ImageView) findViewById(R.id.woFabuImg);
            ImageView wodeQianbao = (ImageView) findViewById(R.id.woQian);
            ImageView wodeXiaoxi = (ImageView) findViewById(R.id.woXiao);
            ImageView wodejihui1 = (ImageView) findViewById(R.id.woJihui);
            ImageView wodeGuan = (ImageView) findViewById(R.id.woGuan);
//            wodeGuan.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(BaseActivity.this,wodeGuanZHui.class);
//                    startActivity(intent);
//                }
//            });
            wodejihui1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this, wodejihui.class);
                    startActivity(intent);
                }
            });
            wodeQianbao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,myWallet.class);
                    startActivity(intent);
                }
            });
            wodeXiaoxi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,MessageActivity.class);
                    intent.putExtra("unread",unreadnum);
//                    intent.putExtra("noteMap",mapping);
                    startActivity(intent);
                }
            });
            wodeFabu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,fabuActivity.class);
                    startActivity(intent);
                }
            });
            Log.d("username","www"+us);
            new Thread(setUpMy).start();





        }


        else if(getContentViewId()==R.layout.activity_notification){
            ImageView picView = (ImageView) findViewById(R.id.getPic);
            EditText titleText = (EditText) findViewById(R.id.titletext);
            EditText Neirong = (EditText) findViewById(R.id.neirong);
            EditText shoufei = (EditText) findViewById(R.id.shoufei);
            EditText fufei = (EditText) findViewById(R.id.fufei);
            EditText ren = (EditText) findViewById(R.id.huoderenshu);

            TextView cic1 = (TextView) findViewById(R.id.circleText1);
            TextView cic2 = (TextView) findViewById(R.id.circleText2);
            TextView cic3 = (TextView) findViewById(R.id.circleText3);
            TextView cic4 = (TextView) findViewById(R.id.circleText4);

            Button fabuBtn = (Button) findViewById(R.id.fabubtn);


            cic1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic1.setTextColor(getColor(R.color.black));
                    cic2.setTextColor(getColor(R.color.white));
                    cic3.setTextColor(getColor(R.color.white));
                    cic4.setTextColor(getColor(R.color.white));
                    clickFlag=1;

                }
            });

            cic2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.black));
                    cic3.setTextColor(getColor(R.color.white));
                    cic4.setTextColor(getColor(R.color.white));
                    clickFlag=2;

                }
            });

            cic3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.white));
                    cic3.setTextColor(getColor(R.color.black));
                    cic4.setTextColor(getColor(R.color.white));
                    clickFlag=3;

                }
            });

            cic4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cic1.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic2.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic3.setBackground(ContextCompat.getDrawable(context,R.drawable.transparent_circle));
                    cic4.setBackground(ContextCompat.getDrawable(context,R.drawable.yeallow_cic));
                    cic1.setTextColor(getColor(R.color.white));
                    cic2.setTextColor(getColor(R.color.white));
                    cic3.setTextColor(getColor(R.color.white));
                    cic4.setTextColor(getColor(R.color.black));
                    clickFlag=4;

                }
            });

            Date currentTime = Calendar.getInstance().getTime();
            long yo = currentTime.getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(yo)).toString();

            Log.d("time ", "tr " + currentTime.toString()+ " sd " + dateString+ " " + (double) currentTime.getTime());
            Spinner bi1 = (Spinner) findViewById(R.id.bizhong);
            Spinner bi2 = (Spinner) findViewById(R.id.bizhong2);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.currency_name, R.layout.item_select);

            adapter.setDropDownViewResource(R.layout.drop_down_item);

            bi1.setAdapter(adapter);
            bi2.setAdapter(adapter);
            bi1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    parent.getItemAtPosition(position);
                    fufeiInt = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    fufeiInt = 0;
                }
            });

            bi2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    parent.getItemAtPosition(position);
                    shoufeiInt=position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    shoufeiInt=0;

                }
            });


            picView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d("typetry"," fu "+fufeiInt+" shou " + shoufeiInt);

                    requstStoragePermission();
                    FishBun.with(BaseActivity.this).setImageAdapter(new GlideAdapter()).startAlbum();
                }
            });

            fabuBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(fufei.getText().length()!=0) {
                        txtFuFei = fufei.getText().toString();
                    }
                    else{
                        txtFuFei="0";
                    }
                    switch (fufeiInt){
                        case 0: txtFuFeiType="cc";break;
                        case 1: txtFuFeiType="eth";break;
                        case 2: txtFuFeiType="btc";break;
                    }
                    if(shoufei.getText().length()!=0) {
                        txtShoufei = shoufei.getText().toString();
                    }
                    else{
                        txtShoufei="0";
                    }

                    switch (shoufeiInt){
                        case 0:txtShoufeiType = "cc";break;
                        case 1:txtShoufeiType="eth";break;
                        case 2:txtShoufeiType="btc";break;
                    }
                    if(!txtShoufei.equals("0")&&!txtFuFei.equals("0")){
                        Toast.makeText(context,"不能同时填写收费金额和付费金额",Toast.LENGTH_LONG).show();

                    }

                    else if(titleText.length()==0){
                        Log.d("wtftt"," shou "+ shoufei+" fu " + fufei);
                        Toast.makeText(context,"请输入标题",Toast.LENGTH_LONG).show();
                    }
                    else if(Neirong.length()==0){
                        Toast.makeText(context,"请输入内容",Toast.LENGTH_LONG).show();
                    }
                    else if(clickFlag==0){
                        Toast.makeText(context,"请选择标签",Toast.LENGTH_LONG).show();
                    }
                    else if(ren.getText().length()==0){
                        Toast.makeText(context,"请输入该机会的人数限制",Toast.LENGTH_LONG).show();
                    }
                    else {
                        textTilte = titleText.getText().toString();
                        textValue = Neirong.getText().toString();
                        renshu=Double.parseDouble(ren.getText().toString());
                        titleText.setText("");
                        Neirong.setText("");
                        shoufei.setText("");
                        fufei.setText("");
                        ren.setText("");
                        new Thread(uploadRunnable).start();
                    }


                }
            });





        }
        else if(getContentViewId() == R.layout.activity_home) {
            new Thread(getChatting2).start();

            Log.d("loading screen ","check if loading screen");
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.actionbar);
            ImageView xiaoxi = (ImageView) actionBar.getCustomView().findViewById(R.id.xiaoxi);
            alert2 = (TextView) actionBar.getCustomView().findViewById(R.id.alert2);

            xiaoxi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,MessageActivity.class);
                    intent.putExtra("unread",unreadnum);
//                    intent.putExtra("noteMap",mapping);
                    startActivity(intent);
                }
            });
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            myThread mThread = new myThread(this,dynamoDBMapper,fragmentTransaction,fragment);
            mThread.start();


        }



    }

    public int getNum(int num){
        number=num;
        return num;
    }



    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }





    Handler pHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:Picasso.get().load(msg.obj.toString()).resize(60,60).centerCrop().into(tImage);break;
                case 2:jianText.setText(msg.obj.toString());break;
                case 3:shenText.setText(msg.obj.toString());break;
                case 4:guanText.setText(msg.obj.toString());break;
                case 5:beiGuanText.setText(msg.obj.toString());break;
                case 6:faText.setText(msg.obj.toString());break;
            }
        }

    };


    Runnable setUpMy = new Runnable() {
        @Override
        public void run() {
            Log.d("wtf ","www"+us);
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,us);
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
                msg.obj="声望：0";
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=3;
                String str = "声望： ";
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
                msg.obj=userPoolDO.getGuanZhu().size();
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
                msg.obj=userPoolDO.getBeiGuanZhu().size();
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


        }
    };

    Handler uploadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:Toast.makeText(context,"已上传发布",Toast.LENGTH_LONG).show();break;
                case 2:Toast.makeText(context,"可用金额不足",Toast.LENGTH_LONG).show();break;
                case 3:Toast.makeText(context,"首次发布奖励Candy100个",Toast.LENGTH_LONG).show();;break;
                case 4:Toast.makeText(context,"今日首次发布奖励Candy10个",Toast.LENGTH_LONG).show();;break;
            }

        }
    };

    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            int cSize = helper.returnChanceeSize(dynamoDBMapper) + 1;
            final ChanceWithValueDO chanceWithValueDO = new ChanceWithValueDO();
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class, username);
            double fee = Double.parseDouble(txtFuFei);
            Log.d("thisshiit",userPoolDO.getAvailableWallet().toString()+username);
            if (userPoolDO.getAvailableWallet() >= fee) {
                Log.d("th11isshiit",userPoolDO.getAvailableWallet().toString()+username);
                userPoolDO.setFrozenwallet(userPoolDO.getFrozenwallet()+fee);
                userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet()-fee);
                List<String> pictureSet = new ArrayList<>();
                for (int i = 0; i < uriList.size(); i++) {
                    try {
                        String path = AppHelper.getPath(uriList.get(i), context);
                        File file = new File(path);
                        Log.d("uyu", "" + ChanceId);
                        observer =
                                sTransferUtility.upload(helper.BUCKET_NAME, String.valueOf(cSize) + "_" + String.valueOf(i) + ".png", file);
                        observer.setTransferListener(new TransferListener() {
                            @Override
                            public void onError(int id, Exception e) {
                                Log.e("onError", "Error during upload: " + id, e);
                            }

                            @Override
                            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                Log.d("onProgress", String.format("onProgressChanged: %d, total: %d, current: %d",
                                        id, bytesTotal, bytesCurrent));
                            }

                            @Override
                            public void onStateChanged(int id, TransferState newState) {
                                Log.d("onState", "onStateChanged: " + id + ", " + newState);
                            }
                        });
                        pictureSet.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/" + String.valueOf(cSize) + "_" + String.valueOf(i) + ".png");
                        //beginUpload(path);
                        Log.d("gooodshit", "upload " + String.valueOf(cSize) + "_" + String.valueOf(i) + ".png");
                    } catch (URISyntaxException e) {
                        Log.d("fck2", "Unable to upload file from the given uri", e);
                    }
                }
                Date currentTime = Calendar.getInstance().getTime();
                String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
                Log.d("letsee ", " " + txtFuFei);
                if (pictureSet.size() != 0) {
                    chanceWithValueDO.setPictures(pictureSet);
                }
                List<String> idList;
                if (userPoolDO.getChanceIdList() == null) {
                    idList = new ArrayList<>();
                    Message msg1 = new Message();
                    msg1.what=3;
                    uploadHandler.sendMessage(msg1);
                    userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet()+100);
                    userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency()+100);

                } else {
                    idList = userPoolDO.getChanceIdList();
                    if(sameDay(userPoolDO.getLastFabu())>0){
                        Message msg1 = new Message();
                        msg1.what=4;
                        uploadHandler.sendMessage(msg1);
                        userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet()+10);
                        userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency()+10);
                    }
                }

                idList.add(String.valueOf(cSize));
                //Log.d("iido",userPoolDO.getChanceIdList().toString()+idList.toString());
                userPoolDO.setChanceIdList(idList);
                userPoolDO.setLastFabu(dateString);
                chanceWithValueDO.setUsername(username);
                chanceWithValueDO.setId(String.valueOf(cSize));
                chanceWithValueDO.setFuFei(fee);
                chanceWithValueDO.setFuFeiType(txtFuFeiType);
                chanceWithValueDO.setShouFei(Double.parseDouble(txtShoufei));
                chanceWithValueDO.setShouFeiType(txtShoufeiType);
                chanceWithValueDO.setTag((double) clickFlag);
                chanceWithValueDO.setTitle(textTilte);
                chanceWithValueDO.setText(textValue);
                chanceWithValueDO.setRenShu(renshu);
                chanceWithValueDO.setTime(Double.parseDouble(dateString));
                dynamoDBMapper.save(chanceWithValueDO);
                dynamoDBMapper.save(userPoolDO);
                msg.what=1;
                uploadHandler.sendMessage(msg);

            } else {
                Log.d("tryfuck me fuck", "Unable to upload file from the given uri");
                msg.what=2;
                uploadHandler.sendMessage(msg);
            }
        }
    };





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("actrrr", "n" + ChanceId);

        Log.d("uri","size "+uriList.size());
        Log.d("get code","reque" + requestCode + " resu " + resultCode);

        if(requestCode == Define.ALBUM_REQUEST_CODE){

            uriList=data.getParcelableArrayListExtra(Define.INTENT_PATH);


        }
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, MyActivity.class));
            } else if (itemId == R.id.navigation_notifications) {
                startActivity(new Intent(this, NotificationActivity.class));
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    private File loadimg(){
        String key = "download.png";
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + key);
        observer =
                sTransferUtility.download(helper.BUCKET_NAME,
                        key,
                        file);
        Log.d("observer","ob "+observer.toString());

        // Attach a listener to the observer to get state update and progress notifications
        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.d("motherfucker","yoyoyshit");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("MainActivity", "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.d("fucker2","yoyoyshit");
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == observer.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + observer.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + observer.getBytesTotal());
        return file;
    }


    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);

    }

    private void requstStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    Runnable getChatting1 = new Runnable() {
        @Override
        public void run() {
            try {
                UserChatDO userChatDO = dynamoDBMapper.load(UserChatDO.class, uId);
                unreadnum = userChatDO.getTotalUnread().intValue();
                if(unreadnum!=0){
                    Message msg = new Message();
                    msg.obj = unreadnum;
                    msg.what=1;
                    chattingHandler.sendMessage(msg);
                }
            }catch (Exception e){
                Log.d("no message",username+e.toString());
            }


        }
    };

    Runnable getChatting2 = new Runnable() {
        @Override
        public void run() {
            try {
                UserChatDO userChatDO = dynamoDBMapper.load(UserChatDO.class, uId);
                unreadnum = userChatDO.getTotalUnread().intValue();
                if(unreadnum!=0){
                    Message msg = new Message();
                    msg.obj = unreadnum;
                    msg.what=2;
                    chattingHandler.sendMessage(msg);
                }
            }catch (Exception e){
                Log.d("no message",username+e.toString());
            }


        }
    };
    Runnable LoginRunnable = new Runnable() {
        @Override
        public void run() {
            Date currentLoginTime = Calendar.getInstance().getTime();
            String loginTime = DateFormat.format("yyyyMMddHHmmss", new Date(currentLoginTime.getTime())).toString();
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,uId);
            Message msg = new Message();
            if(userPoolDO.getLastLogin()==null){
                userPoolDO.setLastLogin(loginTime);
                userPoolDO.setCandyCurrency(100.0);
                userPoolDO.setAvailableWallet(100.0);
                msg.what=0;
                loginHandler.sendMessage(msg);
            }
            else if(sameDay(userPoolDO.getLastLogin())!=0) {
                userPoolDO.setLastLogin(loginTime);
                int cLogin = userPoolDO.getConsecutiveLogin().intValue();
                if (sameDay(userPoolDO.getLastLogin()) == 1) {
                    switch (cLogin) {
                        default:
                            break;
                        case 1:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 5);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 5);
                            msg.what = 1;
                            loginHandler.sendMessage(msg);
                            break;
                        case 2:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 10);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 10);
                            msg.what = 2;
                            loginHandler.sendMessage(msg);
                            break;
                        case 3:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 15);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 15);
                            msg.what = 3;
                            loginHandler.sendMessage(msg);
                            break;
                        case 4:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 30);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 30);
                            msg.what = 4;
                            loginHandler.sendMessage(msg);
                            break;
                        case 5:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 40);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 40);
                            msg.what = 5;
                            loginHandler.sendMessage(msg);
                            break;
                        case 6:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 50);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 50);
                            msg.what = 1;
                            loginHandler.sendMessage(msg);
                            break;
                        case 7:
                            userPoolDO.setShengWang(userPoolDO.getShengWang() + 1);
                            msg.what = 7;
                            loginHandler.sendMessage(msg);
                            break;
                    }
                    userPoolDO.setConsecutiveLogin((double) cLogin + 1);
                }
            }

            dynamoDBMapper.save(userPoolDO);

        }
    };

    Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int selector = msg.what;
            switch (selector){
                case 0:Toast.makeText(context,"首次登录，奖励机会Candy100个",Toast.LENGTH_LONG).show();break;
                case 1:Toast.makeText(context,"连续一天登录，奖励机会Candy5个",Toast.LENGTH_LONG).show();break;
                case 2:Toast.makeText(context,"连续二天登录，奖励机会Candy10个",Toast.LENGTH_LONG).show();break;
                case 3:Toast.makeText(context,"连续三天登录，奖励机会Candy15个",Toast.LENGTH_LONG).show();break;
                case 4:Toast.makeText(context,"连续四天登录，奖励机会Candy30个",Toast.LENGTH_LONG).show();break;
                case 5:Toast.makeText(context,"连续五天登录，奖励机会Candy40个",Toast.LENGTH_LONG).show();break;
                case 6:Toast.makeText(context,"连续六天登录，奖励机会Candy50个",Toast.LENGTH_LONG).show();break;
                case 7:Toast.makeText(context,"连续七天登录，奖励声望积分一个",Toast.LENGTH_LONG).show();break;
            }

        }
    };

    Handler chattingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            unread = msg.obj.toString();
            if (msg.what == 1) {
                alert1.setVisibility(View.VISIBLE);
                alert1.setText(unread);

            }
            else if (msg.what == 2) {
                alert2.setVisibility(View.VISIBLE);
                alert2.setText(unread);
            }
        }

    };

    public void setTry(List<String> mDatasImage,List<String> mDatasText, List<String> tImg, String n){
        this.mDatasText=mDatasText;
        this.mDatasImage=mDatasImage;
        this.touUri = tImg;
        this.trynum=n;
    }

    public void setFragment( List<chanceClass> cc,FragmentTransaction ft){

        fragment.setClass(cc);
        fragmentTransaction.replace(R.id.fragmentHome,fragment);
        ft.commitAllowingStateLoss();
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    Runnable httpRun = new Runnable() {
        @Override
        public void run() {
            try {
//                String url = "http://192.168.31.244:8000";
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                // Display the first 500 characters of the response string.
//                                Log.d("thisfckingtag","Response is: "+ response.substring(0,500));
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("ds","That didn't work!");
//                    }
//                });
                URL url = new URL("http://192.168.0.20:8000/getaccount");
                HttpURLConnection myConnection =
                        (HttpURLConnection) url.openConnection();
                myConnection.setRequestProperty("Content-type","application/json");
                myConnection.setRequestMethod("POST");
                myConnection.setDoInput(true);
                myConnection.setDoOutput(true);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cd","0x5f4B72ca6740532210f9a7BEA162825099138372");
                Log.d("json",jsonObject.toString());
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(myConnection.getOutputStream()));
                writer.write(jsonObject.toString());
                writer.flush();
                myConnection.connect();

                if(myConnection.getResponseCode()==200){
                    Log.d("isucces","yes"+String.valueOf(myConnection.getResponseCode()));
                }
                else{
                    Log.d("isucces","no"+String.valueOf(myConnection.getResponseCode()));
                }
                InputStream responseBody = myConnection.getInputStream();

                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody);
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject();
                while (jsonReader.hasNext()){

                }

//                String line;
//                String response="";
//                BufferedReader br=new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
//                while ((line=br.readLine()) != null) {
//                    response+=line;
//                }

//                Log.d("trythisshiit",response);


            }catch (Exception e){
                Log.d("isucces",e.toString());

            }

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
