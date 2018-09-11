package com.example.jake.chance_chain;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.util.ArrayList;
import java.util.List;

public class hisThread extends Thread {
    private HisActivity hisActivity;
    private DynamoDBMapper dynamoDBMapper;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    private List<String> imgList = new ArrayList<>();
    private volatile boolean running=true;
    private HisFragment fg;
    private String userName;
    private FragmentTransaction ft;
    AppHelper helper = new AppHelper();
    public hisThread(HisActivity hisActivity, DynamoDBMapper dynamoDBMapper, FragmentTransaction fragt, HisFragment fragment, String uid) {
        this.hisActivity = hisActivity;
        this.dynamoDBMapper=dynamoDBMapper;
        this.ft = fragt;
        this.fg=fragment;
        this.userName = uid;
    }

    public void run() {

        UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userName);
        int totChance;
        if(userPoolDO.getChanceIdList()==null){
            totChance=0;
        }
        else{
            totChance=userPoolDO.getChanceIdList().size()-1;
            Log.d("thread", "fg"+String.valueOf(totChance));
            Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));

            if(totChance >= 10){
                for(int i = totChance;i>=totChance-9;i--){
                    putStuffin(userPoolDO.getChanceIdList().get(i));

                }
            }
            else{
                for(int i = totChance;i>=0;i--){
                    putStuffin(userPoolDO.getChanceIdList().get(i));

                }
            }
            ;


            Log.d("thread", "fg"+"sdf");



            Log.d("thread", "fg"+"sd123f");

        }
        hisActivity.setHistFragment(cList,ft);

        //设置布局管理器

        //设置适配器


    }

    public void putStuffin(String i){
        ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,i);
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
            cc.setcNumber(chanceWithValueDO.getCommentIdList().size());
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
