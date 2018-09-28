package com.example.jake.chance_chain;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.ArraySet;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    String priceType;
    //private HashMap<String, Double> mapping = new HashMap<>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        context=getApplication().getApplicationContext();
        sTransferUtility = helper.getTransferUtility(context);
        uId = helper.getCurrentUserName(context);
        Log.d("like wtf",uId);
        dynamoDBMapper=helper.getMapper(context);
        mDatasImage = new ArrayList<String>(Arrays.asList());
        mDatasText = new ArrayList<String>(Arrays.asList());
        touUri = new ArrayList<String>(Arrays.asList());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        uriList = new ArrayList<Uri>();
        username=helper.getCurrentUserName(context);
        new Thread(LoginRunnable).start();
        new Thread(httpRun).start();











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
            userTxt.setText(helper.getCurrentUserName(context));
            us = helper.getCurrentUserName(context);
            jianText = (TextView) findViewById(R.id.wodeJian);
            shenText = (TextView) findViewById(R.id.woshengwang);
            guanText = (TextView) findViewById(R.id.guanzhuNum);
            beiGuanText = (TextView) findViewById(R.id.beiGuanNum);
            faText = (TextView) findViewById(R.id.woFabuNum);
            alert1 = (TextView) findViewById(R.id.alert1);
            RelativeLayout gLay = findViewById(R.id.folllowLay);
            RelativeLayout bLay = findViewById(R.id.beiguanLay);
            RelativeLayout pLay = findViewById(R.id.postLay);
            ImageView wodeFabu = (ImageView) findViewById(R.id.woFabuImg);
            ImageView wodeQianbao = (ImageView) findViewById(R.id.woQian);
            ImageView wodeXiaoxi = (ImageView) findViewById(R.id.woXiao);
            ImageView wodejihui1 = (ImageView) findViewById(R.id.woJihui);
            ImageView wodeGuan = (ImageView) findViewById(R.id.woGuan);
            gLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,wodeGuanZHui.class);
                    intent.putExtra("f","follow");
                    startActivity(intent);
                }
            });
            bLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,wodeGuanZHui.class);
                    intent.putExtra("f","fuck");
                    startActivity(intent);
                }
            });
            pLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,fabuActivity.class);
                    startActivity(intent);
                }
            });
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
            wodeGuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,wodeGuanZHui.class);
                    intent.putExtra("f","follow");
                    startActivity(intent);
                }
            });
            tImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,singleFpic.class);
                    intent.putExtra("pic","https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+uId+".png");
                    startActivity(intent);
                }
            });
            Log.d("username","www"+us);
            new Thread(setUpMy).start();





        }


        else if(getContentViewId()==R.layout.activity_notification){

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.chatbar);
            ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
            TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
            titlteText.setText(R.string.title_activity_notification);
            ImageView picView = (ImageView) findViewById(R.id.getPic);
            EditText titleText = (EditText) findViewById(R.id.titletext);
            EditText Neirong = (EditText) findViewById(R.id.neirong);
            EditText fufei = (EditText) findViewById(R.id.fufei);
            EditText ren = (EditText) findViewById(R.id.huoderenshu);
            TextView titleLimit = findViewById(R.id.textView9);
            TextView contentLimit = findViewById(R.id.textView10);

            TextView cic1 = (TextView) findViewById(R.id.circleText1);
            TextView cic2 = (TextView) findViewById(R.id.circleText2);
            TextView cic3 = (TextView) findViewById(R.id.circleText3);
            TextView cic4 = findViewById(R.id.circleText4);

            Button fabuBtn = (Button) findViewById(R.id.fabubtn);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            });

            titleText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = String.valueOf(s.toString().length())+"/50";
                    Log.d("title",str);
                    if(s.toString().length()>50){
                        titleLimit.setTextColor(getColor(R.color.alert));
                    }
                    else {
                        titleLimit.setTextColor(getColor(R.color.white));
                    }
                    titleLimit.setText(str);
                    titleLimit.setVisibility(View.VISIBLE);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            Neirong.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = String.valueOf(s.toString().length())+"/1000";
                    Log.d("neirong",str);
                    if(s.toString().length()>1000){
                        contentLimit.setTextColor(getColor(R.color.alert));
                    }
                    else {
                        contentLimit.setTextColor(getColor(R.color.white));
                    }
                    contentLimit.setText(str);
                    contentLimit.setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


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
            Spinner bi1 = findViewById(R.id.bizhong);
            Spinner bi2 = findViewById(R.id.bizhong2);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.currency_name, R.layout.item_select);

            adapter.setDropDownViewResource(R.layout.drop_down_item);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                    R.array.type, R.layout.item_select);

            adapter1.setDropDownViewResource(R.layout.drop_down_item);

            bi1.setAdapter(adapter);
            bi2.setAdapter(adapter1);
            bi1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    txtFuFeiType=parent.getItemAtPosition(position).toString();
                    txtShoufeiType=parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    txtFuFeiType = "Candy";
                    txtFuFeiType= "Candy";
                }
            });

            bi2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //选择列表项的操作
                    priceType = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //未选中时候的操作
                    priceType="Reward";

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
                        if(priceType.equals("Pay")) {
                            txtFuFei = fufei.getText().toString();
                            txtShoufei="0";
                        }
                        else {
                            txtShoufei = fufei.getText().toString();
                            txtFuFei="0";
                        }
                    }
                    else{
                        txtFuFei="0";
                        txtShoufei="0";
                    }
                    if(titleText.length()==0){
                        Log.d("wtftt"," shou fu " + fufei);
                        Toast.makeText(context,R.string.entitle,Toast.LENGTH_LONG).show();
                    }
                    else if(titleText.length()>50){
                        Log.d("wtftt"," shou fu " + fufei);
                        Toast.makeText(context,R.string.titlelimit,Toast.LENGTH_LONG).show();
                    }
                    else if(Neirong.length()==0){
                        Toast.makeText(context,R.string.encontent,Toast.LENGTH_LONG).show();
                    }
                    else if(Neirong.length()>1000){
                        Toast.makeText(context,R.string.contentLimit,Toast.LENGTH_LONG).show();
                    }
                    else if(clickFlag==0){
                        Toast.makeText(context,R.string.entag,Toast.LENGTH_LONG).show();
                    }
                    else if(ren.getText().length()==0){
                        Toast.makeText(context,R.string.enquan,Toast.LENGTH_LONG).show();
                    }
                    else {
                        textTilte = titleText.getText().toString();
                        textValue = Neirong.getText().toString();
                        renshu=Double.parseDouble(ren.getText().toString());
                        titleText.setText("");
                        Neirong.setText("");
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
                case 1:Log.d("mytou",msg.obj.toString());Picasso.get().invalidate(msg.obj.toString());Picasso.get().load(msg.obj.toString()).resize(60,60).centerCrop().into(tImage);break;
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
                Resources res = getResources();
                String text = String.format(res.getString(R.string.rep), " 0 ");
                msg.obj=text;
                pHandler.sendMessage(msg);
            }
            else {
                Message msg =new Message();
                msg.what=3;
                int sheng = userPoolDO.getShengWang().intValue();
                Resources res = getResources();
                String text = String.format(res.getString(R.string.rep), String.valueOf(sheng));
                msg.obj=text;
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
                case 1:Toast.makeText(context,R.string.upl,Toast.LENGTH_LONG).show();Intent intent = new Intent(BaseActivity.this,HomeActivity.class);
                    startActivity(intent);break;
                case 2:Toast.makeText(context,R.string.fnote,Toast.LENGTH_LONG).show();break;
                case 3:Toast.makeText(context,R.string.fupload,Toast.LENGTH_LONG).show();;break;
                case 4:Toast.makeText(context,R.string.ftoup,Toast.LENGTH_LONG).show();;break;
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
            double fee = Double.parseDouble(txtFuFei)*renshu;
            Log.d("thisshiit",userPoolDO.getAvailableWallet().toString()+username);
            if (userPoolDO.getAvailableWallet() >= fee) {
                Log.d("th11isshiit",userPoolDO.getAvailableWallet().toString()+username);
                userPoolDO.setFrozenwallet(userPoolDO.getFrozenwallet()+fee);
                userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet()-fee);
                List<String> pictureSet = new ArrayList<>();
                for (int i = 0; i < uriList.size(); i++) {
                    try {
                        String path = helper.getPath(uriList.get(i), context);
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
                List<String> idList=new ArrayList<>();
                if (userPoolDO.getChanceIdList() == null) {
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
                chanceWithValueDO.setFuFei(Double.parseDouble(txtFuFei));
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
            if(resultCode == 0){

            }
            else {
                uriList = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                for(int i=0;i<uriList.size();i++){
                    LinearLayout layout = (LinearLayout) findViewById(R.id.lineScroll);
                    onAddImage(layout,uriList.get(i));
                }
            }
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
            //Log.d("sameshiit1",String.valueOf(sameDay(userPoolDO.getLastLogin())));
            if(userPoolDO.getLastLogin()==null){
                userPoolDO.setLastLogin(loginTime);
                userPoolDO.setCandyCurrency(100.0);
                userPoolDO.setAvailableWallet(100.0);
                msg.what=0;
                loginHandler.sendMessage(msg);
            }
            else if(sameDay(userPoolDO.getLastLogin())!=0) {
                Log.d("sameshiit2",String.valueOf(sameDay(userPoolDO.getLastLogin())));
                int cLogin = userPoolDO.getConsecutiveLogin().intValue();
                Log.d("sameshiit3",String.valueOf(cLogin));
                int value;
                if (sameDay(userPoolDO.getLastLogin()) == 1) {
                    switch (cLogin) {
                        case 0:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 5);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 5);
                            msg.what = 1;
                            loginHandler.sendMessage(msg);
                            value = cLogin + 1;
                            Log.d("sameshiit",String.valueOf(value));
                            userPoolDO.setConsecutiveLogin((double)value);
                            break;
                        case 1:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 10);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 10);
                            msg.what = 2;
                            loginHandler.sendMessage(msg);
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            break;
                        case 2:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 15);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 15);
                            msg.what = 3;
                            loginHandler.sendMessage(msg);
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            break;
                        case 3:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 30);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 30);
                            msg.what = 4;
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            loginHandler.sendMessage(msg);
                            break;
                        case 4:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 40);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 40);
                            msg.what = 5;
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            loginHandler.sendMessage(msg);
                            break;
                        case 5:
                            userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() + 50);
                            userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + 50);
                            msg.what = 1;
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            loginHandler.sendMessage(msg);
                            break;
                        case 6:
                            userPoolDO.setShengWang(userPoolDO.getShengWang() + 1);
                            msg.what = 7;
                            loginHandler.sendMessage(msg);
                            value = cLogin + 1;
                            userPoolDO.setConsecutiveLogin((double)value);
                            break;
                    }
                    userPoolDO.setLastLogin(loginTime);
                }
            }

            dynamoDBMapper.save(userPoolDO);

        }
    };

    Handler loginHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int selector = msg.what;
            switch (selector) {
                case 0:
                    Toast.makeText(context, R.string.flogin, Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(context, R.string.onelog, Toast.LENGTH_LONG).show();
                    break;
                case 2:Toast.makeText(context, R.string.twolog, Toast.LENGTH_LONG).show();
                    break;
                case 3:Toast.makeText(context, R.string.threelog, Toast.LENGTH_LONG).show();
                    break;
                case 4:Toast.makeText(context, R.string.fourlog, Toast.LENGTH_LONG).show();
                    break;
                case 5:Toast.makeText(context, R.string.fivelog, Toast.LENGTH_LONG).show();
                    break;
                case 6:Toast.makeText(context, R.string.sixlog, Toast.LENGTH_LONG).show();
                    break;
                case 7:Toast.makeText(context, R.string.sevenlog, Toast.LENGTH_LONG).show();
                    break;
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
            SharedPreferences preferences = getSharedPreferences("ipaddress",0);
            //helper.createAccount(preferences,dynamoDBMapper,username);
        }
    };

    public void onAddImage(LinearLayout horLay, Uri uri){
        View layout1 = LayoutInflater.from(this).inflate(R.layout.addimageview, horLay, false);
        ImageView img = layout1.findViewById(R.id.imageView22);
        Picasso.get().load(uri).resize(40,40).into(img);
        horLay.addView(layout1);

    };

    private int sameDay(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        int dayDif = Integer.parseInt(sameday2)-Integer.parseInt(sameday1);
        return  dayDif;

    }



}
