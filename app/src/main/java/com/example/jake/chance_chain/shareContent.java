package com.example.jake.chance_chain;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class shareContent extends AppCompatActivity {
    private chanceClass chanceC;
    DynamoDBMapper dynamoDBMapper;
    ImageView touImg,likeImg,likeYellow;
    TextView uName,uTime,nText,zhuanNum,comNum,zhanNum,jineTxt,fuTxt,renshu;
    List<ImageView> imgList = new ArrayList<>();
    List<String> strList = new ArrayList<>();
    List<commentClass> comClass = new ArrayList<>();
    int shareNum ;
    int liuNum ;
    int likeNum ;
    AppHelper helper = new AppHelper();
    String liuText;
    Context context;
    String curUsername;
    RecyclerView mRecyclerView;
    CommentAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    PopupWindow popupWindow;
    View rootview;
    TextView showText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        context = getApplication().getApplicationContext();
        curUsername = helper.getCurrentUserName(context);
        dynamoDBMapper=helper.getMapper(context);
        LinearLayout imgLay = (LinearLayout) findViewById(R.id.imgLayout);
        chanceC = (chanceClass) getIntent().getParcelableExtra("cc");
        shareNum = chanceC.shared;
        liuNum = chanceC.cNumber;
        likeNum = chanceC.liked.size();

        int rNum = (int) chanceC.renshu;
        renshu.setText(" Chance left: "+String.valueOf(rNum));
        touImg = (ImageView) findViewById(R.id.contentTou);
        uName = (TextView) findViewById(R.id.contentUid);
        uTime = (TextView) findViewById(R.id.contentTime);
        nText = (TextView) findViewById(R.id.contentNei);
        likeImg = (ImageView) findViewById(R.id.zanSpic);
        likeYellow = (ImageView) findViewById(R.id.zanSpicyellow);
        zhuanNum = (TextView) findViewById(R.id.zhuanBar);
        comNum = (TextView) findViewById(R.id.pinBar);
        zhanNum = (TextView) findViewById(R.id.zanBar);
        if(chanceC.liked.contains(curUsername)) {
            likeImg.setVisibility(View.INVISIBLE);
            likeYellow.setVisibility(View.VISIBLE);
        }
        else{
            likeImg.setVisibility(View.VISIBLE);
            likeYellow.setVisibility(View.INVISIBLE);
        }

        if(!chanceC.touUri.isEmpty()){
            Picasso.get().load(chanceC.touUri).resize(60,60).into(touImg);
        }
        uName.setText(chanceC.userid);
        Log.d("uptime",String.valueOf(chanceC.uploadTime));

        uTime.setText(helper.displayTime(String.valueOf((long) chanceC.uploadTime)));
        nText.setText(chanceC.txtNeirong);
        if(chanceC.imageSet.size()!=0){
            strList = chanceC.imageSet;
            for(int i = 0; i<strList.size();i++){
                ImageView neiImg =new ImageView(this);
                Picasso.get().load(strList.get(i)).into(neiImg);
                imgList.add(neiImg);
            }
        }
        for(int i =0;i<imgList.size();i++){
            imgLay.addView(imgList.get(i));
            TextView spaceImg = new TextView(this);
            spaceImg.setHeight(20);
            imgLay.addView(spaceImg);
        }


        zhuanNum.setText(String.valueOf(shareNum));
        comNum.setText(String.valueOf(liuNum));
        zhanNum.setText(String.valueOf(likeNum));


        RelativeLayout fLay = (RelativeLayout) findViewById(R.id.firstBar);
        RelativeLayout sLay = (RelativeLayout) findViewById(R.id.secondBar);
        RelativeLayout tLay = (RelativeLayout) findViewById(R.id.thirdBar);
        LinearLayout comLayout = (LinearLayout) findViewById(R.id.liulay);

        mRecyclerView = (RecyclerView) findViewById(R.id.cRecycle);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter= new CommentAdapter(context,chanceC.commentList);
        mRecyclerView.setAdapter(mAdapter);


        fLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shareContent.this,sharingActivity.class);
                intent.putExtra("link",chanceC);
                startActivity(intent);
            }
        });

        sLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comLayout.getVisibility() == View.VISIBLE) {
                    comLayout.setVisibility(View.INVISIBLE);
                } else {
                    comLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        tLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chanceC.liked.contains(curUsername)) {
                    likeImg.setVisibility(View.VISIBLE);
                    likeYellow.setVisibility(View.INVISIBLE);
                    chanceC.deleteLike(curUsername);
                    likeNum--;
                    zhanNum.setText(String.valueOf(likeNum));

                }
                else{
                    likeImg.setVisibility(View.INVISIBLE);
                    likeYellow.setVisibility(View.VISIBLE);
                    chanceC.addLiked(curUsername);
                    likeNum++;
                    zhanNum.setText(String.valueOf(likeNum));
                }
                new Thread(likeThread).start();

            }
        });

        ImageView faLiu = (ImageView) findViewById(R.id.faliuyan);
        EditText shuruFa = (EditText) findViewById(R.id.pingText);

        faLiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liuText = shuruFa.getText().toString();
                chanceC.cNumber++;
                comNum.setText(String.valueOf(chanceC.cNumber));
                new Thread(liuThread).start();
                comLayout.setVisibility(View.INVISIBLE);
            }
        });


        rootview = LayoutInflater.from(shareContent.this).inflate(R.layout.activity_content, null);
        showText = (TextView) findViewById(R.id.xianzhi);

    }





    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d("handler",String.valueOf(msg.what));

            if(msg.what==1){
                zhanNum.setText(msg.obj.toString());
            }
            else if(msg.what==2){
                chanceClass cClass = (chanceClass) msg.getData().getParcelable("chance");
                mAdapter= new CommentAdapter(context,cClass.commentList);
                mRecyclerView.setAdapter(mAdapter);
                comNum.setText(String.valueOf(liuNum));

            }

        }

    };


    Runnable liuThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,curUsername);
            int totcc;
            if(cc.getCommentIdList()!=null) {
                totcc = cc.getCommentIdList().size();
                totcc+=1;
            }
            else {
                totcc=1;
            }
            chanceC.setcNumber(totcc);
            Message msg = new Message();
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedList<CommentTableDO> result = dynamoDBMapper.scan(CommentTableDO.class,scanExpression);
            int comId = result.size()+1;
            final CommentTableDO commentTableDO = new CommentTableDO();
            commentTableDO.setCommentId(String.valueOf(comId));
            commentTableDO.setChanceId(chanceC.cId);
            commentTableDO.setCommentText(liuText);
            commentTableDO.setUserId(curUsername);
            Date currentTime = Calendar.getInstance().getTime();
            String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
            commentTableDO.setUpTime(dateString);
            commentClass commentClass = new commentClass(String.valueOf(comId),dateString,chanceC.cId,liuText,curUsername);
            if(userPoolDO.getProfilePic()!=null){
                commentTableDO.setUserPic(userPoolDO.getProfilePic());
                commentClass.setUpic(userPoolDO.getProfilePic());
            }
            chanceC.addComList(commentClass);
            chanceC.addComId(String.valueOf(comId));
            if(cc.getCommentIdList()!=null) {
                List<String> tempList = cc.getCommentIdList();
                tempList.add(String.valueOf(comId));
                cc.setCommentIdList(tempList);
            }
            else{
                List<String> commentId = new ArrayList<>();
                commentId.add(String.valueOf(comId));
                cc.setCommentIdList(commentId);
            }
            Log.d("show comid ",String.valueOf(commentTableDO.getCommentId())+" "+String.valueOf(cc.getCommentIdList().size()));
            dynamoDBMapper.save(commentTableDO);
            dynamoDBMapper.save(cc);
            msg.what=2;
            Bundle bundle = new Bundle();
            bundle.putParcelable("chance",chanceC);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }
    };

    Runnable likeThread = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO cc = dynamoDBMapper.load(ChanceWithValueDO.class,chanceC.cId);
            if(cc.getLiked()!=null) {
                if (cc.getLiked().contains(curUsername)) {
                    List<String> like = cc.getLiked();
                    like.remove(curUsername);
                }
                else{
                    List<String> like = cc.getLiked();
                    like.add(curUsername);
                    cc.setLiked(like);
                }
            }
            else {
                List<String> Liked = new ArrayList<>();
                Liked.add(curUsername);
                cc.setLiked(Liked);
            }

            dynamoDBMapper.save(cc);

        }
    };



}
