package com.example.jake.chance_chain;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "chance-mobilehub-653619147-userChat")

public class UserChatDO {
    private String _userId;
    private List<String> _chattingList = new ArrayList<>();
    private Map<String, String> _lastSentence = new HashMap<>();
    private Map<String, String> _lastTime = new HashMap<>();
    private Double _totalUnread = 0.0;
    private Map<String, String> _unRead = new HashMap<>();

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "chattingList")
    public List<String> getChattingList() {
        return _chattingList;
    }

    public void removeUsr(String str){this._chattingList.remove(str);}

    public void addChatting(String str){this._chattingList.add(str);}

    public void setChattingList(final List<String> _chattingList) {
        this._chattingList = _chattingList;
    }
    @DynamoDBAttribute(attributeName = "lastSentence")
    public Map<String, String> getLastSentence() {
        return _lastSentence;
    }

    public void addSentence(String str1,String str2){this._lastSentence.put(str1,str2);}

    public void setLastSentence(final Map<String, String> _lastSentence) {
        this._lastSentence = _lastSentence;
    }
    @DynamoDBAttribute(attributeName = "lastTime")
    public Map<String, String> getLastTime() {
        return _lastTime;
    }

    public void addTime(String str1,String str2){
        this._lastTime.put(str1,str2);
    }

    public void setLastTime(final Map<String, String> _lastTime) {
        this._lastTime = _lastTime;
    }
    @DynamoDBAttribute(attributeName = "totalUnread")
    public Double getTotalUnread() {
        return _totalUnread;
    }

    public void addTotal(){this._totalUnread++;}

    public void setTotalUnread(final Double _totalUnread) {
        this._totalUnread = _totalUnread;
    }
    @DynamoDBAttribute(attributeName = "unRead")
    public Map<String, String> getUnRead() {
        return _unRead;
    }


    public void addUnread(String str1){
        if(this._unRead.containsKey(str1)){
            int num = Integer.parseInt(this._unRead.get(str1));
            num++;
            this._unRead.put(str1,String.valueOf(num));
        }
        else {
            this._unRead.put(str1,"1");
        }
    }

    public void setUnRead(final Map<String, String> _unRead) {
        this._unRead = _unRead;
    }

}
