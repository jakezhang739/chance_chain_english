//package com.example.jake.chance_chain;
//
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//
//import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class pulldownThread extends Thread {
//    HomeFragment homeFragment;
//    private List<String> mDatasText;
//    private List<String> mDatasImage;
//    private List<String> dTouImage;
//    DynamoDBMapper dynamoDBMapper;
//
//    public pulldownThread(DynamoDBMapper mapper, HomeFragment fg){
//        this.dynamoDBMapper=mapper;
//        this.homeFragment=fg;
//    }
//
//    public void run(){
//
//        mDatasImage = new ArrayList<String>(Arrays.asList());
//        mDatasText = new ArrayList<String>(Arrays.asList());
//        dTouImage = new ArrayList<String>(Arrays.asList());
//
//        TotalChanceDO totalChanceDO = dynamoDBMapper.load(TotalChanceDO.class,"totalID");
//        Log.d("dyna12",""+totalChanceDO.getTotC());
//
//
//        int totChance = Integer.parseInt(totalChanceDO.getTotC());
//        if(totChance > 10){
//            for(int i = totChance;i>totChance-10;i--){
//                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
//                mDatasImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(i)+".png");
//                mDatasText.add(chanceWithValueDO.getValue());
//                Log.d("dyna123","uid "+chanceWithValueDO.getUser());
//                dTouImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+chanceWithValueDO.getUser()+".png");
//
//            }
//        }
//        else{
//            for(int i = totChance;i>0;i--){
//                ChanceWithValueDO chanceWithValueDO = dynamoDBMapper.load(ChanceWithValueDO.class,String.valueOf(i));
//                mDatasImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+String.valueOf(i)+".png");
//                mDatasText.add(chanceWithValueDO.getValue());
//                Log.d("dyna123","uid "+chanceWithValueDO.getUser());
//                dTouImage.add("https://s3.amazonaws.com/chance-userfiles-mobilehub-653619147/"+chanceWithValueDO.getUser()+".png");
//
//            }
//        }
//        homeFragment.setData(mDatasText,mDatasImage,dTouImage);
//    }
//
//
//}
