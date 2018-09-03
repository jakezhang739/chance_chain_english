package com.example.jake.chance_chain;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class myThread extends Thread {
    BaseActivity baseActivity;
    DynamoDBMapper dynamoDBMapper;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    private List<String> imgList = new ArrayList<>();
    private volatile boolean running=true;
    private HomeFragment fg;
    private FragmentTransaction ft;
    AppHelper helper = new AppHelper();
    public myThread(BaseActivity baseActivity, DynamoDBMapper dynamoDBMapper, FragmentTransaction fragt, HomeFragment fragment) {
        this.baseActivity = baseActivity;
        this.dynamoDBMapper=dynamoDBMapper;
        this.ft = fragt;
        this.fg=fragment;
    }

    public void run() {

        int totChance = helper.returnChanceeSize(dynamoDBMapper);

        Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));

        if(totChance >= 10){
            for(int i = totChance;i>=totChance-9;i--){
                putStuffin(i);

            }
        }
        else{
            for(int i = totChance;i>=1;i--){
                putStuffin(i);

            }
        }
        Log.d("thread", "fg"+"sdf11");
        ;


        Log.d("thread", "fg"+"sdf");



        baseActivity.setFragment(cList,ft);
        Log.d("thread", "fg"+"sd123f");


        //设置布局管理器

        //设置适配器


    }

    public void putStuffin(int i){
        ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
        chanceClass cc = new chanceClass(chanceWithValueDO.getShouFeiType(), chanceWithValueDO.getFuFeiType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getShouFei(),chanceWithValueDO.getFuFei(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime(),chanceWithValueDO.getRenShu());
        UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,cc.userid);
        if(userPoolDO.getProfilePic()!=null){
            cc.settImg(userPoolDO.getProfilePic());
            chanceWithValueDO.setProfilePicture(userPoolDO.getProfilePic());
        }
        if(chanceWithValueDO.getProfilePicture()!=null){
            cc.settImg(chanceWithValueDO.getProfilePicture());
        }
        if(chanceWithValueDO.getPictures()!=null){
            cc.setPicture(chanceWithValueDO.getPictures());
        }
        if(chanceWithValueDO.getShared()!=null){
            cc.setShare(chanceWithValueDO.getShared());
        }
        if(chanceWithValueDO.getGetList()!=null){
            cc.setGetList(chanceWithValueDO.getGetList());
        }
        if(chanceWithValueDO.getLiked()!=null){
            cc.setLiked(chanceWithValueDO.getLiked());
        }
        if(chanceWithValueDO.getCommentIdList()!=null){
            int cTotal = chanceWithValueDO.getCommentIdList().size();
            Log.d("showTot",String.valueOf(cTotal));
            cc.setcNumber((double)chanceWithValueDO.getCommentIdList().size());
            for(int j =0;j<cTotal;j++){
                CommentTableDO commentTableDO = dynamoDBMapper.load(CommentTableDO.class,chanceWithValueDO.getCommentIdList().get(j));
                commentClass comC = new commentClass(commentTableDO.getCommentId(),commentTableDO.getUpTime(),commentTableDO.getChanceId(),commentTableDO.getCommentText(),commentTableDO.getUserId());
                if(commentTableDO.getUserPic()!=null){
                    comC.setUpic(commentTableDO.getUserPic());
                }
                cc.addComList(comC);
            }
        }
        if(chanceWithValueDO.getSharedFrom()!=null){
            cc.setSharfrom(chanceWithValueDO.getSharedFrom());
        }
        dynamoDBMapper.save(chanceWithValueDO);
        cList.add(cc);
    }

}
