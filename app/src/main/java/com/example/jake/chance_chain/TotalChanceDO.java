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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-TotalChance")

public class TotalChanceDO {
    private String _totChanceId;
    private String _totC;

    @DynamoDBHashKey(attributeName = "TotChanceId")
    @DynamoDBAttribute(attributeName = "TotChanceId")
    public String getTotChanceId() {
        return _totChanceId;
    }

    public void setTotChanceId(final String _totChanceId) {
        this._totChanceId = _totChanceId;
    }
    @DynamoDBAttribute(attributeName = "totC")
    public String getTotC() {
        return _totC;
    }

    public void setTotC(final String _totC) {
        this._totC = _totC;
    }

}
