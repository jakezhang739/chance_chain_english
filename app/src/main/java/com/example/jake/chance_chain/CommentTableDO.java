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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-commentTable")

public class CommentTableDO {
    private String _commentId;
    private String _chanceId;
    private String _commentText;
    private String _upTime;
    private String _userId;
    private String _userPic;

    @DynamoDBHashKey(attributeName = "commentId")
    @DynamoDBAttribute(attributeName = "commentId")
    public String getCommentId() {
        return _commentId;
    }

    public void setCommentId(final String _commentId) {
        this._commentId = _commentId;
    }
    @DynamoDBAttribute(attributeName = "chanceId")
    public String getChanceId() {
        return _chanceId;
    }

    public void setChanceId(final String _chanceId) {
        this._chanceId = _chanceId;
    }
    @DynamoDBAttribute(attributeName = "commentText")
    public String getCommentText() {
        return _commentText;
    }

    public void setCommentText(final String _commentText) {
        this._commentText = _commentText;
    }
    @DynamoDBAttribute(attributeName = "upTime")
    public String getUpTime() {
        return _upTime;
    }

    public void setUpTime(final String _upTime) {
        this._upTime = _upTime;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "userPic")
    public String getUserPic() {
        return _userPic;
    }

    public void setUserPic(final String _userPic) {
        this._userPic = _userPic;
    }

}