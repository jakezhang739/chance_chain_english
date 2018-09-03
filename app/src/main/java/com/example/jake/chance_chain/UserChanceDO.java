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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-UserChance")

public class UserChanceDO {
    private String _chanceid;
    private String _userid;
    private String _numid;
    private String _value;

    @DynamoDBHashKey(attributeName = "chanceid")
    @DynamoDBAttribute(attributeName = "chanceid")
    public String getChanceid() {
        return _chanceid;
    }

    public void setChanceid(final String _chanceid) {
        this._chanceid = _chanceid;
    }
    @DynamoDBRangeKey(attributeName = "userid")
    @DynamoDBAttribute(attributeName = "userid")
    public String getUserid() {
        return _userid;
    }

    public void setUserid(final String _userid) {
        this._userid = _userid;
    }
    @DynamoDBAttribute(attributeName = "numid")
    public String getNumid() {
        return _numid;
    }

    public void setNumid(final String _numid) {
        this._numid = _numid;
    }
    @DynamoDBAttribute(attributeName = "value")
    public String getValue() {
        return _value;
    }

    public void setValue(final String _value) {
        this._value = _value;
    }

}
