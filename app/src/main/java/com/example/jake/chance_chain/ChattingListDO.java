package com.example.jake.chance_chain;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "chance-mobilehub-653619147-chattingList")

public class ChattingListDO {
    private String _user1;
    private String _user2;
    private List<String> _chattingText = new ArrayList<>();
    private List<String> _chattingTime = new ArrayList<>();
    private List<String> _srList = new ArrayList<>();


    @DynamoDBHashKey(attributeName = "user1")
    @DynamoDBAttribute(attributeName = "user1")
    public String getUser1() {
        return _user1;
    }

    public void setUser1(final String _user1) {
        this._user1 = _user1;
    }
    @DynamoDBRangeKey(attributeName = "user2")
    @DynamoDBAttribute(attributeName = "user2")
    public String getUser2() {
        return _user2;
    }

    public void setUser2(final String _user2) {
        this._user2 = _user2;
    }
    @DynamoDBAttribute(attributeName = "chattingText")
    public List<String> getChattingText() {
        return _chattingText;
    }
    public void addText(String text){this._chattingText.add(text);}

    public void setChattingText(final List<String> _chattingText) {
        this._chattingText = _chattingText;
    }
    @DynamoDBAttribute(attributeName = "chattingTime")
    public List<String> getChattingTime() {
        return _chattingTime;
    }
    public void addTime(String time){this._chattingTime.add(time);}
    public void setChattingTime(final List<String> _chattingTime) {
        this._chattingTime = _chattingTime;
    }
    @DynamoDBAttribute(attributeName = "srList")
    public List<String> getSrList() {
        return _srList;
    }
    public void addSr(String sr){this._srList.add(sr);}
    public void setSrList(final List<String> _srList) {
        this._srList = _srList;
    }


}
