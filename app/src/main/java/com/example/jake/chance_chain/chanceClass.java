package com.example.jake.chance_chain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.ArraySet;

import com.nostra13.universalimageloader.utils.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class chanceClass implements Parcelable{

    public List<String> imageSet;
    public double shoufei,fufei,uploadTime,tag,renshu;
    int shared,cNumber;
    public String touUri, sType,fType,userid,txtTitle,txtNeirong,cId;
    public List<String> liked;
    public List<String> commentId;
    public List<commentClass> commentList;
    public List<String> shareLink;
    public List<String> gottenId;
    public List<String> completeList;
    public List<String> confirmList;
    public List<String> unConfirmList;

//    public chanceClass(){
//        this.touUri="";
//        this.cNumber=0;
//        this.shared=0;
//        this.imageSet=  new ArrayList<>();
//        this.liked = new ArraySet<>();
//        this.commentSet=  new ArrayMap<>();
//    }




    public chanceClass(String sType,String fType,String userid,String txtTitle,String txtNeirong,String cId, double shoufei, double fufei,  double tag, double uploadTime,double renshu){
        this.touUri="";
        this.cNumber=0;
        this.shared=0;
        this.imageSet=  new ArrayList<>();
        this.sType=sType;
        this.fType=fType;
        this.userid = userid;
        this.txtTitle = txtTitle;
        this.txtNeirong = txtNeirong;
        this.cId = cId;
        this.shoufei = shoufei;
        this.fufei = fufei;
        this.uploadTime = uploadTime;
        this.tag = tag;
        this.liked = new ArrayList<>();
        this.commentId = new ArrayList<>();
        this.commentList=new ArrayList<>();
        this.shareLink = new ArrayList<>();
        this.gottenId=new ArrayList<>();
        this.completeList=new ArrayList<>();
        this.confirmList=new ArrayList<>();
        this.unConfirmList=new ArrayList<>();
        this.renshu = renshu;

    }

    protected chanceClass(Parcel in) {
        imageSet = in.createStringArrayList();
        shoufei = in.readDouble();
        fufei = in.readDouble();
        uploadTime = in.readDouble();
        tag = in.readDouble();
        renshu = in.readDouble();
        shared = in.readInt();
        cNumber = in.readInt();
        touUri = in.readString();
        sType = in.readString();
        fType = in.readString();
        userid = in.readString();
        txtTitle = in.readString();
        txtNeirong = in.readString();
        cId = in.readString();
        liked = in.createStringArrayList();
        commentId = in.createStringArrayList();
        commentList = in.createTypedArrayList(commentClass.CREATOR);
        shareLink = in.createStringArrayList();
        gottenId = in.createStringArrayList();
        completeList = in.createStringArrayList();
        confirmList = in.createStringArrayList();
        unConfirmList = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(imageSet);
        dest.writeDouble(shoufei);
        dest.writeDouble(fufei);
        dest.writeDouble(uploadTime);
        dest.writeDouble(tag);
        dest.writeDouble(renshu);
        dest.writeInt(shared);
        dest.writeInt(cNumber);
        dest.writeString(touUri);
        dest.writeString(sType);
        dest.writeString(fType);
        dest.writeString(userid);
        dest.writeString(txtTitle);
        dest.writeString(txtNeirong);
        dest.writeString(cId);
        dest.writeStringList(liked);
        dest.writeStringList(commentId);
        dest.writeTypedList(commentList);
        dest.writeStringList(shareLink);
        dest.writeStringList(gottenId);
        dest.writeStringList(completeList);
        dest.writeStringList(confirmList);
        dest.writeStringList(unConfirmList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<chanceClass> CREATOR = new Creator<chanceClass>() {
        @Override
        public chanceClass createFromParcel(Parcel in) {
            return new chanceClass(in);
        }

        @Override
        public chanceClass[] newArray(int size) {
            return new chanceClass[size];
        }
    };

    public void setPicture(List<String> imgSet){
        this.imageSet=imgSet;
    }
    public void settImg(String tou){
        this.touUri=tou;
    }
    public void setLiked(List<String> like){this.liked=like;}
    public void setShare (double share){ this.shared = (int) share; }
    public void setcNumber(double number){this.cNumber = (int) number;}
    public void setCid(List<String> cId){this.commentId = cId;}
    public void addLiked(String user){this.liked.add(user);}
    public void deleteLike (String user){ this.liked.remove(user);}
    public void addComList (commentClass comL){this.commentList.add(comL);}
    public void addComId (String comId){this.commentId.add(comId);}
    public void setSharfrom (List<String> shareLink){this.shareLink=shareLink;}
    public void setGetList (List<String> getList){this.gottenId=getList;}
    public void setCompleteList (List<String> complete){this.completeList=complete;}


}
