package com.example.jake.chance_chain;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "chance-mobilehub-653619147-ChattingTable")

public class ChattingTableDO {
    private Double _chatId;
    private String _pictures;
    private String _receiver;
    private String _sender;
    private String _text;
    private String _time;
    private String _unReadNum;

    @DynamoDBHashKey(attributeName = "ChatId")
    @DynamoDBAttribute(attributeName = "ChatId")
    public Double getChatId() {
        return _chatId;
    }

    public void setChatId(final Double _chatId) {
        this._chatId = _chatId;
    }
    @DynamoDBAttribute(attributeName = "Pictures")
    public String getPictures() {
        return _pictures;
    }

    public void setPictures(final String _pictures) {
        this._pictures = _pictures;
    }
    @DynamoDBIndexHashKey(attributeName = "Receiver", globalSecondaryIndexName = "FindReceiver")
    @DynamoDBIndexRangeKey(attributeName = "Receiver", globalSecondaryIndexName = "FindSender")
    public String getReceiver() {
        return _receiver;
    }

    public void setReceiver(final String _receiver) {
        this._receiver = _receiver;
    }
    @DynamoDBIndexHashKey(attributeName = "Sender", globalSecondaryIndexName = "FindSender")
    public String getSender() {
        return _sender;
    }

    public void setSender(final String _sender) {
        this._sender = _sender;
    }
    @DynamoDBAttribute(attributeName = "Text")
    public String getText() {
        return _text;
    }

    public void setText(final String _text) {
        this._text = _text;
    }
    @DynamoDBAttribute(attributeName = "Time")
    public String getTime() {
        return _time;
    }

    public void setTime(final String _time) {
        this._time = _time;
    }
    @DynamoDBIndexRangeKey(attributeName = "unReadNum", globalSecondaryIndexName = "FindReceiver")
    public String getUnReadNum() {
        return _unReadNum;
    }

    public void setUnReadNum(final String _unReadNum) {
        this._unReadNum = _unReadNum;
    }

}
