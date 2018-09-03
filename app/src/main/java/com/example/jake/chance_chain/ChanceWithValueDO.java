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

@DynamoDBTable(tableName = "chance-mobilehub-653619147-ChanceWithValue")

public class ChanceWithValueDO {
    private String _id;
    private List<String> _commentIdList;
    private List<String> _completeList;
    private List<String> _confirmList;
    private Double _fuFei;
    private String _fuFeiType;
    private List<String> _getList;
    private List<String> _liked;
    private List<String> _pictures;
    private String _profilePicture;
    private Double _renShu;
    private Double _shared;
    private List<String> _sharedFrom;
    private Double _shouFei;
    private String _shouFeiType;
    private Double _tag;
    private String _text;
    private Double _time;
    private String _title;
    private List<String> _unConfirmList;
    private String _username;

    @DynamoDBHashKey(attributeName = "Id")
    @DynamoDBAttribute(attributeName = "Id")
    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }
    @DynamoDBAttribute(attributeName = "commentIdList")
    public List<String> getCommentIdList() {
        return _commentIdList;
    }

    public void setCommentIdList(final List<String> _commentIdList) {
        this._commentIdList = _commentIdList;
    }
    @DynamoDBAttribute(attributeName = "completeList")
    public List<String> getCompleteList() {
        return _completeList;
    }

    public void setCompleteList(final List<String> _completeList) {
        this._completeList = _completeList;
    }
    @DynamoDBAttribute(attributeName = "confirmList")
    public List<String> getConfirmList() {
        return _confirmList;
    }

    public void setConfirmList(final List<String> _confirmList) {
        this._confirmList = _confirmList;
    }
    @DynamoDBAttribute(attributeName = "fuFei")
    public Double getFuFei() {
        return _fuFei;
    }

    public void setFuFei(final Double _fuFei) {
        this._fuFei = _fuFei;
    }
    @DynamoDBAttribute(attributeName = "fuFei_type")
    public String getFuFeiType() {
        return _fuFeiType;
    }

    public void setFuFeiType(final String _fuFeiType) {
        this._fuFeiType = _fuFeiType;
    }
    @DynamoDBAttribute(attributeName = "getList")
    public List<String> getGetList() {
        return _getList;
    }

    public void setGetList(final List<String> _getList) {
        this._getList = _getList;
    }
    @DynamoDBAttribute(attributeName = "liked")
    public List<String> getLiked() {
        return _liked;
    }

    public void setLiked(final List<String> _liked) {
        this._liked = _liked;
    }
    @DynamoDBAttribute(attributeName = "pictures")
    public List<String> getPictures() {
        return _pictures;
    }

    public void setPictures(final List<String> _pictures) {
        this._pictures = _pictures;
    }
    @DynamoDBAttribute(attributeName = "profile_picture")
    public String getProfilePicture() {
        return _profilePicture;
    }

    public void setProfilePicture(final String _profilePicture) {
        this._profilePicture = _profilePicture;
    }
    @DynamoDBAttribute(attributeName = "renShu")
    public Double getRenShu() {
        return _renShu;
    }

    public void setRenShu(final Double _renShu) {
        this._renShu = _renShu;
    }
    @DynamoDBAttribute(attributeName = "shared")
    public Double getShared() {
        return _shared;
    }

    public void setShared(final Double _shared) {
        this._shared = _shared;
    }
    @DynamoDBAttribute(attributeName = "sharedFrom")
    public List<String> getSharedFrom() {
        return _sharedFrom;
    }

    public void setSharedFrom(final List<String> _sharedFrom) {
        this._sharedFrom = _sharedFrom;
    }
    @DynamoDBAttribute(attributeName = "shouFei")
    public Double getShouFei() {
        return _shouFei;
    }

    public void setShouFei(final Double _shouFei) {
        this._shouFei = _shouFei;
    }
    @DynamoDBAttribute(attributeName = "shouFei_type")
    public String getShouFeiType() {
        return _shouFeiType;
    }

    public void setShouFeiType(final String _shouFeiType) {
        this._shouFeiType = _shouFeiType;
    }
    @DynamoDBAttribute(attributeName = "tag")
    public Double getTag() {
        return _tag;
    }

    public void setTag(final Double _tag) {
        this._tag = _tag;
    }
    @DynamoDBAttribute(attributeName = "text")
    public String getText() {
        return _text;
    }

    public void setText(final String _text) {
        this._text = _text;
    }
    @DynamoDBAttribute(attributeName = "time")
    public Double getTime() {
        return _time;
    }

    public void setTime(final Double _time) {
        this._time = _time;
    }
    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return _title;
    }

    public void setTitle(final String _title) {
        this._title = _title;
    }
    @DynamoDBAttribute(attributeName = "unConfirmList")
    public List<String> getUnConfirmList() {
        return _unConfirmList;
    }

    public void setUnConfirmList(final List<String> _unConfirmList) {
        this._unConfirmList = _unConfirmList;
    }
    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }

}
