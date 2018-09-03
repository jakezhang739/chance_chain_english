package com.example.jake.chance_chain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<chanceClass> cList = new ArrayList<chanceClass>();
    private Context mContext;
    private String cid;
    private DynamoDBMapper dynamoDBMapper;
    private AppHelper helper = new AppHelper();
    private String myUser;



    public GalleryAdapter(Context context, List<chanceClass> cc)
    {
        this.mInflater = LayoutInflater.from(context);
        this.cList = cc;
        this.mContext = context;
        this.cid = "";
        this.dynamoDBMapper = helper.getMapper(context);
        this.myUser = helper.getCurrentUserName(context);


    }




    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView uImg,tagView,moreContent,fent;
        TextView mTxt,uidTxt,timeTxt,dianzhan,fenxiang,pingjia,fenTitle,fenUsr;
        GridView mGridview;
        RelativeLayout link;
        ProgressBar loading;
        CardView cardView;


    }



    @Override
    public int getItemViewType(int position){
        return position ;
    }

    @Override
    public int getItemCount()
    {
        return cList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view;
        ViewHolder viewHolder;
        if(cList.get(i).shareLink.size()==0) {
            view = mInflater.inflate(R.layout.item,
                    viewGroup, false);
            viewHolder = new ViewHolder(view);
            viewHolder.mTxt=(TextView) view.findViewById(R.id.neirongTxt);
            viewHolder.uImg=(ImageView) view.findViewById(R.id.touxiangImg);
            viewHolder.uidTxt=(TextView) view.findViewById(R.id.userNameText);
            viewHolder.timeTxt=(TextView) view.findViewById(R.id.timeview);
            viewHolder.tagView=(ImageView) view.findViewById(R.id.tagView);
            viewHolder.mGridview = (GridView) view.findViewById(R.id.gallery);
            viewHolder.moreContent = (ImageView) view.findViewById(R.id.gengduo);
            viewHolder.cardView = (CardView) view.findViewById(R.id.card_view);
            viewHolder.pingjia = (TextView) view.findViewById(R.id.liuyan);
            viewHolder.fenxiang = (TextView) view.findViewById(R.id.fenxiang);
            viewHolder.dianzhan = (TextView) view.findViewById(R.id.dianzhan);

            Log.d("gallery adapter","v "+String.valueOf(i));
        }
        else {
            view = mInflater.inflate(R.layout.sharingitem,
                    viewGroup, false);
            viewHolder = new ViewHolder(view);
            viewHolder.mTxt=(TextView) view.findViewById(R.id.sneirongTxt);
            viewHolder.uImg=(ImageView) view.findViewById(R.id.stouxiangImg);
            viewHolder.uidTxt=(TextView) view.findViewById(R.id.suserNameText);
            viewHolder.timeTxt=(TextView) view.findViewById(R.id.stimeview);
            viewHolder.fent = (ImageView) view.findViewById(R.id.fenTou);
            viewHolder.fenTitle = (TextView) view.findViewById(R.id.titleTxt);
            viewHolder.fenUsr = (TextView) view.findViewById(R.id.UserNameAt);
            viewHolder.pingjia = (TextView) view.findViewById(R.id.liuyan);
            viewHolder.fenxiang = (TextView) view.findViewById(R.id.fenxiang);
            viewHolder.dianzhan = (TextView) view.findViewById(R.id.dianzhan);
            viewHolder.link = (RelativeLayout) view.findViewById(R.id.shareLink);
            viewHolder.loading = (ProgressBar) view.findViewById(R.id.waitingbar);
        }


        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {

        if(cList.get(i).shareLink.size()==0) {
            viewHolder.mTxt.setText(cList.get(i).txtTitle);
            viewHolder.uidTxt.setText(cList.get(i).userid);
            String display = displayTime(String.valueOf((long) cList.get(i).uploadTime));
            viewHolder.timeTxt.setText(display);
            switch ((int) cList.get(i).tag) {
                case 1:
                    viewHolder.tagView.setImageResource(R.drawable.huodong);
                    break;
                case 2:
                    viewHolder.tagView.setImageResource(R.drawable.yuema);
                    break;
                case 3:
                    viewHolder.tagView.setImageResource(R.drawable.remwu);
                    break;
                case 4:
                    viewHolder.tagView.setImageResource(R.drawable.qita);
                    break;
            }
            viewHolder.pingjia.setText(String.valueOf(cList.get(i).cNumber));
            if (!cList.get(i).touUri.isEmpty()) {
                Picasso.get().load(cList.get(i).touUri).into(viewHolder.uImg);
            }
            if (cList.get(i).imageSet.size() != 0) {
                ImageAdapter imageAdapter = new ImageAdapter(mContext,cList.get(i).imageSet);
                int adprow = cList.get(i).imageSet.size()/4;;
                if(cList.get(i).imageSet.size()%4>0){
                    adprow++;
                }
                ViewGroup.LayoutParams params = viewHolder.mGridview.getLayoutParams();
                params.height=250*adprow;
                viewHolder.mGridview.setLayoutParams(params);
                viewHolder.mGridview.setAdapter(imageAdapter);
            }
            if (cList.get(i).liked.size() != 0) {
                viewHolder.dianzhan.setText(String.valueOf(cList.get(i).liked.size()));
            }
            if (cList.get(i).shared != 0) {
                viewHolder.fenxiang.setText(String.valueOf(cList.get(i).shared));
            }

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ContentActivity.class);
                        intent.putExtra("cc", cList.get(i));
                        intent.putExtra("comment","false");
                        v.getContext().startActivity(intent);

                }
            });
            viewHolder.uImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clistid user",cList.get(i).userid+myUser);
                    if (!cList.get(i).userid.equals(myUser)) {
                        Intent intent = new Intent(v.getContext(), HisActivity.class);
                        intent.putExtra("userName", cList.get(i).userid);
                        v.getContext().startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(v.getContext(),MyActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }
        else{
            viewHolder.mTxt.setText(cList.get(i).txtTitle);
            viewHolder.uidTxt.setText(cList.get(i).userid);
            String display = displayTime(String.valueOf((long) cList.get(i).uploadTime));
            viewHolder.timeTxt.setText(display);
            viewHolder.pingjia.setText(String.valueOf(cList.get(i).cNumber));
            if (cList.get(i).liked.size() != 0) {
                viewHolder.dianzhan.setText(String.valueOf(cList.get(i).liked.size()));
            }
            if (cList.get(i).shared != 0) {
                Log.d("i need this",String.valueOf(cList.get(i).shared));
                viewHolder.fenxiang.setText(String.valueOf(cList.get(i).shared));
            }
            if (!cList.get(i).touUri.isEmpty()) {
                Picasso.get().load(cList.get(i).touUri).into(viewHolder.uImg);
            }
            if(cList.get(i).shareLink.size()==4){
                Picasso.get().load(cList.get(i).shareLink.get(3)).into(viewHolder.fent);
            }
            viewHolder.fenUsr.setText(cList.get(i).shareLink.get(1));
            viewHolder.fenTitle.setText(cList.get(i).shareLink.get(2));
            viewHolder.uImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clistid user",cList.get(i).userid+myUser);
                    if (!cList.get(i).userid.equals(myUser)) {
                        Intent intent = new Intent(v.getContext(), HisActivity.class);
                        intent.putExtra("userName", cList.get(i).userid);
                        v.getContext().startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(v.getContext(),MyActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });

            viewHolder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clistid user",cList.get(i).userid+myUser);
                    cid = cList.get(i).shareLink.get(0);
                    viewHolder.loading.setVisibility(View.VISIBLE);
                    new Thread(shareLink).start();
                }
            });
        }



    }

    Handler mHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
      if(msg.what==1){
          Intent intent = new Intent(mContext,ContentActivity.class);
          chanceClass cc = (chanceClass) msg.getData().getParcelable("share");
          intent.putExtra("cc",cc);
          intent.putExtra("comment","false");
          mContext.startActivity(intent);
      }
      }

    };

    Runnable shareLink = new Runnable() {
        @Override
        public void run() {
            ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(cid));
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
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("share",cc);
            msg.setData(bundle);
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };

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
