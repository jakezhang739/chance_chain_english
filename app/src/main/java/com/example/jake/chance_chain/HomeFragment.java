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


public class HomeFragment extends Fragment {

    List<String> mImage,mText,touUri,usId;
    DynamoDBMapper dynamoDBMapper;
    private boolean refreshFlag = false;
    private boolean loadmoreFlag = false;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    int tC,temptC;
    RecyclerView mRecyclerView;
    GalleryAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    int uploadOffset=-1;
    int tempOffset=-1;
    AppHelper helper = new AppHelper();
    static {
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
        ClassicsHeader.REFRESH_HEADER_SECONDARY = "释放进入二楼";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("frag","yoyoyoyoyooyoyo");
        dynamoDBMapper = AppHelper.getMapper(getContext());



        View view = inflater.inflate(R.layout.fragment_home, container, false);
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
                if(tC<=10){

                    refreshLayout.finishLoadMore(500,loadmoreFlag,true);
                }

                refreshLayout.finishLoadMore(500,loadmoreFlag,false);
            }
        });

        return view;
    }



    public void setClass(List<chanceClass> cc){
        this.cList = cc;
    }

    Runnable pullDownRunnable = new Runnable() {
        @Override
        public void run() {


            int totChance = helper.returnChanceeSize(dynamoDBMapper);



            cList.clear();

            if(totChance >= 10){
                for(int i = totChance;i>=totChance-9;i--){
                    fragPutIn(i);

                }
            }
            else{
                for(int i = totChance;i>=1;i--){
                    fragPutIn(i);

                }
            }


            refreshFlag = true;
        }
    };

    Runnable pullUpLoad = new Runnable() {
        @Override
        public void run() {

            Log.d("just try222", "come on "+helper.returnChanceeSize(dynamoDBMapper));
            if(cList.size()!=0) {

                int curChance = Integer.parseInt(cList.get(cList.size() - 1).cId) - 1;

                Log.d("jus11t try222", "come on " + curChance);

                if (curChance >= 10) {
                    for (int i = curChance; i >= curChance - 9; i--) {
                        fragPutIn(i);
                    }
                } else {
                    for (int i = curChance; i >= 1; i--) {
                        fragPutIn(i);
                    }
                }
            }
            else{

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
