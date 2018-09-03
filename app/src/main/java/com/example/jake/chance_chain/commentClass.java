package com.example.jake.chance_chain;

import android.os.Parcel;
import android.os.Parcelable;

public class commentClass implements Parcelable{
    public String commentId;
    public String upTime;
    public String ccId;
    public String cText;
    public String uId;
    public String uPic;

    public  commentClass(String cid,String uTime,String ccId,String Text, String _uId){
        this.commentId = cid;
        this.upTime=uTime;
        this.ccId=ccId;
        this.cText = Text;
        this.uId = _uId;
        this.uPic = "";
    }

    protected commentClass(Parcel in) {
        commentId = in.readString();
        upTime = in.readString();
        ccId = in.readString();
        cText = in.readString();
        uId = in.readString();
        uPic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentId);
        dest.writeString(upTime);
        dest.writeString(ccId);
        dest.writeString(cText);
        dest.writeString(uId);
        dest.writeString(uPic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<commentClass> CREATOR = new Creator<commentClass>() {
        @Override
        public commentClass createFromParcel(Parcel in) {
            return new commentClass(in);
        }

        @Override
        public commentClass[] newArray(int size) {
            return new commentClass[size];
        }
    };

    public void setUpic(String _uPic){
        this.uPic=_uPic;
    }

}
