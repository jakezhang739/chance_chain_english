package com.example.jake.chance_chain;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class wodejihui extends AppCompatActivity {
    Context context;
    String myUsr;
    AppHelper helper = new AppHelper();
    DynamoDBMapper mapper;
    List<chanceClass> weiJingxin = new ArrayList<>();
    List<chanceClass> jinXingZhong = new ArrayList<>();
    List<chanceClass> yiWanCheng = new ArrayList<>();
    int flag=1;
    LinearLayout upLayout,taglayout,beijing;
    String tempCid;
    ProgressBar progressBar;
    chanceClass tempClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodejihui);
        ActionBar actionBar = getSupportActionBar();
        context = getApplicationContext().getApplicationContext();
        myUsr = helper.getCurrentUserName(context);
        mapper = helper.getMapper(context);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.chatbar);
        new Thread(myFabu).start();
        ImageView back = (ImageView) actionBar.getCustomView().findViewById(R.id.back);
        TextView titlteText = (TextView) actionBar.getCustomView().findViewById(R.id.title);
        upLayout = (LinearLayout) findViewById(R.id.faburel);
        taglayout = (LinearLayout) findViewById(R.id.tagrel);
        beijing = (LinearLayout) findViewById(R.id.viewLay);
        progressBar = (ProgressBar) findViewById(R.id.progressBarchat);
        titlteText.setText("我的机会");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wodejihui.this,MyActivity.class);
                startActivity(intent);

            }
        });
        TextView jinXinZhong = (TextView) findViewById(R.id.jxz);
        TextView yiWanCheng = (TextView) findViewById(R.id.ywc);
        TextView tag1 = (TextView) findViewById(R.id.tag1);
        TextView tag2 = (TextView) findViewById(R.id.tag2);

        jinXinZhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(jxzRun).start();
                flag=2;
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.INVISIBLE);
            }
        });

        yiWanCheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(ywcRun).start();
                flag=3;
                tag1.setVisibility(View.INVISIBLE);
                tag2.setVisibility(View.VISIBLE);
            }
        });

    }

    Handler firstHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            upLayout.setVisibility(View.VISIBLE);
            taglayout.setVisibility(View.VISIBLE);
//            switch (msg.what){
//                case 1:weiJingxin=(List<chanceClass>) msg.obj;
//                case 2:jinXingZhong=(List<chanceClass>) msg.obj;
//                case 3:yiWanCheng=(List<chanceClass>) msg.obj;
//            }
        }
    };

    Handler setupHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:progressBar.setVisibility(View.INVISIBLE);onAddJingXing((chanceClass) msg.obj,msg.getData().getInt("index"));break;
                case 2:progressBar.setVisibility(View.INVISIBLE);break;
                case 3:beijing.removeAllViews();progressBar.setVisibility(View.VISIBLE);break;
                case 4:progressBar.setVisibility(View.INVISIBLE);onAddView((chanceClass) msg.obj);break;


            }

        }
    };

    public void onAddView(chanceClass cList){
        View layout1 = LayoutInflater.from(this).inflate(R.layout.fabuitem, beijing, false);
        ImageView uImg,tagView,moreContent;
        TextView mTxt,uidTxt,timeTxt,dianzhan,fenxiang,pingjia,confTxt,unconfTxt;
        GridView mGridview;
        CardView cardView;
        Button confirmBtn,cancelBtn;
        Spinner invis = (Spinner) layout1.findViewById(R.id.select);
        invis.setVisibility(View.INVISIBLE);
        mTxt=(TextView) layout1.findViewById(R.id.neirongTxt);
        uImg=(ImageView) layout1.findViewById(R.id.touxiangImg);
        uidTxt=(TextView) layout1.findViewById(R.id.userNameText);
        timeTxt=(TextView) layout1.findViewById(R.id.timeview);
        tagView=(ImageView) layout1.findViewById(R.id.tagView);
        mGridview = (GridView) layout1.findViewById(R.id.gallery);
        moreContent = (ImageView) layout1.findViewById(R.id.gengduo);
        unconfTxt = (TextView) layout1.findViewById(R.id.unConfirmtxt);
        cardView = (CardView) layout1.findViewById(R.id.card_view);
        pingjia = (TextView) layout1.findViewById(R.id.liuyan);
        fenxiang = (TextView) layout1.findViewById(R.id.fenxiang);
        dianzhan = (TextView) layout1.findViewById(R.id.dianzhan);
        confirmBtn = (Button) layout1.findViewById(R.id.button4);
        cancelBtn = (Button) layout1.findViewById(R.id.button5);
        confirmBtn.setVisibility(View.INVISIBLE);
        cancelBtn.setVisibility(View.INVISIBLE);
        confTxt = (TextView) layout1.findViewById(R.id.confirmtxt);
        unconfTxt = (TextView) layout1.findViewById(R.id.unConfirmtxt);

        RelativeLayout selectlay = layout1.findViewById(R.id.spRel);
        selectlay.setVisibility(View.INVISIBLE);
        mTxt.setText(cList.txtTitle);
        uidTxt.setText(cList.userid);
        String display = displayTime(String.valueOf((long) cList.uploadTime));
        timeTxt.setText(display);
        if(cList.confirmList.contains(myUsr)){
            confirmBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
            confTxt.setVisibility(View.VISIBLE);
        }

        if(cList.unConfirmList.contains(myUsr)){
            confirmBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
            unconfTxt.setVisibility(View.VISIBLE);
        }
        switch ((int) cList.tag) {
            case 1:
                tagView.setImageResource(R.drawable.huodong);
                break;
            case 2:
                tagView.setImageResource(R.drawable.yuema);
                break;
            case 3:
                tagView.setImageResource(R.drawable.remwu);
                break;
            case 4:
                tagView.setImageResource(R.drawable.qita);
                break;
        }
        pingjia.setText(String.valueOf(cList.cNumber));
        if (!cList.touUri.isEmpty()) {
            Picasso.get().load(cList.touUri).placeholder(R.drawable.splash).into(uImg);
        }
        if (cList.imageSet.size() != 0) {
            ImageAdapter imageAdapter = new ImageAdapter(context,cList.imageSet);
            int adprow = cList.imageSet.size()/4;;
            if(cList.imageSet.size()%4>0){
                adprow++;
            }
            ViewGroup.LayoutParams params =mGridview.getLayoutParams();
            params.height=250*adprow;
            mGridview.setLayoutParams(params);
            mGridview.setAdapter(imageAdapter);
        }
        if (cList.liked.size() != 0) {
            dianzhan.setText(String.valueOf(cList.liked.size()));
        }
        if (cList.shared != 0) {
            fenxiang.setText(String.valueOf(cList.shared));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wodejihui.this, ContentActivity.class);
                intent.putExtra("cc", cList);
                startActivity(intent);

            }
        });
        beijing.addView(layout1);
        Log.d("wodejihui",cList.userid.toString());
    }

    public void onAddJingXing(chanceClass cList,int i){
        View layout1 = LayoutInflater.from(this).inflate(R.layout.fabuitem, beijing, false);
        ImageView uImg,tagView,moreContent;
        TextView mTxt,uidTxt,timeTxt,dianzhan,fenxiang,pingjia,confTxt,unconfTxt;
        GridView mGridview;
        CardView cardView;
        Button confirmBtn,cancelBtn;
        Spinner invis = (Spinner) layout1.findViewById(R.id.select);
        invis.setVisibility(View.INVISIBLE);
        mTxt=(TextView) layout1.findViewById(R.id.neirongTxt);
        uImg=(ImageView) layout1.findViewById(R.id.touxiangImg);
        uidTxt=(TextView) layout1.findViewById(R.id.userNameText);
        timeTxt=(TextView) layout1.findViewById(R.id.timeview);
        tagView=(ImageView) layout1.findViewById(R.id.tagView);
        mGridview = (GridView) layout1.findViewById(R.id.gallery);
        moreContent = (ImageView) layout1.findViewById(R.id.gengduo);
        unconfTxt = (TextView) layout1.findViewById(R.id.unConfirmtxt);
        cardView = (CardView) layout1.findViewById(R.id.card_view);
        pingjia = (TextView) layout1.findViewById(R.id.liuyan);
        fenxiang = (TextView) layout1.findViewById(R.id.fenxiang);
        dianzhan = (TextView) layout1.findViewById(R.id.dianzhan);
        confirmBtn = (Button) layout1.findViewById(R.id.button4);
        cancelBtn = (Button) layout1.findViewById(R.id.button5);
        RelativeLayout selectlay = layout1.findViewById(R.id.spRel);
        selectlay.setVisibility(View.INVISIBLE);
        mTxt.setText(cList.txtTitle);
        uidTxt.setText(cList.userid);
        String display = displayTime(String.valueOf((long) cList.uploadTime));
        timeTxt.setText(display);
        switch ((int) cList.tag) {
            case 1:
                tagView.setImageResource(R.drawable.huodong);
                break;
            case 2:
                tagView.setImageResource(R.drawable.yuema);
                break;
            case 3:
                tagView.setImageResource(R.drawable.remwu);
                break;
            case 4:
                tagView.setImageResource(R.drawable.qita);
                break;
        }
        pingjia.setText(String.valueOf(cList.cNumber));
        if (!cList.touUri.isEmpty()) {
            Picasso.get().load(cList.touUri).placeholder(R.drawable.splash).into(uImg);
        }
        if (cList.imageSet.size() != 0) {
            ImageAdapter imageAdapter = new ImageAdapter(context,cList.imageSet);
            int adprow = cList.imageSet.size()/4;;
            if(cList.imageSet.size()%4>0){
                adprow++;
            }
            ViewGroup.LayoutParams params =mGridview.getLayoutParams();
            params.height=250*adprow;
            mGridview.setLayoutParams(params);
            mGridview.setAdapter(imageAdapter);
        }
        if (cList.liked.size() != 0) {
            dianzhan.setText(String.valueOf(cList.liked.size()));
        }
        if (cList.shared != 0) {
            fenxiang.setText(String.valueOf(cList.shared));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wodejihui.this, ContentActivity.class);
                intent.putExtra("cc", cList);
                startActivity(intent);

            }
        });
        beijing.addView(layout1);
        Log.d("wodejihui",cList.userid.toString());
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempCid = cList.cId;
                Log.d("wodejihui",cList.userid.toString());
                tempClass = cList;
                new Thread(oncConfirm).start();
                jinXingZhong.remove(i);
                beijing.removeView(layout1);


            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempCid = cList.cId;
                jinXingZhong.remove(i);
                beijing.removeView(layout1);
                Log.d("wodejihui",cList.userid.toString());
                new Thread(onCancel).start();
            }
        });
    }


    Runnable oncConfirm = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO chanceWithValueDO = mapper.load(ChanceWithValueDO.class, tempCid);
            if(chanceWithValueDO.getGetList()!=null){
                if(chanceWithValueDO.getGetList().contains(myUsr)){
                    List<String> tempGet = chanceWithValueDO.getGetList();
                    tempGet.remove(myUsr);
                    if(tempGet.size()==0){
                        tempGet=null;
                    }
                    chanceWithValueDO.setGetList(tempGet);
                    List<String> temp = new ArrayList<>();
                    if(chanceWithValueDO.getCompleteList()!=null){
                        temp=chanceWithValueDO.getCompleteList();
                    }
                    temp.add(myUsr);
                    chanceWithValueDO.setCompleteList(temp);
                    mapper.save(chanceWithValueDO);
                    yiWanCheng.add(tempClass);
//                    Log.d("wodejihui",chanceWithValueDO.getCompleteList().toString()+chanceWithValueDO.getGetList().toString());

                }
            }
        }
    };

    Runnable onCancel = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO chanceWithValueDO = mapper.load(ChanceWithValueDO.class, tempCid);
            if(chanceWithValueDO.getGetList()!=null) {
                if (chanceWithValueDO.getGetList().contains(myUsr)) {
                    if (chanceWithValueDO.getShouFei() != null) {
                        UserPoolDO userPoolDO = mapper.load(UserPoolDO.class, myUsr);
                        double frozenValue = userPoolDO.getFrozenwallet();
                        double freeValue = chanceWithValueDO.getShouFei();
                        userPoolDO.setFrozenwallet(frozenValue - freeValue);
                        userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + freeValue * 0.9);
                        userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() - freeValue * 0.1);
                        List<String> temp = new ArrayList<>();
                        temp = userPoolDO.getGottenList();
                        temp.remove(tempCid);
                        if (temp.size() == 0) {
                            temp = null;
                        }
                        userPoolDO.setGottenList(temp);
                        mapper.save(userPoolDO);
                    } else {
                        String hisUser = chanceWithValueDO.getUsername();
                        UserPoolDO userPoolDO = mapper.load(UserPoolDO.class, hisUser);
                        double frozenValue = userPoolDO.getFrozenwallet();
                        double freeValue = chanceWithValueDO.getShouFei();
                        userPoolDO.setFrozenwallet(frozenValue - freeValue);
                        userPoolDO.setAvailableWallet(userPoolDO.getAvailableWallet() + freeValue * 0.9);
                        userPoolDO.setCandyCurrency(userPoolDO.getCandyCurrency() - freeValue * 0.1);
                        List<String> temp = new ArrayList<>();
                        temp = userPoolDO.getGottenList();
                        temp.remove(tempCid);
                        if (temp.size() == 0) {
                            temp = null;
                        }
                        userPoolDO.setGottenList(temp);
                        mapper.save(userPoolDO);

                    }
                    List<String> temp = new ArrayList<>();
                    List<String> tempGet = chanceWithValueDO.getGetList();
                    tempGet.remove(myUsr);
                    if (tempGet.size() == 0) {
                        chanceWithValueDO.setGetList(null);
                    } else {
                        chanceWithValueDO.setGetList(tempGet);
                    }
                    mapper.save(chanceWithValueDO);

                }
            }
        }
    };


    Runnable jxzRun = new Runnable() {
        @Override
        public void run() {
            Log.d("idontkonw",String.valueOf(weiJingxin.size())+String.valueOf(jinXingZhong.size())+String.valueOf(yiWanCheng.size()));
            Message msg1 = new Message();
            msg1.what=3;
            setupHandler.sendMessage(msg1);
            if(jinXingZhong.isEmpty()){
                Message msg = new Message();
                msg.what=2;
                setupHandler.sendMessage(msg);

            }
            for(int i=jinXingZhong.size()-1; i>=0 ;i--){
                Message msg = new Message();
                msg.what = 1;
                msg.obj = jinXingZhong.get(i);
                Bundle bundle = new Bundle();
                bundle.putInt("index",i);
                msg.setData(bundle);
                setupHandler.sendMessage(msg);
            }

        }
    };


    Runnable ywcRun = new Runnable() {
        @Override
        public void run() {
            Message msg1 = new Message();
            msg1.what=3;
            setupHandler.sendMessage(msg1);
            if(yiWanCheng.isEmpty()){
                Message msg = new Message();
                msg.what=2;
                setupHandler.sendMessage(msg);

            }
            for(int i=yiWanCheng.size()-1; i>=0 ;i--){
                Message msg = new Message();
                msg.what = 4;
                msg.obj = yiWanCheng.get(i);
                setupHandler.sendMessage(msg);
            }
        }
    };

    Runnable myFabu = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = mapper.load(UserPoolDO.class, myUsr);
            if(userPoolDO.getGottenList()!=null) {
                List<String> chanceID = userPoolDO.getGottenList();
                for (int i = 0; i < chanceID.size(); i++) {
                    putStuffin(chanceID.get(i));
                }
                Log.d("showyourself", String.valueOf(weiJingxin.size()) + String.valueOf(jinXingZhong.size()) + String.valueOf(yiWanCheng.size()));
                Message msg1 = new Message();
                firstHandler.sendMessage(msg1);
                if (jinXingZhong.isEmpty()) {
                    Message msg = new Message();
                    msg.what = 2;
                    setupHandler.sendMessage(msg);

                }
                for (int i = jinXingZhong.size()-1; i >=0; i--) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = jinXingZhong.get(i);
                    setupHandler.sendMessage(msg);
                }
            }
            else{
                Message msg = new Message();
                msg.what = 2;
                setupHandler.sendMessage(msg);
            }

        }
    };

    public void putStuffin(String i) {
        ChanceWithValueDO chanceWithValueDO = mapper.load(ChanceWithValueDO.class, String.valueOf(i));
        chanceClass cc = new chanceClass(chanceWithValueDO.getShouFeiType(), chanceWithValueDO.getFuFeiType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getShouFei(),chanceWithValueDO.getFuFei(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime(),chanceWithValueDO.getRenShu());
        UserPoolDO userPoolDO = mapper.load(UserPoolDO.class, cc.userid);
        if(chanceWithValueDO.getConfirmList()!=null){
            cc.confirmList=chanceWithValueDO.getConfirmList();
        }
        if(chanceWithValueDO.getUnConfirmList()!=null){
            cc.unConfirmList=chanceWithValueDO.getUnConfirmList();
        }
        if (userPoolDO.getProfilePic() != null) {
            cc.settImg(userPoolDO.getProfilePic());
            chanceWithValueDO.setProfilePicture(userPoolDO.getProfilePic());
        }
        if (chanceWithValueDO.getProfilePicture() != null) {
            cc.settImg(chanceWithValueDO.getProfilePicture());
        }
        if (chanceWithValueDO.getPictures() != null) {
            cc.setPicture(chanceWithValueDO.getPictures());
        }
        if (chanceWithValueDO.getShared() != null) {
            cc.setShare(chanceWithValueDO.getShared());
        }
        if (chanceWithValueDO.getGetList() != null) {
            cc.setGetList(chanceWithValueDO.getGetList());
        }
        if (chanceWithValueDO.getLiked() != null) {
            cc.setLiked(chanceWithValueDO.getLiked());
        }
        if (chanceWithValueDO.getCommentIdList() != null) {
            int cTotal = chanceWithValueDO.getCommentIdList().size();
            Log.d("showTot", String.valueOf(cTotal));
            cc.setcNumber((double) chanceWithValueDO.getCommentIdList().size());
            for (int j = 0; j < cTotal; j++) {
                CommentTableDO commentTableDO = mapper.load(CommentTableDO.class, chanceWithValueDO.getCommentIdList().get(j));
                commentClass comC = new commentClass(commentTableDO.getCommentId(), commentTableDO.getUpTime(), commentTableDO.getChanceId(), commentTableDO.getCommentText(), commentTableDO.getUserId());
                if (commentTableDO.getUserPic() != null) {
                    comC.setUpic(commentTableDO.getUserPic());
                }
                cc.addComList(comC);
            }
        }
        if(chanceWithValueDO.getCompleteList()!=null){
            cc.setCompleteList(chanceWithValueDO.getCompleteList());
        }
        if (chanceWithValueDO.getSharedFrom() != null) {
            cc.setSharfrom(chanceWithValueDO.getSharedFrom());
        }
        if(chanceWithValueDO.getGetList()!=null){
            if(chanceWithValueDO.getGetList().contains(myUsr)) {
                jinXingZhong.add(cc);
            }
            else{
                yiWanCheng.add(cc);
            }
        }
        else {
            yiWanCheng.add(cc);
        }
        mapper.save(chanceWithValueDO);
        Message msg = new Message();
        firstHandler.sendMessage(msg);

    }

    private String displayTime(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        int hr1,hr2,min1,min2;
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        hr1=Integer.parseInt(thatTime.substring(8,10));
        hr2=Integer.parseInt(dateString.substring(8,10));
        min1=Integer.parseInt(thatTime.substring(10,12));
        min2=Integer.parseInt(dateString.substring(10,12));
        if(!sameday1.equals(sameday2)){
            Log.d("same ",sameday1 + " " + sameday2);
            return sameday1.substring(0,4)+"年"+sameday1.substring(4,6)+'月'+sameday1.substring(6,8)+"号";
        }
        else if(hr1!=hr2){
            Log.d("hr ",hr1+" "+hr2);
            return String.valueOf(hr2-hr1)+"小时前";
        }
        else if(min1!=min2){
            Log.d("min ", min1+" "+min2);
            return String.valueOf(min2-min1)+"分钟前";
        }
        else{
            return "刚刚";
        }



    }


}

