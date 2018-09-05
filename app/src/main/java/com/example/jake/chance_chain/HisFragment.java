package com.example.jake.chance_chain;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HisFragment extends Fragment {

    List<String> mImage,mText,touUri,usId;
    DynamoDBMapper dynamoDBMapper;
    private boolean refreshFlag = false;
    private boolean loadmoreFlag = false;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    Boolean nomoreFlag = false;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    int uploadOffset=-1;
    int tempOffset=-1;
    String userName;
    AppHelper helper = new AppHelper();
    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "Pull down to refresh";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "Refreshing...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "Loading...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "Release to refresh";
        ClassicsHeader.REFRESH_HEADER_FINISH = "Refreshing completed";
        ClassicsHeader.REFRESH_HEADER_FAILED = "Refreshing failed";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "Pull up to load more";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "Relese to load more";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "Refreshing...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "Loading...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "Loading success";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "Loading fail";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "No more data";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("frag","yoyoyoyoyooyoyo");
        dynamoDBMapper = helper.getMapper(getContext());



        View view = inflater.inflate(R.layout.fragment_his, container, false);
        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setSpinnerStyle(SpinnerStyle.FixedBehind));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));




        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


//
//        Log.d("home12", "how many wtf do i need");
//
//
        mAdapter = new GalleryAdapter(getContext(), cList);

        mRecyclerView.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.d("uplll "," "+uploadOffset);


                new Thread(pullDownRunnable).start();

                while(!refreshFlag){

                }
                refreshlayout.finishRefresh(refreshFlag);//传入false表示刷新失败
                refreshLayout.setNoMoreData(false);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Thread(pullUpLoad).start();
                while(!loadmoreFlag){

                }
                if(nomoreFlag){

                    refreshLayout.finishLoadMore(500,loadmoreFlag,true);
                }

                refreshLayout.finishLoadMore(500,loadmoreFlag,false);
            }
        });

        return view;
    }



    public void setClass(List<chanceClass> cc, String user){
        this.cList = cc;
        this.userName = user;
    }

    Runnable pullDownRunnable = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userName);
            int totChance;
            if(userPoolDO.getChanceIdList()==null){
                totChance=0;
            }
            else{
                totChance=userPoolDO.getChanceIdList().size()-1;
            }

            cList.clear();

            if(totChance >= 10){
                for(int i = totChance;i>=totChance-9;i--){
                    fragPutIn(Integer.parseInt(userPoolDO.getChanceIdList().get(i)));

                }
            }
            else{
                for(int i = totChance;i>=0;i--){
                    fragPutIn(Integer.parseInt(userPoolDO.getChanceIdList().get(i)));

                }
            }


            refreshFlag = true;
        }
    };

    Runnable pullUpLoad = new Runnable() {
        @Override
        public void run() {
            UserPoolDO userPoolDO = dynamoDBMapper.load(UserPoolDO.class,userName);
            int i = userPoolDO.getChanceIdList().indexOf(cList.get(cList.size()-1).cId)-1;
            Log.d("hisfrag",String.valueOf(i));
            if(i<0){
                nomoreFlag=true;
            }
            int tempi = i;
            while(i>=0){
                if(tempi-1>=10){
                    break;
                }
                fragPutIn(Integer.parseInt(userPoolDO.getChanceIdList().get(i)));
                i--;
            }

            loadmoreFlag=true;


        }
    };

    public void fragPutIn(int i){
        Log.d("fragshowstuff", "come on "+String.valueOf(i));
        ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
        chanceClass cc = new chanceClass(chanceWithValueDO.getShouFeiType(), chanceWithValueDO.getFuFeiType(),chanceWithValueDO.getUsername(),chanceWithValueDO.getTitle(),chanceWithValueDO.getText(),chanceWithValueDO.getId(),chanceWithValueDO.getShouFei(),chanceWithValueDO.getFuFei(),chanceWithValueDO.getTag(),chanceWithValueDO.getTime(),chanceWithValueDO.getRenShu());
        if(chanceWithValueDO.getProfilePicture()!=null){
            cc.settImg(chanceWithValueDO.getProfilePicture());
        }
        if(chanceWithValueDO.getPictures()!=null){
            cc.setPicture(chanceWithValueDO.getPictures());
        }
        if(chanceWithValueDO.getShared()!=null){
            cc.setShare(chanceWithValueDO.getShared());
        }
        if(chanceWithValueDO.getLiked()!=null){
            cc.setLiked(chanceWithValueDO.getLiked());
        }
        if(chanceWithValueDO.getCommentIdList()!=null){
            int cTotal = chanceWithValueDO.getCommentIdList().size();
            Log.d("showTot",String.valueOf(cTotal));
            cc.setcNumber((double) chanceWithValueDO.getCommentIdList().size());
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
        cList.add(cc);
    }

}
